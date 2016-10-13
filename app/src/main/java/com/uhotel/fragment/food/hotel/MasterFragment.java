package com.uhotel.fragment.food.hotel;


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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.food.FoodFragment;
import com.uhotel.fragment.food.hotel.info.HotelItem;
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
                HotelItem hotelItem = ((HotelAdapter) recyclerView.getAdapter()).getItem(pos);
                Utility.toFragment(MasterFragment.this, com.uhotel.fragment.food.hotel.DetailFragment.init(hotelItem), R.id.fragment);
            }
        };

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotel_master_fragment, container, false);
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

        List<HotelItem> list = new ArrayList<>();

        list.add(new HotelItem("In-room spa treatment", "Reserve today for 50% off", "Step into an urban oasis of water, basalt rock, glass tile and signature rituals inspried by the rugged natural beauty of the Northwest in our hotel. Reserve today for 50% off!", R.drawable.hotel_1));
        list.add(new HotelItem("Bar Luxe", "Complimentary Drink",
                "Sip exotic martinis, famous champagnes and ports, indulgent coffees and cocktails of very description while enjoying savory hor d’oeuvres and other tempting bites. Thursday through Saturday features live piano music during cocktail hour starting at 7pm. Complimentary drink for hotel guests. ", R.drawable.hotel_2));
        list.add(new HotelItem("Couples Package", "Amenities vary with location",
                "Shar champagne on the balcony, spa treatments, or room service and a movie. Amenities very by location. Call us for details.", R.drawable.hotel_3));
        list.add(new HotelItem("Players Club", "Hotel guests receive $100 credit",
                "It’s free and easy, join today! Simply present a valid photo identification at the Diamond Dividents Players Club booth and we will immediately enroll you into our palyer rewards program. Hotel guests recieve $100 credit.", R.drawable.hotel_4));
        HotelAdapter hotelAdapter = new HotelAdapter(list);
        recyclerView.setAdapter(hotelAdapter);

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

    class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {
        private List<HotelItem> list;
        private LayoutInflater layoutInflater;

        public HotelItem getItem(int pos) {
            return list.get(pos);
        }

        public HotelAdapter(List<HotelItem> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<HotelItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hotel_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            final HotelItem item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.toFragment(MasterFragment.this, com.uhotel.fragment.food.hotel.DetailFragment.init(item), R.id.fragment);
                }
            });

            myViewHolder.txtName.setText(item.name);
            myViewHolder.txtDesc.setText(item.desc);
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

            public
            @BindView(R.id.txtName)
            TextView txtName;
            public
            @BindView(R.id.txtDesc)
            TextView txtDesc;
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
