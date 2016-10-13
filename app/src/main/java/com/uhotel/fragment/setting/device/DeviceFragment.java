package com.uhotel.fragment.setting.device;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.control.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class DeviceFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btnAddDevice)
    TextView btnAddDevice;

    private Context context;
    private Unbinder unbinder;

    public static DeviceFragment init() {
        DeviceFragment fragment = new DeviceFragment();
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
        View view = inflater.inflate(R.layout.device_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        List<DeviceInfo> list = new ArrayList<>();

        Random random = new Random();
        Random active = new Random();
        list.add(new DeviceInfo(random.nextInt(3) + 1, "device 1", active.nextBoolean()));
        list.add(new DeviceInfo(random.nextInt(3) + 1, "device 2", active.nextBoolean()));
        list.add(new DeviceInfo(random.nextInt(3) + 1, "device 3", active.nextBoolean()));
        list.add(new DeviceInfo(random.nextInt(3) + 1, "device 4", active.nextBoolean()));

        DeviceAdapter myAdapter = new DeviceAdapter(list);
        int spanCount = 2; // 3 columns
        int spacing = (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 60); // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerView.setAdapter(myAdapter);
    }


    @OnClick(R.id.btnAddDevice)
    void addDeviceClick() {
        AddDeviceDialogFragment dialogFragment = new AddDeviceDialogFragment();
        dialogFragment.show(getFragmentManager(), AddDeviceDialogFragment.class.getName());
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


    class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
        private List<DeviceInfo> list;
        private int total;

        public DeviceAdapter(List<DeviceInfo> list) {

            this.list = list;


        }

        public void setList(List<DeviceInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final DeviceInfo detailItem = list.get(pos);

            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    detailItem.isActive = !detailItem.isActive;
                    notifyItemChanged(pos);
                    return false;
                }
            });

            myViewHolder.txtName.setText(detailItem.name);
            switch (detailItem.type) {
                case 1:
                    if (detailItem.isActive)
                        myViewHolder.imageView.setImageResource(R.drawable.phone);
                    else myViewHolder.imageView.setImageResource(R.drawable.phone_inactive);
                    break;
                case 2:
                    if (detailItem.isActive)
                        myViewHolder.imageView.setImageResource(R.drawable.tablet);
                    else myViewHolder.imageView.setImageResource(R.drawable.tablet_inactive);
                    break;
                case 3:
                    if (detailItem.isActive)
                        myViewHolder.imageView.setImageResource(R.drawable.laptop);
                    else myViewHolder.imageView.setImageResource(R.drawable.laptop_inactive);
                    break;
            }


        }

        public DeviceInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txtName)
            TextView txtName;
            @BindView(R.id.imageView)
            ImageView imageView;


            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }

    class DeviceInfo {
        public DeviceInfo(int type, String name, boolean isActive) {
            this.isActive = isActive;
            this.name = name;

            this.type = type;
        }

        public int type; //1:phone, 2:tablet, 3:laptop
        public String name;

        public boolean isActive;


    }
}
