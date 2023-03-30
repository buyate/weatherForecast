package com.wpl.weatherforecast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.wpl.weatherforecast.bean.City;
import com.wpl.weatherforecast.bean.JsonToGsonCity;
import com.wpl.weatherforecast.database.DBManger;
import com.wpl.weatherforecast.util.CustLocation;
import com.wpl.weatherforecast.util.Permission;
import com.wpl.weatherforecast.util.SharepreferencesUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private AppCompatSpinner mSpinner;
//    private ArrayAdapter<String> mSpAdapter;
//    private String[] mCities;

    ImageView addCityIv;
    ImageView moreIv;
    LinearLayout pointLayout;
    ViewPager mainVp;
    RelativeLayout mainRel;

    //ViewPager数据源
    List<Fragment> fragmentList;
    //显示城市集合
    List<City> cityList;
    //表示ViewPager的页数指示器集合
    List<ImageView> imgList;
    //页面
    private CityFragmentPagerAdapter adapter;
    private Permission permission;

//    private Handler mHandler = new Handler(Looper.myLooper()){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 0){
//                //获取到消息
//                String weather= String.valueOf(msg.obj);
//                Log.d("why", "---主线程收到weather---" + weather);
//
////                Gson gson = new Gson();
////                WeatherBean weatherBean = gson.fromJson("result", WeatherBean.class);
////                Log.d("why", "---WeatherBean---" + weatherBean.toString());
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postCityList();
        //获取控件
        addCityIv = findViewById(R.id.main_iv_add);
        moreIv = findViewById(R.id.main_iv_more);
        pointLayout = findViewById(R.id.main_layout_point);
        mainVp = findViewById(R.id.main_vp);
        mainRel = findViewById(R.id.main_rel);
        addCityIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);

        //初始化
        fragmentList = new ArrayList<>();
        cityList = new ArrayList<>();
        imgList = new ArrayList<>();
        cityList = DBManger.selectCollectionCity();
        //动态权限请求
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        if (allGranted) {
                            //全部获取成功
                            if(cityList.size()<=0) {
                                initLocation();
                            }else {
                                //初始化ViewPager
                                initPager();
                                adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                                mainVp.setAdapter(adapter);
                                //创建指示器
                                initPoint();
                                //设置最后一个城市信息
                                mainVp.setCurrentItem(fragmentList.size() - 1);
                                //设置ViewPager
                                setPagerListener();
                            }
                        } else {
                            Log.d("TAG", "onResult: "+deniedList);
                            Toast.makeText(MainActivity.this, "These permissions are denied: " + deniedList,Toast.LENGTH_LONG).show();
                        }
                    }
                });


        //initLocation();
        //修改背景
        mainRel.setBackgroundResource(SharepreferencesUtils.getbackground(MainActivity.this));

        //initView();
    }


    private void initLocation() {
        CustLocation location = new CustLocation(MainActivity.this);
        location.startLocation();
        location.setBackLocation(new CustLocation.CallBackLocation() {
            @Override
            public void locationInfo(AMapLocation amapLocation) {
                Log.d("定位信息", "locationInfo: "+amapLocation);
                String cty = amapLocation.getCity();
                if (cty.length() >0) {
                    cty = cty.replace("市", "");
                    City city = new City();
                    city.setCityName(cty);
                    cityList.add(city);
                    Toast.makeText(MainActivity.this, "定位成功：" + amapLocation.getAddress(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "locationInfo: " + amapLocation);
                }

                //由于获取地址需要时间
                //初始化ViewPager
                initPager();
                adapter = new CityFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
                mainVp.setAdapter(adapter);
                //创建指示器
                initPoint();
                //设置最后一个城市信息
                mainVp.setCurrentItem(fragmentList.size() - 1);
                //设置ViewPager
                setPagerListener();

                //只需要获取一次
                location.destroyLocation();
            }
        });
    }

    private void setPagerListener() {
        //设置监听事件，实现转换页面时下面圆点变化
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //全部设为未选中图片
                for (int i = 0; i < imgList.size(); i++) {
                    imgList.get(i).setImageResource(R.drawable.point3);
                }
                //选中位置设为选中图片
                imgList.get(position).setImageResource(R.drawable.point1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initPoint() {
        for (int i = 0; i < fragmentList.size(); i++) {
            //创建一个图标
            ImageView pIv = new ImageView(this);
            //设置图标
            pIv.setImageResource(R.drawable.point3);
            //创建视图
            pIv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //获取强制转换
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) pIv.getLayoutParams();
            //设置外边距
            lp.setMargins(0, 0, 0, 0);
            imgList.add(pIv);
            pointLayout.addView(pIv);
        }
        if (imgList.size() > 0) {
            imgList.get((imgList.size() - 1)).setImageResource(R.drawable.point1);
        }
    }

    private void initPager() {
        //创建Fragment对象放到ViewPager数据源中
        for (int i = 0; i < cityList.size(); i++) {
            //创建页面对象
            CityWeatherFragment cwFragment = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city", cityList.get(i).getCityName());
            cwFragment.setArguments(bundle);
            fragmentList.add(cwFragment);
        }
    }

//    private void initView() {
//        mSpinner = findViewById(R.id.sp_city);
//        //可以修改为从网络获取资源
//        mCities = getResources().getStringArray(R.array.cities);
//        //关联资源，格式
//        mSpAdapter = new ArrayAdapter<>(this, R.layout.sp_item_layout, mCities);
//        mSpinner.setAdapter(mSpAdapter);
//        //点击事件
//        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                //获取城市
//                String selCity = mCities[i];
//                //获取资源函数
//                //getWeatherOfCity(selCity);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }

//    //请求网络
//    private void getWeatherOfCity(String selCity) {
//        //开启子线程，请求网络
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //请求网络
//                String weatherOfCity = NetUtil.getWeatherOfCity(selCity);
//                //使用Handler传递给主线程
//                //和new比效率提高，从消息时钟
//                Message message = Message.obtain();
//                message.what = 0;
//                message.obj =weatherOfCity;
//                mHandler.sendMessage(message);
//            }
//        }).start();
//    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.main_iv_add:
                intent.setClass(this, CityManagerActivity.class);
                finish();
                break;
            case R.id.main_iv_more:
                intent.setClass(this, ChangeBackgroudActivity.class);
                finish();
                break;
        }
        startActivity(intent);
    }


    private void postCityList() {
        if (DBManger.selectAllCityName().size() > 0) {
            return;
        }
        Toast.makeText(MainActivity.this, "初次打开同步省市区数据QAQ ,请耐心等待", Toast.LENGTH_LONG).show();
        RequestParams params = new RequestParams("http://apis.juhe.cn/simpleWeather/cityList?" + CityWeatherFragment.url2);
        x.http().get(params, new Callback.CommonCallback<String>() {


            //获取数据成功时
            @Override
            public void onSuccess(String json) {
                Log.d("TAG", "onSuccess: " + json);
                JsonToGsonCity jsonToGsonCity = new Gson().fromJson(json, JsonToGsonCity.class);
                if (jsonToGsonCity.getReason().equals("查询成功")) {
                    List<JsonToGsonCity.result> resultList = jsonToGsonCity.getResult();
                    for (JsonToGsonCity.result result : resultList) {

                        DBManger.addCity(result.getId(), result.getProvince(), result.getCity(), result.getDistrict());

                    }
                    Toast.makeText(MainActivity.this, "同步完成可以正常操作！", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "请求异常：" + json, Toast.LENGTH_LONG).show();
                }
            }

            //获取数据失败时
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            //取消请求
            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            //请求完成
            @Override
            public void onFinished() {

            }
        });


    }
}