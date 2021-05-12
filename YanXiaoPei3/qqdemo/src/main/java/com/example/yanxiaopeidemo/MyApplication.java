package com.example.yanxiaopeidemo;

import android.app.Application;

import com.example.yanxiaopeidemo.util.MyPreference;
import com.facebook.drawee.backends.pipeline.Fresco;


public class MyApplication extends Application{
    public static MyPreference mPreference;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mPreference = new MyPreference(getApplicationContext());
    }
}
