package com.waterfairy.tool;

import android.app.Application;

import com.waterfairy.tool.utils.ToastUtils;

/**
 * Created by water_fairy on 2016/12/5.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.initToast(this);
    }
}
