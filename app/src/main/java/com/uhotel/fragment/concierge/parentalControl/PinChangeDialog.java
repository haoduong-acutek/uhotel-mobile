package com.uhotel.fragment.concierge.parentalControl;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.config.Config;
import com.uhotel.control.AsteriskPassword;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.MyJsonObject;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.helper.UIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kiemhao on 8/31/16.
 */
public class PinChangeDialog extends DialogFragment {


    @BindView(R.id.ediPin1)
    EditText ediPin1;
    @BindView(R.id.ediPin2)
    EditText ediPin2;
    @BindView(R.id.ediPin3)
    EditText ediPin3;
    @BindView(R.id.ediPin4)
    EditText ediPin4;
    @BindView(R.id.ediRePin1)
    EditText ediRePin1;
    @BindView(R.id.ediRePin2)
    EditText ediRePin2;
    @BindView(R.id.ediRePin3)
    EditText ediRePin3;
    @BindView(R.id.ediRePin4)
    EditText ediRePin4;


    private Context context;
    private boolean isEditControl;
    public static String IS_EDIT_CONTROL = "isEditControl";

    public static PinChangeDialog init(String currentPIN) {
        PinChangeDialog pinDialogFragment = new PinChangeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("currentPIN", currentPIN);
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
        View view = inflater.inflate(R.layout.parental_pin_change_dialog, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ediPin1.setTransformationMethod(new AsteriskPassword());
        ediPin2.setTransformationMethod(new AsteriskPassword());
        ediPin3.setTransformationMethod(new AsteriskPassword());
        ediPin4.setTransformationMethod(new AsteriskPassword());
        ediRePin1.setTransformationMethod(new AsteriskPassword());
        ediRePin2.setTransformationMethod(new AsteriskPassword());
        ediRePin3.setTransformationMethod(new AsteriskPassword());
        ediRePin4.setTransformationMethod(new AsteriskPassword());

        ediPin1.addTextChangedListener(new MyTextWatcher(ediPin1));
        ediPin2.addTextChangedListener(new MyTextWatcher(ediPin2));
        ediPin3.addTextChangedListener(new MyTextWatcher(ediPin3));
        ediPin4.addTextChangedListener(new MyTextWatcher(ediPin4));
        ediRePin1.addTextChangedListener(new MyTextWatcher(ediRePin1));
        ediRePin2.addTextChangedListener(new MyTextWatcher(ediRePin2));
        ediRePin3.addTextChangedListener(new MyTextWatcher(ediRePin3));


        isEditControl = getArguments().getBoolean(IS_EDIT_CONTROL);


    }

    @OnClick(R.id.btnClose)
    void closeClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnOk)
    void okClick() {
        Utility.hideKeyboard(getView(),context);
        String pin = UIHelper.getStringFromEditText(ediPin1) + UIHelper.getStringFromEditText(ediPin2)
                + UIHelper.getStringFromEditText(ediPin3) + UIHelper.getStringFromEditText(ediPin4);
        String rePin = UIHelper.getStringFromEditText(ediRePin1) + UIHelper.getStringFromEditText(ediRePin2)
                + UIHelper.getStringFromEditText(ediRePin3) + UIHelper.getStringFromEditText(ediRePin4);

        if (!pin.equals(rePin))
            return;

        changePIN(getArguments().getString("currentPIN"), pin);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void changePIN(final String currentPIN, final String newPIN) {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonObject<Boolean>> call = taskService.changePIN(Utility.getMacAddess(), profileInfo.profileUid, newPIN, currentPIN);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<MyJsonObject<Boolean>>() {

            @Override
            public void onResponse(Call<MyJsonObject<Boolean>> call, Response<MyJsonObject<Boolean>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    MyJsonObject responseInfo = response.body();
                    Boolean isCorrect = (Boolean) responseInfo.result;

                    Log.d(PinChangeDialog.this.getClass().getName(), isCorrect.toString());
                    Intent intent = new Intent();
                    intent.putExtra("isCorrect", isCorrect);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    if (isCorrect)
                        dismiss();
                    else Utility.showMessage(context, "Change PIN failed");
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<MyJsonObject<Boolean>> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context, "Change PIN failed");
                Log.d(PinChangeDialog.this.getClass().getName(), "error" + t.getMessage());
            }
        });

    }


    class MyTextWatcher implements TextWatcher {
        EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (editText.getText().toString().length() == 1) {
                switch (editText.getId()) {
                    case R.id.ediPin1:
                        ediPin2.requestFocus();
                        break;
                    case R.id.ediPin2:
                        ediPin3.requestFocus();
                        break;
                    case R.id.ediPin3:
                        ediPin4.requestFocus();
                        break;
                    case R.id.ediPin4:
                        ediRePin1.requestFocus();
                        break;
                    case R.id.ediRePin1:
                        ediRePin2.requestFocus();
                        break;
                    case R.id.ediRePin2:
                        ediRePin3.requestFocus();
                        break;
                    case R.id.ediRePin3:
                        ediRePin4.requestFocus();
                        break;
                    default:

                        break;
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
