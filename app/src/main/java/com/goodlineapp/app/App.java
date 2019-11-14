package com.goodlineapp.app;

import android.app.Application;

import com.goodlineapp.BuildConfig;
import com.goodlineapp.app.di.component.AppComponent;
import com.goodlineapp.app.di.component.DaggerAppComponent;
import com.goodlineapp.app.di.modules.AppContextModule;
import com.vk.api.sdk.VK;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;
public class App extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerAppComponent
                .builder()
                .appContextModule(new AppContextModule(getApplicationContext()))
                .build();


        VK.initialize(getApplicationContext());

        intitLogger();
    }

    public static AppComponent getComponent() {
        return component;
    }

    private void intitLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, @NotNull String message, Throwable t) {

                }
            });
        }
    }

}
