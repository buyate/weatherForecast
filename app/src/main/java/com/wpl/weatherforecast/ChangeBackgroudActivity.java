package com.wpl.weatherforecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.wpl.weatherforecast.database.DBHelper;
import com.wpl.weatherforecast.database.DBManger;
import com.wpl.weatherforecast.util.SharepreferencesUtils;

public class ChangeBackgroudActivity extends AppCompatActivity implements View.OnClickListener {

    View mainRel, intVi, backIv;
    private boolean bo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_backgroud);

    //    mainRel = findViewById(R.id.main_rel);
        intVi = findViewById(R.id.city_iv_int);
        intVi.setOnClickListener(this);
        backIv = findViewById(R.id.bg_iv_back);
        backIv.setOnClickListener(this);
        findViewById(R.id.background1).setOnClickListener(this);
        findViewById(R.id.background2).setOnClickListener(this);
        findViewById(R.id.background3).setOnClickListener(this);
        findViewById(R.id.background4).setOnClickListener(this);
        findViewById(R.id.background5).setOnClickListener(this);
        findViewById(R.id.background6).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeBackgroudActivity.this);
        switch (view.getId()){
            case R.id.background1:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.drawable.background);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            case R.id.background2:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.drawable.background2);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.background3:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.drawable.background4);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.background4:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.mipmap.bg);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.background5:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.mipmap.bg2);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;

            case R.id.background6:
                builder.setTitle("是否改变背景");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharepreferencesUtils.setbackground(ChangeBackgroudActivity.this,R.mipmap.bg5);
                        Intent intent = new Intent();
                        intent.setClass(ChangeBackgroudActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.city_iv_int:
                builder.setTitle("系统信息");
                String str = DBHelper.getVersion();
                builder.setMessage("当前版本号："+str+".0");
                //builder.setPositiveButton("我知道了",null);
                break;
            case R.id.bg_iv_back:
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                finish();
                startActivity(intent);
                break;
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}