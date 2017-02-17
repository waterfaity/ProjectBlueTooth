package com.waterfairy.tool.bluetooth.btnormal;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by water_fairy on 2016/9/20.
 * 本版 存在蓝牙陪对问题, 需要手动配对,
 * 蓝牙4.0  pin码自动生成,不能再使用setPin(), 所以不能自动配对, 方法暂未找到
 * 网络有setPasskey(int passkey) 未实现
 */
public class BTManager {
    private static final String TAG = "btManager";
    //finial

    /**
     * 常规可被搜索
     */
    public static final int CAN_DISCOVERY_NORMAL = 1;
    /**
     * 指定时间
     */
    public static final int CAN_DISCOVERY_TIME = 2;
    /**
     * 一直可被搜索
     */
    public static final int CAN_DISCOVERY_ALWAYS = 3;

    //param
    private static BTManager btManager;
    private UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");//服务器uuid
    private String serverName = "blueToothServer";//服务器名字
    private BluetoothAdapter bTAdapter;//蓝牙适配器
    private Context context;
    private OnBTDeviceFindListener onBTDeviceFindListener;//设备搜索监听

    //蓝牙服务器
    private BluetoothServerSocket serverSocket;
    private ServerConnectThread serverConnectThread;
    private OnConnectListener serverConnectListener;


    //蓝牙客户端
    private BluetoothSocket userConnectSocket;
    private OnConnectListener userConnectListener;
    private UserConnectThread userConnectThread;

    //服务器连接管理
    private final int SERVER = 1;
    private ConnectManagerThread serverManagerThread;
    private boolean serverConnected;
    //客服端连接管理
    private final int USER = 2;
    private ConnectManagerThread userManagerThread;
    private boolean userConnected;


    private BTManager() {

    }

    public BluetoothAdapter getBTAdapter() {
        return bTAdapter;
    }

    /**
     * 1 获取蓝牙manager
     *
     * @return
     */
    public static BTManager getInstance() {
        if (btManager == null) {
            btManager = new BTManager();

        }
        return btManager;
    }

    /**
     * 2.初始化 查看是否支持蓝牙
     *
     * @param context
     * @return
     */
    public boolean initBL(Context context) {
        this.context = context;
        bTAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bTAdapter == null) {
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
        if (bTAdapter != null) {
            if (!bTAdapter.isEnabled()) {
                bTAdapter.enable();
                if (bTAdapter.isEnabled()) {
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
     * 关闭蓝牙
     */
    public void closeBT() {
        bTAdapter.disable();
    }

    /**
     * 3.搜索蓝牙
     */
    public BTManager searchBT(OnBTDeviceFindListener listener) {
        this.onBTDeviceFindListener = listener;
        initReceiver();
        bTAdapter.startDiscovery();
        return btManager;
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        intentFilter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        intentFilter.addAction("android.bluetooth.device.action.ACL_DISCONNECT_REQUESTED");
        context.registerReceiver(bTReceiver, intentFilter);
    }

    /**
     * 配对
     */
    public Boolean bond(String address) {
        BluetoothDevice device = bTAdapter.getRemoteDevice(address);
        Method createBondMethod = null;
        try {
            createBondMethod = BluetoothDevice.class
                    .getMethod("createBond");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            if (createBondMethod != null) {
                return (Boolean) createBondMethod.invoke(device);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 取消搜索蓝牙
     */

    public void cancelDiscovery() {
        if (bTAdapter != null) {
            bTAdapter.cancelDiscovery();
        }
    }


    /**
     * @param style 可被搜索方式
     * @param time  时间(默认 120s,最多300s)
     */
    public void setCanDiscovery(int style, int time) {

        switch (style) {
            case CAN_DISCOVERY_NORMAL:
                setCanDiscoveryDuration(120);
                break;
            case CAN_DISCOVERY_TIME:
                setCanDiscoveryDuration(time);
                break;
            case CAN_DISCOVERY_ALWAYS:
                setCanDiscoveryDuration(0);
                break;
        }
    }

    /**
     * @param style 可被搜索方式
     */
    public void setCanDiscovery(int style) {
        setCanDiscovery(style, 0);
    }

    public final static int REQUEST_DISCOVERABLE = 0x100;

    /**
     * 有返回result
     *
     * @param activity
     * @param time
     */
    public void setCanDiscovery(Activity activity, int time) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
        activity.startActivityForResult(intent, REQUEST_DISCOVERABLE);
    }

    /**
     * 设置可被搜索的时间
     *
     * @param time
     */
    private void setCanDiscoveryDuration(int time) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
        context.startActivity(intent);
    }

    private final BroadcastReceiver bTReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "onReceive: " + action);
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    if (onBTDeviceFindListener != null) {
                        onBTDeviceFindListener.onSearchFinish();
                    }
                    break;
//                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
//                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    Log.i(TAG, "onReceive: state");
                    break;
//                case BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE:
//                    break;
//                case BluetoothAdapter.ACTION_REQUEST_ENABLE:
//                    break;
//                case BluetoothAdapter.ACTION_SCAN_MODE_CHANGED:
//                    break;
//                case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
//                    break;
//                case BluetoothAdapter.ACTION_STATE_CHANGED:
//                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (onBTDeviceFindListener != null) {
                        onBTDeviceFindListener.onBTFound(device);
                    }
                    break;
                case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.i(TAG, "onReceive: state");
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    Log.i(TAG, "onReceive: state");
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    Log.i(TAG, "onReceive: state");
                    break;
            }
        }
    };

    //设置搜索蓝牙监听
    public BTManager setOnBTDeviceFoundListener(OnBTDeviceFindListener listener) {
        this.onBTDeviceFindListener = listener;
        return btManager;
    }

    //蓝牙搜索监听接口
    public interface OnBTDeviceFindListener {
        void onBTFound(BluetoothDevice device);

        void onSearchFinish();
    }

    //设置作为服务器端
    public void setAsServer(OnConnectListener listener) {
        this.serverConnectListener = listener;
        serverConnectThread = new ServerConnectThread(serverName, uuid);
        if (serverConnectThread.openServer()) {
            serverConnectThread.start();
        } else {
            listener.onFailed();
        }
    }

    public void setAsServer() {
        setAsServer(null);
    }

    public void setServerConnectListener(OnConnectListener listener) {
        this.serverConnectListener = listener;
    }


    //关闭服务器
    public void closeServer() {
        serverConnectThread.cancel();
    }


    //服务器端进程
    private class ServerConnectThread extends Thread {
        private String name;
        private UUID uuid;

        public ServerConnectThread(String name, UUID uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        public boolean openServer() {
            BluetoothServerSocket temp = null;
            try {
                temp = bTAdapter.listenUsingRfcommWithServiceRecord(this.name, this.uuid);
                serverSocket = temp;
                Log.i(TAG, "ServerConnectThread: serverOpen ok");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "ServerConnectThread: severOpen error");
                return false;
            }
        }

        public void run() {
            BluetoothSocket socket = null;
            Log.i(TAG, "ServerConnectThread: waiting  for user to connect ...");
            serverConnectListener.onWaitingConnectTo();
            while (true) {
                try {
                    synchronized (BTManager.class) {
                        if (!serverConnected) {
                            socket = serverSocket.accept();
                            if (socket != null) {
                                managerServerSocket(socket);
                                cancel();
                                return;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
//                    serverConnectListener.onFailed();
                    return;
                }

            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ;
        }

    }


    //服务器端socket管理
    private void managerServerSocket(BluetoothSocket socket) {
        Log.i(TAG, "ServerConnectThread: there is an user connect to server");


        serverManagerThread = new ConnectManagerThread(socket, SERVER);
        if (serverConnected = serverManagerThread.initStream()) {
            serverConnectListener.onSuccess();
            serverManagerThread.start();
        } else {
            serverConnectListener.onFailed();
        }

    }


    //设置作为用户端
    public void setAsUser(BluetoothDevice device, OnConnectListener listener) {
        this.userConnectListener = listener;
        userConnectThread = new UserConnectThread(device, uuid);
        if (userConnectThread.initSocket()) {
            userConnectThread.start();
        } else {
            listener.onFailed();
        }
    }

    public void setAsUser(BluetoothDevice device) {
        setAsUser(device, null);
    }

    public void setUserConnectListener(OnConnectListener listener) {
        this.userConnectListener = listener;
    }


    //用户端连接进程
    private class UserConnectThread extends Thread {
        private BluetoothDevice device;
        private UUID uuid;

        public UserConnectThread(BluetoothDevice device, UUID uuid) {
            this.device = device;
            this.uuid = uuid;
        }

        public boolean initSocket() {
            try {
                BluetoothSocket temp = null;
                if (Build.VERSION.SDK_INT >= 10) {
                    temp = device.createInsecureRfcommSocketToServiceRecord(uuid);
                } else {
                    temp = device.createRfcommSocketToServiceRecord(uuid);
                }


                userConnectSocket = temp;
                Log.i(TAG, "UserConnectThread: init user ok");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "UserConnectThread: init user error ");
                return false;
            }

        }

        public void run() {
            cancelDiscovery();
            try {
                userConnectListener.onConnecting();
                userConnectSocket.connect();
                Log.i(TAG, "UserConnectThread: connect to server ok ");
                managerUserSocket(userConnectSocket);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "UserConnectThread: connect to server error ");
                try {
                    userConnectSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                userConnectListener.onFailed();
            }
        }
    }


    //客户端socket管理
    private void managerUserSocket(BluetoothSocket connectSocket) {
        userManagerThread = new ConnectManagerThread(connectSocket, USER);
        if (userConnected = userManagerThread.initStream()) {
            userConnectListener.onSuccess();
            userManagerThread.start();
        } else {
            userConnectListener.onFailed();
        }

    }

    //连接监听  成功/失败/读/写
    public interface OnConnectListener {
        void onSuccess();

        /**
         * 主要用于服务器端
         */
        void onWaitingConnectTo();

        /**
         * 主要用于用户端
         */
        void onConnecting();

        void onRead(byte[] bytes, int len);

//        void onReadOver();

        void onWrite(byte[] bytes);

        void onFailed();

        void onDisconnect();
    }

    //服务器端写入
    public void writeMsgFromServer(byte[] bytes) {
        serverManagerThread.write(bytes);
    }

    //客户端写入
    public void writeMsgFromUser(byte[] bytes) {
        userManagerThread.write(bytes);
    }


    //客户端/服务器 连接管理
    private class ConnectManagerThread extends Thread {
        private InputStream inputStream;
        private OutputStream outputStream;
        private BluetoothSocket socket;
        private int type;

        public ConnectManagerThread(BluetoothSocket socket, int type) {
            this.socket = socket;
            this.type = type;
        }

        public boolean initStream() {
            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
        }

        public void run() {
            byte[] buffer = new byte[1024 * 1024];
            int len = 0;
            while (true) {
                try {
                    if (socket.isConnected()) {
                        len = inputStream.read(buffer);
//                        Log.i(TAG, "run: 读取长度: " + len);
                        if (len != 0) {
                            if (type == SERVER) {
                                serverConnectListener.onRead(buffer, len);
                            } else {
                                userConnectListener.onRead(buffer, len);
                            }
                        }
//                        else {
//                            if (type == SERVER) {
//                                serverConnectListener.onReadOver();
//                            } else {
//                                userConnectListener.onReadOver();
//                            }
//                        }
                    } else {
                        setClosed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    setClosed();
                    return;
                }
            }

        }

        private void setClosed() {
            if (type == SERVER) {
                serverConnected = false;
                serverConnectListener.onDisconnect();
            } else {
                userConnected = false;
                userConnectListener.onDisconnect();
            }
        }

        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
                outputStream.flush();
                if (type == SERVER) {
                    serverConnectListener.onWrite(bytes);
                } else {
                    userConnectListener.onWrite(bytes);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭BTManager
    public void close() {
        if (userConnectSocket != null) {
            try {
                userConnectSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocket != null) {

            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverConnected = false;
        userConnected = false;
//        bTAdapter = null;
        try {
//            context.unregisterReceiver(bTReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserConnected() {
        return userConnected;
    }


    public boolean isServerConnected() {
        return serverConnected;
    }

}
