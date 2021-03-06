package com.aswamedha.aswamedhaeducation.questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MltpleChoiceHeader implements Parcelable {
    @SerializedName("hdr_id")
    private long hdrId;

    @SerializedName("hdr")
    private String hdr;

    @SerializedName("is_finished")
    private int isFinished;

    public int getIsFinished(){ return isFinished; }

    public long getHdrId() {
        return hdrId;
    }

    public String getHdr() {
        return hdr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.hdrId);
        dest.writeString(this.hdr);
        dest.writeInt(this.isFinished);
    }

    public MltpleChoiceHeader() {
    }

    protected MltpleChoiceHeader(Parcel in) {
        this.hdrId = in.readLong();
        this.hdr = in.readString();
        this.isFinished = in.readInt();
    }

    public static final Parcelable.Creator<MltpleChoiceHeader> CREATOR = new Parcelable.Creator<MltpleChoiceHeader>() {
        @Override
        public MltpleChoiceHeader createFromParcel(Parcel source) {
            return new MltpleChoiceHeader(source);
        }

        @Override
        public MltpleChoiceHeader[] newArray(int size) {
            return new MltpleChoiceHeader[size];
        }
    };
}
