package com.uhotel.fragment.concierge;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.uhotel.fragment.concierge.businessCenter.BusinessCenterFragment;
import com.uhotel.fragment.concierge.carRental.CarRentalFragment;
import com.uhotel.fragment.concierge.checkout.CheckoutFragment;
import com.uhotel.fragment.concierge.houseKeeping.HouseKeepingFragment;
import com.uhotel.fragment.concierge.parentalControl.ParentalControlFragment;
import com.uhotel.fragment.concierge.roomService.RoomServiceFragment;
import com.uhotel.fragment.concierge.valet.ValetFragment;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class ConciergeFragment extends Fragment implements OnBackListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.txtHeaderName)
    TextView txtHeaderName;
    @BindView(R.id.imaHeaderIcon)
    ImageView imaHeaderIcon;
    @BindView(R.id.imaHomeIcon)
    ImageView imaHomeIcon;
    @BindView(R.id.imaHomeText)
    TextView imaHomeText;

    @BindView(R.id.txtPre)
    TextView txtPre;
    @BindView(R.id.txtCurrent)
    TextView txtCurrent;
    @BindView(R.id.txtNext)
    TextView txtNext;

    private Context context;
    private Unbinder unbinder;
    private String[] tabName = new String[]{"Room Service", "Car Rental", "Checkout", "Business Center", "House Keeping",
            "Valet", "Parental Control"};
    private int limit = 10;
    private TabsPagerAdapter tabsPagerAdapter;


    public static ConciergeFragment init(int position) {
        ConciergeFragment fragment = new ConciergeFragment();
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


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.concierge_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imaHeaderIcon.setImageResource(R.drawable.reception_bell);
        txtHeaderName.setText("concierge");

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
                        viewPager.setCurrentItem(0);
                        break;
                    case 2:
                        viewPager.setCurrentItem(6);
                        break;
                }
            }
        }, 100);

    }

    @OnClick(R.id.imaHomeIcon)
    void homeIconClick() {
        ((MainListener) getParentFragment()).openMenu();
    }

    @OnClick(R.id.imaHomeText)
    void homeTextClick() {
        ((MainListener) getParentFragment()).openMenu();
    }


    @Override
    public void onBackPress() {
        Utility.toFragment(this, HomeFragment.init(), R.id.fragment);
    }


    class TabsPagerAdapter extends SmartFragmentStatePagerAdapter {


        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return RoomServiceFragment.init();
                case 1:
                    return CarRentalFragment.init();

                case 2:
                    return CheckoutFragment.init();
                case 3:
                    return BusinessCenterFragment.init();

                case 4:
                    return HouseKeepingFragment.init();

                case 5:
                    return ValetFragment.init();

                default:
                    return ParentalControlFragment.init();

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
}
