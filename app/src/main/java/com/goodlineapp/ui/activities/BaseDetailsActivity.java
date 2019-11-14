package com.goodlineapp.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.arellomobile.mvp.MvpActivity;
import com.goodlineapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class BaseDetailsActivity extends MvpActivity  {
    @BindView(R.id.tbDetailsToolbar) Toolbar tbDetailsToolbar;
    @BindView(R.id.tvDetailsName) TextView tvDetailsName;
    @BindView(R.id.tvDetailsLastName) TextView tvDetailsLastName;
    @BindView(R.id.tvDetailsScreenName) TextView tvDetailsScreenName;
    @BindView(R.id.tvDetailsSex) TextView tvDetailsSex;
    @BindView(R.id.tvDetailsFamilyRelation) TextView tvDetailsFamilyRelation;
    @BindView(R.id.ivDetailsProfileImage) ImageView ivDetailsProfileImage;
    @BindView(R.id.btnDetailsFriends) Button btnDetailsFriends;
    @BindView(R.id.pbDetailsLoading) ProgressBar pbDetailsLoading;

    //Для инфо о юзере. это контейнеры в которых содержится информация
    //меньше кода, если скрывать контейнеры (сделано в активити)
    //поэтому они тут
    @BindView(R.id.llDetailsName) LinearLayout llDetailsName;
    @BindView(R.id.llDetailsLastName) LinearLayout llDetailsLastName;
    @BindView(R.id.llDetailsScreenName) LinearLayout llDetailsScreenName;
    @BindView(R.id.llDetailsSex) LinearLayout llDetailsSex;
    @BindView(R.id.llDetailsFamilyRelation) LinearLayout llDetailsFamilyRelation;

    protected final String[] sex = new String[]{"Не указано", "Женщина", "Мужчина"};
    protected final String[][] relation = new String[][]{
            {"не указано", "не замужем", "есть друг", "помолвлена", "замужем", "всё сложно", "в активном поиске", "влюблена", "в гражданском браке"},
            {"не указано", "не женат", "есть подруга", "помолвлен", "женат", "всё сложно", "в активном поиске", "влюблён", "в гражданском браке"}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_details);

        ButterKnife.bind(this);
    }

    public abstract void fillDetails();

    public void initToolbar() {
        //Обработчик "назад"
        tbDetailsToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.icon_toolbar_back));
        tbDetailsToolbar.setNavigationOnClickListener(view -> super.onBackPressed());
    }

    @OnClick(R.id.btnDetailsFriends)
    public abstract void btnDetailsFriendsClickListener(View view);
}
