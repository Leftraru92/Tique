package uai.diploma.tique.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;
import uai.diploma.tique.R;
import uai.diploma.tique.fragment.IWebServiceFragment;
import uai.diploma.tique.modelo.Comentario;
import uai.diploma.tique.modelo.Item;
import uai.diploma.tique.modelo.ServicioDetalle;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.Util;
import uai.diploma.tique.util.WebService;

public class DetailActivity extends AppCompatActivity implements IWebServiceFragment {

    Toolbar toolbar;
    View loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loading = findViewById(R.id.loadingPanel);

        getData();
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

    private void getData() {

        try {

            loading.setVisibility(View.VISIBLE);

            String partialUrl = Constantes.WS_DETALLE;;
            String params = null;
            JSONObject body = null;
            //params = "?SucursalId=" + idos;

            //loading.setVisibility(View.VISIBLE);

            WebService webService = new WebService(this, this, null);

            webService.callService(partialUrl, params, Constantes.M_GET, Constantes.R_OBJECT, body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWebServiceResult(ArrayList<?> lista) {

    }

    @Override
    public void onWebServiceResult(Object dservicio) {

        loading.setVisibility(View.GONE);

        ServicioDetalle servicio = (ServicioDetalle) dservicio;
        Log.d(Constantes.LOG_NAME, servicio.getName());

        ImageView imgPortada = findViewById(R.id.imgPortada);
        TextView txtNombre = findViewById(R.id.txtNombre);
        ImageView imgPerfil = findViewById(R.id.profile_image);
        TextView txtDireccion = findViewById(R.id.txtDireccion);
        TextView txtDescripcion = findViewById(R.id.txtDescripcion);

        byte[] decodedString = Base64.decode(servicio.getAccount_image(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgPortada.setImageBitmap(decodedByte);

        decodedString = Base64.decode(servicio.getAvatarImage(), Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imgPerfil.setImageBitmap(decodedByte);

        txtNombre.setText(servicio.getName());
        txtDireccion.setText(servicio.getAdress());
        txtDescripcion.setText(servicio.getDescription());


        LinearLayout layout = (LinearLayout) findViewById(R.id.llimagenes);

        for (Item i: servicio.getAtached_images()) {
            decodedString = Base64.decode(i.getImage(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            ImageView imageView = new ImageView(this);
            imageView.setId(i.getCode());
            imageView.setPadding(2, 2, 2, 2);
            imageView.setImageBitmap(decodedByte);
            imageView.setClickable(true);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(200,200,1.0f);
            imageView.setLayoutParams(param);
            layout.addView(imageView);
        }

        LinearLayout llComentarios = findViewById(R.id.llComentarios);
        for (Comentario i: servicio.getComments()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View layout_commment = inflater.inflate(R.layout.fragment_comentarios, layout, false);

            TextView tvNombre = layout_commment.findViewById(R.id.tvNombre);
            TextView tvComentario = layout_commment.findViewById(R.id.tvComentario);
            TextView tvFecha = layout_commment.findViewById(R.id.tvFecha);
            RatingBar ratingBar = layout_commment.findViewById(R.id.ratingBar);

            tvNombre.setText(i.getUser_name());
            tvComentario.setText(i.getComment());
            tvFecha.setText(Util.getDateFormatted(i.getDate()));
            ratingBar.setRating(i.getStars());
            llComentarios.addView(layout_commment);
        }

        toolbar.setTitle(servicio.getName());

    }
}
