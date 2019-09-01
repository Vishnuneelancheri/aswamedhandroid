package com.aswamedha.aswamedhapsc.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainMenuResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("menu")
    private List<MainMenuModel> mainMenuModelList;

    @SerializedName("is_independant")
    private String isIndependant;


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<MainMenuModel> getMainMenuModelList() {
        return mainMenuModelList;
    }

    public String getIsIndependant() {
        return isIndependant;
    }
}
