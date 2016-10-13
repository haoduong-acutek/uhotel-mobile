package com.uhotel.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.activity.HomeActivity;
import com.uhotel.config.Config;
import com.uhotel.control.AsteriskPassword;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.MyJsonObject;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.helper.UIHelper;
import com.uhotel.interfaces.OnBackListener;

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
public class LoginFragment extends Fragment implements OnBackListener {

    @BindView(R.id.ediPIN)
    EditText ediPIN;


    private Context context;
    private Unbinder unbinder;

    public static LoginFragment init() {
        LoginFragment fragment = new LoginFragment();
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
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        ediPIN.setTransformationMethod(AsteriskPassword.getInstance());
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
    public void onBackPress() {
        Utility.toFragment(this, SignupFragment.init(), R.id.fragment);
    }


    @OnClick(R.id.btnLogin)
    void loginClick() {
        if (TextUtils.isEmpty(UIHelper.getStringFromEditText(ediPIN)))
            return;
        Utility.hideKeyboard(getView(),context);
        login(UIHelper.getStringFromEditText(ediPIN));
    }


    private void login(String PIN) {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);

        Call<MyJsonObject<ProfileInfo>> call = taskService.login(Utility.getMacAddess(), PIN);
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<MyJsonObject<ProfileInfo>>() {

            @Override
            public void onResponse(Call<MyJsonObject<ProfileInfo>> call, Response<MyJsonObject<ProfileInfo>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    MyJsonObject responseInfo = response.body();
                    ProfileInfo userInfo = (ProfileInfo) responseInfo.result;
                    MyPreference.setObject("userInfo", userInfo);
                    Log.d(LoginFragment.this.getClass().getName(), userInfo.toString());
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
                } else Utility.showMessage(context, "invalid PIN");
            }

            @Override
            public void onFailure(Call<MyJsonObject<ProfileInfo>> call, Throwable t) {
                simpleProgressDialog.dismiss();
                Utility.showMessage(context, "invalid PIN");
                Log.d(LoginFragment.this.getClass().getName(), "error" + t.getMessage());
            }
        });

    }
}
