package com.aswamedha.aswamedhapsc.empnewsnoticeboarddailynotes;

import com.google.gson.annotations.SerializedName;

public class CategoryOne {
    @SerializedName("menu_name")
    private String menuName;

    @SerializedName("is_active")
    private int isActive;

    @SerializedName("id")
    private String id;

    public String getMenuName() {
        return menuName;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getId() {
        return id;
    }
}
