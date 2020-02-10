package uai.diploma.tique.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import uai.diploma.tique.R;
import uai.diploma.tique.modelo.SingletonUsuario;

public class MisNegociosActivity extends AppCompatActivity {

    Context ctx;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_negocios);

        ctx = this;
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SingletonUsuario.getInstance(ctx).tieneRol("crear_negocio")) {

                    Intent i = new Intent(ctx, NuevoNegocioActivity.class);
                    ctx.startActivity(i);
                }else{
                    Snackbar.make(((MisNegociosActivity) ctx).findViewById(R.id.fab), "No tiene permiso", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
