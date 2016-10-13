package com.uhotel.fragment.concierge.roomService.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MasterItem implements Parcelable {

    // a recipe contains several ingredients
    public List<DetailItem> list;
    public String name;
    public int total;

    public MasterItem() {


    }

    public MasterItem(String name) {
        this.name = name;

    }

    public MasterItem(String name, List<DetailItem> list) {
        this.list = list;
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
        dest.writeString(this.name);
        dest.writeInt(this.total);
    }

    protected MasterItem(Parcel in) {
        this.list = in.createTypedArrayList(DetailItem.CREATOR);
        this.name = in.readString();
        this.total = in.readInt();
    }

    public static final Parcelable.Creator<MasterItem> CREATOR = new Parcelable.Creator<MasterItem>() {
        @Override
        public MasterItem createFromParcel(Parcel source) {
            return new MasterItem(source);
        }

        @Override
        public MasterItem[] newArray(int size) {
            return new MasterItem[size];
        }
    };
}