package com.waterfairy.tool.rxjava.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.waterfairy.tool.rxjava.retrofit.HttpDef.BASE_URL;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class Http {

    private final static Http HTTP = new Http();
    private Retrofit retrofit;
    private RetrofitService retrofitService;

    private Http() {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static Http getInstance() {
        return HTTP;
    }

    public void login(String userName, String password) {
        retrofitService.login(userName, password).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {

            }
        });
    }
}
