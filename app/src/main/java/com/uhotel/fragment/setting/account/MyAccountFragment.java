package com.uhotel.fragment.setting.account;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.activity.MainActivity;
import com.uhotel.config.Constant;
import com.uhotel.control.AsteriskPassword;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MyAccountFragment extends Fragment {

    private Context context;
    private Unbinder unbinder;

    @BindView(R.id.btnleft)
    TextView btnLeft;
    @BindView(R.id.btnRight)
    TextView btnRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private boolean isEdit;

    private AlertDialog alertDialog;

    public static MyAccountFragment init() {
        MyAccountFragment fragment = new MyAccountFragment();
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
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<AccountItem> list = new ArrayList<>();

        list.add(new AccountItem("Name", "Michael Jackson", R.drawable.account_locked));
        list.add(new AccountItem("Room", "2005", R.drawable.account_locked));
        list.add(new AccountItem("Email", "user1@test.com", R.drawable.transparent));
        list.add(new AccountItem("Password", "111111", R.drawable.transparent));
        list.add(new AccountItem("Phone", "111-2222-3333", R.drawable.transparent));
        list.add(new AccountItem("Visa", "xxx-xxx-x012", R.drawable.transparent));
        list.add(new AccountItem("Notifications", "On", R.drawable.transparent));
        AccountAdapter hotelAdapter = new AccountAdapter(list);
        recyclerView.setAdapter(hotelAdapter);

    }

    @OnClick(R.id.btnleft)
    void leftClick() {
        if (!isEdit) {
            isEdit = true;
            btnLeft.setText("Cancel");
            btnRight.setText("Save Changes");

        } else {
            isEdit = false;
            btnLeft.setText("Edit Settings");
            btnRight.setText("Sign Out");

        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @OnClick(R.id.btnRight)
    void rightClick() {
        if (!isEdit) {
            Utility.showMessage(context, "sign out");
            MyPreference.setBoolean(Constant.PRE_IS_LOGINED, false);
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = LayoutInflater.from(context).inflate(R.layout.account_save_dialog, null);
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
    }


    class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<AccountItem> list;
        private LayoutInflater layoutInflater;


        public AccountAdapter(List<AccountItem> list) {

            this.list = list;
            layoutInflater = LayoutInflater.from(context);

        }

        public void setList(List<AccountItem> list) {
            this.list = list;
        }

        // other methods
        @Override
        public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            if(itemType==0)
                return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.account_item, viewGroup, false));
            else return new NotifyHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.account_notify_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder  viewHolder, final int pos) {
            final AccountItem item = list.get(pos);
            switch (getItemViewType(pos)){
                case 0:
                    MyViewHolder myViewHolder= (MyViewHolder) viewHolder;
                    myViewHolder.txtName.setText(item.name);
                    myViewHolder.txtValue.setText(item.value);
                    if (pos == 0 || pos == 1)
                        myViewHolder.txtValue.setEnabled(false);
                    else
                        myViewHolder.txtValue.setEnabled(isEdit);
                    if (item.name.toLowerCase().contains("password")) {
                        myViewHolder.txtValue.setTransformationMethod(AsteriskPassword.getInstance());
                    } else myViewHolder.txtValue.setTransformationMethod(null);
                    if (!isEdit) {
                        myViewHolder.viewGroup.setBackgroundResource(R.drawable.transparent);
                        myViewHolder.imaLock.setVisibility(View.GONE);
                    } else {
                        myViewHolder.viewGroup.setBackgroundResource(R.drawable.setting_row_selector);
                        myViewHolder.imaLock.setVisibility(View.VISIBLE);
                    }
                    if (item.imaResId != 0)
                        myViewHolder.imaLock.setImageResource(item.imaResId);
                    else myViewHolder.imaLock.setImageResource(R.drawable.transparent);

                    break;
                case 1:
                    NotifyHolder notifyHolder= (NotifyHolder) viewHolder;
                    notifyHolder.txtName.setText(item.name);
                    notifyHolder.toggleButton.setChecked(new Boolean(item.value));

                    notifyHolder.toggleButton.setEnabled(isEdit);
                    if (!isEdit) {
                        notifyHolder.viewGroup.setBackgroundResource(R.drawable.transparent);

                    } else {
                        notifyHolder.viewGroup.setBackgroundResource(R.drawable.setting_row_selector);

                    }

                    break;

            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

        @Override
        public int getItemViewType(int position) {
            if(position==list.size()-1)
                return 1;
            else return 0;
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
            @BindView(R.id.txtValue)
            TextView txtValue;
            public
            @BindView(R.id.imaLock)
            ImageView imaLock;
            public
            @BindView(R.id.root)
            ViewGroup viewGroup;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
        class NotifyHolder extends RecyclerView.ViewHolder {

            public
            @BindView(R.id.txtName)
            TextView txtName;
            public
            @BindView(R.id.toggleButton)
            ToggleButton toggleButton;

            public
            @BindView(R.id.root)
            ViewGroup viewGroup;

            public NotifyHolder(View itemView) {

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
        Utility.hideKeyboard(getView(),context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class AccountItem {
        public String name;
        public String value;
        public int imaResId;

        public AccountItem(String name, String value, int imaResId) {
            this.imaResId = imaResId;
            this.name = name;
            this.value = value;
        }
    }

}
