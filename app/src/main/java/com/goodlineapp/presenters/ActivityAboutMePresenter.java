package com.goodlineapp.presenters;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.goodlineapp.app.App;
import com.goodlineapp.app.Constants;
import com.goodlineapp.data.repository.VkApiRepository;
import com.goodlineapp.ui.views.ActivityAboutMeView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


@InjectViewState
public class ActivityAboutMePresenter extends MvpPresenter<ActivityAboutMeView> {

    @Inject CompositeDisposable compositeDisposable;
    @Inject VkApiRepository repository;
    @Inject SharedPreferences sharedPreferences;

    public ActivityAboutMePresenter()
    {
        App.getComponent().inject(this);
    }

    @Override
    public void attachView(ActivityAboutMeView view) {
        super.attachView(view);
        compositeDisposable.add(
                repository.getAboutMe()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(usersSearchItems -> {
                        getViewState().ShowSearchResult(usersSearchItems);
                        getViewState().SetItemsVisible();
                    }, throwable -> {
                        Timber.d("Fetch List error - %s", throwable.getMessage());
                    })
        );

        compositeDisposable.add(
                repository.getNetworkState()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(networkState -> {
                        getViewState().ShowNetworkState(networkState);
                    })

        );

        Timber.d("ActivityAboutMePresenter Composite disposables count - %s", String.valueOf(compositeDisposable.size()));

        Timber.d("ActivityAboutMe attached");
    }

    @Override
    public void detachView(ActivityAboutMeView view) {
        super.detachView(view);
        compositeDisposable.clear();
    }

    public void LoadAboutMe()
    {
        getViewState().SetItemsInvisible();
        repository.LoadAboutMe(sharedPreferences.getLong(Constants.PREFS_KEY_CURRENT_USER_ID, -1L));
    }
}
