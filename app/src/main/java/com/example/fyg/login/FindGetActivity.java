package com.example.fyg.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import okhttp3.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FindGetActivity extends AppCompatActivity {
    /*private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private Spinner mSpinner = null;*/
    private Button btnCancel;
    private Button btnPush;
    private EditText editDstPlace;
    private EditText editRecePassword;
    private EditText editTime;
    private EditText editSize;
    private EditText editPrice;
    private EditText editAddress;
    private TextView tv;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_get);
        _CollectorActivity.addActivity(this);

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnPush = findViewById(R.id.btnPush);
        btnPush.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //judge();
                push();
            }
        });
        editAddress = findViewById(R.id.editAddress);
        editRecePassword = findViewById(R.id.editRecePassword);
        editTime = findViewById(R.id.editTime);
        editSize = findViewById(R.id.editSize);
        editPrice = findViewById(R.id.editPrice);
        editDstPlace = findViewById(R.id.editDstplace);
        // 获取界面布局文件中的Spinner组件
        /*mSpinner = (Spinner) findViewById(R.id.spin);

        String[] arr = { "初识Android开发", "Android初识开发", "Android中级开发",
                "Android高级开发", "Android开发进阶"};
        // 创建ArrayAdapter对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arr);
        // 为Spinner设置Adapter
        mSpinner.setAdapter(adapter);

        // 为Spinner设置选中事件监听器
        mSpinner.setOnItemSelectedListener(this);*/
    }

    /*public void judge(){
        String time=editTime.getText().toString();
        if(isTime(time)==false) {
            tv = (TextView) findViewById(R.id.tv);
            tv.setText("时间格式错误");
        }else{
            push();
        }

    }

    public static boolean isTime(String time) {
        String REGEX_TIME = "^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$";
        return Pattern.matches(REGEX_TIME, time);
    }*/

    public void push(){
        OkHttpClient client = new OkHttpClient();
        String address = editAddress.getText().toString();
        String receaddress = editDstPlace.getText().toString();
        String time = editTime.getText().toString();
        String size = editSize.getText().toString();
        String price = editPrice.getText().toString();
        String recepassword= editRecePassword.getText().toString();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        if(id.equals("")||token.equals("")||receaddress.equals("")||address.equals("")||time.equals("")||size.equals("")||price.equals("")||recepassword.equals("")) {
            Toast.makeText(FindGetActivity.this, "请将信息输入完整", Toast.LENGTH_LONG).show();
            return;
        }
            map.put("id", id);
            map.put("token", token);
            map.put("dstplace", receaddress);
            map.put("srcplace", address);
            map.put("rece_time", time);
            map.put("size", size);
            map.put("price", price);
            map.put("rev_password", recepassword);
            map.put("msg", "我要漂亮小姐姐");
            JSONObject jsonObject = new JSONObject(map);
            String jsonStr = jsonObject.toString();
            RequestBody body = RequestBody.create(JSON, jsonStr);
            Request request = new Request.Builder()
                    .url("http://47.100.116.160:5000/item/propose")
                    .post(body)
                    .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(FindGetActivity.this, "未连接到服务器", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);

                        if (jsonStr.getString("state").equals("success")) {
                            flag = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(FindGetActivity.this, "发布成功", Toast.LENGTH_LONG).show();
                        finish();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(FindGetActivity.this, "发布失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(FindGetActivity.this, "服务器未响应 " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String content = parent.getItemAtPosition(position).toString();
        if(parent.getId()==R.id.spin)
            Toast.makeText(FindGetActivity.this, "选择的收货地址是：" + content, Toast.LENGTH_SHORT).show();
    }*/

   /* @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }*/
   @Override
   protected void onDestroy(){
       _CollectorActivity.removeActivity(this);
       super.onDestroy();
   }
}
