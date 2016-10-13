package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 9/24/16.
 */
public class VodInfo implements Parcelable {

    public Integer purchaseId;
    public String contentInfoId;
    public DetailsInfo details;
    public Integer contentId;
    public Integer createdTime;

    public MediaInfo mediaInfo;
    public PurchaseInfo.Price price;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.purchaseId);
        dest.writeString(this.contentInfoId);
        dest.writeParcelable(this.details, flags);
        dest.writeValue(this.contentId);
        dest.writeValue(this.createdTime);
        dest.writeParcelable(this.mediaInfo, flags);
    }

    public VodInfo() {
    }

    protected VodInfo(Parcel in) {
        this.purchaseId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.contentInfoId = in.readString();
        this.details = in.readParcelable(DetailsInfo.class.getClassLoader());
        this.contentId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.mediaInfo = in.readParcelable(MediaInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<VodInfo> CREATOR = new Parcelable.Creator<VodInfo>() {
        @Override
        public VodInfo createFromParcel(Parcel source) {
            return new VodInfo(source);
        }

        @Override
        public VodInfo[] newArray(int size) {
            return new VodInfo[size];
        }
    };
}
