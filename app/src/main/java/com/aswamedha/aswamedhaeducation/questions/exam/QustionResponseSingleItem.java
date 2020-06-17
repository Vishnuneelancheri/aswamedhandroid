package com.aswamedha.aswamedhaeducation.questions.exam;

import com.google.gson.annotations.SerializedName;

public class QustionResponseSingleItem {
    @SerializedName("qtn_id")
    private String qtnid;

    @SerializedName("qtn_name")
    private String qtnName;

    @SerializedName("anubandham")
    private String anumbandham;

    @SerializedName("option_tag")
    private String optionTag;

    @SerializedName("qtun_header_id")
    private String qtnHdrId;

    @SerializedName("answr_option_id")
    private String answerOptionId;

    @SerializedName("option_id")
    private String optionId;

    @SerializedName("option_valu")
    private String optionValue;

    public String getQtnid() {
        return qtnid;
    }

    public String getQtnName() {
        return qtnName;
    }

    public String getAnumbandham() {
        return anumbandham;
    }

    public String getOptionTag() {
        return optionTag;
    }

    public String getQtnHdrId() {
        return qtnHdrId;
    }

    public String getAnswerOptionId() {
        return answerOptionId;
    }

    public String getOptionId() {
        return optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }
}
