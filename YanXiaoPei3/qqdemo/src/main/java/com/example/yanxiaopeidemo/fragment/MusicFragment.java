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
    //???????????????????????????
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
    private SeekBar mSeekBar;  //?????????
    private Button mPlayOrPause;
    private Button mPlayPattern;
    private Button mPlayPrevious;
    private Button mPlayNext;
    private Button mPlayList;
    private ImageView ivCover;     //??????
    private TextView mMusicName;   //????????????
    private TextView mMusicArtist; //????????????

    private ArrayList songList = new ArrayList<SongInfo>();

    private ArrayList songljList = new ArrayList<String>();

    private int songsTotal;
    public final int PLAY_IN_ORDER = 0;   //????????????
    public final int PLAY_SINGLE = 1;    //????????????
    public final int PLAY_RANDOM = 2;    //????????????
    private int playPattern;  //???????????????0?????????1?????????2??????

    private TextView tvSongCurrent;
    private TextView tvSongDuration;
    private boolean isUserTouchProgressBar;

    public enum PlayState {
        STATE_PLAYING, STATE_PAUSE, STATE_STOP
    }// ?????????????????????????????????????????????????????????????????????0?????????????????????0???1???2??????

    public PlayState pState;//PlayState??????
    //// ??????weekday??????sun??????0???mon??????1???sat??????6???
    //?????????0?????????1?????????2

    private ObjectAnimator CircleAnimator;
    private ImageView mRotateCircle;
    private RequestOptions requestOptions = new RequestOptions().circleCropTransform();

    private Button btnShare;
    private Button btnDownload;
    private Button btnTimer;
    private Button btnMore;

    private Button btnCollect;

    //??????MediaPlayer??????
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //??????AssetManager??????
    private AssetManager assetManager;
    //???????????????????????????id??????
    private int curId;

    private Handler seekProHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    //????????????????????????????????????????????????  ,ms
                    mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    break;
            }
        }
    };

    /**
     * ???????????????
     *
     * @param s
     */
    private void setTime(String s) {
        switch (s) {
            case "10":
                timerState = TIMER.T1;
                stopTimer();//??????????????????????????????
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
                    //????????????????????? ???  ??????????????????   mm:ss
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
        //??????????????????????????????????????????????????????XML??????????????????View??????????????????
//        //true???????????????????????????addView??????
        View inflate = inflater.inflate(R.layout.fragment_music, null);
        findViews(inflate);
        //????????????????????????
        initView(inflate, 0);
        setListener();

        mRotateCircle = (ImageView) inflate.findViewById(R.id.iv_cover);
//        //???????????????????????????
//        loadGlidePic(curId);
//        pState = STATE_PAUSE;
        CircleAnimator = ObjectAnimator.ofFloat(mRotateCircle, "rotation", 0.0f, 360.0f);
        CircleAnimator.setDuration(60000);
        CircleAnimator.setInterpolator(new LinearInterpolator());
        CircleAnimator.setRepeatCount(-1);
        CircleAnimator.setRepeatMode(ObjectAnimator.RESTART);

        //?????????????????????
        dbHelper = new MyDatabaseHelper6
                (getContext(), "SongsN.db", null, 3);
        db = dbHelper.getWritableDatabase();
        //MediaPlayer?????????
        initMediaPlayer();
        // final TextView tvDes = view.findViewById(R.id.tv_description);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * ????????????????????????????????????
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //tvDes.setText("????????????");
                //???????????????????????????mediaPlayer???????????????????????????
                mediaPlayer.seekTo(seekBar.getProgress());
                isUserTouchProgressBar = false;
            }

            /**
             * ????????????????????????????????????
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // tvDes.setText("????????????");
                isUserTouchProgressBar = true;
            }

            /**
             * ????????????????????????????????????
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                //????????????????????????????????????
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
     * ???????????????
     */
    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            Log.i("ljn", "jieshu");
        }
    }

    /**
     * ???????????????
     */
    private void startTimer(int general, int interval) {//????????????
        Log.i("ljn", "kaiqidaojishi");
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(general * 1000, interval * 1000) {
                public void onTick(long millisUntilFinished) {
                    // tvTimerSec.setText("seconds remaining: " + millisUntilFinished / 1000);
                    // tvTimer.setText("?????????h:m:s??????" + transformToHMS((int) millisUntilFinished));
                    tvTimer.setText("????????????" + transformToHMS((int) millisUntilFinished) + "?????????");
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                public void onFinish() {
                    pauseAnim();  //1 ??????anim   STATE_PLAYING  ???????????????
                    pState = PlayState.STATE_PAUSE; //???????????? STATE_PAUSE
                    mediaPlayer.pause();  //?????????????????????pState?????????

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
     * ???????????????????????????
     */
    public String transformToHMS(int msT) {
        String hms;
        if (msT >= 60 * 60 * 1000) { //???????????????????????????
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            //???????????????????????????????????????"mm:ss"------"HH:mm:ss"
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            hms = formatter.format(msT);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
            //???????????????????????????????????????"mm:ss"------"HH:mm:ss"
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            hms = formatter.format(msT);
        }
        return hms;
    }

    /**
     * ?????????????????????s???
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
     * ??????????????????
     */
    private void prepareSongs() {
        Cursor cur = db.query("SongsN", null, null, null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();
        cur.moveToFirst();
        if (cur.getCount() == 0) {
        } else {
            songList.clear();
            if (cur.moveToFirst()) {
                // ????????????
                do {
                    int sId = cur.getInt(cur.getColumnIndex("songId"));
                    String sName = cur.getString(cur.getColumnIndex("songName"));
                    String sArtist = cur.getString(cur.getColumnIndex("songArtist"));
                    int sCollect = cur.getInt(cur.getColumnIndex("collect"));
                    String sCover = cur.getString(cur.getColumnIndex("songCover"));
                    String sFile = cur.getString(cur.getColumnIndex("songFile"));
                    Log.e("SongsN.db", "-songId???" + sId + "-songName???" + sName + "-songArtist:" + sArtist + "-collect:" + sCollect
                            + "-songCover:" + sCover + "-songFile:" + sFile);
                    SongInfo itemData1 = new SongInfo(sId, sName, sArtist, sCollect, sCover, sFile);
                    // ?????????1????????????
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
        //?????????????????????????????????????????????
        if (db.isOpen()) {
            db.close();
        }
    }

    /**
     * ?????????MediaPlayer
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initMediaPlayer() {
        if (mediaPlayer.isPlaying() || mediaPlayer.isLooping()) {
                       pState = PlayState.STATE_PLAYING;
            Log.e("curId:", "" + curId);

            //?????????
            loadSongInfo(curId);
            loadGlidePic(curId);
            //?????????????????????????????????
            int songDuration = mediaPlayer.getDuration();
            tvSongDuration.setText(transformToHMS(songDuration));

            mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
            //???????????????????????????????????????
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
            //??????????????????
            prepareSongs();//???????????????????????? loadsong  ?????????????????????
            pState = PlayState.STATE_PAUSE;
            timerState = TIMER.NO;
            //AssetManager????????????
            assetManager = getContext().getAssets();//getContext()!!!
            //getResourcesInternal().getAssets();
            //????????????1???MP3
            curId = 1;
            loadMP3(1);
            loadSongInfo(1);
            loadGlidePic(curId);
            //?????????????????????????????????
            int songDuration = mediaPlayer.getDuration();
            tvSongDuration.setText(transformToHMS(songDuration));
            //???????????????????????????????????????
            mSeekBar.setMax(mediaPlayer.getDuration());
            //???MediaPlayer?????????????????????????????????(?????????????????????)
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (pState == PlayState.STATE_PAUSE) {
                        //???????????????????????????????????????????????????
                    } else if (pState == PlayState.STATE_PLAYING) { //???????????????????????????????????????
                        //???????????????????????????id
                        curId++;
                        if (curId > 6) {
                            curId = 1;
                        }
                        Log.e("??????MediaPlay??????????????????", "");
                        //?????????????????????
                        loadMP3(curId);
                        loadSongInfo(curId);
                        //????????????
                        mediaPlayer.start();

                        //?????????
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
            Log.i("ljn!!!!!","??????id"+curId + "collcet" +  ((SongInfo)(songList.get( (curId-1==-1) ? 5:(curId-1) ))).getCollect());
        }else if (  ((SongInfo)(songList.get( (curId-1==-1) ? 5:(curId-1) ))).getCollect()== 1){
            btnCollect.setBackgroundResource(R.drawable.r_heart1);
        }
    }


    private void getCollect() {
        if (!db.isOpen()){
            //?????????????????????
            dbHelper = new MyDatabaseHelper6
                    (getContext(), "SongsN.db", null, 3);
            db = dbHelper.getWritableDatabase();
        }
        Cursor cur = db.query("SongsN", null, null, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        cur.moveToFirst();
        if (cur.getCount() == 0) {
        } else {
            if (cur.moveToFirst()) {//??????collcet??????
                // ????????????
                do {//int ??? integer  ????????????
                    int sCollect = cur.getInt(cur.getColumnIndex("collect"));
                    Log.e("SongsN.db",   "-collect:" + sCollect);
                    collectList.add(new Integer(sCollect));
                } while (cur.moveToNext());
            }
            //????????????collect
            for (int i = 0; i < songList.size();i++){
                ((SongInfo)(songList.get(i))).setCollect(((Integer)collectList.indexOf(i)).intValue());
                Log.i("songList.toString",songList.toString());//??????
            }
        }
    }

    private void beginAnim() {
        CircleAnimator.start();
        pState = PlayState.STATE_PLAYING;//??????????????????
    }

    /**
     * ???MediaPlayer????????????????????????????????????????????????
     *
     * @param id ????????????????????????id
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadMP3(int id) {
        try {
            //??????MediaPlayer
            mediaPlayer.reset();
            //?????????????????????MP3??????
            AssetFileDescriptor descriptor =
                    assetManager.openFd("0" + id + ".mp3");
            //???MediaPlayer????????????????????????????????????MP3
            mediaPlayer.setDataSource(descriptor);
            //???????????????
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ?????????????????????????????????
     */
    public static class ClickHelper {
        private static long lastClickTime = 0;

        /**
         * ???????????????????????????????????????????????????
         * ??????????????????????????????1000??????????????????true???????????????false
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


    //????????????
    public class MyListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            if (ClickHelper.isFastDoubleClick()) {//????????????
                return;
            }
            switch (view.getId()) {
                case R.id.btn_play_pattern://??????????????????
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
                case R.id.btn_play_pre://?????????
                    //????????????
                    if (playPattern == 0) {
                        //????????????????????????id
                        curId--;
                        if (curId < 1) {
                            curId = 6;
                        }
                        loadSongInfo(curId);
                    }
                    //????????????
                    else if (playPattern == 1) {
                    }
                    //????????????
                    else if (playPattern == 2) {
                        curId = (int) (Math.random() * 6) + 1;//???1,????????????0????????????  ????????????curID?????????
                        Log.i("loadSongInfo??????curId=", "" + curId);
                        loadSongInfo(curId);
                        Log.i("loadSongInfo??????curId=", "" + curId);
                    }

                    //???????????????????????????
                    loadMP3(curId);
                    //????????????
                    mediaPlayer.start();
                    loadGlidePic(curId);
                    beginAnim();

                    //?????????????????????????????????
                    int songDuration = mediaPlayer.getDuration();
                    tvSongDuration.setText(transformToHMS(songDuration));

                    //???????????????????????????????????????
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    //????????????/??????????????????????????????????????????
                    mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                    break;

                case R.id.btn_play_or_pause://??????/????????????
                    //?????????????????????????????????????????????????????????
                    // ???????????????????????????
                    if (mediaPlayer.isPlaying()) {//??????????????????????????????
                        mediaPlayer.pause();
                        //????????????????????????????????????????????????
                        //stopTimer();
                        mPlayOrPause.setBackgroundResource(R.drawable.r_play);
                        loadGlidePic(curId);
                        pauseAnim();
                    } else {//????????????????????????????????????
                        mediaPlayer.start();
                        //????????????????????????????????????????????????
                        mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                        pauseAnim();
                    }
                    break;
                case R.id.btn_play_next://?????????
                    //????????????
                    if (playPattern == 0) {
                        //??????????????????Id
                        curId++;
                        if (curId > 6) {
                            curId = 1;
                        }
                        loadSongInfo(curId);
                    }

                    //????????????
                    else if (playPattern == 1) {

                    }
                    //????????????
                    else if (playPattern == 2) {
                        songsTotal = songList.size();
                        int r = 0;
                        //(int)(Math.random()*6)+1
                        curId = (int) (Math.random() * 6) + 1;
                        // curId = (r = (curId + (int) (Math.random() * 6)+1) % songsTotal) == curId ? curId+3: r ;
                        Log.i("r=", "" + r);
                        loadSongInfo(curId);
                    }
                    //????????????
                    loadMP3(curId);
                    //??????
                    mediaPlayer.start();
                    loadGlidePic(curId);
                    beginAnim();
                    //??????????????????
                    int songDuration1 = mediaPlayer.getDuration();
                    tvSongDuration.setText(transformToHMS(songDuration1));

                    //???????????????????????????????????????
                    mSeekBar.setMax(mediaPlayer.getDuration());
                    //????????????/??????????????????????????????????????????????????????
                    mPlayOrPause.setBackgroundResource(R.drawable.r_pause);
                    break;
                case R.id.btn_play_list://????????????
                    showSongList(playPattern,(curId-1==-1)?5:(curId-1));

                    break;
                case R.id.btn_collect: //????????????
                    int c1 = ((SongInfo) (songList.get(curId - 1))).getCollect();
                    Log.i("CC---curId-1=",curId-1+":"+c1);
                    if (c1 == 0) {
                        Log.i("CC---curId-1=",curId-1+""+c1);
                        Log.i("?????????", "before");
                        ((SongInfo) (songList.get(curId - 1))).setCollect(1);
                        ContentValues values = new ContentValues();
                        //????????????????????????
                        values.put("collect", 1);
                        db.update("SongsN", values, "songId=?", new String[]{curId + ""});
                        Log.e("????????????", "song:" + ((SongInfo) (songList.get(curId - 1))).getSongName());
                        Toast.makeText(getContext(), "????????????", Toast.LENGTH_SHORT).show();
//                        dataSource.remove(position);
//                        notifyDataSetChanged();
                        btnCollect.setBackgroundResource(R.drawable.r_heart1);
                    } else if (c1 == 1){
                        Log.i("????????????", "before");

                        Log.i("CC---curId-1=",curId-1+""+c1);
                        ((SongInfo) (songList.get(curId - 1))).setCollect(0);
                        ContentValues values = new ContentValues();
                        //????????????????????????
                        values.put("collect", 0);
                        db.update("SongsN", values, "songId=?", new String[]{curId + ""});
                        Log.e("???????????????", "song:" + ((SongInfo) (songList.get(curId - 1))).getSongName());
                        Toast.makeText(getContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                        btnCollect.setBackgroundResource(R.drawable.r_heart0);
                    }
                    break;
                case R.id.btn_share: //??????
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "??????");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "??????????????????????????????" + ((SongInfo) (songList.get(curId - 1))).getSongName() + "???" + songljList.get(curId - 1));
                    startActivity(Intent.createChooser(shareIntent, "?????????"));
                    break;
                case R.id.btn_download://??????
                    Intent iDld = new Intent();
                    iDld.setClass(getContext(), DownloadPopupWin.class);
                    startActivity(iDld);
                    break;
                case R.id.btn_timer://??????
                    showTimerPopupWindow(timerState);

                    break;
                case R.id.btn_more: //??????
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
     * ??????popupwindow??????????????????
     */
    private void showSongList(int pattern,int list_id) {
        songAdapter = new SongAdapter(
                getContext(),       // ???????????????
                songList, // ?????????
                R.layout.layout_song_item // ?????????????????????
        );
        final View inflatedView = getLayoutInflater().inflate(R.layout.layout_songlist, null);
        ListView lv = inflatedView.findViewById(R.id.lv);
        lv.setAdapter(songAdapter);
        ImageView ivP = inflatedView.findViewById(R.id.iv_pattern);
        TextView tvP = inflatedView.findViewById(R.id.tv_pattern);



        switch (pattern) {
            case 0:
                ivP.setImageResource(R.drawable.l_liebiao);
                tvP.setText("????????????");
                break;
            case 1:
                ivP.setImageResource(R.drawable.l_danqu);
                tvP.setText("????????????");
                break;
            case 2:
                ivP.setImageResource(R.drawable.l_suiji1);
                tvP.setText("????????????");
                break;
        }
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(inflatedView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// ??????????????????????????????

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
//                        Log.i("?????????id=",position + "---" + id);
                loadSongInfo(curId);
                //????????????
                loadMP3(curId);
                //??????
                mediaPlayer.start();
                loadGlidePic(curId);
                beginAnim();
                //??????????????????
                int songDuration1 = mediaPlayer.getDuration();
                tvSongDuration.setText(transformToHMS(songDuration1));

                //???????????????????????????????????????
                mSeekBar.setMax(mediaPlayer.getDuration());
                //????????????/??????????????????????????????????????????????????????
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

        Log.e("ljn", "????????????");

        radioGroup = (RadioGroup) (inflatedView.findViewById(R.id.radioGroup));
        setCheckedButton(timerstate, radioGroup);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(inflatedView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);// ??????????????????????????????

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
                                setHour = hourOfDay;//???????????????
                                setMin = minute;
                                timerState = TIMER.T_DIY;
                                stopTimer();//??????????????????????????????
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
                    case R.id.tv_f_ok://????????????????????????id
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
     * ??????/?????????????????????pState  ???  animator???????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pauseAnim() {
        if (pState.equals(PlayState.STATE_PLAYING)) {//???????????????,??????????????????
            CircleAnimator.pause();
            pState = PlayState.STATE_PAUSE;
        } else if (pState.equals(PlayState.STATE_PAUSE)) {  //???????????????????????????????????????
            if (CircleAnimator.isStarted())
                CircleAnimator.resume();
            else {
                CircleAnimator.start();
            }
            pState = PlayState.STATE_PLAYING;
        }
    }

    /**
     * ????????????????????????
     */
    private void loadSongInfo(int curId) {
        //???????????????-1
        Log.i("?????????curId=", "" + curId);//  ((curId - 1)==-1) ? 5:(curId - 1)
        int c = ((SongInfo) (songList.get(curId - 1))).getCollect();//TODO
        Log.i("?????????curId=", "" + curId);
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
     * ????????????????????????????????????id  Drawable
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
     * ???view?????????????????????
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
     * ??????View??????
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

    //?????????????????????????????????
    class myThread extends Thread {
        @Override
        public void run() {
            super.run();
            //?????????????????????????????????????????????
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
     * ???????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView(View view, int playPattern) {
        //????????????
        if (playPattern == PLAY_IN_ORDER) {
            mPlayPattern.setBackgroundResource(R.drawable.r_liexun);
        } else if (playPattern == PLAY_SINGLE) {
            mPlayPattern.setBackgroundResource(R.drawable.r_danqu);
        } else if (playPattern == PLAY_RANDOM) {
            mPlayPattern.setBackgroundResource(R.drawable.r_suiji);
        }
        //??????????????????
        //  getMusicListThread();
        mSeekBar.getThumb().setColorFilter(Color.parseColor("WHITE"), PorterDuff.Mode.SRC_ATOP);//???????????????????????????
        mSeekBar.getProgressDrawable().setColorFilter(Color.parseColor("WHITE"), PorterDuff.Mode.SRC_ATOP);//??????????????????????????????
    }

}
