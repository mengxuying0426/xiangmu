package com.example.yanxiaopeidemo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.mxy2.MyFragmentPagerAdapter1;
import com.example.yanxiaopeidemo.utils.NoScrollViewPager;
import com.google.android.material.tabs.TabLayout;



public class SecondFragment extends Fragment {
    private TabLayout mTabLayout;
    private NoScrollViewPager mViewPager;

    private MyFragmentPagerAdapter1 myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //加载内容页面的布局文件（将内容页面的XML布局文件转成View类型的对象）
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
       view = inflater.inflate(R.layout.fragment_total,container,false);

        //获取内容页面当中空间的引用
        mViewPager = view.findViewById(R.id.view_Pager);
        mViewPager.setScanScroll(false);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter1(getFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

        return view;
    }

}
