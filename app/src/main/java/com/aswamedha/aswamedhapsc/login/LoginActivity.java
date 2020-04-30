package com.aswamedha.aswamedhapsc.login;

import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhapsc.AswamedhamApplication;
import com.aswamedha.aswamedhapsc.MyProgressDialog;
import com.aswamedha.aswamedhapsc.R;
import com.aswamedha.aswamedhapsc.networking.Networker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText edtMob;
    private String mPhoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtMob = findViewById( R.id.edt_mob );
        findViewById( R.id.btn_submit ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
        smsReading();
        requestHint();
    }
    private void login(  ){
        String phone = edtMob.getText().toString();
        if ( phone.length() != 10 ){
            Toast.makeText( this, "Phone number should be 10 digit", Toast.LENGTH_LONG ).show();
            return;
        }
        final JSONObject params = new JSONObject();
        try{
            params.put("phone", phone );
            mPhoneNum = phone;
        }catch (JSONException e ){

        }
        final MyProgressDialog myProgressDialog = new MyProgressDialog( LoginActivity.this );
        myProgressDialog.setCancelable( false );
        myProgressDialog.setCanceledOnTouchOutside( false );
        myProgressDialog.setColor( R.color.colorAccent );
        myProgressDialog.show();
        String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/phone_registration";
        Networker.getInstance().posting(LoginActivity.this, url, params, new Networker.ResponseBridge() {
            @Override
            public void onSuccess(String response) {
                analyzeReport( response );
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_LONG ).show();
                myProgressDialog.dismiss();
            }
        });

    }

    private void smsReading(){
        SmsRetrieverClient smsRetrieverClient = SmsRetriever.getClient(this);
        Task<Void> task = smsRetrieverClient.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });

    }

    private void requestHint(){


        HintRequest request = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();
        PendingIntent intent = Credentials.getClient(this).getHintPickerIntent(request);
        //startIntentSenderForResult(intent.getIntentSender(), 100,0,0,0);
        try{
            startIntentSenderForResult(intent.getIntentSender(),
                    100, null, 0, 0, 0);
        }catch (IntentSender.SendIntentException e ){
            //Do nothing
        }

    }
    private void analyzeReport( String response ){
        try{
            //AIzaSyA0syx05ia36aNp6xjyF3B3zbhdlFTnFyw
            PhoneRegModel phoneRegModel = new Gson().fromJson( response, PhoneRegModel.class );
            if ( phoneRegModel.getRegId() > 0 ){
                Intent intent = new Intent( this, SmsRegistrationActivity.class );
                Bundle bundle = new Bundle();
                bundle.putInt( AswamedhamApplication.REG_ID, phoneRegModel.getRegId() );
                bundle.putString( AswamedhamApplication.TOKEN, phoneRegModel.getRegToken() );
                bundle.putString( AswamedhamApplication.PHONE, mPhoneNum );
                intent.putExtras( bundle );
                startActivity( intent );
            }
            Toast.makeText( LoginActivity.this, phoneRegModel.getMessage(), Toast.LENGTH_LONG ).show();
        }catch ( Exception e ){
            Toast.makeText( LoginActivity.this, "Parse error occurred", Toast.LENGTH_LONG ).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                String phone = credential.getId();
                if ( phone.length() > 9 ){
                    phone = phone.substring(phone.length() - 10);
                    edtMob.setText(phone);
                }
            }
        }
    }

}
