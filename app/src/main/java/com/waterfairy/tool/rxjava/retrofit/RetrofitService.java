package com.waterfairy.tool.rxjava.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by water_fairy on 2017/2/22.
 */

public interface RetrofitService {
    @GET("app/login")
    Call<UserBean> login(
            @Query("userName") String userName,
            @Query("password") String password);
}
