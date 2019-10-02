package com.aswamedha.aswamedhapsc.empnewsnoticeboarddailynotes;

import com.google.gson.annotations.SerializedName;

public class Details {
    @SerializedName("header")
    private String header;

    @SerializedName("details")
    private String details;

    @SerializedName("is_active")
    private int isActive;

    @SerializedName("id")
    private String id;

    public String getHeader() {
        return header;
    }

    public String getDetails() {
        return details;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getId() {
        return id;
    }
}
