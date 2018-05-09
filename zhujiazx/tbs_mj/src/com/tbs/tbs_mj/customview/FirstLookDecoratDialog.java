package com.tbs.tbs_mj.customview;

import com.tbs.tbs_mj.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

public class FirstLookDecoratDialog extends Dialog {

	public FirstLookDecoratDialog(Context context) {
		super(context);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public FirstLookDecoratDialog(Context context, int theme) {
		super(context, theme);
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(false);
	}

	public static class Builder {
		private Context context;
		private View layout;
		private View tv_already_know;

		public Builder(Context context) {
			this.context = context;
		}

		public FirstLookDecoratDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final FirstLookDecoratDialog dialog = new FirstLookDecoratDialog(
					context, R.style.custom_dialog_style);
			layout = inflater.inflate(R.layout.diaglog_first_look_decorat, null);
			tv_already_know = layout.findViewById(R.id.tv_already_know);
			tv_already_know.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			Window window = dialog.getWindow();
//			window.setWindowAnimations(R.style.custom_dialog_animstyle);
			return dialog;
		}
	}
}