package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.login.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationViewFragment extends Fragment {

    public static String FACEBOOK_GROUP_URL = "https://www.facebook.com/groups/451754261929390";
    public static String FACEBOOK_PAGE_ID = /*"451754261929390"*/"aswamedhapsconlinestudy";
    public static String FACEBOOK_PAGE_URL = "https://www.facebook.com/aswamedhapsconlinestudy";
    private TextView mTxtPhone;

    public NavigationViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_view, container, false);
        mTxtPhone = view.findViewById( R.id.txt_phone );

        SharedPreferences sharedPref = getActivity().getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
        final String phone = sharedPref.getString( AswamedhamApplication.PHONE, "");
        mTxtPhone.setText( phone );

        view.findViewById( R.id.btn_ytd ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
                getFragmentManager().beginTransaction().add( R.id.frame_home , new YoutubeListFragment() ).addToBackStack("ytd").commit();
            }
        });
        view.findViewById(R.id.navigation_parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do nothing
            }
        });
        view.findViewById( R.id.btn_home ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });
        view.findViewById( R.id.btn_fb_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookGroupUrl(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        view.findViewById(R.id.btn_fb_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();

                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageUrl(getContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        view.findViewById( R.id.btn_fb_logout ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
                logout();
            }
        });
        view.findViewById(R.id.btn_mail_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        view.findViewById( R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });
        return view;
    }
    private void logout(){
        Activity activity = getActivity();
        SharedPreferences sharedPref = activity.getSharedPreferences(AswamedhamApplication.SHARED_PREF,Context.MODE_PRIVATE);
        sharedPref.edit().clear().apply();
        Intent intent = new Intent( activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity( intent );

    }
    public String getFacebookGroupUrl(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_GROUP_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_GROUP_URL; //normal web url
        }
    }
    public String getFacebookPageUrl(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_PAGE_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_GROUP_URL; //normal web url
        }
    }
    private void closeActivity(){
        ((HomeActivity) getActivity()).navCloseOpen();
    }
    private void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"aswamedhaeducation@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"");
        emailIntent.setType("message/rfc822");
        getContext().startActivity(Intent.createChooser(emailIntent,"Choose an email application"));
    }
    private void shareApp(){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBody = "https://play.google.com/store/apps/details?id=com.aswamedha.education.navneeth";
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share App"));
    }
}