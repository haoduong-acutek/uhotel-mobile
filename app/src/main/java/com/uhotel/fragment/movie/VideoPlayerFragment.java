package com.uhotel.fragment.movie;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.activity.VideoLandscapeActivity;
import com.uhotel.config.Config;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.DetailsInfo;
import com.uhotel.dto.MyJsonString;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.VodInfo;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.fragment.livetv.LiveTVFragment;
import com.uhotel.fragment.other.VideoControlView;
import com.uhotel.fragment.other.VideoControlViewMovieListener;
import com.uhotel.interfaces.OnBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kiemhao on 8/25/16.
 */
public class VideoPlayerFragment extends Fragment implements  OnBackListener ,VideoControlViewMovieListener {

    @BindView(R.id.flPlayer)
    RelativeLayout flPlayer;


    public
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    public
    @BindView(R.id.txtDesc)
    TextView txtDesc;
    public
    @BindView(R.id.txtTimeLeft)
    TextView txtTimeLeft;
    @BindView(R.id.txtDirector)
    TextView txtDirector;
    @BindView(R.id.txtStar)
    TextView txtStar;
    @BindView(R.id.txtGenne)
    TextView txtGenne;

    private VodInfo vodInfo;
    protected Context context;
    protected Unbinder unbinder;
    protected long duration;

    protected String videoURL;
    private boolean isPurchased;
    public static String EXO_POS = "process";
    private VideoControlView controlView;
    private long currentPos;


    public static VideoPlayerFragment init(VodInfo vodInfo, boolean isPurchased) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPurchased", isPurchased);
        bundle.putParcelable("vodInfo", vodInfo);
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
        setHasOptionsMenu(true);
        isPurchased = getArguments().getBoolean("isPurchased");
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        vodInfo = getArguments().getParcelable("vodInfo");


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_video_player_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((ToolbarListener) getParentFragment()).changeNavIcon(R.drawable.ico_back);

        //----broadcast disable slide
        Intent intent = new Intent(LiveTVFragment.LIVE_TV_FILTER);
        intent.putExtra(LiveTVFragment.ENABLE_SLIDE, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        updateUI();

        if (TextUtils.isEmpty(videoURL))
            getAllMediaInfo();



    }

    private void updateUI() {
        txtTitle.setText(vodInfo.details.title);
        txtTimeLeft.setText(vodInfo.details.getFormatDuration());
        txtDesc.setText(vodInfo.details.description);
        txtDirector.setText(vodInfo.details.director);
        txtStar.setText(vodInfo.details.actors);
        String genner="";
        for (DetailsInfo.Genre genre: vodInfo.details.genres){
            genner=genner+","+genre.name;
        }
        if(!TextUtils.isEmpty(genner))
            genner=genner.substring(1);
        txtGenne.setText(genner);

    }


    @Override
    public void onResume() {
        super.onResume();
        setupVideoView();

    }

    private void setupVideoView(){
        controlView=new VideoControlView(context);
        flPlayer.addView(controlView);
        controlView.setupListener(VideoPlayerFragment.this);
        controlView.run();
        if(!TextUtils.isEmpty(videoURL)) {
            controlView.setVideoURL(videoURL);
            controlView.currentPos=currentPos;

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPos=controlView.videoView.getCurrentPosition();
        controlView.onPause();
        flPlayer.removeView(controlView);
        controlView=null;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //controlView.onDestroyView();
        unbinder.unbind();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = new Intent(LiveTVFragment.LIVE_TV_FILTER);
        intent.putExtra(LiveTVFragment.ENABLE_SLIDE, false);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.live_tv_player, menu);
        CastButtonFactory.setUpMediaRouteButton(context,
                menu,
                R.id.media_route_menu_item);
        //super.onCreateOptionsMenu(menu, inflater);

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
    public boolean isPlayCheck() {

        return !isPurchased;
    }

    @Override
    public void toFullScreen() {
        if(TextUtils.isEmpty(videoURL) || !isPurchased)
            return;
        Intent intent = new Intent(context, VideoLandscapeActivity.class);
        try {
            intent.putExtra("videoURL", videoURL);
        } catch (Exception exp) {
            intent.putExtra("videoURL", "");
        }
        intent.putExtra(EXO_POS, controlView.videoView.getCurrentPosition());
        intent.putExtra("isFromLiveTV",false);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RentDialogFragment.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getBooleanExtra("isRent", false)) {
                    isPurchased = true;
                    controlView.play();
                }
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {

                currentPos= data.getLongExtra(VideoPlayerFragment.EXO_POS, 0L);

            }
        }


    }

    private void getAllMediaInfo() {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonString<String>> call = taskService.getRootUrl(Utility.getMacAddess(),
                "/restapi/rest/" + profileInfo.regionId + "/content/media?include_media_resources=true");
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();

        call.enqueue(new Callback<MyJsonString<String>>() {

            @Override
            public void onResponse(Call<MyJsonString<String>> call, Response<MyJsonString<String>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    try {
                        String strResult = response.body().result;

                        String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(res);
                            Type listType = new TypeToken<List<com.uhotel.dto.MediaInfo>>() {
                            }.getType();
                            Gson gson = new Gson();
                            List<com.uhotel.dto.MediaInfo> list = gson.fromJson(
                                    new JsonParser().parse(jsonObject.getString("list"))
                                            .getAsJsonArray(), listType);
                            videoURL = findById(vodInfo.purchaseId + "", list).mediaResources.get(0).src;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            videoURL = "";
                        }
                        controlView.setVideoURL(videoURL);
                        Log.d(VideoPlayerFragment.this.getClass().getName(), videoURL);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        videoURL = "";
                        Utility.showMessage(context, "invalid url");
                    }
                } else Utility.showMessage(context, response.message());

            }

            @Override
            public void onFailure(Call<MyJsonString<String>> call, Throwable t) {
                simpleProgressDialog.dismiss();
                t.printStackTrace();
            }
        });

    }


    private com.uhotel.dto.MediaInfo findById(String id, List<com.uhotel.dto.MediaInfo> mediaInfoList) {
        for (com.uhotel.dto.MediaInfo mediaInfo : mediaInfoList) {
            if (id.equals(mediaInfo.id))
                return mediaInfo;
        }
        return null;
    }


    @Override
    public void onBackPress() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(this).commitNow();
        ((ToolbarListener) getParentFragment()).changeNavIcon(R.drawable.ico_menu);
    }


    @Override
    public void showBuyDialog() {
        RentDialogFragment rentDialogFragment =  RentDialogFragment.init(vodInfo);
        rentDialogFragment.setTargetFragment(this, RentDialogFragment.REQUEST_CODE);
        rentDialogFragment.show(getFragmentManager(), RentDialogFragment.class.getName());
    }
}
