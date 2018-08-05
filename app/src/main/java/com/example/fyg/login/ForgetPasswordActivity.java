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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.String;
import java.util.HashMap;
import java.util.regex.Pattern;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.widget.Button;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText a12;
    private TextInputEditText a22;
    private TextInputEditText a32;
    private Button btn_Submit2;
    private Button btn_Delete2;
    public boolean flag;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        _CollectorActivity.addActivity(this);

        a12 = (EditText)findViewById(R.id.a12);
        a22 = (TextInputEditText) findViewById(R.id.a22);
        a32 = (TextInputEditText) findViewById(R.id.a32);
        btn_Submit2= findViewById(R.id.btn_Submit2);
        btn_Submit2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                judge();
                if(flag==true) {
                    submit();
                }
                finish();
            }
        });
        btn_Delete2= findViewById(R.id.btn_Delete2);
        btn_Delete2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void judge() {
        String username=a12.getText().toString();
        String password1=a22.getText().toString();
        String password2=a32.getText().toString();
        flag=false;
        if(username.length()>20){
            Toast.makeText(ForgetPasswordActivity.this, "用户名不能过长", Toast.LENGTH_LONG).show();
        } else if (isPassword(password1)==false) {
            Toast.makeText(ForgetPasswordActivity.this, "密码只能为6-16位字母数字组合", Toast.LENGTH_LONG).show();
        } else if (isPassword(password2)==false) {
            Toast.makeText(ForgetPasswordActivity.this, "密码只能为6-16位字母数字组合", Toast.LENGTH_LONG).show();
        }
        else{
            flag=true;
        }
    }

    public static boolean isPassword(String password) {
        String REGEX_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return Pattern.matches(REGEX_PASSWORD, password);
    }
    public static boolean isUserName(String username) {
        String REGEX_USERNAME = "^[a-zA-Z]\\\\w{5,17}$";
        return Pattern.matches(REGEX_USERNAME, username);
    }

    public void submit(){
        String username= a12.getText().toString();
        String Newpassword1 = a22.getText().toString();
        String Newpassword2 = a32.getText().toString();
        if(Newpassword1.equals(Newpassword2)){
            OkHttpClient client = new OkHttpClient();
            HashMap<String, String> map = new HashMap<>();
            map.put("username",username);
            map.put("password",Newpassword1);
            JSONObject jsonObject = new JSONObject(map);
            String jsonStr = jsonObject.toString();
            RequestBody body = RequestBody.create(JSON, jsonStr);
            Request request = new Request.Builder()
                    .url("http://47.100.116.160/user/forget")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ForgetPasswordActivity.this, "失败" , Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ForgetPasswordActivity.this, "成功" , Toast.LENGTH_LONG).show();
                            user.success="success";
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(ForgetPasswordActivity.this, "失败" , Toast.LENGTH_LONG).show();
                            user.success="failure";
                            Looper.loop();
                        }
                    } else {
                        Looper.prepare();
                        Toast.makeText(ForgetPasswordActivity.this, "失败" , Toast.LENGTH_LONG).show();
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