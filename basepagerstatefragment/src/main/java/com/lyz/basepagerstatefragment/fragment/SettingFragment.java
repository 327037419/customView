package com.lyz.basepagerstatefragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.activity.TomtoSettingDome;
import com.lyz.basepagerstatefragment.activity.TomtoSettingMusic;
import com.lyz.basepagerstatefragment.view.ToggleView;
import com.lyz.basepagerstatefragment.widget.Constant;
import com.lyz.basepagerstatefragment.widget.UtilsMpref;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/3 0003.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class SettingFragment extends Fragment implements View.OnClickListener {


    @InjectView(R.id.tomato_dome)
    ToggleView tomato_dome;
    @InjectView(R.id.tomato_music)
    ToggleView tomatoMusic;
    @InjectView(R.id.tomato_time)
    ToggleView tomatoTime;
    @InjectView(R.id.is_vibrator)
    ToggleView isVibrator;


    private TextView tv_content;
    private Bundle bundle;
    private String arguments;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

        if (bundle != null) {
            arguments = (String) bundle.get("key");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_setting, null);

        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tomatoMusic.setOnClickListener(this);
        tomato_dome.setOnClickListener(this);
        isVibrator.setOnClickListener(this);


//        if (bundle==null){
//            tv_content.setText("我是一个测试用的Fragment, 我创建的时候没有传进来Bundle, 所以显示这个内容.");
//        }else{
//            tv_content.setText(arguments);
//
//        }

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {
            case R.id.tomato_music:
                intent.setClass(getActivity(), TomtoSettingMusic.class);
                getActivity().startActivity(intent);
                break;
            case R.id.tomato_dome:
                intent.setClass(getActivity(), TomtoSettingDome.class);
                getActivity().startActivity(intent);
                break;
            case R.id.is_vibrator:
/** 为什么点击完,位置变了 , 图片也显示出来了?????????  ,  方法 初始化 没给对
 * 显示默认是开启, 点击后关闭
 * */
//                Ui 改变
                isVibrator.toggle();

//                数据实现
                UtilsMpref.putBoolean(getActivity(), Constant.IS_VIBRATOR, isVibrator.isToggleOn());

                break;

        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
