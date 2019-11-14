package com.goodlineapp.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.MvpActivity;
import com.goodlineapp.R;
import com.goodlineapp.ui.listeners.EndlessRecyclerViewScrollListener;

import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivitySearch extends MvpActivity  {
    @BindView(R.id.tbToolbar) Toolbar tbToolbar;
    @BindView(R.id.rvUsersSearchResult) RecyclerView rvSearchResult;
    @BindView(R.id.tvUsersSearchNothingFound) TextView tvSearchNothingFound;
    @BindView(R.id.strUserSearchRefreshContainer) SwipeRefreshLayout strSearchRefreshContainer;

    public LinearLayoutManager linearLayoutManager;
    public EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initToolbar();
        initRecyclerView();
        initRefreshContainer();
    }

    public abstract void initToolbar();

    public abstract void initRefreshContainer();

    public  void initRecyclerView()
    {
        linearLayoutManager = new LinearLayoutManager(this);
        rvSearchResult.setLayoutManager(linearLayoutManager);
        rvSearchResult.setHasFixedSize(true);
    }

    public abstract void AdapterCallbackClickListener(View view, Integer position);

    public void ShowMessage(Integer messageRes) {
        Snackbar.make(rvSearchResult, getResources().getString(messageRes), Snackbar.LENGTH_SHORT).show();
    }

    public void HideRefreshIndicator() {
        if (strSearchRefreshContainer.isRefreshing()) {
            strSearchRefreshContainer.setRefreshing(false);
        }
    }
}

