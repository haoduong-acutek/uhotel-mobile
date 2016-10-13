package com.uhotel.fragment.livetv.commingUp;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.control.DividerItemDecoration;
import com.uhotel.dto.TVInfo;
import com.uhotel.fragment.MainListener;
import com.uhotel.fragment.livetv.drawer.DrawerContentListener;
import com.uhotel.fragment.livetv.player.VideoPlayerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class CommingUpFragment extends Fragment {

    private Context context;
    private Unbinder unbinder;
    private Handler handler;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static CommingUpFragment init(int itemIndex) {
        CommingUpFragment fragment = new CommingUpFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("itemIndex", itemIndex);
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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.commingup_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((DrawerContentListener) getParentFragment()).changeHomeIcon(R.drawable.ico_menu);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), false, false));

        final RightNowAdapter adapter = new RightNowAdapter(getDataset());
        recyclerView.setAdapter(adapter);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapter==null)
                    return;
                adapter.setList(getDataset());
                adapter.notifyDataSetChanged();
                handler.removeCallbacks(this);
                handler.postDelayed(this,60000);
            }
        },60000);

    }


    private List<TVInfo> getDataset(){
        List<TVInfo> tvInfoList= MyPreference.getListObject("listTV", TVInfo.class);
        if(tvInfoList==null)
            return new ArrayList<>();
        List<TVInfo> listNow=new ArrayList<>();
        boolean isFirst=false;
        for (TVInfo tvInfo:tvInfoList){
            if(isFirst) {
                listNow.add(tvInfo);
                isFirst=false;
            }
            if (tvInfo.isPlaying()) {
                isFirst = true;
                continue;
            }

        }
        return listNow;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((MainListener) getParentFragment().getParentFragment().getParentFragment()).openMenu();
                return true;
        }
        return false;
    }

    class RightNowAdapter extends RecyclerView.Adapter<RightNowAdapter.MyViewHolder> {
        private List<TVInfo> list;
        private LayoutInflater layoutInflater;


        public RightNowAdapter(List<TVInfo> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<TVInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.commingup_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            final TVInfo item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.toFragment(CommingUpFragment.this, VideoPlayerFragment.init(item,0,pos), R.id.fragment);
//                    ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.init();
//                    confirmDialogFragment.show(getFragmentManager(), ConfirmDialogFragment.class.getName());
                }
            });

            myViewHolder.txtTitle.setText(item.title);
            myViewHolder.txtDesc.setText(item.description);
            myViewHolder.txtTimeLeft.setText(item.getTimeLeftUpComing());
            myViewHolder.txtChannelName.setText(item.channelName);
            myViewHolder.txtChannelNo.setText("Channel: " + item.channelNo + "");
            Glide.with(CommingUpFragment.this).load(
                    item.pictureLink)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imageView);
//            Random random=new Random();
//            switch (random.nextInt(4)){
//                case 0:
//                    myViewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
//                    myViewHolder.imaStatus.setVisibility(View.GONE);
//                    break;
//                case 1:
//                    myViewHolder.imaStatus.setImageResource(R.drawable.clock);
//                    myViewHolder.itemView.setBackgroundColor(Color.parseColor("#5d6771"));
//                    myViewHolder.imaStatus.setVisibility(View.VISIBLE);
//                    break;
//                case 2:
//                    myViewHolder.imaStatus.setImageResource(R.drawable.record_big);
//                    myViewHolder.itemView.setBackgroundColor(Color.parseColor("#5c461f"));
//                    myViewHolder.imaStatus.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    myViewHolder.imaStatus.setVisibility(View.GONE);
//                    myViewHolder.itemView.setBackgroundColor(Color.parseColor("#5c461f"));
//                    break;
//           }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {


            public
            @BindView(R.id.txtTitle)
            TextView txtTitle;
            public
            @BindView(R.id.txtDesc)
            TextView txtDesc;
            public
            @BindView(R.id.txtTimeLeft)
            TextView txtTimeLeft;
            public
            @BindView(R.id.txtChannelName)
            TextView txtChannelName;
            public
            @BindView(R.id.txtChannelNo)
            TextView txtChannelNo;
            public
            @BindView(R.id.imaStatus)
            ImageView imaStatus;
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
        if(handler!=null)
            handler.removeCallbacksAndMessages(null);
    }

    class CommingUpItem {
        public int url;
        public String name;
        public String desc;
        public String duration;
        public String channel;
        public String detail;
        public int status; //0:nothing,1:record,2:schedule,3:playing


        public CommingUpItem(String name, String desc, String duration, String channel, String detail, int url, int status) {
            this.channel = channel;
            this.desc = desc;
            this.detail = detail;
            this.duration = duration;
            this.name = name;
            this.url = url;
            this.status = status;
        }
    }
}
