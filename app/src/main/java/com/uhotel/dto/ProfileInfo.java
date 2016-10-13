package com.uhotel.dto;

import com.google.gson.Gson;

/**
 * Created by kiemhao on 9/20/16.
 */
public class ProfileInfo {

    public String subscriberUid;
    public String profileUid;
    public String name;
    public String gender;
    public Integer languageId;
    public Integer parentalRating;
    public boolean autoSubtitlesEnabled;
    public boolean autoAudioEnabled;
    public boolean tvRecommendationsEnabled;
    public boolean vodRecommendationsEnabled;
    public boolean sipPhoneNotificationsEnabled;
    public int userId;
    public int regionId;

    public SettingInfo getSettingObject() {
        try {
            String result = setting.replace("\\", "");
            return new Gson().fromJson(result, SettingInfo.class);
        } catch (Exception exp) {
            return new SettingInfo();
        }
    }

    public SettingInfo settingObject;
    public String setting;

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "autoAudioEnabled=" + autoAudioEnabled +
                ", subscriberUid='" + subscriberUid + '\'' +
                ", profileUid='" + profileUid + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", languageId=" + languageId +
                ", parentalRating=" + parentalRating +
                ", autoSubtitlesEnabled=" + autoSubtitlesEnabled +
                ", tvRecommendationsEnabled=" + tvRecommendationsEnabled +
                ", vodRecommendationsEnabled=" + vodRecommendationsEnabled +
                ", sipPhoneNotificationsEnabled=" + sipPhoneNotificationsEnabled +
                ", userId=" + userId +
                ", regionId=" + regionId +
                ", settingObject=" + settingObject +
                ", setting='" + setting + '\'' +
                '}';
    }
}
