package com.lyz.mydome;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyz.mydome.adapter.MyBaseAdapter;
import com.lyz.mydome.quickmain.Man;
import com.lyz.mydome.utils.Cheeses;
import com.lyz.mydome.utils.Logs;
import com.lyz.mydome.utils.UtilsToast;
import com.lyz.mydome.view.DragLayout;
import com.lyz.mydome.view.ParallaxView;
import com.lyz.mydome.view.QuickIndexView;

/**
 * 时差特效 listview
 * 处理步骤：
 a. 设置HeaderView  ,把自定义listView 添加一个头,测量后把 头对象 传递给他
 b. 当向下拉动ListVie的时候修改HeaderView高度  overScrollBy 拿到移动的高度 加上原本的高度 , 从新赋值, 刷新界面
 c. 当up事件时，将HeaderView高度还原 , valueAnimal 在触摸后 回弹
-------------------
    快速首字母  view
 处理步骤：
 a.绘制所有字母
 b.处理触摸事件
 c.提供字符变更的回调接口

 */
public class MainActivity extends AppCompatActivity {

    private ParallaxView parallaxlistView;
    private QuickIndexView quick_letter;
    String[] NAMES=Cheeses.NAMES;
    private TextView tv_center_letter;
    private Handler handler;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DragLayout dragLayout = (DragLayout) findViewById(R.id.draglayout);
        dragLayout.setOnDragStatusChangeListener(new DragLayout.OnDragStatusChangeListener() {
            @Override
            public void OnClose() {
//                ObjectAnimator obj = ObjectAnimator.ofFloat(iv_header, "translationX", 0, 3f);
//                obj.setDuration(500);
//                obj.setInterpolator(new CycleInterpolator(6));
//                obj.start();
                UtilsToast.showToast(getApplicationContext(), "OnClose");
            }

            @Override
            public void OnOpened() {
                UtilsToast.showToast(getApplicationContext(), "OnOpened");
            }

            @Override
            public void onDraging(float percent) {
                UtilsToast.showToast(getApplicationContext(), "onDraging,percent=" + percent);
//                ViewHelper.setAlpha(iv_header, 1 - percent);//让他上来时 1- 0.0001, 上来就是1, 越拉越没了,
            }
        });




        handler = new Handler();

        parallaxlistView = (ParallaxView) findViewById(R.id.list_View);
        View headerView = View.inflate(this, R.layout.item_header, null);
        parallaxlistView.addHeaderView(headerView);
        setHeaderView(headerView);


        quick_letter = (QuickIndexView) findViewById(R.id.quick_letter);
        tv_center_letter = (TextView) findViewById(R.id.tv_center_letter);


        quick_letter.setQuickletterlistener(new QuickIndexView.OnQuickLetterListener() {
//            5.4 当滑动的时候显示字母弹窗
//            5.4.1 在界面里增加一个在屏幕中间的TextView
//            5.4.2 当监听被回调的时候显示TextView
//            5.4.3 显示的时候发送延迟消息，两秒后隐藏TextView
//            5.4.4 发送消息之前，要移除掉上一次发送的延迟消息
            @Override
            public void LetterChange(String letter) {
//                UtilsToast.showToast(MainActivity.this,"选中的字体是:"+letter);
                for (int i = 1; i < parallaxlistView.getCount(); i++) {//i 从1 开始 , 因为0 是 头布局 图片
                     Man man = (Man) parallaxlistView.getItemAtPosition(i);
                    String manLetter = String.valueOf(man.getLetter());

                    if (TextUtils.equals(manLetter,letter)){
                        parallaxlistView.setSelection(i);  //一个 0 , 就是一个 item 就是一个 字母 加 字体,默认有一个头图片是0索引
                        break;//接收循环
                    }
                }



                handler.removeCallbacksAndMessages(null);
                tv_center_letter.setText(letter);//字体显示 文本
                tv_center_letter.setVisibility(View.VISIBLE); //回调这个接口就显示出来
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Logs.e("-我2秒后延迟执行的------------");

                        tv_center_letter.setVisibility(View.GONE);
                    }
                }, 1000);//1秒钟后隐藏


            }
        });

        parallaxlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logs.e("我点击的索引是:="+position);
            }
        });

    }




    private void setHeaderView(View headerView) {
//        // 将要修改大小的ImageView 对象, 传递给自定义listView
//        headerView.measure(0,0);
        final ImageView ivHeader = (ImageView) headerView.findViewById(R.id.iv_header);

        ivHeader.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parallaxlistView.setHeaderImage(ivHeader);
                ivHeader.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

//      parallaxlistView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Cheeses.NAMES));
        MyBaseAdapter adapter = new MyBaseAdapter();
        adapter.setLetterList(NAMES);//把数据传递过去
        parallaxlistView.setAdapter(adapter);

    }
}
