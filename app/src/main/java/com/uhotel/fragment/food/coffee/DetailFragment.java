package com.uhotel.fragment.food.coffee;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DetailFragment extends Fragment implements OnBackListener, OptionItemListener {

    private Context context;
    private Unbinder unbinder;
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
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    public
    @BindView(R.id.txtDetail)
    TextView txtDetail;
    public
    @BindView(R.id.txtOpenTime)
    TextView txtOpenTime;
    public
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    public
    @BindView(R.id.imageView)
    ImageView imageView;

    private CoffeeItem coffeeItem;
    public static String COFFEE_ITEM = "coffee item";

    public static DetailFragment init(CoffeeItem hotelItem) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(COFFEE_ITEM, hotelItem);
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
        coffeeItem = getArguments().getParcelable(COFFEE_ITEM);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coffee_detail_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtName.setText(coffeeItem.name);
        ratingBar.setProgress(coffeeItem.rating);
        txtResName.setText(coffeeItem.resName);
        txtAddress.setText(coffeeItem.address);
        txtDetail.setText(coffeeItem.detail);
        txtOpenTime.setText(coffeeItem.openTime);
        txtPhone.setText(coffeeItem.phone);

        Glide.with(context).load(coffeeItem.url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);

        ViewPager viewPager = (ViewPager) getParentFragment().getView().getParent();
        ToolbarListener toolbarListener = ((FoodFragment.TabsPagerAdapter) viewPager.getAdapter()).getListener();
        toolbarListener.changeNavIcon(R.drawable.ico_back);
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
        Utility.toFragment(DetailFragment.this, MasterFragment.init(), R.id.fragment);
    }

    @Override
    public void onOptionItemHomeClick() {
        onBackPress();
    }
}
