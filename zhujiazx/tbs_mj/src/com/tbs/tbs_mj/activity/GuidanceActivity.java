package com.tbs.tbs_mj.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.SelectCityDialog;
import com.tbs.tbs_mj.customview.SelectCityDialog.Builder;

import java.util.ArrayList;
import java.util.List;
/***
 * 引导页面
 * @author dec
 *
 */
public class GuidanceActivity extends Activity {
	private ViewPager guidance_viewPager;
	private LinearLayout layout_guidance;
	private List<ImageView> images;
	private ImageView[] dots;
	private Button btn_go;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guidance);
		
		initView();
		initData();
	}

	private void initView() {
		//采用viewpage形式设定相关的欢迎界面
		guidance_viewPager = (ViewPager) findViewById(R.id.guidance_viewPager);
		layout_guidance = (LinearLayout) findViewById(R.id.layout_guidance);
		btn_go = (Button) findViewById(R.id.btn_go);
	}
	
	private void initData() {
		images = new ArrayList<ImageView>();
		for (int i = 0; i < 2; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.guidance_01 + i);
			imageView.setScaleType(ScaleType.CENTER);
			images.add(imageView);
			btn_go.setVisibility(View.GONE);
			
		}
		
		initDots();
		guidance_viewPager.setAdapter(new MyPageAdapter());
		guidance_viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				for (int i = 0; i < 2; i++) {
					dots[i].setEnabled(false);
				}
				dots[position].setEnabled(true);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		// 跳转选择城市Activity
		images.get(1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("FirstUse", 0);
				sp.edit().putBoolean("FirstUse", false).commit(); // 看welcommeactivity
				
				
				// 首次安装 （下面该sharedpreferences设置是用于对选择装修户型，面积的ChooseActivity跳转使用）
				getSharedPreferences("Go_ChooseActivity_SP", Context.MODE_PRIVATE).edit().putString("go_chooseStyle_string", "5").commit();
				getSharedPreferences("Toggle_Icon", Context.MODE_PRIVATE).edit().putInt("icon_flag", 1).commit();
				Builder builder = new SelectCityDialog.Builder(GuidanceActivity.this);
				builder.setPositiveButton("继续",new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent cityIntent = new Intent(GuidanceActivity.this, SelectCtiyActivity.class);
								startActivity(cityIntent);
								dialog.cancel();
								dialog.dismiss();
								finish();
							}
						});

				builder.create().show();
			}
		});
	}

	
	class MyPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (images != null) {
				return images.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(images.get(position));
			return images.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(images.get(position));
		}

	}

	private void initDots() {
		dots = new ImageView[2];
		for (int i = 0; i < dots.length; i++) {
			dots[i] = (ImageView) layout_guidance.getChildAt(i);
			dots[i].setEnabled(false);
			dots[i].setTag(i);
			dots[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					guidance_viewPager.setCurrentItem((Integer) v.getTag());
				}
			});
		}
		dots[0].setEnabled(true);
	}

}
