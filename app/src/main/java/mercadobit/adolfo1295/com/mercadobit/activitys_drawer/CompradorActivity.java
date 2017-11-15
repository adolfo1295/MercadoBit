package mercadobit.adolfo1295.com.mercadobit.activitys_drawer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import mercadobit.adolfo1295.com.mercadobit.R;
import mercadobit.adolfo1295.com.mercadobit.fragments.AyudaFragment;
import mercadobit.adolfo1295.com.mercadobit.fragments.Principal_CompradorFragment;
import mercadobit.adolfo1295.com.mercadobit.fragments.Solicitudes_CompradorFragment;

public class CompradorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = CompradorActivity.class.getSimpleName();
    private AccessToken accessToken;
    private NavigationView navigationView;
    private View hView;
    private DrawerLayout drawer;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprador);
        accessToken = AccessToken.getCurrentAccessToken();
        bindViews();
        navigationView.setNavigationItemSelectedListener(this);
        initPrincipalFragmentComprador();
        getDataFromoGraphApi();
    }


    //inicia el fragment prinicpal
    private void initPrincipalFragmentComprador() {
        callMainFragment(new Principal_CompradorFragment());
    }



    //obtiene los datos de graph api para despues llamar el metodo setDataOnUI
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

    //bindea las vistas en la ui
    private void bindViews(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        hView =  navigationView.getHeaderView(0);
    }

    //coloca los datos obtenidos desde graph api y los coloca en el ehader
    private void setDataOnUI(String url, String nombre, String fbUserEmail) {
        CircularImageView pp = hView.findViewById(R.id.profilePicture);
        TextView userName = hView.findViewById(R.id.tvUsername);
        TextView email = hView.findViewById(R.id.tvEMail);
        Glide.with(CompradorActivity.this).load(url).into(pp);
        userName.setText(nombre);
        email.setText(fbUserEmail);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Principal_CompradorFragment.principalComprador){
                super.onBackPressed();
            }else if (Solicitudes_CompradorFragment.solicitudesCompradorOn){
                callMainFragment(new Principal_CompradorFragment());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.comprador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {

        }else if (id == R.id.nav_configuracion) {

        } else if (id == R.id.nav_ayuda) {
            callFragment(new AyudaFragment());
        } else if (id == R.id.nav_vendedor) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //metodo para llamar el main fragment
    private void callMainFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    //metodo para llamar otros fragment que no sea el main
    public void callFragment(Fragment newFragment){
        //bundle para pasar datos de fragmnt a fragment
        //Bundle bundle = new Bundle();
        //bundle.putString(IDKEY,id);
        //bundle.putString(BANNER, banner);
        //newFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
