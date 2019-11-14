package com.goodlineapp.data.vkapi.api;

import androidx.annotation.NonNull;

import com.goodlineapp.data.vkapi.models.ApiAboutUserResponse;
import com.goodlineapp.data.vkapi.models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VKApiInterface {
    @GET("/method/{method_name}")
    Call<ApiResponse> users_search(
            @Path("method_name") @NonNull String method,
            @Query("user_id") Long user_id,     //необязательное поле. передавать при запросе друзей
            @Query("q") String q,               //необязательное поле. передавать при запосе поиска людей
            @Query("count") Integer count,
            @Query("offset") Integer offset,
            @Query("fields") String fields,
            @Query("access_token") String access_token,
            @Query("v") String v,
            @Query("lang") String lang
    );

    @GET("/method/users.get")
    Call<ApiAboutUserResponse> about_me(
            @Query("user_ids") @NonNull Long user_ids,
            @Query("fields") String fields,
            @Query("name_case") String  name_case,
            @Query("access_token") String access_token,
            @Query("v") String v,
            @Query("lang") String lang
    );
}

