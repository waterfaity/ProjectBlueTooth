package com.waterfairy.tool.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by water_fairy on 2016/11/16.
 */

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<BluetoothDevice> deviceList;

    public SearchAdapter(Context context, List<BluetoothDevice> list) {
        this.context = context;
        this.deviceList = list;
    }

    @Override
    public int getCount() {
        if (deviceList != null) {
            return deviceList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (deviceList != null) {
            return deviceList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.test_list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        BluetoothDevice bluetoothDevice = deviceList.get(position);
        textView.setText(bluetoothDevice.getAddress() + "--" + bluetoothDevice.getType() + "--" + bluetoothDevice.getName());
        return convertView;
    }
}
