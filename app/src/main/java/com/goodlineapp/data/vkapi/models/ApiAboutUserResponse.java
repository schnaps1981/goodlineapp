package com.goodlineapp.data.vkapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ApiAboutUserResponse {
    @SerializedName("response")
    @Expose
    private ArrayList<UsersSearchItem> response = null;

    public ArrayList<UsersSearchItem> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<UsersSearchItem> response) {
        this.response = response;
    }

}

