package com.lyz.basepagerstatefragment.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.fragment.bean.Cheeses;
import com.lyz.basepagerstatefragment.view.ParallaxView;

public class TomtoSettingDome extends Activity {

    @butterknife.InjectView(R.id.parallaxListView)
    ParallaxView parallaxListView;
    private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomto_setting_time);
        butterknife.ButterKnife.inject(this);

        View headerView = View.inflate(this, R.layout.item_header, null);
        iv_icon = (ImageView) headerView.findViewById(R.id.iv_icon);

        parallaxListView.addHeaderView(headerView);
        // 将要修改大小的ImageView告知给列表
        iv_icon.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {

                parallaxListView.setHeaderImage(iv_icon);

                iv_icon.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //Cheeses.NAMES 就是一个数组
        parallaxListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Cheeses.NAMES));

    }
}
