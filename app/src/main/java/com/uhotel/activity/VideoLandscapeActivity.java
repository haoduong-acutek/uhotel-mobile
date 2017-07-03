

package com.uhotel.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.uhotel.R;
import com.uhotel.fragment.other.VideoControlView;
import com.uhotel.fragment.other.VideoControlViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 9/4/16.
 */
public class VideoLandscapeActivity extends AppCompatActivity implements VideoControlViewListener {

    @BindView(R.id.flPlayer)
    RelativeLayout flPlayer;
    private VideoControlView controlView;

    private Unbinder unbinder;
    private String videoURL;
    public static String EXO_POS = "process";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_landscape_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }


        unbinder = ButterKnife.bind(this);

        videoURL=  getIntent().getStringExtra("videoURL");
        controlView=new VideoControlView(this);
        flPlayer.addView(controlView);
        controlView.setupListener(this);
        controlView.setVideoURL(videoURL);
        controlView.currentPos=getIntent().getLongExtra(EXO_POS,0L);
        if(getIntent().getBooleanExtra("isFromLiveTV",false))
            controlView.seekBar.setEnabled(false);
        controlView.run();

    }

    @Override
    public void onBackPressed() {

        toFullScreen();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

    }

    @Override
    protected void onPause() {
        super.onPause();

        controlView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        controlView.onResume();
        controlView.setupPlayButton();
    }



    @Override
    public boolean isPlayCheck() {
        return false;
    }

    @Override
    public void toFullScreen() {
        Intent intent = new Intent();
        intent.putExtra(EXO_POS, controlView.videoView.getCurrentPosition());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}



