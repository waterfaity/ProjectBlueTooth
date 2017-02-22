package com.waterfairy.tool.rxjava.rxjava_retrofit.func;

import com.waterfairy.tool.rxjava.rxjava_retrofit.exception.ExceptionEngine;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by water_fairy on 2017/2/22.
 */

public class HttpResponseFunc<T> implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
