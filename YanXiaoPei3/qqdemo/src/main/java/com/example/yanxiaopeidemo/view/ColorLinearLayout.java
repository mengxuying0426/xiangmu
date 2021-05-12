package com.example.yanxiaopeidemo.view;

import android.content.Context;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.example.yanxiaopeidemo.mode.ColorManager;
import com.example.yanxiaopeidemo.mode.OnColorChangedListener;


public class ColorLinearLayout extends LinearLayout implements
		OnColorChangedListener {

	public ColorLinearLayout(Context context, AttributeSet attrs,
                             int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public ColorLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public ColorLinearLayout(Context context) {
		super(context);

	}

	@Override
	public void onColorChanged(int color) {
		if (getBackground() != null
				&& !(getBackground() instanceof ColorDrawable)) {
			getBackground().setColorFilter(new LightingColorFilter(color, 1));
		} else {
			setBackgroundColor(color);
		}
	}

	@Override
	protected void onAttachedToWindow() {
		ColorManager.getInstance().addListener(this);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		ColorManager.getInstance().removeListener(this);
		super.onDetachedFromWindow();
	}

}
