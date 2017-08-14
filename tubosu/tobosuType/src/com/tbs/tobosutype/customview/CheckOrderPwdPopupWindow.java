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
import android.widget.TextView;

/***
 * 输入订单密码查看订单的弹窗
 * @author dec
 *
 */
public class CheckOrderPwdPopupWindow extends PopupWindow {
    public TextView bt_subit;
    public EditText et_check_order_pwd;
    private View mMenuView;

    public CheckOrderPwdPopupWindow(Context paramContext) {
        super(paramContext);
//		this.mMenuView = ((LayoutInflater) paramContext .getSystemService("layout_inflater")).inflate(R.layout.popupwindow_check_order_pwd, null);
        LayoutInflater inflater = (LayoutInflater) paramContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_check_order_pwd, null);
        this.et_check_order_pwd = ((EditText) this.mMenuView.findViewById(R.id.et_check_order_pwd));
        this.bt_subit = ((TextView) this.mMenuView.findViewById(R.id.bt_subit));
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0x80000000);
//        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.custom_popupwindow_animstyle);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }
}