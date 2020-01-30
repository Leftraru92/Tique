package uai.diploma.tique.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import uai.diploma.tique.R;
import uai.diploma.tique.activity.LoginActivity;
import uai.diploma.tique.activity.MisNegociosActivity;
import uai.diploma.tique.modelo.SingletonUsuario;
import uai.diploma.tique.util.Constantes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CuentaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CuentaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CuentaFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootview, loading;
    private OnFragmentInteractionListener mListener;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    SignInButton signInButton;
    MaterialButton mbLogout, mbLogin, btnNuevoNegocio;
    TextView tvCuenta, tvMail, lbSaludo;

    SingletonUsuario sUsuario;

    public CuentaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CuentaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CuentaFragment newInstance(String param1, String param2) {
        CuentaFragment fragment = new CuentaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_cuenta, container, false);

        loading = getActivity().findViewById(R.id.loadingPanel);
        mbLogout = rootview.findViewById(R.id.mbLogout);
        mbLogin = rootview.findViewById(R.id.mbLogin);
        lbSaludo = rootview.findViewById(R.id.lbSaludo);
        btnNuevoNegocio = rootview.findViewById(R.id.btnNuevoNegocio);

        loading.setVisibility(View.VISIBLE);

        mbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin();
            }
        });

        mbLogout.setOnClickListener(this);

        btnNuevoNegocio.setOnClickListener(this);

        sUsuario = SingletonUsuario.getInstance(this.getContext());

        updateUI();

        return rootview;
    }

    private void callLogin() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(i);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mbLogout:
                signOut();
                break;
            case R.id.btnNuevoNegocio:
                nuevoNegocio();
        }
    }

    private void nuevoNegocio() {
        Intent i = new Intent(getContext(), MisNegociosActivity.class);
        startActivity(i);
    }

    private void updateUI() {


        if (sUsuario.getEmail() != null) {

            Log.i(Constantes.LOG_NAME, "La cuenta seleccionada es " + sUsuario.getEmail() );
            lbSaludo.setText("Hola, " + sUsuario.getDisplayName() + "!");

            loading.setVisibility(View.GONE);
            mbLogout.setVisibility(View.VISIBLE);
            mbLogin.setVisibility(View.GONE);
            btnNuevoNegocio.setVisibility(View.VISIBLE);

        } else {
            //Controlar is expired
//            signInButton.setVisibility(View.VISIBLE);
            Log.i(Constantes.LOG_NAME, "No hay cuenta seleccionada");
            lbSaludo.setText(getString(R.string.saludo));

            loading.setVisibility(View.GONE);
            mbLogout.setVisibility(View.GONE);
            mbLogin.setVisibility(View.VISIBLE);
            btnNuevoNegocio.setVisibility(View.GONE);
        }
    }

    protected void signOut() {
        Log.i(Constantes.LOG_NAME, "Log Out");

        FirebaseAuth.getInstance().signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        if (mGoogleSignInClient == null) {
            //callLogin();
        } else {
            // Google sign out
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //callLogin();
                        }
                    });
        }

        sUsuario.logout();
        updateUI();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


}
