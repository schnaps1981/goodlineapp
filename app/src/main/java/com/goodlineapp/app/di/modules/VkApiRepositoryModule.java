package com.goodlineapp.app.di.modules;

import com.goodlineapp.data.repository.VkApiRepository;
import com.goodlineapp.data.repository.VkApiRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VkApiRepositoryModule {

    @Provides
    @Singleton
    public VkApiRepository provideVkApiRepository()
    {
        return new VkApiRepositoryImpl();
    }
}
