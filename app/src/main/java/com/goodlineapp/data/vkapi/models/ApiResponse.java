package com.goodlineapp.data.vkapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("response")
    @Expose
    private ApiResponseData response;

    public ApiResponseData getResponse() {
        return response;
    }

    public void setResponse(ApiResponseData response) {
        this.response = response;
    }
}
