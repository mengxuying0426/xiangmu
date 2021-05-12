package com.example.yanxiaopeidemo.Activity4;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yanxiaopeidemo.Bean.Event;
import com.example.yanxiaopeidemo.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
    private List<Event> plansList;
    private View view;
    public MyAdapter(List<Event> plansList){
        this.plansList=plansList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView grid_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            grid_txt = itemView.findViewById(R.id.grid_txt);
        }

    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, final int position) {
        //设置显示的文字

        if (Integer.parseInt(plansList.get(position).getSignl())==0){
            Log.e("测试",Integer.parseInt(plansList.get(position).getSignl())+"");
            holder.grid_txt.setBackgroundResource(R.drawable.background_event);
        }else{
            holder.grid_txt.setBackgroundResource(R.drawable.background_event2);
        }
        holder.grid_txt.setText(plansList.get(position).getEventstr());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position,plansList.get(position).getEventstr());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position,plansList.get(position).getEventstr());
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        //设置显示的item数量为fruitList列表的元素的数量
        return plansList.size();
    }


    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position,String shijian);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position,String shijian);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}