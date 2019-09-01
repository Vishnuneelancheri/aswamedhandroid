package com.aswamedha.aswamedhapsc.networking;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueSingleTon {
    private static RequestQueSingleTon requestQueSingleTon;
    private Context mContext;
    private RequestQueue requestQueue;
    public RequestQueSingleTon(Context context){
        mContext = context;
        requestQueue = getRequestQue();
    }
    public static synchronized RequestQueSingleTon getInstance(Context context){

        if ( requestQueSingleTon == null ){
            requestQueSingleTon = new RequestQueSingleTon( context );
        }
        return requestQueSingleTon;
    }

    public RequestQueue getRequestQue(){
        if ( requestQueue == null ){
            requestQueue = Volley.newRequestQueue( mContext.getApplicationContext() );
        }
        return requestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQue().add( req );
    }

}
