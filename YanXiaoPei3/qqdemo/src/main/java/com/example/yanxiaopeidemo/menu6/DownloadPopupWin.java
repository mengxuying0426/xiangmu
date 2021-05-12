package com.example.yanxiaopeidemo.menu6;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxiaopeidemo.R;


public class DownloadPopupWin extends Activity implements View.OnClickListener {

    private TextView tvDown;
    private TextView tvCancel;
    private LinearLayout dldLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_popup_win);

        tvDown = findViewById(R.id.tv_d_dld);
        tvCancel = findViewById(R.id.tv_d_cancel);
        dldLayout = findViewById(R.id.dld_layout);
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        dldLayout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            }
        });
        tvDown.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //实现onTouchEvent触屏函数 点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_d_dld:
                Toast.makeText(DownloadPopupWin.this,
                        "即将下载", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.tv_d_cancel:
                Toast.makeText(DownloadPopupWin.this,
                        "取消", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        finish();
    }
}
