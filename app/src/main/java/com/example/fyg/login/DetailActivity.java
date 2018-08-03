package com.example.fyg.login;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

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

public class DetailActivity extends AppCompatActivity {
    private Button btn_accept;
    private Button btn_giveup;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        _CollectorActivity.addActivity(this);
        initView();
    }

    private void initView() {
        btn_accept = (Button) findViewById(R.id.btn_accept);
        btn_giveup = (Button) findViewById(R.id.btn_giveup);

        btn_accept.setOnClickListener(new MyClickListener1());
        btn_giveup.setOnClickListener(new MyClickListener2());

        Order order=new Order();
        User user=new User();
        int i=user.num;
        String username=order.orders[i].username;
        String srcplace=order.orders[i].srcplace;
        String dstplace=order.orders[i].dstplace;
        String size=order.orders[i].size;
        String price=order.orders[i].price;
        String rece_time=order.orders[i].rece_time;
        TextView text = (TextView) this.findViewById(R.id.textView8d);
        String str = "用户名：" +username+"\n"+
                "取货地点：" +srcplace+"\n"+
                "收货地点：" +dstplace+"\n"+
                "快件大小："+size+"\n"+
                "价格：" +price+"\n"+
                "收货时间：" +rece_time+"\n";
        text.setText(str);
    }

    class MyClickListener1 implements OnClickListener {
        @Override
        public void onClick(View v) {
            refresh();
        }
    }

    protected void refresh(){
        Order order=new Order();
        User user=new User();
        int i=user.num;
        String item_id=order.orders[i].item_id;
        System.out.println("item_id:"+item_id);
        accept(item_id);
        if(user.flag==true) {
            Toast.makeText(DetailActivity.this, "您接受了任务", Toast.LENGTH_LONG).show();
            TextView text = (TextView) this.findViewById(R.id.textView8d);
            String username = order.orders[i].username;
            String srcplace = order.orders[i].srcplace;
            String dstplace = order.orders[i].dstplace;
            String size = order.orders[i].size;
            String price = order.orders[i].price;
            String rece_time = order.orders[i].rece_time;
            String phone = order.orders[i].phone;
            String rev_password = order.orders[i].rev_password;
            String msg = order.orders[i].msg;
            String email = order.orders[i].email;
            String str = "用户名：" + username + "\n" +
                    "取货地点：" + srcplace + "\n" +
                    "收货地点：" + dstplace + "\n" +
                    "快件大小：" + size + "\n" +
                    "价格：" + price + "\n" +
                    "收货时间：" + rece_time + "\n" +
                    "电话：" + phone + "\n" +
                    "取件密码：" + rev_password + "\n" +
                    "备注：" + msg + "\n"+
                    "邮箱："+email+"\n";
            text.setText(str);
            btn_giveup.setVisibility(View.INVISIBLE);
            btn_accept.setVisibility(View.INVISIBLE);
        }
        else{
            Toast.makeText(DetailActivity.this, "出现故障了", Toast.LENGTH_LONG).show();
        }
    }

    class MyClickListener2 implements OnClickListener {
        @Override
        public void onClick(View v) {
            Toast.makeText(DetailActivity.this, "您取消了任务", Toast.LENGTH_LONG).show();
            startActivity(new Intent(DetailActivity.this, HelpGetActivity.class));
        }
    }

    public void accept(String item_id) {
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/accept")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(DetailActivity.this, "未连接到服务器", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);
                        User user=new User();
                        user.flag=false;
                        if (jsonStr.getString("state").equals("success")) {
                            user.flag=true;
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

