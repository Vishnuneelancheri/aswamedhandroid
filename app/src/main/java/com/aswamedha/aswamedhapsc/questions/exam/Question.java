package com.aswamedha.aswamedhapsc.questions.exam;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {
    private String question;
    private String questId;
    private String anubandham;
    private String answerOptionId;
    private List<Option> optionList;
    private Option selectedOption;

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }

    public List<Option> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Option> optionList) {
        this.optionList = optionList;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getAnubandham() {
        return anubandham;
    }

    public void setAnubandham(String anubandham) {
        this.anubandham = anubandham;
    }

    public String getAnswerOptionId() {
        return answerOptionId;
    }

    public void setAnswerOptionId(String answerOptionId) {
        this.answerOptionId = answerOptionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.questId);
        dest.writeString(this.anubandham);
        dest.writeString(this.answerOptionId);
        dest.writeTypedList(this.optionList);
        dest.writeParcelable(this.selectedOption, flags);
    }

    public Question() {
    }

    protected Question(Parcel in) {
        this.question = in.readString();
        this.questId = in.readString();
        this.anubandham = in.readString();
        this.answerOptionId = in.readString();
        this.optionList = in.createTypedArrayList(Option.CREATOR);
        this.selectedOption = in.readParcelable(Option.class.getClassLoader());
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
