package com.uhotel.fragment.movie.tab;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.uhotel.config.Config;
import com.uhotel.dto.CatInfo;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.VodInfo;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.fragment.movie.VideoPlayerFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class FeatureFragment extends Fragment implements ViewPagerTabListener, ViewPagerDestroyListener{
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;
    private int regionId;

    public static FeatureFragment init() {
        FeatureFragment fragment = new FeatureFragment();
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
        ProfileInfo profileInfo= (ProfileInfo) MyPreference.getObject("userInfo",ProfileInfo.class);
        regionId=profileInfo.regionId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feature_fragment, container, false);
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
        List<CatInfo> listCat=MyPreference.getListObject("listCat",CatInfo.class);
        if(listCat!=null)
            setupAdapter(listCat);
    }

    @Override
    public void destroyAll() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }



    class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.MyViewHolder> {
        private List<CatInfo> list;



        public FeatureAdapter(List<CatInfo> list) {

            this.list = list;


        }

        public void setList(List<CatInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feature_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final CatInfo item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VodInfo subInfo = item.listVod.get(item.selectIndex);
                    subInfo.price=item.purchasedInfo.priceList.get(0).price;

                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.add(R.id.fragment, VideoPlayerFragment.init(subInfo,false), VideoPlayerFragment.class.getName()).commitNow();
                }
            });

            myViewHolder.txtVodName.setText(item.listVod.get(0).details.title);
            myViewHolder.txtCatName.setText(item.title);


            Glide.with(FeatureFragment.this).load(
                    String.format(Config.IMAGE_PREFIX_URL,regionId)+ item.listVod.get(0).details.poster)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imageView);
            setupViewPager(item.listVod, myViewHolder.llCircle);
            setButton(0, myViewHolder.llCircle);
            myViewHolder.btnPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.selectIndex > 0)
                        item.selectIndex = --item.selectIndex;
                    setButton(item.selectIndex, myViewHolder.llCircle);
                    VodInfo subInfo = item.listVod.get(item.selectIndex);
//                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right);
//                    myViewHolder.root.startAnimation(animation);
                    updateSubItem(myViewHolder, subInfo);
                }
            });
            myViewHolder.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.selectIndex < item.listVod.size() - 1)
                        item.selectIndex = ++item.selectIndex;
                    setButton(item.selectIndex, myViewHolder.llCircle);
                    VodInfo subInfo = item.listVod.get(item.selectIndex);
                    updateSubItem(myViewHolder, subInfo);

                }
            });
        }

        private void updateSubItem(MyViewHolder myViewHolder, VodInfo subInfo) {
            myViewHolder.txtVodName.setText(subInfo.details.title);
            Glide.with(FeatureFragment.this).load(
                    String.format(Config.IMAGE_PREFIX_URL,regionId)+ subInfo.details.poster)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imageView);

        }

        private void setupViewPager(List<VodInfo> result, LinearLayout llCircle) {
            llCircle.removeAllViews();
            for (int i = 0; i < result.size(); i++) {
                ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(
                        R.layout.circle_item, null);
                Button button = (Button) view.getChildAt(0);
                view.removeAllViews();
                llCircle.addView(button);
            }

        }

        private void setButton(int index, LinearLayout llCircle) {
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
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public
            @BindView(R.id.imageView)
            ImageView imageView;
            public
            @BindView(R.id.txtVodName)
            TextView txtVodName;
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

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    private void setupAdapter(List<CatInfo> catInfoList){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        FeatureAdapter featureAdapter = new FeatureAdapter(catInfoList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(featureAdapter);
    }




}
