package com.lyz.basepagerstatefragment.fragment.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
public class User implements Parcelable {

     public  String name;
     public  int age;
     public  boolean  isMale;
     public  Book  book;



    protected User(Parcel in) {
        name=in.readString();
        age=in.readInt();
        isMale=in.readInt()==1;//说明为true
        /** 把当前线程的 class ClassLoader 给传进入*/
        book=in.readParcelable(Thread.currentThread().getContextClassLoader());

    }
    /** 反序列化 对象 ,  读取 对象 */
    public static final Creator<User> CREATOR = new Creator<User>() {
        /** 从Parcel 怎么得到一个对象 */
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }


    /** 序列化 对象 ,   写入*/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeInt(isMale ? 1 : 0);
        dest.writeParcelable(book,0); //flags一般传入0 就可以;

    }
}
