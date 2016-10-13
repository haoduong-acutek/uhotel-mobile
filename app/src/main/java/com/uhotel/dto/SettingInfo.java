package com.uhotel.dto;

/**
 * Created by kiemhao on 9/21/16.
 */
public class SettingInfo {

    public boolean watchTvState;
    public boolean moviesState;
    public boolean conciergeState;
    public boolean fnaState;
    public boolean roomControlState;

    @Override
    public String toString() {
        return "SettingInfo{" +
                "conciergeState=" + conciergeState +
                ", watchTvState=" + watchTvState +
                ", moviesState=" + moviesState +
                ", fnaState=" + fnaState +
                ", roomControlState=" + roomControlState +
                '}';
    }
}
