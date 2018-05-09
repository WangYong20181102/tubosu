package com.tbs.tbs_mj.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;

import com.tbs.tbs_mj.R;
	/***
	 * 自定义的选择城市Dialog
	 * @author dec
	 *
	 */
public class SelectCityDialog extends Dialog {

	public SelectCityDialog(Context context) {
		super(context);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public SelectCityDialog(Context context, int theme) {
		super(context, theme);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public static class Builder {
		private Context context;
		private String positiveButtonText;
		private DialogInterface.OnClickListener positiveButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setContentView(View v) {
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context, R.style.custom_dialog_style);
			View layout = inflater.inflate(R.layout.diaglog_select_city, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
			}
			dialog.setContentView(layout);
			Window window = dialog.getWindow();
			//去掉dialog出现和消失的动画
//			window.setWindowAnimations(R.style.custom_dialog_animstyle);
			return dialog;
		}
	}
}