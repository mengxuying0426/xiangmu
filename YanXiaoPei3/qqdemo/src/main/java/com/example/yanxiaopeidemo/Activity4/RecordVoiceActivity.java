package com.example.yanxiaopeidemo.Activity4;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxiaopeidemo.Bean.RecordRadio;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RecordVoiceActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaRecorder mMediaRecorder = null;
    private MediaPlayer mPlayer = null;
    private TextView tvRecordTime, tvRecordTag;
    private Button ivControl, ivSubmit, ivRecordAgain;
    public String voicePath = "";   //录制的文件路径
    private static final int MAX_TIME = 60;  //录制最长时间
    private int voiceTime = 0; // 录音时长
    private int playTime = 0; // 播音时长
    private String name;
    private double voiceSize;   //voice体积大小
    private boolean isRunning = false; // 定义一个布尔类型的变量来监控是否正在录音
    private boolean isRecordOk = false; //是否已经录制好了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_voice);
        initView();


    }

    private void initView() {
        tvRecordTime = (TextView) findViewById(R.id.time);
        tvRecordTag = (TextView) findViewById(R.id.tag);
        tvRecordTag.setText("语音录制，最多录制60秒，点击开始");
        ivRecordAgain =  findViewById(R.id.iv_record_again);
        ivRecordAgain.setOnClickListener(this);
        ivRecordAgain.setEnabled(false);
        ivSubmit = findViewById(R.id.iv_submit);
        ivSubmit.setOnClickListener(this);
        ivSubmit.setEnabled(false);
        ivControl = findViewById(R.id.iv_control);
        ivControl.setOnClickListener(this);

    }
    //开始录音
    private void startRecord() {
        tvRecordTag.setText("录音中...点击录音停止");
        ivControl.setBackgroundResource(R.drawable.record);
        ivSubmit.setBackgroundResource(R.drawable.c11);
        ivRecordAgain.setBackgroundResource(R.drawable.c11);
        hanlder.postDelayed(runnable, 1000);
        mMediaRecorder = null;  //这里必须要释放，否则重复播放的时候会出现异常
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder(); // 判断是都为空 如果为空new 一个对象
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置输入源为麦克风
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // 设置输出格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // 设置音频的编码
            voicePath = getFilesDir().getAbsolutePath() + "/test.mp3"; // 创建一个临时文件来存储音频
            System.out.println(voicePath);
            mMediaRecorder.setOutputFile(voicePath);
            try {
                mMediaRecorder.prepare(); // 准备录音
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.start(); // 开启录音
            isRunning = true; // 正在录音
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            hanlder.postDelayed(this, 1000);
            if (!isRecordOk) {   //没有录制好，就录制,且控制录制的时候时间
                voiceTime++;
                tvRecordTime.setText(String.valueOf(voiceTime));
                if (voiceTime >= MAX_TIME) {
                    stopRecord();
                }
            } else {  //录制好了，就播放，且控制播放的时候时间
                if (playTime < 0) {
                    stopRecord();
                }
                playTime--;
                tvRecordTime.setText(String.valueOf(playTime));
            }

        }
    };

    private Handler hanlder = new Handler();
    //停止录音
    private void stopRecord() {
        if (isRunning) { // 如果正在录音
            mMediaRecorder.stop(); // 停止录音
            mMediaRecorder.release(); // 释放资源
        }

        ivRecordAgain.setEnabled(true);
        ivRecordAgain.setBackgroundResource(R.drawable.bc);
        ivSubmit.setEnabled(true);
        ivSubmit.setBackgroundResource(R.drawable.oc);
        tvRecordTag.setText("播放试听");
        hanlder.removeCallbacks(runnable);
        ivControl.setBackgroundResource(R.drawable.record1);
        isRecordOk = true;
        isRunning = false;
    }

    //开始播放
    private void starPlaying() {
        ivRecordAgain.setEnabled(false);
        ivSubmit.setEnabled(false);
        ivSubmit.setBackgroundResource(R.drawable.c11);
        ivRecordAgain.setBackgroundResource(R.drawable.c11);
        playTime = voiceTime;
        tvRecordTime.setText(String.valueOf(playTime));
        tvRecordTag.setText("播放中...点击停止播放");
        ivControl.setBackgroundResource(R.drawable.record);

        mPlayer = null;//这里必须要释放，
        if (null == mPlayer) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        tvRecordTime.setText(String.valueOf(0));    //有时候时间表示不同步，强制设为0
                        stopPlaying();
                    }
                });
                mPlayer.setDataSource(voicePath);  // 设置要播放的文件
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                mPlayer.release();
            }
            hanlder.postDelayed(runnable, 1000);
        }
    }

    // 停止播放
    private void stopPlaying() {
        ivRecordAgain.setEnabled(true);
        ivSubmit.setEnabled(true);
        ivControl.setBackgroundResource(R.drawable.record1);
        ivRecordAgain.setBackgroundResource(R.drawable.bc);
        ivSubmit.setBackgroundResource(R.drawable.oc);
        hanlder.removeCallbacks(runnable);
        if (mPlayer == null) {
            Toast.makeText(this, "您还没播放任何音频", Toast.LENGTH_SHORT).show();
        } else {
            tvRecordTag.setText("播放试听");
            mPlayer.stop();
            Toast.makeText(this, "已终止播放", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_submit:

                final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(this);
                View view1 = View.inflate(this, R.layout.voice_name, null);
                final EditText et = view1.findViewById(R.id.et);
                Button bu = view1.findViewById(R.id.btn);
                Button btn = view1.findViewById(R.id.btn1);
                name = et.getText().toString();
                alertDialog7
                        .setTitle("输入录音名称")
                        .setIcon(R.drawable.record2)
                        .setView(view1)
                        .create();
                final AlertDialog show = alertDialog7.show();
                bu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecordRadio recordRadio = new RecordRadio();
                        recordRadio.setName(et.getText().toString());
                        recordRadio.setUsername(ConfigUtil.USER_NAME);
                        saveRadioName(ConfigUtil.SERVER_ADDR +"SaveRadioServlet",recordRadio);
                        Toast.makeText(RecordVoiceActivity.this, "输入的名称是" + et.getText().toString(), Toast.LENGTH_SHORT).show();
                        show.dismiss();
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        show.dismiss();
                    }
                });
                break;
            case R.id.iv_record_again:
                ivControl.setBackgroundResource(R.drawable.record1);
                ivRecordAgain.setEnabled(false);
                ivSubmit.setEnabled(false);
                ivSubmit.setBackgroundResource(R.drawable.c11);
                ivRecordAgain.setBackgroundResource(R.drawable.c11);
                voicePath = null;
                isRecordOk = false;
                voiceTime = 0;
                tvRecordTime.setText(String.valueOf(voiceTime));
                break;
            case R.id.iv_control:
                if (!isRecordOk) {   //没有录制好，就录制
                    if (!isRunning) {
                        startRecord();
                    } else {
                        stopRecord();
                    }
                } else {  //录制好了，就播放
                    if (null != mPlayer && mPlayer.isPlaying()) {
                        stopPlaying();
                    } else {
                        starPlaying();
                    }
                }
                break;
        }
    }

    private void saveRadioName(final String s, final RecordRadio recordRadio) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Log.e("提示","进入录音保存");
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    OutputStream outputStream = conn.getOutputStream();
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(recordRadio);
                    Log.e("测试",jsonString);
                    outputStream.write(jsonString.getBytes());
                    conn.getInputStream();
                    InputStream in = conn.getInputStream();
                    outputStream.close();
                    in.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPlayer.release();   // Now the object cannot be reused
        mPlayer = null;

     //   mMediaRecorder.release();  // Now the object cannot be reused
        mMediaRecorder = null;
    }
}
