package com.example.fyg.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddaddrActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddr);
        _CollectorActivity.addActivity(this);
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
