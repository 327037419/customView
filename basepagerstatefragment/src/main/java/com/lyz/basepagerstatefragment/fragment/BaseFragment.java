package com.lyz.basepagerstatefragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.widget.PagerSlidingTabStrip;

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
 * ============================================================
 **/
public abstract class BaseFragment  extends Fragment{


    protected ViewPager mViewpager;
    private PagerSlidingTabStrip mStrip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.base_viewpage_fragment,null);


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mViewpager = (ViewPager) view.findViewById(R.id.mpager);

        mStrip = (PagerSlidingTabStrip)view.findViewById(R.id.pageSlidingStrip);

        MyBaseAdapter adapter =new MyBaseAdapter(getChildFragmentManager(),mViewpager,mStrip);


        stupFragment(adapter);


    }

    protected abstract void stupFragment(MyBaseAdapter adapter);
    protected  void setOffscreenPageLimit( ){

    }


    protected Bundle getBundle(String string) {
        Bundle bundle = new Bundle();

        bundle.putString("key",string);

        return  bundle;
    }

}
