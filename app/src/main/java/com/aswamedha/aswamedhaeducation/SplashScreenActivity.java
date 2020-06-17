package com.aswamedha.aswamedhaeducation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.aswamedha.aswamedhaeducation.login.LoginActivity;
import com.aswamedha.aswamedhaeducation.questions.HomeActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );
        ShimmerFrameLayout shimmerFrameLayout = findViewById( R.id.shimmerlayout );
        shimmerFrameLayout.startShimmer();
        SharedPreferences sharedPref = getSharedPreferences(AswamedhamApplication.SHARED_PREF,Context.MODE_PRIVATE);
        final int regId = sharedPref.getInt( AswamedhamApplication.REG_ID, -1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent;
                if ( regId > 0 ){
                    intent = new Intent( SplashScreenActivity.this, HomeActivity.class );
                }else {
                    intent = new Intent( SplashScreenActivity.this, LoginActivity.class );
                }
                startActivity( intent );
                finish();
            }
        }, 2000);

    }
}
