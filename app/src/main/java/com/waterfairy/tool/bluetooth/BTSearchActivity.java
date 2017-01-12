package com.waterfairy.tool.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.waterfairy.tool.qr.CreateHealthQRActivity;
import com.waterfairy.tool.R;
import com.waterfairy.tool.bluetooth.ble.BLEConnectActivity;
import com.waterfairy.tool.bluetooth.ble.BLEManager;
import com.waterfairy.tool.bluetooth.btnormal.BTConnectActivity;
import com.waterfairy.tool.bluetooth.btnormal.BTManager;
import com.waterfairy.tool.utils.ToastUtils;

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

        bleManager = BLEManager.getInstance();
        btManager = BTManager.getInstance();
        listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        registerForContextMenu(listView);
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


    public void setCanDiscovery(View view) {
//        btManager.setCanDiscovery(BTManager.CAN_DISCOVERY_ALWAYS);
        btManager.setCanDiscovery(this, 20);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == BTManager.REQUEST_DISCOVERABLE) {
                ToastUtils.show(R.string.user_ensure_bt_discoveryable);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == BTManager.REQUEST_DISCOVERABLE) {
                ToastUtils.show(R.string.user_cancel_bt_discoveryable);
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, Menu.FIRST, 1, "生成二维码");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        BluetoothDevice device = deviceList.get(position);
        String address = device.getAddress();
        String name = device.getName();
        switch (item.getItemId()) {
            case Menu.FIRST:
                Intent intentQE = new Intent(this, CreateHealthQRActivity.class);
                intentQE.putExtra("name", name);
                intentQE.putExtra("address", address);
                startActivity(intentQE);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
