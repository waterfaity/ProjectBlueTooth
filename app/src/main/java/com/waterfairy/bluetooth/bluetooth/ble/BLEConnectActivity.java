package com.waterfairy.bluetooth.bluetooth.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.bluetooth.R;
import com.waterfairy.bluetooth.utils.NumberChange;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BLEConnectActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {
    private ExpandableListView expandableListView;
    private ExpandAdapter adapter;
    private List<BluetoothGattService> lists;
    public static final String TAG = "ble";
    private CheckBox checkBox;
    private String writeServiceUUID;
    private String readServiceUUID;
    private String readUUID;
    private String writeUUID;
    private String descriptorUUID;
    private byte[] enableType;
    private boolean isConnect;

    private EditText editText;
    private TextView textView;
    private BLEManager bleManager;
    private String address;
    private Button data;
    private MenuItem menuItem;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        editText = (EditText) findViewById(R.id.write_content);
        textView = (TextView) findViewById(R.id.result);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list_view);
        lists = new ArrayList<>();
        adapter = new ExpandAdapter(this, lists);
        address = getIntent().getStringExtra("address");
        bleManager = BLEManager.getInstance();
        expandableListView.setOnChildClickListener(this);
        data = (Button) findViewById(R.id.data);
        registerForContextMenu(data);
        //睡眠uuid
        writeServiceUUID = "0000ffe5-0000-1000-8000-00805f9b34fb";
        readServiceUUID = "";
        readUUID = "0000ffe4-0000-1000-8000-00805f9b34fb";
        writeUUID = "0000ffe9-0000-1000-8000-00805f9b34fb";

        //
        //oumulong  uuid
        editText.setText("00280118");
        writeServiceUUID = "ecbe3980-c9a2-11e1-b1bd-0002a5d5c51b";
        readServiceUUID = "ecbe3980-c9a2-11e1-b1bd-0002a5d5c51b";
        readUUID = "49123040-aee8-11e1-a74d-0002a5d5c51b";
        writeUUID = "b305b680-aee7-11e1-a730-0002a5d5c51b";
        descriptorUUID = "00002902-0000-1000-8000-00805f9b34fb";
        enableType = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
        //
        connect(address);

    }

    private void write(String uuidMain, String writeUUid) {
        String write = editText.getText().toString();

        if (isConnect) {
            if (TextUtils.isEmpty(write)) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] bytes;
            if (checkBox.isChecked()) {
                bytes = NumberChange.hexStringToByte(write);
            } else {
                bytes = write.getBytes();
            }
            bleManager.write(address, bytes);

        } else {
            Toast.makeText(this, "设备未连接", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                write(null, null);
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        BluetoothGattService bluetoothGattService = lists.get(groupPosition);
        UUID uuidMain = bluetoothGattService.getUuid();
        BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristics().get(childPosition);
        UUID uuid = characteristic.getUuid();
//        write(uuidMain.toString(), uuid.toString());
        bleManager.setWriteService(address, uuidMain.toString(), uuid.toString());
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(1, Menu.FIRST + 1, 1, "启动2-08_18");
        menu.add(1, Menu.FIRST + 2, 1, "查询人员-08_4d");
        menu.add(1, Menu.FIRST + 3, 1, "设置人员1-08_ad");
        menu.add(1, Menu.FIRST + 4, 1, "设置人员2-08_29");
        menu.add(1, Menu.FIRST + 5, 1, "未知-10_df");
        menu.add(1, Menu.FIRST + 6, 1, "确认-08_07");
        menu.add(1, Menu.FIRST + 7, 1, "查询数据-08_07");

//        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String content = "";
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                //启动 2
                content = "08:00:00:00:00:10:00:18";
                break;
            case Menu.FIRST + 2:
                //查询
                content = "08:01:00:02:60:26:00:4D";
                break;
            case Menu.FIRST + 3:
                //设置人员1
                content = "08:01:00:02:BA:1C:00:AD";
                break;
            case Menu.FIRST + 4:
                //设置人员2
                content = "08:01:00:06:1E:38:00:29";
                break;
            case Menu.FIRST + 5:
                //未知
                content = "10:01:C0:02:86:08:00:01:00:07:80:00:00:04:00:DF";
                break;
            case Menu.FIRST + 6:
                content = "08:0F:00:00:00:00:00:07";
                break;
            case Menu.FIRST + 7:
                content = "08:01:00:02:D6:0E:00:D3";
                break;


        }

        content = content.replace(":", "");
        editText.setText(content);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManager.disConnect(address);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuItem = menu.add(0, Menu.FIRST, 1, "连接状态");
        menuItem.setIcon(R.drawable.ic_bluetooth_white_24dp);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                if (isConnect) {
                    disconnect();
                    Toast.makeText(this, "正在断开连接...", Toast.LENGTH_SHORT).show();
                } else {
                    connect(address);
                    Toast.makeText(this, "正在连接...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

    private void disconnect() {
        if (isConnect) {
            bleManager.disConnect(address);
        }
    }

    private void connect(String address) {
        bleManager.connect(
                address,
                BLEService.class,
                writeServiceUUID,
                writeUUID,
                readServiceUUID,
                readUUID,
                null,
                enableType,
                new BLEManager.GattDataChangeCallback() {


                    @Override
                    public void onDataRead(byte[] bytes) {
                        String temp = "";
                        for (int i = 0; i < bytes.length; i++) {
                            temp += (bytes[i] + " ");
                        }
                        Log.i(TAG, "onDataRead: " + temp);
                        final String finalTemp = temp;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(finalTemp);
                            }
                        });
                    }

                    @Override
                    public void onDataWrite(byte[] bytes) {

                    }

                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, final int status, final int state) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "onConnectionStateChange: " + status + "---" + state);
                                if (state == BluetoothProfile.STATE_CONNECTED) {
                                    isConnect = true;
                                    menuItem.setIcon(R.drawable.ic_bluetooth_connected_white_24dp);
                                } else {
                                    isConnect = false;
                                    menuItem.setIcon(R.drawable.ic_bluetooth_white_24dp);
                                }
                            }
                        });

                    }

                    @Override
                    public void onDiscoveryService(BluetoothGatt gatt, int characteristic) {
                        List<BluetoothGattService> services = gatt.getServices();
                        lists.removeAll(lists);
                        lists.addAll(services);
                        BLEConnectActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();

                            }
                        });

                    }

                    @Override
                    public void onWriteSuccess() {
                        Toast.makeText(BLEConnectActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onWriteFailed() {
                        Toast.makeText(BLEConnectActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(BLEConnectActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onInitService(BluetoothGatt gatt) {
                        BluetoothGattService service = gatt.getService(UUID.fromString(readServiceUUID));
                        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();

                        for (int i = 5; i < characteristics.size(); i++) {
                            BluetoothGattCharacteristic characteristic = characteristics.get(i);
                            List<BluetoothGattDescriptor> descriptors = characteristic.getDescriptors();
                            if (descriptors != null) {
                                Log.i(TAG, "onInitService descriptors 大小: " + descriptors.size());
                                for (int j = 0; j < descriptors.size(); j++) {
                                    BluetoothGattDescriptor bluetoothGattDescriptor = descriptors.get(j);
                                    String string = bluetoothGattDescriptor.getUuid().toString();
                                    Log.i(TAG, "onInitService: " + i + "--" + j + ":" + string);
                                    bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                                    boolean b = gatt.writeDescriptor(bluetoothGattDescriptor);
                                    boolean bel = gatt.setCharacteristicNotification(characteristic, true);

                                    Log.i(TAG, "onInitService 设置状态: " + bel);
                                }
                            }

                        }
                    }

                });
        expandableListView.setAdapter(adapter);
    }
}
