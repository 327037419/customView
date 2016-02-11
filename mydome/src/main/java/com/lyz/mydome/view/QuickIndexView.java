package com.lyz.mydome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyz.mydome.utils.Cheeses;

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
public class QuickIndexView extends View {





    private Paint paint;
    private int mWidth;
    private int mHight;
    private int index = -1;

    public interface OnQuickLetterListener {
        void LetterChange(String  letter);
    }


    private OnQuickLetterListener quickletterlistener;

    public void setQuickletterlistener(OnQuickLetterListener quickletterlistener) {
        this.quickletterlistener = quickletterlistener;
    }

    public QuickIndexView(Context context) {
        super(context);
        initView();
    }

    public QuickIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public QuickIndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setStrokeWidth(4);

        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;

//        view 在xml里的高度除以26
        mHight = h / Cheeses.LETTERS.length;//计算每一个单元格高度

    }

    /**
     * 我把 这个每个字母都 转给平分位置, 然后
     * <p/>
     * 把这个 长条(也就是本类View) 平分给 26个字母,
     * 然后把每一个字母都在一个矩形框里 居中就可以实现了
     * -------------------------
     * measureTextWidth：用来测量普通文字的宽度，调用native层去测量。
     * getTextBounds：会按严格按照Paint的样式，绘制出文字的边界（设置倾斜后，绘制的文字被截断，但仍然能得到倾斜后的实际宽度），同样也是native层去测量。
     * Layout.desiredWidth：能测量包含Spanned的文字宽度，实际是调用TextLine的measure()计算。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        绘制字体
//        Logs.e("Cheeses.LETTERS.length:" + Cheeses.LETTERS.length);


        for (int i = 0; i < Cheeses.LETTERS.length; i++) {
            if (i == index) {
                paint.setColor(Color.BLACK);
//                Logs.e("i == index+++++++");
            } else {
//                Logs.e("-------------我是-1---");
                paint.setColor(Color.WHITE);
            }

            String text = String.valueOf(Cheeses.LETTERS[i]);
            // 获取文本的宽高
            Rect rect = new Rect();
            paint.getTextBounds(text, 0, 1, rect);
            /**每一个 单元各的高度 加上 字母的高度 ,然后 除以2  , 再加上 单元格的位置  是乘以 * i  不是加 */
            // 计算每一个字母的 在不同单元格 , 居中 的 x,y的 尺寸
            float drawX = (mWidth - rect.width()) / 2;
            long drawY = (mHight + rect.height()) / 2 + (i * mHight);
            canvas.drawText(text, drawX, drawY, paint);
        }


    }

    /**
     * 3. 处理触摸事件，辨别被按到的字母
     * <p/>
     * 非法处理,超出范围不处理,
     * 高亮处理,绘制时使用灰色，否则使用白色
     * <p/>
     * 回调接口 , 弹toast
     * 判断点击的位置
     * 3.3 up事件的时候将preIndex恢复成 -1，供下一次触摸down事件的时候比较使用
     * <p/>
     * <p/>
     * 如果前一个字体 等于当前字体 不触发回调!!
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//不要拦截我的事件
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);//不要拦截我的事件
                int preIndex = index;

                float y = event.getY();
//                Logs.e("y:=" + y + ";mHight=" + mHight);
//                根据手指位置判断当前被按住的字母在数组里的位置
                index = (int) (y / mHight);
//                Logs.e("index=" + index);

//                越界问题
                if (index < 0) {
                    index = 0;
                }
                if (index > Cheeses.LETTERS.length - 1) {
                    index = Cheeses.LETTERS.length - 1;
                }
                //如果上一个 坐标 == 这次的 选择的坐标 就不走 回调接口;

                if (preIndex == index) {
                    break;//如果前一个索引 等于当前索引 ,  不触摸回调
                }

                char letter = Cheeses.LETTERS[index];
                if (quickletterlistener != null) {
                    quickletterlistener.LetterChange(String.valueOf(letter));
                }


                break;
            case MotionEvent.ACTION_UP:
                index = -1;
                break;
        }
        invalidate();
        return true;
    }
}
