package com.waterfairy.tool.bluetooth.btnormal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.tool.R;

public class BTServerActivity extends AppCompatActivity {

    private BTManager btManager;
    private EditText editText;
    private TextView receive, state;
    private boolean isConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btserver);
        btManager = BTManager.getInstance();
        editText = (EditText) findViewById(R.id.edit);
        receive = (TextView) findViewById(R.id.receive);
        state = (TextView) findViewById(R.id.state);
    }

    public void send(View view) {
        if (isConnect) {
            String string = editText.getText().toString();
            if (TextUtils.isEmpty(string)) {
                Toast.makeText(this, "请输入发送内容", Toast.LENGTH_SHORT).show();
            } else {
                btManager.writeMsgFromServer(string.getBytes());
            }
        } else {
            Toast.makeText(this, "蓝牙未连接", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear(View view) {
        receive.setText("");
    }

    public void operateServer2(View view) {
        btManager.setAsServer(new BTManager.OnConnectListener() {
            @Override
            public void onSuccess() {
                isConnect = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        state.setText("连接成功");
                    }
                });
            }

            @Override
            public void onWaitingConnectTo() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        state.setText("等待设备连接");
                    }
                });
            }

            @Override
            public void onConnecting() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        state.setText("设备连接中...");
                    }
                });
            }

            @Override
            public void onRead(final byte[] bytes, final int len) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        receive.setText(receive.getText().toString() + new String(bytes,0, len) + "\n");
                    }
                });
            }

            @Override
            public void onWrite(byte[] bytes) {

            }

            @Override
            public void onFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        state.setText("服务器开启失败");
                    }
                });
            }

            @Override
            public void onDisconnect() {
                isConnect = false;
            }
        });
    }

}
