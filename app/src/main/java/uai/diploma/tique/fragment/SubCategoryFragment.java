package uai.diploma.tique.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import uai.diploma.tique.R;
import uai.diploma.tique.activity.MainActivity;
import uai.diploma.tique.adapter.AdapterCategorias;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.SingletonCategorias;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

public class SubCategoryFragment extends Fragment{

    private static final String P_CATEGORIA = "CodeCat";
    private static int codeCat;

    private OnFragmentInteractionListener mListener;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public View rootview;
    View loading;
    ArrayList<Categorias> lcategorias;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * @param codeCat Parameter 1.
     * @return A new instance of fragment SubCategoryFragment.
     */
    public static SubCategoryFragment newInstance(int codeCat) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(P_CATEGORIA, codeCat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codeCat = getArguments().getInt(P_CATEGORIA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_sub_category, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loading = rootview.findViewById(R.id.loadingPanel);

        //getData();
        cargarSubCategorias();

        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void cargarSubCategorias(){

        SingletonCategorias cat = SingletonCategorias.getInstance();

        RecyclerView.Adapter<AdapterCategorias.MyViewHolder> adapter;

        if (cat.getCategorias(codeCat).size() > 0) {
            Log.i(Constantes.LOG_NAME, "Se encontraron " + cat.getCategorias(codeCat).size() + " resultados");
            adapter = new AdapterCategorias(getContext(), cat.getCategorias(codeCat));
            recyclerView.setAdapter(adapter);

        }else{
            Log.i(Constantes.LOG_NAME, "No se encontró resultado");
            Snackbar.make(rootview.findViewById(R.id.my_recycler_view), "No se encontraron resultados", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        loading.setVisibility(View.GONE);

    }
}
