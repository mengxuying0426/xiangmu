package com.example.yanxiaopeidemo.Activity4;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanxiaopeidemo.Bean.BackEvent;
import com.example.yanxiaopeidemo.MainActivity;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ClockActivity extends AppCompatActivity {
    private ImageView back;
    private String begin;
    private String end;
    private Thread thread;
    private Handler handler;  //异步消息处理器
    private TextView tvTime;
    private SimpleDateFormat sdf;
    private SimpleDateFormat df;
    private TextView data;
    private String mMonth;
    private String mDay;
    private String mWay;
    private String mHours;
    private String mMinute;
    private String mYear;
    private ImageView suoji;

    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 全屏、隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_clock);

        back = findViewById(R.id.back);
        data = findViewById(R.id.data);
        tvTime = findViewById(R.id.tv_1);
        sdf = new SimpleDateFormat("hh:mm:ss");
        df = new SimpleDateFormat("hh:mm");
        //计算进度百分比
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent timeShow = getIntent();
                final String word = timeShow.getStringExtra("word");

                final EditText editText = new EditText(ClockActivity.this);
                AlertDialog.Builder inputDialog =
                        new AlertDialog.Builder(ClockActivity.this);
                inputDialog.setTitle("解锁密码").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText.getText().toString().equals(word)){
                                    String event = timeShow.getStringExtra("event");
                                    String beginTime = timeShow.getStringExtra("begintime");
                                    String endTime = timeShow.getStringExtra("endtime");
                                    end = sdf.format(new Date());
                                    if (event!=null&&beginTime!=null&&endTime!=null){
                                        //出现dialog

                                        //计算百分比
                                        double precent= calculTime(beginTime,endTime);
                                        Log.e("所占百分比", String.valueOf(precent));
                                        //上传数据
                                        BackEvent backEvent = new BackEvent();
                                        backEvent.setEvent(event);
                                        backEvent.setEndwork(df.format(new Date()));
                                        backEvent.setFinish(precent+"%");
                                        backEvent.setUsername(ConfigUtil.USER_NAME);
                                        saveEventFinish(ConfigUtil.SERVER_ADDR +"SaveVEventFinish",backEvent);
                                    }
                                    Intent i = new Intent(ClockActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Toast.makeText(ClockActivity.this,
                                            "学习初心： "+word,
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).show();
            }
        });
        if (begin == null) {
            begin = sdf.format(new Date());
        }
        tvTime.setText(sdf.format(new Date()));
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    tvTime.setText(sdf.format(new Date()));
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        Calendar calendar = Calendar.getInstance();
        mYear = String.valueOf(calendar.get(Calendar.YEAR));
        System.out.println(mYear);
        mMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);        //获取日期的月
        mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));      //获取日期的天
        mWay = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));      //获取日期的星期

        /**
         * 如果小时是个位数
         *
         *则在前面价格“0”
         * */
        if (calendar.get(Calendar.HOUR) < 10) {
            mHours = "0" + calendar.get(Calendar.HOUR);
        } else {
            mHours = String.valueOf(calendar.get(Calendar.HOUR));
        }
        /**
         * 如果分钟是个位数
         *
         *则在前面价格“0”
         * */
        if (calendar.get(Calendar.MINUTE) < 10) {
            mMinute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            mMinute = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        /**
         * 获取星期
         * 并设置出来
         * */
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        data.setText(mYear + "-" + mMonth + "-" + mDay + "  " + "周" + mWay);

    }

    private double calculTime(String beginTime,String endTime) {
        //开始时的总时间
        Date d1 = null;
        Date d2 = null;
        Date d3 = null;
        Date d4 = null;
        try {
            d1 = df.parse(beginTime);
            d2 = df.parse(endTime);
            d3 = sdf.parse(begin);
            d4 = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long between = (d2.getTime() - d1.getTime()) / 1000;
        long use = (d4.getTime() - d3.getTime()) / 1000;
        BigDecimal b1 = new BigDecimal(Double.toString(between));
        BigDecimal b2 = new BigDecimal(Double.toString(use));
        //默认保留两位会有错误，这里设置保留小数点后4位
        Log.e("计划时间", String.valueOf(between));
        Log.e("使用时间", String.valueOf(use));
        return b2.divide(b1, 2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;


    }

    private void saveEventFinish(final String s, final BackEvent backEvent) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Log.e("提示","进入保存事件界面");
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    OutputStream outputStream = conn.getOutputStream();
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(backEvent);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        // super.onResume();
        super.onResume();
        Log.e("测试","锁机进行了几次");
        this.startLockTask();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(ClockActivity.this, "请专心学习啊", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }


}
