package mercadobit.adolfo1295.com.mercadobit.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import mercadobit.adolfo1295.com.mercadobit.R;
import mercadobit.adolfo1295.com.mercadobit.activitys_confirm.ConfirmFacebookActivity;
import mercadobit.adolfo1295.com.mercadobit.activitys_drawer.CompradorActivity;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = IntroActivity.class.getSimpleName();
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        callbackManager = CallbackManager.Factory.create();
        if (AccessToken.getCurrentAccessToken()!=null){
            initMain();
            return;
        }
        setFbButton();
        generateHash();
    }


    private void setFbButton() {
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        // Application code
                        if (response.getError() != null) {
                            Log.e(TAG,"Error al obtener datos del api");
                        } else {

                            Log.d(TAG,"Se obtuvieron los datos correctamente");

                            //datos para recolectar
                            String fbUserId = json.optString("id");
                            String fbUserFirstName = json.optString("name");
                            String fbUserEmail = json.optString("email");
                            Intent toConfirm = new Intent(IntroActivity.this, ConfirmFacebookActivity.class);
                            toConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(toConfirm);
                            Log.d(TAG,"email: "+fbUserEmail);
                        }
                        Log.v("FaceBook Response :", response.toString());
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(IntroActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(IntroActivity.this, "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void initMain() {
        Intent toMain = new Intent(IntroActivity.this, CompradorActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(toMain);
    }

    void generateHash(){
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("mercadobit.adolfo1295.com.mercadobit", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", "Hash: "+something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
