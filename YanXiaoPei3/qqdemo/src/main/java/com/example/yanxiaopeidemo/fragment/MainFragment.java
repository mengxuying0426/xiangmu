package com.example.yanxiaopeidemo.fragment;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yanxiaopeidemo.MainActivity;
import com.example.yanxiaopeidemo.R;

public class MainFragment extends Fragment {


    private static final String TAG = "MainFragment";
    private String mTagtext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Bundle arguments = getArguments();
        //mTagtext = arguments.getString(MainActivity.TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        View inflate = inflater.inflate(R.layout.fragment_main, null);
        TextView tvText = (TextView) inflate.findViewById(R.id.tv_text);
        if (mTagtext != null && !TextUtils.isEmpty(mTagtext)) {
            tvText.setText(mTagtext);
        } else {
            Log.i(TAG, "onCreateView: mTagText -- " + mTagtext);
            tvText.setText("Null");
        }

        return inflate;
    }
}
