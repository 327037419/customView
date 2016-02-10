package com.lyz.basepagerstatefragment.fragment;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.fragment.bean.PagerInfo;
import com.lyz.basepagerstatefragment.widget.Logs;

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
public class TweetFragment extends BaseFragment {


    private static final String TAG ="TweetFragment";

    @Override
    protected void stupFragment(MyBaseAdapter adapter) {

        String[] stringArray = getResources().getStringArray(R.array.news_viewpage_arrays);
        Logs.e(TAG, "stupFragment: stringArray=" + getResources().getStringArray(
                R.array.news_viewpage_arrays));
        PagerInfo info1 =new PagerInfo(stringArray[0],getBundle("我是bundle 数据"),HomeFragment.class);
        PagerInfo info2 =new PagerInfo(stringArray[1],getBundle("我是bundle 数据"),NoteFragment.class);
        PagerInfo info3 =new PagerInfo(stringArray[2],getBundle("我是bundle 数据"),SettingFragment.class);
        adapter.addFragment(info1);
        adapter.addFragment(info2);
        adapter.addFragment(info3);
    }



    @Override
    protected void setOffscreenPageLimit() {

        mViewpager.setOffscreenPageLimit(2);

    }
}