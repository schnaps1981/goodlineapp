package com.goodlineapp.data.repository;

import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

public interface VkApiRepository {
    void TryFetchApiData();
    void LoadUsers(String request, String method);
    void LoadUsers(String request, String method, Long user_id);
    void LoadAboutMe(Long user_id);
    void LoadUsersNextPage();
    void RefreshSearch(String request, String method);
    void RefreshSearch(String request, String method, Long user_id);

    PublishSubject<NetworkState> getNetworkState();
    PublishSubject<ArrayList<UsersSearchItem>> getUserList();
    PublishSubject<Boolean> getRefreshStatus();
    PublishSubject<UsersSearchItem> getAboutMe();

}
