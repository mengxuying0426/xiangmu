package com.example.yanxiaopeidemo.mxy2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.yanxiaopeidemo.R;
import com.example.yanxiaopeidemo.beans.WTiInfo;
import com.example.yanxiaopeidemo.beans.WrongTi;
import com.example.yanxiaopeidemo.utils.ServerConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WrongTiListActivity extends Activity {
    private List<WrongTi> wrongTis;
    private SwipeMenuListView swipeMenuListView;
    private MyWListAdapter myWListAdapter;
    private Handler myHandler;
    private WTiInfo wTiInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exersize_wrongti);
        myHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 1:
                        wTiInfo = (WTiInfo) msg.obj;
                        if(null!=wTiInfo){
                            wrongTis = wTiInfo.getWrongTis();
                            //绑定Adapter
                            myWListAdapter = new MyWListAdapter(getApplicationContext(),wrongTis);
                            swipeMenuListView.setAdapter(myWListAdapter);
                        }
                        break;
                }
            }
        };

        Intent result = getIntent();
        String xinxi = result.getStringExtra("tikufenlei");
        String[] arr = xinxi.split("&");
        String kemu = arr[0];
        int kemusta = Integer.parseInt(arr[1]);
        if(kemusta==1){
            //加载默认错题库
            String url = ServerConfig.SEVER_ADDR+"/"
                    +ServerConfig.NET_HOME+"/downloadsCuoChoice";
            downloadChoice(url,kemu);
        }else {
            //加载自定义错题库
        }


        //获取控件
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(70));
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView = findViewById(R.id.swipelistView);
        swipeMenuListView.setMenuCreator(creator);

         /*
        设置监听
         */
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                WrongTi w = wrongTis.get(position);
                switch (index){
                    case 0:
//                        delete(q);数据库中删除
                        wrongTis.remove(position);
                        myWListAdapter.notifyDataSetChanged();
                        break;
                }
                return true;
            }
        });
        // 监测用户在ListView的SwipeMenu侧滑事件
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                Log.d("位置:" + position, "开始侧滑...");
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });

    }

    /**
     * 下载错题库中的习题
     * @param url
     */
    private void downloadChoice(final String url, final String kemu) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection)url1.openConnection();
                    connection.setRequestMethod("POST");
                    OutputStream out = connection.getOutputStream();
                    String keyValue = ""+kemu;
                    out.write(keyValue.getBytes());
                    connection.getInputStream();
                    out.close();

                    //获取服务器返回数据判断是否登陆成功
                    InputStream in = connection.getInputStream();
                    //读数据（Json串),循环读写方式
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
//                    while((len = in.read(bytes,0,bytes.length))!=-1){
//                        buffer.append(new String(bytes, 0, len));
//                    }
                    String result = reader.readLine();
                    Log.i("mxy错题","json串："+result);
                    in.close();

                    //先将Json串解析成外部WTiInfo对象
                    //创建WTiInfo对象和WrongTi集合对象
                    WTiInfo wTiInfo = new WTiInfo();
                    List<WrongTi> wrongTis = new ArrayList<>();
                    //创建外层JSONObject
                    JSONObject JUsers = new JSONObject(result);
                    JSONArray jArray = JUsers.getJSONArray("wrongtis");
                    //遍历JSONArray对象,解析其中的每个元素
                    for(int i = 0;i<jArray.length();i++){
                        //获取当前的JSONObject对象
                        JSONObject JChoice = jArray.getJSONObject(i);
                        //获取当前元素中的属性信息
                        int id = JChoice.getInt("id");
                        String username = JChoice.getString("username");
                        String kemu = JChoice.getString("kemu");
                        int tiid = JChoice.getInt("tiid");
                        int tista = JChoice.getInt("tista");
                        String stem = JChoice.getString("stem");
                        String opta = JChoice.getString("opta");
                        String optb = JChoice.getString("optb");
                        String optc = JChoice.getString("optc");
                        String optd = JChoice.getString("optd");
                        String correct = JChoice.getString("correct");
                        int keynum = JChoice.getInt("keynum");
                        String analysis = JChoice.getString("analysis");
                        WrongTi wrongTi1 = new WrongTi(id,username,tiid,tista,kemu,stem,opta,optb,optc,optd,correct,analysis,keynum);
                        wrongTis.add(wrongTi1);
                    }
                    wTiInfo.setWrongTis(wrongTis);
                    //2. 通过发送Message对象，将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what、obj）
                    msg.what = 1;
                    msg.obj = wTiInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public int dp2px(float dipValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}

//package net.onest.mypartprj;
//
//        import android.app.Activity;
//        import android.content.Intent;
//        import android.graphics.Color;
//        import android.graphics.drawable.ColorDrawable;
//        import android.os.Bundle;
//        import android.util.Log;
//
//        import androidx.annotation.Nullable;
//
//        import com.baoyz.swipemenulistview.SwipeMenu;
//        import com.baoyz.swipemenulistview.SwipeMenuCreator;
//        import com.baoyz.swipemenulistview.SwipeMenuItem;
//        import com.baoyz.swipemenulistview.SwipeMenuListView;
//
//        import net.onest.mypartprj.beans.QuestionBank;
//        import net.onest.mypartprj.beans.SingleChoice;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//public class WrongTiListActivity extends Activity {
//    private List<SingleChoice> singleChoices;
//    private SwipeMenuListView swipeMenuListView;
//    private MyWListAdapter myWListAdapter;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.exersize_wrongti);
//
//        Intent result = getIntent();
//        String fenlei = result.getStringExtra("fenlei");
//        Log.i("跳转练习",fenlei);
//        String[] arr = fenlei.split("&");
//        int num = Integer.parseInt(arr[1]);
//        List<SingleChoice> s11 = new ArrayList<>();
//        SingleChoice s1 = new SingleChoice("某资本家投资100万元创办企业从事生产，60万元用于固定资本、以购买机器设备等，40万元用于流动资本、以购买原材料和劳动力等(其中购买劳动力支付了10万元),一轮生产结束后，该企业的总资本达到了120万元。那么，该企业的剩余价值率为()。","200%","50%","100%","20%","A",1,"A正确，B、C、D错误。剩余价值率的计算公式是m'=m/v，题干中m的数量是120万与100万的差，即20万，v是用来购买劳动力商品的，题干中v的数量为10万，并不是40万。因此，m'=m/v=20万/10万=200%。题干的混淆项是固定资本和流动资本，但混淆性极弱。故正确答案为A。");
//        s11.add(s1);
//        SingleChoice s2 = new SingleChoice("邓小平指出:社会主义究竟是个什么样子，苏联搞了很多年，也并没有完全搞清楚。可能列宁的思路比较好，搞了个新经济政策，但是后来苏联的模式僵化了。列宁新经济政策关于社会主义的思维之所以“比较好”是因为()。","提出了比较系统的社会主义建设纲领","根据俄国的实际情况来探索社会主义建设的道路","为俄国找到了一种比较成熟的社会发展模式","按照马克思恩格斯关于未来社会的设想来建设社会主义","B",1,"本题考查的知识点为列宁领导下的苏维埃俄国对社会主义的探索。1921年3月，俄共(布)召开十大，决定从战时共产主义政策过渡到以发展商品经济为主要特征的新经济政策。这一决定，表明列宁的社会主义建设思想发生了重大转变，对俄国社会主义的发展道路有了新的认识，标志着列宁正在找到一条符合俄国情况的建设社会主义的道路。B项正确。A项错误，列宁虽然对建设社会主义提出过许多精辟的论述，但并没有提出比较系统的社会主义建设纲领; C项错误，列宁并没有为俄国找到一种成熟的社会发展模式，新经济政策也是一种探索性质的道路; D项错误,列宁并不是按照马克思主义关于社会主义的设想建设俄国的社会主义，而是将马克思主义与俄国的具体国情相结合，建设俄国的社会主义。故正确答案为B。");
//        s11.add(s2);
//        SingleChoice s3 = new SingleChoice("有人认为，既然人的意识是对客观外部世界的反映。那么人脑海里的“鬼神”意识就是对外在世界上鬼、神真实存在的反映。这种观念的错误在于()。","夸大了意识的能动作用","把意识看成是物质的产物","认为意识是对存在的直观反映","混淆了人类意识自然演化的阶段","C",1,"A错误。从题干本身来看，不是在夸大意识的能动作用，相反是在否认意识的能动作用。B错误。此选项把意识看成是物质的产物，本身是一种唯物主义的观点，不符合题意C正确。意识是人脑的机能和属性，是客观世界的主观映象。客观世界中并没有鬼、神，鬼、神是人脑对客观世界的歪曲反映，可以从人世间找到它的原型，是人按照自己的形象塑造出来的。意识是对客观事物的反映，但人的意识不是照镜子似的原物映现，即直观反映。意识是物质的产物，但又不是物质本身。D错误。意识作为一种反映形式，它的形成经历一-切物质所具有的反应特性到低等生物的刺激感应性，再到高等动物的感觉和心理，最终发展为人类的意识这样三个发展阶段，题干并没有涉及到此问题。");
//        s11.add(s3);
//        SingleChoice s4 = new SingleChoice("1914年至1918年的第一次世界大战，是一场空前残酷的大屠杀。它改变了世界政治的格局，也改变了各帝国主义国家在中国的利益格局，对中国产生了巨大的影响。大战使中国的先进分子()。","对中国传统文化产生怀疑","对西方资产阶级民主主义产生怀疑","认识到工人阶级的重要作用","认识到必须优先改造国民性","B",1,"本题考查的知识点为五四运动前后的新文化运动的区别。第一次世界大战及巴黎和会都暴露出资本主义制度固有的不可克服的矛盾以及资产阶级民主的弱点，巴黎和会的外交失败使中国的先进分子对西方资产阶级民主主义产生怀疑。B选项正确。新文化运动一开始就是对中国传统文化的怀疑。A选项错误。五四运动是中国工人阶级登上历史舞台的标志，此前的一战并没有让中国先进分子认识到工人阶级的重要作用。C选项错误。五四运动之前的新文化运动把改造国民性置于优先的地位。D选项错误。故正确答案为B。");
//        s11.add(s4);
//        SingleChoice s5 = new SingleChoice("1905年11月，在同盟会机关报《民报》发刊词中，孙中山将同盟会的纲领概括为三大主义，后被称为三民主义。在这一时期，孙中山主张()。","建立“民族独立的国家”","扫除“恶劣政治的根本”","实现“耕者有其田”","民权应“为一般平民所共有”","AB",2,"A正确民族主义即民族革命要求建立民族独立的国家; B正确，民权主义即政治革命要扫除“恶劣政治之根本”;C、D错误，CD是新三民主义，相对于旧三民主义的发展。故正确答案为AB。");
//        s11.add(s5);
//        SingleChoice s6 = new SingleChoice("邓小平指出:社会主义究竟是个什么样子，苏联搞了很多年，也并没有完全搞清楚。可能列宁的思路比较好，搞了个新经济政策，但是后来苏联的模式僵化了。列宁新经济政策关于社会主义的思维之所以“比较好”是因为()。","提出了比较系统的社会主义建设纲领","根据俄国的实际情况来探索社会主义建设的道路","为俄国找到了一种比较成熟的社会发展模式","按照马克思恩格斯关于未来社会的设想来建设社会主义","B",1,"本题考查的知识点为列宁领导下的苏维埃俄国对社会主义的探索。1921年3月，俄共(布)召开十大，决定从战时共产主义政策过渡到以发展商品经济为主要特征的新经济政策。这一决定，表明列宁的社会主义建设思想发生了重大转变，对俄国社会主义的发展道路有了新的认识，标志着列宁正在找到一条符合俄国情况的建设社会主义的道路。B项正确。A项错误，列宁虽然对建设社会主义提出过许多精辟的论述，但并没有提出比较系统的社会主义建设纲领; C项错误，列宁并没有为俄国找到一种成熟的社会发展模式，新经济政策也是一种探索性质的道路; D项错误,列宁并不是按照马克思主义关于社会主义的设想建设俄国的社会主义，而是将马克思主义与俄国的具体国情相结合，建设俄国的社会主义。故正确答案为B。");
//        s11.add(s6);
//        singleChoices = new ArrayList<>();
//        for(int i = 0;i<num;i++){
//            SingleChoice s = s11.get(i);
//            singleChoices.add(s);
//        }
//
//        //获取控件
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                        0x3F, 0x25)));
//                deleteItem.setWidth(dp2px(90));
//                deleteItem.setIcon(R.drawable.ic_delete);
//                menu.addMenuItem(deleteItem);
//            }
//        };
//        swipeMenuListView = findViewById(R.id.swipelistView);
//        swipeMenuListView.setMenuCreator(creator);
//
//         /*
//        设置监听
//         */
//        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                SingleChoice s = singleChoices.get(position);
//                switch (index){
//                    case 0:
////                        delete(q);数据库中删除
//                        singleChoices.remove(position);
//                        myWListAdapter.notifyDataSetChanged();
//                        break;
//                }
//                return true;
//            }
//        });
//        // 监测用户在ListView的SwipeMenu侧滑事件
//        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
//            @Override
//            public void onSwipeStart(int position) {
//                Log.d("位置:" + position, "开始侧滑...");
//            }
//
//            @Override
//            public void onSwipeEnd(int position) {
//
//            }
//        });
//        //绑定Adapter
//        myWListAdapter = new MyWListAdapter(getApplicationContext(),singleChoices);
//        swipeMenuListView.setAdapter(myWListAdapter);
//
//    }
//    public int dp2px(float dipValue) {
//        final float scale = this.getResources().getDisplayMetrics().density;
//        return (int) (dipValue * scale + 0.5f);
//    }
//}