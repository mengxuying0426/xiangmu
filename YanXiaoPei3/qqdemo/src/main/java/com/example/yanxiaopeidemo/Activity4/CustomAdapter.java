package com.example.yanxiaopeidemo.Activity4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yanxiaopeidemo.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private int itemLayoutRes;
    private  String[] options;

    public CustomAdapter(Context context, String[] options, int itemLayoutRes) {
        this.context = context;
        this.options = options;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if(null!=options){
            return options.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(null!=options){
            return options[i];
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);//布局填充器
            convertView = inflater.inflate(itemLayoutRes, null);
        }
        ImageView img = convertView.findViewById(R.id.iv_photo);
        TextView text = convertView.findViewById(R.id.tv_name);
        text.setText(options[position]);
        return convertView;
    }
}
