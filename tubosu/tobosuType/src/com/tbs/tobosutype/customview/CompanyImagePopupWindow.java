package com.tbs.tobosutype.customview;

import java.util.ArrayList;
import java.util.List;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.PopupWindow;

@SuppressLint("ViewConstructor") public class CompanyImagePopupWindow extends PopupWindow {

	private View conentView;
	private ViewPager vp_company_image;
	private ArrayList<ImageView> imageViews;
	private TextView tv_image_num;

	public CompanyImagePopupWindow(final Activity context,
			final List<String> imgDatas, int postion) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		conentView = inflater.inflate(R.layout.item_pupopuwindow_company_image,
				null);
		this.setContentView(conentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(false);
		this.update();
		ColorDrawable dw = new ColorDrawable(0000000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.custom_popupwindow_animstyle);
		vp_company_image = (ViewPager) conentView
				.findViewById(R.id.vp_company_image);
		tv_image_num = (TextView) conentView.findViewById(R.id.tv_image_num);
		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imgDatas.size(); i++) {
			ImageView iv = new ImageView(context);
			ImageLoaderUtil.loadImage(context, iv, imgDatas.get(i));
			iv.setScaleType(ScaleType.FIT_CENTER);
			imageViews.add(iv);
		}
		vp_company_image.setAdapter(new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return imageViews.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(imageViews.get(position));
				return imageViews.get(position);
			}
		});
		vp_company_image.setCurrentItem(postion);
		tv_image_num.setText((postion + 1) + " / " + imgDatas.size());
		vp_company_image.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int postion) {
				tv_image_num.setText((postion + 1) + " / " + imgDatas.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
}
