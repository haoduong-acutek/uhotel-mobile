package com.uhotel.fragment.concierge.houseKeeping;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.fragment.listener.ViewPagerTabListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class HouseKeepingFragment extends Fragment implements ViewPagerTabListener {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtBottom)
    TextView txtBottom;


    private Context context;
    private Unbinder unbinder;
    private boolean isClicked;

    public static HouseKeepingFragment init() {
        HouseKeepingFragment fragment = new HouseKeepingFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.house_keeping_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadOnSelect() {
        imageView.setImageResource(R.drawable.housekeeping);
        txtBottom.setText("Housekeeping please!");
        txtBottom.setTextColor(getResources().getColor(R.color.darker));
        isClicked = false;
    }

    @OnClick(R.id.imageView)
    void imageClick() {
        if (!isClicked) {
            imageView.setImageResource(R.drawable.housekeeping_bold);
            txtBottom.setText("Housekeeping coming!");
            txtBottom.setTextColor(getResources().getColor(R.color.dark));
            isClicked = true;
        }
    }
}
