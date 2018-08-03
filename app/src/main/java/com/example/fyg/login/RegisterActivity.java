package com.example.fyg.login;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText editEmail;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editTelephone;
    private TextView tv;
    public static  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        _CollectorActivity.addActivity(this);

        btnRegister=findViewById(R.id.btnregister);
        editEmail=findViewById(R.id.editEmail);
        editUsername=findViewById(R.id.editUsename);
        editPassword=findViewById(R.id.editpassword);
        editTelephone=findViewById(R.id.editTelephone);
        initView();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                judge();
            }
        });
    }

    private void initView(){
        //设置焦点事件
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //这里加入name获取焦点事件时所要实现的逻辑
                }else{
                    //这里加入name失去焦点事件时所要实现的逻辑str
                    checkEmail();
                }
            }
        });
        editUsername.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    //这里加入name获取焦点事件时所要实现的逻辑
                }else{
                    //这里加入name失去焦点事件时所要实现的逻辑str
                    checkUsername();
                }
            }
        });
    }

    public void checkEmail() {
        OkHttpClient client = new OkHttpClient();
        String Email = editEmail.getText().toString();;

        HashMap<String, String> map = new HashMap<>();
        map.put("email", Email);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/user/email")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
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
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "邮箱已存在", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "Register Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    public void checkUsername() {
        OkHttpClient client = new OkHttpClient();
        String Email = editUsername.getText().toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("username", Email);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/user/username")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
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
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(RegisterActivity.this, "用户名已存在", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "Register Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    public void judge() {
        String email=editEmail.getText().toString();
        String username=editUsername.getText().toString();
        String password=editPassword.getText().toString();
        String telephone=editTelephone.getText().toString();
        if (isEmail(email)==false) {
            tv = (TextView) findViewById(R.id.tv);
            tv.setText("邮箱格式错误");
        } else if (isMobile(telephone)==false) {
            tv = (TextView) findViewById(R.id.tv);
            tv.setText("手机号错误");
        } else if (isPassword(password)==false) {
            tv = (TextView) findViewById(R.id.tv);
            tv.setText("密码只能为6-16位字母数字组合");
        } else {
            postRegister(email,username,password,telephone);
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
    public static boolean isMobile(String mobile) {
        String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
    public static boolean isEmail(String email) {
        if (email == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
        if (m.matches())
            return true;
        else
            return false;
    }


    public void postRegister(String email,String username,String password,String telephone) {
        OkHttpClient client = new OkHttpClient();
        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("username", username);
        map.put("password", password);
        map.put("telephone", telephone);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/user/register")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this, "Register Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
        finish();
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}





