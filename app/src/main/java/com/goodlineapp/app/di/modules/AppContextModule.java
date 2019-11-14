package com.goodlineapp.app.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppContextModule {
    Context context;

    public AppContextModule(Context context)
    {
        this.context = context;
    }

    @Provides
    @Singleton
    Context provideContext()
    {
        return context;
    }
}
