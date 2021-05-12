package com.example.yanxiaopeidemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yanxiaopeidemo.remember.RememberActivity;
import com.example.yanxiaopeidemo.view.CustomVideoView;


/**
 * 视频资源要添加res文件夹下创建raw文件夹
 * 需要在onRestart()方法里重新加载视频，防止退出返回时视频黑屏
 * 我这样写简单粗暴而已，当然，也可优雅的以自己看播放控件的VideoView处理方法，去处理资源释放和播放显示的问题。
 * 记得修改布局控件<com.daqie.videobackground.CustomVideoView...引用的包名，不然会报错哦
 * android:screenOrientation="portrait" 习惯性的把横竖屏切换也设置一下
 * android:theme="@style/Theme.AppCompat.Light.NoActionBar" ActionBar也可以设置成不显示的状态，可以根据自己喜好和项目需求
 */
public class LoginActivity extends AppCompatActivity {
    //创建播放视频的控件对象
    private CustomVideoView videoview;
    private Button btnLogin;
    public Button btnRememberPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //加载数据
        initView();
        setListener();
    }

    private void setListener() {

        ClickListener clickListener=new ClickListener();
        btnLogin.setOnClickListener(clickListener);
        btnRememberPwd.setOnClickListener(clickListener);
    }
    private void initView() {
        btnLogin=findViewById(R.id.btn_login);
        //加载视频资源控件
        videoview =  findViewById(R.id.videoview);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
        btnRememberPwd=findViewById(R.id.btn_remember_pwd);
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }
    class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login:
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_remember_pwd:
                    Intent intent1=new Intent(LoginActivity.this, RememberActivity.class);
                    startActivity(intent1);
            }

        }
    }
}
