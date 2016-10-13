package com.uhotel.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;

import butterknife.BindView;

/**
 * Created by kiemhao on 8/25/16.
 */
public class BaseFragment extends Fragment {
    @BindView(R.id.imaHomeIcon)
    ImageView imaHomeIcon;
    @BindView(R.id.imaHomeText)
    TextView imaHomeText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
