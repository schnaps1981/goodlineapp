package com.goodlineapp.ui.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

import java.util.ArrayList;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ActivityFriendsView extends MvpView {
    void ShowMessage(Integer messageRes);
    void ShowSearchResult(ArrayList<UsersSearchItem> list);
    void ShowNetworkState(NetworkState networkState);
    void HideRefreshIndicator();
}
