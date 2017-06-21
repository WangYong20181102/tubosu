package com.tobosu.mydecorate.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobosu.mydecorate.R;

import java.text.NumberFormat;


public class CustomProgressDialog extends AlertDialog {
	private boolean mHasStarted;
	private int mMax;
	private CharSequence mMessage;
	private ProgressBar mProgress;
	private TextView mProgressMessage;
	private TextView mProgressPercent;
	private NumberFormat mProgressPercentFormat;
	private int mProgressVal;
	private Handler mViewUpdateHandler;

	public CustomProgressDialog(Context context) {
		super(context);
		initFormats();
		setCanceledOnTouchOutside(false);
		setCancelable(false);
	}

	private void initFormats() {
		this.mProgressPercentFormat = NumberFormat.getPercentInstance();
		this.mProgressPercentFormat.setMaximumFractionDigits(0);
	}

	private void onProgressChanged() {
		this.mViewUpdateHandler.sendEmptyMessage(0);
	}

	public int getMax() {
		if (this.mProgress != null)
			return this.mProgress.getMax();
		return this.mMax;
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.custom_progress_dialog);
		this.mProgress = ((ProgressBar) findViewById(R.id.progress));
		this.mProgressPercent = ((TextView) findViewById(R.id.progress_percent));
		this.mProgressMessage = ((TextView) findViewById(R.id.progress_message));
		this.mViewUpdateHandler = new Handler() {
			public void handleMessage(Message paramMessage) {
				super.handleMessage(paramMessage);
				int i = CustomProgressDialog.this.mProgress.getProgress();
				int j = CustomProgressDialog.this.mProgress.getMax();
				if (CustomProgressDialog.this.mProgressPercentFormat != null) {
					int d = i * 100 / j;
					CustomProgressDialog.this.mProgressPercent.setText(d + "%");
					return;
				}
				CustomProgressDialog.this.mProgressPercent.setText("");
			}
		};
		onProgressChanged();
		if (this.mMessage != null)
			setMessage(this.mMessage);
		if (this.mMax > 0)
			setMax(this.mMax);
		if (this.mProgressVal > 0)
			setProgress(this.mProgressVal);
		Window window = this.getWindow();
	}

	protected void onStart() {
		super.onStart();
		this.mHasStarted = true;
	}

	protected void onStop() {
		super.onStop();
		this.mHasStarted = false;
	}

	public void setIndeterminate(boolean paramBoolean) {
		if (this.mProgress != null)
			this.mProgress.setIndeterminate(paramBoolean);
	}

	public void setMax(int paramInt) {
		if (this.mProgress != null) {
			this.mProgress.setMax(paramInt);
			onProgressChanged();
			return;
		}
		this.mMax = paramInt;
	}

	public void setMessage(CharSequence paramCharSequence) {
		if (this.mProgressMessage != null) {
			this.mProgressMessage.setText(paramCharSequence);
			return;
		}
		this.mMessage = paramCharSequence;
	}

	public void setProgress(int paramInt) {
		if (this.mHasStarted) {
			this.mProgress.setProgress(paramInt);
			onProgressChanged();
			return;
		}
		this.mProgressVal = paramInt;
	}

	public void setProgressStyle(int paramInt) {
	}
}