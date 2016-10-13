package com.uhotel.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by kiemhao on 9/20/16.
 */
public class MyJsonList<T> {
    @Expose
    public List<T> result;
}

