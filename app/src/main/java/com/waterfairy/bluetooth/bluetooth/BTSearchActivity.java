package com.waterfairy.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.waterfairy.bluetooth.bluetooth.btnormal.BTServerActivity;
import com.waterfairy.bluetooth.utils.PermissionUtils;
import com.waterfairy.bluetooth.R;
import com.waterfairy.bluetooth.bluetooth.ble.BLEConnectActivity;
import com.waterfairy.bluetooth.bluetooth.ble.BLEManager;
import com.waterfairy.bluetooth.bluetooth.btnormal.BTConnectActivity;
import com.waterfairy.bluetooth.bluetooth.btnormal.BTManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BTSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "ble";
    private HashMap<String, BluetoothDevice> hashMap;
    private ListView listView;
    private List<BluetoothDevice> deviceList;
    private SearchAdapter adapter;
    private BLEManager bleManager;
    private BTManager btManager;
    private boolean isBLE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_LOCATION);
        PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_LOCATION);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        initBlueToothManager();
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

    private BluetoothDevice device;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra("address", device.getAddress());
        if (isBLE) {
            intent.setClass(this, BLEConnectActivity.class);
            startActivity(intent);
        } else {
            intent.setClass(this, BTConnectActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio1) {
            isBLE = false;
            search2();
        } else {
            isBLE = true;
            search4();
        }
    }

    private void search2() {
        bleManager.stopSearchBLE();
        hashMap = new HashMap<>();
        deviceList = new ArrayList<>();
        adapter = new SearchAdapter(this, deviceList);
        listView.setAdapter(adapter);

        btManager.searchBT(new BTManager.OnBTDeviceFindListener() {
            @Override
            public void onBTFound(BluetoothDevice device) {
                Log.i(TAG, "onBTFound: 2.0 搜索: " + device.getName() + "--" + device.getAddress() + "--" + device.getType() + "--" + device.getUuids() + "--" + device.getBondState());
                if (!hashMap.containsKey(device.getAddress())) {
                    hashMap.put(device.getAddress(), device);
                    deviceList.add(device);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onSearchFinish() {

            }
        });
    }

    private void search4() {
        btManager.cancelDiscovery();
        hashMap = new HashMap<>();
        deviceList = new ArrayList<>();
        adapter = new SearchAdapter(this, deviceList);
        listView.setAdapter(adapter);

        bleManager.searchBLE(new BLEManager.OnBLEFoundListener() {
            @Override
            public void onBLEFound(BluetoothDevice device) {
                Log.i(TAG, "onBLEFound: 4.0搜索: " + device.getName() + "--" + device.getAddress() + "--" + device.getType() + "--" + device.getUuids() + "--" + device.getBondState());
                if (!hashMap.containsKey(device.getAddress())) {
                    hashMap.put(device.getAddress(), device);
                    deviceList.add(device);
                    adapter.notifyDataSetChanged();
                }

            }
        });

    }

    public void setAsServer(View view) {
        startActivity(new Intent(this, BTServerActivity.class));
    }


}
