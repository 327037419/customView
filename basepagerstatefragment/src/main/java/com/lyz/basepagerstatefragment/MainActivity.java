package com.lyz.basepagerstatefragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.lyz.basepagerstatefragment.fragment.TweetFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fl_content,new TweetFragment()).commit();
    }
}
