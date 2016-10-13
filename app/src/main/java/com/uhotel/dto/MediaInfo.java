package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiemhao on 9/24/16.
 */
public class MediaInfo implements Parcelable {


    public String id;
    public List<String> audios = new ArrayList<String>();
    public List<String> subtitles = new ArrayList<String>();
    public String quality;
    public boolean threeD;
    public List<MediaResource> mediaResources = new ArrayList<MediaResource>();
    public String promo;
    public String contentInfoId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeStringList(this.audios);
        dest.writeStringList(this.subtitles);
        dest.writeString(this.quality);
        dest.writeByte(this.threeD ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mediaResources);
        dest.writeString(this.promo);
        dest.writeString(this.contentInfoId);
    }

    public MediaInfo() {
    }

    protected MediaInfo(Parcel in) {
        this.id = in.readString();
        this.audios = in.createStringArrayList();
        this.subtitles = in.createStringArrayList();
        this.quality = in.readString();
        this.threeD = in.readByte() != 0;
        this.mediaResources = in.createTypedArrayList(MediaResource.CREATOR);
        this.promo = in.readString();
        this.contentInfoId = in.readString();
    }

    public static final Parcelable.Creator<MediaInfo> CREATOR = new Parcelable.Creator<MediaInfo>() {
        @Override
        public MediaInfo createFromParcel(Parcel source) {
            return new MediaInfo(source);
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[size];
        }
    };
}
