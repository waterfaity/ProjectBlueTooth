package com.waterfairy.tool.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.waterfairy.tool.utils.ToastUtils;

/**
 * Created by water_fairy on 2017/2/17.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.initToast(this);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        // 调试时，将第三个参数改为true
        Bugly.init(this, "ca73aa3ba3", true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }
}
