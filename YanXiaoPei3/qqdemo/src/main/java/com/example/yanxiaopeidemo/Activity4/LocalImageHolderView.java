package com.example.yanxiaopeidemo.Activity4;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.example.yanxiaopeidemo.R;

public class LocalImageHolderView extends Holder<Integer> {
    private ImageView mImageView;

    public LocalImageHolderView(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mImageView = itemView.findViewById(R.id.item_image);
    }

    @Override
    public void updateUI(Integer data) {
        mImageView.setImageResource(data);
    }
}
