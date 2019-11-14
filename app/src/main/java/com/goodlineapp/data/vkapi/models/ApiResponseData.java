package com.goodlineapp.data.vkapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiResponseData {
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("items")
    @Expose
    private ArrayList<UsersSearchItem> items = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<UsersSearchItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<UsersSearchItem> items) {
        this.items = items;
    }
}
