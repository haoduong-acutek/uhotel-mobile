package com.uhotel.fragment.concierge.parentalControl;


import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.config.Config;
import com.uhotel.control.GridSpacingItemDecoration;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.MyJsonObject;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.SettingInfo;
import com.uhotel.fragment.listener.ViewPagerTabListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kiemhao on 8/25/16.
 */
public class ParentalControlFragment extends Fragment implements ViewPagerTabListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnSave)
    TextView btnSave;
    @BindView(R.id.btnChangePin)
    TextView btnChangePin;
    @BindView(R.id.btnEnableAll)
    TextView btnEnableAll;
    private boolean isEnableAll;


    private Context context;
    private Unbinder unbinder;
    private String[] arrItem = new String[]{"TV", "Movies", "Concierge", "In-room Dining", "Room Control", "Apps"};

    public static ParentalControlFragment init() {
        ParentalControlFragment fragment = new ParentalControlFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { //only verify
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("isCorrect", false)) {
                    JsonObject jsonObject = new JsonObject();
                    List<ItemInfo> list = ((MyAdapter) recyclerView.getAdapter()).getList();
                    for (int i = 0; i < list.size(); i++) {
                        switch (i) {
                            case 0:
                                jsonObject.addProperty("watchTvState", list.get(i).isLocked);
                                break;
                            case 1:
                                jsonObject.addProperty("moviesState", list.get(i).isLocked);
                                break;
                            case 2:
                                jsonObject.addProperty("conciergeState", list.get(i).isLocked);

                                break;
                            case 3:
                                jsonObject.addProperty("fnaState", list.get(i).isLocked);
                                break;
                            case 4:
                                jsonObject.addProperty("roomControlState", list.get(i).isLocked);
                                break;
                            default:

                                break;
                        }
                    }
                    updateParentalControl(data.getStringExtra("currentPIN"), jsonObject.toString());
                }
                //Utility.showMessage(context,"verify pin "+data.getBooleanExtra("isCorrect",false));

            }
        } else if (requestCode == 2) { //verify for change pin
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("isCorrect", false)) {
                    PinChangeDialog dialog = PinChangeDialog.init(data.getStringExtra("currentPIN"));
                    dialog.setTargetFragment(this, 3);
                    dialog.show(getFragmentManager(), PinChangeDialog.class.getName());
                }
            }
        } else if (requestCode == 3) { //change pin
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("isCorrect", false)) {

                }
            }
        }
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
        View view = inflater.inflate(R.layout.parental_control_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        List<ItemInfo> list = new ArrayList<>();
        arrItem = new String[]{"LiveTV", "Movies", "Concierge", "Food &\nActivities", "Room Control"};
        SettingInfo settingInfo = ((ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class)).getSettingObject();
        for (int i = 0; i < 5; i++) {
            boolean isLocked = getValueSetting(i, settingInfo);
            list.add(new ItemInfo(isLocked, arrItem[i], isLocked ? R.drawable.locked : R.drawable.opened));
        }


        MyAdapter myAdapter = new MyAdapter(list);
        int spanCount = 2; // 3 columns
        int spacing = (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 20); // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerView.setAdapter(myAdapter);
    }

    private boolean getValueSetting(int pos, SettingInfo settingInfo) {
        switch (pos) {
            case 0:
                return settingInfo.watchTvState;
            case 1:
                return settingInfo.moviesState;
            case 2:
                return settingInfo.conciergeState;
            case 3:
                return settingInfo.fnaState;
            case 4:
                return settingInfo.roomControlState;
        }
        return false;
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

    @OnClick(R.id.btnSave)
    void editClick() {
        PinVerifyDialog parentalDialogFragment = PinVerifyDialog.init();
        parentalDialogFragment.setTargetFragment(this, 1);
        parentalDialogFragment.show(getFragmentManager(), PinVerifyDialog.class.getName());

    }

    @OnClick(R.id.btnChangePin)
    void changePinClick() {
        PinVerifyDialog parentalDialogFragment = PinVerifyDialog.init();
        parentalDialogFragment.setTargetFragment(this, 2);
        parentalDialogFragment.show(getFragmentManager(), PinVerifyDialog.class.getName());
    }

    @OnClick(R.id.btnEnableAll)
    void enableAllClick() {
        isEnableAll=!isEnableAll;
        setLockButton();
        MyAdapter myAdapter = (MyAdapter) recyclerView.getAdapter();
        myAdapter.setEnable(isEnableAll);
    }

    private void setLockButton(){
        if(isEnableAll)
            btnEnableAll.setText("Disable All");
        else btnEnableAll.setText("Enable All");
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        public List<ItemInfo> getList() {
            return list;
        }

        private List<ItemInfo> list;
        private int total;
        private boolean isLockedAll;

        public MyAdapter(List<ItemInfo> list) {
            this.list = list;
        }

        public void setList(List<ItemInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parental_control_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final ItemInfo detailItem = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailItem.isLocked = !detailItem.isLocked;
                    notifyItemChanged(pos);
                    for(ItemInfo item : list){
                        if(!item.isLocked) {
                            isEnableAll = false;
                            setLockButton();
                            break;
                        }
                    }
                }
            });
            myViewHolder.txtName.setText(detailItem.name);
            myViewHolder.imageView.setImageResource(detailItem.isLocked ? R.drawable.locked : R.drawable.opened);
            myViewHolder.viewGroup.setBackgroundResource(detailItem.isLocked ? R.drawable.control_bold : R.drawable.control);

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
            @BindView(R.id.imageView)
            ImageView imageView;
            @BindView(R.id.llBackground)
            ViewGroup viewGroup;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }

        public void setEnable(boolean isEnable) {
            for (ItemInfo itemInfo : list) {
                itemInfo.isLocked = isEnable;
            }
            notifyDataSetChanged();
        }
    }

    class ItemInfo {
        public int resId;
        public String name;
        public boolean isLocked;


        public ItemInfo(boolean isActive, String name, int resId) {
            this.isLocked = isActive;
            this.name = name;
            this.resId = resId;
        }
    }


    private void updateParentalControl(final String PIN, final String data) {
        Log.d(this.getClass().getName(), data + "_" + PIN);
        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonObject<Boolean>> call = taskService.udpateParentControl(Utility.getMacAddess(), profileInfo.profileUid, data, PIN);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<MyJsonObject<Boolean>>() {

            @Override
            public void onResponse(Call<MyJsonObject<Boolean>> call, Response<MyJsonObject<Boolean>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    MyJsonObject responseInfo = response.body();
                    if ((boolean) responseInfo.result) {

                        ProfileInfo profileInfo = ((ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class));
                        profileInfo.setting = data;
                        MyPreference.setObject("userInfo", profileInfo);
                    }
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<MyJsonObject<Boolean>> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context, "Update failed");
                Log.d(ParentalControlFragment.this.getClass().getName(), "error" + t.getMessage());
            }
        });

    }

}
