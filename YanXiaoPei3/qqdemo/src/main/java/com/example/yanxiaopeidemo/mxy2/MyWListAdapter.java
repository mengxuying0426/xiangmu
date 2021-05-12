package com.example.yanxiaopeidemo.mxy2;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.WrongTi;
import com.example.yanxiaopeidemo.utils.StringUtils;

import java.util.List;

public class MyWListAdapter extends BaseAdapter {
    private Context myContext;
    private List<WrongTi> mQList;
    private LayoutInflater layoutinflater;//视图容器，用来导入布局

    static class ViewHolder {
        private TextView tvStemItem;
        private Button btnDetail;
    }
    /*
     * 实例化Adapter
     */
    public MyWListAdapter(Context context, List<WrongTi> dataSet)
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
            view= layoutinflater.inflate(R.layout.wrong_item, null);
            //获取控件
            holder.tvStemItem = view.findViewById(R.id.tv_stem_item);
            holder.btnDetail = view.findViewById(R.id.btn_detail);
            view.setTag(holder);
            String opt = null;
            if(mQList.get(position).getKeynum()>1){
                opt = "(多选题)";
            }else {
               opt = "(单选题)";
            }
            SpannableString highlightText = StringUtils.highlight(myContext, opt+mQList.get(position).getStem(), opt, "#4169E1", 1, 1);
            holder.tvStemItem.setText(highlightText);

            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(myContext, WrongDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("fenlei",mQList.get(position).getKeynum()+"&"+mQList.get(position).getId()+"&"+mQList.get(position).getTista());
                    myContext.startActivity(intent);
                }
            });
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
            String opt = null;
            if(mQList.get(position).getKeynum()>1){
                opt = "(多选题)";
            }else {
                opt = "(单选题)";
            }
            SpannableString highlightText = StringUtils.highlight(myContext, opt+mQList.get(position).getStem(), opt, "#4169E1", 1, 1);
            holder.tvStemItem.setText(highlightText);

            holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(myContext, WrongDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra("fenlei",mQList.get(position).getKeynum()+"&"+mQList.get(position).getId()+"&"+mQList.get(position).getTista());
                    myContext.startActivity(intent);
                }
            });
        }


        return view;
    }

}
