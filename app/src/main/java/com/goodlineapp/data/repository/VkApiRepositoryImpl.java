package com.goodlineapp.data.repository;

import com.goodlineapp.app.App;
import com.goodlineapp.data.vkapi.datasource.VkApiDataSource;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.subjects.PublishSubject;

public class VkApiRepositoryImpl implements VkApiRepository{

    @Inject
    VkApiDataSource vkApiDataSource;
    private String request = "";
    private String method = "";

    public VkApiRepositoryImpl()
    {
        App.getComponent().inject(this);
    }

    @Override
    public void TryFetchApiData() {
        vkApiDataSource.tryAgain();
    }

    @Override
    public void LoadUsers(String request, String method) {
        this.request = request;
        this.method = method;
        vkApiDataSource.ResetRequestPosition();
        vkApiDataSource.loadMoreUsers(request, method, -1L);
    }

    @Override
    public void LoadUsers(String request, String method, Long user_id) {
        this.request = request;
        this.method = method;
        vkApiDataSource.ResetRequestPosition();
        vkApiDataSource.loadMoreUsers(request, method, user_id);
    }

    @Override
    public void LoadAboutMe(Long user_id) {
        vkApiDataSource.loadAboutUser(user_id);
    }

    @Override
    public void LoadUsersNextPage() {
        vkApiDataSource.loadMoreUsers(request, method);
    }

    @Override
    public void RefreshSearch(String request, String method) {
        this.request = request;
        this.method = method;
        vkApiDataSource.ResetRequestPosition();
        vkApiDataSource.loadMoreUsers(request, method, -1L);
    }

    @Override
    public void RefreshSearch(String request, String method, Long user_id) {
        this.request = request;
        this.method = method;
        vkApiDataSource.ResetRequestPosition();
        vkApiDataSource.loadMoreUsers(request, method, user_id);
    }

    @Override
    public PublishSubject<UsersSearchItem> getAboutMe() {
        return vkApiDataSource.getAboutMe();
    }

    @Override
    public PublishSubject<NetworkState> getNetworkState() {
        return vkApiDataSource.getNetworkState();
    }

    @Override
    public PublishSubject<ArrayList<UsersSearchItem>> getUserList() {
        return vkApiDataSource.getLiveResponseVkApi();
    }

    @Override
    public PublishSubject<Boolean> getRefreshStatus() {
        return vkApiDataSource.getRefreshStatus();
    }




}
