package com.uhotel.fragment.concierge.roomService.info;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 8/29/16.
 */
public class DetailItem implements Parcelable {

    public String name;
    public int value;

    public DetailItem(String name, int value) {
        this.name = name;
        this.value = value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.value);
    }

    protected DetailItem(Parcel in) {
        this.name = in.readString();
        this.value = in.readInt();
    }

    public static final Parcelable.Creator<DetailItem> CREATOR = new Parcelable.Creator<DetailItem>() {
        @Override
        public DetailItem createFromParcel(Parcel source) {
            return new DetailItem(source);
        }

        @Override
        public DetailItem[] newArray(int size) {
            return new DetailItem[size];
        }
    };


}
