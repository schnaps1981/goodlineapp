package com.goodlineapp.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;
import com.goodlineapp.presenters.ActivitySearchPresenter;
import com.goodlineapp.ui.adapters.SearchListAdapter;
import com.goodlineapp.ui.listeners.EndlessRecyclerViewScrollListener;
import com.goodlineapp.ui.views.ActivitySearchView;

import java.util.ArrayList;

import timber.log.Timber;

public class ActivitySearch extends BaseActivitySearch implements ActivitySearchView {

    private SearchListAdapter usersSearchResultAdapter;
    private SearchView searchView;


    @InjectPresenter
    ActivitySearchPresenter activitySearchPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initRefreshContainer() {
        strSearchRefreshContainer.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        strSearchRefreshContainer.setOnRefreshListener(() -> {
            if (usersSearchResultAdapter.getItemCount() == 0) {
                HideRefreshIndicator();
                return; //не обрабатывать, если список пуст
            }
            usersSearchResultAdapter.clearList();
            activitySearchPresenter.RefreshSearch();
        });
    }

    @Override
    public void initRecyclerView() {
        super.initRecyclerView();
        usersSearchResultAdapter = new SearchListAdapter(this::AdapterCallbackClickListener);
        rvSearchResult.setAdapter(usersSearchResultAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                Timber.d("onLoadMore");
                activitySearchPresenter.LoadNextPage();
            }
        };

        rvSearchResult.addOnScrollListener(scrollListener);
    }

    @Override
    public void initToolbar() {
        tbToolbar.inflateMenu(R.menu.menu_activity_main);
        searchView = (SearchView) tbToolbar.getMenu().findItem(R.id.svActionSearch).getActionView();
        searchView.setOnQueryTextListener(QueryTextListener);

        tbToolbar.setOnMenuItemClickListener(menuItemClickListener);
    }

    Toolbar.OnMenuItemClickListener menuItemClickListener = item -> {
        Timber.d("AboutMe selected");
        int menu_id = item.getItemId();
        if (menu_id == R.id.btnAboutMe)
        {
            Intent intent = new Intent(ActivitySearch.this, ActivityAboutMe.class);
            startActivity(intent);
            return true;
        }
        return false;
    };


    @Override
    public void AdapterCallbackClickListener(View view, Integer position) {
        Timber.d("Item was clicked");
        if (view.getId() == R.id.btnNetworkStateRetry) {
            activitySearchPresenter.TryFetchApiData();
        }
        if (view.getId() == R.id.rlUserInfo) {
            //Убираем фокус с вью, чтобы не вылазила клавиатура после просмотра деталей
            searchView.clearFocus();

            UsersSearchItem item = usersSearchResultAdapter.getItem(position);
            Intent intent = new Intent(this, ActivityUserDetails.class);
            intent.putExtra("user", item);
            startActivity(intent);
        }
    }

    private SearchView.OnQueryTextListener QueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            Timber.d("onQueryTextSubmit");
            rvSearchResult.scrollToPosition(0);
            usersSearchResultAdapter.clearList();
            activitySearchPresenter.SubmitRequest(query);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Timber.d("onQueryTextChange");
            return false;
        }
    };

    @Override
    public void ShowSearchResult(ArrayList<UsersSearchItem> list) {
        usersSearchResultAdapter.addItemsToList(list);
        Timber.d("List recived");
    }

    @Override
    public void ShowNetworkState(NetworkState networkState) {
        scrollListener.UpdateNetworkStatus(networkState);
        if (networkState.getStatus() == NetworkState.Status.NO_DATA) {
            tvSearchNothingFound.setVisibility(View.VISIBLE);
        } else {
            tvSearchNothingFound.setVisibility(View.GONE);
        }
        usersSearchResultAdapter.UpdateFooter(networkState);
    }

    @Override
    public void SetStateSearch() {
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
    }
}
