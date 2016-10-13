package com.uhotel.fragment.room;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.MainListener;
import com.uhotel.fragment.SmartFragmentStatePagerAdapter;
import com.uhotel.fragment.listener.OptionItemListener;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.interfaces.OnBackListener;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class RoomFragment extends Fragment implements OnBackListener, ToolbarListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.txtPre)
    TextView txtPre;
    @BindView(R.id.txtCurrent)
    TextView txtCurrent;
    @BindView(R.id.txtNext)
    TextView txtNext;
    @BindView(R.id.txtHeaderName)
    TextView txtHeaderName;
    @BindView(R.id.imaHeaderIcon)
    ImageView imaHeaderIcon;
    @BindView(R.id.imaHomeIcon)
    ImageView imaHomeIcon;
    @BindView(R.id.imaHomeText)
    TextView imaHomeText;

    private Context context;
    private Unbinder unbinder;
    private String[] tabName = new String[]{"Home","Away","Read","Sleep"};
    private int limit = 100;
    private TabsPagerAdapter tabsPagerAdapter;

    public static RoomFragment init() {
        RoomFragment fragment = new RoomFragment();
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
        View view = inflater.inflate(R.layout.food_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imaHeaderIcon.setImageResource(R.drawable.controls);
        txtHeaderName.setText("Room Control");

        tabsPagerAdapter = new TabsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        txtCurrent.setText(tabName[0]);
//        String next = tabName[1];
//        txtNext.setText(next.substring(0,next.length() > limit ? limit-1 :next.length()));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    txtPre.setText("");
                    txtCurrent.setText(tabName[position]);

                    String next = tabName[position + 1];
                    txtNext.setText(next.substring(0, next.length() > limit ? limit - 1 : next.length()));
                } else if (position == tabName.length - 1) {

                    txtCurrent.setText(tabName[position]);
                    String pre = tabName[position - 1];
                    txtPre.setText(pre.substring(pre.length() > limit ? pre.length() - limit : 0));

                    txtNext.setText("");
                } else {
                    txtCurrent.setText(tabName[position]);
                    String pre = tabName[position - 1];
                    txtPre.setText(pre.substring(pre.length() > limit ? pre.length() - limit : 0));

                    String next = tabName[position + 1];
                    txtNext.setText(next.substring(0, next.length() > limit ? limit - 1 : next.length()));
                }
                if(tabsPagerAdapter.getRegisteredFragment(position)!=null)
                ((ViewPagerTabListener) tabsPagerAdapter.getRegisteredFragment(position)).loadOnSelect();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);

    }


    @OnClick(R.id.imaHomeIcon)
    void homeIconClick() {
        ((OptionItemListener) tabsPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).onOptionItemHomeClick();
    }

    @OnClick(R.id.imaHomeText)
    void homeTextClick() {
        ((OptionItemListener) tabsPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).onOptionItemHomeClick();
    }


    @Override
    public void onBackPress() {
//        ((OnBackListener)tabsPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).onBackPress();
        Utility.toFragment(this, HomeFragment.init(), R.id.fragment);
    }


    @Override
    public void changeNavIcon(int resId) {
        imaHeaderIcon.setImageResource(resId);
    }


    @Override
    public void changeNavTitle(String text) {


    }

    @Override
    public void openMenu() {
        ((MainListener) getParentFragment()).openMenu();
    }

    @Override
    public void toHomeFragment() {
        Utility.toFragment(this, HomeFragment.init(), R.id.fragment);
    }


    public class TabsPagerAdapter extends SmartFragmentStatePagerAdapter {


        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Random random=new Random();

                    return com.uhotel.fragment.room.home.HomeFragment.init(random.nextInt(4));

        }


        @Override
        public int getCount() {

            return tabName.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabName[position];

        }

        public ToolbarListener getListener() {
            return RoomFragment.this;
        }
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
        for (int i = 0; i < tabsPagerAdapter.getCount(); i++) {
            if (tabsPagerAdapter.getRegisteredFragment(i) != null)
                ((ViewPagerDestroyListener) tabsPagerAdapter.getRegisteredFragment(i)).destroyAll();
        }
        unbinder.unbind();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
