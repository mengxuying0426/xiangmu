package com.example.yanxiaopeidemo.Activity4;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cretin.tools.inputpsw.dialog.PswInputDialog;
import com.example.yanxiaopeidemo.R;

public class PayActivity extends AppCompatActivity {
    private final int LOGIN_REQUEST =100;
    private ImageView img;
    private LinearLayout twelve;
    private LinearLayout three;
    private LinearLayout one;
    private Button btn;
    private LinearLayout weChat;
    private LinearLayout pay;
    private ImageView circle;
    private String imgUrl;
    private TextView judge;


    //记录用户选择的密码位数
    private int pswCount = 6;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findViews();
        setListener();
        Intent request = getIntent();
        int num = request.getIntExtra("time",0);
        imgUrl = request.getStringExtra("img");
        Bitmap b= BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getAbsolutePath()+"/"+imgUrl);
        System.out.println(imgUrl);
        Glide.with(this)
                .load(R.drawable.head)
                .circleCrop()
                .into(img);
        System.out.println(num);
        if(num==12){
            twelve.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
            btn.setText("35元立即购买");
        }
        if(num==3){
            three.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
            btn.setText("9.9元立即购买");
        }
        if(num==1){
            one.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
            btn.setText("2.3元立即购买");
        }


    }

    private void setListener() {
        MyListener listener =new MyListener();
        twelve.setOnClickListener(listener);
        three.setOnClickListener(listener);
        one.setOnClickListener(listener);
        weChat.setOnClickListener(listener);
        btn.setOnClickListener(listener);
        pay.setOnClickListener(listener);
        circle.setOnClickListener(listener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("嘻嘻");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOGIN_REQUEST &&resultCode==200){
            System.out.println("嘻嘻嘻嘻嘻");
            String name = data.getStringExtra("memober");
            judge.setText(name);

        }
    }

    private void findViews() {
        judge = findViewById(R.id.judge);
        img = findViewById(R.id.head);
        twelve =findViewById(R.id.twelve1);
        three = findViewById(R.id.three);
        one = findViewById(R.id.one);
        btn = findViewById(R.id.btn);
        weChat = findViewById(R.id.wechat);
        pay = findViewById(R.id.pay);
        circle =findViewById(R.id.circle);
    }
    class  MyListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.twelve1:
                    twelve.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                    three.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    one.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    btn.setText("35元立即购买");
                    break;
                case R.id.three:
                    three.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                    twelve.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    one.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    btn.setText("9.9元立即购买");
                    break;
                case R.id.one:
                    one.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                    three.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    twelve.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    btn.setText("2.3元立即购买");
                    break;
                case R.id.wechat:
                    weChat.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                    pay.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    //获取配置信息
                    //SwitchCompat switch_btn = findViewById(R.id.switch_btn);
                    PswInputDialog pswInputDialog = new PswInputDialog(PayActivity.this);
                    pswInputDialog.hideForgetPswClickListener();
                    pswInputDialog.showPswDialog();
                    //设置密码长度
                    pswInputDialog.setPswCount(pswCount);
                    pswInputDialog.setListener(new PswInputDialog.OnPopWindowClickListener() {
                        @Override
                        public void onPopWindowClickListener(String psw, boolean complete) {
                            if (complete) {
                                Intent intent = new Intent();
                                intent.setClass(PayActivity.this, com.example.yanxiaopeidemo.Activity4.PaySucessActivity.class);
                                intent.putExtra("name","微信支付成功");
                                startActivityForResult(intent,LOGIN_REQUEST);
                            }
                        }
                    });
                    break;
                case R.id.pay:
                    pay.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                    weChat.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                    //获取配置信息
                    //SwitchCompat switch_btn = findViewById(R.id.switch_btn);
                    PswInputDialog pswInputDialog2 = new PswInputDialog(PayActivity.this);
                    pswInputDialog2.hideForgetPswClickListener();
                    pswInputDialog2.showPswDialog();
                    //设置密码长度
                    pswInputDialog2.setPswCount(pswCount);
                    pswInputDialog2.setListener(new PswInputDialog.OnPopWindowClickListener() {
                        @Override
                        public void onPopWindowClickListener(String psw, boolean complete) {
                            if (complete) {
                                Intent intent2 = new Intent();
                                intent2.setClass(PayActivity.this, com.example.yanxiaopeidemo.Activity4.PaySucessActivity.class);
                                intent2.putExtra("name","支付宝支付成功");
                                startActivityForResult(intent2,LOGIN_REQUEST);
                            }
                        }
                    });
                    break;
                case R.id.circle:
                    circle.setImageResource(R.drawable.option);
                    break;
            }
        }
    }
}
