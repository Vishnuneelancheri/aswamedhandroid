package com.aswamedha.aswamedhapsc.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MltpleChoiceHdrResponse {
    @SerializedName("status")
    private int status;

    @SerializedName("messag")
    private String message;

    @SerializedName("header")
    private List<MltpleChoiceHeader> mltpleChoiceHeaderList;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<MltpleChoiceHeader> getMltpleChoiceHeaderList() {
        return mltpleChoiceHeaderList;
    }
}
