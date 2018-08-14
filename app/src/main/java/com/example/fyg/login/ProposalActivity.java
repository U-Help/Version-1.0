package com.example.fyg.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProposalActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout mSwipeLayout;//
    private Button btnQ;
    private Button btnD;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);
        _CollectorActivity.addActivity(this);
        Proposer proposer=new Proposer();
        int num=proposer.num;

        btnQ=findViewById(R.id.btnQ);
        btnD=findViewById(R.id.btnD);
        if(proposer.proposers[num].express_state==1) {
            btnD.setVisibility(View.INVISIBLE);
            btnQ.setVisibility(View.VISIBLE);
            init1();
        }
        else{
            btnQ.setVisibility(View.INVISIBLE);
            btnD.setVisibility(View.VISIBLE);
            init2();
        }
        try {
            btnQ.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Succeed();
                }
            });
        }catch(Exception e) {
            ;
        }

        try {
            btnD.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Cancel();
                }
            });
        }catch (Exception e){
            ;
        }
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container3);


        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);/**/
    }

    public void init1(){
        Proposer proposer=new Proposer();
        int num=proposer.num;
        String dstplace=proposer.proposers[num].dstplace;
        String srcplace=proposer.proposers[num].srcplace;
        String username=proposer.proposers[num].user;
        String rece_time=proposer.proposers[num].rece_time;;
        String phone=proposer.proposers[num].rephone;
        String price=proposer.proposers[num].price;
        String size=proposer.proposers[num].size;
        String rev_password=proposer.proposers[num].rev_password;
        TextView text = (TextView) this.findViewById(R.id.textView3);
        String str = "接单者：" +username+"\n"+
                "接单者电话：" +phone+"\n"+
                "取货地点：" +srcplace+"\n"+
                "收货地点：" +dstplace+"\n"+
                "快件大小："+size+"\n"+
                "价格：" +price+"\n"+
                "收货时间：" +rece_time+"\n"+
                "取件密码：" +rev_password+"\n";
        text.setText(str);
    }

    public void init2(){
        Proposer proposer=new Proposer();
        int num=proposer.num;
        String dstplace=proposer.proposers[num].dstplace;
        String srcplace=proposer.proposers[num].srcplace;
        String rece_time=proposer.proposers[num].rece_time;
        String price=proposer.proposers[num].price;
        String size=proposer.proposers[num].size;
        String rev_password=proposer.proposers[num].rev_password;
        TextView text = (TextView) this.findViewById(R.id.textView3);
        String str =
                "取货地点：" +srcplace+"\n"+
                "收货地点：" +dstplace+"\n"+
                "快件大小："+size+"\n"+
                "价格：" +price+"\n"+
                "收货时间：" +rece_time+"\n"+
                "取件密码：" +rev_password+"\n";
        text.setText(str);
    }

    public void Succeed(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        Proposer proposer=new Proposer();
        int i=user.id;
        int num=proposer.num;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.proposers[num].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/ok")
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

                        if (jsonObject.getString("state").equals("success")) {
                            flag=true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(ProposalActivity.this, "确认收货成功", Toast.LENGTH_LONG).show();
                        finish();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(ProposalActivity.this, "确认收货失败", Toast.LENGTH_LONG).show();
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
                Proposer proposer=new Proposer();
                int num=proposer.num;
                if(proposer.proposers[num].express_state==1) {
                    postReInfo1(num);
                    init1();
                }
                else{
                    postReInfo2(num);
                    init2();
                }
                mSwipeLayout.setRefreshing(false);
            }
        });
    }/**/

    public void Cancel(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        Proposer proposer=new Proposer();
        int i=user.id;
        int num=proposer.num;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.proposers[num].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160/item/pro_cut")
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

                        if (jsonObject.getString("state").equals("success")) {
                            flag=true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(ProposalActivity.this, "取消订单成功", Toast.LENGTH_LONG).show();
                        finish();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(ProposalActivity.this, "取消订单失败", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
