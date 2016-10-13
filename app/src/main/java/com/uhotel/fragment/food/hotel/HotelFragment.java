package com.uhotel.fragment.food.hotel;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.listener.OptionItemListener;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.interfaces.OnBackListener;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class HotelFragment extends Fragment implements ViewPagerTabListener, ViewPagerDestroyListener,
        OnBackListener, OptionItemListener {

    private Context context;
    private Unbinder unbinder;


    public static HotelFragment init() {
        HotelFragment fragment = new HotelFragment();
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
        View view = inflater.inflate(R.layout.hotel_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utility.toChildFragment(this, MasterFragment.init(), R.id.fragment);

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
        Utility.toChildFragment(this, MasterFragment.init(), R.id.fragment);
    }


    @Override
    public void destroyAll() {

        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onBackPress() {
        ((OnBackListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
    }

    @Override
    public void onOptionItemHomeClick() {
        ((OptionItemListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onOptionItemHomeClick();
    }
}
