package com.tbs.tbs_mj.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.fragment.ChooseDecorateDesignFragment;
import com.tbs.tbs_mj.fragment.ChooseDecorateSquareFragment;
import com.tbs.tbs_mj.fragment.ChooseDecorateStyleFragment;
import com.tbs.tbs_mj.utils.DensityUtil;

import java.util.ArrayList;
	/**  2017-03-23 弃用 改用动画发单入口
	 * 业主选择家装资料的页面
	 * @author dec
	 */
public class ChooseActivity extends FragmentActivity{
	private static final String TAG = ChooseActivity.class.getSimpleName();
	public static final String SET_PAGE_POSITION_ACTION = "com.tobosu.set_page_positon_action";
	
	private Context context;
	
	private ViewPager viewPager_choose_type;
	
	private LinearLayout dotViewLayout;
	
	private ArrayList<Fragment> fragmentList;
	
	private ArrayList<ImageView> dotViewsList;
	
	private SetPagePositionBroadcastReceiver receiver;
	
	private TextView tv_skip;
	
	public static ArrayList<String> chooseString = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose);
		
		context = ChooseActivity.this;
		initReceiver();
		initView();
	}


	private void initView() {
		
		tv_skip = (TextView) findViewById(R.id.tv_skip);
		tv_skip.setVisibility(View.INVISIBLE);
		tv_skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if("5".equals(getSharedPreferences("Go_ChooseActivity_SP", Context.MODE_PRIVATE).getString("go_chooseStyle_string", "0"))){
					startActivity(new Intent(ChooseActivity.this, MainActivity.class));
				}
				finish();
			}
		});
		
		// 存装修房子类型， 装修房子面积 默认是空字符串
		for(int i=0;i<2;i++){
			chooseString.add("");
		}
		
		viewPager_choose_type = (ViewPager) findViewById(R.id.viewPager_choose_type);
		dotViewLayout = (LinearLayout) findViewById(R.id.dotLayout_choose_type);
		dotViewLayout.bringToFront();
		dotViewLayout.removeAllViews();
		
		fragmentList = new ArrayList<Fragment>();
		dotViewsList = new ArrayList<ImageView>();
		ChooseDecorateStyleFragment styleFragment = new ChooseDecorateStyleFragment();
		ChooseDecorateSquareFragment squareFragment = new ChooseDecorateSquareFragment();
		ChooseDecorateDesignFragment getDesignFragment = new ChooseDecorateDesignFragment();
		fragmentList.add(styleFragment);
		fragmentList.add(squareFragment);
		fragmentList.add(getDesignFragment);
		
		for(int i=0; i<fragmentList.size(); i++){
			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 10),DensityUtil.dip2px(context, 10));
			params.leftMargin = 8;
			params.rightMargin = 8;
			dotViewLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}
		viewPager_choose_type.setAdapter(new ChooseTypeAdapter(getSupportFragmentManager(), fragmentList));
		viewPager_choose_type.setOnPageChangeListener(new MyChooseTypePageChangeListener(dotViewsList));
		
	}
	
	
	
	
	private void initReceiver() {
		receiver = new SetPagePositionBroadcastReceiver();
		IntentFilter filter = new IntentFilter(SET_PAGE_POSITION_ACTION);
		registerReceiver(receiver, filter);
	}
	
	
	class ChooseTypeAdapter extends FragmentPagerAdapter {
		
		private ArrayList<Fragment> list;
		public ChooseTypeAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public int getCount() {
			return list.size();
		}
		
	}
	
	class MyChooseTypePageChangeListener implements OnPageChangeListener{
		private ArrayList<ImageView> viewList;
		
		public MyChooseTypePageChangeListener(ArrayList<ImageView> viewList){
			this.viewList = viewList;
		}
		
		@Override
		public void onPageScrollStateChanged(int position) {
			
		}

		@Override
		public void onPageScrolled(int position, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
//			System.out.println("3  onPageSelected "+position);
			for (int i = 0; i < viewList.size(); i++) {
				if (i == position) {
					((ImageView) viewList.get(position)).setBackgroundResource(R.drawable.icon_dot_selected);
				} else {
					((ImageView) viewList.get(i)).setBackgroundResource(R.drawable.icon_dot_normal);
				}
			}
		}
	}
	
	@Override
		protected void onDestroy() {
			super.onDestroy();
			if(receiver!=null){
				unregisterReceiver(receiver);
				receiver = null;
			}
		}
	
	class SetPagePositionBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(SET_PAGE_POSITION_ACTION)){
				Bundle b = intent.getBundleExtra("Pager_Postion_Intent");
				int position = b.getInt("Pager_Position_Bundle");
				viewPager_choose_type.setCurrentItem(position);
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK && getSharedPreferences("From_Home_Activity", Context.MODE_PRIVATE).getInt("home_activity_int", 0)==3){
			// 从首页过来
			finish();
			return true;
		}else if(keyCode==KeyEvent.KEYCODE_BACK){
			// 第一次安装时 返回
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
