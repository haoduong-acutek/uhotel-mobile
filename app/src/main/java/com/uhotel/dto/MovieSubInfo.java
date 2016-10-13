package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 9/20/16.
 */
public class MovieSubInfo implements Parcelable {

    public String name;
    public String url;
    public String sourceURL;

    public MovieSubInfo(String name, String url) {
        this.name = name;
        this.url = url;
        this.sourceURL = "http://media.karaokecuatui.vn//mv//2016//08//23//7//7//771bbb1d77994a17bdca54c636b21822.mp4";
    }

    public MovieSubInfo(String name, String url, String sourceURL) {
        this.name = name;
        this.url = url;
        this.sourceURL = sourceURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.sourceURL);
    }

    protected MovieSubInfo(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.sourceURL = in.readString();
    }

    public static final Parcelable.Creator<MovieSubInfo> CREATOR = new Parcelable.Creator<MovieSubInfo>() {
        @Override
        public MovieSubInfo createFromParcel(Parcel source) {
            return new MovieSubInfo(source);
        }

        @Override
        public MovieSubInfo[] newArray(int size) {
            return new MovieSubInfo[size];
        }
    };
}
