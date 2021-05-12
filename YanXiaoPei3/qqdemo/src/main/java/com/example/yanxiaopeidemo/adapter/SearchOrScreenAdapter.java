package com.example.yanxiaopeidemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys.SchoolInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.yanxiaopeidemo.adapter.SearchOrScreenAdapter.schools;
import static com.example.yanxiaopeidemo.MainActivity.username;
import static com.example.yanxiaopeidemo.adapter.SearchOrScreenAdapter.name;
import static com.example.yanxiaopeidemo.adapter.SearchOrScreenAdapter.time;

public class SearchOrScreenAdapter extends BaseAdapter {
    private List<SchoolInfo> list = new ArrayList<>();
    private int layoutRes;
    private Context context;
    public static String name = null;
    public static String time = null;
    public static List<SchoolInfo> schools = new ArrayList<>();

    public SearchOrScreenAdapter(List<SchoolInfo> list, int layoutRes, Context context) {
        this.list = list;
        this.layoutRes = layoutRes;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (null != list){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (null != list){
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (null != list){
            return position;
        }
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutRes,null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.ivColl = convertView.findViewById(R.id.iv_coll);
            viewHolder.tvBelong = convertView.findViewById(R.id.tv_belong);
            viewHolder.tvLocation = convertView.findViewById(R.id.tv_loc);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).getName());
        viewHolder.tvLocation.setText(list.get(position).getLoc());
        viewHolder.tvBelong.setText(list.get(position).getBelong());
        if (schools.size()!=0){
            schools.clear();
        }
        DownUserAllCollectSchoolInfoThread thread = new DownUserAllCollectSchoolInfoThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int a = 0;
        for (int i = 0;i<schools.size();i++){
            if (schools.get(i).getName().equals(list.get(position).getName())){
                Log.e("已收藏学校",list.get(position).getName()+ schools.get(i).getName());
                viewHolder.ivColl.setImageResource(R.mipmap.collect_selected);
                viewHolder.ivColl.setTag(R.mipmap.collect_selected);
            }else{
                a++;
            }
        }
        if (a == schools.size()){
            viewHolder.ivColl.setImageResource(R.mipmap.collect);
            viewHolder.ivColl.setTag(R.mipmap.collect);
        }
//        name = list.get(position).getName();
        viewHolder.ivColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.ivColl.getTag().equals(R.mipmap.collect)){
                    viewHolder.ivColl.setImageResource(R.mipmap.collect_selected);
                    viewHolder.ivColl.setTag(R.mipmap.collect_selected);
                    name = viewHolder.tvName.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    time = year + "/" + (month+1) + "/" + day;
                    Toast.makeText(context,"收藏成功", Toast.LENGTH_SHORT).show();
                    UpUserCollectSchoolInfoThread thread = new UpUserCollectSchoolInfoThread();
                    thread.start();
                }else{
                    viewHolder.ivColl.setImageResource(R.mipmap.collect);
                    viewHolder.ivColl.setTag(R.mipmap.collect);
                    Toast.makeText(context,"取消收藏成功", Toast.LENGTH_SHORT).show();
                    UpUserCancelCollectSchoolInfoThread thread = new UpUserCancelCollectSchoolInfoThread();
                    thread.start();
                }
            }
        });
        return convertView;
    }
}

/**
 * 向服务端发送用户收藏的学校信息
 */
class UpUserCollectSchoolInfoThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "UpUserCollectSchoolInfoServlet";
    @Override
    public void run() {
        String keyValue = "?username=" + username + "&school=" + name + "&time=" + time;
        try {
            URL url = new URL(s + keyValue);
            url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


/**
 * 向服务端发送用户取消收藏的学校信息
 */
class UpUserCancelCollectSchoolInfoThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "UpUserCancelCollectSchoolInfoServlet";
    @Override
    public void run() {
        String keyValue = "?username=" + username + "&school=" + name;
        try {
            URL url = new URL(s + keyValue);
            url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

/**
 * 接收服务端发送的用户收藏的所有学校信息
 */
class DownUserAllCollectSchoolInfoThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "DownUserAllCollectSchoolInfoServlet";

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL(s);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, "utf-8"));
            String str = reader.readLine();
            if (null != str){
                str = new String(str.getBytes(), "utf-8");
                Log.e("str",str);
                reader.close();
                in.close();
                if (schools.size()!=0){
                    schools.clear();
                }
                String[] strs = str.split("&&&");
                for (int i = 1; i < strs.length; i=i+5) {
                    SchoolInfo s = new SchoolInfo(strs[i],strs[i+1],strs[i+2],strs[i+3],strs[i+4]);
                    schools.add(s);
                    Log.e("collectschools", schools.size()+"");
                }
            }
        } catch(MalformedURLException e){
            e.printStackTrace();
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
