package com.lyz.mydome;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lyz.mydome.utils.UtilsToast;
import com.lyz.mydome.view.DragLayoutView;

public class MainDragLayout extends AppCompatActivity {

    private DragLayoutView domeDrag;

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
    }
}
