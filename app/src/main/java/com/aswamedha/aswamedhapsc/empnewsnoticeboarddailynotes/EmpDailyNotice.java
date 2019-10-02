package com.aswamedha.aswamedhapsc.empnewsnoticeboarddailynotes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmpDailyNotice {
    @SerializedName("status")
    private Status status;

    @SerializedName("main_menu")
    private List<CategoryOne> categoryOneList;

    @SerializedName("sub_menu")
    private List<CategoryTwo> categoryTwoList;

    @SerializedName("details")
    private List<Details> detailsList;

    public Status getStatus() {
        return status;
    }

    public List<CategoryOne> getCategoryOneList() {
        return categoryOneList;
    }

    public List<CategoryTwo> getCategoryTwoList() {
        return categoryTwoList;
    }

    public List<Details> getDetailsList() {
        return detailsList;
    }

    protected class Status{
        @SerializedName("status")
        private int status;
        @SerializedName("message")
        private String message;

        public int getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
