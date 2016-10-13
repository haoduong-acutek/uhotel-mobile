package com.uhotel.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.control.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class MenuFragment extends Fragment {

    @BindView(R.id.btnClose)
    View btnClose;
    @BindView(R.id.rcvMenu)
    RecyclerView rcvMenu;
    @BindView(R.id.imaLogo)
    ImageView imaLogo;


    private Context context;
    private Unbinder unbinder;
    int itemHeight;

    public static MenuFragment init() {
        MenuFragment fragment = new MenuFragment();
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
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        itemHeight = ((metrics.heightPixels - getStatusBarHeight()) -
                (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 60 + 15)) / 6;

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvMenu.setLayoutManager(linearLayoutManager);
        rcvMenu.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), true, false));
        List<MenuInfo> list = new ArrayList<>();
        list.add(new MenuInfo(R.drawable.reception_bell, "concierge"));
        list.add(new MenuInfo(R.drawable.monitor, "live tv"));
        list.add(new MenuInfo(R.drawable.video_clip, "movies"));
        list.add(new MenuInfo(R.drawable.cutlery, "food & activities"));
        list.add(new MenuInfo(R.drawable.controls, "room control"));
        list.add(new MenuInfo(R.drawable.settings, "settings"));
        MenuAdapter expandAdapter = new MenuAdapter(list);
        rcvMenu.setAdapter(expandAdapter);
    }

    @OnClick(R.id.btnClose)
    void close() {
        ((MainListener) getParentFragment()).onCloseClick();
    }

    @OnClick(R.id.imaLogo)
    void logoClick() {
        ((MainListener) getParentFragment()).onLogoClick();
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


    class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
        private List<MenuInfo> list;

        public MenuAdapter(List<MenuInfo> list) {

            this.list = list;

        }

        public void setList(List<MenuInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(MyViewHolder myViewHolder, final int pos) {
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainListener) getParentFragment()).onMenuItemSelect(pos);

                }
            });
            myViewHolder.itemView.getLayoutParams().height = itemHeight;
            myViewHolder.txtName.setText(list.get(pos).name);
            myViewHolder.imageView.setImageResource(list.get(pos).resId);

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


            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    class MenuInfo {

        public int resId;
        public String name;

        public MenuInfo(int resId, String name) {
            this.resId = resId;
            this.name = name;

        }
    }

}
