package com.waterfairy.tool.bluetooth.ble;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.tool.R;
import com.waterfairy.tool.utils.NumberChange;

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

    private String currentMainUUid;
    private String currentChildUUid;

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
        currentMainUUid = uuidMain.toString();
        currentChildUUid = uuid.toString();
        bleManager.setWriteService(address, currentMainUUid, currentChildUUid);
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(1, Menu.FIRST, 1, "发送数据设置");
        //绑定操作
        menu.add(1, Menu.FIRST + 1, 1, "启动1_02");
        menu.add(1, Menu.FIRST + 2, 1, "输入绑定手机识别码");
        menu.add(1, Menu.FIRST + 3, 1, "启动2_08_18");
        menu.add(1, Menu.FIRST + 4, 1, "设置人员1-08_4D");
        menu.add(1, Menu.FIRST + 5, 1, "设置数据绑定后处理_10_4c");
        menu.add(1, Menu.FIRST + 6, 1, "确认-08_07");

        menu.add(1, Menu.FIRST + 7, 1, "输入查询手机识别码");
        menu.add(1, Menu.FIRST + 8, 1, "查询数据");
        menu.add(1, Menu.FIRST + 9, 1, "设置数据查询后处理");


//        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String content = "";
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                //启动1
                content = "0200000000000000000000000000000000";
                break;
            case Menu.FIRST + 2:
                //输入绑定手机识别码
                content = "005A01072337664F3E80656F2BE4F900FB";
                break;
            case Menu.FIRST + 3:
                //启动2
                content = "0800000000100018";
                break;
            case Menu.FIRST + 4:
                //设置人员1
                content = "080100026026004D";
                break;
            case Menu.FIRST + 5:
                //设置人员后处理
                content = "1001C0028608801B800A80008000004C";
                break;
            case Menu.FIRST + 6:
                //确认
                content = "080F000000000007";
                break;
            case Menu.FIRST + 7:
                content = "015A01072337664F3E80656F2BE4F900FB";
                break;
            case Menu.FIRST + 8:
                content = "08010004340E0037";
                break;
            case Menu.FIRST + 9:
                content = "1001C0028608001C800A8000800000CB";
                break;


            case Menu.FIRST:
                BLEService bleService = bleManager.getServiceHashMap().get(address);
                BluetoothGatt bleGatt = bleService.getBleGatt();
                if (bleGatt != null) {
                    if (!TextUtils.isEmpty(currentChildUUid)) {
                        BluetoothGattDescriptor descriptor = new BluetoothGattDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"), BluetoothGattDescriptor.PERMISSION_WRITE);
                        BluetoothGattService characteristicService = bleGatt.getService(UUID.fromString(currentMainUUid));
                        BluetoothGattCharacteristic characteristic = characteristicService.getCharacteristic(UUID.fromString(currentChildUUid));
//                        BluetoothGattDescriptor descriptor1 = characteristic.getDescriptors().get(0);
//                        descriptor.setValue(descriptor1.getValue());
                        characteristic.getDescriptors().remove(0);
                        characteristic.addDescriptor(descriptor);
                        bleGatt.writeCharacteristic(characteristic);
                    }
                }

                ;
                return true;


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
                    public void onDataRead(byte[] bytes, String uuid) {
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
                    public void onWriteSuccess(byte[] writeBytes) {

                    }

                    @Override
                    public void onWriteFailed(byte[] writeBytes) {

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
