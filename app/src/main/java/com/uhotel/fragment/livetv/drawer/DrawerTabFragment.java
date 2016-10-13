package com.uhotel.fragment.livetv.drawer;


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
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.fragment.livetv.tab.NextFragment;
import com.uhotel.fragment.livetv.tab.NowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DrawerTabFragment extends Fragment implements TabLayout.OnTabSelectedListener ,DrawerTabListener {

    private Context context;
    private Unbinder unbinder;


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    public static DrawerTabFragment init() {
        DrawerTabFragment fragment = new DrawerTabFragment();
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
        View view = inflater.inflate(R.layout.rightnow_drawer_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupTab();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setDefaultTabWhenLoad();
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
        View v = LayoutInflater.from(context).inflate(R.layout.drawer_menu_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTextView);
        tv.setText(getTabText(position));

        return v;
    }

    private String getTabText(int position) {
        String[] arrTabText = new String[]{"Now", "Next"};
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
                replaceSubFragment(NowFragment.init(), NowFragment.class.getName());
                break;
            case 1:
                replaceSubFragment(NextFragment.init(), NextFragment.class.getName());
                break;
        }
    }


    public void replaceSubFragment(Fragment fragment, String tag) {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, fragment, tag);
        ft.commit();
    }

    @Override
    public void loadTab() {
        setDefaultTabWhenLoad();
    }



}
interface DrawerTabListener {
    void loadTab();
}
