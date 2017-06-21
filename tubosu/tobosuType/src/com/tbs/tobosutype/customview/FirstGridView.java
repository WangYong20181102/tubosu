package com.tbs.tobosutype.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
	/**
	 * 热门城市gridview
	 * @author dec
	 *
	 */
public class FirstGridView extends GridView {

	public FirstGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
