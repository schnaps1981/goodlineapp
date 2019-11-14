package com.goodlineapp.ui.utils;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public final class GlideMod extends AppGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 500; // 500 MB
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
        builder.setLogLevel(Log.ERROR);
    }
}
