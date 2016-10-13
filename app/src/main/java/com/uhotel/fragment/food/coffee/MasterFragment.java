package com.uhotel.fragment.food.coffee;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.food.FoodFragment;
import com.uhotel.fragment.food.coffee.info.CoffeeItem;
import com.uhotel.fragment.listener.OptionItemListener;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.interfaces.OnBackListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MasterFragment extends Fragment implements OnBackListener, OptionItemListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;
    private ToolbarListener toolbarListener;
    private BroadcastReceiver broadcastReceiver;

    public static MasterFragment init() {
        MasterFragment fragment = new MasterFragment();
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
                int pos = intent.getIntExtra("position", -1);
                if (recyclerView == null)
                    return;
                CoffeeItem coffeeItem = ((CoffeeAdapter) recyclerView.getAdapter()).getItem(pos);
                Utility.toFragment(MasterFragment.this, com.uhotel.fragment.food.coffee.DetailFragment.init(coffeeItem), R.id.fragment);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coffee_master_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager1 = (ViewPager) getParentFragment().getView().getParent();
        toolbarListener = ((FoodFragment.TabsPagerAdapter) viewPager1.getAdapter()).getListener();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        List<CoffeeItem> list = new ArrayList<>();
        if (((CoffeeListener) getParentFragment()).getScreenIndex() == 0) {
            list.add(new CoffeeItem("Zazie", 5, "$$  French Restaurant", "941 Cole St. San Francisco, CA 94117", "Petite French bistro & popular brunch spot with an outdoor patio & weekly “Bring Your Dog” dinners.",
                    "Open today: 8AM-2PM, 5-9:30PM", "(414) 564-5332", R.drawable.coffee_1));
            list.add(new CoffeeItem("Starbelly", 5, "$$  Californian Restaurant", "3583 16th St. San Francisco, CA 94114", "Californian comfort food & drinks are served at this cafe, which also freatures an outdoor patio.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.coffee_2));
            list.add(new CoffeeItem("Sambrosa", 5, "$$$  Mexican Restaurant", "3200 Fillmore St. San Francisco, CA 94123", "Upscale Mexican dishes & cocktails are presented in a stylish room with banquettes & a U-shaped bar.",
                    "Open today: 4PM-2AM", "(415) 638-6500", R.drawable.coffee_3));
            list.add(new CoffeeItem("Sweet Maple", 3, "$$$  Breakfast Restaurant", "2101 Sutter St. San Francisco, CA 94115", "Local spot for homemade morning/lunch fare like signature brown-sugar-glazed “millionaire’s bacon.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.coffee_4));
        } else {
            list.add(new CoffeeItem("20th Century Cafe", 5, "$  Cafe", "198 Gough St. San Francisco, CA 94102", "Chic, retro corner bakery featuring sweet & savory specialties from Vienna, Budapest & Prague.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.restaurant_1));
            list.add(new CoffeeItem("Namu Gaji", 5, "$$  Asian Restaurant", "499 Dolores St. San Francisco, CA 94110", "An open kitchen turns out innovative dishes by way of local produce & Korean culinary traditions.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.restaurant_2));
            list.add(new CoffeeItem("Pauline’s Pizza", 3, "$$  Pizza Restaurant", "260 Valencia St. San Francisco, CA 94103", "Homegrown ingredients go into the pies & salads at this family-frieindly pizzeria with a wine room.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.restaurant_3));
            list.add(new CoffeeItem("Rich Table", 3, "$$$  Californian Restaurant", "199 Gough St. San Francisco, CA 94102", "Californian fare from local ingredients, served in a salvaged-barn-wood space with an open kitchen.",
                    "Mon - Thurs: 07.30am to 6.00pm", "(719) 262.9500", R.drawable.restaurant_4));
        }


        CoffeeAdapter coffeeAdapter = new CoffeeAdapter(list);
        recyclerView.setAdapter(coffeeAdapter);

        ViewPager viewPager = (ViewPager) getParentFragment().getView().getParent();
        ToolbarListener foodListener = ((FoodFragment.TabsPagerAdapter) viewPager.getAdapter()).getListener();
        foodListener.changeNavIcon(R.drawable.ico_menu);
        foodListener.changeNavTitle(null);
    }


    @Override
    public void onBackPress() {
        toolbarListener.toHomeFragment();
    }

    @Override
    public void onOptionItemHomeClick() {
        toolbarListener.openMenu();
    }

    class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.MyViewHolder> {
        private List<CoffeeItem> list;
        private LayoutInflater layoutInflater;

        public CoffeeItem getItem(int pos) {
            return list.get(pos);
        }

        public CoffeeAdapter(List<CoffeeItem> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<CoffeeItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.coffee_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            final CoffeeItem item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.toFragment(MasterFragment.this, DetailFragment.init(item), R.id.fragment);
                }
            });

            myViewHolder.txtName.setText(item.name);
            myViewHolder.txtResName.setText(item.resName);

            myViewHolder.ratingBar.setProgress(item.rating);

            Glide.with(context).load(item.url)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(myViewHolder.imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            //            public String name;
//            public int rating;
//            public String resName;
//            public String address;
//            public String detail;
//            public String openTime;
//            public String phone;
//            public int url;
            public
            @BindView(R.id.txtName)
            TextView txtName;
            public
            @BindView(R.id.ratingBar)
            RatingBar ratingBar;
            public
            @BindView(R.id.txtResName)
            TextView txtResName;

            public
            @BindView(R.id.imageView)
            ImageView imageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(FoodFragment.TO_ITEM_FILTER));
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

}
