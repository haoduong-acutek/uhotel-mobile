package com.uhotel.fragment.setting.device;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kiemhao on 8/31/16.
 */
public class AddDeviceDialogFragment extends DialogFragment {


    @BindView(R.id.btnAdd)
    Button btnAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private DeviceAdapter deviceAdapter;
    private Context context;

    public static AddDeviceDialogFragment init() {
        AddDeviceDialogFragment pinDialogFragment = new AddDeviceDialogFragment();
        Bundle bundle = new Bundle();

        pinDialogFragment.setArguments(bundle);
        return pinDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.device_add_dialog_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<DeviceInfo> list = new ArrayList<>();
        list.add(new DeviceInfo("device 1"));
        list.add(new DeviceInfo("device 2"));
        list.add(new DeviceInfo("device 3"));
        list.add(new DeviceInfo("device 4"));


        deviceAdapter = new DeviceAdapter(list);
        recyclerView.setAdapter(deviceAdapter);


    }

    @OnClick(R.id.btnClose)
    void closeClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnAdd)
    void addClick() {
        String result = "";
        for (DeviceInfo deviceInfo : deviceAdapter.list) {
            if (deviceInfo.checked)
                result = result + deviceInfo.name;

        }
        Utility.showMessage(context, result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }


    class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
        public List<DeviceInfo> list;
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
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_add_dialog_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final DeviceInfo detailItem = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myViewHolder.checkBox.performClick();
                }
            });
            myViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    detailItem.checked = b;
                }
            });
            myViewHolder.txtName.setText(detailItem.name);


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
            @BindView(R.id.checkbox)
            CheckBox checkBox;


            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

        }
    }

    class DeviceInfo {
        public DeviceInfo(String name) {

            this.name = name;


        }

        public boolean checked;
        public String name;


    }

}
