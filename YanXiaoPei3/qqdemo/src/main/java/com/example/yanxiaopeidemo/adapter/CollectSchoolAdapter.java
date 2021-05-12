package com.example.yanxiaopeidemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys.SchoolInfo;
import com.example.yanxiaopeidemo.util.ConfigUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.yanxiaopeidemo.MainActivity.username;
import static com.example.yanxiaopeidemo.adapter.CollectSchoolAdapter.name;
public class CollectSchoolAdapter extends BaseAdapter {
    private List<SchoolInfo> list = new ArrayList<>();
    private int layoutRes;
    private Context context;
    public static String name = null;

    public CollectSchoolAdapter(List<SchoolInfo> list, int layoutRes, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutRes,null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.ivColl = convertView.findViewById(R.id.iv_coll);
            viewHolder.tvBelong = convertView.findViewById(R.id.tv_belong);
            viewHolder.tvLocation = convertView.findViewById(R.id.tv_loc);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(list.get(position).getName());
        viewHolder.tvLocation.setText(list.get(position).getLoc());
        viewHolder.tvBelong.setText(list.get(position).getBelong());
        viewHolder.tvTime.setText(list.get(position).getTime());
        viewHolder.ivColl.setImageResource(R.mipmap.collect_selected);
        viewHolder.ivColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = list.get(position).getName();
                UpUserCancelCollectSchoolInfoThread2 thread2 = new UpUserCancelCollectSchoolInfoThread2();
                thread2.start();
                try {
                    thread2.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                list.remove(list.get(position));
                notifyDataSetChanged();
                Toast.makeText(context,"取消收藏成功",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}


/**
 * 向服务端发送用户取消收藏的学校信息
 */
class UpUserCancelCollectSchoolInfoThread2 extends Thread {
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

