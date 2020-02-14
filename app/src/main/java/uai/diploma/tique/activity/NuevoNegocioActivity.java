package uai.diploma.tique.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Console;
import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import uai.diploma.tique.R;
import uai.diploma.tique.listener.oclAdjuntarImagen;
import uai.diploma.tique.listener.onClickListenerCategoria;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modulo.moduloProcesarImagen;
import uai.diploma.tique.util.Constantes;

import static java.security.AccessController.getContext;

public class NuevoNegocioActivity extends AppCompatActivity {

    private static final int GALLERY_PO = 1, GALLERY_PE = 3, GALLERY_IA = 5;
    private static final int CAMERA_PO = 2, CAMERA_PE = 4, CAMERA_IA = 6;
    Context context;
    LinearLayout llImagenes;
    TextView tvCountImage, tvHoraDesde, tvHoraHasta;
    ImageView imgPortada, imgPerfil, imgFondo, fabBorrarFotoPerfil, fabBorrarFotoPortada;
    moduloProcesarImagen mpi;
    //RadioGroup rgCategoria;
    FloatingActionButton fabFotoPortada, fabFotoPerfil;
    ImageButton btnCamera;
    MaterialButton tbSubcategoria;
    SeekBar sbDesde, sbHasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_negocio);

        this.context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llImagenes = findViewById(R.id.llImagenes);
        tvCountImage = findViewById(R.id.tvCountImage);
        imgPortada = findViewById(R.id.imgPortada);
        imgPerfil = findViewById(R.id.imgPerfil);
        imgFondo = findViewById(R.id.imgFondo);
        fabBorrarFotoPortada = findViewById(R.id.fabBorrarFotoPortada);
        fabBorrarFotoPerfil = findViewById(R.id.fabBorrarFotoPerfil);
        //rgCategoria = findViewById(R.id.rgCategoria);

        fabFotoPortada = findViewById(R.id.fabFotoPortada);
        fabFotoPerfil = findViewById(R.id.fabFotoPerfil);
        btnCamera = findViewById(R.id.btnCamera);

        tbSubcategoria = findViewById(R.id.tbSubcategoria);
        tvHoraDesde = findViewById(R.id.tvHoraDesde);
        tvHoraHasta = findViewById(R.id.tvHoraHasta);
        sbDesde = findViewById(R.id.sbDesde);
        sbHasta = findViewById(R.id.sbHasta);

        //Seteo listeners

        fabFotoPortada.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PO, CAMERA_PO, false));
        fabFotoPerfil.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PE, CAMERA_PE, false));
        btnCamera.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_IA, CAMERA_IA, true));

        fabBorrarFotoPortada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                borrarImagenDisp((String) imgPortada.getTag());
                imgPortada.setVisibility(View.GONE);
                imgFondo.setVisibility(View.VISIBLE);
                fabBorrarFotoPortada.setVisibility(View.GONE);
            }
        });

        fabBorrarFotoPerfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                borrarImagenDisp((String) imgPerfil.getTag());
                imgPerfil.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.perfil));
                fabBorrarFotoPerfil.setVisibility(View.GONE);
            }
        });
/*
        rgCategoria.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //recargarSubcategorias(checkedId);
            }
        });*/

        tbSubcategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        sbDesde.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHoraDesde.setText(progress+":00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        sbHasta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHoraHasta.setText(progress+":00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private void borrarImagenDisp(String tag) {
        if(tag != null) {
            File file = new File(tag);
            if (file.exists())
                file.delete();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (mpi == null) {
            mpi = new moduloProcesarImagen();
            Log.d(Constantes.LOG_NAME, "Se instancia mpi");
        }

        switch (requestCode){
            case GALLERY_PO:
            case CAMERA_PO:
                mpi.procesarImagen(requestCode, resultCode, data, context, null, null, imgPortada);
                imgPortada.setVisibility(View.VISIBLE);
                imgFondo.setVisibility(View.GONE);
                fabBorrarFotoPortada.setVisibility(View.VISIBLE);
                break;
            case GALLERY_PE:
            case CAMERA_PE:
                mpi.procesarImagen(requestCode, resultCode, data, context, null, null, imgPerfil);
                fabBorrarFotoPerfil.setVisibility(View.VISIBLE);
                break;
            case GALLERY_IA:
            case CAMERA_IA:
                mpi.procesarImagen(requestCode, resultCode, data, context, llImagenes, tvCountImage, null);
                break;
        }
    }

}
