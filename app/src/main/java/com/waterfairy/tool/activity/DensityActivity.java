package com.waterfairy.tool.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.waterfairy.tool.R;

public class DensityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_density);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int heightPixels = displayMetrics.heightPixels;
        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;
        int widthPixels = displayMetrics.widthPixels;
        float scaledDensity = displayMetrics.scaledDensity;

        ApplicationInfo applicationInfo = getApplication().getApplicationInfo();
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String device = Build.DEVICE;
        String model = Build.MODEL;
        String cpuAbi = Build.CPU_ABI;
        String cpuAbi2 = Build.CPU_ABI2;
        String sdk = Build.VERSION.SDK;
        int sdkInt = Build.VERSION.SDK_INT;
        String release = Build.VERSION.RELEASE;


        String info =
                "version\t:" + packageInfo.versionName + "\n" +
                        "widthPixels\t:" + widthPixels + "\n" +
                        "heightPixels\t:" + heightPixels + "\n" +
                        "density\t:" + density + "\n" +
                        "densityDpi\t:" + densityDpi + "\n" +
                        "scaledDensity\t:" + scaledDensity + "\n" +
                        "device\t:" + device + "\n" +
                        "model\t:" + model + "\n" +
                        "cpuAbi\t:" + cpuAbi + "\n" +
                        "cpuAbi2\t:" + cpuAbi2 + "\n" +
                        "sdk\t:" + sdk + "\n" +
                        "release\t:" + release + "\n" +
                        "sdkInt\t:" + sdkInt + "\n";

        ((TextView) findViewById(R.id.text)).setText(info);
    }
}
