package com.aswamedha.aswamedhapsc.login;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhapsc.AswamedhamApplication;
import com.aswamedha.aswamedhapsc.MyProgressDialog;
import com.aswamedha.aswamedhapsc.R;
import com.aswamedha.aswamedhapsc.networking.Networker;
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
}
