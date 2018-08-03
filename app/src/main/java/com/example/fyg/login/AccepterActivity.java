package com.example.fyg.login;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class AccepterActivity extends AppCompatActivity {
    private Button btnD1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepter);
        _CollectorActivity.addActivity(this);

        btnD1=findViewById(R.id.btnD1);
        btnD1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cancel();
            }
        });
        init();
    }

    public void init(){
        Accepter accepter=new Accepter();
        int num=accepter.num;
        String dstplace=accepter.accepters[num].dstplace;
        //System.out.println(proposer.proposers[num].dstplace);
        String srcplace=accepter.accepters[num].srcplace;
        //ystem.out.println(proposer.proposers[num].srcplace);
        String username=accepter.accepters[num].user;
        //System.out.println(proposer.proposers[num].user);
        String rece_time=accepter.accepters[num].rece_time;
        //System.out.println(proposer.proposers[num].rece_time);
        String phone=accepter.accepters[num].pro_phone;
        //System.out.println(proposer.proposers[num].rephone);
        String price=accepter.accepters[num].price;
        //System.out.println(proposer.proposers[num].price);
        String size=accepter.accepters[num].size;
        //System.out.println(proposer.proposers[num].size);
        String rev_password=accepter.accepters[num].rev_password;
        //System.out.println(proposer.proposers[num].rev_password);
        String msg=accepter.accepters[num].msg;
        //System.out.println(proposer.proposers[num].msg);
        TextView text = (TextView) this.findViewById(R.id.textView12);
        String str = "发单者：" +username+"\n"+
                "发单者电话：" +phone+"\n"+
                "取货地点：" +srcplace+"\n"+
                "收货地点：" +dstplace+"\n"+
                "快件大小："+size+"\n"+
                "价格：" +price+"\n"+
                "收货时间：" +rece_time+"\n"+
                "取件密码：" +rev_password+"\n"+
                "备注：" +msg+"\n";
        text.setText(str);
    }

    public void Cancel(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        Accepter accepter=new Accepter();
        int i=user.id;
        int num=accepter.num;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=accepter.accepters[num].item_id;

        HashMap<String, String> map = new HashMap<>();
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/acc_cut")
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
                        Toast.makeText(AccepterActivity.this, "放弃订单成功", Toast.LENGTH_LONG).show();
                        finish();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(AccepterActivity.this, "放弃订单失败", Toast.LENGTH_LONG).show();
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
