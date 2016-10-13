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

import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastState;
import com.google.android.gms.cast.framework.CastStateListener;
import com.uhotel.MyApplicationContext;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.config.Constant;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.MainFragment;
import com.uhotel.interfaces.OnBackListener;

import io.vov.vitamio.LibsChecker;


public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;

    private CastContext mCastContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        MyApplicationContext.setEnableUncatchException(this);

        if (!LibsChecker.checkVitamioLibs(this))
            return;
        //mCastContext = CastContext.getSharedInstance(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, MainFragment.init(), MainFragment.class.getName()).commitNow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        MyPreference.setBoolean(Constant.PRE_IS_LOGINED, true);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Utility.hideKeyboard(getCurrentFocus(),this);
        String currentTag = getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().findFragmentById(R.id.fragment).getTag();

        if (currentTag.equals(HomeFragment.class.getName())
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
//        mCastContext.addCastStateListener(mCastStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //mCastContext.removeCastStateListener(mCastStateListener);



    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
    }


    private CastStateListener mCastStateListener = new CastStateListener() {
        @Override
        public void onCastStateChanged(int newState) {
            if (newState != CastState.NO_DEVICES_AVAILABLE) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }
        }
    };

//
//    private class MySessionManagerListener implements SessionManagerListener<CastSession> {
//
//        @Override
//        public void onSessionEnded(CastSession session, int error) {
//            if (session == mCastSession) {
//                mCastSession = null;
//            }
//            invalidateOptionsMenu();
//        }
//
//        @Override
//        public void onSessionResumed(CastSession session, boolean wasSuspended) {
//            mCastSession = session;
//            invalidateOptionsMenu();
//        }
//
//        @Override
//        public void onSessionStarted(CastSession session, String sessionId) {
//            mCastSession = session;
//            invalidateOptionsMenu();
//        }
//
//        @Override
//        public void onSessionStarting(CastSession session) {
//        }
//
//        @Override
//        public void onSessionStartFailed(CastSession session, int error) {
//        }
//
//        @Override
//        public void onSessionEnding(CastSession session) {
//        }
//
//        @Override
//        public void onSessionResuming(CastSession session, String sessionId) {
//        }
//
//        @Override
//        public void onSessionResumeFailed(CastSession session, int error) {
//        }
//
//        @Override
//        public void onSessionSuspended(CastSession session, int reason) {
//        }
//    }

}
