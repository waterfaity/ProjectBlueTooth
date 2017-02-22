package com.waterfairy.tool.rxjava.rxjava_retrofit;

import android.util.Log;

import com.waterfairy.tool.rxjava.rxjava_retrofit.exception.ApiException;
import com.waterfairy.tool.rxjava.rxjava_retrofit.func.HttpResponseFunc;
import com.waterfairy.tool.rxjava.rxjava_retrofit.manger.BodyManager;
import com.waterfairy.tool.rxjava.rxjava_retrofit.manger.HeaderManager;
import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.BaseResponseObject;
import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.UpdateBean;
import com.waterfairy.tool.rxjava.rxjava_retrofit.bean.UserBean;
import com.waterfairy.tool.rxjava.rxjava_retrofit.subscriber.RxSubscriber;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.waterfairy.tool.rxjava.retrofit.HttpDef.BASE_URL;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class Http {
    private final String TAG = "http";
    private final static Http HTTP = new Http();
    private Retrofit retrofit;
    private RxJava_RetrofitService retrofitService;

    private Http() {
        retrofit = new Retrofit.Builder().
                addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        retrofitService = retrofit.create(RxJava_RetrofitService.class);
    }

    public static Http getInstance() {
        return HTTP;
    }

    public void loginNoToken(String userName, String password) {
        retrofitService
                .loginNoToken(userName, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<BaseResponseObject<UserBean>>() {
                    @Override
                    public void call(BaseResponseObject<UserBean> userBeanBaseResponseObject) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResponseObject<UserBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseResponseObject<UserBean> userBeanBaseResponseObject) {
                        UserBean userBean = userBeanBaseResponseObject.data;
                    }
                });
    }

    public void login(String userName, String password) {
        retrofitService
                .login(userName, password)
                .flatMap(new Func1<String, Observable<UserBean>>() {
                    @Override
                    public Observable<UserBean> call(String token) {
                        return retrofitService.getUserInfo(token);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<UserBean>() {
                    @Override
                    public void call(UserBean userBean) {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<UserBean>() {
                    @Override
                    public void onNext(UserBean userBean) {
                    }

                    @Override
                    protected void onError(ApiException ex) {
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    public void update(String id, String version) {
        retrofitService
                .update(id, version)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<UpdateBean>() {
                    @Override
                    public void call(UpdateBean updateBean) {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResponseFunc<UpdateBean>())
                .subscribe(new RxSubscriber<UpdateBean>() {
                    @Override
                    public void onNext(UpdateBean userBean) {
                        Log.i(TAG, "onNext: " + userBean.isUpdate());
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        Log.i(TAG, "onNext: " + ex.code);
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    public void postMultipart() {
        RequestBody jsonBody1 = BodyManager.createJsonBody("{\"body\":1}");
        RequestBody jsonBody2 = BodyManager.createJsonBody("{\"body\":2}");
        Headers header = HeaderManager.createHeader("title");
        MultipartBody multipartBody = new MultipartBody.Builder()
                .addPart(jsonBody1)
                .addPart(header, jsonBody2)
                .build();

        retrofitService.postMultipartBody(multipartBody);

        List<MultipartBody.Part> list = new ArrayList<>();
        MultipartBody.Part part = MultipartBody.Part.create(HeaderManager.createHeader("title"), jsonBody1);
        list.add(part);
        retrofitService.postMultipart(list);
    }
}
