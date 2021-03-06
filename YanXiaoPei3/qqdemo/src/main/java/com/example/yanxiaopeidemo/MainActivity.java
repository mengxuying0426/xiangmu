package com.example.yanxiaopeidemo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.barnettwong.dragfloatactionbuttonlibrary.view.DragFloatActionButton;
import com.example.yanxiaopeidemo.Activity.ActionButtonActivity;
import com.example.yanxiaopeidemo.Activity4.FirstFragment;
import com.example.yanxiaopeidemo.Activity4.MemberActivity;
import com.example.yanxiaopeidemo.entitys.City;
import com.example.yanxiaopeidemo.entitys.ColorShades;
import com.example.yanxiaopeidemo.entitys.SchoolInfo;
import com.example.yanxiaopeidemo.ChooseSchoolFragment;
import com.example.yanxiaopeidemo.fragment.Fragment2;
import com.example.yanxiaopeidemo.fragment.MusicFragment;
import com.example.yanxiaopeidemo.fragment.PlanFragment;
import com.example.yanxiaopeidemo.fragment.MainFragment;
import com.example.yanxiaopeidemo.fragment.SecondFragment;
import com.example.yanxiaopeidemo.fragment.WorkFragment;
import com.example.yanxiaopeidemo.menu.PersonalCollection;
import com.example.yanxiaopeidemo.menu.PersonalDecoration;
import com.example.yanxiaopeidemo.menu.PersonalWhiteList;
import com.example.yanxiaopeidemo.menu.PersonalWish;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.example.yanxiaopeidemo.util.ImageUtils;
import com.example.yanxiaopeidemo.util.PostUtil;
import com.example.yanxiaopeidemo.view.CircleImageView;
import com.example.yanxiaopeidemo.view.FragmentTabHost;
import com.example.yanxiaopeidemo.view.MenuItemView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.example.yanxiaopeidemo.MainActivity.citylist;
import static com.example.yanxiaopeidemo.MainActivity.collectschools;
import static com.example.yanxiaopeidemo.MainActivity.schoolInfos;
import static com.example.yanxiaopeidemo.MainActivity.username;

public class MainActivity extends AppCompatActivity {

    private DragFloatActionButton btn;
    private Uri imageUri;
    public static final String TAG = "MainActivity";
    private DrawerLayout mDrawer;
    private FragmentTabHost mTabHost;
    private FrameLayout mTabContent;
    private View mTabView;
    private String[] mTabTexts;
    private int[] mTabIcons = new int[]{
            R.mipmap.home,
            R.mipmap.school,
            R.mipmap.plan,
            R.mipmap.work,
            R.mipmap.music};
    private int[] mTabBacks = new int[]{
            R.drawable.selector_nvg_home,
            R.drawable.selector_nvg_school,
            R.drawable.selector_nvg_plan,
            R.drawable.selector_nvg_work,
            R.drawable.selector_nvg_music
    };
    public static final int TAKE_CAMERA = 101;
    public static final int PICK_PHOTO = 102;
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int RESIZE_REQUEST_CODE = 2;
    private String IMAGE_FILE_NAME = System.currentTimeMillis() + "crecirheader.jpg";
    private ImageView iv_show_photo;
    private String photoUrl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    //?????????????????????????????????
                    mCivHead.setImageBitmap(bitmap);
                    imgHead.setImageBitmap(bitmap);
                    break;
            }
        }
    };
    private TextView mTvTitle;
    private ImageView mIvAdd;
    private TextView mTvAdd;
    private TextView mTvMore;
    public static RelativeLayout mRlTitle;
    private RelativeLayout mRlMenu;
    private ColorShades mColorShades;
    private LinearLayout mLlContentMain;
    public static LinearLayout ll_head;
    private CircleImageView mCivHead;
    private CircleImageView imgHead;
    private TextView mTvMessgeCount;
    private TextView mTvContactsCount;
    private TextView mTvStarCount;
    private MenuItemView menuDecoration;
    private MenuItemView menuCollect;
    private MenuItemView menuWhiteList;
    private MenuItemView menuWall;
    private MenuItemView menuMembership;

    public static List<City> citylist = new ArrayList<>();//????????????
    public static List<SchoolInfo> schoolInfos = new ArrayList<>();//????????????
    public static List<SchoolInfo> collectschools = new ArrayList<>();//???????????????????????????
    public static String username = "shengdai";




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// ???????????????
        setContentView(R.layout.activity_main);
        initView();
       // initImg();
        initEvent();
        setListener();


        initEvent();
        setListener();
        try {
            UpUserNameThread thread = new UpUserNameThread();
            thread.start();
            thread.join();
            NewThread thread3 = new NewThread();
            thread3.start();
            thread3.join();
            SearchThread thread2 = new SearchThread();
            thread2.start();
            DownUserAllCollectSchoolInfoThread thread1 = new DownUserAllCollectSchoolInfoThread();
            thread1.start();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void setListener() {
        MyClickListener myClickListener = new MyClickListener();
        menuDecoration.setOnClickListener(myClickListener);
        menuCollect.setOnClickListener(myClickListener);
        menuWhiteList.setOnClickListener(myClickListener);
        menuWall.setOnClickListener(myClickListener);
        menuMembership.setOnClickListener(myClickListener);
        imgHead.setOnClickListener(myClickListener);
        btn.setOnClickListener(myClickListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initView() {
        btn =findViewById(R.id.circle_button);
        imgHead=findViewById(R.id.img_head);
        mDrawer = findViewById(R.id.drawer_layout);
        mRlMenu = findViewById(R.id.rl_menu);
        mLlContentMain = findViewById(R.id.ll_content_main);
        mRlTitle = findViewById(R.id.rl_title);
        ll_head=findViewById(R.id.ll_head);
        mCivHead = findViewById(R.id.civ_head);
        mTvTitle = findViewById(R.id.tv_title);
        menuWhiteList = findViewById(R.id.menu_white_list);
        menuDecoration = findViewById(R.id.menu_decoration);
        menuCollect = findViewById(R.id.menu_collect);
        menuWall = findViewById(R.id.menu_wish);
        menuMembership=findViewById(R.id.menu_membership);
        //??????????????????
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //???????????????
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        //???????????????????????????
        mTabHost.setCurrentTab(0);
        //???????????????????????????,FragmentTabHost???????????????,??????Tab?????????????????????
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        initBottomNavigationView();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initBottomNavigationView() {
        mTabTexts = getResources().getStringArray(R.array.tab_texts);

        for (int i = 0; i < 5; i++) {
            mTabView = View.inflate(this, R.layout.layout_item_tab, null);
            ((TextView) mTabView.findViewById(R.id.tv_tab_text)).setText(mTabTexts[i]);
            ((ImageView) mTabView.findViewById(R.id.iv_tab_icon)).setImageResource(mTabBacks[i]);

            //??????TabSpec
            TabHost.TabSpec messageTabSpec = mTabHost.newTabSpec(mTabTexts[i]).setIndicator(mTabView);

            Bundle bundle = new Bundle();
            bundle.putString(TAG, mTabTexts[i]);
            switch (i) {
                case 0:
                    mTabHost.addTab(messageTabSpec, FirstFragment.class, bundle);
                    break;
                case 1:
                    mTabHost.addTab(messageTabSpec, ChooseSchoolFragment.class, bundle);
                    break;
                case 2:
                    mTabHost.addTab(messageTabSpec, com.example.yanxiaopeidemo.Activity4.SecondFragment.class, bundle);
                    break;
                case 3:
                    mTabHost.addTab(messageTabSpec, SecondFragment.class, bundle);
                    break;
                case 4:
                    mTabHost.addTab(messageTabSpec, MusicFragment.class, bundle);
                    break;
            }
        }
    }

//    private void initImg() {
//        new Thread(){
//            @Override
//            public void run() {
//                //???????????????
//                URL url = null;
//                try {
//                    url = new URL(ConfigUtil.SERVER_ADDR+"InitImgServlet");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    //??????????????????????????????POST
//                    conn.setRequestMethod("POST");
//                    //????????????????????????????????????????????????????????????????????????
//                    InputStream in=conn.getInputStream();
//                    //?????????????????????
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
//                    //??????????????????
//                    String imgUrl = reader.readLine();
//
//                    //??????????????????????????????
//                    URL url1 = null;
//                    try {
//                        url1 = new URL(imgUrl);
//                        InputStream in1 = url1.openStream();
//                        //?????????????????????Bitmap??????
//                        Bitmap bitmap2 = BitmapFactory.decodeStream(in1);
//                        //??????????????????
//                        in1.close();
//
//                        Message msg2 = handler.obtainMessage();
//                        msg2.what=2;
//                        msg2.obj=bitmap2;
//                        handler.sendMessage(msg2);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
    private void initEvent() {
        mCivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i(TAG, "onTabChanged: tabId -- " + tabId);
                mTvTitle.setText(tabId);
            }
        });

        mColorShades = new ColorShades();

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //???????????????????????????????????????
                int drawerViewWidth = drawerView.getWidth();
                mLlContentMain.setTranslationX(drawerViewWidth * slideOffset);

                //?????????????????????????????????
                double padingLeft = drawerViewWidth * (1 - 0.618) * (1 - slideOffset);
                mRlMenu.setPadding((int) padingLeft, 0, 0, 0);

                //??????Title????????????
//                mColorShades.setFromColor("#001AA7F2")
//                        .setToColor(Color.WHITE)
//                        .setShade(slideOffset);
//                mRlTitle.setBackgroundColor(mColorShades.generate());
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }
    /**
     * @return imgStr
     */
    private String bitmap2String(final Bitmap bitmap) {
        System.out.println("?????????????????????");
        if(bitmap==null){
            Toast.makeText(this, "???????????????", Toast.LENGTH_LONG).show();
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        boolean b = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        if(b==false){
            return null;
        }
        byte[] bs = stream.toByteArray();
        String s = Base64.encodeToString(bs, Base64.DEFAULT);

        return s;
    }
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }

    class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.menu_membership:
                    Intent intent6 = new Intent();
                    intent6.setClass(getApplicationContext(), com.example.yanxiaopeidemo.Activity4.MemberActivity.class);
                  //  intent6.putExtra("img",head);
                    startActivity(intent6);
                    break;
                case R.id.circle_button:
                    Intent intent5= new Intent();
                    intent5.setClass(getApplicationContext(), ActionButtonActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.img_head:
                    showBottomDialog();
                    break;
                case R.id.menu_decoration:
                    Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, PersonalDecoration.class);
                    startActivity(intent);
                    break;
                case R.id.menu_collect:
                    Toast.makeText(MainActivity.this, "????????????", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(MainActivity.this, PersonalCollection.class);
                    startActivity(intent2);
                    break;
                case R.id.menu_white_list:
                    Toast.makeText(MainActivity.this, "?????????", Toast.LENGTH_LONG).show();
                    Intent intent3 = new Intent(MainActivity.this, PersonalWhiteList.class);
                    startActivity(intent3);
                    break;
                case R.id.menu_wish:
                    Toast.makeText(MainActivity.this, "?????????", Toast.LENGTH_LONG).show();
                    Intent intent4 = new Intent(MainActivity.this, PersonalWish.class);
                    startActivity(intent4);
                    break;
            }
        }
    }

    private void showBottomDialog(){
        //1?????????Dialog?????????style
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //2???????????????
        View view = View.inflate(this,R.layout.dialog,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //??????????????????
        window.setGravity(Gravity.BOTTOM);
        //??????????????????
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //?????????????????????
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ??????File???????????????????????????????????????
                //???????????????SD?????????????????????????????????
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
               /* ???Android 6.0?????????????????????SD??????????????????????????????????????????????????????SD???????????????????????????
                  ??????????????????????????????????????????????????????????????? ??????????????????????????????
                */
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*
                   7.0????????????????????????????????????????????????Uri????????????????????????????????? ?????????FileUriExposedException?????????
                   ???FileProvider????????????????????????????????????????????????????????? ??????????????????????????????????????????????????????
                   ?????????????????????????????????Uri????????????????????????????????? ??????????????????
                 */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //??????????????????24???7.0????????????
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.qqdemo", outputImage);
                } else {
                    //??????android ??????7.0???24????????????
                    imageUri = Uri.fromFile(outputImage);
                }

                //??????????????????
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //MediaStore.ACTION_IMAGE_CAPTURE = android.media.action.IMAGE_CAPTURE
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_CAMERA);
                dialog.dismiss();

            }
        });

        dialog.findViewById(R.id.tv_take_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????????????????????????????????????
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // ??????????????????,???????????????
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    public void resizeImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESIZE_REQUEST_CODE);
    }
    private void showResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            //            String path = getExternalCacheDir().getAbsolutePath() + File.separator + IMAGE_FILE_NAME;
            System.out.println("?????????"+getExternalCacheDir().getAbsolutePath() + File.separator + IMAGE_FILE_NAME);
            String path = getFilesDir().getPath() + File.separator + IMAGE_FILE_NAME;
            System.out.println("path:"+path);


            ImageUtils.saveImage(photo, path);
            new BitmapDrawable();
            Drawable drawable = new BitmapDrawable(photo);

            imgHead.setImageDrawable(drawable);
            mCivHead.setImageDrawable(drawable);
        //    MemberActivity.img.setImageDrawable(drawable);
            UpImg(photo);
        }
    }
    private void UpImg(final Bitmap bitmap) {

        final String imgStr = bitmap2String(bitmap);
        System.out.println("imgStr:"+imgStr);
        new Thread(new Runnable() {

            @Override
            public void run() {
                String upLoadResult = null;
                try {
                    System.out.println("imgStr:"+imgStr);
                    upLoadResult = new PostUtil().upLoadPhoto(imgStr);
                    System.out.println("?????????"+upLoadResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("???????????????"+upLoadResult);

                //??????????????????????????????
                URL url1 = null;
                try {
                    url1 = new URL(upLoadResult);
                    InputStream in1 = url1.openStream();
                    //?????????????????????Bitmap??????
                    Bitmap bitmap2 = BitmapFactory.decodeStream(in1);
                    //??????????????????
                    in1.close();

                    Message msg2 = handler.obtainMessage();
                    msg2.what=2;
                    msg2.obj=bitmap2;
                    handler.sendMessage(msg2);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_CAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        // ??????????????????????????????
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        System.out.println("?????????"+getExternalCacheDir().getAbsolutePath() + File.separator + IMAGE_FILE_NAME);
                        String path = getFilesDir().getPath() + File.separator + IMAGE_FILE_NAME;
                        System.out.println("path:"+path);
                        ImageUtils.saveImage(bitmap, path);
                        imgHead.setImageBitmap(bitmap);
                        mCivHead.setImageBitmap(bitmap);
                       // MemberActivity.img.setImageBitmap(bitmap);
                        // UpImg(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case IMAGE_REQUEST_CODE:
                resizeImage(data.getData());
                break;

            case RESIZE_REQUEST_CODE:
                if (data != null) {
                    showResizeImage(data);
                }
                break;
            default:
                break;
        }
    }
}


/**
 * ????????????????????????
 */
class NewThread extends Thread {
    @Override
    public void run() {
        Document document;
        City c = new City(0, "??????");
        citylist.add(c);
        {
            try {
                //??????????????????
                document = Jsoup.connect("https://yz.chsi.com.cn/sch/search.do?ssdm=&yxls=").get();
                Element content = document.getElementById("ssdm");
                Elements cities = content.getElementsByTag("option");
                for (Element city : cities) {
                    if (!city.attr("value").equals("")) {
                        int value = Integer.parseInt(city.attr("value"));
                        String name = city.text();
                        City c1 = new City(value, name);
                        citylist.add(c1);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


/**
 * ????????????????????????
 */
class SearchThread extends Thread {
    @Override
    public void run() {
        if (schoolInfos.size() != 0) {
            schoolInfos.clear();
        }
        Document document;
        Integer start = 0;
        int a = 0;
        while (a < 43) {
            String url = "https://yz.chsi.com.cn/sch/search.do?start=" + start;
            try {
                document = Jsoup.connect(url).get();
                Elements elements = document.select("tbody tr");
                for (Element element : elements) {
                    String path = "https://yz.chsi.com.cn/" + element.attr("href");
                    Elements es = element.select("td:lt(3)");
                    String[] strs = es.text().split("\\s+");
                    SchoolInfo s = new SchoolInfo(strs[0], strs[1], strs[2], path);
                    schoolInfos.add(s);
                }
                start += 20;
                a++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


/**
 * ??????????????????????????????????????????
 */
class UpUserNameThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "UpUserNameServlet";

    @Override
    public void run() {
        String keyValue = "?name=" + username;
        try {
            URL url = new URL(s + keyValue);
            url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * ?????????????????????????????????????????????????????????
 */
class DownUserAllCollectSchoolInfoThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "DownUserAllCollectSchoolInfoServlet";

    @Override
    public void run() {
        URL url = null;
        try {
            url = new URL(s);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, "utf-8"));
            String str = reader.readLine();
            if (null != str){
                str = new String(str.getBytes(), "utf-8");
                Log.e("str",str);
                reader.close();
                in.close();
                if (null != str){
                    if (collectschools.size()!=0){
                        collectschools.clear();
                    }
                    String[] strs = str.split("&&&");
                    for (int i = 1; i < strs.length; i=i+5) {
                        SchoolInfo s = new SchoolInfo(strs[i],strs[i+1],strs[i+2],strs[i+3],strs[i+4]);
                        collectschools.add(s);
                        Log.e("collectschools", collectschools.size()+"");
                    }
                }
            }

        } catch (
                MalformedURLException e) {
            e.printStackTrace();
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}









