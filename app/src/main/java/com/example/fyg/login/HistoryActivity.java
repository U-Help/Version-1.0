package com.example.fyg.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HistoryActivity extends Activity {
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        _CollectorActivity.addActivity(this);

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this, R.layout.list_item1, getData());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HistoryActivity.this, i+1+"被单击了", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HistoryActivity.this, HistoryDetailActivity.class));
            }
        });
    }

    private ArrayList<String> getData() {
        list.add("帮忙    2018.7.31");
        list.add("取件    2018.7.16");
        list.add("取件    2018.7.15");
        list.add("取件    2018.7.13");
        list.add("帮忙    2018.7.12");
        return list;
    }
    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
