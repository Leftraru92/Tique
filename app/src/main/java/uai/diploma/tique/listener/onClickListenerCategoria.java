package uai.diploma.tique.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import uai.diploma.tique.R;
import uai.diploma.tique.fragment.ResultadoBusquedaFragment;
import uai.diploma.tique.fragment.SubCategoryFragment;
import uai.diploma.tique.util.Constantes;

public class onClickListenerCategoria implements View.OnClickListener  {

    Context context;
    int id;
    boolean isSon;


    public onClickListenerCategoria(Context context, int id, boolean son) {
        this.context = context;
        this.id = id;
        this.isSon = son;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment;
        Log.i(Constantes.LOG_NAME, "Se tocó el menú " + id );

        //Si está en categorias muestra subcategorias,
        //si está en subcategorías muestra resultados
        if (isSon) {
            fragment = ResultadoBusquedaFragment.newInstance(id);
        }else{
            fragment = SubCategoryFragment.newInstance(id);
        }
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.main_fragment, fragment).addToBackStack(null).commit();

    }
}
