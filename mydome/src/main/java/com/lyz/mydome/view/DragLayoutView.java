package com.lyz.mydome.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lyz.mydome.utils.Logs;
import com.nineoldandroids.view.ViewHelper;

/**
 * ============================================================
 * <p/>
 * 版 权 ： 刘宇哲 版权所有 (c) 2015
 * <p/>
 * 作 者 : 刘宇哲
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ：  on 2016/2/11 0011.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class DragLayoutView extends FrameLayout {

    private ViewDragHelper dragHelper;
    private ViewGroup mLeftView;
    private ViewGroup mContentView;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private Status status = Status.CLOSED;

    private enum Status {
        CLOSED, OPENED, DRAGING;
    }

    //            获得当前状态的方法
    private Status getStatus() {
        return status;
    }

    public interface OnDragStatusChangeListener {
        void onClosed();

        void onOpened();

        void onDraging(float percent);
    }

    private OnDragStatusChangeListener dragStatusChangeListener;

    public void setDragStatusChangeListener(OnDragStatusChangeListener dragStatusChangeListener) {
        this.dragStatusChangeListener = dragStatusChangeListener;
    }

    public DragLayoutView(Context context) {
        super(context);
        initView();
    }

    public DragLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {

        dragHelper = ViewDragHelper.create(this, new MyCallBack());

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException("DragLayout 必须是2个子布局");
        }

        if (!(getChildAt(0) instanceof ViewGroup) || !(getChildAt(1) instanceof ViewGroup)) {
            throw new RuntimeException("DragLayout 必须是2个子布局, 并且 子布局必须是 viewgoup");
        }

        mLeftView = (ViewGroup) getChildAt(0);
        mContentView = (ViewGroup) getChildAt(1);

    }

    /**
     * 获取view 的大小
     */

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;

        mHeight = h;
        /** //就是屏幕宽度的 60%  是侧边栏
         * mRange 侧边栏要显示的区域
         * */
        mRange = (int) (mWidth * 0.6);
    }


    class MyCallBack extends ViewDragHelper.Callback {


        //        9.2.1 返回横向的拖拽范围，不限制真正的左右范围。可以用于确定动画执行的时长，同
//        时返回值大于0才表示可以横向滑动。
        @Override
        public int getViewHorizontalDragRange(View child) {
            Logs.e("child:=" + child);
            return mRange;//不重写这个会 照成 listView不能拉出侧边栏
        }

        //        子类都可以滑动
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }


        /**
         * 返回当child被拖拽时移动到的水平位置<br>
         * left 系统推荐移动到的位置 ,
         * dx 从上一次移动到新位置的偏移量
         * left: 左方向的移动距离 ,拖拽mContentView 主内容给他放到什么地方
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Logs.e("clampViewPositionHorizontal;left:=" + left + ";+dx:=" + dx);
            if (child == mContentView) {
                left = fixContentLeft(left);
            }
            return left;
        }

        //        9.5.1 当view位置改变时被回调，可以执行：伴随动画，更新某些view，等
//        9.5.3 changedView 被拖拽的view
//        9.5.2 left ，top 最新使用的左侧或顶部的位置
//        9.5.3 dx , dy 最新的位置变化量

        //        onViewPositionChanged 类型move
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

            Logs.e("onViewPositionChanged;left:=" + left + ";+dx:=" + dx + ";top:=" + top + ";dy:=" + dy);
            // 当拖拽侧拉菜单时保持不动，转而偏移主内容面板
            if (changedView == mLeftView) {
                //移动 侧边栏, 让他 保持不动 然后把dx移动的距离给 主内容面板加上
                mLeftView.layout(0, 0, mWidth, mHeight);//侧边栏和 主内容一边大的

//                dx 是 侧边栏的移动的偏移量 ,
                ///主内容加上偏移量 ,
                int newLeft = dx + mContentView.getLeft();
                newLeft = fixContentLeft(newLeft);//非法纠正
                mContentView.layout(newLeft, 0, newLeft + mWidth, mHeight);
                // 根据拖拽位置处理view间的联动
            }


            /** 根据当前主面板的位置，协调整个界面的联动 */

            processDragEvent();


            // 在2.3版本上修正位置后并不会主动刷新界面，需要强制处理 , 更新View
            invalidate();
        }

        private void processDragEvent() {
            // 计算面板展开的百分比, 拉动的范围除以 总长度

            float percent = mContentView.getLeft() / (float) mRange;
            // 处理监听回调
            status = updateStatus();//判断3中状态  ,
            /** 面板展开的百分比,展开的动画 */
            animChange(percent);


            if (dragStatusChangeListener != null) {

                dragStatusChangeListener.onDraging(percent);

                if (status == Status.OPENED) {
                    dragStatusChangeListener.onOpened();
                } else if (status == Status.CLOSED) {
                    dragStatusChangeListener.onClosed();
                }


            }


        }

        /**
         * 当被拖拽的view被释放的时候会回调此方法  onViewReleased类型 up
         * <p/>
         * 当手抬起来时回调 , 代表了 移动 加速度方向,如果没有滑动
         * W:原地停住的就是0, 如果是 去左边带滑动 负的, 右边是正的
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Logs.e("--------------------");
            // 根据用户放手时的位置，自动打开或关闭面板
            // 向右移动
            boolean isMoveRight = xvel > 0;
            //在松开手的时候 ,静止状态,  1 是 在 打开 内容栏, 2 并且打开一半了
            boolean isOpenHalf = xvel == 0 && mContentView.getLeft() > mRange / 2;

            if (isOpenHalf || isMoveRight) {
                open();
            } else {
                close();
            }

        }
    }

    private void animChange(float percent) {
//        主内容上下 整体 缩放从 0.8
        ViewHelper.setScaleX(mContentView,evaluate(percent,1.0,0.8));
        ViewHelper.setScaleY(mContentView,evaluate(percent,1.0,0.8));

        // 根据面板展开的百分比，平移侧滑菜单
//        16.3.2 左面板  a 缩放、b 平移、c 透明度动画
//        // 缩放
       float scaleLeft= evaluate(percent,0.5f,1.0f);
        ViewHelper.setScaleX(mLeftView,scaleLeft);
        ViewHelper.setScaleY(mLeftView,scaleLeft);

        //平移
        float translationX= evaluate(percent,-mRange,0);
        ViewHelper.setTranslationX(mLeftView,translationX);

        // 根据面板展开的百分比，修改侧滑菜单透明度
        float alphaLeft=evaluate(percent,0.5f,1.0);
        ViewHelper.setAlpha(mLeftView,alphaLeft);

        // 根据面板展开的百分比，修改背景图的颜色

        // 根据面板展开的百分比，修改背景图的颜色
        int color = (Integer) evaluateColor(percent, Color.BLACK, Color.TRANSPARENT);
        Logs.e("-getBackground:"+getBackground());
        Logs.e("-  PorterDuff.Mode.SRC_OVER:"+  PorterDuff.Mode.SRC_OVER);
        Logs.e("-color:"+color);
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_OVER);


    }
    /** 根据百分比，在起始颜色和最终颜色之间计算出一个过渡颜色 */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }
    /** 根据百分比，在最大值和最小值之间算出一个过渡值 */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
    /**
     * 根据当前面板的 在移动过程中改变的位置 来回调
     * 位置，计算展开状态
     * <p/>
     * 用枚举 记住了3  中状态 ,  我们就可以用枚举的 3中状态 , 分别回调不同的接口了
     */
    private Status updateStatus() {
        if (mContentView.getLeft() == 0) {
            return Status.CLOSED;// 在界面完全 看不见侧边栏时, 才是关闭
        } else if (mContentView.getLeft() == mRange) {
            return Status.OPENED;
        }
        return Status.DRAGING;
    }

    private void open() {
        int left = mRange;

        if (dragHelper.smoothSlideViewTo(mContentView, left, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void close() {
        int left = 0;
        if (dragHelper.smoothSlideViewTo(mContentView, left, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {

        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    /**
     * 修正正文的左侧位置只能在 [0,mRange]区间内
     */
    private int fixContentLeft(int left) {


        if (left < 0) {
            left = 0;
        }

        if (left > mRange) {
            left = mRange;
        }

        return left;
    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return dragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }


}
