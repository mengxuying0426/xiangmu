package com.example.yanxiaopeidemo.menu;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.kd.easybarrage.Barrage;
import com.kd.easybarrage.BarrageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonalWish extends AppCompatActivity {
    private BarrageView barrageView;
    private Button send;
    private List<Barrage> mBarrages = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    String s=(String)msg.obj;
                    String str[]=s.split("##");

                    if(str[0].equals("true")) {
                        Toast.makeText(PersonalWish.this, "已通过审核", Toast.LENGTH_LONG).show();
                        barrageView.addBarrage(new Barrage(str[1], R.color.white, Color.argb(250,235,115,140)));
                    }else{
                        Toast.makeText(PersonalWish.this, "未通过审核", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 2:
                    String s2=(String)msg.obj;
                    String str2[]=s2.split("##");
                    for(int i=0;i<str2.length;i++){
                        barrageView.addBarrage(new Barrage(str2[i], R.color.white,Color.argb(250,235,115,140)));
                    }
                    break;

            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_wish_layout);
        barrageView = findViewById(R.id.barrageView);
        barrageView.setBarrages(mBarrages);
        findViewById(R.id.motive_back).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        init();
        showWishs();
        setListener();
    }

    private void setListener() {
        MyClickListener myClickListener=new MyClickListener();
        send.setOnClickListener(myClickListener);
    }

    private void init() {
        send=findViewById(R.id.send);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        barrageView.destroy();
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.send:

                    AlertDialog.Builder builder = new AlertDialog.Builder(PersonalWish.this);
                    LayoutInflater inflater=LayoutInflater.from(PersonalWish.this);
                    final View v=inflater.inflate(R.layout.dialog_layout,null);
                    final Dialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setContentView(v);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                    Button btnSure=v.findViewById(R.id.btn_sure);
                    final EditText etWish=v.findViewById(R.id.et_wish);
                    btnSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String wish=etWish.getText().toString();
                            addWish(wish);

                            dialog.dismiss();
                        }
                    });
                    barrageView.addBarrage(new Barrage(etWish.getText().toString(), R.color.white, Color.argb(250,235,115,140)));
                    break;

            }
        }
    }
    private void showWishs() {
        new Thread(){
            @Override
            public void run() {
                URL url = null;
                String str;
                try {
                    url = new URL(ConfigUtil.SERVER_ADDR + "ShowWishesServlet");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    //获取网络输出流
                    OutputStream out = conn.getOutputStream();
                    //必须要获取网络输入流，保证客户端和服务端建立连接
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    //读取数据
                    str= reader.readLine();
                    Log.e("str:",str);

                    System.out.println("啦啦啦啦："+str);
                    in.close();
                    out.close();

                    //使用Message将下载好的图片列表发布给UI界面
                    Message msg = handler.obtainMessage();

                    msg.what = 2;
                    msg.obj = str;
                    handler.sendMessage(msg);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void addWish(final String s) {
        new Thread(){
            @Override
            public void run() {
                URL url = null;
                String str;
                try {
                    url = new URL(ConfigUtil.SERVER_ADDR + "AddWishServlet");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    //获取网络输出流
                    OutputStream out = conn.getOutputStream();
                    String t = s;
                    out.write(t.getBytes("utf-8"));

                    //必须要获取网络输入流，保证客户端和服务端建立连接
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    str= reader.readLine();
                    in.close();
                    out.close();

                    //使用Message将下载好的图片列表发布给UI界面
                    Message msg = handler.obtainMessage();
                    msg.what = 1;
                    msg.obj = str;
                    handler.sendMessage(msg);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
