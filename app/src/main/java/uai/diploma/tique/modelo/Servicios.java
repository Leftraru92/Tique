package uai.diploma.tique.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Servicios {

    private int code;
    private String name;
    private String adress;
    private String avatar_image;

    public Servicios(JSONObject jo) throws JSONException {
        this.code = jo.getInt("code");
        this.name = jo.getString("name");
        this.adress = jo.getString("addres");
        this.avatar_image = jo.getString("avatar_image");
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

    public String getAvatarImage() {
        return avatar_image;
    }
}
