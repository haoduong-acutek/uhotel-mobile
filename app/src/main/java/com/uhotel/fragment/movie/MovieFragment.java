package com.uhotel.fragment.movie;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.cast.MediaInfo;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.control.SimpleProgressDialog;
import com.uhotel.dto.CatInfo;
import com.uhotel.dto.MovieTabInfo;
import com.uhotel.dto.MyJsonString;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.PurchaseInfo;
import com.uhotel.dto.VodInfo;
import com.uhotel.fragment.HomeFragment;
import com.uhotel.fragment.listener.ToolbarListener;
import com.uhotel.interfaces.OnBackListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
public class MovieFragment extends Fragment implements OnBackListener, ToolbarListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtHeaderName)
    TextView txtHeaderName;
    @BindView(R.id.imaHeaderIcon)
    ImageView imaHeaderIcon;

    private Context context;
    private Unbinder unbinder;

    public static MovieFragment init() {
        MovieFragment fragment = new MovieFragment();
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
        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ico_menu);


        imaHeaderIcon.setImageResource(R.drawable.video_clip);
        txtHeaderName.setText("Movies");


        //Utility.toChildFragment(MovieFragment.this, MovieTabFragment.init(), R.id.fragment);
        new LoadCatTask().execute();
    }


    @Override
    public void onBackPress() {
        ((OnBackListener) getChildFragmentManager().findFragmentById(R.id.fragment)).onBackPress();
    }


    @Override
    public void changeNavIcon(int resId) {
        toolbar.setNavigationIcon(resId);
    }


    @Override
    public void changeNavTitle(String text) {


    }

    @Override
    public void openMenu() {

    }

    @Override
    public void toHomeFragment() {
        Utility.toFragment(this, HomeFragment.init(), R.id.fragment);
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

    class LoadCatTask extends AsyncTask<Void, Void, Void> {
        SimpleProgressDialog simpleProgressDialog;

        private List<CatInfo> catInfoList=new ArrayList<>();
        @Override
        protected Void doInBackground(Void... para) {
            List<MovieTabInfo> listTab=new ArrayList<>();
            listTab.add(new MovieTabInfo("0","Purchased Movies"));
            listTab.add(new MovieTabInfo("1","Featured"));

            RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
            ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
            Call<MyJsonString<String>> call = taskService.getAllCats(Utility.getMacAddess(), "/restapi/rest/"+profileInfo.languageId+"/store/categories");

            try {
                String strResult = call.execute().body().result;
                String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";
                JSONObject jsonObject = new JSONObject(res);
                Type listType = new TypeToken<List<CatInfo>>() {
                }.getType();
                Gson gson = new Gson();
                catInfoList = gson.fromJson(
                        new JsonParser().parse(jsonObject.getString("list"))
                                .getAsJsonArray(), listType);

                for (CatInfo catInfo:catInfoList){
                    listTab.add(new MovieTabInfo(catInfo.id,catInfo.title));
                    catInfo.purchasedInfo=getVodsOnCatId(catInfo.id).get(0);
                    String paraIds="";
                    for (PurchaseInfo.PurchaseItem purchaseItem :catInfo.purchasedInfo.purchaseItems){
                        paraIds=paraIds+purchaseItem.id+",";
                    }
                    catInfo.listVod= getListVodInfo(paraIds.substring(0,paraIds.length()-1));
                }

                MyPreference.setListObject("listTab",listTab);
                MyPreference.setListObject("listCat",catInfoList);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            simpleProgressDialog = new SimpleProgressDialog(context);
            simpleProgressDialog.showBox();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            simpleProgressDialog.dismiss();

            Utility.toChildFragment(MovieFragment.this, MovieTabFragment.init(), R.id.fragment);
//            if(catInfoList.size()>0)
//                ((MovieTabListener)getChildFragmentManager().findFragmentById(R.id.fragment)).onLoadLiveDone(catInfoList);

        }
    }

    private List<PurchaseInfo> getVodsOnCatId(String catId) {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonString<String>> call = taskService.getVodsOnCatId(Utility.getMacAddess(),
                "/restapi/rest/"+profileInfo.languageId+"/store/products?purchase_category_id="+catId);
        try {
            String strResult = call.execute().body().result;

            String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";

            JSONObject jsonObject = new JSONObject(res);
            Type listType = new TypeToken<List<PurchaseInfo>>() {
            }.getType();
            Gson gson = new Gson();
            List<PurchaseInfo> list = gson.fromJson(
                    new JsonParser().parse(jsonObject.getString("list"))
                            .getAsJsonArray(), listType);
            return list;


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;



    }

    private List<VodInfo> getListVodInfo(String ids) {
        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        Call<MyJsonString<String>> call = taskService.getRootUrl(Utility.getMacAddess(),
                "/vod/info?purchase_item_list="+ids);
        try {
            String strResult = call.execute().body().result;
            Type listType = new TypeToken<List<VodInfo>>() {
            }.getType();
            Gson gson = new Gson();
            List<VodInfo> list = gson.fromJson(
                    new JsonParser().parse(strResult)
                            .getAsJsonArray(), listType);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    private void getAllMediaInfo() {

        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
        Call<MyJsonString<String>> call = taskService.getRootUrl(Utility.getMacAddess(),
                "/restapi/rest/"+profileInfo.regionId+"/content/media?include_media_resources=true");
        final SimpleProgressDialog simpleProgressDialog = new SimpleProgressDialog(context);
        simpleProgressDialog.showBox();
        call.enqueue(new Callback<MyJsonString<String>>() {

            @Override
            public void onResponse(Call<MyJsonString<String>> call, Response<MyJsonString<String>> response) {
                simpleProgressDialog.dismiss();
                if (response.isSuccessful()) {
                    String strResult = response.body().result;
                    Type listType = new TypeToken<List<MediaInfo>>() {
                    }.getType();
                    Gson gson = new Gson();
                    List<MediaInfo> list = gson.fromJson(
                            new JsonParser().parse(strResult)
                                    .getAsJsonArray(), listType);
                    MyPreference.setListObject("listAllMedia",list);



                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<MyJsonString<String>> call, Throwable t) {
                simpleProgressDialog.dismiss();

                t.printStackTrace();
            }
        });

    }


}
