package com.wpl.weatherforecast.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    //获取时间
    public static String getNowTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

}
