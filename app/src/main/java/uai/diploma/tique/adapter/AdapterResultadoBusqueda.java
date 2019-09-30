package uai.diploma.tique.adapter;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uai.diploma.tique.R;
import uai.diploma.tique.activity.DetailActivity;
import uai.diploma.tique.activity.MainActivity;
import uai.diploma.tique.fragment.ResultadoBusquedaFragment.OnListFragmentInteractionListener;
import uai.diploma.tique.fragment.dummy.DummyContent.DummyItem;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.Servicios;
import uai.diploma.tique.util.Constantes;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AdapterResultadoBusqueda extends RecyclerView.Adapter<AdapterResultadoBusqueda.MyViewHolder> {

    private ArrayList<Servicios> lservicios;
    private Context context;

    public AdapterResultadoBusqueda(Context context, ArrayList<Servicios> servicios) {
        this.context = context;
        this.lservicios = servicios;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_servicios, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.txtNombre.setText(lservicios.get(position).getName());
        holder.txtDireccion.setText(lservicios.get(position).getAdress());

        if (!lservicios.get(position).getAvatarImage().equals("null")){
            byte[] decodedString = Base64.decode(lservicios.get(position).getAvatarImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgPerfil.setImageBitmap(decodedByte);
        }


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(Constantes.LOG_NAME, "se presionó el servicio");
                Intent i = new Intent((Activity)context, DetailActivity.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lservicios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public final View mView;
        public final TextView txtNombre;
        public final TextView txtDireccion;
        public final ImageView imgPerfil;
        public DummyItem mItem;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            cardView = itemView.findViewById(R.id.card_view);
            txtNombre = (TextView) view.findViewById(R.id.nombre);
            txtDireccion = (TextView) view.findViewById(R.id.direccion);
            imgPerfil = (ImageView) view.findViewById(R.id.profile_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtNombre.getText() + "'";
        }
    }
}
