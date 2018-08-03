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

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.*;

public class HelpGetActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_get);
        _CollectorActivity.addActivity(this);

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this, R.layout.list_item1, getData());
        mListView.setAdapter(adapter);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    User user=new User();
                    user.num=i;
                    Toast.makeText(HelpGetActivity.this, i+1 + "被单击了", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HelpGetActivity.this, DetailActivity.class));
            }
        });
    }

    private ArrayList<String> getData() {
        Order order=new Order();
        //System.out.println(order.length);
        if(order.length==0) {
            list.add("暂时无订单");
            return list;
        }
        for(int i=1;i<order.length+1;i++){
            list.add("项目 "+i+"\n"+"收货时间"+order.orders[i-1].rece_time+"\n"+"取货点"+order.orders[i-1].srcplace+"\n"+"送达点"+order.orders[i-1].dstplace+"\n\n");
        }
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
               refreshData();
               refreshData();
               mSwipeLayout.setRefreshing(false);
           }
        });
    }

    private void refreshData() {
        Order order=new Order();
        getInfo();
        list.clear();
        if(order.length==0){
            list.add("暂时无订单");
            mListView.setAdapter(adapter);
            return;
        }
        for(int i=1;i<order.length+1;i++){
            list.add("项目 "+i+"\n"+"收货时间"+order.orders[i-1].rece_time+"\n"+"取货点"+order.orders[i-1].srcplace+"\n"+"送达点"+order.orders[i-1].dstplace+"\n\n");
        }
        mListView.setAdapter(adapter);
        /*list.add(0, String.valueOf((int) (Math.random() * 10)));
        adapter.notifyDataSetChanged();*/
    }

    public void getInfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("offset","0");
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/hall")
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
                            Order order=new Order();
                            order.length=0;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(i = 0;i < jsonArray.length();i++){
                                Order s=new Order();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                s.setDstplace(jsonObject1.getString("dstplace"));
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
                                s.setUsername(jsonObject1.getString("username"));
                                s.setMsg(jsonObject1.getString("msg"));
                                order.orders[i]=s;
                                order.length=i+1;
                            }
                            System.out.println(order.length);
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
}
