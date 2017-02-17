package com.waterfairy.tool.bluetooth.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by water_fairy on 2016/11/14.
 */

public class BLEManager {
    private final static BLEManager BLE_MANAGER = new BLEManager();
    private static final String TAG = "bleManager";
    private OnBLEFoundListener onBLEFoundListener;
    private BluetoothManager bluetoothManager;
    private HashMap<String, ServiceConnection> mServiceConnectionHashMap;
    private HashMap<String, BLEService> mServiceHashMap;
    private HashMap<String, Boolean> mBLEConnectStateHashMap;


    private BluetoothAdapter adapter;
    private Context context;

    private BLEManager() {

    }

    public BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    /**
     * 1 获取bleManager
     *
     * @return
     */
    public static BLEManager getInstance() {
        return BLE_MANAGER;
    }

    /**
     * 2.初始化 查看是否支持蓝牙
     *
     * @param context
     * @return
     */
    public boolean initBL(Context context) {
        this.context = context.getApplicationContext();
        bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            try {
                throw new Exception("your device has no bluetooth hardware");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        } else {
            openBL();
            return true;
        }
    }

    /**
     * 打开蓝牙
     *
     * @return
     */
    public boolean openBL() {
        if (adapter != null) {
            if (!adapter.isEnabled()) {
                adapter.enable();
                if (adapter.isEnabled()) {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            try {
                throw new Exception("you have not init bluetoothAdapter or your device has no bluetooth hardware");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 3.蓝牙搜索
     *
     * @param onBLEFoundListener
     */
    public void searchBLE(final OnBLEFoundListener onBLEFoundListener) {
        this.onBLEFoundListener = onBLEFoundListener;
        if (adapter != null) {
            adapter.startLeScan(leScanCallback);
        }
    }

    /**
     * 停止蓝牙搜索
     */
    public void stopSearchBLE() {
        if (adapter == null) {
            return;
        }
        adapter.stopLeScan(leScanCallback);
    }

    public void connect(String address, Class aClass, GattDataChangeCallback callback) {
        connect(address, aClass, null, null, null, null, null, null, callback);
    }

    /**
     * 4.蓝牙连接
     *
     * @param address          地址
     * @param aClass           服务
     * @param writeServiceUUID 写服务
     * @param writeUUID        写
     * @param readServiceUUID  读服务
     * @param readUUID         读
     */
    public void connect(String address,
                        Class aClass,
                        String writeServiceUUID,
                        String writeUUID,
                        String readServiceUUID,
                        String readUUID,
                        String descriptorUUID,
                        byte[] enableType,
                        GattDataChangeCallback callback) {

        Boolean connect = false;
        if (mBLEConnectStateHashMap != null) {
            connect = mBLEConnectStateHashMap.get(address);
        }
        if (!connect) {
            disConnect(address);
            if (mServiceConnectionHashMap == null) {
                mServiceConnectionHashMap = new HashMap<>();
            }
            if (mBLEConnectStateHashMap == null) {
                mBLEConnectStateHashMap = new HashMap<>();
            }
            mBLEConnectStateHashMap.put(address, false);
            ServiceConnection tempServiceConnection = mServiceConnectionHashMap.get(address);
            if (tempServiceConnection == null) {
                tempServiceConnection = getServiceConnection(callback);
                mServiceConnectionHashMap.put(address, tempServiceConnection);
                Intent intent = new Intent(context, aClass);
                intent.putExtra("address", address);
                intent.putExtra("write_uuid", writeUUID);
                intent.putExtra("write_service_uuid", writeServiceUUID);
                intent.putExtra("read_uuid", readUUID);
                intent.putExtra("read_service_uuid", readServiceUUID);
                intent.putExtra("descriptor_uuid", descriptorUUID);
                intent.putExtra("enable_type", enableType);
                context.bindService(intent, tempServiceConnection, Context.BIND_AUTO_CREATE);
            }
        } else {
            Log.i(TAG, "connect: 已连接:" + address);
        }
    }

    public void disConnect(String address) {
        try {
            if (mServiceConnectionHashMap != null)
                context.unbindService(mServiceConnectionHashMap.get(address));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        if (mServiceHashMap != null) {
            BLEService bleService = mServiceHashMap.get(address);
            if (bleService != null) {
                bleService.disconnect();
                bleService = null;
                mServiceHashMap.remove(address);
            }
        }
        if (mServiceConnectionHashMap != null) {
            ServiceConnection serviceConnection = mServiceConnectionHashMap.get(address);
            mServiceConnectionHashMap.remove(address);
            serviceConnection = null;
        }

    }

    public BluetoothGattServer setServer(BluetoothDevice device) {
        BluetoothGattServer bluetoothGattServer = bluetoothManager.openGattServer(context, new BluetoothGattServerCallback() {
            @Override
            public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
                super.onConnectionStateChange(device, status, newState);
                Log.i(TAG, "onConnectionStateChange: ");
            }

            @Override
            public void onServiceAdded(int status, BluetoothGattService service) {
                super.onServiceAdded(status, service);
                Log.i(TAG, "onServiceAdded: ");
            }

            @Override
            public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
                Log.i(TAG, "onCharacteristicReadRequest: ");
            }

            @Override
            public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
                Log.i(TAG, "onCharacteristicWriteRequest: ");
            }

            @Override
            public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
                super.onDescriptorReadRequest(device, requestId, offset, descriptor);
                Log.i(TAG, "onDescriptorReadRequest: ");
            }

            @Override
            public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
                super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
                Log.i(TAG, "onDescriptorWriteRequest: ");
            }

            @Override
            public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
                super.onExecuteWrite(device, requestId, execute);
                Log.i(TAG, "onExecuteWrite: ");
            }

            @Override
            public void onNotificationSent(BluetoothDevice device, int status) {
                super.onNotificationSent(device, status);
            }

            @Override
            public void onMtuChanged(BluetoothDevice device, int mtu) {
                Log.i(TAG, "onMtuChanged: ");
                super.onMtuChanged(device, mtu);
            }
        });
        BluetoothGattService bluetoothGattService = new BluetoothGattService(UUID.fromString("00001124-0000-1000-8000-00805F9B34FB"), BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic characteristic = new BluetoothGattCharacteristic(UUID.fromString("00001125-0000-1000-8000-00805F9B34FB"), BluetoothProfile.STATE_DISCONNECTED, 1);
        bluetoothGattService.addCharacteristic(characteristic);
        bluetoothGattServer.addService(bluetoothGattService);
        bluetoothGattServer.connect(device, false);
        return bluetoothGattServer;
    }

    /**
     * 蓝牙写入
     *
     * @return
     */
    public void write(String address, byte[] bytes) {
        BLEService bleService = mServiceHashMap.get(address);
        if (bleService != null) {
            bleService.write(bytes);
        }
    }

    /**
     * 蓝牙写入
     *
     * @return
     */
    public void write(String address, BluetoothGattCharacteristic writeGatt, byte[] bytes) {
        BLEService bleService = mServiceHashMap.get(address);
        if (bleService != null) {
            bleService.write(writeGatt, bytes);
        }
    }

    /**
     * 蓝牙写入
     *
     * @return
     */
    public void setWriteService(String address, String uuidWriteService, String uuidWrite) {
        BLEService bleService = mServiceHashMap.get(address);
        if (bleService != null) {
            bleService.setWriteService(uuidWriteService, uuidWrite);
        }
    }

    /**
     * 获取服务
     *
     * @param address
     * @return
     */
    public BLEService getService(String address) {
        if (mServiceHashMap != null) return mServiceHashMap.get(address);
        return null;
    }

    private ServiceConnection getServiceConnection(final GattDataChangeCallback callback) {

        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i(TAG, "onServiceConnected: service connected success");
                BLEService.MyBind myBind = (BLEService.MyBind) service;
                final String address = myBind.getAddress();
                BLEService bindService = (BLEService) myBind.getService();
                if (mServiceHashMap == null) {
                    mServiceHashMap = new HashMap<>();
                }
                mServiceHashMap.put(address, bindService);
                bindService.setDataCallback(callback);
                bindService.setConnectStateCallback(new OnConnectStateCallback() {
                    @Override
                    public void onConnect(boolean isConnect) {
                        mBLEConnectStateHashMap.put(address, isConnect);
                    }
                });
                bindService.initGatt();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i(TAG, "onServiceConnected: service disconnected");
            }
        };
    }

    /**
     * 蓝牙搜索回调
     */
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            if (onBLEFoundListener != null) {
                onBLEFoundListener.onBLEFound(bluetoothDevice);
            }
        }
    };


    /**
     * 蓝牙搜索接口
     */
    public interface OnBLEFoundListener {
        void onBLEFound(BluetoothDevice device);
    }

    public interface GattDataChangeCallback {
        /**
         * 连接状态改变
         *
         * @param gatt
         * @param status
         * @param state
         */
        void onConnectionStateChange(BluetoothGatt gatt, int status, int state);

        /**
         * 发现服务
         *
         * @param gatt
         * @param status
         */
        void onDiscoveryService(BluetoothGatt gatt, int status);

        /**
         * 服务设置(服务提醒Notification)
         *
         * @param gatt
         */
        void onInitService(BluetoothGatt gatt);

        /**
         * 数据读取监听
         *
         * @param bytes
         * @param uuid
         */
        void onDataRead(byte[] bytes, String uuid);

        /**
         * 写入成功
         *
         * @param writeBytes
         */
        void onWriteSuccess(byte[] writeBytes);

        /**
         * 写入失败
         *
         * @param writeBytes
         */
        void onWriteFailed(byte[] writeBytes);

        /**
         * 错误
         *
         * @param msg
         */
        void onError(String msg);


    }

    public interface OnConnectStateCallback {
        void onConnect(boolean isConnect);
    }

    public HashMap<String, ServiceConnection> getServiceConnectionHashMap() {
        return mServiceConnectionHashMap;
    }


    public HashMap<String, BLEService> getServiceHashMap() {
        return mServiceHashMap;
    }

}
