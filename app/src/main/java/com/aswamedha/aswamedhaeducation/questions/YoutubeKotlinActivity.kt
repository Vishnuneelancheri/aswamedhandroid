package com.aswamedha.aswamedhaeducation.questions
import android.os.Bundle
import com.aswamedha.aswamedhaeducation.AswamedhamApplication
import com.aswamedha.aswamedhaeducation.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_youtube_player.*
import java.sql.Time
import java.sql.Timestamp
import java.util.*

class YoutubeKotlinActivity: YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    var videoId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)
        val intent = intent

        videoId = intent.getStringExtra(AswamedhamApplication.YTD_ID)
        //val youtubeView = findViewById<?>( R.id.youtube_player_view)
        youtube_player_view.initialize("AIzaSyApcTm6oJyoLv3aIbxSV0eBSYyhCn5UBx0", this)
    }

    override fun onInitializationSuccess(
            p0: YouTubePlayer.Provider?,
            player: YouTubePlayer?,
            restored: Boolean
    ) {
        if (!restored){
//            player!!.cueVideo(videoId)
            player!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
            player.loadVideo(videoId)
        }
    }
    override fun onInitializationFailure(
            p0: YouTubePlayer.Provider?,
            p1: YouTubeInitializationResult?
    ) {

    }
}