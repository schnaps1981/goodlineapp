package com.goodlineapp.data.vkapi.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import timber.log.Timber;


public class UsersSearchItem implements Parcelable {
    private Long id;
    private String first_name;
    private String last_name;
    private Integer sex;
    private String screen_name;
    private String photo_200;
    private String photo_400_orig;
    private Integer relation;

    public UsersSearchItem() { }

    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}

    public String getFirst_name() { return first_name; }
    public void setFirst_name(String first_name) { this.first_name = first_name; }

    public String getLast_name() { return last_name; }
    public void setLast_name(String last_name) { this.last_name = last_name; }

    public Integer getSex() { return sex; }
    public void setSex(Integer sex) { this.sex = sex; }

    public String getScreen_name() { return screen_name;}
    public void setScreen_name(String screen_name) { this.screen_name = screen_name; }

    public String getPhoto_200() { return photo_200; }
    public void setPhoto_200(String photo_200) { this.photo_200 = photo_200; }

    public Integer getRelation() { return relation; }
    public void setRelation(Integer relation) { this.relation = relation; }

    public String getPhoto_400_orig() { return photo_400_orig; }
    public void setPhoto_400_orig(String photo_400_orig) { this.photo_400_orig = photo_400_orig; }


    public UsersSearchItem(Parcel parcel) {
        Timber.d("UsersSearchItem(Parcel parcel)");
        id = parcel.readLong();
        first_name = parcel.readString();
        last_name = parcel.readString();
        sex = parcel.readInt();
        screen_name = parcel.readString();
        photo_200 = parcel.readString();
        photo_400_orig = parcel.readString();
        relation = parcel.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Timber.d("UsersSearchItem writeToParcel");
        parcel.writeLong(id);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeInt(sex);
        parcel.writeString(screen_name);
        parcel.writeString(photo_200);
        parcel.writeString(photo_400_orig);
        parcel.writeInt(relation != null ? relation : 0);
    }

    public static final Parcelable.Creator<UsersSearchItem> CREATOR = new Parcelable.Creator<UsersSearchItem>() {
        public UsersSearchItem createFromParcel(Parcel in) {
            Timber.d("createFromParcel");
            return new UsersSearchItem(in);
        }

        public UsersSearchItem[] newArray(int size) {
            return new UsersSearchItem[size];
        }
    };


}
