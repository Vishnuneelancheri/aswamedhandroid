package com.aswamedha.aswamedhapsc.login;

import com.google.gson.annotations.SerializedName;

public class PhoneRegModel {
    @SerializedName("reg_token")
    private String regToken;

    @SerializedName("token")
    private String token;

    @SerializedName("reg_id")
    private int regId;

    @SerializedName("message")
    private String message;

    @SerializedName("isRegd")
    private String isRegd;

    public String getRegToken() {
        return regToken;
    }

    public int getRegId() {
        return regId;
    }

    public String getMessage() {
        return message;
    }

    public String getIsRegd() {
        return isRegd;
    }

    public String getToken() {
        return token;
    }
}
