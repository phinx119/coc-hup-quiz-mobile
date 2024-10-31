package com.xuanphi.cochup.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("categoryId")
    @Expose
    private long categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("categoryValue")
    @Expose
    private long categoryValue;

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Category withCategoryId(long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category withCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public long getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(long categoryValue) {
        this.categoryValue = categoryValue;
    }

    public Category withCategoryValue(long categoryValue) {
        this.categoryValue = categoryValue;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Category.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("categoryId");
        sb.append('=');
        sb.append(this.categoryId);
        sb.append(',');
        sb.append("categoryName");
        sb.append('=');
        sb.append(((this.categoryName == null)?"<null>":this.categoryName));
        sb.append(',');
        sb.append("categoryValue");
        sb.append('=');
        sb.append(this.categoryValue);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}