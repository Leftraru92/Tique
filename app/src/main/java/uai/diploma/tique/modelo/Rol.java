package uai.diploma.tique.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Rol {

    private String nombre;
    private boolean valor;

    public Rol(JSONObject jo) throws JSONException {
        this.nombre = jo.getString("rol_name");
        this.valor = jo.getBoolean("value");
    }

    public void Rol(){}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isValor() {
        return valor;
    }

    public void setValor(boolean valor) {
        this.valor = valor;
    }

    public JSONObject toJSON(){
        JSONObject jo = new JSONObject();
        try {
            jo.put("rol_name", nombre);
            jo.put("value", valor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public static List<Rol> getListFromJson(JSONArray jaroles) {

        List<Rol> roles = new ArrayList<>();
        try {
            for (int i = 0; i < jaroles.length(); i++) {
                roles.add(new Rol(jaroles.getJSONObject(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return roles;
    }
}
