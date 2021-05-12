package com.example.yanxiaopeidemo.mxy2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.DCInfo;
import com.example.yanxiaopeidemo.beans.DefineChoice;
import com.example.yanxiaopeidemo.utils.ScaleAnimatorUtils;
import com.example.yanxiaopeidemo.utils.ServerConfig;
import com.example.yanxiaopeidemo.utils.StringUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DfActivity extends Activity {
    private ViewPager mViewPager;
    private int mPosition;
    private List<View> myViewList;
    private Handler myHandler;
    private DCInfo dcInfo;
    private List<DefineChoice> defineChoices;
    private CommonPopupWindow window;
    private CommonPopupWindow.LayoutGravity layoutGravity;
    private Map<Integer,Boolean> collect = new HashMap<>();
    private Map<Integer,Boolean> cuotiset = new HashMap<>();
    private TextView tvStem;
    private TextView tvOptA;
    private TextView tvOptB;
    private TextView tvOptC;
    private TextView tvOptD;
    private ImageView ivImg;
    private ImageView ivMore;
    private ImageView ivShoucang;
    private ImageView ivCuoti;
    private LinearLayout llShoucang;
    private LinearLayout llCuoti;
    private TextView tvShoucang;
    private TextView tvCuoti;
    private TextView tvCheck;
    private String Correct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exercise);
        myHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        dcInfo = (DCInfo) msg.obj;
                        if (null != dcInfo) {
                            defineChoices = dcInfo.getDefineChoices();
                            Log.i("handle", "" + defineChoices.size());
                            mViewPager = findViewById(R.id.in_viewpager);
                            myViewList = new ArrayList<>();
                            LayoutInflater layoutInflater = getLayoutInflater().from(DfActivity.this);
                            Log.i("handle",""+defineChoices.size());
                            for(int j = 0;j<defineChoices.size();j++){
                                if(defineChoices.get(j).getTista()==1){
                                    View view1 = layoutInflater.inflate(R.layout.define_exersize, null,false);
                                    myViewList.add(view1);
                                }else {
                                    View view1 = layoutInflater.inflate(R.layout.define_img, null,false);
                                    myViewList.add(view1);
                                }

                            }
                            mViewPager.setAdapter(new MyPager(myViewList));
                            mViewPager.setOffscreenPageLimit(defineChoices.size());
                            mPosition = mViewPager.getCurrentItem();
                            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    Log.i("PageChange-Select", "position:" + position);
                                    Log.i("当前页seclect",""+mPosition);
                                    mPosition=position;
                                    for(int i =0;i<defineChoices.size();i++){
                                        //初始化
                                        if(!collect.containsKey(i)){
                                            collect.put(i,false);
                                        }
                                        if(!cuotiset.containsKey(i)){
                                            cuotiset.put(i,false);

                                        }
                                    }

                                    if(collect.get(mPosition)==true){
                                        Log.i("收藏之前被选中","true");
                                        ivShoucang.setImageResource(R.drawable.shoucangok);
                                    }else if(collect.get(mPosition)==false) {
                                        Log.i("收藏之前没被选中","false");
                                        ivShoucang.setImageResource(R.drawable.shoucang);
                                    }
                                    if(cuotiset.get(mPosition)==true){
                                        Log.i("cuoti之前被选中","true");
                                        ivCuoti.setImageResource(R.drawable.cuotiok);
                                    }else if(cuotiset.get(mPosition)==false) {
                                        Log.i("错题之前没被选中","false");
                                        ivCuoti.setImageResource(R.drawable.cuoti);
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    switch (state) {
                                        case ViewPager.SCROLL_STATE_IDLE:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_IDLE(滑动闲置或滑动结束)");


                                            break;
                                        case ViewPager.SCROLL_STATE_DRAGGING:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_DRAGGING(手势滑动中)");
                                            Log.i("当前页",""+mPosition);

                                            if(mPosition<defineChoices.size()-1){
                                                View view = myViewList.get(mPosition+1);
                                                initView(view,defineChoices.get(mPosition+1));
                                                setViewListener();
                                            }
                                            break;
                                        case ViewPager.SCROLL_STATE_SETTLING:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_SETTLING(代码执行滑动中)");
                                            break;
                                        default:
                                            break;
                                    }

                                }
                            });
                            View view = myViewList.get(mPosition);
                            initView(view,defineChoices.get(mPosition));
                            setViewListener();
                            //更多（包含收藏和加入错题集）
                            window = new CommonPopupWindow(DfActivity.this,R.layout.popup_gravity,500, ViewGroup.LayoutParams.WRAP_CONTENT) {
                                @Override
                                protected void initView() {
                                    Log.i("执行",""+mPosition);
                                    View view=getContentView();
                                    tvCuoti = view.findViewById(R.id.tv_cuoti);
                                    tvShoucang = view.findViewById(R.id.tv_shoucang);
                                    ivShoucang = view.findViewById(R.id.iv_shoucang);
                                    ivCuoti = view.findViewById(R.id.iv_cuoti);
                                    llCuoti = view.findViewById(R.id.ll_cuoti);
                                    llShoucang = view.findViewById(R.id.ll_shoucang);
                                    ivShoucang.setSelected(false);
                                    ivCuoti.setSelected(false);
                                    llShoucang.setOnClickListener(new MyListener());
                                    llCuoti.setOnClickListener(new MyListener());
                                }

                                @Override
                                protected void initEvent() {

                                }
                            };

                            layoutGravity = new CommonPopupWindow.LayoutGravity(CommonPopupWindow.LayoutGravity.CENTER_HORI);
                        }
                        break;
                }
            }
        };

        Intent result = getIntent();
        String fenlei = result.getStringExtra("fenlei");
        Log.i("跳转练习DFActivity", fenlei);
        String[] arr = fenlei.split("&");
        String kemu = arr[0];
        int num = Integer.parseInt(arr[1]);
        int kemusta = Integer.parseInt(arr[2]);

        String url2 = ServerConfig.SEVER_ADDR + "/"
                + ServerConfig.NET_HOME + "/downloadsZiChoices";
        Log.i("mxy", "自定义的");
        downloadZiChoice(url2, kemu);

    }

    private void setViewListener() {
        MyListener listener = new MyListener();
        ivMore.setOnClickListener(listener);
        tvCheck.setOnClickListener(listener);
    }

    private void initView(View view,DefineChoice defineChoice) {
        ivMore = findViewById(R.id.iv_more);
        Correct = defineChoice.getAnswer();
        if(defineChoice.getTista()==1){
            String opt = null;
            if(defineChoice.getAnswer().length()>1){
                opt = "(多选题)";
            }else {
                opt = "(单选题)";
            }

            tvStem = view.findViewById(R.id.tv_stem);
            tvOptA = view.findViewById(R.id.tv_optA);
            tvOptB = view.findViewById(R.id.tv_optB);
            tvOptC = view.findViewById(R.id.tv_optC);
            tvOptD = view.findViewById(R.id.tv_optD);
            tvCheck = view.findViewById(R.id.tv_check);
            SpannableString highlightText = StringUtils.highlight(this, opt+defineChoice.getStem(), opt, "#4169E1", 1, 1);
            tvStem.setText(highlightText);
            Correct = defineChoice.getAnswer();
            tvOptA.setText(defineChoice.getOpta());
            tvOptB.setText(defineChoice.getOptb());
            tvOptC.setText(defineChoice.getOptc());
            tvOptD.setText(defineChoice.getOptd());

        }else {
            ivImg = view.findViewById(R.id.iv_img);
            //获取本地files目录下的文件路径
            String fFile = getFilesDir().getAbsolutePath()+"/"+defineChoice.getImg();
            Bitmap img = BitmapFactory.decodeFile(fFile);
            ivImg.setImageBitmap(img);
            tvCheck = view.findViewById(R.id.tv_check);
            Correct = defineChoice.getAnswer();
        }

    }

    /**
     * 在自定义题库中加载题目
     * @param url2
     */
    private void downloadZiChoice(final String url2, final String kemu) {
        new Thread(){
            @Override
            public void run() {

                try {
                    URL url = new URL(url2);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream out = connection.getOutputStream();
                    String keyValue = ""+kemu;
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();

                    //获取服务器返回数据判断是否操作成功
                    InputStream in = connection.getInputStream();
                    //读数据（Json串),循环读写方式
                    byte[] bytes = new byte[131072];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while((len = in.read(bytes,0,bytes.length))!=-1){
                        buffer.append(new String(bytes, 0, len));
                    }
                    String result = buffer.toString();
                    Log.i("mxy","json串+Exersize："+result);
                    in.close();
                    //先将Json串解析成外部DCInfo对象
                    //创建DCInfo对象和DefineChoice集合对象
                    DCInfo dcInfo = new DCInfo();
                    List<DefineChoice> defineChoices = new ArrayList<>();
                    //创建外层JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("zichoices");
                    //遍历JSONArray对象,解析其中的每个元素
                    for(int i = 0;i<jArray.length();i++){
                        //获取当前的JSONObject对象
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //获取当前元素中的属性信息
                        int tista = JChoice.getInt("tista");
                        if(tista==1){
                            String username = JChoice.getString("username");
                            String stem = JChoice.getString("stem");
                            String correct = JChoice.getString("correct");
                            String opta = JChoice.getString("opta");
                            String optb = JChoice.getString("optb");
                            String optc = JChoice.getString("optc");
                            String optd = JChoice.getString("optd");
                            String kemu = JChoice.getString("kemu");
                            int id = JChoice.getInt("id");
                            DefineChoice defineChoice = new DefineChoice(username,stem,correct,opta,optb,optc,optd,kemu,id,tista);
                            defineChoices.add(defineChoice);
                        }else {
                            String username = JChoice.getString("username");
                            String correct = JChoice.getString("correct");
                            String img = JChoice.getString("img");
                            String kemu = JChoice.getString("kemu");
                            int id = JChoice.getInt("id");
                            DefineChoice defineChoice = new DefineChoice(username,correct,img,kemu,id,tista);
                            defineChoices.add(defineChoice);
                        }
                    }
                    dcInfo.setDefineChoices(defineChoices);
                    //2. 通过发送Message对象，将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = dcInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_check:
                    tvCheck.setText(Correct);
                    break;
                case R.id.iv_more:
                    window.showBashOfAnchor(ivMore,layoutGravity,0,0);
                    break;
                case R.id.ll_shoucang:
                    if(collect.get(mPosition)==false) {
                        ivShoucang.setImageResource(R.drawable.shoucangok);
                        ivShoucang.setSelected(true);
                        ScaleAnimatorUtils.setScalse(ivShoucang);
                        collect.put(mPosition, true);
//                        shoucangCaozuo(1, urlShoucang);
                        Toast.makeText(DfActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        ivShoucang.setImageResource(R.drawable.shoucang);
                        ivShoucang.setSelected(false);
                        collect.put(mPosition, false);
                        Log.i("当前取消收藏页",""+mPosition);
//                        shoucangCaozuo(2, urlShoucang);
                        Toast.makeText(DfActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    }

                    Log.i("mxy收藏posion",""+mPosition);
                    break;
                case R.id.ll_cuoti:
                    if(cuotiset.get(mPosition)==false) {
                        ivCuoti.setImageResource(R.drawable.cuotiok);
                        ivCuoti.setSelected(true);
                        ScaleAnimatorUtils.setScalse(ivCuoti);
                        cuotiset.put(mPosition, true);
//                        cuotiCaozuo(1, urlCuoti);
                        Toast.makeText(DfActivity.this, "添加错题成功", Toast.LENGTH_SHORT).show();
                    } else {
                        ivCuoti.setImageResource(R.drawable.cuoti);
                        ivCuoti.setSelected(false);
                        cuotiset.put(mPosition, false);
                        Log.i("当前取消添加页",""+mPosition);
//                        cuotiCaozuo(2, urlCuoti);
                        Toast.makeText(DfActivity.this, "取消添加错题", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
