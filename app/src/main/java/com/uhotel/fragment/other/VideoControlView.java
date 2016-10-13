package com.uhotel.fragment.other;

import android.content.Context;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uhotel.R;
import com.uhotel.Utility;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;


/**
 * Created by michael on 11/27/15.
 */
public class VideoControlView extends RelativeLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {


    public ImageView btnPlay;
    public VideoView videoView;
    private ProgressBar progressBar;
    public SeekBar seekBar;
    private TextView txtFrom;
    private TextView txtTo;
    public ImageView btnFullScreen;
    private TextView txtDownLoadRate;
    private TextView txtLoadRate;


    protected String videoURL;
    private Handler handler;
    private VideoControlViewListener controlViewListener;

    public long currentPos;
    private boolean onResume;

    public VideoControlView(Context context) {

        this(context, null);

    }

    public VideoControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.video_control, this, true);
        btnPlay = (ImageView) v.findViewById(R.id.btnPlay);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        seekBar = (SeekBar) v.findViewById(R.id.seekBar);
        txtFrom = (TextView) v.findViewById(R.id.txtFrom);
        txtTo = (TextView) v.findViewById(R.id.txtTo);
        btnFullScreen = (ImageView) v.findViewById(R.id.btnFullScreen);
        videoView = (VideoView) v.findViewById(R.id.videoView);
        txtDownLoadRate = (TextView) v.findViewById(R.id.txtDownLoadRate);
        txtLoadRate = (TextView) v.findViewById(R.id.txtLoadRate);
        Vitamio.isInitialized(context);


    }


    public void setupListener(VideoControlViewListener controlViewListener){

        this.controlViewListener=controlViewListener;
        btnFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoControlView.this.controlViewListener.toFullScreen();

            }
        });
    }

    public void setVideoURL(String videoURL){
        this.videoURL=videoURL;
    }

    public void run(){

        handler=new Handler();
        videoView.setOnPreparedListener(this);

        videoView.requestFocus();
        videoView.setOnInfoListener(this);
        videoView.setOnBufferingUpdateListener(this);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!isControlBarShown()) {
                        setControlBarVisible(View.VISIBLE);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setControlBarVisible(View.GONE);
                        }
                    }, 4000);
                }
                return false;
            }
        });


        btnPlay.setActivated(true);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(videoURL))
                    return;
                setupPlayButton();
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }

                txtFrom.setText(Utility.durationInSecondsToString(videoView.getCurrentPosition()));
            }
        });
    }

    public void play(){
        videoView.setVideoURI(Uri.parse(videoURL));
        btnPlay.setActivated(false);
        videoView.start();
        //seekBar.postDelayed(onEverySecond,1000);
    }

    private void setupPlayButton() {
        if(controlViewListener.isPlayCheck()){
            ((VideoControlViewMovieListener)controlViewListener).showBuyDialog();
        }
        else {
            btnPlay.setActivated(!btnPlay.isActivated());
            if (btnPlay.isActivated()) {
                videoView.pause();
            } else {
                play();
            }
        }
    }

    private void setControlBarVisible(int visibility) {
        if (seekBar == null)
            return;
        seekBar.setVisibility(visibility);
        btnPlay.setVisibility(visibility);
        txtFrom.setVisibility(visibility);
        txtTo.setVisibility(visibility);
    }

    private boolean isControlBarShown() {
        return seekBar.getVisibility() == View.VISIBLE ? true : false;
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            if (seekBar != null) {
                seekBar.setProgress((int) videoView.getCurrentPosition());


                handler.postDelayed(this, 1000);
            }
        }
    };


    public void seekTo(long position){
        videoView.seekTo( position);
    }

    public void changeVideoURL(String videoURL){
        this.videoURL=videoURL;
        clearSurface(videoView.getHolder().getSurface());
        play();
    }

    public void onPause(){
        videoView.stopPlayback();
        btnPlay.setActivated(true);
        handler.removeCallbacksAndMessages(null);
    }

    public void onResume(){
        setControlBarVisible(View.VISIBLE);

    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setPlaybackSpeed(1.0f);
        seekBar.setMax((int) videoView.getDuration());
        txtTo.setText(Utility.durationInSecondsToString(videoView.getDuration() ));
                if (currentPos != 0)
                    videoView.seekTo(currentPos);

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    progressBar.setVisibility(View.VISIBLE);
                    txtDownLoadRate.setText("");
                    txtLoadRate.setText("");
                    txtDownLoadRate.setVisibility(View.VISIBLE);
                    txtLoadRate.setVisibility(View.VISIBLE);
                    handler.removeCallbacksAndMessages(null);
                    btnPlay.setVisibility(GONE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                videoView.start();
                progressBar.setVisibility(View.GONE);
                txtDownLoadRate.setVisibility(View.GONE);
                txtLoadRate.setVisibility(View.GONE);
                setControlBarVisible(GONE);
                handler.postDelayed(onEverySecond, 1000);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                txtDownLoadRate.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        txtLoadRate.setText(percent + "%");
    }

    private void clearSurface(Surface surface) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        egl.eglInitialize(display, null);

        int[] attribList = {
                EGL10.EGL_RED_SIZE, 8,
                EGL10.EGL_GREEN_SIZE, 8,
                EGL10.EGL_BLUE_SIZE, 8,
                EGL10.EGL_ALPHA_SIZE, 8,
                EGL10.EGL_RENDERABLE_TYPE,EGL10.EGL_WINDOW_BIT,
                EGL10.EGL_NONE, 0,      // placeholder for recordable [@-3]
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(display, attribList, configs, configs.length, numConfigs);
        EGLConfig config = configs[0];
        EGLContext context = egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL10.EGL_NONE
        });
        EGLSurface eglSurface = egl.eglCreateWindowSurface(display, config, surface,
                new int[]{
                        EGL10.EGL_NONE
                });

        egl.eglMakeCurrent(display, eglSurface, eglSurface, context);
        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        egl.eglSwapBuffers(display, eglSurface);
        egl.eglDestroySurface(display, eglSurface);
        egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT);
        egl.eglDestroyContext(display, context);
        egl.eglTerminate(display);
    }
}