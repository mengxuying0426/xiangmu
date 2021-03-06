package com.example.yanxiaopeidemo.mxy2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.ChoiceInfo;
import com.example.yanxiaopeidemo.beans.SingleChoice;
import com.example.yanxiaopeidemo.utils.ScaleAnimatorUtils;
import com.example.yanxiaopeidemo.utils.ServerConfig;
import com.example.yanxiaopeidemo.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseActivity extends Activity {
    private ViewPager mViewPager;
    private int mPosition;
    private String currentOpt;
    private TextView tvStem;
    private TextView tvOptA;
    private TextView tvOptB;
    private TextView tvOptC;
    private TextView tvOptD;
    private TextView tvAnTitle;
    private TextView tvAnalysis;
    private LinearLayout llOptA;
    private LinearLayout llOptB;
    private LinearLayout llOptC;
    private LinearLayout llOptD;
    private ImageView ivMore;
    private ImageView ivOpt1;
    private ImageView ivOpt2;
    private ImageView ivOpt3;
    private ImageView ivOpt4;
    private ImageView ivShoucang;
    private ImageView ivCuoti;
    private LinearLayout llShoucang;
    private LinearLayout llCuoti;
    private List<SingleChoice> singleChoices;
    private List<View> myViewList;
    private CommonPopupWindow window;
    private TextView tvShoucang;
    private TextView tvCuoti;
    private CommonPopupWindow.LayoutGravity layoutGravity;
    private int keyNum = 0;
    private boolean keyNumA = false;
    private boolean keyNumB = false;
    private boolean keyNumC = false;
    private boolean keyNumD = false;
    private Handler myHandler;
    private ChoiceInfo choiceInfo;
    private Map<Integer,Boolean> collect = new HashMap<>();
    private Map<Integer,Boolean> cuotiset = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_exercise);
        myHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        choiceInfo = (ChoiceInfo) msg.obj;
                        if (null != choiceInfo) {
                            singleChoices = choiceInfo.getSingleChoices();
                            Log.i("handle", "" + singleChoices.size());
                            mViewPager = findViewById(R.id.in_viewpager);
                            myViewList = new ArrayList<>();
                            LayoutInflater layoutInflater = getLayoutInflater().from(ExerciseActivity.this);
                            Log.i("handle",""+singleChoices.size());
                            for(int j = 0;j<singleChoices.size();j++){
                                View view1 = layoutInflater.inflate(R.layout.exercise_first, null,false);
                                myViewList.add(view1);
                            }
                            mViewPager.setAdapter(new MyPager(myViewList));
                            mViewPager.setOffscreenPageLimit(singleChoices.size());
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
                                    for(int i =0;i<singleChoices.size();i++){
                                        //?????????
                                        if(!collect.containsKey(i)){
                                            if(singleChoices.get(mPosition).getIscollect()==0){
                                                collect.put(i,false);
                                                Log.i("??????????????????collect","yes"+i);
                                            }else {
                                                collect.put(i,true);
                                            }

                                        }
                                        if(!cuotiset.containsKey(i)){
                                            if(singleChoices.get(mPosition).getIscuoti()==0){
                                                cuotiset.put(i,false);
                                                Log.i("??????????????????cuotiset","yes"+i);
                                            }else {
                                                collect.put(i,true);
                                            }

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

                                            if(mPosition<singleChoices.size()-1){
                                                View view = myViewList.get(mPosition+1);
                                                initView(view,singleChoices.get(mPosition+1));
                                                keyNum=0;
                                                keyNumA=false;
                                                keyNumB=false;
                                                keyNumC=false;
                                                keyNumD=false;
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
                            initView(view,singleChoices.get(mPosition));
                            setViewListener();
                            //??????????????????????????????????????????
                            window = new CommonPopupWindow(ExerciseActivity.this,R.layout.popup_gravity,500, ViewGroup.LayoutParams.WRAP_CONTENT) {
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
        Log.i("????????????", fenlei);
        String[] arr = fenlei.split("&");
        String kemu = arr[0];
        int num = Integer.parseInt(arr[1]);
        int kemusta = Integer.parseInt(arr[2]);
        collect.put(0,false);
        cuotiset.put(0,false);
        if (kemusta == 1) {
            String url1 = ServerConfig.SEVER_ADDR + "/"
                    + ServerConfig.NET_HOME + "/downloadsMoChoices";
            Log.i("mxy", "?????????" + kemu);
            downloadMoChoice(url1, kemu);
        } else {
            String url2 = ServerConfig.SEVER_ADDR + "/"
                    + ServerConfig.NET_HOME + "/downloadsZiChoices";
            Log.i("mxy", "????????????");
            downloadZiChoice(url2, kemu);
        }
    }

    /**
     * ?????????????????????????????????
     * @param url2
     */
    private void downloadZiChoice(String url2,String kemu) {
        new Thread(){
            @Override
            public void run() {

            }
        }.start();
    }

    /**
     * ??????????????????????????????
     * @param url1
     */
    private void downloadMoChoice(final String url1, final String kemu) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(url1);
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
//                    byte[] bytes = new byte[131072];
//                    StringBuffer buffer = new StringBuffer();
//                    int len = -1;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    while((len = in.read(bytes,0,bytes.length))!=-1){
//                        buffer.append(new String(bytes, 0, len));
//                    }
                    String result = reader.readLine();
                    Log.i("mxy","json???+Exersize???"+result);
                    in.close();

                    //??????Json??????????????????ChoiceInfo??????
                    //??????ChoiceInfo?????????SingleChoice????????????
                    ChoiceInfo choiceInfo = new ChoiceInfo();
                    List<SingleChoice> singleChoices = new ArrayList<>();
                    //????????????JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("choices");
                    //??????JSONArray??????,???????????????????????????
                    for(int i = 0;i<jArray.length();i++){
                        //???????????????JSONObject??????
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //????????????????????????????????????
                        String username = JChoice.getString("username");
                        String kemu = JChoice.getString("kemu");
                        int tiid = JChoice.getInt("tiid");
                        String stem = JChoice.getString("stem");
                        String opta = JChoice.getString("opta");
                        String optb = JChoice.getString("optb");
                        String optc = JChoice.getString("optc");
                        String optd = JChoice.getString("optd");
                        String correct = JChoice.getString("correct");
                        int keynum = JChoice.getInt("keynum");
                        String analysis = JChoice.getString("analysis");
                        int motista = JChoice.getInt("motista");
                        int iscollect = JChoice.getInt("iscollect");
                        int iscuoti = JChoice.getInt("iscuoti");
                        SingleChoice singleChoice = new SingleChoice(username,kemu,tiid,stem,opta,optb,optc,optd,correct,keynum,analysis,motista);
                        singleChoices.add(singleChoice);
                    }
                    choiceInfo.setSingleChoices(singleChoices);
                    //2. ????????????Message??????????????????????????????
                    //??????Message??????
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 1;
                    msg.obj = choiceInfo;
                    //??????Message??????
                    myHandler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void setViewListener() {
        MyListener listener = new MyListener();
        llOptA.setOnClickListener(listener);
        llOptB.setOnClickListener(listener);
        llOptC.setOnClickListener(listener);
        llOptD.setOnClickListener(listener);
        ivMore.setOnClickListener(listener);
    }

    private void initView(View view,SingleChoice singleChoice) {
        tvStem = view.findViewById(R.id.tv_stem);
        tvOptA = view.findViewById(R.id.tv_optA);
        tvOptB = view.findViewById(R.id.tv_optB);
        tvOptC = view.findViewById(R.id.tv_optC);
        tvOptD = view.findViewById(R.id.tv_optD);
        llOptA = view.findViewById(R.id.ll_optA);
        llOptB = view.findViewById(R.id.ll_optB);
        llOptC = view.findViewById(R.id.ll_optC);
        llOptD = view.findViewById(R.id.ll_optD);
        ivMore = findViewById(R.id.iv_more);
        ivOpt1 = view.findViewById(R.id.iv_opt1);
        ivOpt2 = view.findViewById(R.id.iv_opt2);
        ivOpt3 = view.findViewById(R.id.iv_opt3);
        ivOpt4 = view.findViewById(R.id.iv_opt4);
//        ivCuoti = view.findViewById(R.id.iv_cuoti);
//        ivShoucang = view.findViewById(R.id.iv_shoucang);
        tvAnTitle = view.findViewById(R.id.tv_antitle);
        tvAnalysis = view.findViewById(R.id.tv_analysis);
        currentOpt = singleChoice.getCorrect();
        Log.i("cu",currentOpt);
        String opt = null;
        if(singleChoice.getKeyNum()>1){
            opt = "(?????????)";

        }else {
            opt = "(?????????)";
        }
        SpannableString highlightText = StringUtils.highlight(this, opt+singleChoice.getStem(), opt, "#4169E1", 1, 1);
        tvStem.setText(highlightText);
        tvOptA.setText(singleChoice.getOptionA());
        tvOptB.setText(singleChoice.getOptionB());
        tvOptC.setText(singleChoice.getOptionC());
        tvOptD.setText(singleChoice.getOptionD());

    }

    class MyListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            String urlShoucang = ServerConfig.SEVER_ADDR + "/"
                    + ServerConfig.NET_HOME + "/shoucangcaozuo";
            String urlCuoti = ServerConfig.SEVER_ADDR + "/"
                    + ServerConfig.NET_HOME + "/cuoticaozuo";
            switch (v.getId()){
                case R.id.ll_optA:
                    if(keyNumA==false){
                        keyNumA=true;
                        tvOptA.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumA=false;
                        tvOptA.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==singleChoices.get(mPosition).getKeyNum()){
                        Log.i("mxy","????????????A");
                        judgmentOpt();
                    }
                    Log.i("mxy","?????????A");
                    break;
                case R.id.ll_optB:
                    if(keyNumB==false){
                        keyNumB=true;
                        tvOptB.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumB=false;
                        tvOptB.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==singleChoices.get(mPosition).getKeyNum()){
                        Log.i("mxy","????????????B");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optC:
                    if(keyNumC==false){
                        keyNumC=true;
                        tvOptC.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumC=false;
                        tvOptC.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==singleChoices.get(mPosition).getKeyNum()){
                        Log.i("mxy","????????????C");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optD:
                    if(keyNumD==false){
                        keyNumD=true;
                        tvOptD.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumD=false;
                        tvOptD.setTextColor(ExerciseActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==singleChoices.get(mPosition).getKeyNum()){
                        Log.i("mxy","????????????D");
                        judgmentOpt();
                    }
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
                        shoucangCaozuo(1, urlShoucang);
                        Toast.makeText(ExerciseActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        ivShoucang.setImageResource(R.drawable.shoucang);
                        ivShoucang.setSelected(false);
                        collect.put(mPosition, false);
                        Log.i("?????????????????????",""+mPosition);
                        shoucangCaozuo(2, urlShoucang);
                        Toast.makeText(ExerciseActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                    }

                    Log.i("mxy??????posion",""+mPosition);
                    break;
                case R.id.ll_cuoti:
                    if(cuotiset.get(mPosition)==false) {
                        ivCuoti.setImageResource(R.drawable.cuotiok);
                        ivCuoti.setSelected(true);
                        ScaleAnimatorUtils.setScalse(ivCuoti);
                        cuotiset.put(mPosition, true);
                        cuotiCaozuo(1, urlCuoti);
                        Toast.makeText(ExerciseActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    } else {
                        ivCuoti.setImageResource(R.drawable.cuoti);
                        ivCuoti.setSelected(false);
                        cuotiset.put(mPosition, false);
                        Log.i("?????????????????????",""+mPosition);
                        cuotiCaozuo(2, urlCuoti);
                        Toast.makeText(ExerciseActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    }

    /**
     * ????????????
     * @param i
     * @param urlCuoti
     */
    private void cuotiCaozuo(final int i, final String urlCuoti) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlCuoti);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    //??????????????????????????????POST
                    connection.setRequestMethod("POST");
                    OutputStream out = connection.getOutputStream();
                    SingleChoice singleChoice = singleChoices.get(mPosition);
                    String keyValue = i+"&"+singleChoice.getTiid()+"&"+singleChoice.getMotista()+"&"+singleChoice.getKemu()+"&"+singleChoice.getStem()+"&"+singleChoice.getOptionA()
                            +"&"+singleChoice.getOptionB()+"&"+singleChoice.getOptionC()+"&"+singleChoice.getOptionD()+"&"+singleChoice.getCorrect()+"&"+singleChoice.getAnalysis()+"&"+
                            singleChoice.getKeyNum();
                    Log.i("keyvalue:",keyValue);
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();

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

    /**
     * ????????????
     * @param i
     * @param urlShoucang
     */
    private void shoucangCaozuo(final int i, final String urlShoucang) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlShoucang);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    //??????????????????????????????POST
                    connection.setRequestMethod("POST");
                    OutputStream out = connection.getOutputStream();
                    String keyValue = i+"&"+singleChoices.get(mPosition).getTiid()+"&"+singleChoices.get(mPosition).getMotista();
                    Log.i("keyvalue:",keyValue);
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();


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
    private void judgmentOpt() {
        Log.i("judgment",""+currentOpt);
        switch (currentOpt){
            case "A":
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "B":
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "C":
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "D":
                Log.i("mxy","???????????????D");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "AB":
                Log.i("mxy","???????????????AB");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "AC":
                Log.i("mxy","???????????????AC");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "AD":
                Log.i("mxy","???????????????AD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "ABC":
                Log.i("mxy","???????????????ABC");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "ABD":
                Log.i("mxy","???????????????ABD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "ABCD":
                Log.i("mxy","???????????????ABCD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "BC":
                Log.i("mxy","???????????????BC");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "BD":
                Log.i("mxy","???????????????BD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "BCD":
                Log.i("mxy","???????????????BCD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
            case "CD":
                Log.i("mxy","???????????????CD");
                ivOpt1.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(ExerciseActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(singleChoices.get(mPosition).getAnalysis());
                break;
        }

    }


}
