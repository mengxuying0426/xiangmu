package com.example.yanxiaopeidemo.mxy2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.yanxiaopeidemo.fragment.FragmentOne;
import com.example.yanxiaopeidemo.fragment.FragmentSecond;


public class MyFragmentPagerAdapter1 extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"练习", "错题"};
    public MyFragmentPagerAdapter1(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new FragmentSecond();
        }
        return new FragmentOne();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
