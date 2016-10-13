package com.uhotel.control;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerTabStrip;
import android.util.AttributeSet;

import com.uhotel.R;

public class MyPagerTabStrip extends PagerTabStrip {
    public MyPagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyPagerTabStrip);
        setTabIndicatorColor(Color.RED);
        setDrawFullUnderline(true);

        a.recycle();
    }

}