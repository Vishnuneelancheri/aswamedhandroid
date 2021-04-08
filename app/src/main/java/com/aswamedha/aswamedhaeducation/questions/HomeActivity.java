package com.aswamedha.aswamedhaeducation.questions;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    String[] ytLinks = {"bvI9mh6D0gQ", "ITdjtQ2AX8U",
    "STj9_7xL+tx8", "YoIejGfpw4k","0eHEBi1uFn8&t=37s",
    "wIPPMs_9Vqg","4y-NMQ_opoc"};
    private DrawerLayout drawerLayout;
    private ImageView imgNavMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById( R.id.drwr_lyt );
        getSupportFragmentManager().beginTransaction().add(R.id.frame_nav, new NavigationViewFragment() ).commit();
        getSupportFragmentManager().beginTransaction().add( R.id.frame_home , new MainMenuFragment() ).commit();

        imgNavMenu = findViewById( R.id.img_nav_menu );
        imgNavMenu.setOnClickListener( this );
        drawerLayout.setOnClickListener( this );

        initPushNotification();
        /*MobileAds.initialize( this, getString( R.string.adMobId ) );
        AdView adView = findViewById( R.id.adView );
        AdRequest adRequest = new AdRequest.Builder().build();

        adView.loadAd( adRequest );
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });*/

        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        AdView adView = findViewById( R.id.adView );
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener( new AdListener(){
            
        });*/

    }
    @Override
    public void onClick( View view ){
        int id = view.getId();
        switch ( id ){

            case R.id.img_nav_menu:
                navCloseOpen();
                break;
        }
    }
    public void navCloseOpen(){
        try{
            if(!drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.openDrawer(Gravity.START);
            else
                drawerLayout.closeDrawer(GravityCompat.START);

        }catch ( Exception e ){
            //Do nothing
        }
    }
    private void initPushNotification(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        try{
                            String token = task.getResult();
                            Log.d("token", token);
                            updatePushNotification(token);
                        }catch (Exception e ){
                            Log.d("token", e.toString());
                        }
                    }
                });
    }
    private void updatePushNotification(String token){
        SharedPreferences sharedPref = getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
        final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
        final JSONObject params = new JSONObject();
        try{
            params.put("cust_id", regId );
            params.put("fcm_token", token );


        }catch (JSONException e ){
            //Do nothing
        }
        String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/fcm_update";
        Networker.getInstance().posting(this, url, params, new Networker.ResponseBridge() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
