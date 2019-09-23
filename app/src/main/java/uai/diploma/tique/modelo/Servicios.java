package uai.diploma.tique.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Servicios {

    private int code;
    private String name;
    private String adress;
    private String image;

    public Servicios(JSONObject jo) throws JSONException {
        this.code = jo.getInt("code");
        this.name = jo.getString("name");
        this.adress = jo.getString("adress");
        this.image = jo.getString("image");
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getImage() {
        return image;
    }
}
