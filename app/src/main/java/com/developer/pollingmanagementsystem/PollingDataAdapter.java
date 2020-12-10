package com.developer.pollingmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PollingDataAdapter extends RecyclerView.Adapter<PollingDataAdapter.MyViewHolder>{


    public List<PollingDataModel> pollingData;

    public PollingDataAdapter() {
    }

    public PollingDataAdapter(List<PollingDataModel> pollingData) {
        this.pollingData = pollingData;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zonal_template,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PollingDataModel pollingDataModel = pollingData.get(position);
        holder.timeTv.setText(pollingDataModel.getTime());
        holder.stationIdTv.setText(pollingDataModel.getBoothNumber());
        holder.maleCountTv.setText(String.valueOf(pollingDataModel.getMaleCount()));
        holder.femaleCountTv.setText(String.valueOf(pollingDataModel.getFemaleCount()));
        holder.totalCountTv.setText(String.valueOf(pollingDataModel.getTotalCount()));
        holder.percentageCountTv.setText(String.valueOf(pollingDataModel.getPercentage()));

    }


    @Override
    public int getItemCount() {
        return pollingData.size();
    }


// VIEWHOLDER CLASS

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView timeTv,stationIdTv,maleCountTv,femaleCountTv,totalCountTv,percentageCountTv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.time);
            stationIdTv = itemView.findViewById(R.id.boothId);
            maleCountTv = itemView.findViewById(R.id.maleCount);
            femaleCountTv = itemView.findViewById(R.id.femaleCount);
            totalCountTv = itemView.findViewById(R.id.totalCount);
            percentageCountTv = itemView.findViewById(R.id.percentageCount);
        }
    }
}
