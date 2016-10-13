package com.uhotel.fragment.concierge.valet;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.fragment.listener.ViewPagerTabListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class ValetFragment extends Fragment implements ViewPagerTabListener {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtBottom)
    TextView txtBottom;


    private Context context;
    private Unbinder unbinder;
    private boolean isClicked;

    public static ValetFragment init() {
        ValetFragment fragment = new ValetFragment();
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
        View view = inflater.inflate(R.layout.valet_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        imageView.setImageResource(R.drawable.housekeeping);
        String pre = "Please grab my car, thanks!";
        txtBottom.setText(pre);
        txtBottom.setTextColor(getResources().getColor(R.color.darker));
        isClicked = false;
    }

    @OnClick(R.id.imageView)
    void imageClick() {
        if (!isClicked) {
            imageView.setImageResource(R.drawable.housekeeping_bold);
            String pre = "Your car will be right up! Please come to valet counter with ";
            String mes = pre + " your name and room number to get your vehicle";
            SpannableString spannableString = new SpannableString(mes);

            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), pre.length(),
                    pre.length() + 10,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), pre.length() + 15,
                    pre.length() + 26,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            txtBottom.setText(spannableString);
            txtBottom.setTextColor(getResources().getColor(R.color.dark));
            isClicked = true;
        }
    }
}
