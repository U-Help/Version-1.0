package com.example.fyg.login;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ExitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        _CollectorActivity.addActivity(this);
        AppExit(this);
        //_CollectorActivity.finishAll();
    }

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }

    public void AppExit(Context context){
        try{
            _CollectorActivity.finishAll();
            ActivityManager activityMgr = (ActivityManager)context.
                    getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        }catch (Exception ignored){}
    }
}

