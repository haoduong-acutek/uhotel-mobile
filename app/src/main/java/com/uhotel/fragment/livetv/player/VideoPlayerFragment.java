package com.uhotel.fragment.livetv.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;
import com.uhotel.R;
import com.uhotel.Utility;
import com.uhotel.activity.VideoLandscapeActivity;
import com.uhotel.dto.TVInfo;
import com.uhotel.fragment.livetv.LiveTVFragment;
import com.uhotel.fragment.livetv.cast.ExpandedControlsActivity;
import com.uhotel.fragment.livetv.commingUp.CommingUpFragment;
import com.uhotel.fragment.livetv.drawer.DrawerContentListener;
import com.uhotel.fragment.livetv.rightNow.RightNowFragment;
import com.uhotel.fragment.other.VideoControlView;
import com.uhotel.fragment.other.VideoControlViewListener;
import com.uhotel.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



/**
 * Created by kiemhao on 8/25/16.
 */
public class VideoPlayerFragment extends Fragment implements OnBackListener,VideoControlViewListener {


    @BindView(R.id.flPlayer)
    RelativeLayout flPlayer;

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    public
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    public
    @BindView(R.id.txtTimeLeft)
    TextView txtTimeLeft;
    public
    @BindView(R.id.txtChannelName)
    TextView txtChannelName;


    protected CastContext mCastContext;
    protected CastSession mCastSession;
    protected SessionManagerListener<CastSession> mSessionManagerListener;
    protected MediaInfo mSelectedMedia;
    private VideoControlView controlView;

    private TVInfo tvInfo;

    protected Context context;
    protected Unbinder unbinder;


    protected long duration;
    protected Handler handler;
    protected String videoURL;
    protected long exoPos;
    protected boolean isValidCall;



    protected BroadcastReceiver broadcastReceiver;
    public static String TV_INFO = "tv info";
    public static String EXO_POS = "process";
    public static String VIDEO_BC_FILTER = "video filfer";


    public static VideoPlayerFragment init(TVInfo tvInfo, int tabIndex, int itemIndex) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("tvInfo", tvInfo);
        bundle.putInt("tabIndex", tabIndex);
        bundle.putInt("itemIndex", itemIndex);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //controlView.currentPos= data.getLongExtra(PlayerFragment.EXO_POS, 0L);
            //controlView.seekTo( controlView.currentPos);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    controlView.setupPlayButton();
                }
            },1500);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvInfo = getArguments().getParcelable("tvInfo");
        videoURL = tvInfo.channelStreams.get(0).src;
        setupCastListener();
//        mCastContext = CastContext.getSharedInstance(context);
//
//        mCastSession = mCastContext.getSessionManager().getCurrentCastSession();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                exoPos = 0;
                tvInfo = intent.getParcelableExtra(TV_INFO);
                try {
                    videoURL = tvInfo.channelStreams.get(0).src;
                } catch (Exception exp) {
                    videoURL = "";
                }
                controlView.changeVideoURL(videoURL);
                loadRemoteMedia(controlView.seekBar.getProgress(), true);
                updateUI();

            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_video_player_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((DrawerContentListener) getParentFragment()).hideTabBar();

        //----broadcast disable slide
        Intent intent = new Intent(LiveTVFragment.LIVE_TV_FILTER);
        intent.putExtra(LiveTVFragment.ENABLE_SLIDE, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        //-------------Exo
        ((DrawerContentListener) getParentFragment()).changeHomeIcon(R.drawable.ico_back);

        updateUI();
        Log.d(this.getClass().getName(), tvInfo.toString());



    }

    private void updateUI() {
        txtTitle.setText(tvInfo.title);
        txtChannelName.setText(tvInfo.channelName);
        txtTimeLeft.setText(tvInfo.getTimeLeftNow());
        txtDesc.setText(tvInfo.description);
    }

    private void setMediaInfo() {
        MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, "Live TV");
        mediaMetadata.addImage(new WebImage(Uri.parse("http://media.karaokecuatui.vn//image//2016//08//25//e//5//e553f25cbebf4c7aaec8618ce2a10fd5_460_230.jpg")));
        mSelectedMedia = new MediaInfo.Builder(
                videoURL)
                .setContentType("video/mp4")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mediaMetadata)
                .build();
    }


    @Override
    public void onResume() {

        if (mCastSession != null && mCastSession.getRemoteMediaClient() != null) {
            exoPos = (int) mCastSession.getRemoteMediaClient().getApproximateStreamPosition();
            isValidCall = true;
//            mPlayerView.mPreviousPosition=exoPos;
//            mPlayerView.preparePlayer();

            controlView.seekTo((int) exoPos);
            mCastSession.getRemoteMediaClient().seek(duration * exoPos / controlView.seekBar.getMax());
        } else {
            setupVideoView();
        }

//        mCastContext.getSessionManager().addSessionManagerListener(
//                mSessionManagerListener, CastSession.class);
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter(VIDEO_BC_FILTER));
        super.onResume();

    }

    private void setupVideoView(){
        controlView=new VideoControlView(context);
        flPlayer.addView(controlView);
        controlView.setupListener(this);
        controlView.seekBar.setEnabled(false);
        controlView.run();
        if(!TextUtils.isEmpty(videoURL)) {
            controlView.setVideoURL(videoURL);

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        controlView.onPause();
        flPlayer.removeView(controlView);
        controlView=null;
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
//        mCastContext.getSessionManager().removeSessionManagerListener(
//                mSessionManagerListener, CastSession.class);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Intent intent = new Intent(LiveTVFragment.LIVE_TV_FILTER);
        intent.putExtra(LiveTVFragment.ENABLE_SLIDE, false);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


        ((DrawerContentListener) getParentFragment()).showTabBar();

        unbinder.unbind();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.live_tv_player, menu);
//        CastButtonFactory.setUpMediaRouteButton(context,
//                menu,
//                R.id.media_route_menu_item);
//        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPress();
                return true;
            case R.id.iteRecord:
                Utility.showMessage(context, "record");
                return true;
        }
        return false;

    }


    @Override
    public void toFullScreen() {
        if (TextUtils.isEmpty(videoURL))
            return;
        Intent intent = new Intent(context, VideoLandscapeActivity.class);
        try {
            intent.putExtra("videoURL", tvInfo.channelStreams.get(0).src);
        } catch (Exception exp) {
            intent.putExtra("videoURL", "");
        }
        intent.putExtra(EXO_POS, 0);
        intent.putExtra("isFromLiveTV",true);
        startActivityForResult(intent, 1);


    }


    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;


                loadRemoteMedia(controlView.seekBar.getProgress() * 1000, true);
//                if (null != mSelectedMedia) {
//
//                    if (mPlaybackState == PlaybackState.PLAYING) {
//                        mVideoView.pause();
//                        loadRemoteMedia(mSeekbar.getProgress(), true);
//                        return;
//                    } else {
//                        mPlaybackState = PlaybackState.IDLE;
//                        updatePlaybackLocation(PlaybackLocation.REMOTE);
//                    }
//                }
//                updatePlayButton(mPlaybackState);
                //getActivity().invalidateOptionsMenu();
                getActivity().supportInvalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {

                //updatePlaybackLocation(PlaybackLocation.LOCAL);
                //mPlaybackState = PlaybackState.IDLE;


            }
        };
    }


    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            return;
        }
        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
        if (remoteMediaClient == null) {
            return;
        }
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                Intent intent = new Intent(context, ExpandedControlsActivity.class);
                startActivity(intent);
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {
            }

            @Override
            public void onQueueStatusUpdated() {
            }

            @Override
            public void onPreloadStatusUpdated() {
            }

            @Override
            public void onSendingRemoteMediaRequest() {
            }
        });
        setMediaInfo();
        remoteMediaClient.load(mSelectedMedia, autoPlay, position);
    }


    @Override
    public void onBackPress() {
        if (getArguments().getInt("tabIndex") == 0)
            Utility.toFragment(this, RightNowFragment.init(getArguments().getInt("itemIndex")), R.id.fragment);
        else
            Utility.toFragment(this, CommingUpFragment.init(getArguments().getInt("itemIndex")), R.id.fragment);

    }

    @Override
    public boolean isPlayCheck() {
        return false;
    }
}
