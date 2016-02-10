package com.lyz.basepagerstatefragment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.CircleProgress;
import com.lyz.basepagerstatefragment.R;
import com.lyz.basepagerstatefragment.widget.Constant;
import com.lyz.basepagerstatefragment.widget.Logs;
import com.lyz.basepagerstatefragment.widget.UtilsMpref;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 滑动 , 开关用  自定义的 view的 开关,进度 可以手动置为 0,或是100
 */
public class TomatoWork extends Activity {

    @InjectView(R.id.circle_progress)
    CircleProgress circleProgress;
    @InjectView(R.id.iv_rest)
    ImageView ivRest;
    @InjectView(R.id.tv_rest)
    TextView tvRest;
    @InjectView(R.id.fl_progress)
    FrameLayout flProgress;
    @InjectView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @InjectView(R.id.tv_restart)
    TextView tvRestart;
    @InjectView(R.id.rest_progressBar)
    ProgressBar pb_rest;
    private CountDownTimer countdownTimer;
    private static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);  为什么不可以
        setContentView(R.layout.activity_tomato_work);
        ButterKnife.inject(this);
//        updateTime();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logs.e("我走了-onDestroy");
        /** 取消子线程的 计时 */
        countdownTimer.cancel();
//        countdownTimer.onFinish();
    }

    /**
     * 单位怎么转换, 25分钟倒计时,  跟进度条的 更新
     */
    private void initData() {
        int number = 0;
        if (isRest) {
//            就是5分钟 ,
            number = 500; //测试给100, 正式给500 就是5分钟
        } else {
//            计时25分钟
            number = 1500;
        }
/** 19 变 1500就是25分钟了 然后界面给换换在结束的时候  */
        final int finalNumber = number; //局部变量访问不到
        Logs.e("finalNumber=" + finalNumber);
        countdownTimer = new CountDownTimer((finalNumber) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (isRest) {

                    Logs.e(" 更新 休息的 进度条");

                    updateTimeRest(millisUntilFinished, finalNumber);
                } else {

                    Logs.e(" 更新 中间 25分钟的进度条");
                    updateTime(millisUntilFinished, finalNumber);
                }
            }

            @Override
            public void onFinish() {
                /*1 界面更新, 提示 休息,
                    2  把次数给保存到首页的
                今天 番茄数量和总计;
//                弹出tost
                3  25分钟后 需要 铃声响起;,和震动,切换图片,  切换提示字体
                4  点击后 5分钟 倒计时, 倒计时完事, 切换图片 , 是否 开启下一个番茄;
                */
//          刷新界面
                if (isRest) {
                    updateRestOnFinish();
                } else {

                    disposeOnFinish();
                }
//          震动
                /** 默认是开启状态,没有在设置里,点击是走默认状态 */
                boolean is_vibrator = UtilsMpref.getBoolean(TomatoWork.this, Constant.IS_VIBRATOR,true);
                if (is_vibrator) {
                    myVibrator();
                }
//          播放铃声
                PlayRingTone(TomatoWork.this, RingtoneManager.TYPE_RINGTONE);
            }
        }.start();
    }

    /**
     * 休息的界面
     */
    private void updateRestOnFinish() {
        isRest = false;//标记 重置 初始化状态

        pb_rest.setVisibility(View.GONE);
        tvRestart.setVisibility(View.VISIBLE);
        tvRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mediaPlayer.stop();
                Logs.e("  这里我们点击 给开启下一个番茄-------");
                tvCurrentTime.setVisibility(View.VISIBLE);
                circleProgress.setVisibility(View.VISIBLE);
                ivRest.setVisibility(View.GONE);
                tvRest.setVisibility(View.GONE);
                pb_rest.setVisibility(View.GONE);
                tvRestart.setVisibility(View.GONE);
                initData();

            }
        });


    }

    /**
     * 获取的是铃声的Uri
     *
     * @param ctx
     * @param type
     * @return
     */
    public static Uri getDefaultRingtoneUri(Context ctx, int type) {
        return RingtoneManager.getActualDefaultRingtoneUri(ctx, type);
    }

    /**
     * 播放铃声
     *
     * @param ctx
     * @param type
     */
    public static void PlayRingTone(Context ctx, int type) {
        mediaPlayer = MediaPlayer.create(ctx,
                getDefaultRingtoneUri(ctx, type));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    /**
     * 25分钟后,我们来刷新界面
     */
    int tomatonumber;
    int saveTomatonumber;

    private void disposeOnFinish() {
//        tomatonumber++;
//        /**
//         *  每次进来先取出保存的 次数,然后累计,
//         * 没进来一次就加一个 , 加一次就存一次 */
//        if (saveTomatonumber == 0) {
//            saveTomatonumber = UtilsMpref.getInt(TomatoWork.this, Constant.TOMATO_DAY_NUMBER, 0);
//        }
//        tomatonumber = saveTomatonumber + tomatonumber;
//
//        UtilsMpref.putInt(TomatoWork.this, Constant.TOMATO_DAY_NUMBER, tomatonumber);
//        Logs.e("------------------");

        tvCurrentTime.setVisibility(View.GONE);
        circleProgress.setVisibility(View.GONE);
        ivRest.setVisibility(View.VISIBLE);
        tvRest.setVisibility(View.VISIBLE);

//        字体 放大缩小
        TvScaleAnimation(tvRest);

        flProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //字体的放大与缩小,
                // 点击后立马就开启 , 5分钟休息倒计时,
                Logs.e("我点击 了 中间的   // 点击后立马就开启 , 5分钟休息倒计时,");
                mediaPlayer.stop();
                /** 启动休息界面 */
                startRest();
            }
        });
    }


    boolean isRest;

    /**
     * 开启休息, 启动了一次休息界面,就累加一次
     */
    private void startRest() {
        tomatonumber++;
        /**
         *  每次进来先取出保存的 次数,然后累计,
         * 没进来一次就加一个 , 加一次就存一次 */
        if (saveTomatonumber == 0) {
            saveTomatonumber = UtilsMpref.getInt(TomatoWork.this, Constant.TOMATO_DAY_NUMBER, 0);
        }
        tomatonumber = saveTomatonumber + tomatonumber;

        UtilsMpref.putInt(TomatoWork.this, Constant.TOMATO_DAY_NUMBER, tomatonumber);
        Logs.e("------------------");


        isRest = true;

        tvCurrentTime.setVisibility(View.VISIBLE);
        circleProgress.setVisibility(View.GONE);
        ivRest.setVisibility(View.GONE);
        tvRest.setVisibility(View.GONE);
        pb_rest.setVisibility(View.VISIBLE);
        tvRestart.setVisibility(View.GONE);
        initData();
    }

    private void TvScaleAnimation(TextView tvRest) {
        ScaleAnimation sa = new ScaleAnimation(
                0, 2,
                0, 2,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        sa.setDuration(500);
        tvRest.startAnimation(sa);
    }

    /**
     * 监听 返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mediaPlayer != null)
            mediaPlayer.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(TomatoWork.this);
        builder.setTitle("是否要退出番茄");
        builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("继续番茄", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 震动 : 是 系统 的 服务 ，系统的功能
     */
    private void myVibrator() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        long[] pattern = {1000, 1000, 1000, 1000};// 休息1秒震动一秒.....
        vibrator.vibrate(pattern, -1);// -1重复一次
    }

    /**
     * 时间的处理
     * 怎么把1500秒 变成分钟的界面表现形式??/
     */
    private void updateTime(final long millisUntilFinished, int finalNumber) {
        circleProgress.setProgress((int) ((millisUntilFinished / 1000) * 100 / (finalNumber) + 0.5));
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm分ss秒");
        final String format = dateFormat.format(new Date(millisUntilFinished));
//        Logs.e("format=" + format + "millisUntilFinished=" + millisUntilFinished / 1000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCurrentTime.setText(format);
            }
        });
    }

    private void updateTimeRest(long millisUntilFinished, int finalNumber) {
        Logs.e("finalNumber=" + finalNumber);
        pb_rest.setProgress((int) ((millisUntilFinished / 1000) * 100 / (finalNumber) + 0.5));
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm分ss秒");
        final String format = dateFormat.format(new Date(millisUntilFinished));
//        Logs.e("format=" + format + "millisUntilFinished=" + millisUntilFinished / 1000);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvCurrentTime.setText(format);
            }
        });
    }


}
