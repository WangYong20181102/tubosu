package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DesignChartImageAdapter;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HttpServer;
import com.tbs.tobosutype.adapter.utils.PrseImageJsonUtil;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;

/**
 * 由装修公司详情页的公司介绍里的三张图片跳转过来的 -- >>设计图册列表
 * @author dec
 *
 */
public class DesignChartAcitivity extends Activity implements IXListViewListener {
	private Context mContext;
	private String comid;
	private String designChartUrl = Constant.TOBOSU_URL + "tapp/impression/comImsList";
	private RequestParams designChartParams;
	private List<HashMap<String, String>> designDatas;
	private XListView design_chart_listView;
	private DesignChartImageAdapter imageAdapter;
	private int page = 1;
	private ImageView iv_back;
	private LinearLayout ll_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_design_chart);
		mContext = DesignChartAcitivity.this;
		initView();
		initData();
	}

	private void initView() {
		design_chart_listView = (XListView) findViewById(R.id.design_chart_listView);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		design_chart_listView.setPullLoadEnable(true);
		design_chart_listView.setXListViewListener(this);
		design_chart_listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		comid = getIntent().getStringExtra("comid");
		designDatas = new ArrayList<HashMap<String, String>>();
		designChartParams = AppInfoUtil.getPublicParams(getApplicationContext());
		requestDesign();
	}

	private void requestDesign() {
		designChartParams.put("comid", comid);
		designChartParams.put("page", page + "");
		HttpServer.getInstance().requestPOST(designChartUrl, designChartParams,
				new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String result = new String(body);
						System.out.println("DesignChartAcitivity -- 解析前 result" + result);
						List<HashMap<String, String>> temDataList = PrseImageJsonUtil.parsingJson(result, mContext, comid, "DesignChartAcitivity");
						if (temDataList.size() == 0 && designDatas.size() != 0) {
							Toast.makeText(mContext, "没有更多设计图册了！", Toast.LENGTH_SHORT).show();
						}
						if (temDataList.size() != 0) {
							if (page == 1) {
								designDatas.clear();
							}
							for (int i = 0; i < temDataList.size(); i++) {
								designDatas.add(temDataList.get(i));
							}
							System.out.println("DesignChartAcitivity -- designDatas大小为：" +designDatas.size());
						}
						onLoad();
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

					}
				});
	}

	private void onLoad() {
		imageAdapter = new DesignChartImageAdapter(designDatas, this, ll_loading);
		design_chart_listView.setAdapter(imageAdapter);
		imageAdapter.notifyDataSetChanged();
		design_chart_listView.stopRefresh();
		design_chart_listView.stopLoadMore();
		design_chart_listView.setRefreshTime();
	}

	public void onLoadMore() {
		page++;
		requestDesign();
	}

	public void onRefresh() {
		page = 1;
		requestDesign();
	}
}
