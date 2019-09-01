package com.aswamedha.aswamedhapsc.questions.exam;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionResponse {
    @SerializedName("data")
    private List<QustionResponseSingleItem>  qustionResponseSingleItems;

    public List<QustionResponseSingleItem> getQustionResponseSingleItems() {
        return qustionResponseSingleItems;
    }
}
