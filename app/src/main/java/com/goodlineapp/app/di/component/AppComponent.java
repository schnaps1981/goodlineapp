package com.goodlineapp.app.di.component;

import com.goodlineapp.app.di.modules.AppContextModule;
import com.goodlineapp.app.di.modules.CompositeDisposableModule;
import com.goodlineapp.app.di.modules.OkHttpClientModule;
import com.goodlineapp.app.di.modules.SharedPreferencesModule;
import com.goodlineapp.app.di.modules.VkApiClientModule;
import com.goodlineapp.app.di.modules.VkApiDataSourceModule;
import com.goodlineapp.app.di.modules.VkApiRepositoryModule;
import com.goodlineapp.data.repository.VkApiRepositoryImpl;
import com.goodlineapp.data.vkapi.datasource.VkApiDataSource;
import com.goodlineapp.presenters.ActivityAboutMePresenter;
import com.goodlineapp.presenters.ActivityFriendsPresenter;
import com.goodlineapp.presenters.ActivityLoginPresenter;
import com.goodlineapp.presenters.ActivitySearchPresenter;
import com.goodlineapp.ui.activities.ActivityAboutMe;
import com.goodlineapp.ui.activities.ActivityFriends;
import com.goodlineapp.ui.activities.ActivitySearch;
import com.goodlineapp.ui.adapters.NetworkItemViewHolder;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppContextModule.class,
        OkHttpClientModule.class,
        VkApiClientModule.class,
        SharedPreferencesModule.class,
        VkApiDataSourceModule.class,
        VkApiRepositoryModule.class,
        CompositeDisposableModule.class,
})


public interface AppComponent {
    void inject(VkApiDataSource vkApiDataSource);
    void inject(VkApiRepositoryImpl vkApiRepository);

    void inject(ActivityLoginPresenter activityLoginPresenter);
    void inject(ActivitySearchPresenter activitySearchPresenter);
    void inject(ActivityFriendsPresenter activityFriendsPresenter);
    void inject(ActivityAboutMePresenter activityAboutMePresenter);

    void inject(NetworkItemViewHolder networkItemViewHolder);
}
