package com.ma.govinfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetAreaActivity extends BaseActivity {

    private List<Area> areaList=new ArrayList<>();
    private RecyclerView rv_area;
    private AreaAdapter adapter;
    private ProgressDialog dialog;
    private Elements tr;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    dialog.setMessage("正在解析数据...");
                    parseHtml();
                    break;
                case 1:
                    dialog.setMessage("正在保存数据...");
                    adapter.notifyDataSetChanged();
                    saveFile();
                    break;
                case 2:
                    if(dialog!=null&&dialog.isShowing()){
                        dialog.cancel();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_area);
        initView();
        initData();
    }

    private void initView() {
        rv_area=findViewById(R.id.rv_area);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_area.setLayoutManager(layoutManager);
        adapter=new AreaAdapter(GetAreaActivity.this,areaList);
        rv_area.setAdapter(adapter);
        dialog = new ProgressDialog(GetAreaActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在获取数据...");
        dialog.show();
    }

    private void initData() {
        getHtml();
    }

    private void getHtml(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url= Jsoup.connect(Constant.MCA_URL)
                            .get()
                            .getElementsByClass("tzggbox_c_b")
                            .get(2)
                            .select("a[href]")
                            .attr("abs:href");
                    Log.e("abc","===url:"+url);
                    Elements doc= Jsoup.connect(url)
                            .get()
                            .select("script");
                    String s=doc.get(4).data();
                    for (int i=0;i<doc.size();i++){
                        if(doc.get(i).data().contains("window.location.href=")){
                            s=doc.get(i).data().split("=")[1];
                            s=s.replace("\"","");
                            s=s.replace(";","");
                        }
                    }
                    Log.e("abc","===s:"+s);
                    tr= Jsoup.connect(s)
                            .maxBodySize(Integer.MAX_VALUE)
                            .get()
                            .select("table")
                            .get(0)
                            .select("tr");
                    handler.sendEmptyMessage(0);
                    Log.e("abc","===tr:"+tr.size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseHtml(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Area.City> cityList=new ArrayList<>();
                List<Area.City.County> countyList=new ArrayList<>();
                for (int i=0;i<tr.size();i++){
                    if(tr.get(i).select("td").size()>=3) {
                        Elements citys=tr.get(i).select("td");
                        String cityCode = citys.get(1).text();
                        Element cityInfo=citys.get(2);
                        String cityName = cityInfo.text();

                        if(!cityCode.isEmpty()&&!cityName.isEmpty()) {
                            if(cityInfo.hasClass("xl7025509")){//省市
                                if(!cityInfo.select("span").isEmpty()){//市
                                    Log.e("abc","===第"+i+"个数据  市级:"+cityName);
                                    if(countyList.size()>0){
                                        cityList.get(cityList.size()-1).setCountyList(countyList);
                                        countyList=new ArrayList<>();
                                    }
                                    Area.City city=new Area().new City();
                                    city.setCityCode(cityCode);
                                    city.setCityName(cityName);
                                    city.setCityId("city");
                                    cityList.add(city);
                                }else{//省
                                    Log.e("abc","===第"+i+"个数据  省级:"+cityName);
                                    if(countyList.size()>0){
                                        cityList.get(cityList.size()-1).setCountyList(countyList);
                                        countyList=new ArrayList<>();
                                    }
                                    if(cityList.size()>0){
                                        areaList.get(areaList.size()-1).setCityList(cityList);
                                        cityList=new ArrayList<>();
                                    }
                                    Area area = new Area();
                                    area.setProvinceCode(cityCode);
                                    area.setProvinceName(cityName);
                                    area.setProvinceId("area");
                                    areaList.add(area);
                                    if(cityName.endsWith("市")){
                                        Area.City city= area.new City();
                                        city.setCityCode(cityCode);
                                        city.setCityName(cityName);
                                        city.setCityId("city");
                                        cityList.add(city);
                                    }
                                }
                            }else if(cityInfo.hasClass("xl7125509")){//区县
                                if(!cityInfo.select("span").isEmpty()){
                                    Log.e("abc","===第"+i+"个数据  县级:"+cityName);
                                    Area.City.County county=new Area().new City().new County();
                                    county.setCountryCode(cityCode);
                                    county.setCountryName(cityName);
                                    county.setCountryId("county");
                                    countyList.add(county);
                                }
                            }
                        }
                    }
                }
                handler.sendEmptyMessage(1);
                Log.e("abc","===list:"+areaList.size());
            }
        }).start();
    }

    private void saveFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,List<Area>> map=new HashMap<>();
                map.put("data",areaList);
                String str=new Gson().toJson(map);
                Log.e("abc","===str:"+str);
                try {
                    if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    File dir =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, "area20201116.json");
                    if (!file.exists()) {
                        file.createNewFile();
                    }else{
                        //清空文本内容
                        FileWriter fileWriter =new FileWriter(file);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                    }
                    RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                    raf.seek(file.length());
                    raf.write(str.getBytes());
                    raf.close();
                    handler.sendEmptyMessage(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
