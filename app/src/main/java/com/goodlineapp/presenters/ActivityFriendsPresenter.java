package com.goodlineapp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.goodlineapp.app.App;
import com.goodlineapp.data.repository.VkApiRepository;
import com.goodlineapp.data.repository.VkApiRepositoryImpl;
import com.goodlineapp.ui.views.ActivityFriendsView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@InjectViewState
public class ActivityFriendsPresenter extends MvpPresenter<ActivityFriendsView> {

    @Inject VkApiRepository repository;
    @Inject CompositeDisposable compositeDisposable;

    public ActivityFriendsPresenter() {
        App.getComponent().inject(this);
    }

    @Override
    public void attachView(ActivityFriendsView view) {
        super.attachView(view);
        compositeDisposable.add(
                repository.getUserList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(usersSearchItems -> {
                            getViewState().ShowSearchResult(usersSearchItems);
                        }, throwable -> {
                            Timber.d("Fetch List error - %s", throwable.getMessage());
                        })
        );

        compositeDisposable.add(
                repository.getNetworkState()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(networkState -> getViewState().ShowNetworkState(networkState))
        );

        compositeDisposable.add(
                repository.getRefreshStatus()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aBoolean -> getViewState().HideRefreshIndicator())
        );
        Timber.d("ActivityFriendsPresenter Composite disposables count - %s", String.valueOf(compositeDisposable.size()));

        Timber.d("ActivityFriendsPresenter attached");
    }

    @Override
    public void detachView(ActivityFriendsView view) {
        super.detachView(view);
        compositeDisposable.clear();
    }

    public void SubmitRequest(Long user_id) { repository.LoadUsers("", "friends.get", user_id); }

    public void TryFetchApiData()
    {
        repository.TryFetchApiData();
    }

    public void LoadNextPage() { repository.LoadUsersNextPage(); }

    public void RefreshSearch(Long user_id) { repository.RefreshSearch("", "friends.get", user_id ); }

}
