package com.aswamedha.aswamedhaeducation.questions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aswamedha.aswamedhaeducation.AswamedhamApplication;
import com.aswamedha.aswamedhaeducation.R;
import com.aswamedha.aswamedhaeducation.questions.exam.YoutubeVideoModel;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class YtdAdapter extends RecyclerView.Adapter<YtdAdapter.YoutubViewHolder> {
    private List<YoutubeVideoModel> ytdList;
    private Context context;
    private ClickListener clickListener;

    public YtdAdapter(List<YoutubeVideoModel> videos, Context context, ClickListener clickListener ){
        this.ytdList = videos;
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

        final YoutubeVideoModel videoModel = ytdList.get(i);
        youtubeViewHolder.youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.click( videoModel );
            }
        });
        youtubeViewHolder.txtDescription.setText(videoModel.getDescription());
        Glide.with(context).load(videoModel.getThumbNailUrl()).into(youtubeViewHolder.youTubeThumbnailView);
    }

    @Override
    public int getItemCount() {
        return this.ytdList.size();
    }

    public class YoutubViewHolder extends RecyclerView.ViewHolder{
        private ImageView youTubeThumbnailView;
        private TextView txtDescription;
        public YoutubViewHolder(View view){
            super(view);
            youTubeThumbnailView = view.findViewById( R.id.video_thumbnail_image_view );
            txtDescription = view.findViewById(R.id.txt_descrip);
        }
    }
    public interface ClickListener{
        void click(YoutubeVideoModel youtubeVideoModel );
    }
}
