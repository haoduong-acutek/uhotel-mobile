package com.uhotel.fragment.food.attraction.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 8/31/16.
 */
public class AttractItem implements Parcelable {
    public String name;
    public String detail;
    public int url;

    public AttractItem(String name, String detail, int url) {

        this.detail = detail;
        this.name = name;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);

        dest.writeString(this.detail);
        dest.writeInt(this.url);
    }

    protected AttractItem(Parcel in) {
        this.name = in.readString();

        this.detail = in.readString();
        this.url = in.readInt();
    }

    public static final Creator<AttractItem> CREATOR = new Creator<AttractItem>() {
        @Override
        public AttractItem createFromParcel(Parcel source) {
            return new AttractItem(source);
        }

        @Override
        public AttractItem[] newArray(int size) {
            return new AttractItem[size];
        }
    };
}
