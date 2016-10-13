package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 9/24/16.
 */
public class DetailsInfo implements Parcelable {
    public String title;
    public String director;
    public Integer duration;
    public String poster;
    public String description;

    public String getFormatDuration(){
        return duration/60+"h "+duration%60+"min";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.director);
        dest.writeValue(this.duration);
        dest.writeString(this.poster);
        dest.writeString(this.description);
    }

    public DetailsInfo() {
    }

    protected DetailsInfo(Parcel in) {
        this.title = in.readString();
        this.director = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.poster = in.readString();
        this.description = in.readString();
    }

    public static final Parcelable.Creator<DetailsInfo> CREATOR = new Parcelable.Creator<DetailsInfo>() {
        @Override
        public DetailsInfo createFromParcel(Parcel source) {
            return new DetailsInfo(source);
        }

        @Override
        public DetailsInfo[] newArray(int size) {
            return new DetailsInfo[size];
        }
    };
}
