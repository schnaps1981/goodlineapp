package com.goodlineapp.presenters;

import android.content.Intent;
import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.goodlineapp.R;
import com.goodlineapp.app.App;
import com.goodlineapp.app.Constants;
import com.goodlineapp.ui.views.ActivityLoginView;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import timber.log.Timber;

@InjectViewState
public class ActivityLoginPresenter extends MvpPresenter<ActivityLoginView> {

    @Inject SharedPreferences sharedPreferences;

    public ActivityLoginPresenter()
    {
        App.getComponent().inject(this);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        if (VK.isLoggedIn()) {
            getViewState().StartActivitySearch();
        }
    }


    public Boolean loginVk(int requestCode, int resultCode, Intent data)
    {
        if (VK.onActivityResult(requestCode, resultCode, data, new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                sharedPreferences.edit().putString(Constants.PREFS_KEY_ACCESS_TOKEN, vkAccessToken.getAccessToken()).apply();
                sharedPreferences.edit().putLong("current_user_id", vkAccessToken.getUserId()).apply();
                Timber.d(vkAccessToken.getAccessToken());
                getViewState().StartActivitySearch();
            }

            @Override
            public void onLoginFailed(int i) {
                getViewState().ShowError(R.string.MessageErrorLogin);
            }
        })) {
            return false;
        }

        return true;
    }
}
