package com.uhotel.dto;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kiemhao on 9/22/16.
 */
public class ProgramInfo {
    public Integer idProgram;
    public Integer idChannel;
    public String title;
    public String description;
    public long duration;
    public long start;
    public long end;
    public String pictureLink;
    public String seriesUid;
    public List<String> genres;
    public Integer ageRating;
    public boolean recordable;
    public boolean barred;
    public Integer idPayPerViewBlock;


    public String getTimeLeftString() {
        long diff = start - new Date().getTime();

        //long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (minutes < 0 ? 0 : minutes) + " min(s). left";
    }


}
