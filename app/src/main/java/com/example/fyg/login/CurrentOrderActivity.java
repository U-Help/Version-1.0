package com.example.fyg.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;;

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

public class CurrentOrderActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        _CollectorActivity.addActivity(this);

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this,R.layout.list_item1, getData());
        mListView.setAdapter(adapter);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container1);


        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Proposer proposer=new Proposer();
                Accepter accepter=new Accepter();
                if(proposer.length==0&&accepter.length==0);
                else if(i<proposer.length) {
                    if(proposer.proposers[i].express_state==1) {
                        proposer.num=i;
                        postReInfo1(i);
                        startActivity(new Intent(CurrentOrderActivity.this, ProposalActivity.class));
                    }
                    else if(proposer.proposers[i].express_state==0){
                        proposer.num=i;
                        postReInfo2(i);
                        startActivity(new Intent(CurrentOrderActivity.this, ProposalActivity.class));
                    }
                    else;
                }
                else{
                    accepter.num=i-proposer.length;
                    postGeInfo(i-proposer.length);
                    startActivity(new Intent(CurrentOrderActivity.this, AccepterActivity.class));
                }
            }
        });
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
                postProposal();
                //postAccepter();
                //getData();
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //获取数据
                postAccepter();
                getData();
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    private ArrayList<String> getData() {
        Proposer proposer=new Proposer();
        Accepter accepter=new Accepter();
        int i;
        //System.out.println(order.length);
        if(proposer.length==0&&accepter.length==0) {
            list.add("暂时无订单");
            mListView.setAdapter(adapter);
            return list;
        }
        if(proposer.length!=0&&accepter.length==0) {
            for (i = 0; i < proposer.length; i++) {
                if(proposer.proposers[i].express_state==1)
                    list.add("收货"+ "\n\n接单状态：已被接单"  + "\n收货时间：" + proposer.proposers[i].rece_time+"\n\n");
                else
                    list.add("收货"+ "\n\n接单状态：未被接单"+"\n\n");
            }
            mListView.setAdapter(adapter);
            return list;
        }
        else if(proposer.length==0&&accepter.length!=0){
            for (i = 0; i < accepter.length; i++) {
                list.add("取货"+ "\n\n收货时间：" + accepter.accepters[i].rece_time+"\n取货地点："+accepter.accepters[i].srcplace+"\n收货地点："+accepter.accepters[i].dstplace+"\n\n");
            }
            mListView.setAdapter(adapter);
            return list;
        }
        else{
            for (i = 0; i < proposer.length; i++) {
                if(proposer.proposers[i].express_state==1)
                    list.add("收货"+ "\n\n接单状态：已被接单"  + "\n收货时间：" + proposer.proposers[i].rece_time+"\n\n");
                else
                    list.add("收货"+ "\n\n接单状态：未被接单"+"\n\n");
            }
            for (i=0; i < accepter.length; i++) {
                list.add("取货"+ "\n\n收货时间：" + accepter.accepters[i].rece_time+"\n取货地点："+accepter.accepters[i].srcplace+"\n收货地点："+accepter.accepters[i].dstplace+"\n\n");
            }
            mListView.setAdapter(adapter);
            return list;
        }
    }

    public void postReInfo1(int j){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        //user.denum=j;
        Proposer proposer=new Proposer();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.proposers[j].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/proposer_user2")
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
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);
                        JSONObject jsonObject1=jsonStr.getJSONObject("data");
                        if (jsonStr.getString("state").equals("success")) {
                            flag = true;
                            Proposer proposer1=new Proposer();
                            int j=proposer1.num;
                            proposer1.proposers[j].setDstplace(jsonObject1.getString("dstplace"));
                            //System.out.println(proposer1.proposers[j].dstplace);
                            proposer1.proposers[j].setSrcplace(jsonObject1.getString("srcplace"));
                            //System.out.println(proposer1.proposers[j].srcplace);
                            proposer1.proposers[j].setUser(jsonObject1.getString("username"));
                            //System.out.println(proposer1.proposers[j].user);
                            proposer1.proposers[j].setRece_time(jsonObject1.getString("rece_time"));
                            //System.out.println(proposer1.proposers[j].rece_time);
                            proposer1.proposers[j].setRephone(jsonObject1.getString("acc_phone"));
                            //System.out.println(proposer1.proposers[j].rephone);
                            proposer1.proposers[j].setPrice(jsonObject1.getString("price"));
                            //System.out.println(proposer1.proposers[j].price);
                            proposer1.proposers[j].setSize(jsonObject1.getString("size"));
                            //System.out.println(proposer1.proposers[j].size);
                            proposer1.proposers[j].setRev_password(jsonObject1.getString("rev_password"));
                            //System.out.println(proposer1.proposers[j].rev_password);
                            proposer1.proposers[j].setMsg(jsonObject1.getString("msg"));
                            //System.out.println(proposer1.proposers[j].msg);
                            proposer1.proposers[j].setItem_id(jsonObject1.getString("item_id"));
                            //System.out.println(proposer1.proposers[j].item_id);
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

    public void postReInfo2(int j){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        //user.denum=j;
        Proposer proposer=new Proposer();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.proposers[j].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/proposer_user3")
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
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);
                        JSONObject jsonObject1=jsonStr.getJSONObject("data");
                        if (jsonStr.getString("state").equals("success")) {
                            flag = true;
                            Proposer proposer1=new Proposer();
                            int j=proposer1.num;
                            proposer1.proposers[j].setDstplace(jsonObject1.getString("dstplace"));
                            //System.out.println(proposer1.proposers[j].dstplace);
                            proposer1.proposers[j].setSrcplace(jsonObject1.getString("srcplace"));
                            //System.out.println(proposer1.proposers[j].srcplace);
                            //System.out.println(proposer1.proposers[j].user);
                            proposer1.proposers[j].setRece_time(jsonObject1.getString("rece_time"));
                            //System.out.println(proposer1.proposers[j].rece_time);
                            //System.out.println(proposer1.proposers[j].rephone);
                            proposer1.proposers[j].setPrice(jsonObject1.getString("price"));
                            //System.out.println(proposer1.proposers[j].price);
                            proposer1.proposers[j].setSize(jsonObject1.getString("size"));
                            //System.out.println(proposer1.proposers[j].size);
                            proposer1.proposers[j].setRev_password(jsonObject1.getString("rev_password"));
                            //System.out.println(proposer1.proposers[j].rev_password);
                            proposer1.proposers[j].setMsg(jsonObject1.getString("msg"));
                            //System.out.println(proposer1.proposers[j].msg);
                            proposer1.proposers[j].setItem_id(jsonObject1.getString("item_id"));
                            //System.out.println(proposer1.proposers[j].item_id);
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

    public void postGeInfo(int j){
        System.out.println(j);
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        //user.denum=j;
        final Accepter accepter=new Accepter();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=accepter.accepters[j].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/accepter_user2")
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

                        JSONObject jsonStr = new JSONObject(str);
                        JSONObject jsonObject1=jsonStr.getJSONObject("data");
                        if (jsonStr.getString("state").equals("success")) {
                            flag=true;
                            Accepter accepter1=new Accepter();
                            int j=accepter1.num;
                            accepter1.accepters[j].dstplace=jsonObject1.getString("dstplace");
                            accepter1.accepters[j].srcplace=jsonObject1.getString("srcplace");
                            accepter1.accepters[j].user=jsonObject1.getString("username");
                            accepter1.accepters[j].rece_time=jsonObject1.getString("rece_time");
                            accepter1.accepters[j].pro_phone=jsonObject1.getString("pro_phone");
                            accepter1.accepters[j].price=jsonObject1.getString("price");
                            accepter1.accepters[j].size=jsonObject1.getString("size");
                            accepter1.accepters[j].rev_password=jsonObject1.getString("rev_password");
                            accepter1.accepters[j].msg=jsonObject1.getString("msg");
                            accepter1.accepters[j].item_id=jsonObject1.getString("item_id");
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

    public void postProposal(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        //map.put("offset","0");
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/proposer_user")
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
                        String state = jsonObject.getString("state");

                        if (state.equals("success")) {
                            flag = true;
                            Proposer proposer = new Proposer();
                            proposer.length = 0;
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (i = 0; i < jsonArray.length(); i++) {
                                Proposer s = new Proposer();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                s.setExpress_state(jsonObject1.getInt("express_state"));
                                //System.out.println(s.express_state);
                                s.setRece_time(jsonObject1.getString("rece_time"));
                                //System.out.println(s.rece_time);
                                s.setItem_id(jsonObject1.getString("item_id"));
                                //System.out.println(s.item_id);
                                if (s.express_state==1) {
                                    s.setUser(jsonObject1.getString("username"));
                                    System.out.println(s.user);
                                    //System.out.println(s.user);
                                }
                                /*s.setPrice(jsonObject1.getString("price"));
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
                                s.setEmail(jsonObject1.getString("email"));*/
                                proposer.proposers[i] = s;
                                proposer.length = i + 1;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void postAccepter(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        //map.put("offset","0");
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/accepter_user")
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
                            Accepter accepter=new Accepter();
                            accepter.length=0;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(i = 0;i < jsonArray.length();i++){
                                Accepter s=new Accepter();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                s.setUser(jsonObject1.getString("username"));
                                //System.out.println(s.user);
                                s.setRece_time(jsonObject1.getString("rece_time"));
                                //System.out.println(s.rece_time);
                                s.setItem_id(jsonObject1.getString("item_id"));
                                //System.out.println(s.item_id);
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.srcplace);
                                s.setDstplace(jsonObject1.getString("dstplace"));
                                //System.out.println(s.dstplace);
                                /*s.setProp_time(jsonObject1.getString("prop_time"));
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
                                s.setEmail(jsonObject1.getString("email"));*/
                                accepter.accepters[i]=s;
                                accepter.length=i+1;
                            }
                            //System.out.println(proposer.length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
