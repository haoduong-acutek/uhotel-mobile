package com.uhotel.fragment.concierge.carRental;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.fragment.listener.ViewPagerTabListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class CarRentalFragment extends Fragment implements ViewPagerTabListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;

    public static CarRentalFragment init() {
        CarRentalFragment fragment = new CarRentalFragment();
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
        View view = inflater.inflate(R.layout.car_rental_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<ItemInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new ItemInfo("address " + i, "Name " + i));
        }

        MyAdapter myAdapter = new MyAdapter(list);
        recyclerView.setAdapter(myAdapter);

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

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private List<ItemInfo> list;
        private int total;

        public MyAdapter(List<ItemInfo> list) {

            this.list = list;


        }

        public void setList(List<ItemInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.car_rental_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final ItemInfo detailItem = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            myViewHolder.txtName.setText(detailItem.name);
            myViewHolder.txtAddress.setText(detailItem.address);
            myViewHolder.imageView.setImageResource(R.drawable.carrental_item);


        }

        public ItemInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.txtName)
            TextView txtName;
            @BindView(R.id.txtAddress)
            TextView txtAddress;
            @BindView(R.id.imageView)
            ImageView imageView;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }

    class ItemInfo {
        public String url;
        public String name;
        public String address;

        public ItemInfo(String address, String name) {
            this.address = address;
            this.name = name;
        }
    }

}
