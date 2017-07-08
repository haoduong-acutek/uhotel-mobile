package com.uhotel.fragment.other;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.uhotel.MyApplicationContext;
import com.uhotel.R;
import com.uhotel.Utility;


/**
 * Created by michael on 11/27/15.
 */
public class VideoControlView extends RelativeLayout {



    SimpleExoPlayerView simpleExoPlayerView;
    public ImageView btnFullScreen;


    ImageView imaPlay;

    ImageView imaPause;


    protected String videoURL;

    private VideoControlViewListener controlViewListener;
    public SimpleExoPlayer player;
    private DefaultHttpDataSourceFactory dataSourceFactory;

    public long currentPos;
    private boolean isTV;

    public VideoControlView(Context context,boolean isTV) {

        this(context, null);
        this.isTV=isTV;

    }

    public VideoControlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoControlView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View v = LayoutInflater.from(context).inflate(R.layout.video_control, this, true);

        imaPlay=(ImageView) v.findViewById(R.id.exo_play);
        imaPause=(ImageView) v.findViewById(R.id.exo_pause);

        btnFullScreen = (ImageView) v.findViewById(R.id.btnFullScreen);
        simpleExoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.exo_player_view);

        imaPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(controlViewListener.isPlayCheck())
                    ((VideoControlViewMovieListener)controlViewListener).showBuyDialog();
                else run();
            }
        });
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
        currentPos=0;
        initExo();

    }

    public void run(){
        if(isTV)
            playTV();
        else playMovie();




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
                hideButton();
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                hideButton();
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                hideButton();
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                //CrashActivity.sendMail(error.getClass().getCanonicalName()+"---" +error.getMessage());
                if (error instanceof ExoPlaybackException
                        && error.getCause() instanceof BehindLiveWindowException) {
                    currentPos=player.getCurrentPosition();
                    runExo();
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
                        if (player != null)
                            hideButton();
                    }
                }, 100);
                if (visibility == View.VISIBLE) {
                    if (player.getPlayWhenReady()) {
                        imaPause.setSelected(true);
                        imaPause.requestFocus();
                    } else {
                        imaPlay.setSelected(true);
                        imaPlay.requestFocus();
                    }

                } else {

                }
            }
        });
        simpleExoPlayerView.setFastForwardIncrementMs(0);
        simpleExoPlayerView.setRewindIncrementMs(0);
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


    public void changeVideoURL(String videoURL){
        this.videoURL=videoURL;
        run();
    }

    public void onPause(){
        player.stop();

    }

    public void onResume(){


    }

    private void hideButton() {
        if(isTV)
            hideControlButtonInTV();
        else hideControlButtonInMovie();

    }

    private void hideControlButtonInTV() {

    }

    private void hideControlButtonInMovie() {


    }

    private void runExo() {
        try {

            if (player != null) {
                player.setPlayWhenReady(false);
                player.stop();

            }

            if (!TextUtils.isEmpty(videoURL)){
                MediaSource videoSource = buildMediaResouce(Uri.parse(videoURL));
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
                if(currentPos!=0)
                    player.seekTo(currentPos);

            }else {
                Toast.makeText(MyApplicationContext.getInstance(),"Invalid link",Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playTV(){

        runExo();
    }

    private void playMovie(){
        runExo();
    }


}