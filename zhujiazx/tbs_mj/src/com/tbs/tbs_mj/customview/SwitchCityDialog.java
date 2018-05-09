package com.tbs.tbs_mj.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
	/**
	 * 切换城市时警告弹框
	 * @author dec
	 */
public class SwitchCityDialog extends Dialog {
	private Context context;
	
	public SwitchCityDialog(Context context){
		super(context);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
	}
	
	public SwitchCityDialog(Context context, int theme){
		super(context, theme);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
	}
	
	
	public static class Builder{
		private Context context;
		private String title;
		private String message;
		private String negativeButtonText;
		private String positiveButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveClickListener, negativeClickListener;
		private TextView textMessage;
		
		public Builder(Context context){
			this.context = context;
		}
		
		public Builder setMessage(String message){
			this.message = message;
			return this;
		}
		
		
		public Builder setTitle(String title){
			this.title = title;
			return this;
		}
		
		public Builder setContentView(View view){
			this.contentView = view;
			return this;
		}
		
		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener positiveClickListener){
			this.positiveButtonText = (String) this.context.getText(positiveButtonText);
			this.positiveClickListener = positiveClickListener;
			return this;
		}
		
		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener positiveClickListener){
			this.positiveButtonText = positiveButtonText;
			this.positiveClickListener = positiveClickListener;
			return this;
		}
		
		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener negativeOnClickListener){
			this.negativeButtonText = (String) this.context.getText(negativeButtonText);
			this.negativeClickListener = negativeOnClickListener;
			return this;
		}
		
		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener negativeOnClickListener){
			this.negativeButtonText = negativeButtonText;
			this.negativeClickListener = negativeOnClickListener;
			return this;
		}
		
		public SwitchCityDialog create(){
			LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final SwitchCityDialog dialog = new SwitchCityDialog(context, R.style.custom_dialog_style);
			View layout = inflate.inflate(R.layout.switch_city_dialog_layout, null);
			dialog.setContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			textMessage = ((TextView) layout.findViewById(R.id.switch_message));
			textMessage.setGravity(Gravity.CENTER);
			
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positive_button)).setText(positiveButtonText);
				if (positiveClickListener != null) {
					((Button) layout.findViewById(R.id.positive_button)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.positive_button).setVisibility(View.GONE);
			}
			
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
				if (negativeClickListener != null) {
					((Button) layout.findViewById(R.id.negative_button)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.negative_button).setVisibility(View.GONE);
			}
			
			if (message != null) {
				textMessage.setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			
			return dialog;
		}
		
	}
	
}
