package com.goodlineapp.ui.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ActivityLoginView extends MvpView {
    void StartActivitySearch();
    void ShowError(Integer messageRes);

}
