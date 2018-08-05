package com.example.fyg.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xiuyuanc on 03/08/2018.
 */

public class HelpObjAdapter extends ArrayAdapter {
    private final int resourceId;

    public HelpObjAdapter(Context context, int textViewResourceId, List<HelpObj> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HelpObj help_obj = (HelpObj) getItem(position); // 获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView date = (TextView) view.findViewById(R.id.date);//获取该布局内的图片视图
        TextView src = (TextView) view.findViewById(R.id.src);//获取该布局内的文本视图
        TextView dst = (TextView) view.findViewById(R.id.dst);//获取该布局内的文本视图
        date.setText(help_obj.getDate());//为图片视图设置图片资源
        src.setText(help_obj.getSrc());//为图片视图设置图片资源
        dst.setText(help_obj.getDst());//为图片视图设置图片资源
        return view;
    }
}
