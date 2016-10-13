package com.uhotel.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.uhotel.MyApplicationContext;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.config.Constant;
import com.uhotel.fragment.home.LoginFragment;
import com.uhotel.interfaces.OnBackListener;


public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        MyApplicationContext.setEnableUncatchException(this);

        if (MyPreference.getBoolean(Constant.PRE_IS_LOGINED)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, LoginFragment.init(), LoginFragment.class.getName()).commitNow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Utility.hideKeyboard(getCurrentFocus(),this);
        String currentTag = getSupportFragmentManager().findFragmentById(R.id.fragment).getTag();

        if (currentTag.equals(LoginFragment.class.getName())
                ) {
            if (doubleBackToExitPressedOnce) {
                finish();

            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.msg_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(
                    R.id.fragment);
            if (fragment instanceof OnBackListener)
                ((OnBackListener) fragment).onBackPress();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
    }

}
