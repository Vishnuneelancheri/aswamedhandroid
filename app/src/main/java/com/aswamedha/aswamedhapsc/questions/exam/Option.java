package com.aswamedha.aswamedhapsc.questions.exam;

import android.os.Parcel;
import android.os.Parcelable;

public class Option implements Parcelable {
    private String optionId;
    private String optionValue;
    private String optionTag;

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getOptionTag() {
        return optionTag;
    }

    public void setOptionTag(String optionTag) {
        this.optionTag = optionTag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.optionId);
        dest.writeString(this.optionValue);
        dest.writeString(this.optionTag);
    }

    public Option() {
    }

    protected Option(Parcel in) {
        this.optionId = in.readString();
        this.optionValue = in.readString();
        this.optionTag = in.readString();
    }

    public static final Parcelable.Creator<Option> CREATOR = new Parcelable.Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel source) {
            return new Option(source);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };
}
