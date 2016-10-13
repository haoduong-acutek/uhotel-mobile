package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiemhao on 9/22/16.
 */
public class StreamInfo implements Parcelable {

    public String src;
    public String provider;
    public String protocolStack;
    public String location;
    public String profiles;
    public String capabilities;
    public String other;
    public Integer duration;
    public Integer offset;
    public String protocol;
    public String dialect;
    public String oTag;
    public boolean signOauth;

    public StreamInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.src);
        dest.writeString(this.provider);
        dest.writeString(this.protocolStack);
        dest.writeString(this.location);
        dest.writeString(this.profiles);
        dest.writeString(this.capabilities);
        dest.writeString(this.other);
        dest.writeValue(this.duration);
        dest.writeValue(this.offset);
        dest.writeString(this.protocol);
        dest.writeString(this.dialect);
        dest.writeString(this.oTag);
        dest.writeByte(this.signOauth ? (byte) 1 : (byte) 0);
    }

    protected StreamInfo(Parcel in) {
        this.src = in.readString();
        this.provider = in.readString();
        this.protocolStack = in.readString();
        this.location = in.readString();
        this.profiles = in.readString();
        this.capabilities = in.readString();
        this.other = in.readString();
        this.duration = (Integer) in.readValue(Integer.class.getClassLoader());
        this.offset = (Integer) in.readValue(Integer.class.getClassLoader());
        this.protocol = in.readString();
        this.dialect = in.readString();
        this.oTag = in.readString();
        this.signOauth = in.readByte() != 0;
    }

    public static final Creator<StreamInfo> CREATOR = new Creator<StreamInfo>() {
        @Override
        public StreamInfo createFromParcel(Parcel source) {
            return new StreamInfo(source);
        }

        @Override
        public StreamInfo[] newArray(int size) {
            return new StreamInfo[size];
        }
    };
}
