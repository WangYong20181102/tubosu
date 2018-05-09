package com.tbs.tbs_mj.xlistview;

import com.tbs.tbs_mj.R;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	/**整个footer容器*/
	private View mContentView;
	
	private View mProgressBar;
	
	/**加载更多时的提示信息*/
	private TextView mHintView;

	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/***
	 * 初始化footer布局
	 * @param context
	 */
	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
	}
	
	/***
	 * 设置footer状态
	 * @param state
	 */
	public void setState(int state) {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		
		// 上拉 已触动加载更多 时 
		if (state == STATE_READY) {
			mHintView.setVisibility(View.VISIBLE);
			mHintView.setText("松开加载");
			mHintView.setTextColor(Color.parseColor("#ff9c00"));
		} else if (state == STATE_LOADING) { // 上拉 松手正在加载更多时 
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHintView.setVisibility(View.GONE);
		}
	}

	/***
	 * 设置footer的底部BootomMargin
	 * @param height
	 */
	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	/**
	 * 恢复正常不加载数据状态
	 */
	public void normal() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * 正在加载数据的状态
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * FooterView布局高度为0 即隐藏FooterView
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}
	
	/**
	 * 显示FooterView
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}





}
