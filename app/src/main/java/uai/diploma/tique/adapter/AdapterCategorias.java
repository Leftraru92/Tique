package uai.diploma.tique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import uai.diploma.tique.R;
import uai.diploma.tique.fragment.CategoryFragment;
import uai.diploma.tique.listener.onClickListenerCategoria;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.util.Util;

public class AdapterCategorias extends
        RecyclerView.Adapter<AdapterCategorias.MyViewHolder>{

    private ArrayList<Categorias> lcategorias;
    private Context context;

    public AdapterCategorias(Context context, ArrayList<Categorias> categorias) {
        this.lcategorias = categorias;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_categoria, parent, false);
        final AdapterCategorias.MyViewHolder myViewHolder = new AdapterCategorias.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        TextView txtDescription = holder.txtDescription;
        ImageView imgIcon = holder.imgIcon;

        txtDescription.setText(lcategorias.get(position).getDescription());
        imgIcon.setImageResource(Util.getIcon(lcategorias.get(position).getIcon()));
        //holder.cardView.setOnClickListener(CategoryFragment.myOnClickListener);
        holder.cardView.setOnClickListener(new onClickListenerCategoria(context, lcategorias.get(position).getCode()));
    }

    @Override
    public int getItemCount() {
        return lcategorias.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtDescription;
        ImageView imgIcon;


        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
        }
    }
}
