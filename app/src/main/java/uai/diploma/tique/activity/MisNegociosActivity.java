package uai.diploma.tique.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import uai.diploma.tique.R;
import uai.diploma.tique.modelo.SingletonCategorias;
import uai.diploma.tique.modelo.SingletonUsuario;

public class MisNegociosActivity extends AppCompatActivity {

    Context ctx;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_negocios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = this;
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SingletonUsuario.getInstance(ctx).tieneRol("crear_negocio")) {

                    //Si las categorias no estan en memoria llamo al Splash
                    if(SingletonCategorias.getInstance().getCategorias()==null){
                        Intent i = new Intent(ctx, SplashActivity.class);
                        ctx.startActivity(i);
                        Toast.makeText(ctx, "Cargando categorías...", Toast.LENGTH_LONG).show();
                        ((MisNegociosActivity)ctx).finish();
                    }

                    Intent i = new Intent(ctx, NuevoNegocioActivity.class);
                    ctx.startActivity(i);
                }else{
                    Snackbar.make(((MisNegociosActivity) ctx).findViewById(R.id.fab), "No tiene permiso", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
