package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.DesignFreePopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;
import com.umeng.socialize.utils.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 效果精选 详情
 * @author dec
 *
 */
public class SelectedImageDetailActivity extends Activity implements IXListViewListener {
	private RelativeLayout  rel_full_selected_bar;
	private LinearLayout full_selected_images_head_linearlayout;
	private ImageView full_selected_images_detail_back_ ;
	
	/**精选图片listview*/
	private XListView lv_imagecheckness_detail;
	
	/**顶部布局*/
	private View headView;
	private String _id;
	
	/**精选详情接口*/
	private String spcialDetailUrl = Constant.TOBOSU_URL + "tapp/util/spcial_detail";
	
	/**发单接口*/
	private String pubOrderUrl = Constant.TOBOSU_URL + "tapi/order/pub_order";
	
	private RequestParams spcialDetailParams;
	private String title;
	private String desc;
	private String fengxiang;
	private ArrayList<String> clickableList;
	private ArrayList<String> imageDatas;
	private ArrayList<String> textList;
	
	/**精选照片适配器*/
	private SelectedImageDetailAdapter adapter;
	
	private TextView tv_full_selected_detail_content;
	private TextView tv_desc;
	
	/**为我家设计按钮*/
	private ImageView applyfor_submit;
	
	/**为我设计window*/
	private DesignFreePopupWindow designPopupWindow;
	private HashMap<String, Object> pubOrderParams;
	
	private String userid;
	
	/**popwindow上分享朋友圈分享的按钮*/
	private ImageView detail_share;
	
	/**顶部右上角的分享按钮*/
	private ImageView full_selected_images_detail_share_;
	
	/**加载更多时的布局*/
	private LinearLayout ll_loading;
	private String phone;
	
	private String direction;
	private Context mContext;
	
	private FrameLayout framelayout_bar;
	
	private RelativeLayout rel_selected_image_details_layout;
	
	private RelativeLayout rel_no_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AppInfoUtil.setSelectedImageDetailActivityTheme(this);
		setContentView(R.layout.activity_selected_image_detail);
		mContext =  SelectedImageDetailActivity.this;
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		framelayout_bar = (FrameLayout) findViewById(R.id.framelayout_bar);
		framelayout_bar.bringToFront();
		lv_imagecheckness_detail = (XListView) findViewById(R.id.lv_imagecheckness_detail);
		lv_imagecheckness_detail.setPullLoadEnable(true);
		lv_imagecheckness_detail.setXListViewListener(this);
		lv_imagecheckness_detail.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lv_imagecheckness_detail.mFooterView.setVisibility(View.GONE);
		lv_imagecheckness_detail.mHeaderView.setVisibility(View.GONE);
		applyfor_submit = (ImageView) findViewById(R.id.applyfor_submit);
		
		rel_selected_image_details_layout = (RelativeLayout) findViewById(R.id.rel_selected_image_details_layout);
		rel_no_data = (RelativeLayout) findViewById(R.id.rel_no_data);
		
		//精选页面详情头部布局
		headView = getLayoutInflater().inflate(R.layout.headview_imagecheckness_detail, null);
		tv_full_selected_detail_content = (TextView) headView.findViewById(R.id.tv_full_selected_detail_content);
		tv_desc = (TextView) headView.findViewById(R.id.tv_desc);
		
		
		/*----原来back share是在headView中的, 现在改为本页面中的------*/
//		full_selected_images_detail_back = (ImageView) headView.findViewById(R.id.full_selected_images_detail_back);
//		full_selected_images_detail_share = (ImageView) headView.findViewById(R.id.full_selected_images_detail_share);
//		rel_full_selected_images = (RelativeLayout)  headView.findViewById(R.id.rel_full_selected_images);
		rel_full_selected_bar = (RelativeLayout) findViewById(R.id.rel_full_selected_bar);
		full_selected_images_detail_back_ = (ImageView) findViewById(R.id.full_selected_images_detail_back);
		full_selected_images_detail_share_ = (ImageView) findViewById(R.id.full_selected_images_detail_share);
		/*----------------*/
		full_selected_images_head_linearlayout = (LinearLayout) headView.findViewById(R.id.full_selected_images_head_linearlayout);
		lv_imagecheckness_detail.addHeaderView(headView);
		
		
		detail_share = (ImageView) findViewById(R.id.full_selected_detail_share);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading_home);}

	private void initData() {
		clickableList = new ArrayList<String>();
		imageDatas = new ArrayList<String>();
		textList = new ArrayList<String>();
		_id = getIntent().getStringExtra("id");
		
		if(Constant.checkNetwork(mContext)){
			requestForDatas();
			rel_selected_image_details_layout.setVisibility(View.VISIBLE);
			rel_no_data.setVisibility(View.GONE);
			adapter = new SelectedImageDetailAdapter(mContext,imageDatas,textList);
			lv_imagecheckness_detail.setAdapter(adapter);
			lv_imagecheckness_detail.setSelector(new ColorDrawable(Color.TRANSPARENT));
			
		}else{
			Toast.makeText(mContext, "请检查网络~", Toast.LENGTH_SHORT).show();
		}
		
	}

	private void requestForDatas() {
		spcialDetailParams = new RequestParams();
		spcialDetailParams.addQueryStringParameter("id", _id);
		spcialDetailParams.addQueryStringParameter("version", AppInfoUtil.getAppVersionName(getApplicationContext()));
		spcialDetailParams.addQueryStringParameter("device", AppInfoUtil.getDeviceName());
		spcialDetailParams.addQueryStringParameter("city", AppInfoUtil.getCityName(getApplicationContext()));
		spcialDetailParams.addQueryStringParameter("lat", AppInfoUtil.getLat(getApplicationContext()));
		spcialDetailParams.addQueryStringParameter("lng", AppInfoUtil.getLng(getApplicationContext()));
		
		if (!TextUtils.isEmpty(direction)) {
			spcialDetailParams.addQueryStringParameter("direction", direction);
		}
		
		HttpUtils httpUtils = new HttpUtils(10000);
		httpUtils.configCurrentHttpCacheExpiry(5000);
		
		// 精选接口请求网络
		httpUtils.send(HttpMethod.GET, spcialDetailUrl, spcialDetailParams, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException e, String string) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				String result = info.result;
				Log.d("result", result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					Log.d("result", result);
					if (jsonObject.getInt("error_code") == 0) {
						JSONObject data = jsonObject.getJSONObject("data");
						title = data.getString("banner_title");
						desc = data.getString("banner_description");
						fengxiang = data.getString("fengxiang");
						_id = data.getString("id");
						
						JSONArray jsonArray = data.getJSONArray("img");
						imageDatas.clear();
						for (int i = 0; i < jsonArray.length(); i++) {
							imageDatas.add(jsonArray.getString(i));
						}
						
						JSONArray txtJsonArray = data.getJSONArray("wordList");
						textList.clear();
						for (int i = 0; i < jsonArray.length(); i++) {
							textList.add(txtJsonArray.getString(i));
						}
						
						JSONArray clickableArray = data.getJSONArray("isClickList");
						clickableList.clear();
						for (int i = 0; i < clickableArray.length(); i++) {
							clickableList.add(clickableArray.getString(i));
						}
						
						tv_full_selected_detail_content.setText(title);
						tv_desc.setText(desc);
						onLoad();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	private void initData1(){
		if(imageDatas!=null && imageDatas.size()>0 && textList!=null && textList.size()>0){
			rel_selected_image_details_layout.setVisibility(View.VISIBLE);
			rel_no_data.setVisibility(View.GONE);
			adapter = new SelectedImageDetailAdapter(mContext,imageDatas,textList);
			lv_imagecheckness_detail.setAdapter(adapter);
			lv_imagecheckness_detail.setSelector(new ColorDrawable(Color.TRANSPARENT));
			lv_imagecheckness_detail.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if("1".equals(clickableList.get(position-1))){
						String url = imageDatas.get(position-1);
						Bundle bundle = new Bundle();
						bundle.putString("url", url);
						bundle.putString("id", ""); 
						Intent intent = new Intent(mContext, ImageDetailActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if("0".equals(clickableList.get(position-1))){
						Toast.makeText(mContext, "无相关装修图", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			});
		}else if(imageDatas!=null && imageDatas.size()==0){
			rel_selected_image_details_layout.setVisibility(View.GONE);
			rel_no_data.setVisibility(View.VISIBLE);
		}
	}

	
	private void initEvent() {
		rel_full_selected_bar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 什么都不用做
			}
		});
		
		full_selected_images_head_linearlayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 什么都不用做
			}
		});
		// 返回
		full_selected_images_detail_back_.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		// 为我家设计
		applyfor_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				designPopupWindow = new DesignFreePopupWindow(mContext, itemsClick);
				designPopupWindow.showAtLocation(findViewById(R.id.applyfor_submit), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});
		
		// 分享朋友圈
		detail_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ShareUtil(mContext, detail_share, title, desc, fengxiang);
			}
		});
		
		// 分享
		full_selected_images_detail_share_.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new ShareUtil(mContext, detail_share, title, desc, fengxiang);
			}
		});

	}

	
	/***
	 * 精选照片适配器
	 * @author dec
	 *
	 */
	class SelectedImageDetailAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<String> imgDataList;
		private ArrayList<String> textDataList;
		
		public SelectedImageDetailAdapter(Context context, ArrayList<String> imgDataList,ArrayList<String> textDataList){
			this.context = context;
			this.imgDataList = imgDataList;
			this.textDataList = textDataList;
		}
		

		@Override
		public int getCount() {
			if (imgDataList != null) {
				return imgDataList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ImageHolder holder;
			if (convertView == null) {
				holder = new ImageHolder();
				convertView = getLayoutInflater().inflate(R.layout.item_image_checkness_listview, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.detail_iv_selected_imageview);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_selected_imageview_text);
				convertView.setTag(holder);
			}else{
				holder = (ImageHolder) convertView.getTag();
			}
			// 加载图片的类和方法
			ImageLoaderUtil.loadImage(context, holder.iv, imgDataList.get(position));
			holder.tv.setText(textDataList.get(position));
			
			holder.iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					System.out.println("SelectedImageDetailActivity - imageDatas长度"+imageDatas.size()+"*** position"+position);
//					System.out.println("SelectedImageDetailActivity - clickableList长度"+imageDatas.size());
					if("1".equals(clickableList.get(position))){
						String url = imageDatas.get(position);
						Bundle bundle = new Bundle();
						bundle.putString("url", url);
						bundle.putString("id", ""); 
						Intent intent = new Intent(mContext, ImageDetailActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}else if("0".equals(clickableList.get(position))){
						Toast.makeText(mContext, "无相关装修图", Toast.LENGTH_SHORT).show();
						return;
					}
				}
			});
			
			return convertView;
		}

	}
	
	class ImageHolder{
		ImageView iv;
		TextView tv;
	}

	/***
	 * 为弹出窗口实现监听类
	 */
	private OnClickListener itemsClick = new OnClickListener() {
		
		public void onClick(View v) {

			designPopupWindow.dismiss();
			pubOrderParams = new HashMap<String,Object>();
			userid = getSharedPreferences("userInfo", 0).getString("userid", "");
			
			switch (v.getId()) {
			case R.id.dialog_freedesign_submit: // 免费预约确定按钮
				phone = ((DesignFreePopupWindow) designPopupWindow).getPhone();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(getApplicationContext(), "联系电话不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				pubOrderParams.put("cellphone", phone);
				pubOrderParams.put("comeurl", "http://m.tobosu.com/?channel=product&subchannel=android&chcode=android");
				pubOrderParams.put("source", "894");
				pubOrderParams.put("device", AppInfoUtil.getDeviceName());
				pubOrderParams.put("version", AppInfoUtil.getAppVersionName(getApplicationContext()));
				pubOrderParams.put("city", AppInfoUtil.getCityName(getApplicationContext()));
				pubOrderParams.put("lat", AppInfoUtil.getLat(getApplicationContext()));
				pubOrderParams.put("lng", AppInfoUtil.getLng(getApplicationContext()));
				
				if (!TextUtils.isEmpty(userid)) {
					pubOrderParams.put("userid", userid);
				} else {
					pubOrderParams.put("userid", "0");
				}
				
				requestPubOrder();
				break;
			default:
				break;
			}
		}
	};

	
	/****
	 * 输入电话号码发单接口请求
	 */
	private void requestPubOrder() {
		OKHttpUtil.post(pubOrderUrl, pubOrderParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String result = response.body().string();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getInt("error_code") == 0) {
								Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
								intent.putExtra("phone", phone);
								startActivity(intent);
							} else {
								Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	@Override
	public void onRefresh() {
		direction = "1";
		requestForDatas();
		lv_imagecheckness_detail.stopRefresh();
		lv_imagecheckness_detail.stopLoadMore();
	}

	@Override
	public void onLoadMore() {
		direction = "2";
		requestForDatas();
		lv_imagecheckness_detail.stopRefresh();
		lv_imagecheckness_detail.stopLoadMore();
	}

	private void onLoad() {
		if(adapter!=null){
			adapter.notifyDataSetChanged();
			ll_loading.setVisibility(View.GONE);
		}
	}


}