package com.aswamedha.aswamedhaeducation.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menu1Response {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("menu_1")
    private List<Menu1Model> menu1ModelList;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Menu1Model> getMenu1ModelList() {
        return menu1ModelList;
    }
}
