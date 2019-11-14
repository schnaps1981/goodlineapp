package com.goodlineapp.data.vkapi.datasource;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.goodlineapp.app.App;
import com.goodlineapp.app.Constants;
import com.goodlineapp.data.vkapi.api.VKApiInterface;
import com.goodlineapp.data.vkapi.models.ApiAboutUserResponse;
import com.goodlineapp.data.vkapi.models.ApiResponse;
import com.goodlineapp.data.vkapi.models.UsersSearchItem;
import com.goodlineapp.data.vkapi.utils.NetworkState;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class VkApiDataSource implements VkApiDataSourceInterface{
    private PublishSubject<NetworkState> networkStatus;
    private Completable retryCompletable;
    private PublishSubject<Boolean> refreshStatus;
    private PublishSubject<ArrayList<UsersSearchItem>> vkResponseApiObservable;
    private PublishSubject<UsersSearchItem> vkAboutUserObservable;

    //Вк отдает разное количество записей, поэтому позицию, откуда читать записи, нужно учитывать здесь
    //И при запросе на следующую порцию данных позиция для их запроса определяется путем прибавления количества данных по окончанию запроса
    private int offset = 0;

    private String request = "";
    private String method ="";
    private Long user_id = 0L;
    private String accesToken;

    @Inject VKApiInterface vkApiClient;
    @Inject SharedPreferences sharedPreferences;

    public VkApiDataSource() {
        App.getComponent().inject(this);
        networkStatus = PublishSubject.create();
        vkResponseApiObservable = PublishSubject.create();
        refreshStatus = PublishSubject.create();
        vkAboutUserObservable = PublishSubject.create();

        accesToken = sharedPreferences.getString(Constants.PREFS_KEY_ACCESS_TOKEN, "");

    }

    //Сброс позиции запроса данных, например при новом запросе
    public void ResetRequestPosition()
    {
        offset = 0;
    }

    @Override
    public void loadMoreUsers(String request, @NonNull String method, Long user_id)
    {
        if (request == null && user_id == -1L) {  //предотвращаем поиск случайных людей
            refreshStatus.onNext(true);
            return;
        }
        this.request = request;
        this.method = method;
        this.user_id = user_id;

        networkStatus.onNext(NetworkState.LOADING);
        vkApiClient.users_search(method,
                user_id == -1L ? null : user_id,
                user_id == -1L ? request : null,
                Constants.PAGE_SIZE,
                offset,
                "sex, photo_200, photo_400_orig, screen_name, relation",
                accesToken,
                Constants.API_VERSION,
                Constants.API_LANGUAGE
                ).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                refreshStatus.onNext(true);

                if (response.isSuccessful()) {

                    //ответ как бы пришел, но вернулся такого вида {"error":{"error_code":6,
                    //Такое бывает, например при превышении скорости запросов или протух токен
                    if (response.body() == null)
                    {
                        refreshStatus.onNext(true);
                        networkStatus.onNext(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        setRetry(() -> loadMoreUsers(request, method, user_id));
                        return;
                    }

                    int list_len = response.body().getResponse().getItems().size();
                    //Если записей 0 И смещение 0, значит это не ошибк сети, просто ничего не найдено
                    if (list_len == 0 && offset == 0) {
                        Timber.d("No data");
                        networkStatus.onNext(NetworkState.NO_DATA);
                        return;
                    }

                    //Судя по offset > 0 ранее были загружены данные, но раз список вернулся нуевой длины
                    //то значит данных больше нет и не будет
                    if (list_len == 0 && offset > 0) {
                        Timber.d("No more data");
                        networkStatus.onNext(NetworkState.NO_MORE_DATA);
                    } else

                    //возвращаем список с данными для отображения
                    {
                        vkResponseApiObservable.onNext(response.body().getResponse().getItems());

                        offset += list_len;
                        networkStatus.onNext(NetworkState.LOADED);
                        Timber.d("Total records in response = %s", response.body().getResponse().getItems().size());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                refreshStatus.onNext(true);
                networkStatus.onNext(new NetworkState(NetworkState.Status.FAILED, t.getMessage()));
                Timber.d("VK API request onFailure - %s", t.getLocalizedMessage());
                setRetry(() -> loadMoreUsers(request, method, user_id));
            }
        });
    }

    @Override
    public void loadMoreUsers(String request, String method) {
        loadMoreUsers(request, method, -1L);
    }

    //Так хотелось сделать в одном запросе все, но увы, при вызове метода get.user
    //Вк возвращает JSON другого формата.
    @Override
    public void loadAboutUser(Long user_id) {
        networkStatus.onNext(NetworkState.LOADING);

        vkApiClient.about_me(
                user_id,
                "sex, photo_200, photo_400_orig, screen_name, relation",
                "Nom",
                accesToken,
                Constants.API_VERSION,
                Constants.API_LANGUAGE
        ).enqueue(new Callback<ApiAboutUserResponse>() {
            @Override
            public void onResponse(Call<ApiAboutUserResponse> call, Response<ApiAboutUserResponse> response) {
                if (response.isSuccessful())
                {
                    Timber.d("About me loaded");
                    vkAboutUserObservable.onNext(response.body().getResponse().get(0));
                    networkStatus.onNext(NetworkState.LOADED);
                }
            }

            @Override
            public void onFailure(Call<ApiAboutUserResponse> call, Throwable t) {
                Timber.d("About me fail - %s", t.getLocalizedMessage());
                networkStatus.onNext(new NetworkState(NetworkState.Status.FAILED, t.getMessage()));
                setRetry(() -> loadAboutUser(user_id));
            }
        });
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

    @SuppressLint("CheckResult")
    public void tryAgain() {
        if (retryCompletable != null) {
            retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Timber.d(throwable.getMessage()));
        }
    }

    public PublishSubject<NetworkState> getNetworkState() {return networkStatus;}

    public PublishSubject<ArrayList<UsersSearchItem>> getLiveResponseVkApi() { return vkResponseApiObservable;}

    public PublishSubject<Boolean> getRefreshStatus() {return refreshStatus; }

    public PublishSubject<UsersSearchItem> getAboutMe() {return vkAboutUserObservable; }
}
