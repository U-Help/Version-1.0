package com.example.fyg.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        _CollectorActivity.addActivity(this);

        TextView text = (TextView) this.findViewById(R.id.textView10);
        String str = "日期：\n" +
                "收/取货：\n" +
                "收件人：\n" +
                "货物主人：\n" +
                "交易价格：\n";
        text.setText(str);
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}