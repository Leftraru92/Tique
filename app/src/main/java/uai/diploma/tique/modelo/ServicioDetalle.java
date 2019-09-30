package uai.diploma.tique.modelo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServicioDetalle extends Servicios {

    private String description;
    private String account_image;
    ArrayList<Item> atached_images = new ArrayList();
    ArrayList<Comentario> comments = new ArrayList();

    public ServicioDetalle(JSONObject jo) throws JSONException {
        super(jo);

        this.description = jo.getString("description");
        this.account_image = jo.getString("account_image");

        atached_images = new ArrayList<Item>();
        if (jo.has("atached_images") && !jo.isNull("atached_images")) {
            JSONArray arrayItems = jo.getJSONArray("atached_images");
            for (int i = 0; i < arrayItems.length(); i++) {
                JSONObject dataItem = arrayItems.getJSONObject(i);
                atached_images.add(new Item(dataItem));
            }
        }

        comments = new ArrayList<Comentario>();
        if (jo.has("comments") && !jo.isNull("comments")) {
            JSONArray arrayComments = jo.getJSONArray("comments");
            for (int i = 0; i < arrayComments.length(); i++) {
                JSONObject dataItem = arrayComments.getJSONObject(i);
                comments.add(new Comentario(dataItem));
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public String getAccount_image() {
        return account_image;
    }

    public ArrayList<Item> getAtached_images() {
        return atached_images;
    }

    public ArrayList<Comentario> getComments() {
        return comments;
    }
}
