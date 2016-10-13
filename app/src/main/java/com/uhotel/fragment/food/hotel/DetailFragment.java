package com.uhotel.fragment.food.hotel;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DetailFragment extends Fragment implements OnBackListener, OptionItemListener {

    private Context context;
    private Unbinder unbinder;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtDetail)
    TextView txtDetail;
    @BindView(R.id.imageView)
    ImageView imageView;

    private HotelItem hotelItem;
    public static String HOTEL_ITEM = "hotel item";

    public static DetailFragment init(HotelItem hotelItem) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(HOTEL_ITEM, hotelItem);
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
        hotelItem = getArguments().getParcelable(HOTEL_ITEM);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hotel_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName.setText(hotelItem.name);
        txtDetail.setText(hotelItem.detail);

        Glide.with(context).load(hotelItem.url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);



        ViewPager viewPager = (ViewPager) getParentFragment().getView().getParent();
        ToolbarListener toolbarListener = ((FoodFragment.TabsPagerAdapter) viewPager.getAdapter()).getListener();
        toolbarListener.changeNavIcon(R.drawable.ico_back);
        toolbarListener.changeNavTitle("Back");
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
    public void onBackPress() {
        Utility.toFragment(DetailFragment.this, com.uhotel.fragment.food.hotel.MasterFragment.init(), R.id.fragment);
    }

    @Override
    public void onOptionItemHomeClick() {
        onBackPress();
    }
}
