package uai.diploma.tique.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import uai.diploma.tique.R;
import uai.diploma.tique.util.Constantes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;


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
    private static final int RC_SIGN_IN = 1;
    SignInButton signInButton;
    MaterialButton sign_out;
    TextView tvCuenta, tvMail;

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

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        Constantes.GOOGLE_SIGN_IN = mGoogleSignInClient;
        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        //updateUI(account);
        loading = getActivity().findViewById(R.id.loadingPanel);
        loading.setVisibility(View.VISIBLE);

        signInButton = rootview.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        sign_out = rootview.findViewById(R.id.sign_out);
        sign_out.setOnClickListener(this);

        tvCuenta = rootview.findViewById(R.id.tvCuenta);
        tvMail = rootview.findViewById(R.id.tvMail);
        return rootview;
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
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out:
                signOut();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Constantes.LOG_NAME, "requestCode:" + requestCode + " resultCode:" + resultCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w(Constantes.LOG_NAME, "Google sign in failed", e);
            }

        } else {
            //loading.setVisibility(View.GONE);
        }
    }

    private void handleSignInResult(GoogleSignInAccount completedTask) {

        Log.d(Constantes.LOG_NAME, "FirebaseAuthWithGoogle ID:" + completedTask.getId());
        Log.d(Constantes.LOG_NAME, "FirebaseAuthWithGoogle Token:" + completedTask.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(completedTask.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constantes.LOG_NAME, "signInWithCredential:success ");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //completedTask.getPhotoUrl();
                            user.getPhotoUrl();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(rootview.findViewById(R.id.sign_in_button), "Fallo de autentificación", Snackbar.LENGTH_SHORT).show();
                            Log.i(Constantes.LOG_NAME, "signInResult:failed code=" + task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(final FirebaseUser account) {


        if (account != null) {

            account.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                Constantes.token = task.getResult().getToken();
                                //myEditor.putString("TOKEN", task.getResult().getToken());
                                //myEditor.commit();
                                //getSharedPreferences("_", MODE_PRIVATE).edit().putString("TOKEN", task.getResult().getToken()).apply();

                            } else {
                                Constantes.token = "NO hay token";
                            }

                            Log.i(Constantes.LOG_NAME, "Cuenta seleccionada " + account.getDisplayName());
                            Log.i(Constantes.LOG_NAME, Constantes.token);

                            //callServiceLogin(account);
                            //Toast.makeText(getContext(), "Cuenta seleccionada " + account.getDisplayName(), Toast.LENGTH_LONG).show();
                            tvCuenta.setText(account.getDisplayName().toString());
                            tvMail.setText(account.getEmail().toString());
                            loading.setVisibility(View.GONE);
                            signInButton.setVisibility(View.GONE);
                            sign_out.setVisibility(View.VISIBLE);

                        }
                    });

        } else {
            //Controlar is expired
            signInButton.setVisibility(View.VISIBLE);
            Log.i(Constantes.LOG_NAME, "La cuenta es null");
            tvCuenta.setText("");
            tvMail.setText("");
            loading.setVisibility(View.GONE);

            signInButton.setVisibility(View.VISIBLE);
            sign_out.setVisibility(View.GONE);
        }
    }

    protected void signOut() {
        Log.i(Constantes.LOG_NAME, "Log Out");

        FirebaseAuth.getInstance().signOut();

        if (Constantes.GOOGLE_SIGN_IN != null) {
            // Google sign out
            Constantes.GOOGLE_SIGN_IN.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

/*
                            JSONObject jsonBody = new JSONObject();

                            try {
                                jsonBody.put("Email", sUsuairo.getEmail());
                                jsonBody.put("Token_Disp", sUsuairo.getTokenDevice());

                                if(cs == null ) {
                                    cs = new CallWebService(loadingPanel, errorPanel, null, recyclerView);
                                }
                                cs.callService(getApplicationContext(), Constantes.WS_LOGOUT, null, Constantes.M_POST, jsonBody);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            */
                        }
                    });
        }
        updateUI(null);

    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


}
