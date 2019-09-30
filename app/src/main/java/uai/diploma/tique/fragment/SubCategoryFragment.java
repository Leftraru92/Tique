package uai.diploma.tique.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

public class SubCategoryFragment extends Fragment implements IWebServiceFragment{

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

        getData();

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

    private void getData() {

        try {

            String partialUrl = Constantes.WS_SUBCATEGORIAS;;
            String params = null;
            JSONObject body = null;
            //params = "/" + codeCat;

            loading.setVisibility(View.VISIBLE);

            WebService webService = new WebService(getContext(), this, recyclerView);

            webService.callService(partialUrl, params, Constantes.M_GET, Constantes.R_ARRAY, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebServiceResult(ArrayList<?> categorias) {
        this.lcategorias = (ArrayList<Categorias>)categorias;
        RecyclerView.Adapter<AdapterCategorias.MyViewHolder> adapter;

        if (lcategorias.size() > 0) {
            Log.i(Constantes.LOG_NAME, "Se encontraron " + lcategorias.size() + " resultados");
            adapter = new AdapterCategorias(getContext(), lcategorias);
            recyclerView.setAdapter(adapter);

        }else{
            Log.i(Constantes.LOG_NAME, "No se encontró resultado");
        }

        loading.setVisibility(View.GONE);
    }

    @Override
    public void onWebServiceResult(Object lista) {

    }
}
