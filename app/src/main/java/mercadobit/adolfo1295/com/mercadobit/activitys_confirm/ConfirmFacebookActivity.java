package mercadobit.adolfo1295.com.mercadobit.activitys_confirm;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import mercadobit.adolfo1295.com.mercadobit.R;
import mercadobit.adolfo1295.com.mercadobit.activitys_drawer.CompradorActivity;

public class ConfirmFacebookActivity extends AppCompatActivity {

    private static final String TAG = ConfirmFacebookActivity.class.getSimpleName();
    private AccessToken accessToken;
    private String numero;

    @BindView(R.id.confirmPP) CircularImageView confirmPP;
    @BindView(R.id.confirmNombre) TextView confirmNombre;
    @BindView(R.id.confirmEmail) TextView confirmarEmail;
    @BindView(R.id.confirmCelular) TextInputEditText confirmCelular;
    @BindView(R.id.btnConfirmData) Button btnConfirmData;
    @BindView(R.id.switch_rolUsuario) Switch switchRol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_facebook);
        accessToken = AccessToken.getCurrentAccessToken();
        ButterKnife.bind(this);
        getDataFromoGraphApi();
    }

    private void getDataFromoGraphApi(){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json, GraphResponse response) {
                if (response.getError() != null) {
                    Log.e(TAG,"Error al obtener datos del api");
                } else {
                    Log.d(TAG,"Se obtuvieron los datos correctamente");
                    Profile profile = Profile.getCurrentProfile();
                    String url = String.valueOf(profile.getProfilePictureUri(100,100));
                    String nombre = profile.getName();
                    String fbUserEmail = json.optString("email");
                    setDataOnUI(url,nombre,fbUserEmail);
                }
                Log.v("FaceBook Response :", response.toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setDataOnUI(String urlPP, String nombre, String email){
        //foto de perfil
        Glide.with(ConfirmFacebookActivity.this).load(urlPP).into(confirmPP);
        //nombre de facebook
        confirmNombre.setText(nombre);
        //email en fb
        confirmarEmail.setText(email);
        btnConfirmData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numero = confirmCelular.getText().toString();
                if (numero.isEmpty())
                    confirmCelular.setError("Ingresa un nuevo celular");
                if (switchRol.isChecked()){
                    //Toast.makeText(ConfirmFacebookActivity.this, "Vendedor", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(ConfirmFacebookActivity.this, "Comprador", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
