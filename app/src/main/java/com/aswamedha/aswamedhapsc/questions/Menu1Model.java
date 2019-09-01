package com.aswamedha.aswamedhapsc.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Menu1Model implements Parcelable {
    @SerializedName("menu_1_id")
    private String menu1Id;

    @SerializedName("menu_1_name")
    private String menu1Name;

    @SerializedName("fk_main_menu_id")
    private String fkMainMenuId;

    public String getMenu1Id() {
        return menu1Id;
    }

    public String getMenu1Name() {
        return menu1Name;
    }

    public String getFkMainMenuId() {
        return fkMainMenuId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.menu1Id);
        dest.writeString(this.menu1Name);
        dest.writeString(this.fkMainMenuId);
    }

    public Menu1Model() {
    }

    protected Menu1Model(Parcel in) {
        this.menu1Id = in.readString();
        this.menu1Name = in.readString();
        this.fkMainMenuId = in.readString();
    }

    public static final Parcelable.Creator<Menu1Model> CREATOR = new Parcelable.Creator<Menu1Model>() {
        @Override
        public Menu1Model createFromParcel(Parcel source) {
            return new Menu1Model(source);
        }

        @Override
        public Menu1Model[] newArray(int size) {
            return new Menu1Model[size];
        }
    };
}
