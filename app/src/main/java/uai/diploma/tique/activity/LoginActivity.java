package uai.diploma.tique.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import uai.diploma.tique.R;
import uai.diploma.tique.modelo.Rol;
import uai.diploma.tique.modelo.SingletonUsuario;
import uai.diploma.tique.util.Constantes;
import uai.diploma.tique.util.WebService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    FloatingActionButton fabClose;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    String deviceToken;
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 1;
    static SingletonUsuario sUsuairo;
    static Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ctx = this;
        sUsuairo = SingletonUsuario.getInstance(getApplicationContext());

        fabClose = findViewById(R.id.fabClose);
        signInButton = findViewById(R.id.sign_in_button);

        fabClose.setOnClickListener(this);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseApp.initializeApp(this.getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                deviceToken = instanceIdResult.getToken();
                Log.d(Constantes.LOG_NAME, "Device token: " + deviceToken);
                sUsuairo.setToken(deviceToken);
            }
        });

        Constantes.GOOGLE_SIGN_IN = mGoogleSignInClient;
        //SingletonUsuario sUsuario = SingletonUsuario.getInstance();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.sign_in_button:
                //Log.i(Constantes.LOG_NAME, "Se tocó el botón login");
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                signIn();
                break;
            case R.id.fabClose:
                finish();
                break;

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Constantes.LOG_NAME, "requestCode:" + requestCode + " resultCode:" + resultCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w(Constantes.LOG_NAME, "Google sign in failed", e);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Snackbar.make(fabClose, "Error al iniciar sesión", Snackbar.LENGTH_LONG).show();
            }

        } else {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    private void handleSignInResult(GoogleSignInAccount completedTask) {

        Log.d(Constantes.LOG_NAME, "FirebaseAuthWithGoogle ID:" + completedTask.getId());
        Log.d(Constantes.LOG_NAME, "FirebaseAuthWithGoogle Token:" + completedTask.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(completedTask.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(Constantes.LOG_NAME, "signInWithCredential:success ");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //completedTask.getPhotoUrl();
                            user.getPhotoUrl();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.sign_in_button), "Fallo de autentificación", Snackbar.LENGTH_SHORT).show();
                            Log.i(Constantes.LOG_NAME, "signInResult:failed code=" + task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(final FirebaseUser account) {


        if (account != null) {

            account.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                //sUsuairo.setToken(task.getResult().getToken());
                            } else {
                                //sUsuairo.setToken(null);
                            }

                            Log.i(Constantes.LOG_NAME, "Cuenta seleccionada " + account.getDisplayName());
                            Log.i(Constantes.LOG_NAME, Constantes.token);


                            callServiceLogin(account);

                        }
                    });

        } else {
            //Controlar is expired
            signInButton.setVisibility(View.VISIBLE);
            Log.i(Constantes.LOG_NAME, "La cuenta es null");
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }
    }

    public void callServiceLogin(FirebaseUser cuenta) {

        //sUsuairo.setEmail(cuenta.getEmail());
        sUsuairo.setDisplayName(cuenta.getDisplayName());
        sUsuairo.setEmail(cuenta.getEmail());
        sUsuairo.setTelefono(cuenta.getPhoneNumber());
        sUsuairo.setFoto(cuenta.getPhotoUrl());

        //this.finish();

        WebService cs = new WebService(this, null);

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", sUsuairo.getEmail());
            jsonBody.put("device_id", sUsuairo.getToken());
            jsonBody.put("proyect_id", Constantes.PROJECT_ID);
            jsonBody.put("nombre", sUsuairo.getDisplayName());
            jsonBody.put("telefono", (sUsuairo.getTelefono()==null)?"0":sUsuairo.getTelefono());

            Log.i(Constantes.LOG_NAME, "Body:" + jsonBody);

            //Busco las sucursales asignadas
            cs.callService(Constantes.WS_LOGIN, null, Request.Method.POST, Constantes.R_OBJECT, jsonBody);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(Constantes.LOG_NAME, e.toString());
        }
    }

    public static void onResult(JSONObject response) {



        String mensaje = "";
        if (response.has("roles") && !response.isNull("roles")) {

            try {
                sUsuairo.setRoles(Rol.getListFromJson((JSONArray) response.get("roles")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(ctx, "Bienvenido " + sUsuairo.getDisplayName(), Toast.LENGTH_LONG).show();
            ((LoginActivity) ctx).finish();

        }else{
            Snackbar.make(((LoginActivity) ctx).findViewById(R.id.sign_in_button), "No se pudo iniciar sesión", Snackbar.LENGTH_SHORT).show();
        }
    }
}
