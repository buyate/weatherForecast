package com.wpl.weatherforecast;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wpl.weatherforecast.bean.WeatherBean;
import com.wpl.weatherforecast.database.DBManger;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityWeatherFragment extends BaseFragment {

    private TextView frag_tv_win;
    private TextView frag_tv_air;
    private TextView frag_tv_city;
    private TextView frag_tv_content;
    private TextView frag_tv_tem;
    private TextView frag_tv_weather;
    private TextView frag_tv_humidity;
    private ImageView frag_iv_weather;
    private LinearLayout rlv_future_weather;

    String url1 = "http://apis.juhe.cn/simpleWeather/query?city=";
    public static String url2 = "&key=c4ce7ac218ce511f54e01d0331e35d5c";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView future_iv_weather;
    private String city;

    public CityWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityWeatherFragment newInstance(String param1, String param2) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);

        //初始化控件
        initView(view);

        //通过activity传值
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        Log.d("why", "---city---" + city);

        //city = "宜昌";
        String url = url1+ city +url2;
        //调用父类获取方法
        loadDate(url);

        return view;
    }

    @Override
    public void onSuccess(String result) {
//        super.onSuccess(result);
        //获取Json数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (weatherBean.getError_code() !=0){
            Toast.makeText(getActivity(), "查询失败了QAQ"+result, Toast.LENGTH_SHORT).show();
            return;
        }
        //解析并展示数据
        parseShowData(result);

        //获取成功，更新数据库
        int i = DBManger.updateInfoByCity(city, result);
        if(i<=0){
            //更新数据库失败，修改数据小于1，添加城市
            DBManger.addCityInfo(city, result);
        }
    }

    //获取网络链接失败读取数据库中信息
    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        //super.onError(ex, isOnCallback);
        String s = DBManger.queryInfoByCity(city);
        if (!TextUtils.isEmpty(s)) {
            parseShowData(s);
        }else {
            Toast.makeText(getActivity(), "查询失败了QAQ", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseShowData(String result) {
        Log.d("DATA", "parseShowData: "+result);
        //获取Json数据
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);

        String city = weatherBean.getResult().getCity();
        WeatherBean.ResultBean.RealtimeBean realtime = weatherBean.getResult().getRealtime();
        WeatherBean.ResultBean.FutureBean NowBean = weatherBean.getResult().getFuture().get(0);

        //设置
        frag_tv_city.setText(city);
        frag_tv_tem.setText(realtime.getTemperature()+"℃");
        frag_tv_weather.setText(NowBean.getWeather()+"("+NowBean.getDate()+")");
        frag_tv_content.setText(NowBean.getTemperature());
        frag_tv_win.setText(realtime.getDirect() + realtime.getPower());
        frag_tv_air.setText("空气质量："+realtime.getAqi());
        frag_tv_humidity.setText("湿度："+realtime.getHumidity());
        initImg(realtime.getWid(), 1);
        
        //未来天气
        List<WeatherBean.ResultBean.FutureBean> future = weatherBean.getResult().getFuture();
        for (int i = 1; i < future.size(); i++) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_future_weather,null);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            rlv_future_weather.addView(itemView);
            WeatherBean.ResultBean.FutureBean futureBean = future.get(i);
            future_iv_weather = itemView.findViewById(R.id.future_iv_weather);
            initImg(futureBean.getWid().getDay(), 2);
            TextView future_tv_content = itemView.findViewById(R.id.future_tv_content);
            future_tv_content.setText(futureBean.getTemperature());
            TextView future_tv_date = itemView.findViewById(R.id.future_tv_date);
            future_tv_date.setText(futureBean.getDate());
            TextView future_tv_weather = itemView.findViewById(R.id.future_tv_weather);
            future_tv_weather.setText(futureBean.getWeather());
        }
    }

    private void initImg(String wid, int i) {
        if(i==1) {
            switch (wid) {
                case "00":
                    frag_iv_weather.setImageResource(R.drawable.sun);
                    break;
                case "01":
                    frag_iv_weather.setImageResource(R.drawable.cloudy);
                    break;
                case "02":
                    frag_iv_weather.setImageResource(R.drawable.cloudy_sky);
                    break;
                case "03":
                case "09":
                    frag_iv_weather.setImageResource(R.drawable.rain_b);
                    break;
                case "04":
                case "05":
                    frag_iv_weather.setImageResource(R.drawable.thunder);
                    break;
                case "06":
                case "13":
                case "14":
                case "15":
                case "26":
                case "27":
                    frag_iv_weather.setImageResource(R.drawable.snow);
                    break;
                case "07":
                case "08":
                case "19":
                case "21":
                case "22":
                    frag_iv_weather.setImageResource(R.drawable.rain);
                    break;
                case "10":
                case "11":
                case "12":
                case "25":
                case "24":
                case "23":
                    frag_iv_weather.setImageResource(R.drawable.rain_big);
                    break;
                case "16":
                case "17":
                case "28":
                    frag_iv_weather.setImageResource(R.drawable.snow_big);
                    break;
                case "18":
                    frag_iv_weather.setImageResource(R.drawable.cloudy_more);
                    break;
                case "20":
                case "29":
                case "30":
                case "31":
                case "53":
                    frag_iv_weather.setImageResource(R.drawable.windy);
                    break;
                default:
                    frag_iv_weather.setImageResource(R.drawable.sun);
                    break;
            }
        }else if(i == 2){
            switch (wid) {
                case "00":
                    future_iv_weather.setImageResource(R.drawable.sun);
                    break;
                case "01":
                    future_iv_weather.setImageResource(R.drawable.cloudy);
                    break;
                case "02":
                    future_iv_weather.setImageResource(R.drawable.cloudy_sky);
                    break;
                case "03":
                case "09":
                    future_iv_weather.setImageResource(R.drawable.rain_b);
                    break;
                case "04":
                case "05":
                    future_iv_weather.setImageResource(R.drawable.thunder);
                    break;
                case "06":
                case "13":
                case "14":
                case "15":
                case "26":
                case "27":
                    future_iv_weather.setImageResource(R.drawable.snow);
                    break;
                case "07":
                case "08":
                case "19":
                case "21":
                case "22":
                    future_iv_weather.setImageResource(R.drawable.rain);
                    break;
                case "10":
                case "11":
                case "12":
                case "25":
                case "24":
                case "23":
                    future_iv_weather.setImageResource(R.drawable.rain_big);
                    break;
                case "16":
                case "17":
                case "28":
                    future_iv_weather.setImageResource(R.drawable.snow_big);
                    break;
                case "18":
                    future_iv_weather.setImageResource(R.drawable.cloudy_more);
                    break;
                case "20":
                case "29":
                case "30":
                case "31":
                case "53":
                    future_iv_weather.setImageResource(R.drawable.windy);
                    break;
                default:
                    future_iv_weather.setImageResource(R.drawable.sun);
                    break;
            }
        }
    }



    private void initView(View view) {
        //获取控件
        frag_tv_city = view.findViewById(R.id.frag_tv_city);
        frag_tv_air = view.findViewById(R.id.frag_tv_air);
        frag_tv_content = view.findViewById(R.id.frag_tv_content);
        frag_tv_tem = view.findViewById(R.id.frag_tv_tem);
        frag_tv_weather = view.findViewById(R.id.frag_tv_weather);
        frag_tv_win = view.findViewById(R.id.frag_tv_win);
        frag_iv_weather = view.findViewById(R.id.frag_iv_weather);
        frag_tv_humidity = view.findViewById(R.id.frag_tv_humidity);
        rlv_future_weather = view.findViewById(R.id.rlv_future_weather);
    }

}