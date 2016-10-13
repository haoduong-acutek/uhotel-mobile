package com.uhotel.fragment.room.home;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.control.seekarc.SeekArc;
import com.uhotel.fragment.listener.OptionItemListener;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.fragment.room.RoomFragment;
import com.uhotel.interfaces.OnBackListener;

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
public class HomeFragment extends Fragment implements ViewPagerTabListener, ViewPagerDestroyListener,
        OnBackListener, OptionItemListener {

    private Context context;
    private Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btnUp)
    ImageView btnUp;
    @BindView(R.id.btnDown)
    ImageView btnDown;
    @BindView(R.id.seekArc)
    SeekArc seekArc;
    @BindView(R.id.txtProcess)
    TextView txtProcess;


    private HomeAdapter expandAdapter;

    public static HomeFragment init(int index) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index",index);
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
        View view = inflater.inflate(R.layout.room_home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider),true,false));
        int index=getArguments().getInt("index");
        Random random=new Random();
        List<ChildItem> list1 = new ArrayList<>();
        list1.add(new ChildItem("Main", random.nextInt(100)));
        list1.add(new ChildItem("Overhead", random.nextInt(100)));
        list1.add(new ChildItem("Wall", random.nextInt(100)));

        List<ChildItem> list2 = new ArrayList<>();
        list2.add(new ChildItem("Sheers", random.nextInt(100)));
        list2.add(new ChildItem("Blackouts",random.nextInt(100)));
        list2.add(new ChildItem("Sliders", random.nextInt(100)));

        List<MasterItem> list = new ArrayList<>();
        list.add(new MasterItem("Lights", list1));
        list.add(new MasterItem("Drapes", list2));

        expandAdapter = new HomeAdapter(context, list);
        recyclerView.setAdapter(expandAdapter);

        expandAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                ((MasterItem) expandAdapter.getParentItemList().get(position)).isExpand = true;
                expandAdapter.notifyParentItemChanged(position);

            }

            @Override
            public void onListItemCollapsed(int position) {
                ((MasterItem) expandAdapter.getParentItemList().get(position)).isExpand = false;
                expandAdapter.notifyParentItemChanged(position);
            }
        });
        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {

                txtProcess.setText(progress + "º");
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {

            }
        });
        seekArc.setProgress(random.nextInt(100));

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
//        Utility.toChildFragment(this, HomeFragment.init(getArguments().getInt("index")), R.id.fragment);
    }


    @Override
    public void destroyAll() {

        getFragmentManager().beginTransaction().remove(this).commit();
    }

    @Override
    public void onBackPress() {
        //((OnBackListener)getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
    }

    @Override
    public void onOptionItemHomeClick() {
        ViewPager viewPager = (ViewPager) getView().getParent();
        ToolbarListener toolbarListener = ((RoomFragment.TabsPagerAdapter) viewPager.getAdapter()).getListener();
        toolbarListener.openMenu();
    }


    @OnClick(R.id.btnUp)
    void onUpClick() {
        int process = seekArc.getProgress() + 1 > 100 ? 100 : seekArc.getProgress() + 1;
        seekArc.setProgress(process);

    }

    @OnClick(R.id.btnDown)
    void onDownClick() {
        int process = seekArc.getProgress() - 1 < 0 ? 0 : seekArc.getProgress() - 1;
        seekArc.setProgress(process);
//        txtProcess.setText(process+"º");
    }

    class HomeAdapter extends ExpandableRecyclerAdapter<MasterViewHolder, MyChildViewHolder> {

        private LayoutInflater mInflator;
        private TextView masterName;
        private ImageView masterImage;

        public HomeAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
            super(parentItemList);
            mInflator = LayoutInflater.from(context);

        }

        // onCreate ...
        @Override
        public MasterViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup, int viewType) {
            View masterView = mInflator.inflate(R.layout.room_home_item_master, parentViewGroup, false);
            masterName = (TextView) masterView.findViewById(R.id.txtName);
            masterImage = (ImageView) masterView.findViewById(R.id.imageView);
            return new MasterViewHolder(masterView);
        }

        @Override
        public MyChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup, int viewType) {
            View childView = mInflator.inflate(R.layout.room_home_item_child, childViewGroup, false);

            return new MyChildViewHolder(childView, masterName, masterImage);
        }

        // onBind ...
        @Override
        public void onBindParentViewHolder(MasterViewHolder masterViewHolder, int position, ParentListItem parentListItem) {
            MasterItem masterItem = (MasterItem) parentListItem;
            masterViewHolder.txtName.setText(masterItem.name);
            if (masterItem.isExpand) {
                masterViewHolder.lineView.setVisibility(View.GONE);
            } else {
                masterViewHolder.lineView.setVisibility(View.VISIBLE);
            }
            setMasterState(masterViewHolder, (List<ChildItem>) parentListItem.getChildItemList(), masterItem);


        }

        @Override
        public void onBindChildViewHolder(final MyChildViewHolder childViewHolder, final int parentPosition, final int childPosition, Object childListItem) {
            final ChildItem childItem = (ChildItem) childListItem;


            childViewHolder.txtName.setText(childItem.name);

            childViewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    childItem.value = i;
                    MasterItem masterItem = (MasterItem) getParentItemList().get(parentPosition);
                    List<ChildItem> childItemList = (List<ChildItem>) getParentItemList().get(parentPosition).getChildItemList();
                    MasterViewHolder masterViewHolder = null;
                    if (parentPosition == 0)
                        masterViewHolder = (MasterViewHolder) recyclerView.findViewHolderForAdapterPosition(parentPosition);
                    else {
                        MasterItem firstMasterItem = (MasterItem) getParentItemList().get(0);
                        masterViewHolder = (MasterViewHolder) recyclerView.findViewHolderForAdapterPosition(firstMasterItem.isExpand ? 4 : 1);
                    }
                    if (masterViewHolder == null)
                        return;
                    setMasterState(masterViewHolder, childItemList, masterItem);
                    setTextLeftRightColor(childViewHolder);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            childViewHolder.seekBar.setProgress(childItem.value);
            setTextLeftRightColor(childViewHolder);
        }


    }

    private void setMasterState(MasterViewHolder masterViewHolder, List<ChildItem> childItemList, MasterItem masterItem) {

        int total = 0;
        for (ChildItem childItem1 : childItemList) {
            total = total + childItem1.value;
        }
        if (total > 0) {
            if (masterItem.isExpand)
                masterViewHolder.imageView.setImageResource(R.drawable.ico_down_bold);
            else masterViewHolder.imageView.setImageResource(R.drawable.ico_up_bold);
            masterViewHolder.txtName.setTextColor(getResources().getColor(R.color.orange));
        } else {
            if (masterItem.isExpand)
                masterViewHolder.imageView.setImageResource(R.drawable.ico_down);
            else masterViewHolder.imageView.setImageResource(R.drawable.ico_up);
            masterViewHolder.txtName.setTextColor(getResources().getColor(R.color.dark));
        }
    }


    private void setTextLeftRightColor(MyChildViewHolder childViewHolder) {
        if (childViewHolder.seekBar.getProgress() > 0) {
            childViewHolder.txtLeft.setTextColor(getResources().getColor(R.color.dark));
            childViewHolder.txtRight.setTextColor(getResources().getColor(R.color.white));
        } else {
            childViewHolder.txtLeft.setTextColor(getResources().getColor(R.color.white));
            childViewHolder.txtRight.setTextColor(getResources().getColor(R.color.dark));
        }
    }


    class MasterViewHolder extends ParentViewHolder {

        private TextView txtName;
        private View lineView;
        private ImageView imageView;

        public MasterViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            lineView = itemView.findViewById(R.id.lineView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

        }

    }

    class MyChildViewHolder extends ChildViewHolder {

        private TextView txtName;
        private SeekBar seekBar;
        private TextView txtRight;
        private TextView txtLeft;


        public MyChildViewHolder(View itemView, TextView txtNameMaster, ImageView imageViewMaster) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtRight = (TextView) itemView.findViewById(R.id.txtRight);
            txtLeft = (TextView) itemView.findViewById(R.id.txtLeft);
            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
        }


    }

    class MasterItem implements ParentListItem {

        // a recipe contains several ingredients
        private List<ChildItem> list;
        public String name;
        public boolean isExpand;


        public MasterItem(String name, List list) {
            this.list = list;
            this.name = name;

        }

        @Override
        public List getChildItemList() {
            return list;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    class ChildItem {

        public String name;
        public int value;

        public ChildItem(String name, int value) {
            this.name = name;
            this.value = value;
        }


    }
}
