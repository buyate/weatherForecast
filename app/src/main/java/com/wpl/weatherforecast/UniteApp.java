package com.wpl.weatherforecast;

import android.app.Application;

import com.wpl.weatherforecast.database.DBManger;

import org.xutils.x;

public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //初始化数据库，使数据库成为全局对象
        DBManger.initDB(this);
    }
}
