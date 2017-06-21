package com.tbs.tobosutype.customview;

import com.tbs.tobosutype.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

public class BusinessLicensePopupWindow extends PopupWindow {
	private View mMenuView;
	private ImageView popdismiss;

	public BusinessLicensePopupWindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow_business_license, null);
		popdismiss = (ImageView) mMenuView.findViewById(R.id.pop_dismiss);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.custom_popupwindow_animstyle); 
		mMenuView.setOnTouchListener(new OnTouchListener() {

			private float downX;
			private float downY;
			private float upY;

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.business_license_top)
						.getTop();
				int y = (int) event.getY();
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					if (y < height) {
						dismiss();
					}
					upY = event.getY();
				default:
					break;
				}
				if (Math.abs(downY - downX) > 0) {
					if (upY - downY > 0) {
						dismiss();
					}
				}
				return true;
			}
		});
		popdismiss.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismiss();
				return true;
			}
		});
	}

}
