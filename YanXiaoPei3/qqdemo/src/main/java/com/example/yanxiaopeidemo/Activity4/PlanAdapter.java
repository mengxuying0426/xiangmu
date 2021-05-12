package com.example.yanxiaopeidemo.Activity4;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.yanxiaopeidemo.R;

import java.text.SimpleDateFormat;

public class PlanAdapter extends BaseAdapter {
    private Context mContext;
    private String[] students;
    private int itemLayoutRes;
    private SimpleDateFormat sdf;

    public PlanAdapter(Context mContext, String[] students, int itemLayoutRes) {
        this.mContext = mContext;
        this.students = students;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {
        if (null != students){
            return students.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != students){
            return students[i];
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View  convertView, ViewGroup viewGroup) {
        //convertView每个item的视图对象
        //加载item的布局文件
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);//布局填充器
            convertView = inflater.inflate(itemLayoutRes, null);
        }
        LinearLayout linearLayout =convertView.findViewById(R.id.bg);
        final TextView name = convertView.findViewById(R.id.name);
        final TextView pro = convertView.findViewById(R.id.pro);
        final TextView time = convertView.findViewById(R.id.time);
        final TextView start =convertView.findViewById(R.id.start1);
        //linearLayout.setBackgroundResource(students[i].split(" ")[]);
        Bitmap b= BitmapFactory.decodeFile(mContext.getFilesDir().getAbsolutePath()+"/"+students[i].split(" ")[2]);
        Drawable drawable = new BitmapDrawable(b);
      //  linearLayout.setBackground(drawable);
        name.setText(students[i].split(" ")[1]);
        pro.setText(students[i].split(" ")[3]);
        time.setText(students[i].split(" ")[0]);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder customizeDialog =
                        new AlertDialog.Builder(mContext);
                final View dialogView = LayoutInflater.from(mContext)
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
                                Intent intent = new Intent();
                                intent.setClass(mContext, ClockActivity.class);
                                intent.putExtra("word", edit_text.getText().toString());
                                intent.putExtra("begintime",time.getText().toString().split("-")[0]);
                                intent.putExtra("endtime",time.getText().toString().split("-")[1]);
                                intent.putExtra("event",name.getText().toString());
                                System.out.println(time.getText().toString().split("-")[0]+time.getText().toString().split("-")[1]+name.getText().toString());
                                mContext.startActivity(intent);
                            }
                        });
                customizeDialog.show();
            }
        });
        return convertView;
    }
}
