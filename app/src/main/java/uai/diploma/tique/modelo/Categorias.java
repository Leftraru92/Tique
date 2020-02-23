package uai.diploma.tique.modelo;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

public class Categorias{

    private static Categorias categorias;
    private Context ctx;
    private int code;
    private String description;
    private String icon;
    private boolean isSon;
    private List<Categorias> subCategorias;

    public Categorias(JSONObject jo, boolean isSon) throws JSONException {
        this.code = jo.getInt("codigo");
        this.description = jo.getString("descripcion").toUpperCase();
        this.icon = jo.getString("icono");
        this.isSon = isSon;
        this.subCategorias = new ArrayList<>();

        if (jo.has("subcategorias") && !jo.isNull("subcategorias")) {

            JSONArray arrayItems = jo.getJSONArray("subcategorias");
            for (int i = 0; i < arrayItems.length(); i++) {
                JSONObject dataItem = arrayItems.getJSONObject(i);
                subCategorias.add(new Categorias(dataItem, true));
            }
        }
    }


    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isSon() { return isSon; }

    public List<Categorias> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<Categorias> subCategorias) {
        this.subCategorias = subCategorias;
    }
}
