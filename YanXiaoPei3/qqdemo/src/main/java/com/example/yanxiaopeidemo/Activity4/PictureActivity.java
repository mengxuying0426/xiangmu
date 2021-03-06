package com.example.yanxiaopeidemo.Activity4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralBasicParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.WordSimple;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.core.os.LocaleListCompat.create;

public class PictureActivity extends Activity {
    private TextView ivPho;
    private Uri uri;
    private TextView text;
    private LinearLayout btnImg;
    private LinearLayout btnText;
    private String[] list;
    private String addName;
    private String option;
    private String imgUrl;
    private EditText correct;
    private String op="";
    private List<Option> students = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    list = (String[]) msg.obj;
                    System.out.println(list);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        upStr(ConfigUtil.SERVER_ADDR+"BankServlet");
        //initData();
        OCR.getInstance(getApplication()).initAccessToken(new OnResultListener() {
            public void onResult(AccessToken result) {
                // ???????????????getApplication()??????AccessToken??????
                String token = result.getAccessToken();
            }
            @Override
            public void onResult(Object o) {
            }
            @Override
            public void onError(OCRError error) {
                // ?????????????????????OCRError??????SDKError??????
            } }, getApplicationContext());
        //   upStr(ConfigUtil.SERVER_ADDR + "BankServlet");
        ivPho =findViewById(R.id.iv_pic);
        btnImg =findViewById(R.id.img);
        btnText =findViewById(R.id.word);
        correct = findViewById(R.id.correct);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                btnImg.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                btnText.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                View bottomView = View.inflate(PictureActivity.this,R.layout.option_list,null);//??????ListView??????
                ListView lvCarIds = (ListView) bottomView.findViewById(R.id.lv_list);//?????????ListView??????
                lvCarIds.setAdapter(new CustomAdapter(getApplicationContext(), list,
                        R.layout.list_item));//ListView???????????????
                lvCarIds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String cor = correct.getText().toString();
                        if(cor!=null&&!cor.equals("")){
                            option=list[i]+" "+imgUrl+" "+ConfigUtil.USER_NAME+" "+cor;
                            System.out.println(option);
                            addOption(ConfigUtil.SERVER_ADDR+"StemServlet");
                            correct.setText("");
                            Toast.makeText(PictureActivity.this,  "????????????", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(PictureActivity.this,  "?????????????????????", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                final  AlertDialog  parkIdsdialog = new AlertDialog.Builder(PictureActivity.this)
                        .setTitle("????????????").setView(bottomView)//???????????????????????????listview???????????????dialog???
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(PictureActivity.this);
                                View view1 = View.inflate(PictureActivity.this, R.layout.add, null);
                                final EditText et = view1.findViewById(R.id.et);

                                Button bu = view1.findViewById(R.id.btn);
                                Button cancel = view1.findViewById(R.id.btn1);
                                alertDialog7
                                        .setTitle("????????????")
                                        .setIcon(R.drawable.add)
                                        .setView(view1)
                                        .create();
                                final AlertDialog show = alertDialog7.show();
                                bu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addName = et.getText().toString();
                                        System.out.println(addName);
                                        addQuestion(ConfigUtil.SERVER_ADDR+"AddServlet");
                                        Toast.makeText(PictureActivity.this, "??????" + et.getText().toString(), Toast.LENGTH_SHORT).show();
                                        show.dismiss();
                                    }

                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        show.dismiss();
                                    }
                                });




                            }
                        }).create();
                AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        parkIdsdialog.dismiss();
                    }
                });
                parkIdsdialog.show();
                parkIdsdialog.getWindow().setBackgroundDrawableResource(R.drawable.circle_icon02);

            }
        });
        btnText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                btnImg.setBackground(getResources().getDrawable(R.drawable.corners_bg1));
                btnText.setBackground(getResources().getDrawable(R.drawable.circle_ic2));
                View bottomView = View.inflate(PictureActivity.this,R.layout.option_list,null);//??????ListView??????
                ListView lvCarIds = (ListView) bottomView.findViewById(R.id.lv_list);//?????????ListView??????
                lvCarIds.setAdapter(new CustomAdapter(getApplicationContext(), list,
                        R.layout.list_item));//ListView???????????????
                lvCarIds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String cor = correct.getText().toString();
                        if(cor!=null&&!cor.equals("")){
                            option= op+cor+"&"+ConfigUtil.USER_NAME+"&"+list[i];
                            System.out.println(option);
                            addOption(ConfigUtil.SERVER_ADDR+"StemTestServlet");
                            Toast.makeText(PictureActivity.this,  "????????????", Toast.LENGTH_SHORT).show();
                            correct.setText("");
                        }else{
                            Toast.makeText(PictureActivity.this,  "?????????????????????", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                final  AlertDialog  parkIdsdialog = new AlertDialog.Builder(PictureActivity.this)
                        .setTitle("????????????").setView(bottomView)//???????????????????????????listview???????????????dialog???
                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setPositiveButton("????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(PictureActivity.this);
                                View view1 = View.inflate(PictureActivity.this, R.layout.add, null);
                                final EditText et = view1.findViewById(R.id.et);
                                Button bu = view1.findViewById(R.id.btn);
                                Button cancel = view1.findViewById(R.id.btn1);
                                alertDialog7
                                        .setTitle("????????????")
                                        .setIcon(R.drawable.add)
                                        .setView(view1)
                                        .create();
                                final AlertDialog show = alertDialog7.show();
                                bu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        addName = et.getText().toString();
                                        System.out.println(addName);
                                        addQuestion(ConfigUtil.SERVER_ADDR+"AddServlet");
                                        Toast.makeText(PictureActivity.this, "??????" + et.getText().toString(), Toast.LENGTH_SHORT).show();
                                        show.dismiss();
                                    }

                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        show.dismiss();
                                    }
                                });




                            }
                        }).create();
                AlertDialog.Builder builder = new AlertDialog.Builder(PictureActivity.this);
                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        parkIdsdialog.dismiss();
                    }
                });
                parkIdsdialog.show();
                parkIdsdialog.getWindow().setBackgroundDrawableResource(R.drawable.circle_ic2);



            }
        });
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File outImage=new File(getExternalCacheDir(),timeStamp+"output_image.jpg");
        try{
            if(outImage.exists())
            {
                outImage.delete();
            }
            outImage.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24)
        {
            uri= FileProvider.getUriForFile(PictureActivity.this,"com.example.qqdemo",outImage);
        }
        else
        {
            uri= Uri.fromFile(outImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,1);

    }



    /**
     * ??????
     */

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("WrongViewCast")
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case 1:
                if(resultCode==RESULT_OK)
                {
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        ivPho.setBackground( new BitmapDrawable(bitmap));
                        ocrNormal(bitmap);
                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    public  File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);//???????????????????????????100????????????????????????????????????????????????baos???
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //????????????????????????
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //????????????
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //?????????
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        Log.d("=-=-=-=-=-", "compressImage: " + file);
        // recycleBitmap(bitmap);
        return file;
    }

    private void ocrNormal(Bitmap bitmap) {
        text=findViewById(R.id.text);
        // ??????????????????????????????
        GeneralBasicParams param = new GeneralBasicParams();
        param.setDetectDirection(true);
        //?????????????????????????????????????????????????????????????????????
        File file;


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);//???????????????????????????100????????????????????????????????????????????????baos???
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //?????????
        String filename = format.format(date);
        file = new File(getFilesDir().getAbsolutePath(), "/"+filename + ".png");
        imgUrl =filename + ".png";
        System.out.println(getFilesDir().getAbsolutePath()+ "/"+filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        param.setImageFile(file);
        // ??????????????????????????????
        OCR.getInstance(getApplication()).recognizeAccurateBasic(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                // ?????????????????????GeneralResult??????
                for (WordSimple wordSimple : result.getWordList()) {
                    // wordSimple?????????????????????
                    WordSimple word = wordSimple;
                    sb.append(word.getWords());
                    System.out.println(word.getWords());
                    if(word.getWords()!=null&&!word.getWords().equals("")){
                        op+=word.getWords()+"&";
                    }

                    sb.append("\n");
                }
                String ocrResult = sb.toString();
                Log.v("4","===================================="+ocrResult);
                // json?????????????????????result.getJsonRes())
                text.setText(ocrResult);
            }

            @Override
            public void onError(OCRError error) {
                // ?????????????????????OCRError??????
                text.setText("?????????");
                Log.v("1","================================================"+error.getLocalizedMessage());
                Log.v("2","================================================"+error.getMessage());
                Log.v("3","================================================"+error.getErrorCode());
            }
        });
    }
    protected void onDestroy() {
        super.onDestroy();
// ??????????????????
        OCR.getInstance(this).release();
    }

    private void upStr(final String s) {
        new Thread(){
            @Override
            public void run() {
                //??????URL???HttpURLconnection????????????????????????
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //??????????????????????????????POST
                    conn.setRequestMethod("POST");
                    //?????????????????????
                    OutputStream out = conn.getOutputStream();
                    //???????????????????????????
                    //?????????????????????
                    // Intent request =getIntent();
                    //sName = request.getStringExtra("name");
                    out.write(ConfigUtil.USER_NAME.getBytes());
                    System.out.println(ConfigUtil.USER_NAME);
                    //????????????????????????????????????????????????????????????????????????
                    InputStream in= conn.getInputStream();
                    out.close();
                    //?????????????????????
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    //??????????????????
                    String str = reader.readLine();
                    System.out.println(str);
                    //?????????
                    reader.close();
                    in.close();
                    //?????????Message??????????????????????????????????????????
                    //??????Message??????
                    Message msg = new Message();
                    //??????Message???????????????
                    msg.what = 1;
                    msg.obj = str.split(" ");
                    //??????Message
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void addQuestion(final String s) {
        new Thread(){
            @Override
            public void run() {
                //??????URL???HttpURLconnection????????????????????????
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //??????????????????????????????POST
                    conn.setRequestMethod("POST");
                    //?????????????????????
                    OutputStream out = conn.getOutputStream();
                    //???????????????????????????
                    String lis=addName+" "+ConfigUtil.USER_NAME;
                    upStr(ConfigUtil.SERVER_ADDR + "BankServlet");
                    System.out.println(lis+"lyx");
                    out.write(lis.getBytes());
                    //????????????????????????????????????????????????????????????????????????
                    InputStream in= conn.getInputStream();
                    out.close();
                    //?????????????????????
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    //?????????
                    reader.close();
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void addOption(final String s) {
        new Thread(){
            @Override
            public void run() {
                //??????URL???HttpURLconnection????????????????????????
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //??????????????????????????????POST
                    conn.setRequestMethod("POST");
                    //?????????????????????
                    OutputStream out = conn.getOutputStream();
                    //???????????????????????????
                    System.out.println(option+"lyx");
                    out.write(option.getBytes());
                    //????????????????????????????????????????????????????????????????????????
                    InputStream in= conn.getInputStream();
                    out.close();
                    //?????????????????????
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    //??????????????????
                    String str = reader.readLine();
                    System.out.println("?????????"+str);
                    //?????????
                    reader.close();
                    in.close();
                    //?????????Message??????????????????????????????????????????
                    //??????Message??????
                    Message msg = new Message();
                    //??????Message???????????????
                    msg.what = 2;
                    msg.obj = str;
                    //??????Message
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
