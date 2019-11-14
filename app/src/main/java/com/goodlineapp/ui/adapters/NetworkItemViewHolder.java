package com.goodlineapp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.goodlineapp.R;
import com.goodlineapp.app.App;
import com.goodlineapp.data.vkapi.utils.NetworkState;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NetworkItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvNetworkStateErrorMsg) TextView tvNetworkStateErrorMsg;
    @BindView(R.id.btnNetworkStateRetry) Button  btnNetworkStateRetry;
    @BindView(R.id.pbNetworkStateProgress) ProgressBar pbNetworkStateProgress;

    @Inject Context context;

    public NetworkItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        App.getComponent().inject(this);
    }

    public void bind(NetworkState networkState)
    {
        if (networkState.getStatus() == NetworkState.Status.NO_MORE_DATA)
        {
            pbNetworkStateProgress.setVisibility(View.GONE);
            btnNetworkStateRetry.setVisibility(View.GONE);

            tvNetworkStateErrorMsg.setVisibility(View.VISIBLE);
            //tvNetworkStateErrorMsg.setText(networkState.getMsg());
            tvNetworkStateErrorMsg.setText(context.getResources().getString(R.string.no_more_data));
            return;
        }

        if (networkState.getStatus() == NetworkState.Status.RUNNING)  {
            pbNetworkStateProgress.setVisibility(View.VISIBLE);
        }
        else
        {
            pbNetworkStateProgress.setVisibility(View.GONE);
        }

        if (networkState.getStatus() == NetworkState.Status.FAILED)
        {
            btnNetworkStateRetry.setVisibility(View.VISIBLE);
            tvNetworkStateErrorMsg.setVisibility(View.VISIBLE);
        }
        else
        {
            btnNetworkStateRetry.setVisibility(View.GONE);
            tvNetworkStateErrorMsg.setVisibility(View.GONE);
        }

    }
}
