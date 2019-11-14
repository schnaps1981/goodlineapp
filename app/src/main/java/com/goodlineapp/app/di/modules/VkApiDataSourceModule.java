package com.goodlineapp.app.di.modules;

import com.goodlineapp.data.vkapi.datasource.VkApiDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VkApiDataSourceModule {

    @Provides
    @Singleton
    public VkApiDataSource provideVkApiDataSource()
    {
       return new VkApiDataSource();
    }
}
