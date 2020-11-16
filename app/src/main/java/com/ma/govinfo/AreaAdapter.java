package com.ma.govinfo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {

    private Context context;
    private List<Area> areaList;

    public AreaAdapter(Context context, List<Area> areaList){
        this.context=context;
        this.areaList=areaList;
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
        Log.e("abc","==="+areaList.size()+"==="+position);
        Area area =areaList.get(position);
        String s= "第"+(position+1)+"个省级区域   "+area.getProvinceCode()+"  "+ area.getProvinceName();
        holder.tv_area.setText(s);
        holder.position=position;
    }

    @Override
    public int getItemCount() {
        return areaList.size();
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
                    Intent toCity=new Intent(context,CityActivity.class);
                    toCity.putExtra("area",areaList.get(position));
                    context.startActivity(toCity);
                }
            });
        }
    }
}
