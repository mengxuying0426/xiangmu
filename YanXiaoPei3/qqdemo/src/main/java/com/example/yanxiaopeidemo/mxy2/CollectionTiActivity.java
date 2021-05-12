package com.example.yanxiaopeidemo.mxy2;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.utils.StringUtils;

public class CollectionTiActivity extends Activity {
    private int keynum;
    private String stem;
    private String opta;
    private String optb;
    private String optc;
    private String optd;
    private String correct;
    private String analysis;
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
    private boolean keyNumA = false;
    private boolean keyNumB = false;
    private boolean keyNumC = false;
    private boolean keyNumD = false;
    private int keyNum = 0;
    private ImageView ivOpt1;
    private ImageView ivOpt2;
    private ImageView ivOpt3;
    private ImageView ivOpt4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_tidetail);

        Intent result = getIntent();
        String fenlei = result.getStringExtra("fenlei");
        Log.i("跳转练习",fenlei);
        String[] arr = fenlei.split("&");
        keynum = Integer.parseInt(arr[0]);
        stem = arr[1];
        opta = arr[2];
        optb = arr[3];
        optc = arr[4];
        optd = arr[5];
        correct = arr[6];
        analysis = arr[7];

        findViews();
        setListener();
    }
    private void setListener() {
        MyListener listener = new MyListener();
        llOptA.setOnClickListener(listener);
        llOptB.setOnClickListener(listener);
        llOptC.setOnClickListener(listener);
        llOptD.setOnClickListener(listener);
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
        ivOpt1 = findViewById(R.id.iv_opt1);
        ivOpt2 = findViewById(R.id.iv_opt2);
        ivOpt3 = findViewById(R.id.iv_opt3);
        ivOpt4 = findViewById(R.id.iv_opt4);
        String opt = null;
        if(keynum>1){
            opt = "(多选题)";
        }else {
            opt = "(单选题)";
        }
        SpannableString highlightText = StringUtils.highlight(getApplicationContext(), opt+stem, opt, "#4169E1", 1, 1);
        tvStem.setText(highlightText);
        tvOptA.setText(opta);
        tvOptB.setText(optb);
        tvOptC.setText(optc);
        tvOptD.setText(optd);


    }
    class MyListener implements View.OnClickListener{

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_optA:
                    if(keyNumA==false){
                        keyNumA=true;
                        tvOptA.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumA=false;
                        tvOptA.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==keynum){
                        Log.i("mxy","点击完毕A");
                        judgmentOpt();
                    }
                    Log.i("mxy","点击了A");
                    break;
                case R.id.ll_optB:
                    if(keyNumB==false){
                        keyNumB=true;
                        tvOptB.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumB=false;
                        tvOptB.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==keynum){
                        Log.i("mxy","点击完毕B");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optC:
                    if(keyNumC==false){
                        keyNumC=true;
                        tvOptC.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumC=false;
                        tvOptC.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==keynum){
                        Log.i("mxy","点击完毕C");
                        judgmentOpt();
                    }
                    break;
                case R.id.ll_optD:
                    if(keyNumD==false){
                        keyNumD=true;
                        tvOptD.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorSel));
                        keyNum++;
                    }else{
                        keyNumD=false;
                        tvOptD.setTextColor(CollectionTiActivity.this.getResources().getColor(R.color.colorNoSel));
                        keyNum--;
                    }
                    if(keyNum==keynum){
                        Log.i("mxy","点击完毕D");
                        judgmentOpt();
                    }
                    break;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void judgmentOpt() {
        switch (correct){
            case "A":
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "B":
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "C":
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "D":
                Log.i("mxy","正确答案是D");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "AB":
                Log.i("mxy","正确答案是AB");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "AC":
                Log.i("mxy","正确答案是AC");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "AD":
                Log.i("mxy","正确答案是AD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "ABC":
                Log.i("mxy","正确答案是ABC");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "ABD":
                Log.i("mxy","正确答案是ABD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "ABCD":
                Log.i("mxy","正确答案是ABCD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "BC":
                Log.i("mxy","正确答案是BC");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "BD":
                Log.i("mxy","正确答案是BD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "BCD":
                Log.i("mxy","正确答案是BCD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
            case "CD":
                Log.i("mxy","正确答案是CD");
                ivOpt1.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt2.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.no,null));
                ivOpt3.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                ivOpt4.setImageDrawable(CollectionTiActivity.this.getResources().getDrawable(R.drawable.yes,null));
                tvAnTitle.setText("解析：");
                tvAnalysis.setText(analysis);
                break;
        }

    }
}
