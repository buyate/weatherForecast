package com.wpl.weatherforecast.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
    未使用
     */
public class Permission {

    //需要申请的权限
    private static String[] permission={
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    };

    private static List<String> permissionList = new ArrayList<>();
    public static int REQUEST_CODE = 1000;

    public static void checkPermission(Activity activity){
        //只对Android 6.0及以上系统进行校验
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //检查当前APP是否开启了权限
            for (int i = 0; i < permission.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permission[i])!= PackageManager.PERMISSION_GRANTED){
                    //未开启该权限，则请求系统弹窗，好让用户选择是否立即开启权限
                        permissionList.add(permission[i]);
                }
            }
            //调用申请方法
            if(permissionList.size()>0){
                requestPermission(activity);
            }
        }
    }

    private static void requestPermission(Activity activity){
        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]),REQUEST_CODE);
    }
}
