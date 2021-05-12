package com.example.yanxiaopeidemo.Activity6;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.adapter6.CSongAdapter;
import com.example.yanxiaopeidemo.entitys6.SongInfo;
import com.example.yanxiaopeidemo.mode6.MyDatabaseHelper6;

import java.util.ArrayList;

public class MyCollectActivity extends AppCompatActivity {

    private SQLiteDatabase db1;
    private MyDatabaseHelper6 dbHelper1;
    private ArrayList<SongInfo> csongs = new ArrayList<>();
    private ListView cSonglv;
    private CSongAdapter csongAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_music_collect);
        //获取控件
        cSonglv = findViewById(R.id.lv_csong);
        //获取数据库对象
        dbHelper1 = new MyDatabaseHelper6
                (MyCollectActivity.this, "SongsN.db", null, 3);
        db1 = dbHelper1.getWritableDatabase();

        searchCSongs();
        csongAdapter1 = new CSongAdapter(
                MyCollectActivity.this,       // 上下文环境
                csongs, // 数据源
                R.layout.layout_csong_item // 列表项布局文件
        );

        cSonglv.setAdapter(csongAdapter1);
    }

    private void searchCSongs() {
        Log.e("ljn:","执行搜索一次");
        Cursor c =  db1.query("SongsN", null, "collect=1", null,null,null,null, null);

        if (csongs.size() != 0){
            csongs.clear();
        }
        if(c.moveToFirst()) {
            // 遍历游标
            do {
                int sId = c.getInt(c.getColumnIndex("songId"));
                String sName = c.getString(c.getColumnIndex("songName"));
                String sArtist = c.getString(c.getColumnIndex("songArtist"));
                int sCollect = c.getInt(c.getColumnIndex("collect"));
                String sCover = c.getString(c.getColumnIndex("songCover"));
                String sFile = c.getString(c.getColumnIndex("songFile"));
                Log.e("SongsN.db","-songId：" + sId  +"-songName：" + sName + "-songArtist:" + sArtist + "-collect:" + sCollect
                        + "-songCover:" + sCover + "-songFile:" + sFile);
                SongInfo itemData1 = new SongInfo(sId,sName,sArtist,sCollect,sCover,sFile);
                // 定义第1个数据项
                csongs.add(itemData1);
            } while (c.moveToNext());
        }
    }
}
