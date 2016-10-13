package com.uhotel.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiemhao on 9/22/16.
 */
public class TVInfo implements Parcelable,Comparator<TVInfo> {
    public String title;


    public long start;
    public long end;

    public String channelName;
    public int channelNo;
    public String description;
    public String pictureLink;
    public List<StreamInfo> channelStreams;

    public TVInfo() {
    }

    public String getTimeLeftNow() {
        long diff = end - new Date().getTime();

        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff)+1;

        return (minutes < 0 ? 0 : minutes) + " min(s). left";
    }

    public String getTimeLeftUpComing() {
        long diff = start - new Date().getTime();

        long days= TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff)+1;

        String timeleft=days>0? (days+" day(s)") : hours>0? (hours+" hour(s)"):minutes+" min(s)";
        return "in "+timeleft;
    }

    public boolean isPlaying() {
        long current=new Date().getTime();
        long currentStartDiff = current-start;
        long endCurrentDiff = end - current;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(endCurrentDiff);

        if(currentStartDiff>=0 && endCurrentDiff>=0)
            return true;
        else return  false;
    }

    public boolean isComingUp() {
        long current=new Date().getTime();
        if(current>start && current<end)
            return true;
        else return  false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeLong(this.start);
        dest.writeString(this.channelName);
        dest.writeInt(this.channelNo);
        dest.writeString(this.description);
        dest.writeString(this.pictureLink);
        dest.writeTypedList(this.channelStreams);
    }

    protected TVInfo(Parcel in) {
        this.title = in.readString();
        this.start = in.readLong();
        this.channelName = in.readString();
        this.channelNo = in.readInt();
        this.description = in.readString();
        this.pictureLink = in.readString();
        this.channelStreams = in.createTypedArrayList(StreamInfo.CREATOR);
    }

    public static final Creator<TVInfo> CREATOR = new Creator<TVInfo>() {
        @Override
        public TVInfo createFromParcel(Parcel source) {
            return new TVInfo(source);
        }

        @Override
        public TVInfo[] newArray(int size) {
            return new TVInfo[size];
        }
    };

    @Override
    public String toString() {
        return "TVInfo{" +
                "channelId=" + channelNo +
                ", title='" + title + '\'' +
                ", start=" + start +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", pictureLink='" + pictureLink + '\'' +
                ", channelStreams=" + channelStreams +
                '}';
    }

    public boolean isNew() {
        long diff = start - new Date().getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return minutes > 0;
    }

    @Override
    public int compare(TVInfo source, TVInfo desc) {
        return source.channelNo-desc.channelNo;
    }
}
