package com.uhotel.fragment.food.attraction;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.uhotel.fragment.food.attraction.info.AttractItem;
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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.attract_master_fragment, container, false);
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

        List<AttractItem> list = new ArrayList<>();

        list.add(new AttractItem("Golden Gate Bridge", "The Golden Gate Bridge is a suspension bridge spanning the Golden Gate strait, the one-mile-wide (1.6 km), three-mile-long (4.8 km) channel between San Francisco Bay and the Pacific Ocean. The structure links the American city of San Francisco, California – the northern tip of the San Francisco Peninsula – to Marin County, carrying both U.S. Route 101 and California State Route 1 across the strait.", R.drawable.attraction_1));
        list.add(new AttractItem("Russian Hill", "Russian Hill is directly to the north (and slightly downhill) from Nob Hill, to the south (uphill) from Fisherman's Wharf, and to the west of the North Beach neighborhood. The Hill is bordered on its west side by parts of the neighborhoods of Cow Hollow and the Marina District.", R.drawable.attraction_2));
        list.add(new AttractItem("Alcatraz Island", "Alcatraz Island offers a close-up look at the site of the first lighthouse and US built fort on the West Coast, the infamous federal penitentiary long off-limits to the public, and the 18 month occupation by Indians of All Tribes which saved the tribes. Rich in history, there is also a natural side to the Rock - gardens, tide pools, bird colonies, and bay views beyond compare.", R.drawable.attraction_3));
        list.add(new AttractItem("Hyde St. Pier", "Everybody looks good at this dimly lit, wood-paneled spot, which quickly turned from a neighborhood favorite into a dining destination.", R.drawable.attraction_4));



        AttractAdapter hotelAdapter = new AttractAdapter(list);
        recyclerView.setAdapter(hotelAdapter);

        ViewPager viewPager = (ViewPager) getParentFragment().getView().getParent();
        ToolbarListener foodListener = ((FoodFragment.TabsPagerAdapter) viewPager.getAdapter()).getListener();
        foodListener.changeNavIcon(R.drawable.ico_menu);

    }


    @Override
    public void onBackPress() {
        toolbarListener.toHomeFragment();
    }

    @Override
    public void onOptionItemHomeClick() {
        toolbarListener.openMenu();
    }

    class AttractAdapter extends RecyclerView.Adapter<AttractAdapter.MyViewHolder> {
        private List<AttractItem> list;
        private LayoutInflater layoutInflater;


        public AttractAdapter(List<AttractItem> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<AttractItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.attract_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            final AttractItem item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.toFragment(MasterFragment.this, DetailFragment.init(item), R.id.fragment);
                }
            });

            myViewHolder.txtName.setText(item.name);

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
