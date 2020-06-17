package com.aswamedha.aswamedhaeducation.questions;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeListFragment extends Fragment {
    private String[] ytLinks = {"bvI9mh6D0gQ", "ITdjtQ2AX8U",
            "STj9_7xLtx8", "YoIejGfpw4k","0eHEBi1uFn8&t=37s",
            "wIPPMs_9Vqg","4y-NMQ_opoc"};

    public YoutubeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_youtube_list, container, false);
        RecyclerView recyclerView = view.findViewById( R.id.recy_ytd );
        final Activity activity = getActivity();

        YtdAdapter ytdAdapter = new YtdAdapter(ytLinks, activity, new YtdAdapter.ClickListener() {
            @Override
            public void click(String id) {

                Intent intent = new Intent( activity, YoutubePlayerActivity.class );
                intent.putExtra( AswamedhamApplication.YTD_ID, id);
                activity.startActivity( intent );

            }
        });
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( activity );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter( ytdAdapter );
        view.findViewById( R.id.parent ).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

}
