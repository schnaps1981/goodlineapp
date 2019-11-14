package com.goodlineapp.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.ui.utils.GlideApp;

public class ActivityUserDetails extends BaseDetailsActivity {

    private UsersSearchItem user_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //Так надо, супер вызывать в самом конце

        user_details = getIntent().getParcelableExtra("user");

        initToolbar();

        fillDetails();
    }

    @Override
    public void fillDetails() {
        tvDetailsName.setText(user_details.getFirst_name());
        tvDetailsLastName.setText(user_details.getLast_name());
        tvDetailsScreenName.setText(user_details.getScreen_name());

        tvDetailsSex.setText(sex[user_details.getSex()]);
        tvDetailsFamilyRelation.setText(relation[user_details.getSex() - 1][user_details.getRelation()]);

        GlideApp
                .with(this)
                .load(user_details.getPhoto_200())
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
    public void initToolbar() {
        super.initToolbar();
        //Установка тайтла
        // https://material.io/design/platform-guidance/cross-platform-adaptation.html#
        // гугл рекомендует выравнивать тайтл по умолчанию, то есть слева, да и вообще в мокапе айфон

        tbDetailsToolbar.setTitle(user_details.getFirst_name());
        tbDetailsToolbar.setTitleTextAppearance(this, R.style.MyToolbarTitleStyle);
    }

    @Override
    public void btnDetailsFriendsClickListener(View view) {
        {
            Intent intent = new Intent(this, ActivityFriends.class);
            intent.putExtra("data", user_details);
            startActivity(intent);
        }
    }
}
