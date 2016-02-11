package com.lyz.mydome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lyz.mydome.utils.Cheeses;
import com.lyz.mydome.utils.UtilsToast;
import com.lyz.mydome.view.DragLayoutView;
import com.lyz.mydome.view.LinearLayoutDome;

public class MainDragLayout extends AppCompatActivity {

    private DragLayoutView domeDrag;
    private ListView list_view_dome;
    private LinearLayoutDome layout_content_dome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drag_layout);
        domeDrag = (DragLayoutView) findViewById(R.id.domeDrag);


        domeDrag.setDragStatusChangeListener(new DragLayoutView.OnDragStatusChangeListener() {
            @Override
            public void onClosed() {

                UtilsToast.showToast(getApplicationContext(), "OnClose");
            }

            @Override
            public void onOpened() {
                UtilsToast.showToast(getApplicationContext(), "OnOpened");
            }

            @Override
            public void onDraging(float percent) {
                UtilsToast.showToast(getApplicationContext(), "onDraging,percent=" + percent);
            }

        });
        layout_content_dome = (LinearLayoutDome) findViewById(R.id.layout_content_dome);

        layout_content_dome.getDragLayout(domeDrag);
        list_view_dome = (ListView) findViewById(R.id.list_view_dome);

        list_view_dome.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));


    }


}
