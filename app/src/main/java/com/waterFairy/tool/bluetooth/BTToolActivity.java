package com.waterfairy.tool.bluetooth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.waterfairy.tool.R;
import com.waterfairy.tool.bluetooth.ble.BLEManager;
import com.waterfairy.tool.bluetooth.btnormal.BTManager;
import com.waterfairy.tool.bluetooth.btnormal.BTServerActivity;
import com.waterfairy.tool.utils.PermissionUtils;

public class BTToolActivity extends AppCompatActivity  {
    BLEManager bleManager;
    BTManager btManager;
    private EditText discoverAbleTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bttool);
        //权限设置
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_LOCATION);
        //初始化蓝牙
        initBlueToothManager();
        initView();
    }

    private void initView() {
        discoverAbleTime = (EditText) findViewById(R.id.discoverable_time);
    }

    private void initBlueToothManager() {
        //4.0
        bleManager = BLEManager.getInstance();
        boolean b = bleManager.initBL(this);
        if (b) {

        }
        //2.0
        btManager = BTManager.getInstance();
        boolean b1 = btManager.initBL(this);
        if (b1) {

        }
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search:
                //蓝牙搜索
                startActivity(new Intent(this, BTSearchActivity.class));
                break;
            case R.id.open_server:
                //开启蓝牙服务
                startActivity(new Intent(this, BTServerActivity.class));
                break;
            case R.id.open_discoverable:
                //开启可被搜索

                String timeStr = discoverAbleTime.getText().toString();
                int time = 0;
                time = TextUtils.isEmpty(timeStr) ? 0 : Integer.parseInt(timeStr);
                btManager.setCanDiscovery(this, time);
                break;
        }
    }

}
