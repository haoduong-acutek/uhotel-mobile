package com.uhotel.dto;

import java.util.List;

/**
 * Created by kiemhao on 9/20/16.
 */
public class MovieInfo {
    public String title;
    public String catName;
    public String url;
    public List<MovieSubInfo> listItem;
    public int selectIndex;

    public MovieInfo(String title, String catName, String url, List<MovieSubInfo> listItem) {
        this.catName = catName;
        this.listItem = listItem;
        this.title = title;
        this.url = url;
    }
}
