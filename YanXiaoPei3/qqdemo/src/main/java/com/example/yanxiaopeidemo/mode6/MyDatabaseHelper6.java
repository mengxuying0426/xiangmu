package com.example.yanxiaopeidemo.mode6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class MyDatabaseHelper6 extends SQLiteOpenHelper {

    public MyDatabaseHelper6(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库不存在，第一次创建数据库时被自动调用
     * 并且只被调用1次
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //执行建表语句
        sqLiteDatabase.execSQL( "create table SongsN(songId int primary key not null," +
                "songName varchar(128),songArtist varchar(64),collect int ," +
                " songCover varchar(64),songFile varchar(10));");
        String insertData0 = "insert into SongsN values(1, '大鱼', '周深', 0, 's_dayu','1') ;" ;
        sqLiteDatabase.execSQL(insertData0);
        String insertData1 = "insert into SongsN values(2, 'Stay With Me', 'Punch/灿烈', 0, 's_staywithme', '2')";
        sqLiteDatabase.execSQL(insertData1);
        String insertData2 = "insert into SongsN values(3, '残酷月光', '林宥嘉', 0, 's_cankuyueguang', '3')";
        sqLiteDatabase.execSQL(insertData2);
        String insertData3 = "insert into SongsN values(4, '彩虹', '周杰伦', 0, 's_caihong','4')";
        sqLiteDatabase.execSQL(insertData3);
        String insertData4 = "insert into SongsN values(5, '发如雪', '周杰伦', 0, 's_faruxue', '5')";
        sqLiteDatabase.execSQL(insertData4);
        String insertData5 = "insert into SongsN values(6, 'Kiss The Rain', '李闰珉', 0, 's_kisstherain', '6')";
        sqLiteDatabase.execSQL(insertData5);
    }

    /**
     * 当数据库版本有更新时会被自动调用
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}