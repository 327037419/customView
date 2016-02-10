package com.lyz.basepagerstatefragment.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/4 0004.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class ToggleView extends RelativeLayout {


    private  boolean isToggleOn;//是否是开启状态
    private ImageView ivOn;
    private boolean images =true;


    public ToggleView(Context context) {
        this(context, null);
    }

    public ToggleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleView(Context context, AttributeSet set, int defStyleAttr) {
        super(context, set, defStyleAttr);

        View view = View.inflate(getContext(), R.layout.item_view, this);
        /** 获取id */
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        ivOn = (ImageView) view.findViewById(R.id.iv_view_on);
        // 读取自定的属性
        TypedArray typedArray = getContext().obtainStyledAttributes(set,R.styleable.ToggleView);
        String title = typedArray.getString(R.styleable.ToggleView_mtitle);
        images = typedArray.getBoolean(R.styleable.ToggleView_mimages, false);
//        回收
        typedArray.recycle();
//
        //为true就是设置了 图片 就是On,我靠,这里 我写错了 , 是setVisibility ,来控制显示和隐藏
        ivOn.setVisibility(images ? View.VISIBLE: View.GONE);
        tvName.setText(title);

        //默认开启状态
        toggle();

    }
//    实现滑块原理
    private  void setToggleOn(boolean ToggleOn){
        if (isToggleOn!=ToggleOn){
            this.isToggleOn=ToggleOn;
            if (isToggleOn){
                ivOn.setImageResource(R.drawable.on);
            }else{
                ivOn.setImageResource(R.drawable.off);
            }
        }
    }

    public boolean isToggleOn(){
        return  isToggleOn;
    }

    // 如果打开就关闭，如果关闭就打开
    public  void toggle(){
      setToggleOn(!isToggleOn);//把当前状态取返,进行操作
    }

}
