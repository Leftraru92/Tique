package uai.diploma.tique.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {

    private int code;
    private String image;

    public Item(JSONObject dataItem) throws JSONException {

        this.code = dataItem.getInt("code");
        this.image = dataItem.getString("image");
    }

    public int getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }
}
