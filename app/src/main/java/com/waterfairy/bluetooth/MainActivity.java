package com.waterfairy.bluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.waterfairy.bluetooth.bluetooth.BTSearchActivity;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        String content = URLEncoder.encode("哈哈");
        Log.i(TAG, "onCreate: "+content);
        ;

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blueTooth:
                startActivity(new Intent(this, BTSearchActivity.class));
                break;
            case R.id.density:
                startActivity(new Intent(this, DensityActivity.class));
                break;
            case R.id.calc:
                startActivity(new Intent(this, CalcActivity.class));
                break;
        }
    }
}