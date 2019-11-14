package com.goodlineapp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.goodlineapp.app.App;
import com.goodlineapp.data.repository.VkApiRepository;
import com.goodlineapp.ui.views.ActivitySearchView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@InjectViewState
public class ActivitySearchPresenter extends MvpPresenter<ActivitySearchView> {

    @Inject VkApiRepository repository;
    @Inject CompositeDisposable compositeDisposable;

    private String request;
    public ActivitySearchPresenter()
    {
        App.getComponent().inject(this);
    }

    @Override
    public void attachView(ActivitySearchView view) {
        super.attachView(view);
        compositeDisposable.add(
                repository.getUserList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(usersSearchItems -> {
                            getViewState().ShowSearchResult(usersSearchItems);
                        }, throwable -> Timber.d("Fetch List error - %s", throwable.getMessage()))
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

        Timber.d("ActivitySearch composite disposables %s", compositeDisposable.size());
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().SetStateSearch();
    }

    @Override
    public void detachView(ActivitySearchView view) {
        super.detachView(view);
        Timber.d("ActivitySearchView detached");
        compositeDisposable.clear();
    }

    public void SubmitRequest(String request) {
        this.request = request;
        Timber.d("SubmitRequest composite disposables %s", compositeDisposable.size());
        repository.LoadUsers(request, "users.search");
    }

    public void RefreshSearch() { repository.RefreshSearch(request, "users.search"); }

    public void TryFetchApiData() { repository.TryFetchApiData(); }

    public void LoadNextPage() { repository.LoadUsersNextPage(); }

}
