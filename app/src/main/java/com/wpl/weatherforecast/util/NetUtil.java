package com.wpl.weatherforecast.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

    /**
     未使用
     */

public class NetUtil {

    public static String URL_WEATHER_CITY = "http://apis.juhe.cn/simpleWeather/query";
    public static String KEY = "c4ce7ac218ce511f54e01d0331e35d5c";
    private static String cityURL;

    public static String doGet(String urlStr){

        String result = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        //连接网络
        try {
            URL url = new URL(urlStr);
            Log.d("why", "---url---"+url);
            connection = (HttpURLConnection) url.openConnection();
            Log.d("why", "---connection---");
//
            connection.setRequestMethod("GET");
//            connection.setConnectTimeout(5000);
//            Log.d("why", "------");

            if(connection.getResponseCode() == 200){
                //从链接获取数据
                InputStream inputStream = connection.getInputStream();
                //Log.d("why", "---inputStream---"+inputStream);
                inputStreamReader = new InputStreamReader(inputStream);
                //Log.d("why", "---inputStreamReader---"+inputStreamReader);
                //缓冲区
                bufferedReader = new BufferedReader(inputStreamReader);

                //拼接字符串
                StringBuilder stringBuilder = new StringBuilder();
                String Line = "";
                while((Line = bufferedReader.readLine()) != null){
                    stringBuilder.append(Line);
                }
                result = stringBuilder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
            if(inputStreamReader != null){
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String getWeatherOfCity(String city){
        //拼接UIL

        //?city=%E8%8B%8F%E5%B7%9E&key="
        cityURL = URL_WEATHER_CITY + "?city=" + city + "&key=" + KEY;
        Log.d("why", "---cityURL---"+ cityURL);

        String weatherResult = doGet(cityURL);
        Log.d("why", "---weatherResult---"+weatherResult);
        return weatherResult;
    }
}
