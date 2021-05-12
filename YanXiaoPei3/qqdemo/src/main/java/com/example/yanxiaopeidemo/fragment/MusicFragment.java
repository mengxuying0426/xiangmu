package com.example.yanxiaopeidemo.fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.adapter6.SongAdapter;
import com.example.yanxiaopeidemo.entitys6.SongInfo;
import com.example.yanxiaopeidemo.menu6.DownloadPopupWin;
import com.example.yanxiaopeidemo.menu6.MoreBottomMenu;
import com.example.yanxiaopeidemo.mode6.MyDatabaseHelper6;


import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;



public class MusicFragment extends Fragment implements View.OnClickListener {
    private ArrayList collectList = new ArrayList<Integer>();
    //创建数据库对象属性
    private SQLiteDatabase db;
    private MyDatabaseHelper6 dbHelper;

    //    ArrayList<SongInfo> dataSourceLiebiao = new ArrayList<>();
    private ListView songListView;
    //    private ArrayList songList = new ArrayList<SongInfo>();
    private SongAdapter songAdapter;


    private int timerInfo;
    private int setHour, setMin;
    private TimePickerDialog p;
    private CountDownTimer countDownTimer;
    private TextView tvTimer;
    public TIMER timerState;

    private myThread thread = new myThread();

    @Override
    public void onClick(View v) {

    }

    public enum TIMER {
        NO, T1, T2, T3, T6, T9, T_DIY
    }

    private static final String TAG = "ljn";
    private MoreBottomMenu moreMenuWindow;
    private SeekBar mSeekBar;  //进度条
    private Button mPlayOrPause;
    private Button mPlayPattern;
    private Button mPlayPrevious;
    private Button mPlayNext;
    private Button mPlayList;
    private ImageView ivCover;     //封面
    private TextView mMusicName;   //歌曲名称
    private TextView mMusicArtist; //歌手名称

    private ArrayList songList = new ArrayList<SongInfo>();

    private ArrayList songljList = new ArrayList<String>();

    private int songsTotal;
    public final int PLAY_IN_ORDER = 0;   //顺序播放
    public final int PLAY_SINGLE = 1;    //单曲循环
    public final int PLAY_RANDOM = 2;    //随机播放
    private int playPattern;  //播放状态，0顺序，1单曲，2随机

    private TextView tvSongCurrent;
    private TextView tvSongDuration;
    private boolean isUserTouchProgressBar;

    public enum PlayState {
        STATE_PLAYING, STATE_PAUSE, STATE_STOP
    }// 枚举元素本身由系统定义了一个表示序号的数值，从0开始顺序定义为0，1，2…。

    public PlayState pState;//PlayState变量
    //// 如在weekday中，sun值为0，mon值为1，sat值为6。
    //正播放0，暂停1，停止2

    private ObjectAnimator CircleAnimator;
    private ImageView mRotateCircle;
    private RequestOptions requestOptions = new RequestOptions().circleCropTransform();

    private Button btnShare;
    private Button btnDownload;
    private Button btnTimer;
    private Button btnMore;

    private Button btnCollect;

    //定义MediaPlayer属性
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //定义AssetManager属性
    private AssetManager assetManager;
    //定义标识当前音频的id属性
    private int curId;

    private Handler seekProHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    //设置进度条当前位置为音频播放位置  ,ms
                    mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    break;
            }
        }
    };

    /**
     * 设置定时器
     *
     * @param s
     */
    private void setTime(String s) {
        switch (s) {
            case "10":
                timerState = TIMER.T1;
                stopTimer();//获取当前歌曲剩余时间
                startTimer(transformToS(0, 10, 0), 1);
                break;
            case "20":
                timerState = TIMER.T2;
                stopTimer();
                startTimer(transformToS(0, 20, 0), 1);
                break;
            case "30":
                timerState = TIMER.T3;
                stopTimer();
                startTimer(transformToS(0, 30, 0), 1);
                break;
            case "60":
                timerState = TIMER.T6;
                stopTimer();
                startTimer(transformToS(0, 60, 0), 1);
                break;
            case "90":
                timerState = TIMER.T9;
                stopTimer();
                startTimer(transformToS(0, 90, 0), 1);
                break;
        }
    }

    private Handler setTimerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stopTimer();
                    timerState = TIMER.NO;
                    tvTimer.setText("");
                    break;
                case 10:
                    setTime("10");
                    //设置倒计时时间 为  用户选择位置   mm:ss
                    // mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    break;
                case 20:
                    setTime("20");
                    break;
                case 30:
                    setTime("30");
                    break;
                case 60:
                    setTime("60");
                    break;
                case 90:
                    setTime("90");
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载内容页面的布局文件（将内容页面的XML布局文件转成View类型的对象）
//        //true表示不需要手动调用addView方法
        View inflate = inflater.inflate(R.layout.fragment_music, null);
        findViews(inflate);
        //初始化播放器界面
        initView(inflate, 0);
        setListener();

        mRotateCircle = (ImageView) inflate.findViewById(R.id.iv_cover);
//        //加载第一首歌的图片
//        loadGlidePic(curId);
//        pState = STATE_PAUSE;
        CircleAnimator = ObjectAnimator.ofFloat(mRotateCircle, "rotation", 0.0f, 360.0f);
        CircleAnimator.setDuration(60000);
        CircleAnimator.setInterpolator(new LinearInterpolator());
        CircleAnimator.setRepeatCount(-1);
        CircleAnimator.setRepeatMode(ObjectAnimator.RESTART);

        //获取数据库对象
        dbHelper = new MyDatabaseHelper6
                (getContext(), "SongsN.db", null, 3);
        db = dbHelper.getWritableDatabase();
        //MediaPlayer初始化
        initMediaPlayer();
        // final TextView tvDes = view.findViewById(R.id.tv_description);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tvDes.setText("拖动停止");
                //当拖动停止后，控制mediaPlayer播放指定位置的音乐
                mediaPlayer.seekTo(seekBar.getProgress());
                isUserTouchProgressBar = false;
            }

            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // tvDes.setText("开始拖动");
                isUserTouchProgressBar = true;
            }

            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //设置改变当前播放的分秒数
                int currentPos = mediaPlayer.getCurrentPosition();
                tvSongCurrent.setText(transformToHMS(currentPos));
            }
        });

        if (thread.isAlive()) {
            Log.e("thread:", "alive");
        } else {
            //thread.stop();
            (thread = new myThread()).start();
            Log.e("mythread:", "start");
        }
        return inflate;
    }

    public void loadGlidePic(int curid) {
        String picName = ((SongInfo) (songList.get(curid - 1))).getSongCover();
        int DrawId = getImageId(picName);
        Glide.with(this)
                .asBitmap()
                .load(DrawId)
                .apply(requestOptions)
                .into(mRotateCircle);
    }

    /**
     * 结束倒计时
     */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            Log.i("ljn", "jieshu");
        }
    }

    /**
     * 开启倒计时
     */
    private void startTimer(int general, int interval) {//变成秒值
        Log.i("ljn", "kaiqidaojishi");
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(general * 1000, interval * 1000) {
                public void onTick(long millisUntilFinished) {
                    // tvTimerSec.setText("seconds remaining: " + millisUntilFinished / 1000);
                    // tvTimer.setText("剩余（h:m:s）：" + transformToHMS((int) millisUntilFinished));
                    tvTimer.setText("音乐将在" + transformToHMS((int) millisUntilFinished) + "后停止");
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public void onFinish() {
                    pauseAnim();  //1 暂停anim   STATE_PLAYING  ，切换暂停
                    pState = PlayState.STATE_PAUSE; //也会设置 STATE_PAUSE
                    mediaPlayer.pause();  //暂停音乐，此时pState暂停。

                    tvTimer.setText("");
                    mPlayOrPause.setBackgroundResource(R.drawable.r_play);
                    loadGlidePic(curId);
                    timerState = TIMER.NO;
                }
            };
        }
        countDownTimer.start();
    }

    /**
     * 将毫秒数转为时分秒
     */
    public String transformToHMS(int msT) {
        String hms;
        if (msT >= 60 * 60 * 1000) { //如果大于等于一小时
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            //这里想要只保留分秒可以写成"mm:ss"------"HH:mm:ss"
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            hms = formatter.format(msT);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            //这里想要只保留分秒可以写成"mm:ss"------"HH:mm:ss"
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            hms = formatter.format(msT);
        }
        return hms;
    }

    /**
     * 将时分秒转化为s数
     *
     * @param h
     * @param tMin
     * @param tSec
     * @return
     */
    public int transformToS(int h, int tMin, int tSec) {
        int sumS = 0;
        sumS = h * 60 * 60 + tMin * 60 + tSec;
        return sumS;
    }

    /**
     * 准备歌曲文件
     */
    private void prepareSongs() {
        Cursor cur = db.query("SongsN", null, null, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        cur.moveToFirst();
        if (cur.getCount() == 0) {
        } else {
            songList.clear();
            if (cur.moveToFirst()) {
                // 遍历游标
                do {
                    int sId = cur.getInt(cur.getColumnIndex("songId"));
                    String sName = cur.getString(cur.getColumnIndex("songName"));
                    String sArtist = cur.getString(cur.getColumnIndex("songArtist"));
                    int sCollect = cur.getInt(cur.getColumnIndex("collect"));
                    String sCover = cur.getString(cur.getColumnIndex("songCover"));
                    String sFile = cur.getString(cur.getColumnIndex("songFile"));
                    Log.e("SongsN.db", "-songId：" + sId + "-songName：" + sName + "-songArtist:" + sArtist + "-collect:" + sCollect
                            + "-songCover:" + sCover + "-songFile:" + sFile);
                    SongInfo itemData1 = new SongInfo(sId, sName, sArtist, sCollect, sCover, sFile);
                    // 定义第1个数据项
                    songList.add(itemData1);
                } while (cur.moveToNext());
            }
        }

        String lj1 = "https://c.y.qq.com/base/fcgi-bin/u?__=aVXsjyI";
        String lj2 = "https://c.y.qq.com/base/fcgi-bin/u?__=qUyyE5";
        String lj3 = "https://c.y.qq.com/base/fcgi-bin/u?__=mN89e4K";
        String lj4 = "https://c.y.qq.com/base/fcgi-bin/u?__=CbWTHdd";
        String lj5 = "https://c.y.qq.com/base/fcgi-bin/u?__=NsAyvhs";
        String lj6 = "https://c.y.qq.com/base/fcgi-bin/u?__=h5Ltl";
        songljList.add(lj1);
        songljList.add(lj2);
        songljList.add(lj3);
        songljList.add(lj4);
        songljList.add(lj5);
        songljList.add(lj6);
    }

    //make it public -protected
    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭数据库（如果是打开状态时）
        if (db.isOpen()) {
            db.close();
        }
    }

    /**
     * 初始化MediaPlayer
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initMediaPlayer() {
        if (mediaPlayer.isPlaying() || mediaPlayer.isLooping()) {
                       pState = PlayState.STATE_PLAYING;
            Log.e("curId:", "" + curId);

            //添加的
            loadSongInfo(curId);
            loadGlidePic(curId);
            //设置歌曲时长（分：秒）
            int songDuration = mediaPlayer.getDuration();
            tvSongDuration.setText(transformToHMS(songDuration));

            mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
            //添加的，设置进度条最大长度
            mSeekBar.setMax(mediaPlayer.getDuration());
            loadGlidePic(curId);
            beginAnim();
            if (playPattern == 0) {
                mPlayPattern.setBackgroundResource(R.drawable.r_liexun);
            } else if (playPattern == 1) {
                mPlayPattern.setBackgroundResource(R.drawable.r_danqu);
            } else {
                mPlayPattern.setBackgroundResource(R.drawable.r_suiji);
            }

//            getCollect();
//            setCurCollect();
            Log.e("curId:", "" + curId);
//            pauseAnim();
        } else {
            //加载歌曲对象
            prepareSongs();//只在第一次创建时 loadsong  否则每次都引入
            pState = PlayState.STATE_PAUSE;
            timerState = TIMER.NO;
            //AssetManager对象获取
            assetManager = getContext().getAssets();//getContext()!!!
            //getResourcesInternal().getAssets();
            //预加载第1首MP3
            curId = 1;
            loadMP3(1);
            loadSongInfo(1);
            loadGlidePic(curId);
            //设置歌曲时长（分：秒）
            int songDuration = mediaPlayer.getDuration();
            tvSongDuration.setText(transformToHMS(songDuration));
            //添加的，设置进度条最大长度
            mSeekBar.setMax(mediaPlayer.getDuration());
            //给MediaPlayer注册播放完成事件监听器(顺序播放下一首)
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (pState == PlayState.STATE_PAUSE) {
                        //如果现在播放状态是暂停。那么就不管
                    } else if (pState == PlayState.STATE_PLAYING) { //如果不是暂停，就是正在播放
                        //计算下一个音频文件id
                        curId++;
                        if (curId > 6) {
                            curId = 1;
                        }
                        Log.e("注册MediaPlay播放完监听器", "");
                        //加载音频数据源
                        loadMP3(curId);
                        loadSongInfo(curId);
                        //启动播放
                        mediaPlayer.start();

                        //添加的
                        mSeekBar.setMax(mediaPlayer.getDuration());
                        loadGlidePic(curId);
                        beginAnim();
                        playPattern = 0;
                        Log.e("completatioon-curId:", "" + curId);
                    }

                }
            });
        }
    }

    private void setCurCollect() {
        if (  ((SongInfo)(songList.get( (curId-1==-1) ? 5:(curId-1) ))).getCollect()== 0){
            btnCollect.setBackgroundResource(R.drawable.r_heart0);
            Log.i("ljn!!!!!","当前id"+curId + "collcet" +  ((SongInfo)(songList.get( (curId-1==-1) ? 5:(curId-1) ))).getCollect());
        }else if (  ((SongInfo)(songList.get( (curId-1==-1) ? 5:(curId-1) ))).getCollect()== 1){
            btnCollect.setBackgroundResource(R.drawable.r_heart1);
        }
    }


    private void getCollect() {
        if (!db.isOpen()){
            //获取数据库对象
            dbHelper = new MyDatabaseHelper6
                    (getContext(), "SongsN.db", null, 3);
            db = dbHelper.getWritableDatabase();
        }
        Cursor cur = db.query("SongsN", null, null, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        cur.moveToFirst();
        if (cur.getCount() == 0) {
        } else {
            if (cur.moveToFirst()) {//获得collcet数组
                // 遍历游标
                do {//int 转 integer  拆箱装箱
                    int sCollect = cur.getInt(cur.getColumnIndex("collect"));
                    Log.e("SongsN.db",   "-collect:" + sCollect);
                    collectList.add(new Integer(sCollect));
                } while (cur.moveToNext());
            }
            //修改对象collect
            for (int i = 0; i < songList.size();i++){
                ((SongInfo)(songList.get(i))).setCollect(((Integer)collectList.indexOf(i)).intValue());
                Log.i("songList.toString",songList.toString());//拆箱
            }
        }
    }

    private void beginAnim() {
        CircleAnimator.start();
        pState = PlayState.STATE_PLAYING;//状态，正播放
    }

    /**
     * 给MediaPlayer对象设置音频数据源，并加载准备好
     *
     * @param id 待加载的音频文件id
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadMP3(int id) {
        try {
            //重置MediaPlayer
            mediaPlayer.reset();
            //获取当前指定的MP3文件
            AssetFileDescriptor descriptor =
                    assetManager.openFd("0" + id + ".mp3");
            //给MediaPlayer对象设置音频数据源为当前MP3
            mediaPlayer.setDataSource(descriptor);
            //加载准备好
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断两次点击事件的间隔
     */
    public static class ClickHelper {
        private static long lastClickTime = 0;

        /**
         * 判断事件出发时间间隔是否超过预定值
         * 如果小于间隔（目前是1000毫秒）则返回true，否则返回false
         */
        public static boolean isFastDoubleClick() {
            long time = System.currentTimeMillis();
            long timeD = time - lastClickTime;
            if (0 < timeD && timeD < 1000) {
                return true;
            }
            lastClickTime = time;
            return false;
        }
    }


    //监听器类
    public class MyListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            if (ClickHelper.isFastDoubleClick()) {//连续点击
                return;
            }
            switch (view.getId()) {
                case R.id.btn_play_pattern://设置播放模式
                    if (playPattern == 0) {
                        playPattern = 1;
                        mPlayPattern.setBackgroundResource(R.drawable.r_danqu);
                    } else if (playPattern == 1) {
                        playPattern = 2;
                        mPlayPattern.setBackgroundResource(R.drawable.r_suiji);
                    } else {
                        playPattern = 0;
                        mPlayPattern.setBackgroundResource(R.drawable.r_liexun);
                    }
                    break;
                case R.id.btn_play_pre://上一曲
                    //顺序播放
                    if (playPattern == 0) {
                        //修改当前音频文件id
                        curId--;
                        if (curId < 1) {
                            curId = 6;
                        }
                        loadSongInfo(curId);
                    }
                    //单曲循环
                    else if (playPattern == 1) {
                    }
                    //随机播放
                    else if (playPattern == 2) {
                        curId = (int) (Math.random() * 6) + 1;//加1,避免出现0导致负数  为啥要加curID啊！！
                        Log.i("loadSongInfo之前curId=", "" + curId);
                        loadSongInfo(curId);
                        Log.i("loadSongInfo之后curId=", "" + curId);
                    }

                    //加载相应的音频文件
                    loadMP3(curId);
                    //开始播放
                    mediaPlayer.start();
                    loadGlidePic(curId);
                    beginAnim();

                    //设置歌曲时长（分：秒）
                    int songDuration = mediaPlayer.getDuration();
                    tvSongDuration.setText(transformToHMS(songDuration));

                    //添加的，设置进度条最大长度
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    //修改播放/暂停按钮的图片为“暂停播放”
                    mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                    break;

                case R.id.btn_play_or_pause://暂停/继续播放
                    //如果正在播放，则暂停；否则，开始播放；
                    // 相应的修改背景图像
                    if (mediaPlayer.isPlaying()) {//如果正在播放，则暂停
                        mediaPlayer.pause();
                        //修改当前的背景图片为“开始播放”
                        //stopTimer();
                        mPlayOrPause.setBackgroundResource(R.drawable.r_play);
                        loadGlidePic(curId);
                        pauseAnim();
                    } else {//没有正在播放，则开始播放
                        mediaPlayer.start();
                        //修改当前的背景图片为“暂停播放”
                        mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                        pauseAnim();
                    }
                    break;
                case R.id.btn_play_next://下一曲
                    //顺序播放
                    if (playPattern == 0) {
                        //修改当前音频Id
                        curId++;
                        if (curId > 6) {
                            curId = 1;
                        }
                        loadSongInfo(curId);
                    }

                    //单曲循环
                    else if (playPattern == 1) {

                    }
                    //随机播放
                    else if (playPattern == 2) {
                        songsTotal = songList.size();
                        int r = 0;
                        //(int)(Math.random()*6)+1
                        curId = (int) (Math.random() * 6) + 1;
                        // curId = (r = (curId + (int) (Math.random() * 6)+1) % songsTotal) == curId ? curId+3: r ;
                        Log.i("r=", "" + r);
                        loadSongInfo(curId);
                    }
                    //加载音频
                    loadMP3(curId);
                    //播放
                    mediaPlayer.start();
                    loadGlidePic(curId);
                    beginAnim();
                    //设置歌曲时长
                    int songDuration1 = mediaPlayer.getDuration();
                    tvSongDuration.setText(transformToHMS(songDuration1));

                    //添加的，设置进度条最大长度
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    //修改播放/暂停播放按钮的背景图片为“暂停播放”
                    mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                    break;
                case R.id.btn_play_list://播放列表
                    showSongList(playPattern,(curId-1==-1)?5:(curId-1));

                    break;
                case R.id.btn_collect: //收藏曲目
                    int c1 = ((SongInfo) (songList.get(curId - 1))).getCollect();
                    Log.i("CC---curId-1=",curId-1+":"+c1);
                    if (c1 == 0) {
                        Log.i("CC---curId-1=",curId-1+""+c1);
                        Log.i("未收藏", "before");
                        ((SongInfo) (songList.get(curId - 1))).setCollect(1);
                        ContentValues values = new ContentValues();
                        //存放更新后的信息
                        values.put("collect", 1);
                        db.update("SongsN", values, "songId=?", new String[]{curId + ""});
                        Log.e("已收藏：", "song:" + ((SongInfo) (songList.get(curId - 1))).getSongName());
                        Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
//                        dataSource.remove(position);
//                        notifyDataSetChanged();
                        btnCollect.setBackgroundResource(R.drawable.r_heart1);
                    } else if (c1 == 1){
                        Log.i("已经收藏", "before");

                        Log.i("CC---curId-1=",curId-1+""+c1);
                        ((SongInfo) (songList.get(curId - 1))).setCollect(0);
                        ContentValues values = new ContentValues();
                        //存放更新后的信息
                        values.put("collect", 0);
                        db.update("SongsN", values, "songId=?", new String[]{curId + ""});
                        Log.e("取消收藏：", "song:" + ((SongInfo) (songList.get(curId - 1))).getSongName());
                        Toast.makeText(getContext(), "取消收藏成功", Toast.LENGTH_SHORT).show();
                        btnCollect.setBackgroundResource(R.drawable.r_heart0);
                    }
                    break;
                case R.id.btn_share: //分享
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "我给你分享了纯音乐《" + ((SongInfo) (songList.get(curId - 1))).getSongName() + "》" + songljList.get(curId - 1));
                    startActivity(Intent.createChooser(shareIntent, "分享到"));
                    break;
                case R.id.btn_download://下载
                    Intent iDld = new Intent();
                    iDld.setClass(getContext(), DownloadPopupWin.class);
                    startActivity(iDld);
                    break;
                case R.id.btn_timer://定时
                    showTimerPopupWindow(timerState);

                    break;
                case R.id.btn_more: //更多
                    moreMenuWindow = new MoreBottomMenu(
                            getActivity(),
                            this,
                            ((SongInfo) (songList.get(curId - 1))).getSongName());
                    moreMenuWindow.show();
                    break;
            }
        }
    }

    /**
     * 弹出popupwindow展示歌曲信息
     */
    private void showSongList(int pattern,int list_id) {
        songAdapter = new SongAdapter(
                getContext(),       // 上下文环境
                songList, // 数据源
                R.layout.layout_song_item // 列表项布局文件
        );
        final View inflatedView = getLayoutInflater().inflate(R.layout.layout_songlist, null);
        ListView lv = inflatedView.findViewById(R.id.lv);
        lv.setAdapter(songAdapter);
        ImageView ivP = inflatedView.findViewById(R.id.iv_pattern);
        TextView tvP = inflatedView.findViewById(R.id.tv_pattern);



        switch (pattern) {
            case 0:
                ivP.setImageResource(R.drawable.l_liebiao);
                tvP.setText("列表循环");
                break;
            case 1:
                ivP.setImageResource(R.drawable.l_danqu);
                tvP.setText("单曲循环");
                break;
            case 2:
                ivP.setImageResource(R.drawable.l_suiji1);
                tvP.setText("随机播放");
                break;
        }
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(inflatedView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// 设置允许在外点击消失

        View.OnTouchListener touchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = inflatedView.findViewById(R.id.layout_songs).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popWindow.dismiss();
                    }
                }
                return true;
            }
        };
        inflatedView.setOnTouchListener(touchListener);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curId = position + 1;
//                        Log.i("列表中id=",position + "---" + id);
                loadSongInfo(curId);
                //加载音频
                loadMP3(curId);
                //播放
                mediaPlayer.start();
                loadGlidePic(curId);
                beginAnim();
                //设置歌曲时长
                int songDuration1 = mediaPlayer.getDuration();
                tvSongDuration.setText(transformToHMS(songDuration1));

                //添加的，设置进度条最大长度
                mSeekBar.setMax(mediaPlayer.getDuration());
                //修改播放/暂停播放按钮的背景图片为“暂停播放”
                mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.l_cancel:
                        break;
                }
                popWindow.dismiss();
            }
        };
        inflatedView.findViewById(R.id.l_cancel).setOnClickListener(listener);
//        inflatedView.findViewById(R.id.tv_f_ok).setOnClickListener(listener);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    private void showTimerPopupWindow(TIMER timerstate) {
        Log.e("timerstate", "" + timerstate);
        final View inflatedView = getLayoutInflater().inflate(R.layout.timer_pop_menu, null);
        RadioGroup radioGroup;

        Log.e("ljn", "获取控件");

        radioGroup = (RadioGroup) (inflatedView.findViewById(R.id.radioGroup));
        setCheckedButton(timerstate, radioGroup);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(inflatedView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// 设置允许在外点击消失

        View.OnTouchListener touchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = inflatedView.findViewById(R.id.layout_timer).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        popWindow.dismiss();
                    }
                }
                return true;
            }
        };
        inflatedView.setOnTouchListener(touchListener);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.t_diy: {
                        (p = new TimePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                setHour = hourOfDay;//选择的结果
                                setMin = minute;
                                timerState = TIMER.T_DIY;
                                stopTimer();//获取当前歌曲剩余时间
                                startTimer(transformToS(setHour, setMin, 0), 1);
                            }
                        }, 0, 0, true)
                        ).show();
                        popWindow.dismiss();
                    }
                    case R.id.t_no:
                        timerInfo = 0;
                        break;
                    case R.id.t_10:
                        timerInfo = 10;
                        break;
                    case R.id.t_20:
                        timerInfo = 20;
                        break;
                    case R.id.t_30:
                        timerInfo = 30;
                        break;
                    case R.id.t_60:
                        timerInfo = 60;
                        break;
                    case R.id.t_90:
                        timerInfo = 90;
                        break;
                }
            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_f_dld:
                        popWindow.dismiss();
                        break;
                    case R.id.tv_f_ok://提交的时候，得到id
                        Message m = new Message();
                        m.what = timerInfo;
                        setTimerHandler.sendMessage(m);
                        popWindow.dismiss();
                        break;
                }

            }
        };
        inflatedView.findViewById(R.id.tv_f_dld).setOnClickListener(listener);
        inflatedView.findViewById(R.id.tv_f_ok).setOnClickListener(listener);
        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        popWindow.showAtLocation(inflatedView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void setCheckedButton(TIMER timerstate1, RadioGroup rg) {
        switch (timerstate1) {
            case NO:
                break;
            case T1:
                rg.check(R.id.t_10);
                break;
            case T2:
                rg.check(R.id.t_20);
                break;
            case T3:
                rg.check(R.id.t_30);
                break;
            case T6:
                rg.check(R.id.t_60);
                break;
            case T9:
                rg.check(R.id.t_90);
                break;
            case T_DIY:
                rg.check(R.id.t_diy);
                break;
        }
    }

    /**
     * 暂停/继续播放，修改pState  与  animator暂停，继续
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pauseAnim() {
        if (pState.equals(PlayState.STATE_PLAYING)) {//此时正播放,转为暂停播放
            CircleAnimator.pause();
            pState = PlayState.STATE_PAUSE;
        } else if (pState.equals(PlayState.STATE_PAUSE)) {  //此时是暂停播放，转为正播放
            if (CircleAnimator.isStarted())
                CircleAnimator.resume();
            else {
                CircleAnimator.start();
            }
            pState = PlayState.STATE_PLAYING;
        }
    }

    /**
     * 加载歌曲信息界面
     */
    private void loadSongInfo(int curId) {
        //下标是顺序-1
        Log.i("减之前curId=", "" + curId);//  ((curId - 1)==-1) ? 5:(curId - 1)
        int c = ((SongInfo) (songList.get(curId - 1))).getCollect();//TODO
        Log.i("减之后curId=", "" + curId);
        if (c == 0) {
            btnCollect.setBackgroundResource(R.drawable.r_heart0);
        } else if (c == 1)
            btnCollect.setBackgroundResource(R.drawable.r_heart1);//curId - 1
        String sCover = ((SongInfo) (songList.get(curId - 1))).getSongCover();
        ivCover.setImageResource(getImageId(sCover));
        String sName = ((SongInfo) (songList.get(curId - 1))).getSongName();
        String sArtist = ((SongInfo) (songList.get(curId - 1))).getSongArtist();

        mMusicName.setText(sName);
        mMusicArtist.setText(sArtist);
        tvSongCurrent.setText("00:00");
    }

    /**
     * 根据图片名称获取图片资源id  Drawable
     *
     * @param name
     * @return
     */
    public static int getImageId(String name) {
        Class drawable = R.drawable.class;
        Field field = null;
        try {
            field = drawable.getField(name);
            int imageId = field.getInt(field.getName());
            return imageId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 给view控件设置监听器
     */
    private void setListener() {
        MyListener myListener = new MyListener();

        mPlayPattern.setOnClickListener(myListener);
        mPlayPrevious.setOnClickListener(myListener);
        mPlayOrPause.setOnClickListener(myListener);
        mPlayNext.setOnClickListener(myListener);
        mPlayList.setOnClickListener(myListener);

        btnShare.setOnClickListener(myListener);
        btnDownload.setOnClickListener(myListener);
        btnTimer.setOnClickListener(myListener);
        btnMore.setOnClickListener(myListener);

        btnCollect.setOnClickListener(myListener);
    }

    /**
     * 获取View控件
     *
     * @param v
     */
    private void findViews(View v) {
        tvTimer = v.findViewById(R.id.tv_timer);
        ivCover = v.findViewById(R.id.iv_cover);
        mMusicName = v.findViewById(R.id.tv_music_name);
        mMusicArtist = v.findViewById(R.id.tv_music_artist);
        mSeekBar = v.findViewById(R.id.seek_bar);
        mPlayPattern = v.findViewById(R.id.btn_play_pattern);
        mPlayPrevious = v.findViewById(R.id.btn_play_pre);
        mPlayOrPause = v.findViewById(R.id.btn_play_or_pause);
        mPlayNext = v.findViewById(R.id.btn_play_next);
        mPlayList = v.findViewById(R.id.btn_play_list);

        btnShare = v.findViewById(R.id.btn_share);
        btnTimer = v.findViewById(R.id.btn_timer);
        btnDownload = v.findViewById(R.id.btn_download);
        btnMore = v.findViewById(R.id.btn_more);

        btnCollect = v.findViewById(R.id.btn_collect);
        tvSongCurrent = v.findViewById(R.id.tv_proLeft);
        tvSongDuration = v.findViewById(R.id.tv_lenRight);
    }

    //设置一个线程运行进度条
    class myThread extends Thread {
        @Override
        public void run() {
            super.run();
            //判断当前播放位置是否小于总时长
            int i = 5;
            while (i > 0) {
                if (mediaPlayer != null && pState == PlayState.STATE_PLAYING && !isUserTouchProgressBar) {
                    Message message = new Message();
                    message.what = 100;
                    seekProHandler.sendMessage(message);
                    Log.e("send", "message1");
                }
                try {
                    Thread.sleep(100);//ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化界面
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(View view, int playPattern) {
        //模式转换
        if (playPattern == PLAY_IN_ORDER) {
            mPlayPattern.setBackgroundResource(R.drawable.r_liexun);
        } else if (playPattern == PLAY_SINGLE) {
            mPlayPattern.setBackgroundResource(R.drawable.r_danqu);
        } else if (playPattern == PLAY_RANDOM) {
            mPlayPattern.setBackgroundResource(R.drawable.r_suiji);
        }
        //获取音乐列表
        //  getMusicListThread();
        mSeekBar.getThumb().setColorFilter(Color.parseColor("WHITE"), PorterDuff.Mode.SRC_ATOP);//设置滑块颜色、样式
        mSeekBar.getProgressDrawable().setColorFilter(Color.parseColor("WHITE"), PorterDuff.Mode.SRC_ATOP);//设置进度条颜色、样式
    }

}
