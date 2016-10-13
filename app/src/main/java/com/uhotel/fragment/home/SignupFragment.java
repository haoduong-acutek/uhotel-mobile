package com.uhotel.fragment.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.activity.HomeActivity;
import com.uhotel.control.AsteriskPassword;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 8/25/16.
 */
public class SignupFragment extends Fragment implements OnBackListener {
    @BindView(R.id.ediEmail)
    EditText ediEmail;
    @BindView(R.id.ediPassword)
    EditText ediPassword;


    private Context context;
    private Unbinder unbinder;

    public static SignupFragment init() {
        SignupFragment fragment = new SignupFragment();
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
        View view = inflater.inflate(R.layout.signup_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        ediPassword.setTransformationMethod(AsteriskPassword.getInstance());
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

    @OnClick(R.id.btnSignup)
    void signupClick() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.btnToLogin)
    void toLoginClick() {
        Utility.toFragment(this, LoginFragment.init(), R.id.fragment);
    }

    @Override
    public void onBackPress() {

    }
}
