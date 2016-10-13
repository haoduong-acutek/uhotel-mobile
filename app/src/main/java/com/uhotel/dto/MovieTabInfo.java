package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 9/24/16.
 */
public class MovieTabInfo implements Parcelable {
    public String id;
    public String title;

    public MovieTabInfo(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
    }

    protected MovieTabInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<MovieTabInfo> CREATOR = new Parcelable.Creator<MovieTabInfo>() {
        @Override
        public MovieTabInfo createFromParcel(Parcel source) {
            return new MovieTabInfo(source);
        }

        @Override
        public MovieTabInfo[] newArray(int size) {
            return new MovieTabInfo[size];
        }
    };
}
