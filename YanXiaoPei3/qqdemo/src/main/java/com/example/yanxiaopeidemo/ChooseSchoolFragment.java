package com.example.yanxiaopeidemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.yanxiaopeidemo.adapter.SchoolAdapter;
import com.example.yanxiaopeidemo.entitys.City;
import com.example.yanxiaopeidemo.entitys.Major;
import com.example.yanxiaopeidemo.entitys.School;
import com.example.yanxiaopeidemo.entitys.SchoolInfo;
import com.example.yanxiaopeidemo.entitys.Subject;
import com.example.yanxiaopeidemo.flowlayout.FlowLayout;
import com.example.yanxiaopeidemo.flowlayout.TagAdapter;
import com.example.yanxiaopeidemo.flowlayout.TagFlowLayout;
import com.example.yanxiaopeidemo.util.ConfigUtil;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.header.WaveSwipeHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zj.filters.FiltrateBean;
import com.zj.filters.FlowPopWindow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.example.yanxiaopeidemo.ChooseSchoolFragment.historyinfo;
import static com.example.yanxiaopeidemo.ChooseSchoolFragment.majs;
import static com.example.yanxiaopeidemo.ChooseSchoolFragment.subs;
import static com.example.yanxiaopeidemo.MainActivity.citylist;
import static com.example.yanxiaopeidemo.MainActivity.schoolInfos;
import static com.example.yanxiaopeidemo.MainActivity.username;
import static com.example.yanxiaopeidemo.SearchActivity.searchlist;

public class ChooseSchoolFragment extends Fragment {
    private View root;
    private ListView listView;
    private ImageButton ibScreen;
    private TextView tvSearch;
    private Button confirm;
    private DrawerLayout drawerLayout;
    private LinearLayout right;
    private GridView loc;
    private GridView major;
    private GridView score;
    public static List<School> schools = new ArrayList<>();
    public static List<Major> majs = new ArrayList<>();//????????????
    public static List<Subject> subs = new ArrayList<Subject>();// ??????
    public static List<String> historyinfo = new ArrayList<>();//????????????????????????
    //????????????????????????
    private final int DEFAULT_RECORD_NUMBER = 10;
    private List<String> recordList = new ArrayList<>();
    private TagAdapter mRecordsAdapter;


    private ImageView clearAllRecords;
    private TagFlowLayout tagFlowLayout;
    private ImageView moreArrow;
    private LinearLayout mHistoryContent;
    private TextView tvHis;


    //???????????????
    private FlowPopWindow flowPopWindow;
    //???????????????
    private List<FiltrateBean> dictList = new ArrayList<>();
    public static List<String> list = new ArrayList<>();//????????????????????????

    private boolean isGetData = false;

    private SmartRefreshLayout srl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.choose_school_fragment, container, false);
        findViews();
        try {
            UpZyInfoThread thread1 = new UpZyInfoThread();
            thread1.start();
            thread1.join();
            DownUserAllHistorySearchInfoThread thread = new DownUserAllHistorySearchInfoThread();
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initData();
        initListView();
        initDaoData();
        initDaoView();
        setListener();
        initParam();
        initView();
        getHistoryInfo();//????????????????????????
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getHistoryInfo();
                initDaoData();
                srl.finishRefresh();
            }
        });
        srl.setReboundDuration(1000);
        srl.setEnableRefresh(true);
        return root;
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private void getHistoryInfo() {
        DownUserAllHistorySearchInfoThread thread = new DownUserAllHistorySearchInfoThread();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   ????????????Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //   ????????????????????????????????????????????????????????????
            getHistoryInfo();
            initDaoData();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }



    private void initDaoView() {
        //???????????????????????????
        //??????????????????????????????
        mRecordsAdapter = new TagAdapter<String>(historyinfo) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.tv_history,
                        tagFlowLayout, false);
                //??????????????????????????????
                tv.setText(s);
                return tv;
            }
        };


        tagFlowLayout.setAdapter(mRecordsAdapter);


        //view?????????????????????
        tagFlowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOverFlow = tagFlowLayout.isOverFlow();
                boolean isLimit = tagFlowLayout.isLimit();
                if (isLimit && isOverFlow) {
                    moreArrow.setVisibility(View.VISIBLE);
                } else {
                    moreArrow.setVisibility(View.GONE);
                }
            }
        });

        moreArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagFlowLayout.setLimit(false);
                mRecordsAdapter.notifyDataChanged();
            }
        });


        //??????????????????
        clearAllRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("????????????????????????????????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tagFlowLayout.setLimit(true);
                        //??????????????????
                        historyinfo.clear();
                        initDaoView();
                        DeleteAllHistoryInfoThread thread = new DeleteAllHistoryInfoThread();
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tvHis.setVisibility(View.GONE);
                        clearAllRecords.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private void showDialog(String dialogTitle, @NonNull DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogTitle);
        builder.setPositiveButton("??????", onClickListener);
        builder.setNegativeButton("??????", null);
        builder.create().show();
    }

    private void initDaoData() {
        if (null == historyinfo || historyinfo.size() == 0) {
            mHistoryContent.setVisibility(View.GONE);
            tvHis.setVisibility(View.GONE);
            clearAllRecords.setVisibility(View.GONE);

        } else {
            mHistoryContent.setVisibility(View.VISIBLE);
            tvHis.setVisibility(View.VISIBLE);
            clearAllRecords.setVisibility(View.VISIBLE);
        }
        if (mRecordsAdapter != null) {
            mRecordsAdapter.setData(historyinfo);
            mRecordsAdapter.notifyDataChanged();
        }

    }


    private void initView() {
        ibScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flowPopWindow = new FlowPopWindow(getActivity(), dictList);
                flowPopWindow.showAsDropDown(ibScreen);
                flowPopWindow.setOnConfirmClickListener(new FlowPopWindow.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick() {
                        StringBuilder sb = new StringBuilder();
                        for (FiltrateBean fb : dictList) {
                            List<FiltrateBean.Children> cdList = fb.getChildren();
                            for (int x = 0; x < cdList.size(); x++) {
                                FiltrateBean.Children children = cdList.get(x);
                                if (children.isSelected()) {
                                    sb.append(fb.getTypeName() + ":" + children.getValue() + "???");
                                    list.add(children.getValue());
                                }

                            }
                        }
                        if (!TextUtils.isEmpty(sb.toString()))
                            Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getContext(), ScreenResultActivity.class);
                        intent.putExtra("cityname", list.get(list.size() - 2));
                        intent.putExtra("zy", list.get(list.size() - 1));
                        startActivity(intent);
                    }
                });

            }
        });
    }

    private void initParam() {
        FiltrateBean fb1 = new FiltrateBean();
        fb1.setTypeName("??????");
        List<FiltrateBean.Children> childrenList = new ArrayList<>();
        for (City city : citylist) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            cd.setValue(city.getName());
            childrenList.add(cd);
        }
        fb1.setChildren(childrenList);

        FiltrateBean fb2 = new FiltrateBean();
        fb2.setTypeName("??????");
        List<FiltrateBean.Children> childrenList2 = new ArrayList<>();
        for (Major m : majs) {
            FiltrateBean.Children cd = new FiltrateBean.Children();
            if (!m.getName().equals("???")) {
                cd.setValue(m.getName());
            } else {
                for (int i = 0; i < subs.size(); i++) {
                    if (subs.get(i).getDm().equals(m.getPredm())) {
                        cd.setValue(subs.get(i).getName());
                    }
                }
            }

            childrenList2.add(cd);
        }
        fb2.setChildren(childrenList2);

        dictList.add(fb1);
        dictList.add(fb2);
    }


    private void setListener() {
        ibScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SearchActivity.class);
                startActivity(i);
            }
        });
    }

    private void initListView() {
        SchoolAdapter adapter = new SchoolAdapter(schools, R.layout.school_list_item, getContext());
        listView.setAdapter(adapter);
    }

    private void initData() {
        if (schools.size() != 0) {
            schools.clear();
        }
        School s1 = new School(R.mipmap.njsfdx, "??????????????????", "????????????", "211??????", "?????????","https://souky.eol.cn/HomePage/index_1115.html");
        School s2 = new School(R.mipmap.hdsf, "??????????????????", "????????????", "985???211??????", "?????????","https://souky.eol.cn/HomePage/index_261.html");
        School s3 = new School(R.mipmap.szdx, "????????????", "????????????", "211??????", "?????????","https://souky.eol.cn/HomePage/index_284.html");
        School s4 = new School(R.mipmap.whdx, "????????????", "????????????", "985???211??????", "?????????","https://souky.eol.cn/HomePage/index_3.html");
        School s5 = new School(R.mipmap.whlg, "??????????????????", "????????????", "211??????", "?????????","https://souky.eol.cn/HomePage/index_126.html");
        School s6 = new School(R.mipmap.scdx, "????????????", "????????????", "985???211??????", "?????????","https://souky.eol.cn/HomePage/index_208.html");
        School s7 = new School(R.mipmap.zndx, "????????????", "????????????", "985???211??????", "?????????","https://souky.eol.cn/HomePage/index_51.html");
        School s8 = new School(R.mipmap.zzdx, "????????????", "????????????", "211??????", "?????????","https://souky.eol.cn/HomePage/index_25.html");
        School s9 = new School(R.mipmap.njdx, "????????????", "????????????", "985???211??????", "?????????","https://souky.eol.cn/HomePage/index_39.html");
        schools.add(s1);
        schools.add(s2);
        schools.add(s3);
        schools.add(s4);
        schools.add(s5);
        schools.add(s6);
        schools.add(s7);
        schools.add(s8);
        schools.add(s9);
    }

    private void findViews() {
        srl = root.findViewById(R.id.refresh);
        listView = root.findViewById(R.id.school_list);
        ibScreen = root.findViewById(R.id.ib_screen);
        tvSearch = root.findViewById(R.id.tv_search);
        drawerLayout = root.findViewById(R.id.drawer_layout_home);
        score = root.findViewById(R.id.score);
        tagFlowLayout = root.findViewById(R.id.fl_search_records);
        clearAllRecords = root.findViewById(R.id.clear_all_records);
        moreArrow = root.findViewById(R.id.iv_arrow);
        mHistoryContent = root.findViewById(R.id.ll_history_content);
        tvHis = root.findViewById(R.id.tv_his);
    }

}




/**
 * ????????????????????????
 */
class UpZyInfoThread extends Thread {
    @Override
    public void run() {
        if (majs.size() != 0) {
            majs.clear();
        }
        Document document = null;
        try {
            document = Jsoup.connect("http://www.cdgdc.edu.cn/xwyyjsjyxx/sy/glmd/267001.shtml").get();
            Elements elements = document.select("tbody");
            int i = 1;
            while (i < 394) {
                String[] strs = (elements.get(0).children().get(i).children().text()).split("\\s+");
                for (int a = 0; a < strs.length; a++) {
                    Matcher m = Pattern.compile("[???-???]").matcher(strs[a]);
                    if (strs[a].length() == 6 && !m.find()) {
                        String ml_dm = strs[a].substring(0, 4);
                        String name = strs[a + 1];
                        Major m1 = new Major(name, strs[a], ml_dm);
                        majs.add(m1);
                    } else if (strs[a].length() == 4 && !m.find()) {
                        String ml_dm = strs[a].substring(0, 2);
                        String name = strs[a + 1];
                        System.out.println(name);
                        Subject s = new Subject(name, strs[a], ml_dm);
                        subs.add(s);

                    }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



/**
 * ????????????????????????????????????????????????
 */
class DownUserAllHistorySearchInfoThread extends Thread {
    String s = ConfigUtil.SERVER_ADDR + "DownUserHistorySearchInfoServlet";
    @Override
    public void run() {
        try {
            URL url = new URL(s);
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
            String str = reader.readLine();
            if (null!=str){
                if (historyinfo.size()!=0){
                    historyinfo.clear();
                }
                String[] strs = str.split("&&&");
                for (int i = 1;i<strs.length;i++){
                    historyinfo.add(strs[i]);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


/**
 * ??????????????????????????????
 */
class DeleteAllHistoryInfoThread extends Thread{
    String s = ConfigUtil.SERVER_ADDR + "DeleteAllHistoryInfoServlet";
    @Override
    public void run() {
        String keyValue = "?username=" + username;
        try {
            URL url = new URL(s+keyValue);
            url.openStream();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

