package com.waterfairy.tool.bluetooth.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by water_fairy on 2016/11/14.
 */

public class BLEService extends Service {
    private String address;
    private BluetoothAdapter bluetoothAdapter;
    private String ble_service_uuid;
    private String write_service_uuid;
    private String write_uuid;
    private String read_service_uuid;
    private String read_uuid;
    private String descriptor_uuid;
    private byte[] enableType;
    private boolean openService;
    private BLEManager.GattDataChangeCallback dataCallback;
    private BluetoothDevice remoteDevice;
    private BluetoothGatt blueToothGatt;
    private BluetoothGattCharacteristic writeGatt;
    private BluetoothGattCharacteristic readGatt;
    private BLEManager.OnConnectStateCallback connectStateCallback;

    public void setWriteUUid(String write_uuid) {
        this.write_uuid = write_uuid;
    }

    public void setReadUUID(String read_uuid) {
        this.read_uuid = read_uuid;
    }

    public void setServiceUUID(String ble_service_uuid) {
        this.ble_service_uuid = ble_service_uuid;
    }

    public void setDataCallback(BLEManager.GattDataChangeCallback dataCallback) {
        this.dataCallback = dataCallback;
    }

    public void initGatt() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            remoteDevice = bluetoothAdapter.getRemoteDevice(address);
            remoteDevice.connectGatt(this, false, new GattCallback());
        } else {
            Log.i(TAG, "initGatt: you device has no bluetooth");
        }
    }

    public void disconnect() {
        if (blueToothGatt != null) {
            blueToothGatt.disconnect();
            blueToothGatt.close();
        }
    }

    public BluetoothGatt getBleGatt() {
        return blueToothGatt;
    }

    public void write(BluetoothGattCharacteristic writeGatt, byte[] bytes) {
        if (writeGatt != null&&blueToothGatt!=null) {
            writeGatt.setValue(bytes);
            boolean b = blueToothGatt.writeCharacteristic(writeGatt);
            if (b) {
                if (dataCallback != null) {
                    dataCallback.onWriteSuccess(bytes);
                }
            } else {
                if (dataCallback != null) {
                    dataCallback.onWriteFailed(bytes);
                }
            }
        } else {
            if (dataCallback != null) {
                dataCallback.onError("写服务未开启");
            }
        }
    }

    public void write(byte[] bytes) {
        write(writeGatt, bytes);
    }

    public void setReadServiceUUID(String readServiceUUID) {
        this.read_service_uuid = readServiceUUID;
    }

    public void setWriteServiceUUID(String writeServiceUUID) {
        this.write_service_uuid = writeServiceUUID;
    }

    public void setWriteService(String uuidWriteService, String uuidWrite) {
        setWriteServiceUUID(uuidWriteService);
        setWriteUUid(uuidWrite);
        if (blueToothGatt != null) {
            BluetoothGattService service = blueToothGatt.getService(UUID.fromString(uuidWriteService));
            if (service != null && uuidWrite != null) {
                writeGatt = service.getCharacteristic(UUID.fromString(uuidWrite));
            }
        }
    }

    public void setDescriptorUUID(String descriptorUUID) {
        this.descriptor_uuid = descriptorUUID;
    }

    public void setEnableType(byte[] enableType) {
        this.enableType = enableType;
    }

    public void setConnectStateCallback(BLEManager.OnConnectStateCallback connectStateCallback) {
        this.connectStateCallback = connectStateCallback;
    }

    class MyBind extends Binder {
        private String address;

        public MyBind(String address) {
            this.address = address;
        }

        Service getService() {
            return BLEService.this;
        }

        String getAddress() {
            return address;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        address = intent.getStringExtra("address");
        openService = intent.getBooleanExtra("openService", true);
        setServiceUUID(intent.getStringExtra("service_uuid"));
        setWriteUUid(intent.getStringExtra("write_uuid"));
        setReadUUID(intent.getStringExtra("read_uuid"));
        setReadServiceUUID(intent.getStringExtra("read_service_uuid"));
        setWriteServiceUUID(intent.getStringExtra("write_service_uuid"));
        setDescriptorUUID(intent.getStringExtra("descriptor_uuid"));
        setEnableType(intent.getByteArrayExtra("enable_type"));
        return new MyBind(address);
    }

    class GattCallback extends BluetoothGattCallback {
        /**
         * 连接状态改变
         *
         * @param gatt
         * @param status
         * @param newState
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (dataCallback != null) {
                dataCallback.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //连接成功
                    gatt.discoverServices();
                    Log.i(TAG, "initGatt: connect ok, discovery services ");
                    if (connectStateCallback != null) {
                        connectStateCallback.onConnect(true);
                    }
                } else {
                    BLEManager.getInstance().disConnect(address);
                    if (connectStateCallback != null) {
                        connectStateCallback.onConnect(false);
                    }
                }
            }
        }

        /**
         * 数据传输改变
         *
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i(TAG, "onCharacteristicChanged: 有数据变动");
            byte[] value1 = characteristic.getValue();
            String string = "";
            for (int i = 0; i < value1.length; i++) {
                string += ("" + value1[i] + " ");
            }
            Log.i(TAG, "onCharacteristicChanged: " + characteristic.getUuid() + "--" + string);
            dataCallback.onDataRead(value1, characteristic.getUuid().toString());
        }


        /**
         * 服务搜索
         *
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i(TAG, "onServicesDiscovered: service discovery finish");
            blueToothGatt = gatt;

            BluetoothGattService writeService;
            if (!TextUtils.isEmpty(write_service_uuid)) {
                writeService = blueToothGatt.getService(UUID.fromString(write_service_uuid));
                if (writeService != null) {
                    writeGatt = writeService.getCharacteristic(UUID.fromString(write_uuid));
                }
            }
            BluetoothGattService readService;
            if (!TextUtils.isEmpty(read_service_uuid)) {
                readService = blueToothGatt.getService(UUID.fromString(read_service_uuid));
                if (readService != null) {
                    readGatt = readService.getCharacteristic(UUID.fromString(read_uuid));
                }
            }

            dataCallback.onDiscoveryService(gatt, status);
            if (initService(gatt)) {
                dataCallback.onInitService(gatt);
            }
        }


        private boolean initService(BluetoothGatt gatt) {
            if (TextUtils.isEmpty(descriptor_uuid)) {
                return false;
            }
            BluetoothGattService service = gatt.getService(UUID.fromString(read_service_uuid));
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(read_uuid));
            if (characteristic != null) {
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(descriptor_uuid));
                if (descriptor != null) {
                    descriptor.setValue(enableType);
                    boolean belWrite = gatt.writeDescriptor(descriptor);
                    boolean bel = gatt.setCharacteristicNotification(characteristic, true);
                    if (bel) {
                        Log.i(TAG, "initService:  set notify success");
                        return true;
                    } else {
                        Log.i(TAG, "initService:  set notify failed");
                        return false;
                    }
                } else return false;
            } else return false;
        }
    }

    public BluetoothGattCharacteristic getWriteGatt() {
        return writeGatt;
    }

    public BluetoothGattCharacteristic getReadGatt() {
        return readGatt;
    }


}
