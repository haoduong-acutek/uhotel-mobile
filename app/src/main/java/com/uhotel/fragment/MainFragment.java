package com.uhotel.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.SettingInfo;
import com.uhotel.fragment.concierge.ConciergeFragment;
import com.uhotel.fragment.food.FoodFragment;
import com.uhotel.fragment.livetv.LiveTVFragment;
import com.uhotel.fragment.movie.MovieFragment;

import com.uhotel.fragment.room.RoomFragment;
import com.uhotel.fragment.setting.SettingFragment;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MainFragment extends Fragment implements MainListener, OnBackListener {


    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    private Context context;
    private Unbinder unbinder;
    private int drawerPos;
    private int landingPos;

    public static MainFragment init() {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AccessDialogFragment.REQUEST_CODE_MENU_POS) {
            if (resultCode == AccessDialogFragment.RESULT_CODE_UNLOCK) {

                switch (drawerPos) {
                    case 0:
                        Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(0), R.id.fragment);
                        break;
                    case 1:
                        Utility.toChildFragment(MainFragment.this, LiveTVFragment.init(), R.id.fragment);
                        break;
                    case 2:
                        Utility.toChildFragment(MainFragment.this, MovieFragment.init(), R.id.fragment);
                        break;
                    case 3:
                        Utility.toChildFragment(MainFragment.this, FoodFragment.init(0), R.id.fragment);
                        break;
                    case 4:
                        Utility.toChildFragment(MainFragment.this, RoomFragment.init(), R.id.fragment);
                        break;
                    case 5:
                        Utility.toChildFragment(MainFragment.this, SettingFragment.init(), R.id.fragment);
                        break;
                }
            } else if (resultCode == AccessDialogFragment.RESULT_CODE_HOME) {
                Utility.toChildFragment(MainFragment.this, HomeFragment.init(), R.id.fragment);
            }
        } else if (requestCode == AccessDialogFragment.REQUEST_CODE_LANDING_POS) {
            if (resultCode == AccessDialogFragment.RESULT_CODE_UNLOCK) {

                switch (landingPos) {
                    case 1:
                        Utility.toChildFragment(MainFragment.this, FoodFragment.init(1), R.id.fragment);
                        break;
                    case 2:
                        Utility.toChildFragment(MainFragment.this, FoodFragment.init(2), R.id.fragment);
                        break;
                    case 3:
                        Utility.toChildFragment(MainFragment.this, FoodFragment.init(3), R.id.fragment);
                        break;
                    case 4:
                        Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(1), R.id.fragment);
                        break;
                    case 5:
                        Utility.toChildFragment(MainFragment.this, MovieFragment.init(), R.id.fragment);
                        break;
                    case 6:
                        Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(2), R.id.fragment);
                        break;
                }
            } else if (resultCode == AccessDialogFragment.RESULT_CODE_HOME) {
                Utility.toChildFragment(MainFragment.this, HomeFragment.init(), R.id.fragment);
            }
        }
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
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Utility.toChildFragment(this, HomeFragment.init(), R.id.fragment);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                toMenuItem();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
    public void openMenu() {
        drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void onMenuItemSelect(int position) {
        drawerPos = position;
        drawerLayout.closeDrawers();

    }

    @Override
    public void onCloseClick() {
        drawerPos = -1;
        drawerLayout.closeDrawers();
    }

    @Override
    public void onLogoClick() {
        Utility.toChildFragment(MainFragment.this, HomeFragment.init(), R.id.fragment);
        onCloseClick();
    }

    @Override
    public void toMenuItem(int landingPos) {
        this.landingPos = landingPos;
        SettingInfo settingInfo = ((ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class)).getSettingObject();
        switch (landingPos) {
            case 1:
                if (settingInfo.fnaState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, FoodFragment.init(1), R.id.fragment);
                break;
            case 2:
                if (settingInfo.fnaState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, FoodFragment.init(2), R.id.fragment);
                break;
            case 3:
                if (settingInfo.fnaState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, FoodFragment.init(3), R.id.fragment);
                break;
            case 4:
                if (settingInfo.conciergeState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(1), R.id.fragment);
                break;
            case 5:
                if (settingInfo.moviesState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, MovieFragment.init(), R.id.fragment);
                break;
            case 6:
                if (settingInfo.conciergeState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_LANDING_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(2), R.id.fragment);
                break;
        }
    }

    private void showAccessDialog(int requestCode) {
        AccessDialogFragment accessDialog = AccessDialogFragment.init();
        accessDialog.setTargetFragment(this, requestCode);
        accessDialog.show(getFragmentManager(), AccessDialogFragment.class.getName());
    }

    private void toMenuItem() {

        SettingInfo settingInfo = ((ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class)).getSettingObject();
        switch (drawerPos) {
            case 0:
                if (settingInfo.conciergeState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_MENU_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, ConciergeFragment.init(0), R.id.fragment);
                break;
            case 1:
                if (settingInfo.watchTvState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_MENU_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, LiveTVFragment.init(), R.id.fragment);
                break;
            case 2:
                if (settingInfo.moviesState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_MENU_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, MovieFragment.init(), R.id.fragment);
                break;
            case 3:
                if (settingInfo.fnaState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_MENU_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, FoodFragment.init(0), R.id.fragment);
                break;
            case 4:
                if (settingInfo.roomControlState) {
                    showAccessDialog(AccessDialogFragment.REQUEST_CODE_MENU_POS);
                } else
                    Utility.toChildFragment(MainFragment.this, RoomFragment.init(), R.id.fragment);
                break;
            case 5:
                Utility.toChildFragment(MainFragment.this, SettingFragment.init(), R.id.fragment);
                break;
        }
    }


    @Override
    public void onBackPress() {
        ((OnBackListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
    }
}
