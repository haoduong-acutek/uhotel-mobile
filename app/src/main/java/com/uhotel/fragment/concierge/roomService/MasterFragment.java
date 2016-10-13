package com.uhotel.fragment.concierge.roomService;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.control.DividerItemDecoration;
import com.uhotel.fragment.concierge.DetailsTransition;
import com.uhotel.fragment.concierge.roomService.info.DetailItem;
import com.uhotel.fragment.concierge.roomService.info.MasterItem;
import com.uhotel.fragment.concierge.roomService.listener.RoomServiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MasterFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnRequest)
    Button btnRequest;


    private Context context;
    private Unbinder unbinder;
    private RoomServiceListener roomServiceListener;
    private AlertDialog alertDialog;


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
        roomServiceListener = (RoomServiceListener) getParentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_service_master, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), true, false));

        List<MasterItem> list = new ArrayList<>();
        String[] arrMaster = ((RoomServiceListener) getParentFragment()).getArrMaster();
        for (String str : arrMaster) {
            list.add(new MasterItem(str));
        }

        RoomServiceAdapter expandAdapter = new RoomServiceAdapter(list);
        recyclerView.setAdapter(expandAdapter);

        HashMap<Integer, MasterItem> hashMap = roomServiceListener.getHashMap();
        Iterator it = hashMap.entrySet().iterator();
        int totalInt = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MasterItem masterItem = (MasterItem) pair.getValue();
            totalInt = totalInt + masterItem.total;
        }

        String totalText = "";
        if (totalInt > 0) {
            totalText = " Items (" + totalInt + ")";
            btnRequest.setBackgroundResource(R.drawable.rectangle_orange);
            btnRequest.setTextColor(getResources().getColor(R.color.orange));
        }
        btnRequest.setText("Request" + totalText);

    }

    @OnClick(R.id.btnRequest)
    void requestClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(context).inflate(R.layout.room_service_dialog, null);
        view.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    class RoomServiceAdapter extends RecyclerView.Adapter<RoomServiceAdapter.MyViewHolder> {
        private List<MasterItem> list;
        private LayoutInflater layoutInflater;


        public RoomServiceAdapter(List<MasterItem> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<MasterItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.room_service_item_master, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MasterItem masterItem = roomServiceListener.getHashMap().get(pos);
                    if (masterItem == null) {
                        masterItem = new MasterItem();
                        masterItem.name = roomServiceListener.getArrMaster()[pos];
                        masterItem.list = generateBasedOnPosition(pos);
                    }
                    DetailFragment detailFragment = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        detailFragment = DetailFragment.init(pos, masterItem, myViewHolder.root.getTransitionName());
                    } else detailFragment = DetailFragment.init(pos, masterItem, "");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        detailFragment.setSharedElementEnterTransition(new DetailsTransition());
                        //detailFragment.setEnterTransition(new Explode());
                        setExitTransition(new Fade());
                        //detailFragment.setSharedElementReturnTransition(new DetailsTransition());
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getFragmentManager()
                                .beginTransaction()
                                .addSharedElement(myViewHolder.root, myViewHolder.root.getTransitionName())
                                .replace(R.id.fragment, detailFragment, detailFragment.getClass().getName()).commit();
                    } else {
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment, detailFragment, detailFragment.getClass().getName()).commit();
                    }


                }
            });

            myViewHolder.imageView.setImageResource(R.drawable.ico_down);
            MasterItem masterItem = roomServiceListener.getHashMap().get(pos);
            String totalText = "";
            if (masterItem != null && masterItem.total > 0) {
                totalText = " (" + masterItem.total + ")";
                myViewHolder.txtName.setTextColor(getResources().getColor(R.color.orange));
                myViewHolder.imageView.setImageResource(R.drawable.ico_down_bold);
            } else {
                myViewHolder.txtName.setTextColor(getResources().getColor(R.color.dark));
                myViewHolder.imageView.setImageResource(R.drawable.ico_down);
            }
            myViewHolder.txtName.setText(list.get(pos).name + totalText);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myViewHolder.root.setTransitionName("name" + pos);
            }
        }


        private List<DetailItem> generateBasedOnPosition(int pos) {
            List<DetailItem> listBath = new ArrayList<>();
            switch (pos) {
                case 0:
                    listBath.add(new DetailItem("Bath towers", 0));
                    listBath.add(new DetailItem("Washcloths", 0));
                    listBath.add(new DetailItem("Bathwash", 0));
                    listBath.add(new DetailItem("Shampoo", 0));
                    listBath.add(new DetailItem("Conditioner", 0));
                    break;
                case 1:
                    listBath.add(new DetailItem("Hygiene Type 1", 0));
                    listBath.add(new DetailItem("Hygiene Type 2", 0));
                    listBath.add(new DetailItem("Hygiene Type 3", 0));
                    break;
                case 2:
                    listBath.add(new DetailItem("Office Type 1", 0));
                    listBath.add(new DetailItem("Office Type 2", 0));
                    listBath.add(new DetailItem("Office Type 3", 0));
                    break;
                case 3:
                    listBath.add(new DetailItem("Apps Gen 1", 0));
                    listBath.add(new DetailItem("Apps Gen 2", 0));
                    break;
                case 4:
                    listBath.add(new DetailItem("Dental Type 1", 0));
                    listBath.add(new DetailItem("Dental Type 2", 0));
                    break;
                default:

                    break;
            }
            return listBath;
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
            public
            @BindView(R.id.root)
            RelativeLayout root;

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
