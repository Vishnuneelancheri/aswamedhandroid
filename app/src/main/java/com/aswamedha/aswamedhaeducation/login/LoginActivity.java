package com.aswamedha.aswamedhaeducation.login;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.content.SharedPreferences;
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
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText edtMob;
    private String mPhoneNum;
    private String firebasePushNotificationToken ;
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
        findViewById( R.id.lnr_google_signin ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        edtMob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                requestHint();
            }
        });
        initiateGoogle();
        smsReading();
        setupFirebase();
        //requestHint();
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
            //Do nothing
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
    private void setupFirebase(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("sf", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        Log.w("sf", task.getResult());
                        // Get new FCM registration token
                        firebasePushNotificationToken = task.getResult();
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
        else if ( requestCode == RC_SIGN_IN ){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 10;
    private void initiateGoogle(){
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }
    private void googleSignIn() {
        googleSignInClient.signOut();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;
            String email = account.getEmail();
            String token = account.getId();
            gmailSignServerCommunication(email,token);
        }catch (Exception e ){
            Log.d("googlesig", e.toString() );
        }
    }
    private void gmailSignServerCommunication(String gmail, String tokn){

        final JSONObject params = new JSONObject();
        try{
            params.put("gmail", gmail );
            params.put("token", tokn );
            if (firebasePushNotificationToken != null && !firebasePushNotificationToken.isEmpty()){
                params.put(AswamedhamApplication.FIREBASE_TOKEN, firebasePushNotificationToken);
            }

        }catch (JSONException e ){
            //Do nothing
        }
        final MyProgressDialog myProgressDialog = new MyProgressDialog( LoginActivity.this );
        myProgressDialog.setCancelable( false );
        myProgressDialog.setCanceledOnTouchOutside( false );
        myProgressDialog.setColor( R.color.colorAccent );
        myProgressDialog.show();
        String url = AswamedhamApplication.IP_ADDRESS + "CustomerRegistration/gmail_registration";
        Networker.getInstance().posting(LoginActivity.this, url, params, new Networker.ResponseBridge() {
            @Override
            public void onSuccess(String response) {
                parseGmailLoginResponse( response );
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText( LoginActivity.this, error.toString(), Toast.LENGTH_LONG ).show();
                myProgressDialog.dismiss();
            }
        });
    }
    private void parseGmailLoginResponse(String response){
        try{
            PhoneRegModel phoneRegModel = new Gson().fromJson( response, PhoneRegModel.class );
            SharedPreferences sharedPref = getSharedPreferences(AswamedhamApplication.SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt( AswamedhamApplication.REG_ID, phoneRegModel.getRegId() );
            editor.putString( AswamedhamApplication.TOKEN, phoneRegModel.getRegToken() );
            editor.putString( AswamedhamApplication.PHONE, phoneRegModel.getGmail() );
            editor.apply();

            Intent intent = new Intent( this, HomeActivity.class );
            startActivity( intent );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }catch (Exception e ){
            Toast.makeText( LoginActivity.this, "Server error", Toast.LENGTH_LONG ).show();
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
                bundle.putString(AswamedhamApplication.FIREBASE_TOKEN, firebasePushNotificationToken);
                intent.putExtras( bundle );
                startActivity( intent );
            }
            Toast.makeText( LoginActivity.this, phoneRegModel.getMessage(), Toast.LENGTH_LONG ).show();
        }catch ( Exception e ){
            Toast.makeText( LoginActivity.this, "Parse error occurred", Toast.LENGTH_LONG ).show();
        }
    }
}
