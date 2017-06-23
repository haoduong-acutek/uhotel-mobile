package com.uhotel.fragment.livetv.drawer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.ChannelInfo;
import com.uhotel.dto.MyJsonString;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.ProgramInfo;
import com.uhotel.dto.TVInfo;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.livetv.commingUp.CommingUpFragment;
import com.uhotel.fragment.livetv.rightNow.RightNowFragment;
import com.uhotel.interfaces.OnBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DrawerContentFragment extends Fragment implements TabLayout.OnTabSelectedListener, OnBackListener, DrawerContentListener {

    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.txtHeaderName)
    TextView txtHeaderName;
    @BindView(R.id.imaHeaderIcon)
    ImageView imaHeaderIcon;


    public static DrawerContentFragment init() {
        DrawerContentFragment fragment = new DrawerContentFragment();
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
        View view = inflater.inflate(R.layout.live_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupTab();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);


        imaHeaderIcon.setImageResource(R.drawable.monitor);
        txtHeaderName.setText("Live TV");

//        setDefaultTabWhenLoad();
//        ((DrawerTabListener)getFragmentManager().findFragmentById(R.id.navigationFragment)).loadTab();
        new DownloadAsyncTask().execute();
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
        View v = LayoutInflater.from(context).inflate(R.layout.live_tab_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.tabTextView);
        tv.setText(getTabText(position));

        return v;
    }

    private String getTabText(int position) {
        String[] arrTabText = new String[]{"Right Now", "Coming Up"};
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
                replaceSubFragment(RightNowFragment.init(0), RightNowFragment.class.getName());
                break;
            case 1:
                replaceSubFragment(new CommingUpFragment(), CommingUpFragment.class.getName());
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
    public void onBackPress() {
        if(getChildFragmentManager().findFragmentById(R.id.fragment) instanceof OnBackListener)
            ((OnBackListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
        else
        getParentFragment().getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, HomeFragment.init(), HomeFragment.class.getName()).commit();

    }


    @Override
    public void changeHomeIcon(int resId) {
        toolbar.setNavigationIcon(resId);
    }

    @Override
    public void showTabBar() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTabBar() {
        tabLayout.setVisibility(View.GONE);
    }


    class DownloadAsyncTask extends AsyncTask<Void, Void, Void> {
        SimpleProgressDialog simpleProgressDialog;

        @Override
        protected Void doInBackground(Void... para) {
            RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
            ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
            Call<MyJsonString<String>> call = taskService.getChannel(Utility.getMacAddess(), "/restapi/rest/" + profileInfo.languageId + "/channels");

            try {
                String strResult = call.execute().body().result;
                String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";
                JSONObject jsonObject = new JSONObject(res);
                Type listType = new TypeToken<List<ChannelInfo>>() {
                }.getType();
                Gson gson = new Gson();
                List<ChannelInfo> listChannel = gson.fromJson(
                        new JsonParser().parse(jsonObject.getString("list"))
                                .getAsJsonArray(), listType);
                getChannelProgram(listChannel);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleProgressDialog = new SimpleProgressDialog(context);
            simpleProgressDialog.showBox();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleProgressDialog.dismiss();
            setDefaultTabWhenLoad();
            ((DrawerTabListener)getFragmentManager().findFragmentById(R.id.navigationFragment)).loadTab();
        }
    }

    private void getChannelProgram(final List<ChannelInfo> channelInfos) {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonString<String>> call = taskService.getChannelProgram(Utility.getMacAddess(),
                "/restapi/rest/" + profileInfo.languageId
                        + "/tvprogram?date=" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        try {
            String strResult = call.execute().body().result;

            String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";

            JSONObject jsonObject = new JSONObject(res);
            Type listType = new TypeToken<List<ProgramInfo>>() {
            }.getType();
            Gson gson = new Gson();
            List<ProgramInfo> listProgram = gson.fromJson(
                    new JsonParser().parse(jsonObject.getString("list"))
                            .getAsJsonArray(), listType);
            setListNowUp(channelInfos, listProgram);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    void setListNowUp(List<ChannelInfo> channelInfoList, List<ProgramInfo> programInfoList) {

        List<TVInfo> list= new ArrayList<>();
        TVInfo tvInfo;
        for (ProgramInfo programInfo : programInfoList) {

            tvInfo = new TVInfo();
            tvInfo.description = programInfo.description;

            tvInfo.title = programInfo.title;
            tvInfo.start = programInfo.start;
            tvInfo.end=programInfo.end;


            ChannelInfo channelInfo = getChannelInfoOnId(channelInfoList, programInfo.idChannel);
            if (channelInfo != null) {
                tvInfo.channelStreams = channelInfo.streams;
                tvInfo.channelName = channelInfo.name;
                tvInfo.pictureLink=channelInfo.icon;
                tvInfo.channelNo = channelInfo.number;
            }
            Log.d("listTV", tvInfo.title+"_"+tvInfo.channelNo+"_"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tvInfo.start))
            +"_"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(tvInfo.end)));

            list.add(tvInfo);

        }
        Collections.sort(list,new TVInfo());
        MyPreference.setListObject("listTV", list);
    }


    private ChannelInfo getChannelInfoOnId(List<ChannelInfo> channelInfoList, int channelId) {
        for (ChannelInfo channelInfo : channelInfoList) {
            if (channelInfo.id == channelId)
                return channelInfo;
        }
        return null;
    }

    private Date convertToGmt(Date date) {
        TimeZone tz = TimeZone.getDefault();
        Date ret = new Date(date.getTime() - tz.getRawOffset());

        // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
        if (tz.inDaylightTime(ret)) {
            Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

            // check to make sure we have not crossed back into standard time
            // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
            if (tz.inDaylightTime(dstDate)) {
                ret = dstDate;
            }
        }
        return ret;
    }

}
