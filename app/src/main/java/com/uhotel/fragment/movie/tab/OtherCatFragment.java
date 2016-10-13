package com.uhotel.fragment.movie.tab;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.config.Config;
import com.uhotel.control.GridSpacingItemDecoration;
import com.uhotel.dto.CatInfo;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.VodInfo;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.fragment.movie.MovieTabFragment;
import com.uhotel.fragment.movie.VideoPlayerFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class OtherCatFragment extends Fragment implements ViewPagerTabListener, ViewPagerDestroyListener {
    public
    @BindView(R.id.imageView)
    ImageView imageView;
    public
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    public
    @BindView(R.id.txtCatName)
    TextView txtCatName;
    public
    @BindView(R.id.llCircle)
    LinearLayout llCircle;
    public
    @BindView(R.id.btnPre)
    ImageView btnPre;
    public
    @BindView(R.id.btnNext)
    ImageView btnNext;
    public
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;

    private CatInfo catInfo;
    private ProfileInfo profileInfo;

    public static OtherCatFragment init(int tabIndex) {
        OtherCatFragment fragment = new OtherCatFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tabIndex",tabIndex);

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
        profileInfo= (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_cat_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void loadOnSelect() {
        ViewPager viewPager = (ViewPager) getView().getParent();
        catInfo= ((MovieTabFragment.TabsPagerAdapter) viewPager.getAdapter()).getCatInfo(getArguments().getInt("tabIndex"));
        updateSubItem(catInfo.listVod.get(0));
        setupViewPager(catInfo, llCircle);
        setButton(0);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 3);
        MyAdapter featureAdapter = new MyAdapter(catInfo.listVod);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(featureAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 10), true));

    }

    @OnClick(R.id.btnPre)
    void onPreClick() {
        if (catInfo.selectIndex > 0)
            catInfo.selectIndex = --catInfo.selectIndex;
        setButton(catInfo.selectIndex);
        VodInfo subInfo = catInfo.listVod.get(catInfo.selectIndex);
        updateSubItem(subInfo);

    }

    @OnClick(R.id.btnNext)
    void oneNextClick() {
        if (catInfo.selectIndex < catInfo.listVod.size() - 1)
            catInfo.selectIndex = ++catInfo.selectIndex;
        setButton(catInfo.selectIndex);
        VodInfo subInfo = catInfo.listVod.get(catInfo.selectIndex);
        updateSubItem(subInfo);

    }

    @OnClick(R.id.imageView)
    void imageClick() {
        VodInfo subInfo = catInfo.listVod.get(catInfo.selectIndex);
        FragmentManager fm=getFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.add(R.id.fragment, VideoPlayerFragment.init(subInfo,false), VideoPlayerFragment.class.getName()).commitNow();

    }

    private void updateSubItem(VodInfo subInfo) {
        txtTitle.setText(subInfo.details.title);

        Glide.with(this).load(
                String.format(Config.IMAGE_PREFIX_URL,profileInfo.regionId)+ subInfo.details.poster)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    private void setupViewPager(CatInfo catInfo, LinearLayout llCircle) {
        llCircle.removeAllViews();
        for (int i = 0; i < catInfo.listVod.size(); i++) {
            ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(
                    R.layout.circle_item, null);
            Button button = (Button) view.getChildAt(0);
            view.removeAllViews();
            llCircle.addView(button);
        }

    }

    private void setButton(int index) {
        final Animation animationFadeIn = AnimationUtils.loadAnimation(context,
                R.anim.fadein);

        for (int i = 0; i < llCircle.getChildCount(); i++) {
            if (index == i) {
                llCircle.getChildAt(i)
                        .startAnimation(animationFadeIn);
                llCircle.getChildAt(i)
                        .setBackgroundResource(R.drawable.home_circle_selected_shape);
            } else {

                llCircle.getChildAt(i)
                        .setBackgroundResource(R.drawable.home_circle_shape);
            }
        }
    }

    @Override
    public void destroyAll() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<VodInfo> list;


        public MyAdapter(List<VodInfo> list) {

            this.list = list;

        }

        public void setList(List<VodInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchased_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final VodInfo item = list.get(pos);
            item.price=catInfo.purchasedInfo.priceList.get(0).price;
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.add(R.id.fragment, VideoPlayerFragment.init(item,false), VideoPlayerFragment.class.getName()).commitNow();

                }
            });

            //myViewHolder.imageView.setImageResource(Integer.parseInt(item.));
            Glide.with(OtherCatFragment.this).load(
                    String.format(Config.IMAGE_PREFIX_URL,profileInfo.regionId)+ item.details.poster)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imageView);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public
            @BindView(R.id.imageView)
            ImageView imageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    public interface OtherTabListener{
        CatInfo getCatInfo(int index);
    }
}
