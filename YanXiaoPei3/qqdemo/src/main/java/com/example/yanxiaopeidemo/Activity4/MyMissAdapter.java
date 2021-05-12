package com.example.yanxiaopeidemo.Activity4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yanxiaopeidemo.Bean.Mission;
import com.example.yanxiaopeidemo.Bean.Mission;
import com.example.yanxiaopeidemo.R;

import java.util.List;

public class MyMissAdapter extends RecyclerView.Adapter <MyMissAdapter.MyViewHolder> {
    private List<Mission> missionList;
    private View view;

    public MyMissAdapter(List<Mission> missionList) {
        this.missionList = missionList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView shijian;
        private TextView finish;
        private TextView time;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            shijian = itemView.findViewById(R.id.shijian);
            finish =  itemView.findViewById(R.id.finish);
            time =  itemView.findViewById(R.id.time);
        }
    }

    public MyMissAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_wancheng, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyMissAdapter.MyViewHolder holder, int position) {
        holder.shijian.setText(missionList.get(position).getEvent());
        holder.finish.setText(missionList.get(position).getFinish());
        holder.time.setText(missionList.get(position).getEndwork());
    }

    @Override
    public int getItemCount() {
        return missionList.size();
    }
}
