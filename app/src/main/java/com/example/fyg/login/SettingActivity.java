package com.example.fyg.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    private Button btnPasswordChange;
    private Button btnAddress;
    private Button btnShare;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        _CollectorActivity.addActivity(this);

        btnPasswordChange=findViewById(R.id.btnPasswordchange);
        btnPasswordChange.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SettingActivity.this,ChangePasswordActivity.class));
            }
        });
        btnAddress=findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SettingActivity.this,AddressActivity.class));
            }
        });
        btnShare=findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(SettingActivity.this,ShareActivity.class));
            }
        });
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
