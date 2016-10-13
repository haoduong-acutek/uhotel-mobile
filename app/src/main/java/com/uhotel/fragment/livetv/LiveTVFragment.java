package com.uhotel.fragment.livetv;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.livetv.drawer.DrawerContentFragment;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class LiveTVFragment extends Fragment implements LiveTVListener, OnBackListener {

    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.drawer)
    DrawerLayout drawer;

    public static String ENABLE_SLIDE = "enable slide";
    public static String LIVE_TV_FILTER = "live tv filter";
    private BroadcastReceiver broadcastReceiver;

    public static LiveTVFragment init() {
        LiveTVFragment fragment = new LiveTVFragment();
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
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                drawer.setDrawerLockMode(intent.getBooleanExtra(ENABLE_SLIDE, false) ?
                        DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rightnow_drawer_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Utility.toChildFragment(this, DrawerContentFragment.init(), R.id.fragment);


    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(LIVE_TV_FILTER));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
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
    public void closeDrawer() {
        drawer.closeDrawers();
    }


    @Override
    public void onBackPress() {
        ((OnBackListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
    }
}
