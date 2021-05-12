package com.example.yanxiaopeidemo.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.BankInfo;
import com.example.yanxiaopeidemo.beans.QuestionBank;
import com.example.yanxiaopeidemo.mxy2.MyQAdapter;
import com.example.yanxiaopeidemo.utils.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentOne extends Fragment {
    private View view;
    private SwipeMenuListView swipeMenuListView;
    private List<QuestionBank> mQList;
    private MyQAdapter myQAdapter;
    private Handler myHandler;
    private BankInfo bankInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       /* if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }*/
        view = inflater.inflate(R.layout.fragment1,container,false);
        myHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        bankInfo = (BankInfo)msg.obj;
                        if(null!=bankInfo){
                            mQList = bankInfo.getQuestionBanks();
                            //绑定Adapter
                            myQAdapter = new MyQAdapter(view.getContext(),mQList);
                            swipeMenuListView.setAdapter(myQAdapter);
                        }
                        break;
                }
            }
        };
        //获取控件
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(70));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView = view.findViewById(R.id.swipelistView);
        swipeMenuListView.setMenuCreator(creator);

        /*
        设置监听
         */
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                QuestionBank q = mQList.get(position);
                switch (index){
                    case 0:
//                        delete(q);数据库中删除
                        mQList.remove(position);
                        myQAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        // 监测用户在ListView的SwipeMenu侧滑事件
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                Log.d("位置:" + position, "开始侧滑...");
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
//        //设置一些死数据
//        mQList = new ArrayList<>();
//        QuestionBank q1 = new QuestionBank(1,"cxk","马克思主义基本原理概论",3,1);
//        QuestionBank q2 = new QuestionBank(2,"cxk","中国近代史纲要",6,1);
//        QuestionBank q3 = new QuestionBank(3,"cxk","毛泽东思想和中国特色社会主义理论体系概论",3,1);
//        QuestionBank q4 = new QuestionBank(4,"cxk","思想道德修养与法律基础",3,1);
//        QuestionBank q5 = new QuestionBank(4,"cxk","马克思主义基本原理概论",3,1);
//        mQList.add(q1);
//        mQList.add(q2);
//        mQList.add(q3);
//        mQList.add(q4);
//        mQList.add(q5);
//        //绑定Adapter
//        myQAdapter = new MyQAdapter(view.getContext(),mQList);
//        swipeMenuListView.setAdapter(myQAdapter);

        String url = ServerConfig.SEVER_ADDR+"/"
                +ServerConfig.NET_HOME+"/downloadstiku";
        downloadTiku(url);



        return view;
    }

    /**
     * 下载题库
     * @param url
     */
    private void downloadTiku(final String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL urlPath = new URL(url);
                    //通过URL对象获取网络输入流
                    InputStream in = urlPath.openStream();
                    //读数据（Json串),循环读写方式
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    while((len = in.read(bytes,0,bytes.length))!=-1){
//                        buffer.append(new String(bytes, 0, len));
//                    }
                    String result = reader.readLine();
                    Log.i("mxy","json串："+result);
                    in.close();
                    //先将Json串解析成外部BankInfo对象
                    //创建BankInfo对象和QuestionBank集合对象
                    BankInfo bankInfo = new BankInfo();
                    List<QuestionBank> questionBanks = new ArrayList<>();
                    //创建外层JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("bank");
                    //遍历JSONArray对象,解析其中的每个元素
                    for(int i = 0;i<jArray.length();i++){
                        QuestionBank questionBank = new QuestionBank();
                        //获取当前的JSONObject对象
                        JSONObject JBank = jArray.getJSONObject(i);
                        //获取当前元素中的属性信息
                        int id = JBank.getInt("id");
                        String username = JBank.getString("username");
                        String kemu = JBank.getString("kemu");
                        int tinum = JBank.getInt("tinum");
                        int kemusta = JBank.getInt("kemusta");
                        questionBank.setId(id);
                        questionBank.setUsername(username);
                        questionBank.setKemu(kemu);
                        questionBank.setTinum(tinum);
                        questionBank.setKemuSta(kemusta);
                        questionBanks.add(questionBank);
                    }
                    bankInfo.setQuestionBanks(questionBanks);
                    //2. 通过发送Message对象，将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = bankInfo;
                    //发送Message对象
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
    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        String url = ServerConfig.SEVER_ADDR+"/"
                +ServerConfig.NET_HOME+"/downloadstiku";
        downloadTiku(url);
    }

}
