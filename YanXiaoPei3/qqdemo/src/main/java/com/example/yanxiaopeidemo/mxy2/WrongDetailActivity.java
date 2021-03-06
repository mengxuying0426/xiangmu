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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.WTiInfo;
import com.example.yanxiaopeidemo.beans.WrongTi;
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
import java.util.ArrayList;
import java.util.List;

public class WrongDetailActivity extends Activity {
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
    private ImageView ivOpt1;
    private ImageView ivOpt2;
    private ImageView ivOpt3;
    private ImageView ivOpt4;
    private ImageView ivShoucang1;
    private int keyNum = 0;
    private boolean keyNumA = false;
    private boolean keyNumB = false;
    private boolean keyNumC = false;
    private boolean keyNumD = false;
    private int keynum;
    private int id;
    private int tista;
    private Handler myHandler;
    private WTiInfo wTiInfo;
    private List<WrongTi> wrongTis;
    private WrongTi wrongTi;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong_detail);
        myHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        wTiInfo = (WTiInfo) msg.obj;
                        if(null!=wTiInfo){
                            wrongTis = wTiInfo.getWrongTis();
                            wrongTi = wrongTis.get(0);
                            tvStem.setText(wrongTi.getStem());
                            String opt = null;
                            if(wrongTi.getKeynum()>1){
                                opt = "(?????????)";
                            }else {
                                opt = "(?????????)";
                            }
                            SpannableString highlightText = StringUtils.highlight(getApplicationContext(), opt+wrongTi.getStem(), opt, "#4169E1", 1, 1);
                            tvStem.setText(highlightText);
                            tvOptA.setText(wrongTi.getOpta());
                            tvOptB.setText(wrongTi.getOptb());
                            tvOptC.setText(wrongTi.getOptc());
                            tvOptD.setText(wrongTi.getOptd());
                            currentOpt = wrongTi.getCorrect();

                        }
                        break;
                }
            }
        };

        Intent result = getIntent();
        String fenlei = result.getStringExtra("fenlei");
        Log.i("????????????",fenlei);
        String[] arr = fenlei.split("&");
        keynum = Integer.parseInt(arr[0]);
        id = Integer.parseInt(arr[1]);
        tista = Integer.parseInt(arr[2]);
        String url = ServerConfig.SEVER_ADDR + "/"
                + ServerConfig.NET_HOME + "/downloadsdetail";
        downloadDetail(id,url);


//        int p = Integer.parseInt(arr[2]);
//        SingleChoice s1 = new SingleChoice("??????????????????100?????????????????????????????????60??????????????????????????????????????????????????????40????????????????????????????????????????????????????????????(??????????????????????????????10??????),??????????????????????????????????????????????????????120????????????????????????????????????????????????()???","200%","50%","100%","20%","A",1,"A?????????B???C???D??????????????????????????????????????????m'=m/v????????????m????????????120??????100???????????????20??????v?????????????????????????????????????????????v????????????10???????????????40???????????????m'=m/v=20???/10???=200%?????????????????????????????????????????????????????????????????????????????????????????????A???");
//        SingleChoice s2 = new SingleChoice("???????????????:?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????()???","????????????????????????????????????????????????","???????????????????????????????????????????????????????????????","?????????????????????????????????????????????????????????","????????????????????????????????????????????????????????????????????????","B",1,"???????????????????????????????????????????????????????????????????????????????????????1921???3????????????(???)?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????B????????????A???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????; C??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????; D?????????,?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????B???");
//        SingleChoice s3 = new SingleChoice("????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????()???","??????????????????????????????","?????????????????????????????????","???????????????????????????????????????","??????????????????????????????????????????","C",1,"A??????????????????????????????????????????????????????????????????????????????????????????????????????????????????B????????????????????????????????????????????????????????????????????????????????????????????????????????????C????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????D???????????????????????????????????????????????????????????????-?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
//        SingleChoice s4 = new SingleChoice("1914??????1918????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????()???","?????????????????????????????????","?????????????????????????????????????????????","????????????????????????????????????","????????????????????????????????????","B",1,"??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????B??????????????????????????????????????????????????????????????????????????????A??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????C?????????????????????????????????????????????????????????????????????????????????????????????D?????????????????????????????????B???");
//        SingleChoice s5 = new SingleChoice("1905???11????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????()???","?????????????????????????????????","?????????????????????????????????","???????????????????????????","???????????????????????????????????????","AB",2,"A??????????????????????????????????????????????????????????????????; B????????????????????????????????????????????????????????????????????????;C???D?????????CD???????????????????????????????????????????????????????????????????????????AB???");
//        SingleChoice s6 = new SingleChoice("???????????????:?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????()???","????????????????????????????????????????????????","???????????????????????????????????????????????????????????????","?????????????????????????????????????????????????????????","????????????????????????????????????????????????????????????????????????","B",1,"???????????????????????????????????????????????????????????????????????????????????????1921???3????????????(???)?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????B????????????A???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????; C??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????; D?????????,?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????B???");
//        switch (p){
//            case 0:
//                singleChoice=s1;
//            case 1:
//                singleChoice=s2;
//            case 2:
//                singleChoice=s3;
//            case 3:
//                singleChoice = s4;
//            case 4:
//                singleChoice = s5;
//            case 5:
//                singleChoice = s6;
//        }
        findViews();
        setListener();
    }

    /**
     * ??????????????????
     * @param id
     * @param url
     */
    private void downloadDetail(final int id, final String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection)url1.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream out = connection.getOutputStream();
                    String keyValue = ""+id;
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();

                    InputStream in = connection.getInputStream();
                    //????????????Json???),??????????????????
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    while((len = in.read(bytes,0,bytes.length))!=-1){
//                        buffer.append(new String(bytes, 0, len));
//                    }
                    String result = reader.readLine();
                    Log.i("mxy","json???+Exersize???"+result);
                    in.close();

                    //??????Json??????????????????WTiInfo??????
                    //??????WTiInfo?????????WrongTi????????????
                    WTiInfo wTiInfo = new WTiInfo();
                    List<WrongTi> wrongTis = new ArrayList<>();
                    //????????????JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("wrongtid");
                    //??????JSONArray??????,???????????????????????????
                    for(int i = 0;i<jArray.length();i++){
                        //???????????????JSONObject??????
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //????????????????????????????????????
                        int id = JChoice.getInt("id");
                        String username = JChoice.getString("username");
                        String kemu = JChoice.getString("kemu");
                        int tiid = JChoice.getInt("tiid");
                        int tista = JChoice.getInt("tista");
                        String stem = JChoice.getString("stem");
                        String opta = JChoice.getString("opta");
                        String optb = JChoice.getString("optb");
                        String optc = JChoice.getString("optc");
                        String optd = JChoice.getString("optd");
                        String correct = JChoice.getString("correct");
                        int keynum = JChoice.getInt("keynum");
                        String analysis = JChoice.getString("analysis");
                        WrongTi wrongTi1 = new WrongTi(id,username,tiid,tista,kemu,stem,opta,optb,optc,optd,correct,analysis,keynum);
                        wrongTis.add(wrongTi1);
                    }
                    wTiInfo.setWrongTis(wrongTis);
                    //2. ????????????Message??????????????????????????????
                    //??????Message??????
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 1;
                    msg.obj = wTiInfo;
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

    private void setListener() {
        MyListener listener = new MyListener();
        llOptA.setOnClickListener(listener);
        llOptB.setOnClickListener(listener);
        llOptC.setOnClickListener(listener);
        llOptD.setOnClickListener(listener);
        ivShoucang1.setOnClickListener(listener);
    }

    private void findViews() {
        tvStem = findViewById(R.id.tv_stem);
        tvOptA = findViewById(R.id.tv_optA);
        tvOptB = findViewById(R.id.tv_optB);
        tvOptC = findViewById(R.id.tv_optC);
        tvOptD = findViewById(R.id.tv_optD);
        llOptA = findViewById(R.id.ll_optA);
        llOptB = findViewById(R.id.ll_optB);
        llOptC = findViewById(R.id.ll_optC);
        llOptD = findViewById(R.id.ll_optD);
        tvAnalysis = findViewById(R.id.tv_analysis);
        tvAnTitle = findViewById(R.id.tv_antitle);
        ivShoucang1 = findViewById(R.id.iv_shoucang1);
        ivShoucang1.setSelected(false);
        ivOpt1 = findViewById(R.id.iv_opt1);
        ivOpt2 = findViewById(R.id.iv_opt2);
        ivOpt3 = findViewById(R.id.iv_opt3);
        ivOpt4 = findViewById(R.id.iv_opt4);
//        tvOptA.setText(singleChoice.getOptionA());
//        tvOptB.setText(singleChoice.getOptionB());
//        tvOptC.setText(singleChoice.getOptionC());
//        tvOptD.setText(singleChoice.getOptionD());
//        currentOpt = singleChoice.getCorrect();
//        tvStem.setText(singleChoice.getStem());
//        if(singleChoice.getKeyNum()>1){
//            tvOpt.setText("(?????????)");
//        }else {
//            tvOpt.setText("(?????????)");
//        }
    }

    class MyListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            String urlshoucang = ServerConfig.SEVER_ADDR + "/"
                    + ServerConfig.NET_HOME + "/cuoticollection";
            switch (v.getId()){
                case R.id.iv_shoucang1:
                    if(ivShoucang1.isSelected()==false){
                        ivShoucang1.setImageResource(R.drawable.shoucang1);
                        ivShoucang1.setSelected(true);
                        ScaleAnimatorUtils.setScalse(ivShoucang1);
                        shoucangUp(1,urlshoucang,id,tista);
                        Toast.makeText(WrongDetailActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                    }else {
                        ivShoucang1.setImageResource(R.drawable.shoucang1no);
                        ivShoucang1.setSelected(false);
                        shoucangUp(2,urlshoucang,id,tista);
                        Toast.makeText(WrongDetailActivity.this,"????????????",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ll_optA:
                    if(keyNumA==false){
                        keyNumA=true;
                        tvOptA.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumA=false;
                        tvOptA.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==wrongTi.getKeynum()){
                        Log.i("mxy","????????????A");
                        judgmentOpt();
                    }
                    Log.i("mxy","?????????A");
                    break;
                case R.id.ll_optB:
                    if(keyNumB==false){
                        keyNumB=true;
                        tvOptB.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumB=false;
                        tvOptB.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==wrongTi.getKeynum()){
                        Log.i("mxy","????????????B");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optC:
                    if(keyNumC==false){
                        keyNumC=true;
                        tvOptC.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumC=false;
                        tvOptC.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==wrongTi.getKeynum()){
                        Log.i("mxy","????????????C");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optD:
                    if(keyNumD==false){
                        keyNumD=true;
                        tvOptD.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumD=false;
                        tvOptD.setTextColor(WrongDetailActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==wrongTi.getKeynum()){
                        Log.i("mxy","????????????D");
                        judgmentOpt();
                    }
                    break;
            }
        }
    }

    /**
     * ????????????????????????
     * @param urlshoucang
     */
    private void shoucangUp(final int i, final String urlshoucang, final int id, final int tista) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(urlshoucang);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    //??????????????????????????????POST
                    connection.setRequestMethod("POST");
                    OutputStream out = connection.getOutputStream();
                    String keyValue = i+"&"+id+"&"+tista;
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
        switch (currentOpt){
            case "A":
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "B":
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "C":
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "D":
                Log.i("mxy","???????????????D");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "AB":
                Log.i("mxy","???????????????AB");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "AC":
                Log.i("mxy","???????????????AC");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "AD":
                Log.i("mxy","???????????????AD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "ABC":
                Log.i("mxy","???????????????ABC");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "ABD":
                Log.i("mxy","???????????????ABD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "ABCD":
                Log.i("mxy","???????????????ABCD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "BC":
                Log.i("mxy","???????????????BC");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "BD":
                Log.i("mxy","???????????????BD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "BCD":
                Log.i("mxy","???????????????BCD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
            case "CD":
                Log.i("mxy","???????????????CD");
                ivOpt1.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(WrongDetailActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("?????????");
                tvAnalysis.setText(wrongTi.getAnalysis());
                break;
        }

    }
}
