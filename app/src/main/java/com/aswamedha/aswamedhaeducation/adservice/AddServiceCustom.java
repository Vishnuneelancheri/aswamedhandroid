package com.aswamedha.aswamedhaeducation.adservice;

import android.content.Context;

import com.aswamedha.aswamedhaeducation.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AddServiceCustom {
    private static AddServiceCustom addServiceCustom;
    public synchronized static AddServiceCustom getInstance(){
        if ( addServiceCustom == null )
            addServiceCustom = new AddServiceCustom();
        return addServiceCustom;
    }
    public void initAdd(Context context, AdView adView){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener( new AdListener(){

        });
    }
}
