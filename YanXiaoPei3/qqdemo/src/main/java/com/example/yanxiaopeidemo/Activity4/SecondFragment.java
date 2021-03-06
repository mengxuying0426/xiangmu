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
                    //?????????myAdapter??????????????????????????????????????????missionsList?????????
                    myMissAdapter = new MyMissAdapter(missions);
                    //?????????mRecycler
                    mRecycler2 = view.findViewById(R.id.recycler2);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    //?????????????????????????????????????????????
                    mRecycler2.setLayoutManager(layoutManager);
                    //???????????????
                    mRecycler2.setAdapter(myMissAdapter);
                    break;
                case 2:
                    //?????????myAdapter??????????????????????????????????????????fruitList?????????
                    List<Event> events = new ArrayList<Event>();
                    events = (List<Event>) msg.obj;
                    myAdapter = new MyAdapter(events);
                    //?????????mRecycler
                    mRecycler = view.findViewById(R.id.recycler);
                    RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getContext(), 5);
                    //?????????????????????????????????????????????
                    mRecycler.setLayoutManager(layoutManager1);
                    //???????????????
                    mRecycler.setAdapter(myAdapter);
                    //???????????????
                    mRecycler.addItemDecoration(new MyDecoration());

                    myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(int position, String shijian) {

                            // ??????dialog?????????view??????
                            View view1 = getLayoutInflater().from(getContext()).inflate(R.layout.dialog_select_picture, null);

                            AlertDialog dialog = new AlertDialog.Builder(getContext())
                                    .setTitle("????????????")//??????
                                    .setMessage(shijian)//??????;
                                    .create();
                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.circle_ic2);

                            dialog.show();
                            //???AlertDialog??????4?????????

                        }
                    });
                    break;
                case 3:
                    String number = (String)msg.obj;
                    Log.e("??????",number);

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
        dataList.add(new DataBean("20???01???","????????????"));
        dataList.add(new DataBean("20???02???","????????????"));
        dataList.add(new DataBean("20???03???","??????????????????"));
        dataList.add(new DataBean("20???04???","??????????????????"));
        dataList.add(new DataBean("20???05???","??????????????????"));
        dataList.add(new DataBean("20???06???","???????????????"));
        dataList.add(new DataBean("20???07???","???????????????"));
        dataList.add(new DataBean("20???08???","????????????"));
        dataList.add(new DataBean("20???09???","????????????"));
        dataList.add(new DataBean("20???10???","??????????????????"));
        dataList.add(new DataBean("20???11???","????????????"));
        dataList.add(new DataBean("20???12???","????????????"));
        dataList.add(new DataBean("12???26???","?????????"));
        signl = 1;
    }

    private void initListView3() {
        LinearLayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        //????????????
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //????????????
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //??????adapter
        timerAdapter =new TimerAdapter(getContext(), (ArrayList<DataBean>) dataList);

        mRecyclerView.setAdapter(timerAdapter);

        timerAdapter.setOnItemClickListener(
                new TimerAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(final int position) {
                        final String[] nnn = new String[1];
                        final EditText editText = new EditText(getContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("????????????").setView(editText)
                                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
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
                    Log.e("??????", "getList: " + result);
                    Type collectionType = new TypeToken<List<Event>>() {
                    }.getType();
                    List<Event> eventList = (List<Event>) gson.fromJson(result, collectionType);

                    EventsInfo eventsInfo = new EventsInfo();
                    eventsInfo.setEventList(eventList);

                    in.close();
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 2;
                    msg.obj = eventList;
                    //??????Message??????
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
                    //??????Message??????????????????what???obj???
                    msg.what = 3;
                    msg.obj = result;
                    //??????Message??????
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
                    Log.e("??????", "getList: " + result);
                    Type collectionType = new TypeToken<List<Mission>>() {
                    }.getType();
                    List<Mission> missionList = null;
                    missionList=  gson.fromJson(result, collectionType);

                    MissionInfo missionInfo = new MissionInfo();
                    missionInfo.setMissionList(missionList);
                    in.close();
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 1;
                    msg.obj = missionInfo;
                    //??????Message??????
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
            //outRect.set()??????????????????????????????????????????????????????
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
                    customizeDialog.setTitle("????????????");
                    customizeDialog.setView(dialogView);
                    customizeDialog.setPositiveButton("??????",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ??????EditView??????????????????
                                    EditText edit_text =
                                            (EditText) dialogView.findViewById(R.id.edit_text);
                                    Intent i = new Intent(getActivity(), ClockActivity.class);
                                    i.putExtra("word", edit_text.getText().toString());
                                    /*i.putExtra("event", "?????????");
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
                String calender = year + "???" + (month + 1) + "???" + dayOfMonth + "???";
                Intent i = new Intent(getActivity(), com.example.yanxiaopeidemo.Activity4.SetActivity.class);
                i.putExtra("calendar", calender);
                startActivity(i);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
