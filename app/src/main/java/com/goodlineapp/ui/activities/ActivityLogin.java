package com.goodlineapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.arellomobile.mvp.MvpActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.goodlineapp.R;
import com.goodlineapp.presenters.ActivityLoginPresenter;
import com.goodlineapp.ui.views.ActivityLoginView;
import com.google.android.material.snackbar.Snackbar;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKScope;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java9.util.stream.Stream;

import static java9.util.stream.Collectors.toList;


public class ActivityLogin extends MvpActivity implements ActivityLoginView {
    @BindView(R.id.btnLoginButton) Button btnLoginButton;

    @InjectPresenter(type = PresenterType.GLOBAL)
    ActivityLoginPresenter activityLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    //Кнопка нужна больше для понимания, что это активити логин, по хорошему, если пользователь не зарегистрирован,
    // нужно запускать форму авторизации сразу
    @OnClick(R.id.btnLoginButton)
    public void onLoginButtonClick(View view)
    {
        VK.login(this, Stream.of(VKScope.FRIENDS, VKScope.PHOTOS).collect(toList()));
    }

    @Override
    public void StartActivitySearch() {

        Intent intent = new Intent(this, ActivitySearch.class);

        intent
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        finish();
    }

    @Override
    public void ShowError(Integer messageRes) {
        Snackbar.make(btnLoginButton, getResources().getString(messageRes), Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!activityLoginPresenter.loginVk(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
