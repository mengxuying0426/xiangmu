package com.example.yanxiaopeidemo.fragment;

import android.content.Intent;
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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.WBankInfo;
import com.example.yanxiaopeidemo.beans.WrongSet;
import com.example.yanxiaopeidemo.mxy2.MyWAdapter;
import com.example.yanxiaopeidemo.utils.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FragmentSecond extends Fragment {
    private View view;
    private SwipeMenuListView swipeMenuListView;
    private List<WrongSet> mQList;
    private MyWAdapter myWAdapter;
    private Handler myHandler;
    private WBankInfo wBankInfo;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        /*if (view != null) {
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
                        wBankInfo = (WBankInfo)msg.obj;
                        if(null!=wBankInfo){
                            mQList = wBankInfo.getWrongSets();
                            //??????Adapter
                            myWAdapter = new MyWAdapter(view.getContext(),mQList);
                            swipeMenuListView.setAdapter(myWAdapter);
                        }
                        break;
                }
            }
        };
        //?????????????????????
        String url = ServerConfig.SEVER_ADDR+"/"
                +ServerConfig.NET_HOME+"/downloadscuotiku";
        Log.i("mxy???????????????",""+url);
        downloadCuoTiku(url);
        Log.i("mxy???????????????","??????"+url);
        //????????????
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(view.getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView = view.findViewById(R.id.swipelistView);
        swipeMenuListView.setMenuCreator(creator);

        /*
        ????????????
         */
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                WrongSet w = mQList.get(position);
                switch (index){
                    case 0:
//                        delete(q);??????????????????
                        mQList.remove(position);
                        myWAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        // ???????????????ListView???SwipeMenu????????????
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                Log.d("??????:" + position, "????????????...");
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

        return view;
    }

    /**
     * ??????Cuo??????
     * @param url
     */
    private void downloadCuoTiku(final String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL urlPath = new URL(url);
                    //??????URL???????????????????????????
                    InputStream in = urlPath.openStream();
                    //????????????Json???),??????????????????
                    byte[] bytes = new byte[1024];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while((len = in.read(bytes,0,bytes.length))!=-1){
                        buffer.append(new String(bytes, 0, len));
                    }
                    String result = buffer.toString();
                    Log.i("mxy","json??????"+result);
                    in.close();
                    //??????Json??????????????????WBankInfo??????
                    //??????WBankInfo?????????WrongSet????????????
                    WBankInfo wbankInfo = new WBankInfo();
                    List<WrongSet> wrongSets = new ArrayList<>();
                    //????????????JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("wbank");
                    //??????JSONArray??????,???????????????????????????
                    for(int i = 0;i<jArray.length();i++){
                        WrongSet wrongSet = new WrongSet();
                        //???????????????JSONObject??????
                        JSONObject JBank = jArray.getJSONObject(i);
                        //????????????????????????????????????
                        int id = JBank.getInt("id");
                        String username = JBank.getString("username");
                        String kemu = JBank.getString("kemu");
                        int kemusta = JBank.getInt("kemusta");
                        wrongSet.setId(id);
                        wrongSet.setUsername(username);
                        wrongSet.setKemu(kemu);
                        wrongSet.setKemusta(kemusta);
                        wrongSets.add(wrongSet);
                    }
                    wbankInfo.setWrongSets(wrongSets);
                    //2. ????????????Message??????????????????????????????
                    //??????Message??????
                    Message msg = myHandler.obtainMessage();
                    //??????Message??????????????????what???obj???
                    msg.what = 1;
                    msg.obj = wbankInfo;
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

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        String url = ServerConfig.SEVER_ADDR+"/"
                +ServerConfig.NET_HOME+"/downloadscuotiku";
        Log.i("mxy???????????????",""+url);
        downloadCuoTiku(url);
        Log.i("mxy???????????????","??????"+url);
    }
}
