package com.wpl.weatherforecast.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;

public class CustLocation {
    private static String TAG = CustLocation.class.getSimpleName();
    private Context context;

    public CallBackLocation setBackLocation(CallBackLocation callBackLocation) {
        this.backLocation = callBackLocation;
        return backLocation;
    }

    static CallBackLocation backLocation;

    public interface CallBackLocation {
        void locationInfo(AMapLocation amapLocation);
    }


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    public CustLocation(Context context) {
        //初始化定位
        try {

            AMapLocationClient.updatePrivacyAgree(context, true);
            AMapLocationClient.updatePrivacyShow(context, true, true);

            mLocationClient = new AMapLocationClient(context);
            mLocationOption = initAMapLocationClientOption();
            mLocationClient.setLocationOption(mLocationOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private AMapLocationClientOption initAMapLocationClientOption() {
//初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        mLocationOption.setOnceLocation(false);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        // mLocationOption.setOnceLocationLatest(true);

//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
//设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(2000);
//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);


        return
                mLocationOption;
    }


    /**
     * 开始定位
     */
    public void startLocation() {
        Log.d("TAG", "startLocation: " + mLocationClient);
//给定位客户端对象设置定位参数

//启动定位
        mLocationClient.stopLocation();
        mLocationClient.startLocation();
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (backLocation != null) {
                    backLocation.locationInfo(amapLocation);
                }

            }

        });

    }

    public void destroyLocation() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。


        }


    }
}
