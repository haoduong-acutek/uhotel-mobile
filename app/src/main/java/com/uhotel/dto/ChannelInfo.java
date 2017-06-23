package com.uhotel.dto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kiemhao on 9/22/16.
 */
public class ChannelInfo implements Comparator<ChannelInfo> {

    public Integer id;
    public String name;
    public Integer number;
    public String type;
    public boolean hd;
    public boolean broadcasting;
    public Integer activated;
    public Integer deactivated;
    public boolean programRecordable;
    public boolean timeshift;
    public boolean pauseAndResume;
    public boolean instantRecordable;
    public boolean voidEpgPopup;
    public Integer ageRating;
    public String icon;
    public String poster;
    public boolean localRecordable;
    public Object previewDuration;
    public List<StreamInfo> streams = new ArrayList<StreamInfo>();
    public List<TVInfo> listRightNow;

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "activated=" + activated +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", type='" + type + '\'' +
                ", hd=" + hd +
                ", broadcasting=" + broadcasting +
                ", deactivated=" + deactivated +
                ", programRecordable=" + programRecordable +
                ", timeshift=" + timeshift +
                ", pauseAndResume=" + pauseAndResume +
                ", instantRecordable=" + instantRecordable +
                ", voidEpgPopup=" + voidEpgPopup +
                ", ageRating=" + ageRating +
                ", icon='" + icon + '\'' +
                ", poster='" + poster + '\'' +
                ", localRecordable=" + localRecordable +
                ", previewDuration=" + previewDuration +
                ", streams=" + streams +
                '}';
    }

    @Override
    public int compare(ChannelInfo source, ChannelInfo desc) {
        return source.number-desc.number;
    }
}
