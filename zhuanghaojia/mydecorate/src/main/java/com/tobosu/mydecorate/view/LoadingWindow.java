package com.tobosu.mydecorate.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tobosu.mydecorate.R;


public class LoadingWindow extends PopupWindow{
	private Context mContext;
	private RelativeLayout rel_layout;
	private TextView tvLoadingText;
	private ProgressBar pbProgress;
	private LayoutInflater inflater;
	
	public LoadingWindow(Context mContext){
		super(mContext);
		this.mContext = mContext;
		this.inflater = LayoutInflater.from(mContext);
		this.rel_layout = (RelativeLayout) inflater.inflate(R.layout.layout_loading_window, null);
		this.pbProgress = (ProgressBar) this.rel_layout.findViewById(R.id.pb_loading_wx);
		this.tvLoadingText = (TextView) this.rel_layout.findViewById(R.id.tv_loading_text);
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	


}
