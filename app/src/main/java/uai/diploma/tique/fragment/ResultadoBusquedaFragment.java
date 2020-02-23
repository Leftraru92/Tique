package uai.diploma.tique.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uai.diploma.tique.R;
import uai.diploma.tique.adapter.AdapterCategorias;
import uai.diploma.tique.adapter.AdapterResultadoBusqueda;
import uai.diploma.tique.fragment.dummy.DummyContent.DummyItem;
import uai.diploma.tique.modelo.Servicios;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ResultadoBusquedaFragment extends Fragment implements IWebServiceFragment{

    private static final String P_SUBCATEGORIA = "codeSubCat";
    private static int codeSubCat;

    private OnListFragmentInteractionListener mListener;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    public View rootview;
    View loading;
    ArrayList<Servicios> lservicios;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultadoBusquedaFragment() {
    }

    public static ResultadoBusquedaFragment newInstance(int codeSubCat) {
        ResultadoBusquedaFragment fragment = new ResultadoBusquedaFragment();
        Bundle args = new Bundle();
        args.putInt(P_SUBCATEGORIA, codeSubCat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            codeSubCat = getArguments().getInt(P_SUBCATEGORIA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_resultado_busqueda, container, false);

        recyclerView = (RecyclerView) rootview.findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loading = rootview.findViewById(R.id.loadingPanel);

        getData();

        return rootview;
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private void getData() {

        try {

            String partialUrl = Constantes.WS_SERVICIOS;
            String params = null;
            JSONObject body = null;
            params = "/" + codeSubCat;

            loading.setVisibility(View.VISIBLE);

            WebService webService = new WebService(getContext(), this);

            webService.callService(partialUrl, params, Request.Method.GET, Constantes.R_ARRAY, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebServiceResult(ArrayList<?> servicios) {
        this.lservicios = (ArrayList<Servicios>)servicios;
        RecyclerView.Adapter<AdapterResultadoBusqueda.MyViewHolder> adapter;

        if (lservicios.size() > 0) {
            Log.i(Constantes.LOG_NAME, "Se encontraron " + lservicios.size() + " resultados");
            adapter = new AdapterResultadoBusqueda(getContext(), lservicios);
            recyclerView.setAdapter(adapter);

        }else{
            Log.i(Constantes.LOG_NAME, "No se encontró resultado");
            Snackbar.make(rootview.findViewById(R.id.my_recycler_view), "No se encontraron resultados", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            getFragmentManager().popBackStack();
        }

        loading.setVisibility(View.GONE);
    }

    @Override
    public void onWebServiceResult(Object lista) {

    }
}
