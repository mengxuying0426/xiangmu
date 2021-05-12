package com.example.yanxiaopeidemo.Activity4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//第一个内容页面对应的Fragment类
public class FirstFragment extends Fragment {
    private ConvenientBanner localConvenientBanner;
    private List<String> netImages = new ArrayList<>();
    private ImageView img;
    private TextView days;
    private String  mDay;
    private long mHour = 8;
    private long mMin = 23;
    private long mSecond = 00;// 天 ,小时,分钟,秒
    private boolean isRun = true;
    private Button btn;
    private TextView start;
    private TextView start1;
    private View view;
    private int times=0;
    private TextView name;
    private String[] list;
    private ScrollView sc;
    private String imgUrl;
    private String head;
    private List<com.example.yanxiaopeidemo.Activity4.Plan> students = new ArrayList<>();
    private List<Integer> localImages = new ArrayList<>();
    private Integer[] imagesInteger = new Integer[] { R.drawable.b, R.drawable.p, R.drawable.h ,R.drawable.y};
    private Handler timeHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Calendar calendar = Calendar.getInstance();
                    mDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                    days.setText(26-(Integer.parseInt(mDay))+"天");
                    break;
                case 2:
                    String str = (String) msg.obj;
                    if(null != str){
                        imgUrl=str.split(" ")[1];
                        head =str.split(" ")[0];
                        System.out.println(getContext().getFilesDir().getAbsolutePath()+"/"+head);
                        setImg(imgUrl);
                        setImg(head);
                        Bitmap b= BitmapFactory.decodeFile(getContext().getFilesDir().getAbsolutePath()+"/"+imgUrl);
                        Drawable drawable = new BitmapDrawable(b);
                      //  sc.setBackground(drawable);
                        Bitmap bitmap= BitmapFactory.decodeFile(getContext().getFilesDir().getAbsolutePath()+"/"+head);
//                        Glide.with(getContext())
//                                .load(bitmap)
//                                .circleCrop()
//                                .into(img);
                    }

                    break;
                case 3:
                    list = (String[]) msg.obj;
                    setImgs();
                    com.example.yanxiaopeidemo.Activity4.PlanAdapter customAdapter = new com.example.yanxiaopeidemo.Activity4.PlanAdapter(getContext(), list,
                            R.layout.plan_item);
                    ListView stuListView = view.findViewById(R.id.plan_list);
                    stuListView.setAdapter(customAdapter);

                    break;
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载内容页面的布局文件（将内容页面的XML布局文件转成View类型的对象）
       view = inflater.inflate(R.layout.first_fragment,//内容页面的布局文件
                container,//根视图对象
                false);//false表示需要手动调用addView方法将view添加到container
                                     //true表示不需要手动调用addView方法
        findViews();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getContext(), com.example.yanxiaopeidemo.Activity4.PictureActivity.class);
                startActivity(intent);
            }
        });

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getContext(), com.example.yanxiaopeidemo.Activity4.MemberActivity.class);
//                intent.putExtra("img",head);
//                startActivity(intent);
//            }
//        });
        upStr(ConfigUtil.SERVER_ADDR + "UserMsgServlet");
        upPlan(ConfigUtil.SERVER_ADDR + "PlanServlet");
        startRun();
        initViews();
        init();
        //准备数据
        times++;
        return view;
    }

    private void findViews() {
     //   img = view.findViewById(R.id.img);
        btn =view.findViewById(R.id.pic);
        days =view.findViewById(R.id.days_tv);
        start = view.findViewById(R.id.start1);
        sc = view.findViewById(R.id.sc);
    }
    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void initViews() {
        localConvenientBanner = view.findViewById(R.id.localConvenientBanner);

    }

    private void init() {
        //广告栏播放本地图片资源
        localRes();
    }

    /**
     * 广告栏播放本地图片资源
     */
    private void localRes() {
        if(times==0){
            loadTestDatas();
        }

        ///设置用图片作为翻页方式，同时也可以设置其他空间作为翻页方式
        localConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new com.example.yanxiaopeidemo.Activity4.LocalImageHolderView(itemView); //使用当地的图片
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_image;
            }
        }, localImages)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器，不需要圆点指示器可以不设
                .setPageIndicator(new int[] { R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused })
                //设置指示器的位置（左、中、右）
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                //设置指示器是否可见
                .setPointViewVisible(true)

        //监听翻页事件
//                .setOnPageChangeListener(this)
        ;
    }

    /**
     * 加载本地图片资源
     */
    private void loadTestDatas() {
        localImages.addAll(Arrays.asList(imagesInteger));
    }

    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        localConvenientBanner.startTurning();
    }

    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        localConvenientBanner.stopTurning();
    }
    private void upStr(final String s) {
        new Thread(){
            @Override
            public void run() {
                //使用URL和HttpURLconnection方式进行网络连接
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    //获取网络输出流
                    OutputStream out = conn.getOutputStream();
                    //获取待发送的字符串
                    //接收传递的对象
                    out.write(ConfigUtil.USER_NAME.getBytes());
                    System.out.println(ConfigUtil.USER_NAME);
                    //必须要获取网络输入流，保证客户端和服务端建立连接
                    InputStream in= conn.getInputStream();
                    out.close();
                    //使用字符流读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    System.out.println(str);
                    //关闭流
                    reader.close();
                    in.close();
                    //借助于Message，把收到的字符串显示在页面上
                    //创建Message对象
                    Message msg = new Message();
                    //设置Message对象的参数
                    msg.what = 2;
                    msg.obj = str;
                    //发送Message
                    timeHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void setImg(final String pic) {
        try {
            new Thread() {
                @Override
                public void run() {
                    //创建url对象
                        String netHeader =ConfigUtil.SERVER_ADDR+pic;
                        System.out.println(netHeader);
                        final String files =getContext().getFilesDir().getAbsolutePath();
                        System.out.println(files);
                        String imgs = files+"/img";
                        //判断imgs目录是否存在
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            //如果目录不存在则创建
                            dirImgs.mkdir();
                        }
                        try {
                            URL urlPath = new URL(netHeader);
                            InputStream in = urlPath.openStream();
                            //获取本地的files目录
                            String imgName = pic;
                            String imgPath =  files + "/" + imgName;
                            Log.i("lww", "拼接头像名称：" + imgPath);
                            //获取本地文件输出流
                            System.out.println(imgPath);
                            OutputStream out = new FileOutputStream(imgPath);
                            //循环读写
                            int b = -1;
                            while ((b = in.read()) != -1) {
                                out.write(b);
                                out.flush();
                            }
                            out.close();
                            in.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }}.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private void upPlan(final String s) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(s);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //设置网络请求的方式为POST
                    conn.setRequestMethod("POST");
                    //获取网络输出流
                    OutputStream out = conn.getOutputStream();
                    //获取待发送的字符串
                    //接收传递的对象
                    // Intent request =getIntent();
                    //sName = request.getStringExtra("name");
                    out.write(ConfigUtil.USER_NAME.getBytes());
                    System.out.println(ConfigUtil.USER_NAME);
                    //必须要获取网络输入流，保证客户端和服务端建立连接
                    InputStream in= conn.getInputStream();
                    out.close();
                    //使用字符流读取
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(in, "utf-8"));
                    //读取字符信息
                    String str = reader.readLine();
                    System.out.println(str);
                    //关闭流
                    reader.close();
                    in.close();
                    //借助于Message，把收到的字符串显示在页面上
                    //创建Message对象
                    if(null != str){
                        Message msg = new Message();
                        //设置Message对象的参数
                        msg.what = 3;
                        msg.obj = str.split("&");
                        //发送Message
                        timeHandler.sendMessage(msg);
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void setImgs() {
        try {
            new Thread() {
                @Override
                public void run() {
                    //创建url对象
                    for(int i=0;i<list.length;i++){
                        String netHeader =ConfigUtil.SERVER_ADDR+list[i].split(" ")[2];
                        System.out.println(netHeader);
                        final String files =getContext().getFilesDir().getAbsolutePath();
                        System.out.println(files);
                        String imgs = files+"/img";
                        //判断imgs目录是否存在
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            //如果目录不存在则创建
                            dirImgs.mkdir();
                        }
                        try {
                            URL urlPath = new URL(netHeader);
                            InputStream in = urlPath.openStream();
                            //获取本地的files目录
                            String imgName = list[i].split(" ")[0];
                            String imgPath =  files + "/" + imgName;
                            Log.i("lww", "拼接头像名称：" + imgPath);
                            //获取本地文件输出流
                            OutputStream out = new FileOutputStream(imgPath);
                            //循环读写
                            int b = -1;
                            while ((b = in.read()) != -1) {
                                out.write(b);
                                out.flush();
                            }
                            out.close();
                            in.close();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }}}.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
