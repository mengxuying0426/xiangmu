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
import com.example.yanxiaopeidemo.beans.WrongSet;

import java.util.List;

public class MyWAdapter extends BaseAdapter {
    private Context myContext;
    private List<WrongSet> mQList;
    private LayoutInflater layoutinflater;//视图容器，用来导入布局

    static class ViewHolder {
        private TextView tvCuotiKu;
        private Button btnStartTi;
    }
    /*
     * 实例化Adapter
     */
    public MyWAdapter(Context context, List<WrongSet> dataSet)
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
            view= layoutinflater.inflate(R.layout.item_list_wrong, null);
            //获取控件
            holder.tvCuotiKu = view.findViewById(R.id.tv_cuotiku);
//            holder.tvTiNum = view.findViewById(R.id.tv_ti_num);
            holder.btnStartTi = view.findViewById(R.id.btn_start_ti);
            view.setTag(holder);
            holder.tvCuotiKu.setText(mQList.get(position).getKemu());
//            holder.tvTiNum.setText(mQList.get(position).getTinum()+"");
            Log.i("mxy",mQList.get(position).getKemu());
            holder.btnStartTi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(myContext, WrongTiListActivity.class);
                    intent.putExtra("tikufenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getKemusta());
                    myContext.startActivity(intent);
                }
            });
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
            holder.tvCuotiKu.setText(mQList.get(position).getKemu());
//            holder.tvTiNum.setText(mQList.get(position).getTinum()+"");
            Log.i("mxy",mQList.get(position).getKemu());
            holder.btnStartTi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(myContext, WrongTiListActivity.class);
                    intent.putExtra("tikufenlei",mQList.get(position).getKemu()+"&"+mQList.get(position).getKemusta());
                    myContext.startActivity(intent);
                }
            });
        }


        return view;
    }

}
