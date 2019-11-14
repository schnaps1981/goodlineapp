package com.goodlineapp.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;
import com.goodlineapp.presenters.ActivityAboutMePresenter;
import com.goodlineapp.ui.utils.GlideApp;
import com.goodlineapp.ui.views.ActivityAboutMeView;
import com.google.android.material.snackbar.Snackbar;

import timber.log.Timber;

public class ActivityAboutMe extends BaseDetailsActivity implements ActivityAboutMeView {

    private UsersSearchItem current_user = null;

    @InjectPresenter
    ActivityAboutMePresenter activityAboutMePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d("About Me");

        initToolbar();

        fillDetails();
    }

    @Override
    public void fillDetails() {
        activityAboutMePresenter.LoadAboutMe();

    }

    @Override
    public void SetItemsInvisible() {
        llDetailsName.setVisibility(View.INVISIBLE);
        llDetailsLastName.setVisibility(View.INVISIBLE);
        llDetailsScreenName.setVisibility(View.INVISIBLE);
        llDetailsSex.setVisibility(View.INVISIBLE);
        llDetailsFamilyRelation.setVisibility(View.INVISIBLE);
        ivDetailsProfileImage.setVisibility(View.INVISIBLE);
        btnDetailsFriends.setVisibility(View.INVISIBLE);
    }

    @Override
    public void ShowSearchResult(UsersSearchItem item) {
        current_user = item;

        tvDetailsName.setText(item.getFirst_name());
        tvDetailsLastName.setText(item.getLast_name());
        tvDetailsScreenName.setText(item.getScreen_name());

        tvDetailsSex.setText(sex[item.getSex()]);
        tvDetailsFamilyRelation.setText(relation[item.getSex() - 1][item.getRelation()]);

        GlideApp
                .with(this)
                .load(item.getPhoto_200())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pbDetailsLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbDetailsLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.icon_image_load_error)
                .into(ivDetailsProfileImage);
    }

    @Override
    public void ShowNetworkState(NetworkState networkState) {
        if (networkState.getStatus() == NetworkState.Status.FAILED)
        {
            Snackbar.make(ivDetailsProfileImage, getResources().getString(R.string.error_loading), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    public void SetItemsVisible() {
        llDetailsName.setVisibility(View.VISIBLE);
        llDetailsLastName.setVisibility(View.VISIBLE);
        llDetailsScreenName.setVisibility(View.VISIBLE);
        llDetailsSex.setVisibility(View.VISIBLE);
        llDetailsFamilyRelation.setVisibility(View.VISIBLE);
        ivDetailsProfileImage.setVisibility(View.VISIBLE);
        btnDetailsFriends.setVisibility(View.VISIBLE);
    }

    @Override
    public void initToolbar() {
        super.initToolbar();
    }

    @Override
    public void btnDetailsFriendsClickListener(View view) {
        Intent intent = new Intent(this, ActivityFriends.class);
        intent.putExtra("data", current_user);
        startActivity(intent);
    }
}
