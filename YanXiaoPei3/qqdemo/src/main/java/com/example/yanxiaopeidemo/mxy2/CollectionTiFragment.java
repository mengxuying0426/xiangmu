package com.example.yanxiaopeidemo.mxy2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.ChoiceInfo;
import com.example.yanxiaopeidemo.beans.SingleChoice;
import com.example.yanxiaopeidemo.utils.ServerConfig;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CollectionTiFragment extends Fragment {
    private View view;
    private Handler myHandler;
    private ChoiceInfo choiceInfo;
    private List<SingleChoice> singleChoices;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.collect_ti_item, container, false);
        myHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 1:
                        choiceInfo = (ChoiceInfo) msg.obj;
                        if (null != choiceInfo) {
                            singleChoices = choiceInfo.getSingleChoices();
                            Log.i("handle", "" + singleChoices.size());
                            MyCAdapter myCAdapter = new MyCAdapter(view.getContext(),singleChoices);
                            listView = view.findViewById(R.id.lv_ti_item);
                            listView.setAdapter(myCAdapter);
                        }
                }
            }
        };
        String url = ServerConfig.SEVER_ADDR + "/"
                + ServerConfig.NET_HOME + "/downloadscollectti";
        Log.i("mxy下载收藏的题", "" + url);
        downloadCollectti(url);
        Log.i("mxy下载收藏的题", "完毕" + url);


        return view;
    }

    private void downloadCollectti(final String url1) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(url1);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream out = connection.getOutputStream();
                    String keyValue = "" + "shengdai";
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();

                    //获取服务器返回数据判断是否操作成功
                    InputStream in = connection.getInputStream();
                    //读数据（Json串),循环读写方式
//                    byte[] bytes = new byte[131072];
//                    StringBuffer buffer = new StringBuffer();
//                    int len = -1;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
//                    while((len = in.read(bytes,0,bytes.length))!=-1){
//                        buffer.append(new String(bytes, 0, len));
//                    }
                    String result = reader.readLine();
                    Log.i("mxy", "json串+Exersize：" + result);
                    in.close();

                    //先将Json串解析成外部ChoiceInfo对象
                    //创建ChoiceInfo对象和SingleChoice集合对象
                    ChoiceInfo choiceInfo = new ChoiceInfo();
                    List<SingleChoice> singleChoices = new ArrayList<>();
                    //创建外层JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("choices");
                    //遍历JSONArray对象,解析其中的每个元素
                    for (int i = 0; i < jArray.length(); i++) {
                        //获取当前的JSONObject对象
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //获取当前元素中的属性信息
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
                        SingleChoice singleChoice = new SingleChoice(username, kemu, tiid, stem, opta, optb, optc, optd, correct, keynum, analysis, motista);
                        singleChoices.add(singleChoice);
                    }
                    choiceInfo.setSingleChoices(singleChoices);
                    //2. 通过发送Message对象，将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = choiceInfo;
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
}
