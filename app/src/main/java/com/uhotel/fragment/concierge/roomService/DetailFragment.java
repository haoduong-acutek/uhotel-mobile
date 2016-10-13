package com.uhotel.fragment.concierge.roomService;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.concierge.roomService.info.DetailItem;
import com.uhotel.fragment.concierge.roomService.info.MasterItem;
import com.uhotel.fragment.concierge.roomService.listener.RoomServiceListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DetailFragment extends Fragment {

    @BindView(R.id.root)
    ViewGroup root;
    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.btnRequest)
    Button btnRequest;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private final static String POSITION = "position";
    private final static String MASTER = "master";
    private Context context;
    private Unbinder unbinder;
    private int position;
    private MasterItem masterItem;
    int total = 0;

    public static DetailFragment init(int position, MasterItem masterItem, String transName) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putString("transName", transName);
        bundle.putParcelable(MASTER, masterItem);
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
        position = getArguments().getInt(POSITION);
        masterItem = getArguments().getParcelable(MASTER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_service_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            root.setTransitionName(getArguments().getString("transName"));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        DetailAdapter expandAdapter = new DetailAdapter(masterItem.list);
        recyclerView.setAdapter(expandAdapter);
        txtName.setText(masterItem.name);
        total = masterItem.total;
        String totalText = "";
        if (total > 0) {
            totalText = " (" + total + ")";
            txtName.setTextColor(getResources().getColor(R.color.orange));
            btnClose.setImageResource(R.drawable.ico_up_bold);
        }
        btnRequest.setText("Request" + totalText);


    }


    @OnClick(R.id.btnClose)
    void close() {
        RoomServiceListener parentListener = (RoomServiceListener) getParentFragment();
        MasterItem masterItem = new MasterItem();
        masterItem.name = parentListener.getArrMaster()[position];
        masterItem.total = total;
        masterItem.list = ((DetailAdapter) recyclerView.getAdapter()).list;
        ((RoomServiceListener) getParentFragment()).getHashMap().put(position, masterItem);
        Utility.toFragment(DetailFragment.this, MasterFragment.init(), R.id.fragment);

    }

    class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
        private List<DetailItem> list;
        private int total;

        public DetailAdapter(List<DetailItem> list) {

            this.list = list;


        }

        public void setList(List<DetailItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_service_item_child, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final DetailItem detailItem = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            myViewHolder.txtName.setText(detailItem.name);
            myViewHolder.txtValue.setText(detailItem.value + "");

            myViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    detailItem.value = progress;
                    myViewHolder.txtValue.setText(detailItem.value + "");

                    total = total + detailItem.value;
                    if (detailItem.value == 0) {
                        myViewHolder.txtName.setTextColor(getResources().getColor(R.color.dark));
                        myViewHolder.txtValue.setTextColor(getResources().getColor(R.color.dark));
                    } else {
                        myViewHolder.txtName.setTextColor(getResources().getColor(R.color.white));
                        myViewHolder.txtValue.setTextColor(getResources().getColor(R.color.white));
                    }
                    updateRequestTotal();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            myViewHolder.seekBar.setProgress(detailItem.value);

        }

        public DetailItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView txtName;
            private TextView txtValue;
            private SeekBar seekBar;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                txtValue = (TextView) itemView.findViewById(R.id.txtValue);
                seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);


            }

        }
    }


    private void updateRequestTotal() {
        total = 0;
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {

            total = total + ((DetailAdapter) recyclerView.getAdapter()).getItem(i).value;
        }
        String totalText = "";
        if (total > 0)
            totalText = " (" + total + ")";
        btnRequest.setText("Request" + totalText);
        if (!totalText.equals("")) {
            txtName.setTextColor(getResources().getColor(R.color.orange));
            btnClose.setImageResource(R.drawable.ico_up_bold);
        } else {
            txtName.setTextColor(getResources().getColor(R.color.dark));
            btnClose.setImageResource(R.drawable.ico_up);
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
