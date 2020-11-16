package com.ma.govinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CountyAdapter extends RecyclerView.Adapter<CountyAdapter.ViewHolder> {

    private Context context;
    private List<Area.City.County> countyList;
    private String cityName;
    private String provinceName;

    public CountyAdapter(Context context, Area.City city,String provinceName){
        this.context=context;
        this.countyList=city.getCountyList();
        this.cityName=city.getCityName();
        this.provinceName=provinceName;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_area,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Area.City.County county= countyList.get(position);
        String s="第"+(position+1)+"个县级区域    "+provinceName+"    "+cityName+"\n"+county.getCountryCode()+"   "+county.getCountryName();
        holder.tv_area.setText(s);

    }

    @Override
    public int getItemCount() {
        return countyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_area;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area=itemView.findViewById(R.id.item_tv_area);
        }
    }
}
