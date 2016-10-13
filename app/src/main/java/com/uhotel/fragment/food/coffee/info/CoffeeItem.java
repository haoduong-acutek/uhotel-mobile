package com.uhotel.fragment.food.coffee.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 8/31/16.
 */
public class CoffeeItem implements Parcelable {
    public String name;
    public int rating;
    public String resName;
    public String address;
    public String detail;
    public String openTime;
    public String phone;
    public int url;

    public CoffeeItem(String name, int rating, String resName, String address, String detail, String openTime, String phone, int url) {
        this.address = address;
        this.detail = detail;
        this.name = name;
        this.openTime = openTime;
        this.phone = phone;
        this.rating = rating;
        this.resName = resName;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.rating);
        dest.writeString(this.resName);
        dest.writeString(this.address);
        dest.writeString(this.detail);
        dest.writeString(this.openTime);
        dest.writeString(this.phone);
        dest.writeInt(this.url);
    }

    public CoffeeItem() {
    }

    protected CoffeeItem(Parcel in) {
        this.name = in.readString();
        this.rating = in.readInt();
        this.resName = in.readString();
        this.address = in.readString();
        this.detail = in.readString();
        this.openTime = in.readString();
        this.phone = in.readString();
        this.url = in.readInt();
    }

    public static final Creator<CoffeeItem> CREATOR = new Creator<CoffeeItem>() {
        @Override
        public CoffeeItem createFromParcel(Parcel source) {
            return new CoffeeItem(source);
        }

        @Override
        public CoffeeItem[] newArray(int size) {
            return new CoffeeItem[size];
        }
    };
}
