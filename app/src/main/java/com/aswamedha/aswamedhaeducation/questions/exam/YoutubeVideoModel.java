package com.aswamedha.aswamedhaeducation.questions.exam;

import com.google.gson.annotations.SerializedName;

public class YoutubeVideoModel {
    @SerializedName("id_video")
    private int videoId;
    @SerializedName("youtube_id")
    private String youtubeVideoId;
    @SerializedName("is_active")
    private int isActive;
    @SerializedName("video_short_description")
    private String description;

    @SerializedName("thumbnail_url")
    private String thumbNailUrl;

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
