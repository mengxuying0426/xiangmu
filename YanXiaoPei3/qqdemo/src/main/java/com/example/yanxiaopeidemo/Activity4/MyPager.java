package com.example.yanxiaopeidemo.Activity4;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyPager extends PagerAdapter {

    private List<View> myViewList;
    private View mCurrentView;

    private final static String TAG = "MyPager";

    public MyPager(List<View> myViewList){
        this.myViewList = myViewList;
    }

    @Override
    public int getCount() {
        return myViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final View view = myViewList.get(position);
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        container.addView(view);
        Log.d(TAG, "instantiateItem: " + myViewList.get(position));
        return myViewList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        View view = (View) object;
//        container.removeView(view);
//        view = null;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        mCurrentView = (View)object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public View getPrimaryItem() {
        return mCurrentView;
    }
}
