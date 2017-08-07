package com.ontime.prj1353;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ontime.prj1353.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProductActivity extends AppCompatActivity {
    //五个页面的列表
    private List<View> list;
    private MyViewPagerAdapter adapter;
    //包含指示小圆点的容器
    private LinearLayout llContainer;
    private ImageView[] dots;
    private ViewPager viewPager;

    private MyCount myCount;
    private TextView tvCountTime;

    private ImageView ivIphone;

    private SharedPreferences sp;
    private String BluetoothTagId;

    private Boolean flag = false;
    private EditText etBluetoothTagId;

    private Timer timer;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    flag = (Boolean) msg.obj;
                    if(flag){
                        tvCountTime.setVisibility(View.VISIBLE);
                    }
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏状态栏



        setContentView(R.layout.activity_product);

        //隐藏导航栏
       // View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);

//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        View decorView = getWindow().getDecorView();
int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
| View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
decorView.setSystemUiVisibility(option);
getWindow().setNavigationBarColor(Color.TRANSPARENT);
getWindow().setStatusBarColor(Color.TRANSPARENT);





        sp = getSharedPreferences("BluetoothTagIds",MODE_PRIVATE);
       // BluetoothTagId = sp.getString("BluetoothTagId","03003c4d");

        tvCountTime = (TextView) findViewById(R.id.tv_count_time);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        tvCountTime.setVisibility(View.GONE);
        list = new ArrayList<>();
        View firstView = LayoutInflater.from(this).inflate(R.layout.layout_first,null);
        View secondView = LayoutInflater.from(this).inflate(R.layout.layout_second,null);
        View thirdView = LayoutInflater.from(this).inflate(R.layout.layout_third,null);
        View forthView = LayoutInflater.from(this).inflate(R.layout.layout_forth,null);
        View fifthView = LayoutInflater.from(this).inflate(R.layout.layout_fifth,null);

        final View inflateView = LayoutInflater.from(this).inflate(R.layout.dialog_view,null);
        etBluetoothTagId = (EditText) inflateView.findViewById(R.id.etDeviceID);

        ivIphone = (ImageView) firstView.findViewById(R.id.iv_iphone);

       // ivIphone.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        ivIphone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                AlertDialog dialog = new AlertDialog.Builder(ProductActivity.this)
                        .setTitle("提示")
                        .setMessage("请输入需要绑定的蓝牙标签ID：")
                        .setView(inflateView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String bluetoothTagId = etBluetoothTagId.getText().toString();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("BluetoothTagId",bluetoothTagId);
                                editor.commit();
                                if(bluetoothTagId.equals("")){
                                    Toast.makeText(ProductActivity.this, "标签ID不可为空", Toast.LENGTH_SHORT).show();
                                    ViewGroup parent = (ViewGroup) inflateView.getParent();
                                    if(parent!=null){
                                        parent.removeView(inflateView);
                                    }
                                }else{
                                    Toast.makeText(ProductActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ProductActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                ViewGroup parent = (ViewGroup) inflateView.getParent();
                                if(parent!=null){
                                    parent.removeView(inflateView);
                                   // parent.removeView(inflateView);
                                }
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });

        list.add(firstView);
        list.add(secondView);
        list.add(thirdView);
        list.add(forthView);
        list.add(fifthView);
        adapter = new MyViewPagerAdapter(list);
        viewPager.setAdapter(adapter);

        dots = new ImageView[5];
        for(int i=0;i<dots.length;i++){
            dots[i] = (ImageView) llContainer.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setTag(i);
            dots[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem((Integer) v.getTag());
                }
            });
        }
        dots[0].setEnabled(false);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for(int i=0;i<dots.length;i++){
                    dots[i].setEnabled(true);
                }
                dots[position].setEnabled(false);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        myCount = new MyCount(20*1000,1000);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myCount.start();
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = true;
                handler.sendMessage(message);

            }
        },5000);
    }



    class MyCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvCountTime.setText(millisUntilFinished/1000+"");
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(ProductActivity.this, MainActivity.class);
            startActivity(intent);
            ProductActivity.this.finish();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();
decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            super.onWindowFocusChanged(hasFocus);
    }
}
