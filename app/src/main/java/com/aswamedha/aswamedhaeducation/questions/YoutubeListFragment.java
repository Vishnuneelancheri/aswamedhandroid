package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.MyProgressDialog;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.networking.Networker;
import com.aswamedha.aswamedhaeducation.questions.exam.YoutubeVideoModel;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeListFragment extends Fragment {
    /*private String[] ytLinks = {"bvI9mh6D0gQ", "ITdjtQ2AX8U",
            "STj9_7xLtx8", "YoIejGfpw4k","0eHEBi1uFn8&t=37s",
            "wIPPMs_9Vqg","4y-NMQ_opoc"};*/
    private RecyclerView recyclerView;
    public YoutubeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_youtube_list, container, false);
        recyclerView = view.findViewById( R.id.recy_ytd );
        final Activity activity = getActivity();
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( activity );
        recyclerView.setLayoutManager( layoutManager );

        getAllVideos();

        return view;
    }
    private void getAllVideos(){
        final JSONObject params = new JSONObject();
        try{
            params.put("admin_id", "2" );
            params.put("token", "5" );
            params.put("mode","2");

        }catch (JSONException e ){

        }
        final MyProgressDialog myProgressDialog = new MyProgressDialog( getContext() );
        myProgressDialog.setCancelable( false );
        myProgressDialog.setCanceledOnTouchOutside( false );
        myProgressDialog.setColor( R.color.colorAccent );
        myProgressDialog.show();
        String url = AswamedhamApplication.IP_ADDRESS + "AdminLoginAction/get_all_videos";
        Networker.getInstance().posting(getContext(), url, params, new Networker.ResponseBridge() {
            @Override
            public void onSuccess(String response) {
                parseResponse( response );
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(VolleyError error) {
                Toast.makeText( getContext(), error.toString(), Toast.LENGTH_LONG ).show();
                myProgressDialog.dismiss();
            }
        });
    }
    private void parseResponse(String response){
         try{
             YtdResponse ytdResponse = new Gson().fromJson(response, YtdResponse.class);
             if ( ytdResponse.getVideos().size() > 0 ){
                 YtdAdapter ytdAdapter = new YtdAdapter(ytdResponse.getVideos(), getContext(), new YtdAdapter.ClickListener() {
                     @Override
                     public void click(YoutubeVideoModel youtubeVideoModel) {
                         Activity activity = getActivity();
                         Intent intent = new Intent( activity, YoutubeKotlinActivity.class );
                         intent.putExtra( AswamedhamApplication.YTD_ID, youtubeVideoModel.getYoutubeVideoId());
                         activity.startActivity( intent );
                     }
                 });

                 recyclerView.setAdapter( ytdAdapter );
//                 view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
//                     @Override
//                     public void onClick(View v) {
//
//                     }
//                 });
             }
         }catch (Exception e ){

         }

    }

    private class YtdResponse{
        @SerializedName("data")
        private List<YoutubeVideoModel> videos;
        public List<YoutubeVideoModel> getVideos(){
            return videos;
        }
    }

}
