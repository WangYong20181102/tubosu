package com.tbs.tobosutype.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.HintInput;

	/***
	 * 免费预约popupwindow 从底部往上弹出
	 * @author dec
	 */
@SuppressLint("ViewConstructor")
public class DesignFreePopupWindow extends PopupWindow {
	
	private EditText dialog_freedesign_phone;
	
	/**确定提交按钮*/
	private TextView dialog_freedesign_submit;
	public TextView dialog_freedesign_title;
	private View mView;

	public DesignFreePopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.call_dialog_freedesign, null);

		dialog_freedesign_phone = (EditText) mView.findViewById(R.id.dialog_freedesign_phone);
		// 监听虚拟键盘
		new HintInput(11, dialog_freedesign_phone, context);
		
		dialog_freedesign_submit = (TextView) mView.findViewById(R.id.dialog_freedesign_submit);
		dialog_freedesign_title = (TextView) mView.findViewById(R.id.dialog_freedesign_title);
		dialog_freedesign_submit.setOnClickListener(itemsOnClick);

		this.setContentView(mView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x80000000);
		this.setBackgroundDrawable(dw);
		this.setAnimationStyle(R.style.custom_popupwindow_animstyle);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		mView.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = mView.findViewById(R.id.pop).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	public String getPhone() {
		String strPhone = dialog_freedesign_phone.getText().toString();
		return strPhone;
	}
}
