package com.lyz.basepagerstatefragment.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ListView;

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
 * 创建日期 ：  on 2016/2/4 0004.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class ParallaxView extends ListView {

    private ImageView iv_icon;
    private int intrinsicHeight;
    private int maxH;
    private int originH;

    public ParallaxView(Context context) {
        super(context);
    }

    public ParallaxView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 把 头布局的  对象  传递 过来
     */
    public void setHeaderImage(ImageView iv_icon) {
        this.iv_icon = iv_icon;
        maxH = (int) (iv_icon.getDrawable().getIntrinsicHeight() * 0.7);

        originH = iv_icon.getHeight();

    }


    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        Logs.e("ParallaxView.overScrollBy.deltaY=" + deltaY + ";scrollY=" + scrollY
                + ";isTouchEvent=" + isTouchEvent);
        if (deltaY < 0) {
//            说明在往上拉  , 拉多少 就变多少
            /** iv_icon.getHeight() 原先的高度 */
            int newHeight = iv_icon.getHeight() + Math.abs(deltaY);
            if (newHeight <= maxH) {
                iv_icon.getLayoutParams().height = newHeight;
                iv_icon.requestLayout();
            }
        }


        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {

            final int tmpH = iv_icon.getHeight();

            ValueAnimator valueAnimator = ValueAnimator.ofInt(1);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    //百分比
                    float percent = animator.getAnimatedFraction();
                    int newH = evaluate(percent, tmpH, originH);

                    iv_icon.getLayoutParams().height=newH;/** 少了这句 就会导致没有回弹效果 */

                    iv_icon.requestLayout();
                }
            });
            // 500ms内不断生成从0趋向于1的值
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 根据百分比，在最大值和最小值之间算出一个过渡值
     */
    public int evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return (int) (startFloat + fraction * (endValue.floatValue() - startFloat));
    }

}
