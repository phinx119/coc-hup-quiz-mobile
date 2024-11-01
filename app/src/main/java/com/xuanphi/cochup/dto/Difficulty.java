package com.xuanphi.cochup.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Difficulty {

    @SerializedName("difficultyId")
    @Expose
    private long difficultyId;
    @SerializedName("difficultyName")
    @Expose
    private String difficultyName;

    public long getDifficultyId() {
        return difficultyId;
    }

    public void setDifficultyId(long difficultyId) {
        this.difficultyId = difficultyId;
    }

    public Difficulty withDifficultyId(long difficultyId) {
        this.difficultyId = difficultyId;
        return this;
    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    public Difficulty withDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Difficulty.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("difficultyId");
        sb.append('=');
        sb.append(this.difficultyId);
        sb.append(',');
        sb.append("difficultyName");
        sb.append('=');
        sb.append(((this.difficultyName == null)?"<null>":this.difficultyName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}