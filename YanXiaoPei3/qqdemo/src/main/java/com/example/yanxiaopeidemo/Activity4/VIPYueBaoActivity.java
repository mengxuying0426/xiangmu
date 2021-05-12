package com.example.yanxiaopeidemo.Activity4;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.yanxiaopeidemo.R;

import java.util.ArrayList;
import java.util.List;

public class VIPYueBaoActivity extends Activity {
    private ViewPager mViewPager;
    private List<View> myViewList;
    private int mPosition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vip_yuebao);
        mViewPager = findViewById(R.id.in_viewpager);
        myViewList = new ArrayList<>();
        LayoutInflater layoutInflater = getLayoutInflater().from(VIPYueBaoActivity.this);

        View view1 = layoutInflater.inflate(R.layout.yuebao_item1, null,false);
        View view2 = layoutInflater.inflate(R.layout.yuebao_item2, null,false);
        myViewList.add(view1);
        myViewList.add(view2);
        mViewPager.setAdapter(new MyPager(myViewList));
        mPosition = mViewPager.getCurrentItem();
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition=position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                      //  Log.i("PageChange-State", "state:SCROLL_STATE_IDLE(�������û򻬶�����)");


                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
//                        Log.i("PageChange-State", "state:SCROLL_STATE_DRAGGING(���ƻ�����)");
//                        Log.i("��ǰҳ",""+mPosition);

                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                       // Log.i("PageChange-State", "state:SCROLL_STATE_SETTLING(����ִ�л�����)");
                        break;
                    default:
                        break;
                }

            }
        });

    }
}
