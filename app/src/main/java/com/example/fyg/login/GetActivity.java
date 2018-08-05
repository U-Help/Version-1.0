package com.example.fyg.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
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

public class GetActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button btn_get;
    private Button btn_help;
    private Button btn_status;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        _CollectorActivity.addActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postProposal();
                postAccepter();
                startActivity(new Intent(GetActivity.this, CurrentOrderActivity.class));
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_get = findViewById(R.id.btn_get);
        btn_get.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(GetActivity.this, FindGetActivity.class));
            }
        });
        btn_help = findViewById(R.id.btn_help);
        btn_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getInfo();
                startActivity(new Intent(GetActivity.this, HelpGetActivity.class));
            }
        });
        btn_status = findViewById(R.id.btn_status);
        btn_status.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(GetActivity.this, HistoryActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.get, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            postProposal();
            postAccepter();
            startActivity(new Intent(GetActivity.this, CurrentOrderActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            startActivity(new Intent(GetActivity.this, ChangePasswordActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(GetActivity.this, AddressActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(GetActivity.this, HelpandReflectActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(GetActivity.this, ExitActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            startActivity(new Intent(GetActivity.this, SettingActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
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
                .url("http://47.100.116.160/item/hall")
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
                                s.setEmail(jsonObject1.getString("email"));
                                order.orders[i]=s;
                                order.length=i+1;
                            }
                            //System.out.println(order.length);
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
                                    //System.out.println(s.user);
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
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.srcplace);
                                s.setDstplace(jsonObject1.getString("dstplace"));
                                //System.out.println(s.dstplace);
                                s.setItem_id(jsonObject1.getString("item_id"));
                                //System.out.println(s.item_id);
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
}