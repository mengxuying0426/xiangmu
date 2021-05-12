package com.example.yanxiaopeidemo.Activity4;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yanxiaopeidemo.Bean.Event;
import com.example.yanxiaopeidemo.Bean.EventsInfo;
import com.example.yanxiaopeidemo.Bean.Mission;
import com.example.yanxiaopeidemo.Bean.MissionInfo;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.Bean.Mission;
import com.example.yanxiaopeidemo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SecondFragment extends Fragment {
    private TextView xiping;
    private RecyclerView mRecycler;
    private MyAdapter myAdapter;
    private int signl=0;
    private TimerAdapter timerAdapter;
    private RecyclerView mRecycler2;
    private  RecyclerView mRecyclerView;
    private MyMissAdapter myMissAdapter;
    private List<Event> eventList = new ArrayList<>();
    private List<Mission> missionsList = new ArrayList<>();
    private List<DataBean> dataList = new ArrayList<>();
    private ImageView jishiqi;
    private ImageView daiban;
    private ImageView biao;
    private View view;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView jindu;
    private ImageView smile;
    private Handler myHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    List<Mission> missions = new ArrayList<Mission>();
                    MissionInfo missionInfo = (MissionInfo)msg.obj;
                    missions = missionInfo.getMissionList();
                    //实例化myAdapter并调用带参数的构造方法传一个missionsList的列表
                    myMissAdapter = new MyMissAdapter(missions);
                    //实例化mRecycler
                    mRecycler2 = view.findViewById(R.id.recycler2);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    //设置布局管理器为线性布局管理器
                    mRecycler2.setLayoutManager(layoutManager);
                    //设置适配器
                    mRecycler2.setAdapter(myMissAdapter);
                    break;
                case 2:
                    //实例化myAdapter并调用带参数的构造方法传一个fruitList的列表
                    List<Event> events = new ArrayList<Event>();
                    events = (List<Event>) msg.obj;
                    myAdapter = new MyAdapter(events);
                    //实例化mRecycler
                    mRecycler = view.findViewById(R.id.recycler);
                    RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getContext(), 5);
                    //设置布局管理器为线性布局管理器
                    mRecycler.setLayoutManager(layoutManager1);
                    //设置适配器
                    mRecycler.setAdapter(myAdapter);
                    //设置分隔线
                    mRecycler.addItemDecoration(new MyDecoration());

                    myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(int position, String shijian) {

                            // 构建dialog显示的view布局
                            View view1 = getLayoutInflater().from(getContext()).inflate(R.layout.dialog_select_picture, null);

                            AlertDialog dialog = new AlertDialog.Builder(getContext())
                                    .setTitle("计划详情")//标题
                                    .setMessage(shijian)//内容;
                                    .create();
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.circle_ic2);

                            dialog.show();
                            //给AlertDialog设置4个圆角

                        }
                    });
                    break;
                case 3:
                    String number = (String)msg.obj;
                    Log.e("数字",number);

                    String[] numbers = number.split("%");
                    DecimalFormat df = new DecimalFormat("#####0.00");
                    String str = df.format(Double.parseDouble(numbers[0]));
                    jindu.setText(str+"%");
                    if (Double.parseDouble(numbers[0]) >= 60) {
                        smile.setImageResource(R.drawable.ic_red_emo);
                    } else {
                        smile.setImageResource(R.drawable.ic_md_insert_emoticon);
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_daiban, container, false);

        findView();
        listener();

        initListView();
        initListView2();
        changeSmile();
        initListView3();
        if (signl ==0){
            initData();
        }
        return view;
    }


    private void initData() {
        dataList.add(new DataBean("20年01月","复习英语"));
        dataList.add(new DataBean("20年02月","复习英语"));
        dataList.add(new DataBean("20年03月","数学基础复习"));
        dataList.add(new DataBean("20年04月","数学基础复习"));
        dataList.add(new DataBean("20年05月","数学巩固复习"));
        dataList.add(new DataBean("20年06月","专业课复习"));
        dataList.add(new DataBean("20年07月","专业课复习"));
        dataList.add(new DataBean("20年08月","数学复习"));
        dataList.add(new DataBean("20年09月","英语复习"));
        dataList.add(new DataBean("20年10月","数学巩固复习"));
        dataList.add(new DataBean("20年11月","政治复习"));
        dataList.add(new DataBean("20年12月","综合复习"));
        dataList.add(new DataBean("12月26日","考研日"));
        signl = 1;
    }

    private void initListView3() {
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        //设置方向
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置adapter
        timerAdapter =new TimerAdapter(getContext(), (ArrayList<DataBean>) dataList);

        mRecyclerView.setAdapter(timerAdapter);

        timerAdapter.setOnItemClickListener(
                new TimerAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(final int position) {
                        final String[] nnn = new String[1];
                        final EditText editText = new EditText(getContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("复习步骤").setView(editText)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @SuppressLint("ResourceType")
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        nnn[0] = String.valueOf(editText.getText());
                                        DataBean dataBean = new DataBean(dataList.get(position).getDate(),nnn[0]);
                                        dataList.set(position,dataBean);
                                        initListView3();
                                    }
                                });
                        builder.create().show();
                    }
                }
        );

    }

    private void initListView2() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL urlPath = new URL(ConfigUtil.SERVER_ADDR + "downLoadEvent" + "?username=" + ConfigUtil.USER_NAME);
                    StringBuffer bs = new StringBuffer();
                    HttpURLConnection urlConnection = (HttpURLConnection) urlPath.openConnection();
                    urlConnection.connect();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String str = null;
                    while ((str = in.readLine()) != null) {
                        bs.append(str);
                    }
                    String result = bs.toString();
                    Gson gson = new Gson();
                    Log.e("测试", "getList: " + result);
                    Type collectionType = new TypeToken<List<Event>>() {
                    }.getType();
                    List<Event> eventList = (List<Event>) gson.fromJson(result, collectionType);

                    EventsInfo eventsInfo = new EventsInfo();
                    eventsInfo.setEventList(eventList);

                    in.close();
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 2;
                    msg.obj = eventList;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void changeSmile() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL urlPath = new URL(ConfigUtil.SERVER_ADDR + "calculateFinish" + "?username=" + ConfigUtil.USER_NAME);
                    StringBuffer bs = new StringBuffer();
                    HttpURLConnection urlConnection = (HttpURLConnection) urlPath.openConnection();
                    urlConnection.connect();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String str = null;
                    while ((str = in.readLine()) != null) {
                        bs.append(str);
                    }
                    String result = bs.toString();
                    in.close();
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 3;
                    msg.obj = result;
                    //发送Message对象
                    myHandler.sendMessage(msg);
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

    private void initListView() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL urlPath = new URL(ConfigUtil.SERVER_ADDR+ "downLoadMissions" + "?username=" + ConfigUtil.USER_NAME);
                    StringBuffer bs = new StringBuffer();
                    HttpURLConnection urlConnection = (HttpURLConnection) urlPath.openConnection();
                    urlConnection.connect();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String str = null;
                    while ((str = in.readLine()) != null) {
                        bs.append(str);
                    }
                    String result = bs.toString();
                    Gson gson = new Gson();
                    Log.e("测试", "getList: " + result);
                    Type collectionType = new TypeToken<List<Mission>>() {
                    }.getType();
                    List<Mission> missionList = null;
                    missionList=  gson.fromJson(result, collectionType);

                    MissionInfo missionInfo = new MissionInfo();
                    missionInfo.setMissionList(missionList);
                    in.close();
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = missionInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            //outRect.set()中的参数分别对应左、上、右、下的间隔
            outRect.set(0, 0, 0, 0);
        }
    }

    private void listener() {
        MyListener myListener = new MyListener();
        jishiqi.setOnClickListener(myListener);
        daiban.setOnClickListener(myListener);
        biao.setOnClickListener(myListener);
        xiping.setOnClickListener(myListener);
    }

    private void findView() {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        xiping = view.findViewById(R.id.back_home);
        jishiqi = view.findViewById(R.id.jishiqi);
        daiban = view.findViewById(R.id.daiban);
        biao = view.findViewById(R.id.biao);
        calendar = Calendar.getInstance();
        jindu = view.findViewById(R.id.jindu);
        smile = view.findViewById(R.id.simle);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back_home:
                    Intent s = new Intent(getActivity(), CloseActivity.class);
                    startActivity(s);
                    break;
                case R.id.jishiqi:
                    Intent j = new Intent(getActivity(), com.example.yanxiaopeidemo.Activity4.TimeActivity.class);
                    startActivity(j);
                    break;
                case R.id.daiban:
                    showCalenderDialog();
                    break;
                case R.id.biao:
                    AlertDialog.Builder customizeDialog =
                            new AlertDialog.Builder(getActivity());
                    final View dialogView = LayoutInflater.from(getActivity())
                            .inflate(R.layout.dialog_customize, null);
                    customizeDialog.setTitle("解锁标语");
                    customizeDialog.setView(dialogView);
                    customizeDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 获取EditView中的输入内容
                                    EditText edit_text =
                                            (EditText) dialogView.findViewById(R.id.edit_text);
                                    Intent i = new Intent(getActivity(), ClockActivity.class);
                                    i.putExtra("word", edit_text.getText().toString());
                                    /*i.putExtra("event", "背单词");
                                    i.putExtra("begintime", "10:10");
                                    i.putExtra("endtime", "10:20");*/
                                    startActivity(i);
                                }
                            });
                    customizeDialog.show();
                    break;
            }
        }
    }

    private void showCalenderDialog() {
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String calender = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                Intent i = new Intent(getActivity(), com.example.yanxiaopeidemo.Activity4.SetActivity.class);
                i.putExtra("calendar", calender);
                startActivity(i);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
