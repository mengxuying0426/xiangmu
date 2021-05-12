package com.example.yanxiaopeidemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.example.yanxiaopeidemo.R;

public class ScreenAdapter extends BaseAdapter {
    private int layoutRes;
    private Context context;
    private String[] list = {};

    public ScreenAdapter(int layoutRes, Context context, String[] list) {
        this.layoutRes = layoutRes;
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutRes,null);
        }
        final CheckBox cb1 = convertView.findViewById(R.id.text);
        cb1.setText(list[position]);
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("twx","选择了"+cb1.getText().toString());
            }
        });
        return convertView;
    }
}
