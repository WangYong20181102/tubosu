package com.tbs.tobosutype.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.fragment.LoginFragmentAccount;
import com.tbs.tobosutype.fragment.LoginFragmentPhone;
import com.tbs.tobosutype.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录页面
 * @author dec
 *
 */
public class LoginActivity extends FragmentActivity {
	public static final String POSITION_SWITCH_ACTION_PHONE = "login_switch_position_action_phone";
	public static final String POSITION_SWITCH_ACTION_ACCOUNT = "login_switch_position_action_account";
	public static final int POSITION_ACCOUNT = 0;
	public static final int POSITION_PHONE = 1;
	private Context mContext;
	/**退出app*/
//	private ImageView login_back;
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private ViewPager mViewPager;
	
	private RelativeLayout relBack;

	private boolean isRegister = true;

	/**
	 * 初始化为-2
	 */
    public static int fromTab = -2;
	
//	/**忘记密码按钮*/
//	private TextView login_frogetPwd;
	
//	private List<String> mDatas = Arrays.asList("手机快速登录", "账户登录");
//	private ViewPagerIndicator mIndicator;
//	private LoginFragment fragment;
	
	private LoginFragmentAccount accountFragment;
	
	private LoginFragmentPhone phoneFragment;
	
	private SwitchViewpagerBroadcastReceiver switchReceiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//		AppInfoUtil.setTranslucentStatus(this);
		mContext = LoginActivity.this;


        initData();
		initReceiver();
		initView();
		initFratgmentData();
	}


	private void initData(){
//        b.putInt("tabPosition", tabPosition);
//        it.putExtra("tabPositionBundle", b);
        if(getIntent()!=null && getIntent().getBundleExtra("tabPositionBundle")!=null){
            Bundle bundle = getIntent().getBundleExtra("tabPositionBundle");
            fromTab = bundle.getInt("tabPosition");
        }

    }

	private void initReceiver() {
		if(isRegister){
			switchReceiver = new SwitchViewpagerBroadcastReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(POSITION_SWITCH_ACTION_ACCOUNT);
			filter.addAction(POSITION_SWITCH_ACTION_PHONE);
			registerReceiver(switchReceiver, filter);
		}

	}

	private void initView() {
		
		mViewPager = (ViewPager) findViewById(R.id.login_vp);

		// 来自点击收藏时 跳转 intent.putExtra("isFav", true);
//		if(getIntent()!=null && getIntent().getExtras()!=null && getIntent().getExtras().getBoolean("isFav")){
//			login_framelayout.setVisibility(View.VISIBLE);
//			iv_login_back_.setVisibility(View.VISIBLE);
//			iv_login_back_.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					exitTbs();
//				}
//			});
//		}else{
//			login_framelayout.setVisibility(View.GONE);
//			iv_login_back_.setVisibility(View.GONE);
//		}
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
		
//		mIndicator.setTabItemTitles(mDatas);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
	}


	
	/**
	 * 切换页面的广播
	 */
	private class SwitchViewpagerBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(POSITION_SWITCH_ACTION_PHONE.equals(intent.getAction())){
				mViewPager.setCurrentItem(0);
			}else if(POSITION_SWITCH_ACTION_ACCOUNT.equals(intent.getAction())){
				mViewPager.setCurrentItem(1);
			}
		}
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


			if(fromTab==-2){
				// 不是来自MainActivity的一级菜单的时候
				finish();
				Util.setLog("LoginActivity", "==========OnkeyDown======== fromTab = -2 =================");
			}else{
				Util.setLog("LoginActivity", "==========OnkeyDown======== fromTab != -2 =================");
				Intent it = new Intent();
				Bundle b = new Bundle();
				b.putInt("back", fromTab);
				it.putExtra("backBundle", b);
				setResult(0x000018, it);
				finish();
			}
			overridePendingTransition(R.anim.activity_close,0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/***
	 * 退出应用
	 */
	private void exitTbs() {
		Intent intent = getIntent();
		if (intent.getBooleanExtra("isFav", false)) {
			intent.putExtra("token", "");
			setResult(0, intent);
			finish();
		} else {
			CustomDialog.Builder builder = new CustomDialog.Builder(this);
			builder.setMessage("你确定退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									finish();
									System.exit(0);
								}
							})
					.setNegativeButton("再看看", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});
			builder.create().show();
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		try{
			if(isRegister==true){
				if(switchReceiver!=null){
					unregisterReceiver(switchReceiver);
					isRegister = false;
				}
			}
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}


	}

}
