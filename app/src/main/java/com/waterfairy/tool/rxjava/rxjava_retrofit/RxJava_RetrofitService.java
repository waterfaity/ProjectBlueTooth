package com.waterfairy.tool.rxjava.rxjava_retrofit;

import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.BaseResponseObject;
import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.UpdateBean;
import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.UserBean;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by water_fairy on 2017/2/22.
 */

public interface RxJava_RetrofitService {
    @GET("app/user/login")
    Observable<BaseResponseObject<UserBean>> loginNoToken(
            @Query("userName") String userName,
            @Query("password") String password
    );

    @GET("app/login")
    Observable<String> login(
            @Query("userName") String userName,
            @Query("password") String password
    );

    @GET("app/getUserInfo")
    Observable<UserBean> getUserInfo(
            @Query("token") String token
    );

    @GET("version")
    Observable<UpdateBean> update(
            @Query("id") String id,
            @Query("version") String version
    );




    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("app/name")
    @Multipart()
    Observable<UserBean> postJson(
            @Query("json") String json
    );

    @Multipart
    @POST("app/anme")
    Observable<UserBean> postMultipart(@Part List<MultipartBody.Part> parts);

    @POST("app/name")
    Observable<UserBean> postMultipartBody(@Body MultipartBody body);

}
