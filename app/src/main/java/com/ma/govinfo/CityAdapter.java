package com.ma.govinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private Context context;
    private List<Area.City> cityList;
    private String provinceName;

    public CityAdapter(Context context, Area area){
        this.context=context;
        this.cityList=area.getCityList();
        this.provinceName=area.getProvinceName();
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
        Area.City city=  cityList.get(position);
        String s="第"+(position+1)+"个市级区域    "+provinceName+"\n"+city.getCityCode()+"  "+city.getCityName();
        holder.tv_area.setText(s);
        holder.position=position;
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private int position;
        private TextView tv_area;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_area=itemView.findViewById(R.id.item_tv_area);
            tv_area.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toCounty=new Intent(context,CountyActivity.class);
                    toCounty.putExtra("city",cityList.get(position));
                    toCounty.putExtra("provinceName",provinceName);
                    context.startActivity(toCounty);
                }
            });
        }
    }
}
