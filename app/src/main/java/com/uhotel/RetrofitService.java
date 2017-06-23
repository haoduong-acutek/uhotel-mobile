package com.uhotel;


import com.uhotel.dto.MyJsonObject;
import com.uhotel.dto.MyJsonString;
import com.uhotel.dto.ProfileInfo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {


    @POST("authorize/Beesmart/AuthorizeService/{mac}/{pin}?region=default&operator=default")
    Call<MyJsonObject<ProfileInfo>> login(@Path("mac") String mac, @Path("pin") String pin);

    @POST("check/Beesmart/PinCodeService/{mac}/{profileId}/authorize")
    Call<MyJsonObject<Boolean>> verifyPIN(@Path("mac") String mac, @Path("profileId") String profileId,
                                          @Query("parentalPin") String pin);

    @POST("check/Beesmart/PinCodeService/{mac}/{profileId}/changepin")
    Call<MyJsonObject<Boolean>> changePIN(@Path("mac") String mac, @Path("profileId") String profileId,
                                          @Query("data") String currentPIN,
                                          @Query("parentalPin") String newPIN);

    @POST("check/Beesmart/PinCodeService/{mac}/{profileId}/update")
    Call<MyJsonObject<Boolean>> udpateParentControl(@Path("mac") String mac, @Path("profileId") String profileId,
                                                    @Query("data") String data,
                                                    @Query("parentalPin") String pin);

    //Live TV
    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getChannel(@Path("mac") String mac,
                                          @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getChannelProgram(@Path("mac") String mac,
                                                 @Query("path") String regionPath);

    //Movie
    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getAllCats(@Path("mac") String mac,
                                          @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getAllPurchasedItem(@Path("mac") String mac,
                                       @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getVodsOnCatId(@Path("mac") String mac,
                                                   @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getVodsOnListCatId(@Path("mac") String mac,
                                              @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getAllVod(@Path("mac") String mac,
                                              @Query("path") String regionPath);

    @GET("request/{mac}/Beesmart")
    Call<MyJsonString<String>> getRootUrl(@Path("mac") String mac,
                                         @Query("path") String regionPath);



}