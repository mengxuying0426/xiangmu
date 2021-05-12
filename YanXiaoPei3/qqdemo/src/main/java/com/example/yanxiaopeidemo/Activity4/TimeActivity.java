package com.example.yanxiaopeidemo.Activity4;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.example.yanxiaopeidemo.R;

public class TimeActivity extends AppCompatActivity {
    private Chronometer timer;
    private Button start;
    private Button stop;
    private Button restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        timer = findViewById(R.id.timer);
        start =findViewById(R.id.startT);
        stop = findViewById(R.id.stop);
        restart =findViewById(R.id.restart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.start();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.stop();
            }
        });
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                timer.stop();
            }
        });

    }
}
