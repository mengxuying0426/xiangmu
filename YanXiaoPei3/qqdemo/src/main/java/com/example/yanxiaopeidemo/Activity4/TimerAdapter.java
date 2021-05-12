package com.example.yanxiaopeidemo.Activity4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yanxiaopeidemo.R;

import java.util.ArrayList;

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<DataBean> mDataList;

    public TimerAdapter(Context context, ArrayList<DataBean> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.new_item_view, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DataBean bean = mDataList.get(position);
        if (null == bean) {
            return;
        }
        holder.tv_date.setText(bean.getDate());
        holder.tv_desc.setText(bean.getDescribe());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
        //处理顶部条目
        if (position == 0) {
           // holder.tv_line_top.setVisibility(View.INVISIBLE);
        } else if (position == mDataList.size() - 1) {
            //底部数据
           // holder.tv_line_bottom.setVisibility(View.INVISIBLE);
        } else {
            //设置圆点背景
            holder.tv_dot.setBackgroundResource(R.drawable.new_dot_normal);
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //第一步 定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }
    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_date;
        private final TextView tv_desc;
        private final TextView tv_line_top;
        private final TextView tv_line_bottom;
        private final TextView tv_dot;

        public MyViewHolder(View itemView) {
            super(itemView);
            //日期
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            //描述
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
            //顶部线
            tv_line_top = (TextView) itemView.findViewById(R.id.tv_line_top);
            //圆点
            tv_dot = (TextView) itemView.findViewById(R.id.tv_dot);
            //底部线
            tv_line_bottom = (TextView) itemView.findViewById(R.id.tv_line_bottom);
        }
    }
}
