package uai.diploma.tique.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Categorias{

    private int code;
    private String description;
    private String icon;
    private boolean isSon;

    public Categorias(JSONObject jo) throws JSONException {
        this.code = jo.getInt("code");
        this.description = jo.getString("description");
        this.icon = jo.getString("icon");
        this.isSon = jo.getBoolean("isSon");
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

}
