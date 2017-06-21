package com.tbs.tobosutype.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.umeng.analytics.MobclickAgent;
	/***
	 * 拨打电话 
	 * @author dec
	 *
	 */
public class CallDialogCompany extends Dialog {
	private TextView callButton, cancelButton, numberTextView;
	private String contactNumber;
	private String tel;
	private Context mContext;
	private ImageView lineImage;

	public CallDialogCompany(Context context, int theme, String contactNumber, String tel) {
		super(context, theme);

		this.setContentView(R.layout.call_dialog_company);

		this.contactNumber = contactNumber;
		this.tel = tel;
		this.mContext = context;
		callButton = (TextView) findViewById(R.id.telText);
		cancelButton = (TextView) findViewById(R.id.cancelButton);
		numberTextView = (TextView) findViewById(R.id.contactNumberText);
		lineImage = (ImageView) findViewById(R.id.lineImage);

		callButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext,"click_find_com_com_detail_phone_call(phone_call_succeed)");
				Uri uri = Uri.parse("tel:" + CallDialogCompany.this.tel);
				Intent i = new Intent(Intent.ACTION_DIAL, uri);
				mContext.startActivity(i);
				CallDialogCompany.this.dismiss();
			}
		});

		numberTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(mContext,"click_find_com_com_detail_phone_call(phone_call_succeed)");
				String number = "";
				if (CallDialogCompany.this.contactNumber.contains("土拨鼠")) {
					number = "400-606-2221";
				} else {
					number = CallDialogCompany.this.contactNumber;
				}

				Uri uri = Uri.parse("tel:" + number);
				Intent i = new Intent(Intent.ACTION_DIAL, uri);
				mContext.startActivity(i);
				CallDialogCompany.this.dismiss();
			}
		});

		//
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CallDialogCompany.this.dismiss();
			}
		});

		callButton.setText("固话：" + tel);
		if (contactNumber.contains("土拨鼠")) {
			numberTextView.setText("土拨鼠热线：400-606-2221");
		} else {
			numberTextView.setText("手机： " + contactNumber);
		}

		if (contactNumber.equals("")) {
			numberTextView.setVisibility(View.GONE);
			lineImage.setVisibility(View.GONE);
		}
		if (tel.equals("")) {
			callButton.setVisibility(View.GONE);
			lineImage.setVisibility(View.GONE);
		}
		Window window = this.getWindow();
		// 设置显示动画
//		window.setWindowAnimations(R.style.calldialog_animstyle);
		this.setCanceledOnTouchOutside(true);
	}

}
