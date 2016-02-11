package com.lyz.mydome.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/10 0010.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ==
 *       需要把 imageview的 对象 拿到 来
 *
 * ==========================================================
 **/
public class ParallaxView extends ListView {
    private ImageView ivHeader;
    private int originH;
    private int maxHight;

    public ParallaxView(Context context) {
        super(context);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderImage(ImageView ivHeader) {
        this.ivHeader = ivHeader;
//        4.4.1 在setParallaxImagView方法获取图片的真实高度
        maxHight = (int) (ivHeader.getDrawable().getIntrinsicHeight()*0.7);

//        4.2 获取Header初始高度	4.2.1 HeaderView的layout里包含ImageView设置填充模式为centerCrop
        originH = ivHeader.getHeight();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

//        .1 当deltaY是负值，即下拉,同时isTouchEvent为true，
// 即是用户发起的滚动事件。则把deltaY的绝对值，添加到Header的高度上
//        Logs.e("ParallaxView.overScrollBy.deltaY="+deltaY+";scrollY="+scrollY
//				+";isTouchEvent="+isTouchEvent);
        if (deltaY<0&&isTouchEvent){ //是这种情况,我们来处理 ,剩下的交给系统
            int newH=ivHeader.getHeight()+Math.abs(deltaY);

            if (newH<=maxHight){//M没有拉出范围
                ivHeader.getLayoutParams().height=newH;
                requestLayout();
            }

        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }


    /**  5.1 当用户松手之后需要将图片回弹
     5.2 处理up事件*/
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//不要拦截我的事件
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                final ValueAnimator va=ValueAnimator.ofFloat(1);
                // 500ms内不断生成从0趋向于1的值

                final int tempheight = ivHeader.getHeight();
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int newsH = evaluate(va.getAnimatedFraction(), tempheight , originH);//需要从 拉动到的位置 tempheight, 恢复到 开始的位置 originH
                        ivHeader.getLayoutParams().height=newsH;
                        requestLayout();
                    }
                });
                    va.setInterpolator(new OvershootInterpolator(4)); //更有弹性
                va.setDuration(500);
                va.start(); //必须地start 才会开始
        }
        return super.onTouchEvent(ev);
    }

    /** 根据百分比，在最大值和最小值之间算出一个过渡值 */
    public int evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return (int) (startFloat + fraction * (endValue.floatValue() - startFloat));
    }
}