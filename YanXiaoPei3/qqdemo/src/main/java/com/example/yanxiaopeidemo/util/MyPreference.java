package com.example.yanxiaopeidemo.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yanxiaopeidemo.mode.ColorManager;


public class MyPreference {
	private final static String KEY_APP_SKIN = "key_app_skin";
	private SharedPreferences mPreferences;
	public MyPreference(Context context) {
		mPreferences = context.getSharedPreferences("motive_preference",
				Context.MODE_PRIVATE);
	}
	public void setSkinColorValue(int color) {
		mPreferences.edit().putInt(KEY_APP_SKIN, color).commit();
	}
	public int getSkinColorValue() {
		return mPreferences.getInt(KEY_APP_SKIN, ColorManager.DEFAULT_COLOR);
	}
}
