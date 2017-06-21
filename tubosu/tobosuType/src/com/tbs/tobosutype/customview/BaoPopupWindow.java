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

public class BaoPopupWindow extends PopupWindow {
	private View mMenuView;
	private ImageView popdismiss;

	public BaoPopupWindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow_bao, null);
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
			private float moveY;
			private float moveX;

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.bao_top).getTop();
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
				case MotionEvent.ACTION_HOVER_MOVE:
					moveY = event.getY();
					moveX = event.getX();
				default:
					break;
				}
				if (Math.abs(moveY - downY - (moveX - downX)) > 0) {
					if (moveY - downY > 10) {
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
