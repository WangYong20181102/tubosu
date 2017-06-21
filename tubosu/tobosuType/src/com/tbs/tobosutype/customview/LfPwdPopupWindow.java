package com.tbs.tobosutype.customview;

import com.tbs.tobosutype.R;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
/***
 * 量房弹窗
 * @author dec
 *
 */
public class LfPwdPopupWindow extends PopupWindow {
	public Button bt_subit;
	public EditText et_lf_situation;
	private View mMenuView;

	public LfPwdPopupWindow(Context paramContext) {
		super(paramContext);
		this.mMenuView = ((LayoutInflater) paramContext.getSystemService("layout_inflater")).inflate(R.layout.popupwindow_lf, null);
		this.et_lf_situation = ((EditText) this.mMenuView.findViewById(R.id.et_lf_situation));
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