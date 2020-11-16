package com.ma.govinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView getAreaTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    protected void initView() {
        getAreaTv= findViewById(R.id.main_area);
        getAreaTv.setOnClickListener(this);
    }

    protected void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_area:
                startActivity(new Intent(MainActivity.this,GetAreaActivity.class));
        }
    }
}
