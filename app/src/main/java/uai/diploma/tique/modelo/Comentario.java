package uai.diploma.tique.modelo;

import org.json.JSONException;
import org.json.JSONObject;

public class Comentario {

    private String user_name;
    private String comment;
    private int stars;
    private String date;

    public Comentario(JSONObject dataItem) throws JSONException {

        this.user_name = dataItem.getString("user_name");
        this.comment = dataItem.getString("comment");
        this.stars = dataItem.getInt("stars");
        this.date = dataItem.getString("date");
    }

    public String getUser_name() {
        return user_name;
    }

    public String getComment() {
        return comment;
    }

    public int getStars() {
        return stars;
    }

    public String getDate() {
        return date;
    }
}
