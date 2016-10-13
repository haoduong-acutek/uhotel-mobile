package com.uhotel.fragment.food.hotel.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 8/31/16.
 */
public class HotelItem implements Parcelable {
    public String name;
    public String desc;
    public String detail;
    public int url;

    public HotelItem(String name, String desc, String detail, int url) {
        this.desc = desc;
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
        dest.writeString(this.desc);
        dest.writeString(this.detail);
        dest.writeInt(this.url);
    }

    protected HotelItem(Parcel in) {
        this.name = in.readString();
        this.desc = in.readString();
        this.detail = in.readString();
        this.url = in.readInt();
    }

    public static final Parcelable.Creator<HotelItem> CREATOR = new Parcelable.Creator<HotelItem>() {
        @Override
        public HotelItem createFromParcel(Parcel source) {
            return new HotelItem(source);
        }

        @Override
        public HotelItem[] newArray(int size) {
            return new HotelItem[size];
        }
    };
}
