package com.uhotel.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.dto.ProfileInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;

/**
 * Created by kiemhao on 8/25/16.
 */
public class HomeFragment extends Fragment implements YahooWeatherInfoListener {

    @BindView(R.id.root)
    ViewGroup rootView;
    @BindView(R.id.llExpand)
    ViewGroup viewGroup;
    @BindView(R.id.imaMenu)
    ImageView imaMenu;
    @BindView(R.id.rcvExpand)
    RecyclerView rcvExpand;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtWeather)
    TextView txtWeather;
    @BindView(R.id.imgWeather)
    ImageView imgWeather;

    private int screenHeight;
    private int visibleItem;

    private YahooWeather mYahooWeather;

    private Context context;
    private Unbinder unbinder;

    public static HomeFragment init() {
        HomeFragment fragment = new HomeFragment();
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
        mYahooWeather = YahooWeather.getInstance(5000, true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels - getStatusBarHeight();
        rootView.getLayoutParams().height = screenHeight;
//        Glide.with(this).load(R.drawable.landing_image).asBitmap().into(new SimpleTarget<Bitmap>(metrics.widthPixels,
//                screenHeight) {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Drawable drawable = new BitmapDrawable(resource);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    rootView.setBackground(drawable);
//                }
//            }
//        });
//        //rootView.setBackgroundResource(R.drawable.landing_image_copy);

        ProfileInfo profileInfo= (ProfileInfo) MyPreference.getObject("userInfo",ProfileInfo.class);
        txtName.setText((profileInfo.gender.toLowerCase().trim().equals("m")?"Mr.":"Ms.")+ profileInfo.name);

        //rcvExpand.setVisibility(View.VISIBLE);
        runDelayStartup();


        searchByPlaceName("united state");
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @OnClick(R.id.imaMenu)
    void openMenu() {

        ((MainListener) getParentFragment()).openMenu();
    }

    @OnClick(R.id.llExpand)
    void expand() {
        scrollView.scrollTo(0, screenHeight);
    }

    private void runDelayStartup(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(rcvExpand==null)
                    return;;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rcvExpand.setLayoutManager(linearLayoutManager);
                ExpandAdapter expandAdapter = new ExpandAdapter(context);
                List<ExpandInfo> list = new ArrayList<>();
                list.add(new ExpandInfo(R.drawable.hotel_1, "hotel exclusive", "In-room spa treatment", "Reserve today for 50% off"));
                list.add(new ExpandInfo(R.drawable.hotel_2, "hotel exclusive", "Bar Luxe", "Complimentary Drink"));
                list.add(new ExpandInfo(R.drawable.coffee_3, "", "Sambrosa", "Reserve a table & get one free appetizer"));
                list.add(new ExpandInfo(R.drawable.landing_drink, "", "Room Service", "Order Food"));
                list.add(new ExpandInfo(R.drawable.landing_movie, "", "Mad Max", "Rent Movies"));
                list.add(new ExpandInfo(R.drawable.landing_health, "", "Parental Control", "Edit Settings"));
                expandAdapter.setList(list);
                rcvExpand.setAdapter(expandAdapter);
                rcvExpand.setNestedScrollingEnabled(false);
            }
        },1000);
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



    class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.MyViewHolder> {
        private List<ExpandInfo> list;
        private Context context;
        private int itemShowCount;

        public ExpandAdapter(Context context) {
            this.context = context;
            this.list = new ArrayList<>();

        }

        public void setList(List<ExpandInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expand_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {

            Glide.with(HomeFragment.this).load(list.get(pos).bgURL)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(myViewHolder.imageView);
            myViewHolder.txtLine1.setText(list.get(pos).title);
            myViewHolder.txtLine2.setText(list.get(pos).name);
            myViewHolder.txtLine3.setText(list.get(pos).desc);

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainListener) getParentFragment()).toMenuItem(pos + 1);

                }
            });
        }

        private void calculateItemCount(int itemHeight) {
            visibleItem = 1;
            do {
                visibleItem++;
            }
            while (itemHeight * visibleItem < screenHeight);
            visibleItem--;

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public
            @BindView(R.id.line2)
            TextView txtLine2;
            public
            @BindView(R.id.line3)
            TextView txtLine3;
            public
            @BindView(R.id.line1)
            TextView txtLine1;
            public
            @BindView(R.id.imageView)
            ImageView imageView;


            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    class ExpandInfo {
        public int bgURL;

        public String name;
        public String title;
        public String desc;

        public ExpandInfo(int bgURL, String title, String name, String desc) {
            this.bgURL = bgURL;
            this.desc = desc;
            this.name = name;
            this.title = title;
        }
    }

    //--------Yahoo weather api
    private void searchByPlaceName(String location) {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.FAHRENHEIT);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.PLACE_NAME);
        mYahooWeather.queryYahooWeatherByPlaceName(context, location, this);
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo.getCurrentConditionIcon() != null) {
            imgWeather.setImageBitmap(weatherInfo.getCurrentConditionIcon());
        }
        if(weatherInfo!=null)
            txtWeather.setText(weatherInfo.getCurrentTemp()+" °F");

    }

}
