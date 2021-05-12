package com.example.yanxiaopeidemo.menu;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.yanxiaopeidemo.MainActivity;
import com.example.yanxiaopeidemo.MyApplication;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.adapter.WaterFallAdapter;
import com.example.yanxiaopeidemo.entitys.PersonCard;
import com.example.yanxiaopeidemo.mode.ColorManager;

import java.util.ArrayList;
import java.util.List;

public class PersonalDecoration extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private WaterFallAdapter mAdapter;
    private final int[] layouts = { R.id.skin_01, R.id.skin_02, R.id.skin_03,
            R.id.skin_04, R.id.skin_05 };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_decoration_layout);
        mRecyclerView=findViewById(R.id.recyclerview);
        init();
    }
    private void init() {

        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new WaterFallAdapter(this, buildData());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.motive_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        int colorArr[] = ColorManager.getInstance().getSkinColor(this);
        for (int i = 0; i < layouts.length; i++) {
            View view = findViewById(layouts[i]);
            View color = view.findViewById(R.id.motive_item_color);
            View selected = view.findViewById(R.id.motive_item_selected);
            color.setBackgroundColor(colorArr[i]);
            if (colorArr[i] == MyApplication.mPreference.getSkinColorValue()) {
                selected.setVisibility(View.VISIBLE);
            }
            color.setOnClickListener(new OnSkinColorClickListener(i));
        }
    }
    //生成6个明星数据，这些Url地址都来源于网络
    private List<PersonCard> buildData() {

        String[] names = {"杨紫","金晨","蒋依依","谭松韵","唐艺昕","张佳宁"};
        int[] imgUrs = {R.drawable.yangzi,R.drawable.jingchen,R.drawable.jiangyiyi,R.drawable.tansongyun,
                        R.drawable.tangyixin,R.drawable.zhangjianing};

        List<PersonCard> list = new ArrayList<>();
        for(int i=0;i<6;i++) {
            PersonCard p = new PersonCard();
            p.avatarUrl = imgUrs[i];
            p.name = names[i];
            p.imgHeight = (i % 2)*100 + 400; //偶数和奇数的图片设置不同的高度，以到达错开的目的
            list.add(p);
        }

        return list;
    }
    class OnSkinColorClickListener implements View.OnClickListener {

        private int position;

        public OnSkinColorClickListener(int position) {
            this.position = position;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {
            for (int i = 0; i < layouts.length; i++) {
                View view = findViewById(layouts[i]);
                View selected = view.findViewById(R.id.motive_item_selected);
                selected.setVisibility(i == position ? View.VISIBLE : View.GONE);
                ColorManager.getInstance().setSkinColor(PersonalDecoration.this, position);
               if(position==0){
                   MainActivity.mRlTitle.setBackgroundColor(0xFFFFB6C1);
                   MainActivity.ll_head.setBackgroundColor(0xFFFFB6C1);
               }else if(position==1){
                   MainActivity.mRlTitle.setBackgroundColor(0xFFCC99CC);
                   MainActivity.ll_head.setBackgroundColor(0xFFCC99CC);
               }else if(position==2){
                   MainActivity.mRlTitle.setBackgroundColor(0xFFCCCCFF);
                   MainActivity.ll_head.setBackgroundColor(0xFFCCCCFF);
               }else if(position==3){
                   MainActivity.mRlTitle.setBackgroundColor(0xFF99CC99);
                   MainActivity.ll_head.setBackgroundColor(0xFF99CC99);
               }else if(position==4){
                   MainActivity.mRlTitle.setBackgroundColor(0xFFffdd99);
                   MainActivity.ll_head.setBackgroundColor(0xFF99CC99);
               }

            }
        }
    }
}
