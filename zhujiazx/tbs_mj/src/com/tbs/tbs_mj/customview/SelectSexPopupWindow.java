package com.tbs.tbs_mj.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
	/**
	 * 选择性别window
	 * @author dec
	 *
	 */
public class SelectSexPopupWindow extends PopupWindow {
	public RadioButton bt_male, bt_female;
	public TextView tv_enter;
	private View mMenuView;

	public SelectSexPopupWindow(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow_selectsex, null);
		bt_male = (RadioButton) mMenuView.findViewById(R.id.bt_male);
		bt_female = (RadioButton) mMenuView.findViewById(R.id.bt_female);
		tv_enter = (TextView) mMenuView.findViewById(R.id.tv_enter);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.custom_popupwindow_animstyle);  
	}
}
