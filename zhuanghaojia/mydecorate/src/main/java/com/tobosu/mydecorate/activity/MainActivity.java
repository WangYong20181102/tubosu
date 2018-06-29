package com.tobosu.mydecorate.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.fragment.DecorateArticleListFragment;
import com.tobosu.mydecorate.fragment.NewHomeFragment;
import com.tobosu.mydecorate.fragment.NewMineFragment;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.WarnDialog;
import com.umeng.analytics.MobclickAgent;



public class MainActivity extends BaseActivity {
    private static String TAG = MainActivity.class.getSimpleName();

    private Context context;
    private FrameLayout main_container;
    private FragmentTransaction transaction;
    private FragmentManager fManager;
    private Fragment fragment = null;
    private MainBroadcastReceiver mainReceiver = null;

    private Button rb_home;
    private Button rb_bible;
    private Button rb_mine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;



        initView();
        initReceiver();
        initFragments(0); // 解决从popactivity过来时为空白
        initChannelIntent();
    }


    private void initView() {
        System.out.println("------------------------你来到 mainactivity 页面---------------------");

        main_container = (FrameLayout) findViewById(R.id.main_container);
        rb_home = (Button) findViewById(R.id.rb_home);
        rb_bible = (Button) findViewById(R.id.rb_bible);
        rb_mine = (Button) findViewById(R.id.rb_mine);


        //必须要
        if (getSharedPreferences("HomeFragmentPositionSetting", Context.MODE_PRIVATE).getInt("start", -1) == 0) {
            initFragments(0);
            getSharedPreferences("HomeFragmentPositionSetting", Context.MODE_PRIVATE).edit().putInt("start", 1).commit();
        }

        rb_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(0);
            }
        });
        rb_bible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(1);
            }
        });
        rb_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFragments(2);
            }
        });
    }

    private void initReceiver() {
        mainReceiver = new MainBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.LOGOUT_ACTION_ACTION);
        filter.addAction(Constant.WEIXIN_LOGIN_ACTION);
        filter.addAction(Constant.REFRESH_MINEFRAGMENT_DATA_ACITION);
        filter.addAction(Constant.GO_DECORATE_BIBLE_ACTION);
        registerReceiver(mainReceiver, filter);
    }

    private int decorateTitlePosition = 0;
    private void initChannelIntent(){
        if(getIntent()!=null && getIntent().getBundleExtra("channel_bundle")!=null){
            if(getIntent().getBundleExtra("channel_bundle").getInt("channel")==10){
                initFragments(1);
            }
        }
        if(getIntent()!=null && getIntent().getBundleExtra("switch_channel_bundle")!=null){
            initFragments(1);
        }
    }

    /**
     * 初始化三个页面
     */
    public int initFragments(int fragmentNum) {
//        initData(); // 初始化数据
        if (fManager == null) {
            fManager = getSupportFragmentManager();
        }
        transaction = fManager.beginTransaction();
        fragment = null;
        switch (fragmentNum) {
            case 0://首页
                MobclickAgent.onEvent(context, "click_app_index");
                fragment = new NewHomeFragment();
                setRadioButtonStyle(0);
                break;
            case 1://装修宝典
                MobclickAgent.onEvent(context, "click_app_focous");
                fragment = new DecorateArticleListFragment();
                setRadioButtonStyle(1);
                break;
            case 2://我的
                MobclickAgent.onEvent(context, "click_app_preson_center");
                fragment = new NewMineFragment();
                setRadioButtonStyle(2);
                break;
        }

        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
        return fragmentNum;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            WarnDialog.Builder builder = new WarnDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    Util.HttpPostUserUseInfo();
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    class MainBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.LOGOUT_ACTION_ACTION.equals(intent.getAction())) {
                NewMineFragment f = (NewMineFragment) fragment;
            } else if (Constant.REFRESH_MINEFRAGMENT_DATA_ACITION.equals(intent.getAction())) {
//                getConcernedDataFromNet();
            }else if(Constant.GO_DECORATE_BIBLE_ACTION.equals(intent.getAction())){
                initFragments(1);
            }
        }
    }

    private void setRadioButtonStyle(int pos){
        switch (pos){
            case 0:
                rb_home.setTextColor(Color.parseColor("#FF9C00"));
                Drawable homeDrawable0= getResources().getDrawable(R.drawable.icon_home_tab_selected);
                homeDrawable0.setBounds(0, 0, homeDrawable0.getMinimumWidth(), homeDrawable0.getMinimumHeight());
                rb_home.setCompoundDrawables(null,homeDrawable0,null,null);

                rb_bible.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable bibleDrawable0= getResources().getDrawable(R.drawable.icon_bible_tab_normal);
                bibleDrawable0.setBounds(0, 0, bibleDrawable0.getMinimumWidth(), bibleDrawable0.getMinimumHeight());
                rb_bible.setCompoundDrawables(null,bibleDrawable0,null,null);

                rb_mine.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable mineDrawable0= getResources().getDrawable(R.drawable.icon_mine_tab_normal);
                mineDrawable0.setBounds(0, 0, mineDrawable0.getMinimumWidth(), mineDrawable0.getMinimumHeight());
                rb_mine.setCompoundDrawables(null,mineDrawable0,null,null);
                break;
            case 1:
                rb_home.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable homeDrawable1= getResources().getDrawable(R.drawable.icon_home_tab_normal);
                homeDrawable1.setBounds(0, 0, homeDrawable1.getMinimumWidth(), homeDrawable1.getMinimumHeight());
                rb_home.setCompoundDrawables(null,homeDrawable1,null,null);

                rb_bible.setTextColor(Color.parseColor("#FF9C00"));
                Drawable bibleDrawable1= getResources().getDrawable(R.drawable.icon_bible_tab_selected);
                bibleDrawable1.setBounds(0, 0, bibleDrawable1.getMinimumWidth(), bibleDrawable1.getMinimumHeight());
                rb_bible.setCompoundDrawables(null,bibleDrawable1,null,null);

                rb_mine.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable mineDrawable1= getResources().getDrawable(R.drawable.icon_mine_tab_normal);
                mineDrawable1.setBounds(0, 0, mineDrawable1.getMinimumWidth(), mineDrawable1.getMinimumHeight());
                rb_mine.setCompoundDrawables(null,mineDrawable1,null,null);
                break;
            case 2:
                rb_home.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable homeDrawable2= getResources().getDrawable(R.drawable.icon_home_tab_normal);
                homeDrawable2.setBounds(0, 0, homeDrawable2.getMinimumWidth(), homeDrawable2.getMinimumHeight());
                rb_home.setCompoundDrawables(null,homeDrawable2,null,null);

                rb_bible.setTextColor(Color.parseColor("#B1B1B1"));
                Drawable bibleDrawable2= getResources().getDrawable(R.drawable.icon_bible_tab_normal);
                bibleDrawable2.setBounds(0, 0, bibleDrawable2.getMinimumWidth(), bibleDrawable2.getMinimumHeight());
                rb_bible.setCompoundDrawables(null,bibleDrawable2,null,null);

                rb_mine.setTextColor(Color.parseColor("#FF9C00"));
                Drawable mineDrawable2= getResources().getDrawable(R.drawable.icon_mine_tab_selected);
                mineDrawable2.setBounds(0, 0, mineDrawable2.getMinimumWidth(), mineDrawable2.getMinimumHeight());
                rb_mine.setCompoundDrawables(null,mineDrawable2,null,null);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mainReceiver != null) {
            unregisterReceiver(mainReceiver);
        }
    }


    public void onResume() {
        super.onResume();
        initView();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Util.setLog("MainActivity", "----MainActivity-----------------------------" + requestCode + "--" + resultCode);
        switch (resultCode) {
            case Constant.APP_LOGOUT:
                finish();
                break;
            case Constant.GOBACK_MAINACTIVITY_RESULTCODE:

                break;
            case Constant.BIBLE_POP_RESULTCODE:
                initFragments(1);
                break;

        }

        if (resultCode == Constant.FINISH_MAINACTIVITY) {
            finish();
        }

    }
}