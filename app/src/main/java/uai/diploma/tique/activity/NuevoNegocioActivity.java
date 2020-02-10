package uai.diploma.tique.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import uai.diploma.tique.R;
import uai.diploma.tique.listener.oclAdjuntarImagen;
import uai.diploma.tique.modulo.moduloProcesarImagen;
import uai.diploma.tique.util.Constantes;

import static java.security.AccessController.getContext;

public class NuevoNegocioActivity extends AppCompatActivity {

    private static final int GALLERY_PO = 1, GALLERY_PE = 3, GALLERY_IA = 5;
    private static final int CAMERA_PO = 2, CAMERA_PE = 2, CAMERA_IA = 2;
    Context context;
    LinearLayout llImagenes;
    TextView tvCountImage;
    moduloProcesarImagen mpi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_negocio);

        this.context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.fabFotoPortada).setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PO, CAMERA_PO, false));
        findViewById(R.id.fabFotoPerfil).setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PE, CAMERA_PE, false));
        findViewById(R.id.btnCamera).setOnClickListener(new oclAdjuntarImagen(this, GALLERY_IA, CAMERA_IA, true));
        llImagenes = findViewById(R.id.llImagenes);
        tvCountImage = findViewById(R.id.tvCountImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALLERY_IA || requestCode == CAMERA_IA) {
            super.onActivityResult(requestCode, resultCode, data);

            if (mpi == null) {
                mpi = new moduloProcesarImagen();
                Log.d(Constantes.LOG_NAME, "Se instancia mpi");
            }

            mpi.procesarImagen(requestCode, resultCode, data, context, llImagenes, tvCountImage);
        }
    }

}
