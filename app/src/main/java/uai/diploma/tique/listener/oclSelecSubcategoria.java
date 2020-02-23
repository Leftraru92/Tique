package uai.diploma.tique.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import uai.diploma.tique.R;
import uai.diploma.tique.activity.MainActivity;
import uai.diploma.tique.activity.NuevoNegocioActivity;
import uai.diploma.tique.modelo.Categorias;
import uai.diploma.tique.modelo.SingletonCategorias;

public class oclSelecSubcategoria implements View.OnClickListener {

    static Context context;
    private static View rootView;
    private static int codeCat;
    static ScrollView scroll;
    static SingletonCategorias sCategorias;
    View viewInflater;

    public oclSelecSubcategoria(Context context, int codeCat) {
        this.context = context;
        this.codeCat = codeCat;
        sCategorias = SingletonCategorias.getInstance();
    }

    @Override
    public void onClick(View view) {

        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewInflater = inflater.inflate(R.layout.dialog_select_subcategoria, null);

        TextView tvSubcat = viewInflater.findViewById(R.id.tvSubcat);
        final RadioGroup rgSubcat = viewInflater.findViewById(R.id.rgSubcat);
        scroll = viewInflater.findViewById(R.id.scroll);


        tvSubcat.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generarRadioButtons(rgSubcat, charSequence, inflater);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        generarRadioButtons(rgSubcat, null, inflater);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Seleccione Subcategoría")
                .setView(viewInflater)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        final AlertDialog dialog = builder.create();
        dialog.show();
/*
        if(openDialog != null){
            openDialog.dismiss();
        }*/


        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int codeSubcat = ((RadioGroup) viewInflater.findViewById(R.id.rgSubcat)).getCheckedRadioButtonId();
                RadioButton rbSelected = (RadioButton) viewInflater.findViewById(codeSubcat);

                if (codeSubcat > 0) {
                    sCategorias.setSubCatSelected(codeSubcat);
                    ((NuevoNegocioActivity) context).updateUI(rbSelected.getText());

                    dialog.dismiss();
/*
                    //Actualizar dialogo
                    if (openDialog != null) {
                        ((Button) openDialog.findViewById(R.id.tbSucursal)).setText(sUsuairo.getSucursalSelected(context).getNombre());
                        openDialog.show();
                    }*/

                } else {
                    Snackbar.make(v, "Debe seleccionar una suibcategoría", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    private static void generarRadioButtons(RadioGroup rgSubcat, CharSequence charSequence, LayoutInflater inflater) {

        rgSubcat.removeAllViews();

        //int ucSelected = sCategorias.getSucursalSelected(context).getUnidad_Comercial();

        for (Categorias cat : sCategorias.getCategorias(codeCat) ) {

            if(charSequence == null || cat.getDescription().contains(charSequence.toString().toUpperCase())) {

                RadioButton rdbtn = (RadioButton) inflater.inflate(R.layout.control_radio_button, null);

                rdbtn.setId(cat.getCode());
                rdbtn.setText(cat.getDescription());

                /*if(ucSelected == uc.getUnidad_Comercial()){
                    rdbtn.setChecked(true);
                }*/

                rgSubcat.addView(rdbtn);
            }

            //scroll.setLayoutParams(new ConstraintLayout.LayoutParams(200, 0));
        }
    }
}
