package com.aswamedha.aswamedhaeducation.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.MyProgressDialog;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.aswamedha.aswamedhaeducation.questions.HomeActivity;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SmsRegistrationActivity extends AppCompatActivity implements MySMSBroadcastReceiver.OTPReceiveListener{
    private String token, mPhoneNum;
    private int regId;
    private ViewPager mViewPager;
    private MySMSBroadcastReceiver mySMSBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_registration);

        Bundle bundle = getIntent().getExtras();
        try{
            token = bundle.getString(AswamedhamApplication.TOKEN );
            regId = bundle.getInt( AswamedhamApplication.REG_ID );
            mPhoneNum = bundle.getString( AswamedhamApplication.PHONE );
        }catch ( Exception e ){
            Log.d("bundle error", e.toString() );
        }
        mViewPager = findViewById( R.id.view_pager );
        findViewById( R.id.btn_submit ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOtp();
            }
        });
        int[] images = {
                R.drawable.viewpager1/*, R.drawable.viewpager2, R.drawable.viewpager3*/, R.drawable.viewpager4};
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter( this, images );
        mViewPager.setAdapter( myCustomPagerAdapter );

        mySMSBroadcastReceiver = new MySMSBroadcastReceiver();
        mySMSBroadcastReceiver.setOtpReceiveListener(this);
        smsAutoRead();

    }
    private void smsAutoRead(){
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Success message
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void submitOtp(){
        EditText edtOtp = findViewById( R.id.edt_otp );
        String otp = edtOtp.getText().toString();
        if ( otp.isEmpty() ){
            Toast.makeText( this, "Please enter OTP", Toast.LENGTH_LONG ).show();
            return;
        }
        String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/verify_otp";
        final JSONObject params = new JSONObject();
        try{
            params.put("otp", otp );
            params.put("temp_token", token );
            params.put("name", "-" );
            params.put("gender", "1" );
            params.put("reg_id", regId );

        }catch (JSONException e ){
            //Do nothing
        }
        final MyProgressDialog myProgressDialog = new MyProgressDialog( SmsRegistrationActivity.this );
        myProgressDialog.setCancelable( false );
        myProgressDialog.setCanceledOnTouchOutside( false );
        myProgressDialog.setColor( R.color.colorAccent );
        myProgressDialog.show();
        Networker.getInstance().posting(SmsRegistrationActivity.this, url, params, new Networker.ResponseBridge() {
            @Override
            public void onSuccess(String response) {
                analyzeReport( response );
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                myProgressDialog.dismiss();
            }
        });
        /* RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        analyzeReport( response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> para = new HashMap<String, String>();
                para.put("Content-Type", "application/json");
                return para;
            }
        } ;*/
       /* queue.add(jsonObjectRequest);*/
    }
    private void analyzeReport( String response ){
        try{
            PhoneRegModel phoneRegModel = new Gson().fromJson( response, PhoneRegModel.class );
            Toast.makeText( this, phoneRegModel.getMessage(), Toast.LENGTH_LONG ).show();
            if ( phoneRegModel.getRegId() > 0 ){
                SharedPreferences sharedPref = getSharedPreferences(AswamedhamApplication.SHARED_PREF,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt( AswamedhamApplication.REG_ID, phoneRegModel.getRegId() );
                editor.putString( AswamedhamApplication.TOKEN, phoneRegModel.getToken() );
                editor.putString( AswamedhamApplication.PHONE, mPhoneNum );
                editor.apply();

                Intent intent = new Intent( this, HomeActivity .class );
                startActivity( intent );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
            }
        }catch (Exception e ){
            Toast.makeText( this, "Parse error occurred", Toast.LENGTH_LONG ).show();
        }
    }

    @Override
    public void onOTPReceived(String otp) {
        Toast.makeText(this,otp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }
}
