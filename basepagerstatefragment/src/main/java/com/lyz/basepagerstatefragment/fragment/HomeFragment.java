package com.lyz.basepagerstatefragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.activity.TomatoWork;
import com.lyz.basepagerstatefragment.widget.Constant;
import com.lyz.basepagerstatefragment.widget.Logs;
import com.lyz.basepagerstatefragment.widget.UtilsMpref;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/1 0001.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ==============
 * 首先: 图片需要一个点击事件, 跳转到开始计时界面,
 *   if  如果要是完成了一个番茄需要今日和 总计 加 一 ;
 *   else 不变
 *
 *    今日的 每天需要 半夜0点 清0 ,
 *
 *    总计永远累计;
 * ==============================================
 **/
public class HomeFragment extends Fragment implements View.OnClickListener {

    private TextView tv_content;
    private Bundle bundle;
    private String arguments;
    private ImageView iv_home;
    private TextView tv_today;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

        if (bundle != null) {
            arguments = (String) bundle.get("key");
        }
    }


/** 总计一直记录,  每日的每天需要清空 */
    @Override
    public void onResume() {
        super.onResume();
        /** 初始化 番茄的数量*/
        Logs.e("--");

        int tomatonumber = UtilsMpref.getInt(getActivity(), Constant.TOMATO_DAY_NUMBER, 0);
        tv_today.setText(String.valueOf(tomatonumber));

/** 如当前时间为半夜,00,我们就清空当天的 sp 番茄数量 */
        SimpleDateFormat formatter    =   new    SimpleDateFormat    ("HH");
        Date    curDate    =   new Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        if (str.equals("00")){
            UtilsMpref.putInt(getActivity(),Constant.TOMATO_DAY_NUMBER,0);
        }





            //每次取出上次的加入的这次的
        int tomato_count_number = UtilsMpref.getInt(getActivity(), Constant.TOMATO_COUNT_NUMBER, 0);

        UtilsMpref.putInt(getActivity(),Constant.TOMATO_COUNT_NUMBER,tomatonumber+tomato_count_number);




        tv_content.setText( String.valueOf(tomatonumber + tomato_count_number));
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_content = (TextView) view.findViewById(R.id.tv_count);
        tv_today = (TextView) view.findViewById(R.id.tv_today);
        iv_home = (ImageView) view.findViewById(R.id.home);



//        if (bundle == null) {  先不从父类拿数据
//            tv_content.setText("我是一个测试用的Fragment, 我创建的时候没有传进来Bundle, 所以显示这个内容.");
//        } else {
//            tv_content.setText(arguments);
//        }
        initData();
    }

    private void initData() {
        iv_home.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v==iv_home){
            getActivity().startActivity(new Intent(getActivity(), TomatoWork.class));
        }

    }
}
