package com.example.yanxiaopeidemo.Activity4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yanxiaopeidemo.R;

public class MemberActivity extends AppCompatActivity {
    public static ImageView img;
    private TextView pay;
    private LinearLayout twelve;
    private LinearLayout three;
    private LinearLayout one;
    private  ImageView btnImg;
    private ImageView ex;
    private ImageView note;
    private ImageView topic;
    private ImageView he,people,repote,music;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        findViews();
        Intent request = getIntent();
        imgUrl = request.getStringExtra("img");
       // Bitmap b= BitmapFactory.decodeFile(getApplicationContext().getFilesDir().getAbsolutePath()+"/"+imgUrl);
        Glide.with(this)
                .load(R.drawable.head)
                .circleCrop()
                .into(img);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.PayActivity.class);
                intent.putExtra("time",1);
                intent.putExtra("img",imgUrl);
                startActivity(intent);

            }
        });
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("点击了");
                Intent intent = new Intent();
                intent.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.RecordVoiceActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.motive_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        setListener();

    }

    private void setListener() {
        MyListener listener = new MyListener();
        twelve.setOnClickListener(listener);
        three.setOnClickListener(listener);
        one.setOnClickListener(listener);
        ex.setOnClickListener(listener);
        note.setOnClickListener(listener);
        ex.setOnClickListener(listener);
        note.setOnClickListener(listener);
        he.setOnClickListener(listener);
        topic.setOnClickListener(listener);
        people.setOnClickListener(listener);
        repote.setOnClickListener(listener);
        music.setOnClickListener(listener);
    }

    private void findViews() {
        img = findViewById(R.id.head);
        pay = findViewById(R.id.tP);
        twelve =findViewById(R.id.twelve);
        three = findViewById(R.id.three);
        one = findViewById(R.id.one);
        btnImg =findViewById(R.id.btnImg);
        ex=findViewById(R.id.ex);
        note =findViewById(R.id.note);
        he=findViewById(R.id.he);
        topic =findViewById(R.id.topic);
        people =findViewById(R.id.people);
        repote =findViewById(R.id.repote);
        music = findViewById(R.id.music);
    }
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.twelve:
                    Intent intent = new Intent();
                    intent.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.PayActivity.class);
                    intent.putExtra("time",12);
                    intent.putExtra("img",imgUrl);
                    startActivity(intent);
                    break;
                case R.id.three:
                    Intent intent1 = new Intent();
                    intent1.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.PayActivity.class);
                    intent1.putExtra("time",3);
                    intent1.putExtra("img",imgUrl);
                    startActivity(intent1);
                    break;
                case R.id.one:
                    Intent intent2 = new Intent();
                    intent2.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.PayActivity.class);
                    intent2.putExtra("time",1);
                    intent2.putExtra("img",imgUrl);
                    startActivity(intent2);
                    break;
                case R.id.ex:
                    Intent intent3 = new Intent();
                    intent3.setClass(MemberActivity.this,ExpressionActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.note:
                    Intent intent4 = new Intent();
                    intent4.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.PlanActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.he:
                    Intent intent5 = new Intent();
                    intent5.setClass(MemberActivity.this,HeadActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.topic:
                    Intent intent6 = new Intent();
                    intent6.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.TopicActivity.class);
                    startActivity(intent6);
                    break;
                case R.id.people:
                    Intent intent7 =new Intent();
                    intent7.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.RobotActivity.class);
                    startActivity(intent7);
                    break;
                case R.id.repote:
                    Intent intent8 =new Intent();
                    intent8.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.VIPYueBaoActivity.class);
                    startActivity(intent8);
                    break;
                case R.id.music:
                    Intent intent9 = new Intent();
                    intent9.setClass(MemberActivity.this, com.example.yanxiaopeidemo.Activity4.MusciActivity.class);
                    startActivity(intent9);
                    break;

            }
        }
    }
}
