package com.lyz.basepagerstatefragment.widget;

import android.content.Context;
import android.content.SharedPreferences;

public class UtilsMpref {

    private static SharedPreferences mPref;

    public static SharedPreferences getSharedPreferences(Context context) {
        if (mPref == null) {

            mPref = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return mPref;
    }

    public static boolean getBoolean(Context context, String key) {
        return getBoolean(context, key, false);// 对自己类里的方法2次优化封装
    }

    /**
     * 获得 boolean类型的配置信息
     * 
     * @param key
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean devfVlaue) {
        return getSharedPreferences(context).getBoolean(key, devfVlaue);// 默认给true
                                                                        // ，说明我们想他上来是开启状态
    }

    /**
     * 保存 boolean类型的配置信息
     * 
     * @param key
     * @return
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        return getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    /**
     * 获得 String类型的配置信息,默认获得的value值为null
     * 
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, null);// 对自己类里的方法2次优化封装
    }

    /**
     * 获得 String类型的配置信息
     * 
     * @param key
     * @return
     */
    public static String getString(Context context, String key, String devfVlaue) {
        return getSharedPreferences(context).getString(key, devfVlaue);// 默认给true
                                                                       // ，说明我们想他上来是开启状态
    }

    /**
     * 设置String 类型的配置信息
     * 
     * @param key
     * @return
     */
    public static boolean putString(Context context, String key, String Vlaue) {
        // 获得sp对象，自己调用自己类里的方法
        return getSharedPreferences(context).edit().putString(key, Vlaue).commit();
    }

    /**
     * 设置String 类型的配置信息，默认存储值为null
     * 
     * @param key
     * @return
     */
    public static boolean putString(Context context, String key) {
        // 获得sp对象，自己调用自己类里的方法
        return getSharedPreferences(context).edit().putString(key, null).commit();
    }
    /**
     * 设置Int 类型的配置信息，默认存储值为value
     * 
     * @param key
     * @return
     */
    public static boolean putInt(Context context, String key,int value) {
        // 获得sp对象，自己调用自己类里的方法
        return getSharedPreferences(context).edit().putInt(key, value).commit();
    }
    /**
     * 设置Int 类型的配置信息，默认存储值为0
     * 
     * @param key
     * @return
     */
    public static int getInt(Context context, String key,int devfVlaue) {

        // 获得sp对象，自己调用自己类里的方法
        return getSharedPreferences(context).getInt(key, devfVlaue);
    }





    /**
     * 解绑
     * 
     * @param context
     * @param key
     * @return
     */
    public static boolean removeString(Context context, String key) {
        // 获得sp对象，自己调用自己类里的方法
        return getSharedPreferences(context).edit().remove(key).commit();
    }
}
