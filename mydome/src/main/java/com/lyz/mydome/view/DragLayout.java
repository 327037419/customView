package com.lyz.mydome.view;

import android.animation.FloatEvaluator;
import android.annotation.SuppressLint;
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
 * 创建日期 ：  on 2016/1/31 0031.
 * <p/>
 * 描 述 ：
 * <p/>
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class DragLayout extends FrameLayout {

    private ViewDragHelper dragHelper;
    private ViewGroup mLeftView;
    private ViewGroup mContentView;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private Status status= Status.CLOSED;

    //监听回调接口
    public enum  Status{
        CLOSED,OPENED,DRAGING;
    }

    public Status getStatus(){
        return status;
    }
    /** 三个状态对应3个方法 */
    public interface  OnDragStatusChangeListener{
        void OnClose();
        void OnOpened();
        void onDraging(float percent);
    }

    private OnDragStatusChangeListener  onDragStatusChangeListener;

    public void setOnDragStatusChangeListener(OnDragStatusChangeListener onDragStatusChangeListener) {
        this.onDragStatusChangeListener = onDragStatusChangeListener;
    }

    //    数学运算就是一个工具类 ,计算缩放的
    private FloatEvaluator evaluator = new FloatEvaluator();

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public DragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragLayout(Context context) {
        super(context);
        initView();
    }
    /**
     * 初始化view将要使用到的相关对象
     */
    private void initView() {
        dragHelper = ViewDragHelper.create(this, new MyCallback());//默认敏感度 1.0
    }
    /**
     * 拦截触摸事件,我们来处理 触摸监听 , 转交给 dragHelper 来处理;
     */
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {

        return dragHelper.shouldInterceptTouchEvent(event);
    }
    /**
     * 触摸事件交给dragHelper 来处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    // 获取两个子布局
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 健壮性检查
        if (getChildCount() != 2) {
            throw new RuntimeException("DragLayout只能有两个子布局");
        }
        if (!(getChildAt(0) instanceof ViewGroup || getChildAt(1) instanceof ViewGroup)) {
            throw new RuntimeException("DragLayout的子布局只能是ViewGroup");
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
        mWidth = w; //view 在xml 设置的宽度
        mHeight = h;
        /** //就是屏幕宽度的 60%  是侧边栏
         * mRange 侧边栏要显示的区域
         * */
        mRange = (int) (mWidth * 0.6);
    }

    private final class MyCallback extends ViewDragHelper.Callback {
        @Override
        /** 返回child在水平方向上的拖拽可移动范围 */
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }
        @Override
        /** 返回true则说明child可以被拖拽 */
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
            //			logE("DragLayout.onViewPositionChanged.");
            // 当拖拽侧拉菜单时保持不动，转而偏移主内容面板
            if (child == mContentView) {
                left = fixContentLeft(left);
            }
            return left;
        }
        /**
         * clampViewPositionHorizontal 是在界面更新 之前调用的 ,  返回的left 是要更新的位置的, 用于更新的 ,
         * onViewPositionChanged 是在界面更新之后调用的; 返回的left位置已经定下来了,我们再来改变其他view的位置, 联动!
         */
        @Override
        /** 当被拖拽的view移动位置后，会调用此方法。可以用于处理View之间的联动
         *  当view位置改变时被回调，可以执行：伴随动画，更新某些view，等
         *  changedView 被拖到的view
         *
         * */
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
//			logE("DragLayout.onViewPositionChanged.");
            // 当拖拽侧拉菜单时保持不动，转而偏移主内容面板
            if (changedView == mLeftView) {
                mLeftView.layout(0, 0, mWidth, mHeight); //移动 侧边栏, 让他 保持不动 然后把dx移动的距离给 主内容面板加上
//                dx 是 侧边栏的移动的偏移量 ,
                int newLeft = dx + mContentView.getLeft(); ///主内容加上偏移量 ,
                newLeft = fixContentLeft(newLeft);//W:纠正 非法操作
                mContentView.layout(newLeft, 0, newLeft + mWidth, mHeight);

            }
            // 根据拖拽位置处理view间的联动
            processDragEvent();
            // 在2.3版本上修正位置后并不会主动刷新界面，需要强制处理 , 更新View
            invalidate();
        }

        @Override
        /** 当被拖拽的view被释放的时候会回调此方法
         *
         * 当手抬起来时回调 , 代表了 移动 加速度方向,如果没有滑动    W:原地停住的就是0, 如果是 去左边带滑动 负的, 右边是正的
         * xvel 用户放手时，被拖拽的view在x方向的移动速度,像素/秒,向右移动为+
         yvel 用户放手时，被拖拽的view在x方向的移动速度,像素/秒，向下移动时为+
         * */
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            			Log.e("DragLayout.onViewReleased.xvel="+xvel);
            // 根据用户放手时的位置，自动打开或关闭面板
            // 向右移动
            boolean isRightMove = xvel > 0;
            // 静止，但是面板已打开超过一半
            boolean isOpenHalfMore = (xvel == 0 && mContentView.getLeft() > mRange / 2);
            //在松开手的时候, 是 在 打开 内容栏, 并且打开一半了
            if (isRightMove || isOpenHalfMore) {
                open();
            } else {
                close();
            }

        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        //通过不断重绘来实现滑动
    }
    /**
     * 打开面板
     */
    private void open() {
        int left = mRange; //
//        mContentView.layout(left,0,left+mWidth,mHeight); 不平滑
        if (dragHelper.smoothSlideViewTo(mContentView, left, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    public void close() {
/** mWidth 代表了尺寸代表了后的大小(剩多少拿多少宽度),  整个内容页的宽度  */
        int left = 0;
//        mContentView.layout(left,0,left+mWidth,mHeight);
        if (dragHelper.smoothSlideViewTo(mContentView, left, 0)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 根据当前主面板的位置，协调整个界面的联动
     */
    private void processDragEvent() {
        // 计算面板展开的百分比
        float percent = mContentView.getLeft() / (float) mRange;

        /** 面板展开的百分比,展开的动画 */
        animChange(percent);

        /** 根据当前 面板 拉伸的 位置 设置状态 */
         status = updateChangeStatus();


        if (onDragStatusChangeListener!=null){ //调用者 传递了 接口对象
            //那么肯定是移动了,
            onDragStatusChangeListener.onDraging(percent);

            if (status== Status.OPENED){
                onDragStatusChangeListener.OnOpened();
            }else if (status== Status.CLOSED){
                onDragStatusChangeListener.OnClose();
            }


        }

    }

    private Status updateChangeStatus() {
        if (mContentView.getLeft()==0){
            return Status.CLOSED;
        }

        if (mContentView.getLeft()==mRange){
          return  Status.OPENED;
        }

        return Status.DRAGING;

    }

    private void animChange(float percent) {
    /*     float maxContentScale = 1.0f;
         float minContentScale = 0.8f;//最多缩放到0.8
 //        当前我已经变化的量
         float offsetContentScale= (maxContentScale-minContentScale)*porcent;
 //         本来是 你拉动后就会缩放,所以是在 本来的样子上减去
         float finalContentScale = maxContentScale-offsetContentScale;

 //            mContentView.setScaleX(finalContentScale); 用这个为什么只有在送手的一刹那才会缩放
 //            mContentView.setScaleY(finalContentScale);

         ViewHelper.setScaleX(mContentView,finalContentScale);  麻烦 , 有简单的方法
         ViewHelper.setScaleY(mContentView,finalContentScale);
 */

        ViewHelper.setScaleX(mContentView, evaluate(percent, 1.0, 0.8));
        ViewHelper.setScaleY(mContentView, evaluate(percent, 1.0, 0.8));


        // 根据面板展开的百分比，平移侧滑菜单
        float translationX = evaluate(percent, -mRange, 0);
        ViewHelper.setTranslationX(mLeftView, translationX);

        // 根据面板展开的百分比，缩放侧滑菜单
        float scaleLeft = evaluate(percent, 0.5f, 1.0f);
        ViewHelper.setScaleX(mLeftView, scaleLeft);
        ViewHelper.setScaleY(mLeftView, scaleLeft);

        // 根据面板展开的百分比，修改侧滑菜单透明度
        float alphaLeft = evaluate(percent, 0.5, 1.0);
        ViewHelper.setAlpha(mLeftView, alphaLeft);

        // 根据面板展开的百分比，修改背景图的颜色
        int color = (Integer) evaluateColor(percent, Color.BLACK, Color.TRANSPARENT);
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


    /**
     * 修正正文的左侧位置只能在 [0,mRange]区间内
     */
    private int fixContentLeft(int left) {
        if (left <0) {
            left = 0;
        }

        if (left > mRange) {//mRange 侧边栏要显示的区域
            left = mRange;
        }

        return left;
    }

    /**
     * 根据百分比，在最大值和最小值之间算出一个过渡值
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
