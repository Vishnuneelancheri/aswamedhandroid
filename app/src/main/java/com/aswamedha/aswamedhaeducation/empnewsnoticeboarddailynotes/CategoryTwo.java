package com.aswamedha.aswamedhaeducation.empnewsnoticeboarddailynotes;

import com.google.gson.annotations.SerializedName;

public class CategoryTwo {
    @SerializedName("sub_menu_name")
    private String subMenuName;

    @SerializedName("is_active")
    private int isActive;

    @SerializedName("id")
    private String id;

    public String getSubMenuName() {
        return subMenuName;
    }

    public int getIsActive() {
        return isActive;
    }

    public String getId() {
        return id;
    }
}
