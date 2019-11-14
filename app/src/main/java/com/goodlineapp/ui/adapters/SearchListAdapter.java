package com.goodlineapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

public class SearchListAdapter extends BaseListAdapter<UsersSearchItem> {

    private static final int TYPE_NETWORK_STATUS = 0;
    private static final int TYPE_ITEM = 1;
    private NetworkState networkState;

    public interface OnItemClickListener
    {
        void onItemClick(View view, Integer position);
    }
    private final OnItemClickListener listener;

    public SearchListAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NETWORK_STATUS)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.network_state_item, parent, false);
            return new NetworkItemViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
            return new SearchItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchItemViewHolder)
        {
            ((SearchItemViewHolder)holder).bind(getItem(position), true);
            ((SearchItemViewHolder)holder).itemView.setOnClickListener(view -> listener.onItemClick(view, position));

        }

        if (holder instanceof NetworkItemViewHolder)
        {
            ((NetworkItemViewHolder)holder).bind(super.getNetworkState());
            ((NetworkItemViewHolder)holder).btnNetworkStateRetry.setOnClickListener(view -> listener.onItemClick(view, position));
        }
    }
}
