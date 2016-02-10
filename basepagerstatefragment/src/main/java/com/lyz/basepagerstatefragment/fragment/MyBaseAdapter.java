package com.lyz.basepagerstatefragment.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.fragment.bean.PagerInfo;
import com.lyz.basepagerstatefragment.widget.PagerSlidingTabStrip;

import java.util.ArrayList;

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
class MyBaseAdapter extends FragmentStatePagerAdapter {
    private final FragmentManager fm;
    private final ViewPager viewPager;
    private PagerSlidingTabStrip tabStrip;
    private final Context mContext;
    ArrayList<PagerInfo> mData=new ArrayList<PagerInfo>();


    public MyBaseAdapter(FragmentManager fm , ViewPager viewPager,PagerSlidingTabStrip tabStrip) {
        super(fm);
        this.fm = fm;
        this.viewPager = viewPager;
        this.tabStrip = tabStrip;
        this.mContext=viewPager.getContext();
        viewPager.setAdapter(this);
        tabStrip.setViewPager(viewPager);

    }

      /** 封装方法
       *
       * */
      public void addFragment(PagerInfo info){

          View view = View.inflate(mContext, R.layout.base_viewpage_fragment_tab_item, null);
          TextView title = (TextView) view.findViewById(R.id.tab_title);
          title.setText(info.title);
          tabStrip.addTab(view);

          mData.add(info);

          notifyDataSetChanged();

      }
      public void addTab(PagerInfo info){
          PagerInfo pagerInfo =new PagerInfo(info.title,info.bundle,info.clzz);
          addFragment(pagerInfo);

      }
      public void addTabs(ArrayList<PagerInfo> pagerInfoArrayList ){
          for (PagerInfo info: pagerInfoArrayList
               ) {
              addFragment(info);
          }
      }

    @Override
    public Fragment getItem(int position) {
        PagerInfo pagerInfo = mData.get(position);
        return Fragment.instantiate(mContext,pagerInfo.clzz.getName() ,pagerInfo.bundle );
    }


    @Override
    public int getCount() {
        return mData.size();
    }
}
