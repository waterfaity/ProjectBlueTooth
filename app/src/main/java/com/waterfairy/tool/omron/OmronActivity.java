package com.waterfairy.tool.omron;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.waterfairy.tool.R;
import com.waterfairy.tool.bluetooth.ble.BLEManager;
import com.waterfairy.tool.bluetooth.ble.BLEService;

import java.util.List;
import java.util.UUID;

public class OmronActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omron);

    }


}
