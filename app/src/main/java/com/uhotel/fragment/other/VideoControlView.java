package com.uhotel.fragment.other;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.uhotel.R;
import com.uhotel.Utility;


/**
 * Created by michael on 11/27/15.
 */
public class VideoControlView extends RelativeLayout {


    public ImageView btnPlay;

    SimpleExoPlayerView simpleExoPlayerView;
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
    public SimpleExoPlayer player;
    private DefaultHttpDataSourceFactory dataSourceFactory;

    public long currentPos;

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
        simpleExoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.exo_player_view);
        txtDownLoadRate = (TextView) v.findViewById(R.id.txtDownLoadRate);
        txtLoadRate = (TextView) v.findViewById(R.id.txtLoadRate);



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
        initExo();
//        videoView.setOnPreparedListener(this);
//
//        videoView.requestFocus();
//        videoView.setOnInfoListener(this);
//        videoView.setOnBufferingUpdateListener(this);

        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
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
                    player.seekTo(progress);
                }

                txtFrom.setText(Utility.durationInSecondsToString(player.getCurrentPosition()));
            }
        });
    }

    private void initExo() {
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);


        player= ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, new DefaultLoadControl());
        simpleExoPlayerView.setPlayer(player);

        dataSourceFactory = new DefaultHttpDataSourceFactory("exo player");
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {


            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                //CrashActivity.sendMail(error.getClass().getCanonicalName()+"---" +error.getMessage());
                if (error instanceof ExoPlaybackException
                        && error.getCause() instanceof BehindLiveWindowException) {

                } else {

                }

            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }


        });

        simpleExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 100);
                if (visibility == View.VISIBLE) {

                } else {

                }
            }
        });
    }

    private MediaSource buildMediaResouce(Uri uri){
        int type = Utility.inferContentType(uri);
        switch (type){
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, dataSourceFactory, new Handler(), null);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, dataSourceFactory, new DefaultExtractorsFactory(),
                        new Handler(), null);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    public void play(){
        MediaSource videoSource = buildMediaResouce(Uri.parse(videoURL));
        player.prepare(videoSource);
        player.setPlayWhenReady(true);

        btnPlay.setActivated(false);

        //seekBar.postDelayed(onEverySecond,1000);
    }

    private void setupPlayButton() {
        if(controlViewListener.isPlayCheck()){
            ((VideoControlViewMovieListener)controlViewListener).showBuyDialog();
        }
        else {
            btnPlay.setActivated(!btnPlay.isActivated());
            if (btnPlay.isActivated()) {
                player.setPlayWhenReady(false);
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
                seekBar.setProgress((int) player.getCurrentPosition());


                handler.postDelayed(this, 1000);
            }
        }
    };


    public void seekTo(long position){
        player.seekTo( position);
    }

    public void changeVideoURL(String videoURL){
        this.videoURL=videoURL;
        play();
    }

    public void onPause(){
        player.stop();
        btnPlay.setActivated(true);
        handler.removeCallbacksAndMessages(null);
    }

    public void onResume(){
        setControlBarVisible(View.VISIBLE);

    }



}