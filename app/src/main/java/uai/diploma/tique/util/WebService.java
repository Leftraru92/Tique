package uai.diploma.tique.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import uai.diploma.tique.adapter.AdapterCategorias;
import uai.diploma.tique.fragment.CategoryFragment;
import uai.diploma.tique.fragment.IWebServiceFragment;
import uai.diploma.tique.modelo.Categorias;

public class WebService {
    Context context;
    IWebServiceFragment fragment;
    RecyclerView recyclerView;

    public WebService(Context context, CategoryFragment fragment, RecyclerView recyclerView) {
        this.context = context;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
    }

    public void callService(final String partialUrl, String params, String method, JSONObject jsonBody) throws JSONException {

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constantes.WS_DOMINIO + partialUrl;

        if (params != null) {
            url = url + params;
        }

        JSONArray postparams = new JSONArray();

        Log.i(Constantes.LOG_NAME, url);


        switch (method) {
            case Constantes.M_POST:


                JsonObjectRequest stringRequestPost = new JsonObjectRequest(Request.Method.POST, url,
                        jsonBody,
                        jsonObjectListener(partialUrl, context), newErrorListener()) {

                   /* @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getToken();
                    }*/
                };

                stringRequestPost.setRetryPolicy(new DefaultRetryPolicy(
                        Constantes.DEFAULT_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                // Add the request to the RequestQueue.
                queue.add(stringRequestPost);

                break;
            case Constantes.M_GET:

                JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, postparams,
                        jsonArrayListener(partialUrl, context),
                        newErrorListener()) {
/*
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return getToken();
                    }*/
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        Constantes.DEFAULT_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(stringRequest);
                break;
        }
    }

    private Response.Listener<JSONArray> jsonArrayListener(final String partialUrl, final Context context) {
        return
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(Constantes.LOG_NAME, "Respuesta " + partialUrl + ": " + response.toString());
                        try {

                            switch (partialUrl) {
                                case Constantes.WS_CATEGORIAS:
                                    respuestaCategorias(response, context);
                                    break;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(Constantes.LOG_NAME, e.toString());
                        }
                    /*if (errorPanel != null) {
                        errorPanel.setVisibility(View.GONE);
                    }/**/
//                        loadingPanel.animate().alpha(0.0f).setDuration(500);
                        // loadingPanel.setVisibility(View.GONE);
                    }
                };
    }

    private Response.Listener<JSONObject> jsonObjectListener(final String partialUrl, Context context) {
        return
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Constantes.LOG_NAME, "Respuesta " + partialUrl + ": " + response.toString());

                        switch (partialUrl) {
                            default:
                                break;
                        }
                    }
                };
    }

    private Response.ErrorListener newErrorListener() {

        return
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        String description;
                        String message;

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.i(Constantes.LOG_NAME, error.toString());
                           // if(txtMessage != null){txtMessage.setText(R.string.error_timeout);}

                        } else {

                            try {
                                NetworkResponse networkResponse = error.networkResponse;
                                final String result = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
                                Log.i(Constantes.LOG_NAME, result);

                                JSONObject descriptionjson = new JSONObject(result);

                                if (networkResponse.statusCode == 401 || networkResponse.statusCode == 403) {
                                    description = descriptionjson.getString("Message");
                                   // Constantes.token = null;
                                } else {
                                    description = descriptionjson.getJSONObject("Message").getString("description");
                                }

                                Log.i(Constantes.LOG_NAME, networkResponse.statusCode + " " + description);
                                if (networkResponse.statusCode == 401) {
                                    //volverAlLogin(description);
                                }

                                Log.i(Constantes.LOG_NAME, description);
                               // if(txtMessage != null){txtMessage.setText(description);}

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Log.i(Constantes.LOG_NAME, e.getMessage());
                               // if(txtMessage != null){txtMessage.setText(R.string.error_inesperado);}
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(Constantes.LOG_NAME, e.getMessage());
                              //  if(txtMessage != null){txtMessage.setText(R.string.error_inesperado);}
                            }
                        }
                        /*if (errorPanel != null) {
                            errorPanel.setVisibility(View.VISIBLE);
                        }
                        if (loadingPanel != null) {
                            loadingPanel.setVisibility(View.GONE);
                        }*/
                    }
                };
    }

    private Map<String, String> getToken() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Authorization", "jwt " + Constantes.TOKEN);

        return params;
    }

    private void respuestaCategorias(JSONArray response, Context context) throws JSONException {

        JSONArray responseJson = new JSONArray(response.toString());
        ArrayList<Categorias> categorias = new ArrayList<>();


        for (int i = 0; i < responseJson.length(); i++) {

            Log.d(Constantes.LOG_NAME, responseJson.get(i).toString());
            JSONObject dataItem = responseJson.getJSONObject(i);
            categorias.add(new Categorias(dataItem));
        }

        this.fragment.onWebServiceResult(categorias);
        /*
        if (categorias.size() > 0) {
            adapter = new AdapterCategorias(categorias);
            recyclerView.setAdapter(adapter);

        }else{
            Log.i(Constantes.LOG_NAME, "No se encontró resultado");
        }*/
    }
}
