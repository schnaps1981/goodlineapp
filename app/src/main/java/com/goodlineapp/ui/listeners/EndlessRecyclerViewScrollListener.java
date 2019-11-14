package com.goodlineapp.ui.listeners;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.goodlineapp.app.Constants;
import com.goodlineapp.data.vkapi.utils.NetworkState;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    //расстяоние в ячейках, на которое нужно домотать вниз, чтобы началась подгрузка
    private int visibleThreshold = Constants.PAGE_PREFETCH_DISTANCE;

    private NetworkState networkState;
    private RecyclerView.LayoutManager mLayoutManager;

    private boolean state_busy = false;

    public void UpdateNetworkStatus(NetworkState state)
    {
        this.networkState = state;
    }

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        state_busy = networkState.getStatus() == NetworkState.Status.RUNNING;

        int lastVisibleItemPosition = 0;

        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        //Определяем, что скролл подходит к концу и требуются еще данные
        if ((lastVisibleItemPosition + visibleThreshold > totalItemCount)   //если до самого низа осталось строк меньше чем "расстояние загрузки"
                && totalItemCount != 0                                      //если это не начальная загрузка
                && !state_busy                                              //если в данный момент загрузки данных нет
                && dy > 0)                                                  //если скроллим вниз
        {
            onLoadMore();                                                   //загрузи даанные!
            state_busy = true;                                              //становится занятым, пока идет подгрузка
        }
    }

    public abstract void onLoadMore();
}