package com.example.yanxiaopeidemo.mode;

import android.content.Context;


import com.example.yanxiaopeidemo.MyApplication;
import com.example.yanxiaopeidemo.R;

import java.util.ArrayList;
import java.util.List;

public class ColorManager {

	public static final int DEFAULT_COLOR = 0xFFffdd99;
	private final List<OnColorChangedListener> listeners = new ArrayList<OnColorChangedListener>();
	private int mCurrentColor = DEFAULT_COLOR;
	private static ColorManager instance;

	public static ColorManager getInstance() {
		if (instance == null) {
			return instance = new ColorManager();
		}
		return instance;
	}

	public void addListener(OnColorChangedListener listener) {
		if (!this.listeners.contains(listener)) {
			if (listener != null) {
				listener.onColorChanged(mCurrentColor);
				this.listeners.add(listener);
			}
		}
	}

	public void removeListener(OnColorChangedListener listener) {
		this.listeners.remove(listener);
	}

	public void notifyColorChanged(int color) {
		if (mCurrentColor == color) {
			return;
		}
		mCurrentColor = color;
		for (OnColorChangedListener listener : this.listeners) {
			if (listener != null) {
				listener.onColorChanged(color);
			}
		}
	}

	public int[] getSkinColor(Context context) {
		return context.getResources().getIntArray(R.array.default_color_array);
	}

	public void setSkinColor(Context context, int position) {
		int[] colorArr = context.getResources().getIntArray(
				R.array.default_color_array);
		MyApplication.mPreference.setSkinColorValue(colorArr[position]);
		notifyColorChanged(colorArr[position]);
	}

}
