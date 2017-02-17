package com.waterfairy.tool.omron;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import com.waterfairy.tool.bluetooth.ble.BLEManager;
import com.waterfairy.tool.bluetooth.ble.BLEService;
import com.waterfairy.tool.utils.NumberChange;

import java.util.List;
import java.util.UUID;

/**
 * Created by water_fairy on 2016/12/23.
 */

public class OmronDeviceManager {


    public static final int ACTIVATE_DEVICE = 1;//启动设备
    public static final int SEND_DEVICE = 2;//发送链接设备

    public static final int ACTIVATE_COMMAND = 3;//开启设备指令接收
    public static final int SEND_HUMAN = 4;//发送人员1
    public static final int SEND_HUMAN_HANDLE = 5;//发送人员处理
    public static final int ENSURE = 6;//确认

    public static final int SEND_DEVICE_QUERY = 7;//发送查询设备
    public static final int QUERY_HANDLE = 8;//发送链接设备后处理


    private static final String TAG = "omron";
    private String address;

    final String writeServiceUUID = "ecbe3980-c9a2-11e1-b1bd-0002a5d5c51b";
    final String readServiceUUID = "ecbe3980-c9a2-11e1-b1bd-0002a5d5c51b";
    final String descriptorUUID = "00002902-0000-1000-8000-00805f9b34fb";
    final byte[] enableType = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
    final BLEManager bleManager = BLEManager.getInstance();


    final String writeUUID1 = "b305b680-aee7-11e1-a730-0002a5d5c51b";
    final String writeUUID2 = "db5b55e0-aee7-11e1-965e-0002a5d5c51b";

    final String readUUID1 = "49123040-aee8-11e1-a74d-0002a5d5c51b";
    final String readUUID2 = "4d0bf320-aee8-11e1-a0d9-0002a5d5c51b";
    final String readUUID3 = "5128ce60-aee8-11e1-b84b-0002a5d5c51b";


    private static final OmronDeviceManager OMRON_DEVICE_MANAGER = new OmronDeviceManager();

    private OmronDeviceManager() {

    }


    public static OmronDeviceManager getInstance() {
        return OMRON_DEVICE_MANAGER;
    }

    public void connect(String address) {
        this.address = address;
        bleManager.connect(address, BLEService.class, writeServiceUUID, null, readServiceUUID, null, descriptorUUID, enableType,
                new BLEManager.GattDataChangeCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int state) {
                        if (state == BluetoothProfile.STATE_CONNECTED) {
                            Log.i(TAG, "onConnectionStateChange: 连接成功");
                            write(getCommand(ACTIVATE_DEVICE));

                        } else {
                            Log.i(TAG, "onConnectionStateChange: 连接断开");
                        }
                    }

                    @Override
                    public void onDiscoveryService(BluetoothGatt gatt, int status) {

                    }

                    @Override
                    public void onInitService(BluetoothGatt gatt) {
                        //初始化服务
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

                    @Override
                    public void onDataRead(byte[] bytes, String uuid) {


                    }

                    @Override
                    public void onWriteFailed(byte[] writeBytes) {

                    }

                    @Override
                    public void onWriteSuccess(byte[] writeBytes) {

                    }

                    @Override
                    public void onError(String msg) {

                    }
                });


    }

    private void write(byte[] command) {

        
    }


    public byte[] getCommand(int command) {
        String content = "";
        switch (command) {
            case ACTIVATE_DEVICE:
                //启动1
                content = "0200000000000000000000000000000000";
                break;
            case SEND_DEVICE:
                //输入绑定手机识别码
                content = "005A01072337664F3E80656F2BE4F900FB";
                break;
            case ACTIVATE_COMMAND:
                //启动2
                content = "0800000000100018";
                break;
            case SEND_HUMAN:
                //设置人员1
                content = "080100026026004D";
                break;
            case SEND_HUMAN_HANDLE:
                //设置人员后处理
                content = "1001C0028608801B800A80008000004C";
                break;
            case ENSURE:
                //确认
                content = "080F000000000007";
                break;
            case SEND_DEVICE_QUERY:
                content = "015A01072337664F3E80656F2BE4F900FB";
                break;
            case QUERY_HANDLE:
                content = "1001C0028608001C800A8000800000CB";
                break;
        }
        content = content.replace(":", "");
        byte[] bytes = NumberChange.hexStringToByte(content);
        return bytes;
    }
}
