package com.lyz.basepagerstatefragment.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.adapter.MyAdapter;
import com.lyz.basepagerstatefragment.fragment.bean.Person;

import java.util.ArrayList;
import java.util.List;

public class DomeAty extends Activity {

    private LinearLayout table_title;
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dome_aty);
        table_title = (LinearLayout) findViewById(R.id.table_title);
        list_view = (ListView) findViewById(R.id.list_view);

        table_title.setBackgroundColor(Color.rgb(255, 100, 10));

        List<Person> list=new ArrayList<>();

        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));
        list.add(new Person("刘德华", "男", 50));

        MyAdapter adapter=new MyAdapter(this,list);


        list_view.setAdapter(adapter);
    }
}
