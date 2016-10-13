package com.uhotel.fragment.concierge.roomService;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.fragment.concierge.roomService.info.MasterItem;
import com.uhotel.fragment.concierge.roomService.listener.RoomServiceListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class RoomServiceFragment extends Fragment implements RoomServiceListener, ViewPagerTabListener {

    private Context context;
    private Unbinder unbinder;
    private HashMap<Integer, MasterItem> hashMapRequest;
    private String[] arrMaster = new String[]{"Bath", "Hygiene", "Office", "Appliance", "Dental"};

    public static RoomServiceFragment init() {
        RoomServiceFragment fragment = new RoomServiceFragment();
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
        View view = inflater.inflate(R.layout.room_service_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loadOnSelect();
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

//    @Override
//    public void setValueToHashMap(int position,MasterItem masterItem) {
//        hashMapRequest.put(position,masterItem);
//    }
//
//    @Override
//    public MasterItem getMasterItem(int position) {
//        return hashMapRequest.get(position);
//    }

    @Override
    public String[] getArrMaster() {
        return arrMaster;
    }

    @Override
    public HashMap<Integer, MasterItem> getHashMap() {
        return hashMapRequest;
    }

    @Override
    public void loadOnSelect() {
        hashMapRequest = new HashMap<>();
        Utility.toChildFragment(this, MasterFragment.init(), R.id.fragment);
    }
}
