package com.lyz.basepagerstatefragment.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.lyz.basepagerstatefragment.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TomtoSettingMusic extends Activity {

    @InjectView(R.id.tv_text)
    TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomto_music);
        ButterKnife.inject(this);
//        startActivity(new Intent(this,DomeAty.class)); listView 实现 GridView table 效果



        // 打开系统铃声设置框
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        // 设置类型为来电
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        // 设置显示的标题
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "1");
        startActivityForResult(intent, RingtoneManager.TYPE_RINGTONE);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            // 得到我们选择的铃声
            // Uri uri = MediaStore.Audio.Media.getContentUriForPath(/sdcard/yoyomusic/后来.mp3);
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                switch (requestCode) {
                    case RingtoneManager.TYPE_RINGTONE:
                        System.out.println("---------------------");
                        RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
