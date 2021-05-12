package com.example.yanxiaopeidemo.adapter6;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxiaopeidemo.Activity6.MyCollectActivity;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys6.SongInfo;
import com.example.yanxiaopeidemo.mode6.MyDatabaseHelper6;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSongAdapter extends BaseAdapter {
    private SQLiteDatabase db;
    private MyDatabaseHelper6 helper;
    private ArrayList<SongInfo> dataSource;
    private Context context;
    private int item_layout_id;
    //    private Button btnCShow;
    private ImageView ivShowCover;
    private TextView tvShowSong;
    private TextView tvShowArtist;


    //声明列表项中的控件
    public CSongAdapter(Context context, ArrayList<SongInfo> dataSource, int item_layout_id) {
        this.context = context;//上下文环境
        this.dataSource = dataSource;//数据源
        this.item_layout_id = item_layout_id;//列表项数据文件
    }
    @Override
    public int getCount() {
        if (null != dataSource){//获得数据的条数
            return dataSource.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        //获取每个item显示的数据对象
        if (null != dataSource){
            return dataSource.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        //获取每个item的id值
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            //convertView每个item的视图对象
            //加载列表项布局文件
            LayoutInflater mInflator = LayoutInflater.from(context);
            convertView = mInflator.inflate(item_layout_id, null);
        }
        //获取item中控件的引用
        ivShowCover = convertView.findViewById(R.id.iv_show_cover);
        tvShowSong = convertView.findViewById(R.id.tv_c_show_song);
        tvShowArtist = convertView.findViewById(R.id.tv_c_show_artist);
//        btnCShow = convertView.findViewById(R.id.btn_c_show);

        //获取数据库对象,（可供修改）
        helper = new MyDatabaseHelper6
                (context, "SongsN.db", null, 3);
        db = helper.getWritableDatabase();

        //给数据项填充数据/设置控件内容
        SongInfo mItemData = dataSource.get(position);
        String strCover = mItemData.getSongCover();
        String strSongName  = mItemData.getSongName();
        String strSongArtist = mItemData.getSongArtist();

        ivShowCover.setImageResource(getImageId(strCover));;
        tvShowSong.setText(strSongName);
        tvShowArtist.setText(strSongArtist);
//        btnCShow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContentValues values = new ContentValues();
//                //存放更新后的信息
//                values.put("collect" ,0);
//                int sId= ((SongInfo)getItem(position)).getId();
//                db.update("SongsN",values,"songId=?" , new String[]{ sId +""} );
//                Log.e("取消收藏：","song:" + dataSource.get(position).getSongName());
//                Toast.makeText(context,"取消收藏成功",Toast.LENGTH_SHORT).show();
//                dataSource.remove(position);
//                notifyDataSetChanged();
//            }
//        });
        return convertView;
    }
    private int getImageId(String strCover) {
        Class drawable = R.drawable.class;
        Field field = null;
        try {
            field = drawable.getField(strCover);
            int imageId = field.getInt(field.getName());
            return imageId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
