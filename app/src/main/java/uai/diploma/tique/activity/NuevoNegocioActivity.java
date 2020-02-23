package uai.diploma.tique.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Console;
import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import uai.diploma.tique.R;
import uai.diploma.tique.listener.oclAdjuntarImagen;
import uai.diploma.tique.listener.oclSelecSubcategoria;
import uai.diploma.tique.listener.onClickListenerCategoria;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.SingletonCategorias;
import uai.diploma.tique.modulo.moduloProcesarImagen;
import uai.diploma.tique.util.Constantes;

import static java.security.AccessController.getContext;

public class NuevoNegocioActivity extends AppCompatActivity {

    private static final int GALLERY_PO = 1, GALLERY_PE = 3, GALLERY_IA = 5;
    private static final int CAMERA_PO = 2, CAMERA_PE = 4, CAMERA_IA = 6;
    Context context;
    LinearLayout llImagenes;
    TextView tvCountImage, tvHoraDesde, tvHoraHasta, tvErrorSubCat, tvErrorDuracion,
            tvNombre, tvDireccion, tvAltura, tvCiudad, tvDesc, tvCantTurno;
    TextInputLayout tilNombre, tilDireccion, tilAltura, tilCiudad, tilDesc, tilCantTurno;
    ImageView imgPortada, imgPerfil, imgFondo, fabBorrarFotoPerfil, fabBorrarFotoPortada;
    moduloProcesarImagen mpi;
    //RadioGroup rgCategoria;
    FloatingActionButton fabFotoPortada, fabFotoPerfil;
    ImageButton btnCamera;
    MaterialButton tbSubcategoria, btnEnviar, mbNegocio, mbServicio;
    SeekBar sbDesde, sbHasta;
    MaterialButtonToggleGroup tbCategoria, tbDuracionTurno;
    SingletonCategorias sCategorias;
    String duracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_negocio);

        this.context = this;
        this.sCategorias = SingletonCategorias.getInstance();

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

        tbCategoria = findViewById(R.id.tbCategoria);
        tbDuracionTurno = findViewById(R.id.tbDuracionTurno);
        mbNegocio = findViewById(R.id.mbNegocio);
        mbServicio = findViewById(R.id.mbServicio);
        tbSubcategoria = findViewById(R.id.tbSubcategoria);
        tvHoraDesde = findViewById(R.id.tvHoraDesde);
        tvHoraHasta = findViewById(R.id.tvHoraHasta);
        sbDesde = findViewById(R.id.sbDesde);
        sbHasta = findViewById(R.id.sbHasta);

        tvNombre = findViewById(R.id.tvNombre);
        tilNombre = findViewById(R.id.tilNombre);
        tvDireccion = findViewById(R.id.tvDireccion);
        tilDireccion = findViewById(R.id.tilDireccion);
        tvAltura = findViewById(R.id.tvAltura);
        tilAltura = findViewById(R.id.tilAltura);
        tvCiudad = findViewById(R.id.tvCiudad);
        tilCiudad = findViewById(R.id.tilCiudad);
        tvDesc = findViewById(R.id.tvDesc);
        tilDesc = findViewById(R.id.tilDesc);
        tvCantTurno = findViewById(R.id.tvCantTurno);
        tilCantTurno = findViewById(R.id.tilCantTurno);

        //Errores
        tvErrorSubCat = findViewById(R.id.tvErrorSubCat);
        tvErrorDuracion = findViewById(R.id.tvErrorDuracion);

        //Seteo listeners

        fabFotoPortada.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PO, CAMERA_PO, false));
        fabFotoPerfil.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_PE, CAMERA_PE, false));
        btnCamera.setOnClickListener(new oclAdjuntarImagen(this, GALLERY_IA, CAMERA_IA, true));

        fabBorrarFotoPortada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarImagenDisp((String) imgPortada.getTag());
                imgPortada.setVisibility(View.GONE);
                imgFondo.setVisibility(View.VISIBLE);
                fabBorrarFotoPortada.setVisibility(View.GONE);
            }
        });

        fabBorrarFotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarImagenDisp((String) imgPerfil.getTag());
                imgPerfil.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.perfil));
                fabBorrarFotoPerfil.setVisibility(View.GONE);
            }
        });

        mbNegocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbSubcategoria.setText(Constantes.SELECCIONE);
                sCategorias.setSubCatSelected(0);
                tbSubcategoria.setOnClickListener(new oclSelecSubcategoria(context, 1));
            }
        });

        mbServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tbSubcategoria.setText(Constantes.SELECCIONE);
                sCategorias.setSubCatSelected(0);
                tbSubcategoria.setOnClickListener(new oclSelecSubcategoria(context, 2));
            }
        });
/*
        rgCategoria.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //recargarSubcategorias(checkedId);
            }
        });*/

        tbDuracionTurno.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                MaterialButton btn = findViewById(checkedId);

                if (!isChecked ) {
                    Log.d(Constantes.LOG_NAME, "Se deseleccionó " + btn.getText());
                    duracion = null;
                }
                if (isChecked) {
                    Log.d(Constantes.LOG_NAME, "Se seleccionó " + btn.getText());
                    duracion = (String) btn.getText();
                    tvErrorDuracion.setVisibility(View.GONE);
                }
            }
        });

        tbSubcategoria.setOnClickListener(new oclSelecSubcategoria(context, 1));

        sbDesde.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHoraDesde.setText(progress + ":00");
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
                tvHoraHasta.setText(progress + ":00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
/*
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarFormulario();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);

        Drawable drawable = menu.findItem(R.id.send).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.send:
                enviarFormulario();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void enviarFormulario() {

        int errores = validarFormularios();

        if (errores > 0) {
            Snackbar
                    .make(imgPerfil, "El formulario contiene " + errores + " errores", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.design_default_color_error))
                    .setTextColor(getResources().getColor(R.color.white))
                    .show();
            //Toast.makeText(this.getApplicationContext(), "El formulario contiene errores", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getApplicationContext(), "Enviando...", Toast.LENGTH_SHORT).show();
            recolectarDatos();
            guardarDatos();
        }
    }


    private int validarFormularios() {
        //boolean errores = false;
        int validado = 0;
        if (sCategorias.getSubCatSelected() == 0) {
            validado++;
            tvErrorSubCat.setVisibility(View.VISIBLE);
        }

        if (tvNombre.length() < 4 || tvNombre.length() > 50) {
            validado++;
            tilNombre.setError("Debe contener entre 4 y 50 caracteres");
        }else{
            tilNombre.setError("");
        }

        if (tvDireccion.length() < 4 || tvDireccion.length() > 50) {
            validado++;
            tilDireccion.setError("Debe contener entre 4 y 50 caracteres");
        }else{
            tilDireccion.setError("");
        }

        if (tvAltura.length() < 1 || tvAltura.length() > 6) {
            validado++;
            tilAltura.setError("*");
        }else{
            tilAltura.setError("");
        }

        if (tvCiudad.length() < 4 || tvCiudad.length() > 50) {
            validado++;
            tilCiudad.setError("Debe contener entre 4 y 50 caracteres");
        }else{
            tilCiudad.setError("");
        }

        if (tvDesc.length() > 500) {
            validado++;
            tilDesc.setError("Debe contener menos de 500 caracteres");
        }else{
            tilDesc.setError("");
        }

        if (duracion == null){
            validado++;
            tvErrorDuracion.setVisibility(View.VISIBLE);
        }

        if (tvCantTurno.length() < 1 || tvCantTurno.length() > 3) {
            validado++;
            tilCantTurno.setError("Debe contener entre 1 y 3 caracteres");
        }else{
            tilCantTurno.setError("");
        }

        return validado;

    }

    private void recolectarDatos() {
    }

    private void guardarDatos() {
    }


    private void borrarImagenDisp(String tag) {
        if (tag != null) {
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

        switch (requestCode) {
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

    public void updateUI(CharSequence text) {

        tbSubcategoria.setText(text);
        tvErrorSubCat.setVisibility(View.GONE);
    }

}
