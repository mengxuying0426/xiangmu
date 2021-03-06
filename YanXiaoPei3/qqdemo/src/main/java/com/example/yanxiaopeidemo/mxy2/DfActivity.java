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
                                    Log.i("?????????seclect",""+mPosition);
                                    mPosition=position;
                                    for(int i =0;i<defineChoices.size();i++){
                                        //?????????
                                        if(!collect.containsKey(i)){
                                            collect.put(i,false);
                                        }
                                        if(!cuotiset.containsKey(i)){
                                            cuotiset.put(i,false);

                                        }
                                    }

                                    if(collect.get(mPosition)==true){
                                        Log.i("?????????????????????","true");
                                        ivShoucang.setImageResource(R.drawable.shoucangok);
                                    }else if(collect.get(mPosition)==false) {
                                        Log.i("????????????????????????","false");
                                        ivShoucang.setImageResource(R.drawable.shoucang);
                                    }
                                    if(cuotiset.get(mPosition)==true){
                                        Log.i("cuoti???????????????","true");
                                        ivCuoti.setImageResource(R.drawable.cuotiok);
                                    }else if(cuotiset.get(mPosition)==false) {
                                        Log.i("????????????????????????","false");
                                        ivCuoti.setImageResource(R.drawable.cuoti);
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    switch (state) {
                                        case ViewPager.SCROLL_STATE_IDLE:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_IDLE(???????????????????????????)");


                                            break;
                                        case ViewPager.SCROLL_STATE_DRAGGING:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_DRAGGING(???????????????)");
                                            Log.i("?????????",""+mPosition);

                                            if(mPosition<defineChoices.size()-1){
                                                View view = myViewList.get(mPosition+1);
                                                initView(view,defineChoices.get(mPosition+1));
                                                setViewListener();
                                            }
                                            break;
                                        case ViewPager.SCROLL_STATE_SETTLING:
                                            Log.i("PageChange-State", "state:SCROLL_STATE_SETTLING(?????????????????????)");
                                            break;
                                        default:
                                            break;
                                    }

                                }
                            });
                            View view = myViewList.get(mPosition);
                            initView(view,defineChoices.get(mPosition));
                            setViewListener();
                            //??????????????????????????????????????????
                            window = new CommonPopupWindow(DfActivity.this,R.layout.popup_gravity,500, ViewGroup.LayoutParams.WRAP_CONTENT) {
                                @Override
                                protected void initView() {
                                    Log.i("??????",""+mPosition);
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
        Log.i("????????????DFActivity", fenlei);
        String[] arr = fenlei.split("&");
        String kemu = arr[0];
        int num = Integer.parseInt(arr[1]);
        int kemusta = Integer.parseInt(arr[2]);

        String url2 = ServerConfig.SEVER_ADDR + "/"
                + ServerConfig.NET_HOME + "/downloadsZiChoices";
        Log.i("mxy", "????????????");
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
                opt = "(?????????)";
            }else {
                opt = "(?????????)";
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
            //????????????files????????????????????????
            String fFile = getFilesDir().getAbsolutePath()+"/"+defineChoice.getImg();
            Bitmap img = BitmapFactory.decodeFile(fFile);
            ivImg.setImageBitmap(img);
            tvCheck = view.findViewById(R.id.tv_check);
            Correct = defineChoice.getAnswer();
        }

    }

    /**
     * ?????????????????????????????????
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

                    //???????????????????????????????????????????????????
                    InputStream in = connection.getInputStream();
                    //????????????Json???),??????????????????
                    byte[] bytes = new byte[131072];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while((len = in.read(bytes,0,bytes.length))!=-1){
                        buffer.append(new String(bytes, 0, len));
                    }
                    String result = buffer.toString();
                    Log.i("mxy","json???+Exersize???"+result);
                    in.close();
                    //??????Json??????????????????DCInfo??????
                    //??????DCInfo?????????DefineChoice????????????
                    DCInfo dcInfo = new DCInfo();
                    List<DefineChoice> defineChoices = new ArrayList<>();
                    //????????????JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("zichoices");
                    //??????JSONArray??????,???????????????????????????
                    for(int i = 0;i<jArray.length();i++){
                        //???????????????JSONObject??????
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //????????????????????????????????????
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
                    //2. ????????????Message??????????????????????????????
                    //??????Message??????
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 1;
                    msg.obj = dcInfo;
                    //??????Message??????
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
                        Toast.makeText(DfActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        ivShoucang.setImageResource(R.drawable.shoucang);
                        ivShoucang.setSelected(false);
                        collect.put(mPosition, false);
                        Log.i("?????????????????????",""+mPosition);
//                        shoucangCaozuo(2, urlShoucang);
                        Toast.makeText(DfActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }

                    Log.i("mxy??????posion",""+mPosition);
                    break;
                case R.id.ll_cuoti:
                    if(cuotiset.get(mPosition)==false) {
                        ivCuoti.setImageResource(R.drawable.cuotiok);
                        ivCuoti.setSelected(true);
                        ScaleAnimatorUtils.setScalse(ivCuoti);
                        cuotiset.put(mPosition, true);
//                        cuotiCaozuo(1, urlCuoti);
                        Toast.makeText(DfActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        ivCuoti.setImageResource(R.drawable.cuoti);
                        ivCuoti.setSelected(false);
                        cuotiset.put(mPosition, false);
                        Log.i("?????????????????????",""+mPosition);
//                        cuotiCaozuo(2, urlCuoti);
                        Toast.makeText(DfActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
