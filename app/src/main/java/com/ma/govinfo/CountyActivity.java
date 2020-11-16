package com.ma.govinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.List;

public class CountyActivity extends AppCompatActivity {

    private Area.City city;
    private String provinceName;
    private RecyclerView rv_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county);
        city= (Area.City) getIntent().getSerializableExtra("city");
        provinceName=getIntent().getStringExtra("provinceName");
        initView();
    }

    private void initView() {
        rv_area=findViewById(R.id.rv_area);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_area.setLayoutManager(layoutManager);
        CountyAdapter adapter = new CountyAdapter(CountyActivity.this,city,provinceName);
        rv_area.setAdapter(adapter);
    }
}
