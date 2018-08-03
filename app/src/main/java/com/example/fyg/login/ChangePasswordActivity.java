package com.example.fyg.login;

import android.os.Looper;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.view.View.OnFocusChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.String;
import java.util.HashMap;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.Button;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText a1;
    private TextInputEditText a2;
    private TextInputEditText a3;
    private Button btn_Submit;
    private Button btn_Delete;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        _CollectorActivity.addActivity(this);

        a1 = (EditText)findViewById(R.id.a1);
        a2 = (TextInputEditText) findViewById(R.id.a2);
        a3 = (TextInputEditText) findViewById(R.id.a3);
        btn_Submit= findViewById(R.id.btn_Submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });
        btn_Delete= findViewById(R.id.btn_Delete);
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView(){
        //设置焦点事件
        a1.setOnFocusChangeListener(new OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //这里加入name获取焦点事件时所要实现的逻辑
                }else{
                    //这里加入name失去焦点事件时所要实现的逻辑str
                   reset();
                }
            }
        });
    }

    public void reset() {
        OkHttpClient client = new OkHttpClient();
        String password = a1.getText().toString();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("id",id);
        map.put("token",token);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/user/reset1")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(ChangePasswordActivity.this, "Reset Failed", Toast.LENGTH_LONG).show();
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

                    User user=new User();
                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(ChangePasswordActivity.this, "原始密码正确", Toast.LENGTH_LONG).show();
                        user.success="success";
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(ChangePasswordActivity.this, "原始密码错误", Toast.LENGTH_LONG).show();
                        user.success="failure";
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(ChangePasswordActivity.this, "Reset Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    public void submit(){
        String Newpassword1 = a2.getText().toString();
        String Newpassword2 = a3.getText().toString();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        if(user.success.equals("failure")){
            a1.setText("");
            a2.setText("") ;
            a3.setText("") ;
        }
        if(user.success.equals("success")&&!Newpassword1.equals(Newpassword2)){
            a2.setText("") ;
            a3.setText("") ;
        }
        if(Newpassword1.equals(Newpassword2)&&user.success.equals("success")){
            OkHttpClient client = new OkHttpClient();

            HashMap<String, String> map = new HashMap<>();
            map.put("id",id);
            map.put("token",token);
            map.put("newpassword", Newpassword1);
            JSONObject jsonObject = new JSONObject(map);
            String jsonStr = jsonObject.toString();
            RequestBody body = RequestBody.create(JSON, jsonStr);
            Request request = new Request.Builder()
                    .url("http://47.100.116.160:5000/user/reset2")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ChangePasswordActivity.this, "Reset Failed", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ChangePasswordActivity.this, "Reset Successfully", Toast.LENGTH_LONG).show();
                            finish();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(ChangePasswordActivity.this, "Reset Failure", Toast.LENGTH_LONG).show();
                            a1.setText("");
                            Looper.loop();
                        }
                    } else {
                        Looper.prepare();
                        Toast.makeText(ChangePasswordActivity.this, "Reset Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }
            });
        }
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
