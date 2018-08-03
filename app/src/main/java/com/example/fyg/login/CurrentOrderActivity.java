package com.example.fyg.login;

import android.content.Intent;
import android.os.Looper;
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

public class CurrentOrderActivity extends AppCompatActivity {
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
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Proposer proposer=new Proposer();
                if(i==0||i==proposer.length+1){
                }
                else{
                    if(i<=proposer.length) {
                        if(proposer.proposers[i-1].express_state==1) {
                            proposer.num=i-1;
                            postReInfo(i - 1);
                            startActivity(new Intent(CurrentOrderActivity.this, ProposalActivity.class));
                        }
                        else if(proposer.proposers[i-1].express_state==0){
                            proposer.num=i-1;
                            postReInfo(i - 1);
                            startActivity(new Intent(CurrentOrderActivity.this, ProposalActivity.class));
                        }
                        else;
                    }
                    else{
                        Accepter accepter=new Accepter();
                        accepter.num=i-proposer.length-2;
                        postGeInfo(i-proposer.length-2);
                        startActivity(new Intent(CurrentOrderActivity.this, AccepterActivity.class));
                    }
                }
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
            list.add("收货    是否有人接单     接单人   收货时间");
            for (i = 0; i < proposer.length; i++) {
                if(proposer.proposers[i].express_state==1)
                    list.add(i + 1 + ".    是" + "     " + proposer.proposers[i].user + "     " + proposer.proposers[i].rece_time);
                else
                    list.add(i + 1 + ".    否");
            }
            mListView.setAdapter(adapter);
            return list;
        }
        else if(proposer.length==0&&accepter.length!=0){
            list.add("取货    收货人   收货时间   取货地点   收货地点");
            for (i = 0; i < accepter.length; i++) {
                list.add(i + 1 + ".    " + accepter.accepters[i].user + "     " + accepter.accepters[i].rece_time + "     " + accepter.accepters[i].srcplace+"     "+accepter.accepters[i].dstplace);
            }
            mListView.setAdapter(adapter);
            return list;
        }
        else{
            list.add("收货    是否有人接单     接单人   收货时间");
            for (i = 0; i < proposer.length; i++) {
                if(proposer.proposers[i].express_state==1)
                    list.add(i + 1 + ".    是" + "     " + proposer.proposers[i].user + "     " + proposer.proposers[i].rece_time);
                else
                    list.add(i + 1 + ".    否");
            }
            list.add("取货    收货人   收货时间   取货地点   收货地点");
            for (i=0; i < accepter.length; i++) {
                list.add(i  +1+ ".    " + accepter.accepters[i].user + "     " + accepter.accepters[i].rece_time + "     " + accepter.accepters[i].srcplace+"     "+accepter.accepters[i].dstplace);
            }
            mListView.setAdapter(adapter);
            return list;
        }
    }

    public void postReInfo(int j){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
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
                .url("http://47.100.116.160:5000/item/proposer_user2")
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

    public void postGeInfo(int j){
        System.out.println(j);
        OkHttpClient client = new OkHttpClient();
        User user=new User();
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
                .url("http://47.100.116.160:5000/item/accepter_user2")
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

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
