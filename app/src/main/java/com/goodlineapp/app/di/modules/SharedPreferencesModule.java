package com.goodlineapp.app.di.modules;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppContextModule.class)
public class SharedPreferencesModule {

    private SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    public SharedPreferences providesSharedPreferences(Context context)
    {
        return getSharedPreferences(context);
    }
}
