package com.example.yanxiaopeidemo.menu6;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.entitys6.SongInfo;

import static android.content.Context.AUDIO_SERVICE;
import static android.media.AudioManager.STREAM_MUSIC;


public class MoreBottomMenu implements View.OnClickListener, View.OnTouchListener {

    private TextView tvVolume;
    private AudioManager mAudioManager;
    private TextView tvSongName;
    private PopupWindow popupWindow;
    private SeekBar skVol;
    private LinearLayout  lmAdd, lmBuy, lmDel, lmRing, lmZhuti,lmShare;
    private View mMenuView;
    private Activity mContext;
    private View.OnClickListener clickListener;

    public MoreBottomMenu(Activity context, View.OnClickListener clickListener, String songName) {
        //一切用到context的都要先加载了context再说！！！
        LayoutInflater inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
        mContext = context;
        mMenuView = inflater.inflate(R.layout.layout_bottom_menu_more, null);
        //tvVolume = mMenuView.findViewById(R.id.tv_volume);

        skVol = mMenuView.findViewById(R.id.sk_vol);
        lmAdd = mMenuView.findViewById(R.id.m_add);
        lmBuy = mMenuView.findViewById(R.id.m_buy);
        lmDel = mMenuView.findViewById(R.id.m_del);
        lmRing = mMenuView.findViewById(R.id.m_ring);
        lmZhuti = mMenuView.findViewById(R.id.m_zhuti);
        lmShare =mMenuView.findViewById(R.id.m_share);

        tvSongName = mMenuView.findViewById(R.id.tv_m_song_name);
        tvSongName.setText(songName);

        lmZhuti.setOnClickListener(this);
        lmRing.setOnClickListener(this);
        lmBuy.setOnClickListener(this);
        lmAdd.setOnClickListener(this);
        lmDel.setOnClickListener(this);
        lmShare.setOnClickListener(this);

        //加载音频管理器
        mAudioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        //获得当前的音量大小
        int progress = mAudioManager.getStreamVolume(STREAM_MUSIC);
        //根据当前音量设置seekBar进度
        skVol.setProgress(progress);
        //可作监测使用，设置一个tv
//        tvVolume.setText("" + progress);
        skVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i("ljn", "用户拖动进度");
                //tvVolume.setText("当前音量：" + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i("ljn", "用户开始拖动");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i("ljn", "用户结束拖动，并修改音量");
                mAudioManager.setStreamVolume(STREAM_MUSIC, skVol.getProgress(), AudioManager.FLAG_SHOW_UI);
            }
        });

        mMenuView.findViewById(R.id.vol_d).setOnClickListener(this);
        mMenuView.findViewById(R.id.vol_u).setOnClickListener(this);
        mMenuView.findViewById(R.id.tv_m_cancel).setOnClickListener(this);

        popupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.ccc));
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setClippingEnabled(true);
        mMenuView.setOnTouchListener(this);
    }

    /**
     * 显示菜单
     */
    public void show() {
        //得到当前activity的rootView
        View rootView = ((ViewGroup) mContext.findViewById(android.R.id.content)).getChildAt(0);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent event) {
        int height = mMenuView.findViewById(R.id.layout_more).getTop();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (y < height) {
                popupWindow.dismiss();
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.m_share:
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "我给你分享了纯音乐《" + ((SongInfo) (songList.get(curId - 1))).getSongName() + "》" + songljList.get(curId - 1));
//                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
            case R.id.m_add:
                break;
            case R.id.m_buy://跳转开通vip
                break;
            case R.id.m_ring:
                break;
            case R.id.m_zhuti://设置主题
                Intent i = new Intent();
                break;
            case R.id.m_del:
                break;
            case R.id.tv_m_cancel:
                popupWindow.dismiss();
                break;
        }
        popupWindow.dismiss();
    }
}
