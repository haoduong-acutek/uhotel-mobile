package com.uhotel.fragment.setting;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.MainListener;
import com.uhotel.fragment.setting.account.MyAccountFragment;
import com.uhotel.fragment.setting.device.DeviceFragment;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class SettingFragment extends Fragment implements TabLayout.OnTabSelectedListener, OnBackListener {

    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.txtHeaderName)
    TextView txtHeaderName;
    @BindView(R.id.imaHeaderIcon)
    ImageView imaHeaderIcon;
    @BindView(R.id.imaHomeIcon)
    ImageView imaHomeIcon;
    @BindView(R.id.imaHomeText)
    TextView imaHomeText;


    public static SettingFragment init() {
        SettingFragment fragment = new SettingFragment();
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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupTab();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDefaultTabWhenLoad();

        imaHeaderIcon.setImageResource(R.drawable.settings);
        txtHeaderName.setText("Settings");
    }


    void setDefaultTabWhenLoad() {
        //tabLayout.getTabAt(1).getCustomView().setSelected(false);
        tabLayout.setOnTabSelectedListener(this);
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
        tabLayout.getTabAt(0).select();

    }


    private void setupTab() {
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }
    }


    private View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.setting_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTextView);
        tv.setText(getTabText(position));
        return v;
    }

    private String getTabText(int position) {
        String[] arrTabText = new String[]{"My Account", "Devices"};
        return arrTabText[position];
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
    public void onTabSelected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(true);
        setCurrentTabFragment(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        tab.getCustomView().setSelected(true);
        setCurrentTabFragment(tab.getPosition());
    }


    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition) {
            case 0:
                replaceSubFragment(new MyAccountFragment(), MyAccountFragment.class.getName());
                break;
            case 1:
                replaceSubFragment(new DeviceFragment(), DeviceFragment.class.getName());
                break;
        }
    }


    public void replaceSubFragment(Fragment fragment, String tag) {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment, tag);
        ft.commit();
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
}
