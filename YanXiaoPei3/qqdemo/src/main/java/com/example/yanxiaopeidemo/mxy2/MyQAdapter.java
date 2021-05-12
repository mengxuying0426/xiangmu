package com.example.yanxiaopeidemo.mxy2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.QuestionBank;


import java.util.List;

public class MyQAdapter extends BaseAdapter {
    private Context myContext;
    private List<QuestionBank> mQList;
    private LayoutInflater layoutinflater;//视图容器，用来导入布局

    static class ViewHolder {
        private TextView tvKemu;
        private TextView tvTiNum;
        private Button btnStartTi;
    }
    /*
     * 实例化Adapter
     */
    public MyQAdapter(Context context, List<QuestionBank> dataSet)
    {
        this.myContext = context;
        this.mQList = dataSet;
        this.layoutinflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mQList.size();
    }

    @Override
    public Object getItem(int position) {
        return mQList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            holder= new ViewHolder();
            view= layoutinflater.inflate(R.layout.item_list_app, null);
            //获取控件
            holder.tvKemu = view.findViewById(R.id.tv_kemu);
            holder.tvTiNum = view.findViewById(R.id.tv_ti_num);
            holder.btnStartTi = view.findViewById(R.id.btn_start_ti);
            view.setTag(holder);
            holder.tvKemu.setText(mQList.get(position).getKemu());
            holder.tvTiNum.setText(mQList.get(position).getTinum()+"");
            Log.i("mxy",mQList.get(position).getKemu()+mQList.get(position).getTinum());
            holder.btnStartTi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mQList.get(position).getKemuSta()==1){
                        Intent intent = new Intent();
                        intent.setClass(myContext, ExerciseActivity.class);
                        intent.putExtra("fenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getTinum()+"&"+mQList.get(position).getKemuSta());
                        myContext.startActivity(intent);
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(myContext, DfActivity.class);
                        intent.putExtra("fenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getTinum()+"&"+mQList.get(position).getKemuSta());
                        myContext.startActivity(intent);
                    }

                }
            });
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
            holder.tvKemu.setText(mQList.get(position).getKemu());
            holder.tvTiNum.setText(mQList.get(position).getTinum()+"");
            Log.i("mxy",mQList.get(position).getKemu()+mQList.get(position).getTinum());
            holder.btnStartTi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mQList.get(position).getKemuSta()==1){
                        Intent intent = new Intent();
                        intent.setClass(myContext, ExerciseActivity.class);
                        intent.putExtra("fenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getTinum()+"&"+mQList.get(position).getKemuSta());
                        myContext.startActivity(intent);
                    }else {
                        Intent intent = new Intent();
                        intent.setClass(myContext, DfActivity.class);
                        intent.putExtra("fenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getTinum()+"&"+mQList.get(position).getKemuSta());
                        myContext.startActivity(intent);
                    }
                }
            });
        }


        return view;
    }


}
