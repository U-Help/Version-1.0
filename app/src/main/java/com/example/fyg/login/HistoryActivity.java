package com.example.fyg.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.fyg.login.MainActivity.JSON;

public class HistoryActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        _CollectorActivity.addActivity(this);

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this, R.layout.list_item1, getData());
        mListView.setAdapter(adapter);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container2);


        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private ArrayList<String> getData() {
        int i;
        _History history=new _History();
        History history1=new History();
        //System.out.println(order.length);
        if(history.length==0&&history1.length==0) {
            list.add("暂时无历史信息");
            mListView.setAdapter(adapter);
            return list;
        }
        for(i=0;i<history.length;i++){
            list.add("交易用户：" +history._histories[i].username+"\n"+
                    "完成时间："+history._histories[i].accept_time+"\n\n");
        }
        for(;i<history.length+history1.length;i++){
            list.add("交易用户：" +history1.histories[i-history.length].username+"\n"+
                    "完成时间："+history1.histories[i-history.length].accept_time+"\n\n");
        }
        mListView.setAdapter(adapter);
        return list;
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止刷新
                mSwipeLayout.setRefreshing(false);

            }
        }, 3000);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //获取数据
                list.clear();
                getInfo();
                getinfo();
                getData();
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    public void getInfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/pro_history")//acc_history
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int i;
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonObject = new JSONObject(str);
                        String state=jsonObject.getString("state");

                        if (state.equals("success")) {
                            flag=true;
                            _History history=new _History();
                            history.length=0;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(i = 0;i < jsonArray.length();i++){
                                _History s=new _History();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                                s.setUsername(jsonObject1.getString("username"));
                                //System.out.println(s.item_id);//
                                s.setAcc_phone(jsonObject1.getString("acc_phone"));
                                //System.out.println(s.accept_time);
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.rece_time);//
                                s.setDstplace(jsonObject1.getString("dstplace"));
                                s.setSize(jsonObject1.getString("size"));
                                s.setPrice(jsonObject1.getString("price"));
                                s.setAccept_time(jsonObject1.getString("accept_time"));
                                //System.out.println(s.username);

                                /*s.setDstplace(jsonObject1.getString("dstplace"));
                                //System.out.println(s.dstplace);
                                s.setIs_available(jsonObject1.getString("is_available"));
                                //System.out.println(s.is_available);
                                s.setItem_id(jsonObject1.getString("item_id"));
                                //System.out.println(s.item_id);
                                s.setPhone(jsonObject1.getString("phone"));
                                //System.out.println(s.phone);
                                s.setPrice(jsonObject1.getString("price"));
                                //System.out.println(s.price);
                                s.setProp_time(jsonObject1.getString("prop_time"));
                                //System.out.println(s.prop_time);
                                s.setRece_time(jsonObject1.getString("rece_time"));
                                //System.out.println(s.rece_time);
                                s.setRev_password(jsonObject1.getString("rev_password"));
                                //System.out.println(s.rev_password);
                                s.setSize(jsonObject1.getString("size"));
                                //System.out.println(s.size);
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.srcplace);
                                s.setUsername(jsonObject1.getString("username"));*///rece_time,accept_time,username,item_id
                                //s.setMsg(jsonObject1.getString("msg"));
                                history._histories[i]=s;
                                history.length=i+1;
                            }
                            //System.out.println(history.length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    //Toast.makeText(GetActivity.this, "服务器未响应" + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    public void getinfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/acc_history")//acc_history
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int i;
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonObject = new JSONObject(str);
                        String state=jsonObject.getString("state");

                        if (state.equals("success")) {
                            flag=true;
                            History history=new History();
                            history.length=0;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(i = 0;i < jsonArray.length();i++){
                                History s=new History();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                                s.setUsername(jsonObject1.getString("username"));
                                //System.out.println(s.item_id);//
                                s.setPro_phone(jsonObject1.getString("pro_phone"));
                                //System.out.println(s.accept_time);
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.rece_time);//
                                s.setDstplace(jsonObject1.getString("dstplace"));
                                s.setSize(jsonObject1.getString("size"));
                                s.setPrice(jsonObject1.getString("price"));
                                s.setAccept_time(jsonObject1.getString("accept_time"));
                                history.histories[i]=s;
                                history.length=i+1;
                            }
                            //System.out.println(history.length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    //Toast.makeText(GetActivity.this, "服务器未响应" + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        getInfo();
        getinfo();
        super.onResume();
    }
}