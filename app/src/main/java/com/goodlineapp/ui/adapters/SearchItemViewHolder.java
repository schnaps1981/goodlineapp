package com.goodlineapp.ui.adapters;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodlineapp.R;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.ui.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvProfileName) TextView tvProfileName;
    @BindView(R.id.pbProfileLoading) ProgressBar pbProfileLoading;
    @BindView(R.id.ivProfileDetails) ImageView ivProfileDetails;

    public SearchItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UsersSearchItem user, Boolean details_visible)
    {
        tvProfileName.setText(user.getFirst_name());
        ivProfileDetails.setVisibility(details_visible ? View.VISIBLE : View.INVISIBLE);

        GlideApp
                .with(itemView.getContext())
                .load(user.getPhoto_200())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        pbProfileLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        pbProfileLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.icon_image_load_error)
                .into(ivProfileImage);


    }
}
