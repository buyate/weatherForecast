package com.wpl.weatherforecast.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    //1数据库名称
    private static final String DB_NAME = "forecast.db";
    //2表名称
    private static final String TABLE_NAME = "info";
    //1版本号
    private static final int DB_VERSION = 1;
    //3
    private static DBHelper mHelper = null;
    //4读写锁
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    //1构造方法
    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    //2创建数据库，建表
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql =" CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " city VARCHAR(20) UNIQUE NOT NULL," +
                " content text NOT NULL)";
        sqLiteDatabase.execSQL(sql);
         sql =" CREATE TABLE IF NOT EXISTS City  (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " cityId VARCHAR(200)  NOT NULL," +
                " province VARCHAR(200)  NOT NULL," +
                " cityName VARCHAR(200)  NOT NULL," +
                " district VARCHAR(200)  NOT NULL)";
        sqLiteDatabase.execSQL(sql);
        sql =" CREATE TABLE IF NOT EXISTS CollectionCity  (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " cityId VARCHAR(200)  NOT NULL," +
                " province VARCHAR(200)  NOT NULL," +
                " cityName VARCHAR(200)  NOT NULL," +
                " district VARCHAR(200)  NOT NULL)";
        sqLiteDatabase.execSQL(sql);
    }

    //数据库版本更新DB_VERSION
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //获取版本号
    public static String getVersion(){
        return String.valueOf(DB_VERSION);
    }

}
