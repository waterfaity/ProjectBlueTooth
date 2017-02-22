package com.waterfairy.tool.rxjava.rxjava_retrofit.subscriber;

import android.widget.Toast;

import com.waterfairy.tool.rxjava.rxjava_retrofit.exception.ApiException;

import rx.Observer;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class RxSubscriber<T> extends ErrorSubscriber<T> {


    @Override
    public void onCompleted() {
//        DialogHelper.stopProgressDlg();
    }

    @Override
    protected void onError(ApiException ex) {
//        DialogHelper.stopProgressDlg();
//        Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {

    }
}


abstract class ErrorSubscriber<T> implements Observer<T> {
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, 123));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);
}