package com.tbs.tobosupicture.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.tbs.tobosupicture.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 自定义GridView 下拉刷新
 *
 * @author zad
 *
 */
@SuppressLint("SimpleDateFormat")
public class GridView2 extends GridView {
	private LayoutInflater inflater;
	/**
	 * 头部view
	 */
	private static View headerView;
	/**
	 * 向下或向上箭头图
	 */
	private static ImageView arrowImgView;
	/**
	 * 进度条
	 */
	private static ProgressBar progressBar;
	/**
	 * 状态视图，显示下拉、释放或正在刷新
	 */
	private static TextView stateTextView;
	/**
	 * 显示上次刷新时间
	 */
	private static TextView lastUpdateTimeTextView;
	/**
	 * 头部高度
	 */
	private static int viewHeaderHight;
	/**
	 * 摁下时的相对于屏幕的Y坐标
	 */
	private int downY;
	/**
	 * 滑动时相对于屏幕的Y坐标
	 */
	private int moveY;
	/**
	 * 是否加载完成
	 */
	private boolean isPullRefreshCompleted = true;
	/**
	 * 是否加载更多完成
	 */
	private boolean isLoadingMoreCompleted = true;

	/**
	 * 下拉状态枚举
	 */
	private enum State {
		/**
		 * 常规状态
		 */
		STATE_NONE,
		/**
		 * 下拉刷新状态
		 */
		STATE_PULL_TO_REFRESH,
		/**
		 * 释放刷新状态
		 */
		STATE_RELEASE_TO_REFRESH,
		/**
		 * 正在刷新状态
		 */
		STATE_REFRESHING
	}

	/**
	 * 默认初始化状态为常规状态
	 */
	private static State currentStatus = State.STATE_NONE;
	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private State lastStatus = currentStatus;

	private PullToRefreshListener listener;
	/**
	 * GridView滑动距离
	 */
	private int gridViewScrollY;
	/**
	 * 自定义旋转动画
	 */
	private RotateAnimation animationRotate01, animationRotate02;

	public GridView2(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public GridView2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public GridView2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// 初始化头部视图
		initHeaderView();
		// initFooterView();
		// 初始化动画效果
		initAnimation();
	}

	/**
	 * 初始化头部视图
	 */
	private void initHeaderView() {

		headerView = inflater.inflate(R.layout.pull_refresh_header, null);
		arrowImgView = (ImageView) headerView
				.findViewById(R.id.header_arrowImageView); // 箭头向上向下图片
		progressBar = (ProgressBar) headerView
				.findViewById(R.id.header_progressBar); // 进度条,初始化隐藏
		stateTextView = (TextView) headerView
				.findViewById(R.id.header_pullTextView); // 初始化下拉刷新的文本框
		lastUpdateTimeTextView = (TextView) headerView
				.findViewById(R.id.header_lastUpdatedTextView); // 记录更新时间的文本框
		headerView.measure(0, 0); // 初始化时先测量一下headerView
		viewHeaderHight = headerView.getMeasuredHeight(); // 获取headerView的实际高度
		setPaddingTop(headerView, -viewHeaderHight); // 设置内边距为负数，隐藏头部
	}

	/**
	 * 初始化动画效果
	 */
	private void initAnimation() {
		/* RotateAnimation.RELATIVE_TO_SELF 表示相对于控件自己的坐标，0.5f 表示以中心点为轴心旋转 */
		animationRotate01 = new RotateAnimation(180, 360,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animationRotate01.setInterpolator(new LinearInterpolator()); // 设置动画的变化速度
		// new
		// LinearInterpolator()表示匀速
		animationRotate01.setDuration(100); // 动画持续时间
		animationRotate01.setFillAfter(true); // 动画停止与最后时刻

		animationRotate02 = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animationRotate02.setInterpolator(new LinearInterpolator());
		animationRotate02.setDuration(100);
		animationRotate02.setFillAfter(true);

	}

	/**
	 * 刷新前的准备工作
	 *
	 * @param distance
	 */
	private void prepareToRefreshing(int distance) {

		if (this.getGridViewScrollY() > 0 && currentStatus == State.STATE_NONE) {
			/* 防止GridView不是在第一个孩子的位置向下滑动时，将刷新头部下拉 */
			return;
		}
		if (this.getGridViewScrollY() == 0 && currentStatus == State.STATE_NONE) {
			/* 在当上一条件不满足的情况下，然而这时候的滑动距离distance 不定是0 所以要将其清零 否则会有突然弹出头部的bug */
			distance = 0;
		}

		setPaddingTop(headerView, -viewHeaderHight + distance / 3); // 通过滑动距离，计算并设置头部内边距露出高度

		int headerPaddingTop = getPaddingTop(headerView); // 获取头部顶端内边距
		if (headerPaddingTop > 0
				&& currentStatus != State.STATE_RELEASE_TO_REFRESH) {
			currentStatus = State.STATE_RELEASE_TO_REFRESH;
		}
		if (headerPaddingTop < 0
				&& currentStatus != State.STATE_PULL_TO_REFRESH) {
			currentStatus = State.STATE_PULL_TO_REFRESH;
		}

		// 避免重复操作
		if (lastStatus != currentStatus) {

			if (currentStatus == State.STATE_PULL_TO_REFRESH) {
				/* 下拉 */
				stateTextView.setVisibility(TextView.VISIBLE);
				arrowImgView.setVisibility(View.VISIBLE);
				arrowImgView.setImageResource(R.mipmap.default_ptr_flip);
				// lastUpdateTimeTextView.setVisibility(TextView.VISIBLE);
				arrowImgView.clearAnimation();
				arrowImgView.startAnimation(animationRotate01);
				progressBar.setVisibility(View.GONE);
				stateTextView.setText("下拉刷新");

			} else if (currentStatus == State.STATE_RELEASE_TO_REFRESH) {
				/* 释放 */
				stateTextView.setVisibility(TextView.VISIBLE);
				arrowImgView.setVisibility(View.VISIBLE);
				arrowImgView.setImageResource(R.mipmap.default_ptr_flip);
				// lastUpdateTimeTextView.setVisibility(TextView.VISIBLE);
				arrowImgView.clearAnimation();
				arrowImgView.startAnimation(animationRotate02);
				progressBar.setVisibility(View.GONE);
				stateTextView.setText("释放立即刷新");

			}
		}

		lastStatus = currentStatus;
	}

	/**
	 * 头部刷新
	 */
	private void headerRefreshing() {

		currentStatus = State.STATE_REFRESHING; // 正在刷新状态
		setPaddingTop(headerView, 0); // 设置内边距为0 表示不隐藏刷新头部
		arrowImgView.setVisibility(View.GONE);
		arrowImgView.setImageDrawable(null);
		progressBar.setVisibility(View.VISIBLE);
		stateTextView.setVisibility(View.VISIBLE);
		stateTextView.setText("正在刷新...");
		if (listener != null) {
			listener.onPullToRefreshing(); // 调用被重写的方法
		}
	}

	/**
	 * 设置头顶内边距
	 */
	private static void setPaddingTop(View view, int topPadding) {
		view.setPadding(0, topPadding, 0, 0); // 左上右下
	}

	/**
	 * 获取头顶内边距
	 *
	 * @param view
	 * @return
	 */
	private int getPaddingTop(View view) {
		return view.getPaddingTop();
	}

	public View getHeaderView() {
		return headerView;
	}

	/**
	 * 结束刷新消息机制
	 */
	public static Handler refreshingCompletedHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 0x126) {
				stateTextView.setText("下拉刷新");
				progressBar.setVisibility(View.GONE);
				currentStatus = State.STATE_NONE;
				setPaddingTop(headerView, -viewHeaderHight); // 隐藏
				arrowImgView.setImageResource(R.mipmap.down_arrow);
				arrowImgView.setVisibility(View.VISIBLE);
				lastUpdateTimeTextView.setVisibility(View.VISIBLE);
				stateTextView.setVisibility(View.VISIBLE);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				lastUpdateTimeTextView.setText("上次更新：" + sdf.format(new Date()));
			}

		}

	};

	/**
	 * 用于外部调用
	 *
	 * @param listener
	 */
	@SuppressLint("SimpleDateFormat")
	public void setOnPullToRefreshingListener(PullToRefreshListener listener) {

		if (listener != null) {
			this.listener = listener;
		}

	}

	/**
	 * 外部接口
	 *
	 * @author Administrator
	 *
	 */
	public interface PullToRefreshListener {
		/**
		 * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
		 */
		public void onPullToRefreshing();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: // 按下
				downY = (int) event.getRawY(); // getRawY()获得的是相对屏幕的位置
				break;
			case MotionEvent.ACTION_MOVE: // 滑动
				if(isLoadingMoreCompleted){
					moveY = (int) event.getRawY(); // getRawY()获取屏幕滑动的相对位置
					int distance = 0;
					distance = moveY - downY; // 计算按下和滑动的距离;
					prepareToRefreshing(distance); // 准备刷新
				}
				break;
			case MotionEvent.ACTION_UP: // 抬起
				if (currentStatus == State.STATE_RELEASE_TO_REFRESH) {
				/* 当前状态为释放刷新 */
					headerRefreshing(); // 执行刷新
				} else if (currentStatus == State.STATE_PULL_TO_REFRESH) {
				/* 下拉刷新 */
					currentStatus = State.STATE_NONE; // 设置当前状态为常规状态
					setPaddingTop(headerView, -viewHeaderHight); // 隐藏头部
				}
		}

		return super.onTouchEvent(event);
	}

	/**
	 * 获取GridView滑动距离
	 *
	 * @return
	 */
	public int getGridViewScrollY() {
		return gridViewScrollY;
	}

	/**
	 * 设置GridView滑动距离
	 *
	 * @param gridViewScrollY
	 */
	public void setGridViewScrollY(int gridViewScrollY) {
		this.gridViewScrollY = gridViewScrollY;
	}

	/**
	 * 是否已经刷新结束
	 */
	public boolean isPullRefreshCompleted() {
		return isPullRefreshCompleted;
	}
	/**
	 * 设置刷新是否结束
	 */
	public void setPullRefreshCompleted(boolean isLoadingMoreCompleted) {
		this.isLoadingMoreCompleted = isLoadingMoreCompleted;
		if(isLoadingMoreCompleted){
			refreshingCompletedHandler.sendEmptyMessage(0x126);
		}
	}
	/**
	 * 加载更多是否结束
	 */
	public boolean isLoadingMoreCompleted() {
		return isLoadingMoreCompleted;
	}
	/**
	 * 设置加载更多是否结束
	 */
	public void setLoadingMoreCompleted(boolean isLoadingMoreCompleted) {
		this.isLoadingMoreCompleted = isLoadingMoreCompleted;
	}
}
