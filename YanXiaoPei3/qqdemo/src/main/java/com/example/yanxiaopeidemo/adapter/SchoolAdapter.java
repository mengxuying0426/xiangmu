package com.example.yanxiaopeidemo.adapter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.RequiresApi;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolAdapter extends BaseAdapter {
    private List<School> list = new ArrayList<>();
    private int layoutRes;
    private Context context;

    public SchoolAdapter(List<School> list, int layoutRes, Context context) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutRes,null);
            holder = new ViewHolder();
            holder.photo = convertView.findViewById(R.id.photo);
            holder.tvName = convertView.findViewById(R.id.tv_schoolname);
            holder.tvLocation = convertView.findViewById(R.id.tv_location);
            holder.tvTag = convertView.findViewById(R.id.tv_tag);
            holder.btnInfo = convertView.findViewById(R.id.btn_schoolinfo);
            holder.tvScore = convertView.findViewById(R.id.score);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Bitmap b = BitmapFactory.decodeResource(context.getResources(),list.get(position).getImg());
        BitmapDrawable bd = new BitmapDrawable(b);
        holder.photo.setBackground(bd);
        holder.tvName.setText(list.get(position).getName());
        holder.tvLocation.setText(list.get(position).getLocation());
        holder.tvScore.setText(list.get(position).getType());
        holder.tvTag.setText(list.get(position).getTag());
        holder.btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(list.get(position).getPath());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
