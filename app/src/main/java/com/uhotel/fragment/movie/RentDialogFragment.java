package com.uhotel.fragment.movie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.dto.VodInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kiemhao on 8/31/16.
 */
public class RentDialogFragment extends DialogFragment {
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtPrice)
    TextView txtPrice;

    public static int REQUEST_CODE = 2;

    private Context context;

    public static RentDialogFragment init(VodInfo vodInfo) {
        RentDialogFragment pinDialogFragment = new RentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("vodInfo",vodInfo);
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
        View view = inflater.inflate(R.layout.rent_cancel_dialog_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VodInfo vodInfo=getArguments().getParcelable("vodInfo");
        txtTitle.setText("Watch '"+vodInfo.details.title+"'");
        if(vodInfo.price!=null)
        txtPrice.setText("($"+vodInfo.price.value+")");


    }

    @OnClick(R.id.btnClose)
    void closeClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnRent)
    void rentClick() {
        Intent intent = new Intent();
        intent.putExtra("isRent", true);
        getTargetFragment().onActivityResult(REQUEST_CODE, Activity.RESULT_OK, intent);
        this.dismiss();
    }

    @OnClick(R.id.btnCancel)
    void cancelClick() {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

}
