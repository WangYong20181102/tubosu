package com.tbs.tobosutype.activity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.MyApplication;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 订单反馈页 -- 类似即时聊天页面
 * @author dec
 *
 */
public class OrderFeedBackActivity extends Activity {

	private EditText feedback_input_content;
	
	/**未量房*/
	private RadioButton feedback_noamount_room;
	
	/**未签约*/
	private RadioButton feedback_not_signed;
	
	/**已经签约*/
	private RadioButton feedback_contract_signed;
	
	/**申请退款*/
	private RadioButton feedback_apply_refund;
	
	private String sendContent;
	
	private ListView order_feedback_listview;
	
	/***订单反馈追踪接口*/
	private String orderFeedBackUrl = Constant.TOBOSU_URL + "/tapp/order/fangkui";
	
	/***保存反馈信息接口*/
	private String saveFeedBackUrl = Constant.TOBOSU_URL + "/tapp/order/save_fangkiui";
	
	private HashMap<String, Object> feedBackParams;
	private HashMap<String, Object> saveFeedBackParams;
	
	/**反馈内容集合*/
	private ArrayList<HashMap<String, String>> feedBackList;
	
	/***聊天适配器*/
	private OrderFeedBackAdapter orderFeedBackAdapter;
	
	/**发送按钮*/
	private TextView tv_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("SimpleDateFormat")
	private void initView() {
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_order_feedback);
		feedback_input_content = (EditText) findViewById(R.id.feedback_input_content);
		feedback_noamount_room = (RadioButton) findViewById(R.id.feedback_noamount_room);
		feedback_not_signed = (RadioButton) findViewById(R.id.feedback_not_signed);
		feedback_contract_signed = (RadioButton) findViewById(R.id.feedback_contract_signed);
		feedback_apply_refund = (RadioButton) findViewById(R.id.feedback_apply_refund);
		order_feedback_listview = (ListView) findViewById(R.id.order_feedback_listview);
		tv_send = (TextView) findViewById(R.id.tv_send);
		
		tv_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendContent = "";
				String content_class_id = "";
				if (feedback_noamount_room.isChecked()) {
					sendContent += "【未量房】  ";
					content_class_id = "1";
				}
				if (feedback_not_signed.isChecked()) {
					content_class_id = "3";
					sendContent += "【未签约】  ";
				}
				if (feedback_contract_signed.isChecked()) {
					content_class_id = "4";
					sendContent += "【已签约】  ";
				}
				if (feedback_apply_refund.isChecked()) {
					content_class_id = "5";
					sendContent += "【申请退款】  ";
				}
				if (sendContent.length() == 0) {
					Toast.makeText(getApplicationContext(), "请选择反馈类型！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				String input_content = feedback_input_content.getText().toString().trim();
				if (TextUtils.isEmpty(input_content)) {
					Toast.makeText(getApplicationContext(), "请输入反馈内容！",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					sendContent += input_content;
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("contents", sendContent);
				map.put("opp_realname", "opp_realname");
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				map.put("addtime", format.format(date));
				map.put("replayer", "公司");
				
				if (feedBackList == null) {
					feedBackList = new ArrayList<HashMap<String, String>>();
				}
				feedBackList.add(map);
				orderFeedBackAdapter.notifyDataSetChanged();
				order_feedback_listview.setSelection(order_feedback_listview.getCount() - 1);
				saveFeedBackParams.put("content", feedback_input_content
						.getText().toString().trim());
				saveFeedBackParams.put("content_class_id", content_class_id);
				Intent intent = getIntent();
				saveFeedBackParams.put("id", intent.getStringExtra("order_id"));
				saveFeedBackParams.put("token", intent.getStringExtra("token"));
				saveFeedBackParams.put("content_class_id", content_class_id);
				
				// 保存反馈信息接口请求
				OKHttpUtil.post(saveFeedBackUrl, saveFeedBackParams, new Callback() {
							@Override
							public void onFailure(Call call, IOException e) {

							}

							@Override
							public void onResponse(Call call, Response response) throws IOException {

							}
						});
				feedback_input_content.setText("");
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		feedBackList = new ArrayList<HashMap<String, String>>();
		feedBackParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		saveFeedBackParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		feedBackParams.put("token", intent.getStringExtra("token"));
		feedBackParams.put("id", intent.getStringExtra("order_id"));
		
		// 订单反馈追踪接口请求
		OKHttpUtil.post(orderFeedBackUrl, feedBackParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String result = response.body().string();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						feedBackList = jsonToFeedBackList(result);
						orderFeedBackAdapter = new OrderFeedBackAdapter();
						orderFeedBackAdapter.notifyDataSetChanged();
						order_feedback_listview.setAdapter(orderFeedBackAdapter);
						order_feedback_listview.setSelection(order_feedback_listview.getCount() - 1);
					}
				});
			}
		});
	}

	/***
	 * 将json转化成集合
	 * @param result
	 * @return
	 */
	protected ArrayList<HashMap<String, String>> jsonToFeedBackList(String result) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject object = new JSONObject(result);
			if (object.getInt("error_code") == 0) {
				JSONArray data = object.getJSONArray("data");
				for (int i = 0; i < data.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject dataObject = data.getJSONObject(i);
					String contents = dataObject.getString("contents");
					String opp_realname = dataObject.getString("opp_realname");
					map.put("contents", contents);
					map.put("opp_realname", opp_realname);
					map.put("addtime", dataObject.getString("addtime"));
					map.put("replayer", dataObject.getString("replayer"));
					list.add(map);
				}
				return list;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 订单反馈适配器
	 * @author dec
	 *
	 */
	class OrderFeedBackAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (feedBackList != null && feedBackList.size() > 0) {
				return feedBackList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			if (feedBackList != null && feedBackList.size() > 0) {
				return feedBackList.get(position);
			} else {
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(OrderFeedBackActivity.this)
						.inflate(R.layout.item_order_feedback, null);
				holder.feedback_receive_pic = (ImageView) convertView
						.findViewById(R.id.feedback_receive_pic);
				holder.feedback_receive_text = (TextView) convertView
						.findViewById(R.id.feedback_receive_text);
				holder.feedback_send_pic = (ImageView) convertView
						.findViewById(R.id.feedback_send_pic);
				holder.feedback_send_text = (TextView) convertView
						.findViewById(R.id.feedback_send_text);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.ll_order_feedback_receive = (RelativeLayout) convertView
						.findViewById(R.id.ll_order_feedback_receive);
				holder.ll_order_feedback_send = (RelativeLayout) convertView
						.findViewById(R.id.ll_order_feedback_send);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			
			String contents = feedBackList.get(position).get("contents");
			String replayer = feedBackList.get(position).get("replayer");
			
			if (replayer.equals("客服")) {
				holder.ll_order_feedback_receive.setVisibility(View.VISIBLE);
				holder.ll_order_feedback_send.setVisibility(View.INVISIBLE);
				holder.feedback_receive_text.setText(contents);
			} else {
				holder.ll_order_feedback_receive.setVisibility(View.INVISIBLE);
				holder.ll_order_feedback_send.setVisibility(View.VISIBLE);
				holder.feedback_send_text.setText(contents);
				ImageLoaderUtil.loadImage(getApplicationContext(),
						holder.feedback_send_pic, MyApplication.iconUrl);
			}
			String time = feedBackList.get(position).get("addtime");
			holder.tv_time.setText(time.substring(time.lastIndexOf("-") - 2,
					time.length()));
			return convertView;
		}
	}

	class Holder {
		RelativeLayout ll_order_feedback_send; // 发送布局
		RelativeLayout ll_order_feedback_receive; // 接收布局
		
		TextView feedback_send_text; // 发送消息
		TextView feedback_receive_text; // 接收消息
		
		TextView tv_time; // 消息时间
		
		ImageView feedback_send_pic; //发送方图片
		ImageView feedback_receive_pic; // 接收方图片
	}

	public void back(View v) {
		finish();
	}

}