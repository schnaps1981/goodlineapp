package com.goodlineapp.ui.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goodlineapp.data.vkapi.utils.NetworkState;

import java.util.ArrayList;

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_NETWORK_STATUS = 0;
    private static final int TYPE_ITEM = 1;
    private boolean isFooter = false;
    private NetworkState oldnetState, networkState = null;

    private ArrayList<T> itemsList = new ArrayList<>();

    public void addItemsToList(ArrayList<T> itemsList) {
        this.itemsList.addAll(itemsList);
        notifyDataSetChanged();
    }

    public <T> T getItem(Integer position) {
        return (T) itemsList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter && isFooterPosition(position)) {
            return TYPE_NETWORK_STATUS;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (itemsList != null) itemCount = itemsList.size();
        if (isFooter) itemCount++;
        return itemCount;
    }

    @NonNull
    @Override
    public abstract RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position);

    private boolean isFooterPosition(int position) {
        return position == getItemCount() - 1;
    }

    public NetworkState getNetworkState() {
        return networkState;
    }

    public void UpdateFooter(NetworkState netState) {
        networkState = netState;
        if (netState.getStatus() != NetworkState.Status.SUCCESS && !isFooter) {
            isFooter = true;
            int pos = getItemCount() - 1;
            notifyItemInserted(pos);
        }

        if (oldnetState != netState && isFooter) {
            isFooter = true;
            int pos = getItemCount() - 1;
            notifyItemChanged(pos);
        }

        if (netState.getStatus() == NetworkState.Status.SUCCESS && isFooter) {
            isFooter = false;
            int pos = getItemCount();
            notifyItemRemoved(pos);
        }

        oldnetState = netState;
    }

    public void clearList()
    {
        itemsList.clear();
        notifyDataSetChanged();
    }

}
