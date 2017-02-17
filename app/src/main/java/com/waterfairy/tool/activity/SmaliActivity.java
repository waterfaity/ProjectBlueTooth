package com.waterfairy.tool.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.waterfairy.tool.R;

public class SmaliActivity extends AppCompatActivity {

    private DisplayMetrics displayMetrics;
    private int testHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smali);


    }

    private int getSystemHeight(int width) {
        int height = 0;
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        height = height;
        testHeight = height;
        return height;
    }

    private int getTest() {
        if (displayMetrics != null) {
            return displayMetrics.densityDpi;
        }
        return 100;
    }


}
