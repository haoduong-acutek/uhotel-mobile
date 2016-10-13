package com.uhotel.fragment.livetv.commingUp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.uhotel.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kiemhao on 8/31/16.
 */
public class ConfirmDialogFragment extends DialogFragment {


    @BindView(R.id.btnClose)
    ImageView btnClose;
    @BindView(R.id.btnRecord)
    ViewGroup btnRecord;
    @BindView(R.id.btnReminder)
    ViewGroup btnReminder;

    private Context context;

    public static ConfirmDialogFragment init() {
        ConfirmDialogFragment pinDialogFragment = new ConfirmDialogFragment();
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
        View view = inflater.inflate(R.layout.commingup_dialog_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @OnClick(R.id.btnClose)
    void closeClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnRecord)
    void recordClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnReminder)
    void reminderClick() {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

}
