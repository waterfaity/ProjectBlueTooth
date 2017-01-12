package com.waterfairy.tool.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by water_fairy on 2016/7/29.
 */
public class PermissionUtils {

    /**
     * 位置权限,定位/蓝牙
     */
    public final static int REQUEST_LOCATION = 1;

    /**
     * 文件读写
     */
    public final static int REQUEST_STORAGE = 2;

    /**
     * 相机
     */
    public final static int REQUEST_CAMERA = 3;


    public static void requestPermission(Activity activity, int request) {
        String[] permissions = null;
        String permission = null;
        switch (request) {
            case REQUEST_LOCATION:
                permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
                permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                break;
            case REQUEST_STORAGE:
                permissions = new String[]{Manifest.permission.CAMERA};
                permission = Manifest.permission.CAMERA;
                break;
            case REQUEST_CAMERA:
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE};
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        if (permissions != null) {
            requestPermission(activity, permissions, permission, request);
        }
    }

    /**
     * @param activity
     * @param permissions
     * @param permission
     * @param request
     */
    public static void requestPermission(Activity activity,
                                         @NonNull String[] permissions,
                                         @NonNull String permission,
                                         int request) {
        int permissionCode = checkPermission(activity, permission);
        if (permissionCode != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissions, request);
        }

    }

    public static int checkPermission(Activity activity, String permission) {
        return ActivityCompat.checkSelfPermission(activity, permission);
    }

}
