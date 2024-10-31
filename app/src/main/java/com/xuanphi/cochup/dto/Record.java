package com.xuanphi.cochup.dto;

import java.util.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("recordId")
    @Expose
    private long recordId;
    @SerializedName("userId")
    @Expose
    private long userId;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("difficulty")
    @Expose
    private Difficulty difficulty;
    @SerializedName("highScore")
    @Expose
    private long highScore;
    @SerializedName("recordDate")
    @Expose
    private Date recordDate;

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public Record withRecordId(long recordId) {
        this.recordId = recordId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Record withUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Record withCategory(Category category) {
        this.category = category;
        return this;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Record withDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public long getHighScore() {
        return highScore;
    }

    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }

    public Record withHighScore(long highScore) {
        this.highScore = highScore;
        return this;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public Record withRecordDate(Date recordDate) {
        this.recordDate = recordDate;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Record.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("recordId");
        sb.append('=');
        sb.append(this.recordId);
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(this.userId);
        sb.append(',');
        sb.append("category");
        sb.append('=');
        sb.append(((this.category == null)?"<null>":this.category));
        sb.append(',');
        sb.append("difficulty");
        sb.append('=');
        sb.append(((this.difficulty == null)?"<null>":this.difficulty));
        sb.append(',');
        sb.append("highScore");
        sb.append('=');
        sb.append(this.highScore);
        sb.append(',');
        sb.append("recordDate");
        sb.append('=');
        sb.append(((this.recordDate == null)?"<null>":this.recordDate));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}