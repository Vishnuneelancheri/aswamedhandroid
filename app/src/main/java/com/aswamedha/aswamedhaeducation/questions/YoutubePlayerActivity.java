package com.aswamedha.aswamedhaeducation.questions;

import android.os.Bundle;

import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayerActivity extends YouTubeBaseActivity {
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);

        youTubePlayerView = findViewById( R.id.youtube_player_view );
        String ytdId = getIntent().getStringExtra(AswamedhamApplication.YTD_ID );
        initialize( ytdId );

    }
    private void initialize(final String id ){
//        youTubePlayerView.initialize(AswamedhamApplication.YTD_API_KEY, new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                if ( !b ){
//                    youTubePlayer.setPlayerStyle( YouTubePlayer.PlayerStyle.DEFAULT);
//                    youTubePlayer.loadVideo( id );
//
//                }
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//            }
//        });
    }
}
