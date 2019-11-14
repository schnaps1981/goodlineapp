package com.goodlineapp.ui.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ActivityAboutMeView extends MvpView {
    void SetItemsVisible();
    void SetItemsInvisible();
    void ShowSearchResult(UsersSearchItem item);
    void ShowNetworkState(NetworkState networkState);
}
