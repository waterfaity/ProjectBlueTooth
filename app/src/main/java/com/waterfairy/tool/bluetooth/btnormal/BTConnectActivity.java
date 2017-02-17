package com.waterfairy.tool.bluetooth.btnormal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.NumberChange;

import java.util.Arrays;

public class BTConnectActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "btConnect";
    private TextView connectState, connectState1, connect, receive;
    private boolean isContent;
    private BTManager btManager;
    private CheckBox checkBox;
    private EditText editText;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btconnect);
        editText = (EditText) findViewById(R.id.edit);
        {
//            editText.setText("AA BB 06 00 00 00 01 06 03 04");
        }
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        receive = (TextView) findViewById(R.id.receive);
        connectState = (TextView) findViewById(R.id.connect_state);
        connectState1 = (TextView) findViewById(R.id.connect_state1);
        connect = (TextView) findViewById(R.id.connect);
        connect.setOnClickListener(this);
        address = getIntent().getStringExtra("address");
        btManager = BTManager.getInstance();
        connect(address, 1);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BTManager.getInstance().close();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                String content = editText.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    if (isContent) {
                        boolean checked = checkBox.isChecked();
                        byte[] bytes;
                        if (checked) {
                            bytes = getOxBytes(content);
                        } else {
                            bytes = content.getBytes();
                        }
                        btManager.writeMsgFromUser(bytes);
                    } else {
                        Toast.makeText(this, "设备未连接", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.clear:
                receive.setText("");
                break;
            case R.id.connect:
                connect(address, 2);
                break;
        }

    }

    private void connect(String address, final int tag) {
        btManager.setAsUser(btManager.getBTAdapter().getRemoteDevice(address), new BTManager.OnConnectListener() {


            @Override
            public void onSuccess() {
                isContent = true;
                Log.i(TAG, "onSuccess: ");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tag == 1) {

                            connectState.setText("success");
                        } else {
                            connectState1.setText("success");
                        }
                    }
                });

            }

            @Override
            public void onWaitingConnectTo() {

            }

            @Override
            public void onConnecting() {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tag == 1) {
                            connectState.setText("onConnecting");
                        } else {
                            connectState1.setText("onConnecting");
                        }

                    }
                });

            }

            @Override
            public void onRead(byte[] bytes, int len) {
                byte[] bytes1 = Arrays.copyOf(bytes, len);
                String temp = "";
                Log.i(TAG, "onReadLen: " + bytes1.length);
                for (int i = 0; i < bytes1.length; i++) {
                    Log.i(TAG, "onRead: " + bytes1[i]);
                    temp += " " + bytes1[i];
                }
                final String finalTemp = temp;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        receive.setText(receive.getText().toString() + "\n" + finalTemp + "\n");
                    }
                });

            }

            @Override
            public void onWrite(byte[] bytes) {

            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onDisconnect() {
                isContent = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (tag == 1) {
                            connectState.setText("onDisconnect");
                        } else {
                            connectState1.setText("onDisconnect");
                        }
                    }
                });
            }
        });
    }

    private byte[] getOxBytes(String content) {
        content = content.replace(" ", "");
        return NumberChange.hexStringToByte(content);
    }

    {
//        ecbe3980-c9a2-11e1-b1bd-0002a5d5c51b

//        b305b680-aee7-11e1-a730-0002a5d5c51b
//        db5b55e0-aee7-11e1-965e-0002a5d5c51b
//        e0b8a060-aee7-11e1-92f4-0002a5d5c51b
//        0ae12b00-aee8-11e1-a192-0002a5d5c51b
//        10e1ba60-aee8-11e1-89e5-0002a5d5c51b

//        49123040-aee8-11e1-a74d-0002a5d5c51b
//        4d0bf320-aee8-11e1-a0d9-0002a5d5c51b
//        5128ce60-aee8-11e1-b84b-0002a5d5c51b
//
//
        //
    }

}
