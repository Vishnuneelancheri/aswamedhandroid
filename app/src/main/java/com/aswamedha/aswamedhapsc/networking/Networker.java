package com.aswamedha.aswamedhapsc.networking;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Networker {
    private static Networker networker;
    public synchronized static Networker getInstance(){
        if ( networker == null ){
            networker = new Networker();
        }
        return networker;
    }
    public void posting(Context context, String url, JSONObject params, final ResponseBridge responseBridge ){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        responseBridge.onSuccess( response.toString() );
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseBridge.onError( error );

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> para = new HashMap<String, String>();
                para.put("Content-Type", "application/json");
                return para;
            }
        } ;
        RequestQueSingleTon.getInstance( context ).getRequestQue().add( jsonObjectRequest );
    }

    public interface ResponseBridge{
        void onSuccess(String response );
        void onError(VolleyError error );
    }
}
