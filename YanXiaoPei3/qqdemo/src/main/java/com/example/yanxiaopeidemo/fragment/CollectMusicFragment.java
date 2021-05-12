package com.example.yanxiaopeidemo.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.adapter6.CSongAdapter;
import com.example.yanxiaopeidemo.entitys6.SongInfo;
import com.example.yanxiaopeidemo.mode6.MyDatabaseHelper6;

import java.util.ArrayList;


public class CollectMusicFragment extends Fragment {

    private static final String TAG = "CollectMusicFragment";
//    private String mTagtext;
    private SQLiteDatabase db1;
    private MyDatabaseHelper6 dbHelper1;
    private ArrayList<SongInfo> csongs = new ArrayList<>();
    private ListView cSonglv;
    private CSongAdapter csongAdapter1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle arguments = getArguments();
//        mTagtext = arguments.getString(MainActivity.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "MusicCollect-onCreateView: ");
        View inflate = inflater.inflate(R.layout.fragment_music_collect, null);


//        TextView tvText = (TextView) inflate.findViewById(R.id.tv_text);
//        if (mTagtext != null && !TextUtils.isEmpty(mTagtext)) {
//            tvText.setText(mTagtext);
//        } else {
//            Log.i(TAG, "onCreateView: mTagText -- " + mTagtext);
//            tvText.setText("Null");
//        }
        //获取控件
        cSonglv = inflate.findViewById(R.id.lv_csong);
        //获取数据库对象
        dbHelper1 = new MyDatabaseHelper6
                (getContext(), "SongsN.db", null, 3);
        db1 = dbHelper1.getWritableDatabase();

        searchCSongs();
        csongAdapter1 = new CSongAdapter(
                getContext(),       // 上下文环境
                csongs, // 数据源
                R.layout.layout_csong_item // 列表项布局文件
        );

        cSonglv.setAdapter(csongAdapter1);
        return inflate;
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
