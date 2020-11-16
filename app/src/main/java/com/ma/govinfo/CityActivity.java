package com.ma.govinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.List;

public class CityActivity extends AppCompatActivity {

    private Area area;
    private RecyclerView rv_area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        area= (Area) getIntent().getSerializableExtra("area");
        initView();
    }

    private void initView() {
        rv_area=findViewById(R.id.rv_area);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_area.setLayoutManager(layoutManager);
        CityAdapter adapter = new CityAdapter(CityActivity.this,area);
        rv_area.setAdapter(adapter);
    }
}
