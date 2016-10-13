package com.uhotel.fragment.movie;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.dto.CatInfo;
import com.uhotel.dto.MovieTabInfo;
import com.uhotel.fragment.MainListener;
import com.uhotel.fragment.SmartFragmentStatePagerAdapter;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.fragment.movie.tab.FeatureFragment;
import com.uhotel.fragment.movie.tab.OtherCatFragment;
import com.uhotel.fragment.movie.tab.PurchasedFragment;
import com.uhotel.interfaces.OnBackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MovieTabFragment extends Fragment implements OnBackListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.vpHeader)
    RelativeLayout vpHeader;

    @BindView(R.id.txtPre)
    TextView txtPre;
    @BindView(R.id.txtCurrent)
    TextView txtCurrent;
    @BindView(R.id.txtNext)
    TextView txtNext;

    private List<CatInfo> catInfoList;
    private Context context;
    private Unbinder unbinder;
    private List<MovieTabInfo> listTab = new ArrayList<>();


    private int limit = 10;
    public static String TO_ITEM_FILTER = "ToItemFilter";
    private TabsPagerAdapter tabsPagerAdapter;

    public static MovieTabFragment init() {
        MovieTabFragment fragment = new MovieTabFragment();
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
        setHasOptionsMenu(true);;



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_tab_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        catInfoList=MyPreference.getListObject("listCat",CatInfo.class);
        if(catInfoList!=null){
            listTab =MyPreference.getListObject("listTab",MovieTabInfo.class);
            setupTabLayout();
        }
    }



    private void setupTabLayout(){
        tabsPagerAdapter = new TabsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        txtCurrent.setText(listTab.get(0).title);
        String next = listTab.get(1).title;
        txtNext.setText(next.substring(0, next.length() > limit ? limit - 1 : next.length()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    txtPre.setText("");
                    txtCurrent.setText(listTab.get(position).title);

                    String next = listTab.get(position + 1).title;
                    txtNext.setText(next.substring(0, next.length() > limit ? limit - 1 : next.length()));
                } else if (position == listTab.size() - 1) {

                    txtCurrent.setText(listTab.get(position).title);
                    String pre = listTab.get(position - 1).title;
                    txtPre.setText(pre.substring(pre.length() > limit ? pre.length() - limit : 0));

                    txtNext.setText("");
                } else {
                    txtCurrent.setText(listTab.get(position).title);
                    String pre = listTab.get(position - 1).title;
                    txtPre.setText(pre.substring(pre.length() > limit ? pre.length() - limit : 0));

                    String next = listTab.get(position + 1).title;
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
                viewPager.setCurrentItem(1);
            }
        }, 100);

    }

    @Override
    public void onBackPress() {
        ((ToolbarListener) getParentFragment()).toHomeFragment();
    }

//    @Override
//    public void onLoadLiveDone(List<CatInfo> catInfoList) {
//        this.catInfoList=catInfoList;
//        if(this.catInfoList!=null){
//            listTab =MyPreference.getListObject("listTab",MovieTabInfo.class);
//            setupTabLayout();
//        }
//    }

    public class TabsPagerAdapter extends SmartFragmentStatePagerAdapter {


        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PurchasedFragment.init();
                case 1:
                    return FeatureFragment.init();
                default:
                    return OtherCatFragment.init(position);


            }

        }

        public CatInfo getCatInfo(int tabIndex){
            return catInfoList.get(tabIndex-2);
        }
        @Override
        public int getCount() {

            return listTab.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTab.get(position).title;

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
        if(tabsPagerAdapter!=null) {
            for (int i = 0; i < tabsPagerAdapter.getCount(); i++) {
                if (tabsPagerAdapter.getRegisteredFragment(i) != null)
                    ((ViewPagerDestroyListener) tabsPagerAdapter.getRegisteredFragment(i)).destroyAll();
            }
        }
        unbinder.unbind();
        catInfoList=null;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(getFragmentManager().findFragmentByTag(VideoPlayerFragment.class.getName())==null)
                ((MainListener) getParentFragment().getParentFragment()).openMenu();
                return false;
        }
        return false;

    }


//    public interface FeatureListener{
//        void onLoadDone(List<CatInfo> catInfoList);
//    }
}
