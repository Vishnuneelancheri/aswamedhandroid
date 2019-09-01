package com.aswamedha.aswamedhapsc.questions;

import com.google.gson.annotations.SerializedName;

public class MainMenuModel {
    @SerializedName("main_menu_id")
    private int mainMenuId;

    @SerializedName("m_menu_name")
    private String menuName;

    public int getMainMenuId() {
        return mainMenuId;
    }

    public String getMenuName() {
        return menuName;
    }
}
