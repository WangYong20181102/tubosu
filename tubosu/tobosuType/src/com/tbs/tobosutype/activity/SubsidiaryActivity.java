package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
/**
 * 没使用
 * @author dec
 *
 */
public class SubsidiaryActivity extends Activity {
	private ImageView subsidiary_back;
	private String requestUrl = AllConstants.TOBOSU_URL
			+ "/tapp/company/fund_manage";
	private RequestParams params;
	private int page = 1;
	private ListView listView;
	private static SubsidiaryAdapter adapter;
	private TextView tv_subsidiary_amount_available;
	private String token;
	private String menony;
	LinearLayout subsidiary_loading;
	LinearLayout ll_subsidiary_amount_available;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		token = getIntent().getExtras().getString("token");
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_subsidiary);
		subsidiary_back = (ImageView) findViewById(R.id.subsidiary_back);
		listView = (ListView) findViewById(R.id.subsidiary_listview);
		tv_subsidiary_amount_available = (TextView) findViewById(R.id.tv_subsidiary_amount_available);
		ll_subsidiary_amount_available = (LinearLayout) findViewById(R.id.ll_subsidiary_amount_available);
		subsidiary_loading = (LinearLayout) findViewById(R.id.ll_loading);
	}

	private void initData() {
		params = AppInfoUtil.getPublicParams(getApplicationContext());
		params.put("page", page);
		params.put("token", token);
		requestSubsidiaryPost();
	}

	private void initEvent() {
		subsidiary_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	private void requestSubsidiaryPost() {
		HttpServer.getInstance().requestPOST(requestUrl, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2,
							Throwable arg3) {
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						String result = new String(responseBody);
						List<HashMap<String, Object>> subsidiaryListDatas = new ArrayList<HashMap<String, Object>>();
						subsidiaryListDatas = parseJSON(result);
						tv_subsidiary_amount_available.setText(menony);
						ll_subsidiary_amount_available
								.setVisibility(View.VISIBLE);
						adapter = new SubsidiaryAdapter(SubsidiaryActivity.this, subsidiaryListDatas);
						listView.setAdapter(adapter);
						subsidiary_loading.setVisibility(View.GONE);
					}
				});
	}

	private List<HashMap<String, Object>> parseJSON(String result) {
		try {
			List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			JSONObject object = new JSONObject(result);
			int error_code = object.getInt("error_code");
			if (error_code == 0) {
				JSONObject data = object.getJSONObject("data");
				menony = data.getString("menony");
				JSONArray historyArray = data.getJSONArray("history");
				for (int i = 0; i < historyArray.length(); i++) {
					HashMap<String, Object> dataMap = new HashMap<String, Object>();
					JSONObject historyObject = historyArray.getJSONObject(i);
					dataMap.put("orderid", historyObject.getString("orderid"));
					dataMap.put("memo", historyObject.getString("memo"));
					dataMap.put("tradetime",
							historyObject.getString("tradetime"));
					dataMap.put("fund", historyObject.getString("fund"));
					list.add(dataMap);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	class SubsidiaryAdapter extends BaseAdapter {
		private List<HashMap<String, Object>> list = null;
		private Context context;

		public SubsidiaryAdapter(Context context,
				List<HashMap<String, Object>> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			if (list != null) {

				return list.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			if (listView == null) {
				listView = (ListView) parent;
			}
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_subsidiary, null);
				holder.subsidiary_deduction_details = (TextView) convertView
						.findViewById(R.id.subsidiary_deduction_details);
				holder.subsidiary_deduction_amount = (TextView) convertView
						.findViewById(R.id.subsidiary_deduction_amount);
				holder.subsidiary_deduction_time = (TextView) convertView
						.findViewById(R.id.subsidiary_deduction_time);
				holder.subsidiary_deduction_id = (TextView) convertView
						.findViewById(R.id.subsidiary_deduction_id);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.subsidiary_deduction_details.setText(list.get(position)
					.get("memo").toString());
			holder.subsidiary_deduction_amount.setText(list.get(position)
					.get("fund").toString());
			holder.subsidiary_deduction_time.setText(list.get(position)
					.get("tradetime").toString());
			holder.subsidiary_deduction_id.setText("订单编号 ："
					+ list.get(position).get("orderid").toString());
			return convertView;
		}

		class ViewHolder {
			private TextView subsidiary_deduction_details;
			private TextView subsidiary_deduction_amount;
			private TextView subsidiary_deduction_time;
			private TextView subsidiary_deduction_id;
		}
	}

}