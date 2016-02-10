package com.lyz.basepagerstatefragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.view.ToggleView;

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
public class NoteFragment extends Fragment implements View.OnClickListener {


    private TextView tv_content;
    private Bundle bundle;
    private String arguments;
    private ToggleView toggleView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();

        if (bundle !=null){
            arguments = (String) bundle.get("key");


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.fragment_note,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        toggleView = (ToggleView) view.findViewById(R.id.toggleView);
        toggleView.setOnClickListener(this);
        if (bundle==null){
            tv_content.setText("我是一个测试用的Fragment, 我创建的时候没有传进来Bundle, 所以显示这个内容.");
        }else{
            tv_content.setText(arguments);

        }

    }


    @Override
    public void onClick(View v) {
        toggleView.toggle();//界面改变
    }
}
