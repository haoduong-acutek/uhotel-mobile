package com.uhotel.fragment.livetv.tab;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.dto.TVInfo;
import com.uhotel.fragment.livetv.LiveTVListener;
import com.uhotel.fragment.livetv.player.VideoPlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class NowFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;


    public static NowFragment init() {
        NowFragment fragment = new NowFragment();
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
        View view = inflater.inflate(R.layout.rightnow_tab_now_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        NowAdapter hotelAdapter = new NowAdapter(getDataset());
        recyclerView.setAdapter(hotelAdapter);

    }

    private List<TVInfo> getDataset(){
        List<TVInfo> tvInfoList= MyPreference.getListObject("listTV", TVInfo.class);
        if(tvInfoList==null)
            return new ArrayList<>();
        List<TVInfo> listNow=new ArrayList<>();
        for (TVInfo tvInfo:tvInfoList){
            if (tvInfo.isPlaying())
                listNow.add(tvInfo);
        }
        return listNow;
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


    class NowAdapter extends RecyclerView.Adapter<NowAdapter.MyViewHolder> {
        private List<TVInfo> list;
        private LayoutInflater layoutInflater;


        public NowAdapter(List<TVInfo> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<TVInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rightnow_tab_now_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            final TVInfo item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VideoPlayerFragment.VIDEO_BC_FILTER);

                    intent.putExtra(VideoPlayerFragment.TV_INFO, item);

                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    ((LiveTVListener) getParentFragment().getParentFragment()).closeDrawer();
                }
            });

            myViewHolder.txtName.setText(item.channelName);
            myViewHolder.txtTitle.setText(item.title);
            myViewHolder.txtDuration.setText(item.getTimeLeftNow());

            Glide.with(NowFragment.this).load(
                    item.pictureLink)
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
            @BindView(R.id.txtName)
            TextView txtName;
            public
            @BindView(R.id.txtTitle)
            TextView txtTitle;
            public
            @BindView(R.id.txtDuration)
            TextView txtDuration;

            public
            @BindView(R.id.imageView)
            ImageView imageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }


}
