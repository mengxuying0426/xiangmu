package com.example.yanxiaopeidemo.adapter6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys6.SongInfo;

import java.util.ArrayList;


public class SongAdapter extends BaseAdapter {
    private ArrayList dataSource = new ArrayList<SongInfo>();
    private Context context;
    private int item_layout_id;
    private TextView tvSiSong;
    private TextView tvSiArtist;
    private LinearLayout linearItem;
    //声明列表项中的控件
    public SongAdapter(Context context, ArrayList<SongInfo> dataSource, int item_layout_id) {
        this.context = context;//上下文环境
        this.dataSource = dataSource;//数据源
        this.item_layout_id = item_layout_id;//列表项数据文件
    }

    @Override
    public int getCount() {
        if (null != dataSource) {//获得数据的条数
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        //获取每个item显示的数据对象
        if (null != dataSource) {
            return dataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            //convertView每个item的视图对象
            //加载列表项布局文件
            LayoutInflater mInflator = LayoutInflater.from(context);
            convertView = mInflator.inflate(item_layout_id, null);
        }
        //获取item中控件的引用
        tvSiSong = convertView.findViewById(R.id.tv_si_song);
        tvSiArtist = convertView.findViewById(R.id.tv_si_artist);
        //linearItem = convertView.findViewById(R.id.linear_item);

        //给数据项填充数据/设置控件内容
        SongInfo mItemData = (SongInfo) dataSource.get(position);
        String strSong = mItemData.getSongName();
        String strArtist = mItemData.getSongArtist();
        tvSiSong.setText(setStr(strSong,13));
        tvSiArtist.setText("-"+setStr(strArtist,8));

        return convertView; // 返回列表项
    }
    private String setStr(String str,int n) {
        if (str.length() >n){
            return str.substring(0,n)+"...";
        }else
            return str;
    }
}
