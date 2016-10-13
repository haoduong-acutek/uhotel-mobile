package com.uhotel.fragment.food;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.MainListener;
import com.uhotel.fragment.SmartFragmentStatePagerAdapter;
import com.uhotel.fragment.food.attraction.AttractFragment;
import com.uhotel.fragment.food.coffee.CoffeeFragment;
import com.uhotel.fragment.food.hotel.HotelFragment;
import com.uhotel.fragment.listener.OptionItemListener;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class FoodFragment extends Fragment implements OnBackListener, ToolbarListener, FoodListener {
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
    private String[] tabName = new String[]{"Hotel Exclusives", "Coffee & Brunch", "Best SF Restaurant",
            "Top 4 attractions"};
    private int limit = 10;
    public static String TO_ITEM_FILTER = "ToItemFilter";
    private TabsPagerAdapter tabsPagerAdapter;

    public static FoodFragment init(int position) {
        FoodFragment fragment = new FoodFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
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
        setHasOptionsMenu(true);

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


        imaHeaderIcon.setImageResource(R.drawable.cutlery);
        txtHeaderName.setText("Food & Activities");
        imaHomeIcon.setImageResource(R.drawable.ico_menu);


        tabsPagerAdapter = new TabsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        txtCurrent.setText(tabName[0]);
        String next = tabName[1];
        txtNext.setText(next.substring(0, next.length() > limit ? limit - 1 : next.length()));

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
                ((ViewPagerTabListener) tabsPagerAdapter.getRegisteredFragment(position)).loadOnSelect();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPager == null)
                    return;
                int position = getArguments().getInt("position");
                switch (position) {
                    case 1:
                        Intent intent = new Intent(TO_ITEM_FILTER);
                        intent.putExtra("position", 0);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        break;
                    case 2:
                        intent = new Intent(TO_ITEM_FILTER);
                        intent.putExtra("position", 1);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        break;
                    case 3:
                        viewPager.setCurrentItem(1);
                        intent = new Intent(TO_ITEM_FILTER);
                        intent.putExtra("position", 2);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        break;
                }
            }
        }, 100);


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
        ((OnBackListener) tabsPagerAdapter.getRegisteredFragment(viewPager.getCurrentItem())).onBackPress();
    }


    @Override
    public void changeNavIcon(int resId) {
        imaHomeIcon.setImageResource(resId);
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

    @Override
    public void toHome() {
        Utility.toFragment(this, HomeFragment.init(), R.id.fragment);
    }


    public class TabsPagerAdapter extends SmartFragmentStatePagerAdapter {


        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HotelFragment.init();
                case 1:
                    return CoffeeFragment.init(0);
                case 2:
                    return CoffeeFragment.init(1);
                default:
                    return AttractFragment.init();

            }

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
            return FoodFragment.this;
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.live_tv_player, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
