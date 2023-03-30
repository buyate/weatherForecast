package com.wpl.weatherforecast;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteCityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView sp_item_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sp_item_layout);

        sp_item_delete = findViewById(R.id.sp_item_delete);
        sp_item_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}