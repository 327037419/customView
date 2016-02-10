package com.lyz.basepagerstatefragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.fragment.bean.Person;
import com.lyz.basepagerstatefragment.widget.Logs;

import java.util.List;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/6 0006.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/

public class MyAdapter extends BaseAdapter {
    private List<Person> list;
    private LayoutInflater inflater;

    public MyAdapter(Context context, List<Person> list){
        this.list = list;
        Logs.e("list:"+list.size());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person person = (Person) this.getItem(position);
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.mTextName = (TextView) convertView.findViewById(R.id.text_name);
            viewHolder.mTextSex = (TextView) convertView.findViewById(R.id.text_sex);
            viewHolder.mTextAge = (TextView) convertView.findViewById(R.id.text_age);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextName.setText(person.getName());
        viewHolder.mTextSex.setText(person.getSex());
        viewHolder.mTextAge.setText(person.getAge() + "岁");


        return convertView;
    }

    public static class ViewHolder{
        public TextView mTextName;
        public TextView mTextSex;
        public TextView mTextAge;

    }

}
