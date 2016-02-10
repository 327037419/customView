package com.lyz.basepagerstatefragment.fragment.bean;

import android.os.Bundle;

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
public class PagerInfo {

    public String title;
    public Bundle bundle;
    public Class<?>  clzz;


    public PagerInfo (String title, Bundle bundle , Class<?> clzz) {
        this.title = title;
        this.bundle = bundle;
        this.clzz = clzz;
    }
}
