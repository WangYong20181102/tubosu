package com.tbs.tobosutype.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.utils.Util;

/***
 * 输入密码提醒 弹窗
 * @author dec
 *
 */
public class InputWarnDialog extends Dialog {

	public InputWarnDialog(Context context) {
		super(context);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public InputWarnDialog(Context context, int theme) {
		super(context, theme);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;
		private TextView tv_title;
		private TextView tv_message;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

//		public Builder setMessage(int message) {
//			this.message = (String) context.getText(message);
//			return this;
//		}
//
//		public Builder setTitle(int title) {
//			this.title = (String) context.getText(title);
//			return this;
//		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public InputWarnDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final InputWarnDialog dialog = new InputWarnDialog(context, R.style.custom_dialog_style);
			View layout = inflater.inflate(R.layout.input_warn_dialog_layout, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv_title = ((TextView) layout.findViewById(R.id.warn_title));
			tv_message = ((TextView) layout.findViewById(R.id.warn_message));
			if (title != null) {
				tv_title.setText(title);
				tv_title.setVisibility(View.VISIBLE);
			} else {
				tv_title.setVisibility(View.GONE);
			}
			tv_message.setGravity(Gravity.CENTER);
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.warn_positiveButton)).setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.warn_positiveButton)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
			}
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.warn_negativeButton)).setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.warn_negativeButton)).setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
			}
			if (message != null) {
				Util.setLog("cao","-----------------------------------" + message);
				tv_message.setText("您还没设置订单密码，请先设置");
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.warn_content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.warn_content)).addView(contentView, new LayoutParams(
								LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
			dialog.setContentView(layout);
			Window window = dialog.getWindow();
//			window.setWindowAnimations(R.style.custom_dialog_animstyle);
			return dialog;
		}
	}
}