package com.tbs.tobosutype.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.tbs.tobosutype.R;
/***
 * 订单设置密码弹窗
 * @author dec
 *
 */
public class SettingOrderPwdPopupWindow extends PopupWindow {
	public Button bt_subit;
	public EditText et_setting_order_pwd1;
	public EditText et_setting_order_pwd2;
	private View mMenuView;

	public SettingOrderPwdPopupWindow(Context paramContext) {
		super(paramContext);
		this.mMenuView = ((LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popupwindow_setting_order_pwd, null);
		this.et_setting_order_pwd1 = ((EditText) this.mMenuView.findViewById(R.id.et_setting_order_pwd1));
		this.et_setting_order_pwd2 = ((EditText) this.mMenuView.findViewById(R.id.et_setting_order_pwd2));
		this.bt_subit = ((Button) this.mMenuView.findViewById(R.id.bt_subit));
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setAnimationStyle(R.style.custom_popupwindow_animstyle);  
	}
}