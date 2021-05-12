package com.example.yanxiaopeidemo.Activity4;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.MainActivity;
import com.example.yanxiaopeidemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

public class SetActivity extends Activity {
    private EditText eventWord;
    private TextView ensure;
    private TextView cancel;
    private TimePicker timePick1;
    private TimePicker timePick2;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);
        findView();
        timePick1.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        timePick2.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        //是否使用24小时制
        timePick1.setIs24HourView(true);
        timePick2.setIs24HourView(true);
        listener();
    }

    private void findView() {
        eventWord = findViewById(R.id.event);
        ensure = findViewById(R.id.ensure);
        cancel = findViewById(R.id.cancel);
        back = findViewById(R.id.back);
        timePick1=(TimePicker)findViewById(R.id.timePic1);
        timePick2=(TimePicker)findViewById(R.id.timePic2);
    }

    private void listener() {
        final String[] endTime = {null};
        final String[] beginTime = {null};
        final String[] event = {null};

        //时间复位
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                timePick1.setIs24HourView(true);
                timePick2.setIs24HourView(true);
                timePick1.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePick1.setCurrentMinute(calendar.get(Calendar.MINUTE));
                timePick2.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePick2.setCurrentMinute(calendar.get(Calendar.MINUTE));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetActivity.this.finish();
            }
        });
        timePick1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int h1=timePick1.getCurrentHour();
                int m1=timePick1.getCurrentMinute();

                beginTime[0] = h1 + ":" + m1;
                Log.e("测试",""+beginTime[0]);
            }
        });
        timePick2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int h2=timePick2.getCurrentHour();
                int m2=timePick2.getCurrentMinute();
                endTime[0] = h2 +":" + m2;
                Log.e("测试",""+endTime[0]);
            }
        });

        ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String calendar = intent.getStringExtra("calendar");
                event[0] = String.valueOf(eventWord.getText());
                String allInfo = event[0]+"!!"+beginTime[0]+"!!"+endTime[0]+"!!"+ ConfigUtil.USER_NAME+"!!"+calendar;
                saveSetting(ConfigUtil.SERVER_ADDR +"SavePlaneServlet",allInfo);
                Intent i = new Intent(SetActivity.this, MainActivity.class);
                startActivity(i);
                SetActivity.this.finish();
            }
        });
    }

    private void saveSetting(final String s, final String allInfo) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Log.e("提示","进入上传商品页面");
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");

                    OutputStream outputStream = conn.getOutputStream();
                    Log.e("提示","进入上传商品页面");
                    outputStream.write(allInfo.getBytes());
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


}
