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
import uai.diploma.tique.fragment.SubCategoryFragment;
import uai.diploma.tique.util.Constantes;

public class onClickListenerCategoria implements View.OnClickListener  {

    Context context;
    int id;


    public onClickListenerCategoria(Context context, int id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public void onClick(View v) {

        Log.i(Constantes.LOG_NAME, "Se tocó el menú " + id );

        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        Fragment fragment = new SubCategoryFragment();
        manager.beginTransaction().replace(R.id.main_fragment, fragment).commit();

    }
}
