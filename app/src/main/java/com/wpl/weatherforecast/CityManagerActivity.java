package com.wpl.weatherforecast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wpl.weatherforecast.bean.City;
import com.wpl.weatherforecast.database.DBManger;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends AppCompatActivity {
    private RelativeLayout cityTopLayout;
    private ImageView cityIvBack;
    private ImageView cityIvDelete;
    private TextView cityTvTitle;
    private ImageView cityLine;
    private ListView cityLv;
    private ImageView cityIvAdd;
    private City cityClik = null;
    private List<City> collectionCityList;
    List<City> cityList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        cityTopLayout = (RelativeLayout) findViewById(R.id.city_top_layout);
        cityIvBack = (ImageView) findViewById(R.id.city_iv_back);
        cityIvDelete = (ImageView) findViewById(R.id.city_iv_delete);
        cityTvTitle = (TextView) findViewById(R.id.city_tv_title);
        cityLine = (ImageView) findViewById(R.id.city_line);
        cityLv = (ListView) findViewById(R.id.city_lv);
        cityIvAdd = (ImageView) findViewById(R.id.city_iv_add);
        /**********收藏ListView**********/
        collectionCityList = DBManger.selectCollectionCity();
        ListViewAdapter listViewAdapter = new ListViewAdapter(collectionCityList);
        cityLv.setAdapter(listViewAdapter);
        cityLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cityClik = collectionCityList.get(i);
                new AlertDialog.Builder(CityManagerActivity.this).setMessage("是否删除！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (cityClik == null) {
                            return;
                        }
                        DBManger.deleteCollectionCity(cityClik.get_id());
                        collectionCityList = DBManger.selectCollectionCity();
                        ListViewAdapter listViewAdapter = new ListViewAdapter(collectionCityList);
                        cityLv.setAdapter(listViewAdapter);
                    }
                }).create().show();

            }
        });
        /**********添加收藏**********/
        cityIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collectionCityList.size() >= 5) {
                    Toast.makeText(CityManagerActivity.this, "已经超过5个不能继续添加！！", Toast.LENGTH_LONG).show();
                    return;

                }
                cityClik = null;
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_search_city, null);
                ImageView searchIvSubmit = (ImageView) v.findViewById(R.id.search_iv_submit);
                EditText searchEt = (EditText) v.findViewById(R.id.search_et);
                //TextView searchTv = (TextView) v.findViewById(R.id.search_tv);
                ListView searchGv = (ListView) v.findViewById(R.id.search_gv);
                AlertDialog.Builder builder = new AlertDialog.Builder(CityManagerActivity.this);
                builder.setTitle("选择收藏城市");
                builder.setView(v);
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();

                cityList = DBManger.selectAllCityName();
                ListViewAdapter listViewAdapter = new ListViewAdapter(cityList);
                searchGv.setAdapter(listViewAdapter);
                searchIvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (searchEt.getText().toString().length() == 0) {
                            List<City> cityList = DBManger.selectAllCityName();
                            ListViewAdapter listViewAdapter = new ListViewAdapter(cityList);
                            searchGv.setAdapter(listViewAdapter);
                            return;
                        }
                        cityList = DBManger.selectLikeCityName(searchEt.getText().toString());
                        ListViewAdapter listViewAdapter = new ListViewAdapter(cityList);
                        searchGv.setAdapter(listViewAdapter);

                    }
                });
                searchGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        cityClik = cityList.get(i);
                        DBManger.addCollectionCity(cityClik.getCityId(), cityClik.getProvince(), cityClik.getCityName(), cityClik.getDistrict());
                        dialog.dismiss();

                        DBManger.deleteCollectionCity(cityClik.get_id());
                        collectionCityList = DBManger.selectCollectionCity();
                        ListViewAdapter listViewAdapter = new ListViewAdapter(collectionCityList);
                        cityLv.setAdapter(listViewAdapter);
                    }
                });
            }
        });
        cityIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  intent   =new Intent(CityManagerActivity.this,MainActivity.class);

        startActivity(intent);
        finish();

    }

    private class ListViewAdapter extends BaseAdapter {
        private List<City> cityList = new ArrayList<>();

        public ListViewAdapter(List<City> cityList) {
            this.cityList = cityList;
        }

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int i) {
            return cityList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.sp_item_layout, null);
            City city = cityList.get(i);
            TextView text1 = (TextView) v .findViewById(R.id.text1);
            text1.setText(city.getProvince() + " -> " + city.getCityName() + " -> " + city.getDistrict());
            return v;
        }
    }


}