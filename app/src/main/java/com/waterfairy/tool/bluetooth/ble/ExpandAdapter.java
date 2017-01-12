package com.waterfairy.tool.bluetooth.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.waterfairy.tool.R;

import java.util.List;
import java.util.UUID;

/**
 * Created by water_fairy on 2016/11/16.
 */

public class ExpandAdapter extends BaseExpandableListAdapter {
    public static final String TAG = "expandAdapter";
    private Context context;
    private List<BluetoothGattService> lists;

    public ExpandAdapter(Context context, List<BluetoothGattService> lists) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return lists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return lists.get(groupPosition).getCharacteristics().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        BluetoothGattService bluetoothGattService = lists.get(groupPosition);
        UUID uuid = bluetoothGattService.getUuid();
        textView.setText("　　" + uuid.toString());
        Log.i(TAG, "getGroupView: " + uuid.toString());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_child, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        BluetoothGattService bluetoothGattService = lists.get(groupPosition);
        BluetoothGattCharacteristic bluetoothGattCharacteristic = bluetoothGattService.getCharacteristics().get(childPosition);
        textView.setText(" " + childPosition + ": " + bluetoothGattCharacteristic.getUuid().toString());
        Log.i(TAG, "getChildView:         " + bluetoothGattCharacteristic.getUuid().toString());
        List<BluetoothGattDescriptor> descriptors = bluetoothGattCharacteristic.getDescriptors();
        for (int i = 0; i < descriptors.size(); i++) {
            BluetoothGattDescriptor descriptor = descriptors.get(i);
            UUID uuid = descriptor.getUuid();
            int permissions = descriptor.getPermissions();
            byte[] value = descriptor.getValue();
            Log.i(TAG, "getChildView: descriptor uuid 　　　-" + uuid);
            Log.i(TAG, "getChildView: descriptor permission-" + permissions);
            Log.i(TAG, "getChildView: descriptor values    -" + value);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
