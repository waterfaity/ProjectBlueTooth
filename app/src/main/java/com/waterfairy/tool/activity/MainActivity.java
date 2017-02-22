package com.waterfairy.tool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.waterfairy.tool.R;
import com.waterfairy.tool.bluetooth.BTToolActivity;
import com.waterfairy.tool.databinding.DataBindingActivity;
import com.waterfairy.tool.date.DateActivity;
import com.waterfairy.tool.exception.ExceptionTestActivity;
import com.waterfairy.tool.qr.QRListActivity;
import com.waterfairy.tool.regular.RegularActivity;
import com.waterfairy.tool.rxjava.RXJavaActivity;
import com.waterfairy.tool.update.UpdateActivity;
import com.waterfairy.tool.utils.PermissionUtils;
import com.waterfairy.tool.video.bilibili.VideoActivity;
import com.waterfairy.tool.xml.XMLActivity;

import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_STORAGE);
        test();
    }

    private void test() {
        ((Button) findViewById(R.id.before)).setText(" 新 测试1");
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        linkedBlockingQueue.offer(new byte[]{1});
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blueTooth:
                startActivity(new Intent(this, BTToolActivity.class));
                break;
            case R.id.density:
                startActivity(new Intent(this, DensityActivity.class));
                break;
            case R.id.calc:
                startActivity(new Intent(this, CalcActivity.class));
                break;
            case R.id.date:
                startActivity(new Intent(this, DateActivity.class));
                break;
//            case R.id.qr:
//                startActivity(new Intent(this, QRActivity.class));
//                break;
            case R.id.qr:
                startActivity(new Intent(this, SmaliActivity.class));
                break;
            case R.id.update:
                startActivity(new Intent(this, UpdateActivity.class));
                break;
            case R.id.exception_text:
                startActivity(new Intent(this, ExceptionTestActivity.class));
                break;
            case R.id.text:
                startActivity(new Intent(this, TextActivity.class));
                break;
            case R.id.device_list:
                startActivity(new Intent(this, QRListActivity.class));
                break;
            case R.id.print:
                startActivity(new Intent(this, PrintActivity.class));
                break;
            case R.id.tool:
                startActivity(new Intent(this, ToolActivity.class));
                break;
            case R.id.xml:
                startActivity(new Intent(this, XMLActivity.class));
                break;
            case R.id.reg:
                startActivity(new Intent(this, RegularActivity.class));
                break;
            case R.id.before:
                int i = 1 / 0;
                break;
            case R.id.data_bind:
                startActivity(new Intent(this, DataBindingActivity.class));
                break;
            case R.id.video:
                startActivity(new Intent(this, VideoActivity.class));
                break;
            case R.id.rx_java:
                startActivity(new Intent(this, RXJavaActivity.class));
                break;
        }
    }
}