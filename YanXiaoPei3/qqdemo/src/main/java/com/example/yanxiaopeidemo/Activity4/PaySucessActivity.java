package com.example.yanxiaopeidemo.Activity4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yanxiaopeidemo.R;

public class PaySucessActivity extends AppCompatActivity {
    private TextView text,cor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_sucess);
        text = findViewById(R.id.suc);
        cor =findViewById(R.id.yes);
        Intent request = getIntent();
        String name = request.getStringExtra("name");
        text.setText(name);
        cor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent response = new Intent();
                response.putExtra("memober","已开通会员");
                setResult(200,response);
                //结束当前的NewActivity
                finish();
            }
        });


    }
}
