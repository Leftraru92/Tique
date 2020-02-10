package uai.diploma.tique.modelo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import uai.diploma.tique.util.Constantes;

import static android.content.Context.MODE_PRIVATE;
import static uai.diploma.tique.modelo.Rol.getListFromJson;

public class SingletonUsuario {

    static SingletonUsuario susuario;
    Context ctx;
    //String email;
    String displayName, telefono, email, token;
    Uri foto;
    List<Rol> roles;

    private SingletonUsuario(Context ctx) {
        this.ctx = ctx;

        this.displayName = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("NOMBRE", null);
        this.telefono = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("TELEFONO", null);
        this.email = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("EMAIL", null);
        this.token = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("TOKEN", null);
        String fotoString = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("FOTO", null);
        if(fotoString!=null)
            this.foto = Uri.parse(fotoString);

        String rolesString = ctx.getSharedPreferences("_", MODE_PRIVATE).getString("ROLES", null);
        if(rolesString!= null) {
            try {
                JSONArray jsonArray = new JSONArray(rolesString);
                this.roles = getListFromJson(jsonArray);
            } catch (JSONException e) {
                this.roles = null;
            }
        }
    }

    public static SingletonUsuario getInstance(Context ctx) {
        if (susuario == null) {
            susuario = new SingletonUsuario(ctx);
        }
        return susuario;
    }

    public static SingletonUsuario getSusuario() {
        return susuario;
    }

    public static void setSusuario(SingletonUsuario susuario) {
        SingletonUsuario.susuario = susuario;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("NOMBRE", displayName).apply();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("EMAIL", email).apply();
    }

    public void setTelefono(String phoneNumber) {
        this.telefono = phoneNumber;
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("TELEFONO", phoneNumber).apply();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setFoto(Uri photoUrl) {
        this.foto = photoUrl;
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("FOTO", photoUrl.toString()).apply();
    }

    public Uri getFoto() {
        return foto;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("TOKEN", token).apply();
    }

    public void logout(){
        this.email = null;
        this.displayName = null;
        this.telefono = null;
        this.foto = null;
        this.token = null;
        this.roles = null;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
        String rolesString="";
        JSONArray jsonArray = new JSONArray();
        for (Rol rol: roles) {
            jsonArray.put(rol.toJSON());
        }
        ctx.getSharedPreferences("_", MODE_PRIVATE).edit().putString("ROLES", jsonArray.toString()).apply();
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public boolean tieneRol(String nombreRol) {
        boolean tiene = false;

        if (getRoles() != null) {
            for (Rol rol : getRoles()) {
                if (nombreRol.equals(rol.getNombre()))
                    tiene = true;
            }
        }
        return tiene;
    }
}
