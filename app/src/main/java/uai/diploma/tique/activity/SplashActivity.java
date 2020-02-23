package uai.diploma.tique.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import io.fabric.sdk.android.Fabric;
import uai.diploma.tique.R;
import uai.diploma.tique.adapter.AdapterCategorias;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.SingletonCategorias;
import uai.diploma.tique.modelo.SingletonUsuario;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_LONG;

public class SplashActivity extends AppCompatActivity {

    static Context context;
    static View img_logo;
    private final Handler mHideHandler = new Handler();

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            //mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        this.context = this;
        img_logo = findViewById(R.id.img_logo);
        getData();
/*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Snackbar.make(findViewById(R.id.img_logo), "Esto es una prueba", Snackbar.LENGTH_LONG).show();

            }
        }, 2000);*/
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }


    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private void getData() {

        try {

            String partialUrl = Constantes.WS_CATEGORIAS;
            WebService webService = new WebService(this, null);
            webService.callService(partialUrl, null, Request.Method.GET, Constantes.R_ARRAY, null);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onResult(JSONArray response, String partialUrl) {

        JSONArray responseJson = null;
        try {
            responseJson = new JSONArray(response.toString());

            ArrayList<Categorias> categorias = new ArrayList<>();

            for (int i = 0; i < responseJson.length(); i++) {

                Log.d(Constantes.LOG_NAME, responseJson.get(i).toString());
                JSONObject dataItem = responseJson.getJSONObject(i);
                categorias.add(new Categorias(dataItem, false));
            }

            if (categorias.size() > 0){
                SingletonCategorias cat = SingletonCategorias.getInstance();
                cat.setCategorias(categorias);

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                ((SplashActivity)context).finish();
            }else{
                Snackbar.make(img_logo, "No se encontraron categorías", LENGTH_LONG);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    public void onWebServiceResult(ArrayList<Categorias> categorias){


        if (lcategorias.size() > 0) {
            Log.i(Constantes.LOG_NAME, "Se encontraron " + lcategorias.size() + " resultados");
            adapter = new AdapterCategorias(getContext(), lcategorias);
            recyclerView.setAdapter(adapter);

        }else{
            Log.i(Constantes.LOG_NAME, "No se encontró resultado");
        }

        loading.setVisibility(View.GONE);
    }*/
}
