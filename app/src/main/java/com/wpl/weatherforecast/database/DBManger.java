package com.wpl.weatherforecast.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wpl.weatherforecast.bean.City;

import java.util.ArrayList;
import java.util.List;

public class DBManger {
    public static SQLiteDatabase database;

    //1数据库名称
    private static final String DB_NAME = "forecast.db";
    //2表名称
    private static final String TABLE_NAME = "info";
    //版本号
    private static final int DB_VERSION = 1;

    //初始化数据库信息
    public static void initDB(Context context) {
        //获取数据库对象
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    //获取数据库中全部城市
    public static List<String> queryAllCityName() {
        List<String> cityList = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        //遍历游标cursor判断是否有下一条
        while (cursor.moveToNext()) {
            int city1 = cursor.getColumnIndex("city");
            String city = cursor.getString(city1);
            cityList.add(city);
        }
        return cityList;
    }

    //根据城市名称，替换数据库内容
    public static int updateInfoByCity(String city, String content) {
        ContentValues values = new ContentValues();
        values.put("content", content);
        return database.update(TABLE_NAME, values, "city=?", new String[]{city});
    }

    //新增一条城市记录
    public static long addCityInfo(String city, String content) {
        ContentValues values = new ContentValues();
        values.put("city", city);
        values.put("content", content);
        return database.insert(TABLE_NAME, null, values);
    }

    //新增一条城市记录
    public static long addCity(String cityId, String province, String city, String district) {
        ContentValues values = new ContentValues();
        values.put("cityId", cityId);
        values.put("province", province);
        values.put("cityName", city);
        values.put("district", district);
        return database.insert("City", null, values);
    }

    //新增一条城市收藏记录
    public static long addCollectionCity(String cityId, String province, String city, String district) {
        ContentValues values = new ContentValues();
        values.put("cityId", cityId);
        values.put("province", province);
        values.put("cityName", city);
        values.put("district", district);
        return database.insert("CollectionCity", null, values);
    }


    //获取收藏城市
    public static List<City> selectCollectionCity() {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = database.query("CollectionCity", null, null, null, null, null, null);
        //遍历游标cursor判断是否有下一条
        while (cursor.moveToNext()) {
            City city = new City();


            int _id_index = cursor.getColumnIndex("_id");
            Long _id = cursor.getLong(_id_index);
            city.set_id(_id);

            int cityId_index = cursor.getColumnIndex("cityId");
            String cityId = cursor.getString(cityId_index);
            city.setCityId(cityId);

            int province_index = cursor.getColumnIndex("province");
            String province = cursor.getString(province_index);
            city.setProvince(province);

            int city_index = cursor.getColumnIndex("cityName");
            String cityStr = cursor.getString(city_index);
            city.setCityName(cityStr);

            int district_index = cursor.getColumnIndex("district");
            String district = cursor.getString(district_index);
            city.setDistrict(district);

            cityList.add(city);
        }
        return cityList;
    }


     //删除收藏城市
    public static void deleteCollectionCity(long _id) {
        String sql = "delete  from  CollectionCity  where   _id = ? ";

        database.execSQL(sql, new Long[]{_id});
    }


    //获取数据库中全部城市
    public static List<City> selectAllCityName() {
        List<City> cityList = new ArrayList<>();
        Cursor cursor = database.query("City", null, null, null, null, null, null);
        //遍历游标cursor判断是否有下一条
        while (cursor.moveToNext()) {
            City city = new City();


            int _id_index = cursor.getColumnIndex("_id");
            Long _id = cursor.getLong(_id_index);
            city.set_id(_id);

            int cityId_index = cursor.getColumnIndex("cityId");
            String cityId = cursor.getString(cityId_index);
            city.setCityId(cityId);

            int province_index = cursor.getColumnIndex("province");
            String province = cursor.getString(province_index);
            city.setProvince(province);

            int city_index = cursor.getColumnIndex("cityName");
            String cityStr = cursor.getString(city_index);
            city.setCityName(cityStr);

            int district_index = cursor.getColumnIndex("district");
            String district = cursor.getString(district_index);
            city.setDistrict(district);

            cityList.add(city);
        }
        return cityList;
    }


    //获取数据库中全部城市
    public static List<City> selectLikeCityName(String cityName) {
        List<City> cityList = new ArrayList<>();
        String sql = "select   *  from   City   where   cityName   like '%"+cityName+"%'";
        Cursor cursor = database.rawQuery(sql, new String[]{} );
        //遍历游标cursor判断是否有下一条
        while (cursor.moveToNext()) {
            City city = new City();


            int _id_index = cursor.getColumnIndex("_id");
            Long _id = cursor.getLong(_id_index);
            city.set_id(_id);

            int cityId_index = cursor.getColumnIndex("cityId");
            String cityId = cursor.getString(cityId_index);
            city.setCityId(cityId);

            int province_index = cursor.getColumnIndex("province");
            String province = cursor.getString(province_index);
            city.setProvince(province);

            int city_index = cursor.getColumnIndex("cityName");
            String cityStr = cursor.getString(city_index);
            city.setCityName(cityStr);

            int district_index = cursor.getColumnIndex("district");
            String district = cursor.getString(district_index);
            city.setDistrict(district);

            cityList.add(city);
        }
        return cityList;
    }


    //新增一条城市收藏
    public static long City(String cityId, String province, String city, String district) {
        ContentValues values = new ContentValues();
        values.put("cityId", cityId);
        values.put("province", province);
        values.put("city", city);
        values.put("district", district);
        return database.insert("City", null, values);
    }

    //根据城市名称，查询数据库内容
    public static String queryCollectByCity(String city) {
        Cursor cursor = database.query(TABLE_NAME, null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int content1 = cursor.getColumnIndex("content");
            String content = cursor.getString(content1);
            return content;
        }
        ;
        return null;
    }

    //根据城市名称，查询数据库内容
    public static String queryInfoByCity(String city) {
        Cursor cursor = database.query(TABLE_NAME, null, "city=?", new String[]{city}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int content1 = cursor.getColumnIndex("content");
            String content = cursor.getString(content1);
            return content;
        }
        return null;
    }
}
