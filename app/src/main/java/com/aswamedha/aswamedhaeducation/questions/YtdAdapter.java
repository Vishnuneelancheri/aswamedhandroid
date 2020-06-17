package com.aswamedha.aswamedhaeducation.questions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class YtdAdapter extends RecyclerView.Adapter<YtdAdapter.YoutubViewHolder> {
    private String[] ytdList;
    private Context context;
    private ClickListener clickListener;

    public YtdAdapter( String[] ytdList, Context context, ClickListener clickListener ){
        this.ytdList = ytdList;
        this.context = context;
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public YoutubViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.row_ytd, viewGroup, false);
        return new YoutubViewHolder( view );

    }

    @Override
    public void onBindViewHolder(@NonNull YoutubViewHolder youtubeViewHolder, final int i) {
        youtubeViewHolder.youTubeThumbnailView.initialize(AswamedhamApplication.YTD_API_KEY,
                new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess( YouTubeThumbnailView youTubeThumbnailView,
                                                         final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo( ytdList[i]);

                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                                youTubeThumbnailLoader.release();
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                                //print or show error when thumbnail load failed

                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
        youtubeViewHolder.youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.click( ytdList[i] );
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.ytdList.length;
    }

    public class YoutubViewHolder extends RecyclerView.ViewHolder{
        private YouTubeThumbnailView youTubeThumbnailView;
        public YoutubViewHolder(View view){
            super(view);
            youTubeThumbnailView = view.findViewById( R.id.video_thumbnail_image_view );

        }
    }
    public interface ClickListener{
        void click(String  id );
    }
}
