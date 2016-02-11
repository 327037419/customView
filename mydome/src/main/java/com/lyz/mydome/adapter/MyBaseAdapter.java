package com.lyz.mydome.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lyz.mydome.R;
import com.lyz.mydome.quickmain.Man;
import com.lyz.mydome.quickmain.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/11 0011.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class MyBaseAdapter extends BaseAdapter {


    ArrayList<Man> list = new ArrayList<>();


    //处理字母的数据

    public void setLetterList(String[] names) {

        for (int i = 0; i < names.length; i++) {
            char letter = StringUtils.getPinyin(names[i]).charAt(0);//拿到首字母
            Man man = new Man(names[i], letter);
            list.add(man);
        }

        Collections.sort(list);//排序下
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
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_man, null);
        }
        // 从初始化控件
        TextView tv_index = (TextView) convertView.findViewById(R.id.tv_index);
        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        Man man = list.get(position);
        tv_name.setText(man.getName());
        String manLetter = String.valueOf(man.getLetter());
        tv_index.setText(manLetter);

//        根据是否是某字母的第一个item来处理标题的可见和不可见
        if (position!=0){
            Man preMan=list.get(position - 1);//拿到上一个 item对象
            String preLetter = String.valueOf(preMan.getLetter());
            if (TextUtils.equals(manLetter,preLetter)){
//                上一个 和当前的一样,那么当前的item ,就不应该显示出来
                tv_index.setVisibility(View.GONE);
            }else{
                tv_index.setVisibility(View.VISIBLE);
            }
        }else{
            tv_index.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
