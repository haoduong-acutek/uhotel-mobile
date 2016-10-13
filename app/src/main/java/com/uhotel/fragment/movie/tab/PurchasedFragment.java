package com.uhotel.fragment.movie.tab;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.uhotel.MyPreference;
import com.uhotel.R;
import com.uhotel.RetrofitService;
import com.uhotel.ServiceGenerator;
import com.uhotel.Utility;
import com.uhotel.config.Config;
import com.uhotel.control.GridSpacingItemDecoration;
import com.uhotel.dto.MediaInfo;
import com.uhotel.dto.MyJsonString;
import com.uhotel.dto.ProfileInfo;
import com.uhotel.dto.PurchaseInfo;
import com.uhotel.dto.VodInfo;
import com.uhotel.fragment.listener.ViewPagerDestroyListener;
import com.uhotel.fragment.listener.ViewPagerTabListener;
import com.uhotel.fragment.movie.VideoPlayerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;

//import com.uhotel.fragment.movie.player.PlayerFragment;

/**
 * Created by kiemhao on 8/25/16.
 */
public class PurchasedFragment extends Fragment implements ViewPagerTabListener, ViewPagerDestroyListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context context;
    private Unbinder unbinder;
    private int regionId;

    public static PurchasedFragment init() {
        PurchasedFragment fragment = new PurchasedFragment();
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
        View view = inflater.inflate(R.layout.purchased_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

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

    @Override
    public void loadOnSelect() {
        List<VodInfo> listPurchase=MyPreference.getListObject("listPurchase",VodInfo.class);
        if(listPurchase==null)
            new LoadPurchaseTabTask().execute();
        else setupAdapter(listPurchase);

    }

    @Override
    public void destroyAll() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }


    class PurchasedAdapter extends RecyclerView.Adapter<PurchasedAdapter.MyViewHolder> {
        private List<VodInfo> list;


        public PurchasedAdapter(List<VodInfo> list) {

            this.list = list;

        }

        public void setList(List<VodInfo> list) {
            this.list = list;
        }

        // other methods
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchased_item, viewGroup, false));
        }


        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, final int pos) {
            final VodInfo item = list.get(pos);
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    ft.add(R.id.fragment, VideoPlayerFragment.init(item,true), VideoPlayerFragment.class.getName()).commitNow();


                }
            });


            Glide.with(PurchasedFragment.this).load(String.format(Config.IMAGE_PREFIX_URL,regionId)+ item.details.poster)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imageView);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public
            @BindView(R.id.imageView)
            ImageView imageView;

            public MyViewHolder(View itemView) {

                super(itemView);
                ButterKnife.bind(this, itemView);

            }

        }
    }

    class LoadPurchaseTabTask extends AsyncTask<Void, Void, Void> {
//        SimpleProgressDialog simpleProgressDialog;

        List<VodInfo> purchaseInfoList;
        @Override
        protected Void doInBackground(Void... para) {
            RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
            ProfileInfo profileInfo = (ProfileInfo) MyPreference.getObject("userInfo", ProfileInfo.class);
            regionId=profileInfo.regionId;
            Call<MyJsonString<String>> call = taskService.getRootUrl(Utility.getMacAddess(),
                    "/restapi/rest/"+profileInfo.languageId+"/"+profileInfo.userId+"/store/purchased-products");

            try {
                String strResult = call.execute().body().result;
                String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";
                JSONObject jsonObject = new JSONObject(res);
                Type listType = new TypeToken<List<PurchaseInfo>>() {
                }.getType();
                Gson gson = new Gson();
                List<PurchaseInfo> masterInfoList= gson.fromJson(
                        new JsonParser().parse(jsonObject.getString("list"))
                                .getAsJsonArray(), listType);

                String paraIds="";
                for (PurchaseInfo.PurchaseItem purchaseItem:masterInfoList.get(0).purchaseItems){
                    paraIds=paraIds+purchaseItem.id+",";
                }
                purchaseInfoList=getListPurchaseInfo(paraIds.substring(0,paraIds.length()-1));
                MyPreference.setListObject("listPurchase",purchaseInfoList);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        private MediaInfo getVodInfo(String id, List<MediaInfo> vodInfoList){
            for (MediaInfo vodInfo:vodInfoList){
                if(id.equals(vodInfo.id))
                    return vodInfo;

            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            simpleProgressDialog = new SimpleProgressDialog(context);
//            simpleProgressDialog.showBox();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(purchaseInfoList!=null)
            setupAdapter(purchaseInfoList);


        }
    }

    private List<VodInfo> getListPurchaseInfo(String ids) {
        RetrofitService taskService = ServiceGenerator.createService(RetrofitService.class);
        Call<MyJsonString<String>> call = taskService.getRootUrl(Utility.getMacAddess(),
                "/vod/info?purchase_item_list="+ids);
        try {
            String strResult = call.execute().body().result;

//            String res = "{" + strResult.substring(1, strResult.length() - 1) + "}";
//

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


    private void setupAdapter(List<VodInfo> list){
        GridLayoutManager linearLayoutManager = new GridLayoutManager(context, 2);
        PurchasedAdapter featureAdapter = new PurchasedAdapter(list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(featureAdapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, (int) Utility.convertDPtoPIXEL(TypedValue.COMPLEX_UNIT_DIP, 10), true));
    }



}
