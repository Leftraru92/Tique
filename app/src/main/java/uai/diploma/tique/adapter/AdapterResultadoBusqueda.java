package uai.diploma.tique.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uai.diploma.tique.R;
import uai.diploma.tique.fragment.ResultadoBusquedaFragment.OnListFragmentInteractionListener;
import uai.diploma.tique.fragment.dummy.DummyContent.DummyItem;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.Servicios;

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

        if (!lservicios.get(position).getImage().equals("null")){
            byte[] decodedString = Base64.decode(lservicios.get(position).getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgPerfil.setImageBitmap(decodedByte);
        }

/*
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return lservicios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtNombre;
        public final TextView txtDireccion;
        public final ImageView imgPerfil;
        public DummyItem mItem;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
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
