package com.goodlineapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;
import com.goodlineapp.presenters.ActivityFriendsPresenter;
import com.goodlineapp.ui.adapters.FriendsListAdapter;
import com.goodlineapp.ui.listeners.EndlessRecyclerViewScrollListener;
import com.goodlineapp.ui.views.ActivityFriendsView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import timber.log.Timber;

public class ActivityFriends extends BaseActivitySearch implements ActivityFriendsView {

    private FriendsListAdapter usersFriendsSearchResultAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private UsersSearchItem user_details;

    @InjectPresenter
    ActivityFriendsPresenter activityFriendsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user_details = intent.getParcelableExtra("data");

        LoadFriendsList();
    }

    @Override
    public void initRefreshContainer() {
        strSearchRefreshContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        strSearchRefreshContainer.setOnRefreshListener(() -> {
            if (usersFriendsSearchResultAdapter.getItemCount() == 0) {
                HideRefreshIndicator();
                return; //не обрабатывать, если список пуст
            }
            usersFriendsSearchResultAdapter.clearList();
            activityFriendsPresenter.RefreshSearch(user_details.getId());
        });
    }

    @Override
    public void initRecyclerView() {
        usersFriendsSearchResultAdapter = new FriendsListAdapter(this::AdapterCallbackClickListener);


        linearLayoutManager = new LinearLayoutManager(this);
        rvSearchResult.setLayoutManager(linearLayoutManager);
        rvSearchResult.setHasFixedSize(true);
        rvSearchResult.setAdapter(usersFriendsSearchResultAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                Timber.d("onLoadMore");
               activityFriendsPresenter.LoadNextPage();
            }
        };

        rvSearchResult.addOnScrollListener(scrollListener);
    }
    @Override
    public void initToolbar() {
        tbToolbar.setTitle(getResources().getString(R.string.friends));
        tbToolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleStyle);

        //Обработчик "назад"
        tbToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_toolbar_back));
        tbToolbar.setNavigationOnClickListener(view -> ActivityFriends.super.onBackPressed());
    }

    @Override
    public void AdapterCallbackClickListener(View view, Integer position) {
        Timber.d("Item was clicked");
        if (view.getId() == R.id.btnNetworkStateRetry) {
            activityFriendsPresenter.TryFetchApiData();
        }
    }

    @Override
    public void ShowMessage(Integer messageRes) {
        Snackbar.make(rvSearchResult, getResources().getString(messageRes), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void ShowSearchResult(ArrayList<UsersSearchItem> list) {
        usersFriendsSearchResultAdapter.addItemsToList(list);
        Timber.d("Friends activity. List recived");
    }

    @Override
    public void ShowNetworkState(NetworkState networkState) {
        scrollListener.UpdateNetworkStatus(networkState);
        if (networkState.getStatus() == NetworkState.Status.NO_DATA) {
            tvSearchNothingFound.setVisibility(View.VISIBLE);
        } else {
            tvSearchNothingFound.setVisibility(View.GONE);
        }
        usersFriendsSearchResultAdapter.UpdateFooter(networkState);
    }

    private void LoadFriendsList() {
        Timber.d("Friends Activity. request friends");
        rvSearchResult.scrollToPosition(0);
        usersFriendsSearchResultAdapter.clearList();
        activityFriendsPresenter.SubmitRequest(user_details.getId());
    }

}
