package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.fragment.LoginFragmentAccount;
import com.tbs.tobosutype.fragment.LoginFragmentPhone;
import com.tbs.tobosutype.utils.Util;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录页面
 *
 * @author dec
 */
public class LoginActivity extends FragmentActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private ImageView ivBack;
    private TextView tvFindBackPass;
    private TextView tvFastLogin;
    private ImageView ivFastLogin;
    private TextView tvAccountLogin;
    private ImageView ivAccountLogin;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private RelativeLayout rel_find_decorate_titlebar;

    /**
     * 初始化为-2
     */
    public static int fromTab = -2;
    private LoginFragmentAccount accountFragment;
    private LoginFragmentPhone phoneFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = LoginActivity.this;

        initData();
        initView();
        initFratgmentData();
        setClick();
    }


    private void initData() {
        if (getIntent() != null && getIntent().getBundleExtra("tabPositionBundle") != null) {
            Bundle bundle = getIntent().getBundleExtra("tabPositionBundle");
            fromTab = bundle.getInt("tabPosition");
        }

    }

    private void initView() {
        rel_find_decorate_titlebar = (RelativeLayout) findViewById(R.id.rel_find_decorate_titlebar);
        ivBack = (ImageView) findViewById(R.id.login_back);
        tvFindBackPass = (TextView) findViewById(R.id.tv_find_password);
        tvFastLogin = (TextView) findViewById(R.id.fast_login);
        ivFastLogin = (ImageView) findViewById(R.id.iv_fast_login);
        tvAccountLogin = (TextView) findViewById(R.id.account_login);
        ivAccountLogin = (ImageView) findViewById(R.id.iv_account_login);

        mViewPager = (ViewPager) findViewById(R.id.login_vp);
        rel_find_decorate_titlebar.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    /***
     * 初始化两个fragment
     */
    private void initFratgmentData() {

        phoneFragment = new LoginFragmentPhone();
        fragmentList.add(phoneFragment);

        accountFragment = new LoginFragmentAccount();
        fragmentList.add(accountFragment);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setAdapter(mAdapter);
        setStyle(0);
    }

    public void startCount() {
        phoneFragment.startCount();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//			setResult(0x000018);
//			finish();
//			overridePendingTransition(R.anim.activity_close,0);
////			exitTbs();
//			return true;


            if (fromTab == -2) {
                // 不是来自MainActivity的一级菜单的时候
                finish();
                Util.setLog("LoginActivity", "==========OnkeyDown======== fromTab = -2 =================");
            } else {
                Util.setLog("LoginActivity", "==========OnkeyDown======== fromTab != -2 =================");
                Intent it = new Intent();
                Bundle b = new Bundle();
                b.putInt("back", fromTab);
                it.putExtra("backBundle", b);
                setResult(0x000018, it);
                finish();
            }
//			overridePendingTransition(R.anim.activity_close,0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//    /***
//     * 退出应用
//     */
//    private void exitTbs() {
//        Intent intent = getIntent();
//        if (intent.getBooleanExtra("isFav", false)) {
//            intent.putExtra("token", "");
//            setResult(0, intent);
//            finish();
//        } else {
//            CustomDialog.Builder builder = new CustomDialog.Builder(this);
//            builder.setMessage("你确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//                @Override
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                    finish();
//                    System.exit(0);
//                }
//            })
//                    .setNegativeButton("再看看", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//            builder.create().show();
//        }
//    }

    private void setClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvFindBackPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, FindPwdActivity1.class));
            }
        });

        tvFastLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyle(0);
            }
        });
        tvAccountLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStyle(1);
            }
        });
    }

    private void setStyle(int po) {
        mViewPager.setCurrentItem(po);
        switch (po) {
            case 0:
                tvFastLogin.setTextColor(Color.parseColor("#FEB20E"));
                tvAccountLogin.setTextColor(Color.parseColor("#898C8F"));
                ivFastLogin.setVisibility(View.VISIBLE);
                ivAccountLogin.setVisibility(View.GONE);
                break;
            case 1:
                tvFastLogin.setTextColor(Color.parseColor("#898C8F"));
                tvAccountLogin.setTextColor(Color.parseColor("#FEB20E"));
                ivFastLogin.setVisibility(View.GONE);
                ivAccountLogin.setVisibility(View.VISIBLE);
                break;
        }
    }
}
