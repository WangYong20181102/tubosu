package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.DesignFreePopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.http.HttpPost;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 首页本地优惠装修公司详情页面
 * @author dec
 *
 */
public class LocalDiscountDetailActivity extends Activity implements OnClickListener {
	private static final String TAG = LocalDiscountDetailActivity.class.getSimpleName();
	
	private Context mContext;
	private ImageView free_back;
	
	/**标题栏分享按钮*/
	private ImageView image_share;
	
	/**标题控件*/
	private TextView title_name;
	
	/**页面标题显示的内容*/
	private String title;
	
	/**左下角显示的公司名称*/
	private TextView tv_company_name;
	
	private String companyId;
	
	/**右下角我要报名按钮*/
	private TextView tv_baoming;
	
	/**上部分banner图片下的简述性文字描述*/
	private TextView tv_title;
	
	/**上部分banner图片下的简述性文字描述下面的时间段*/
	private TextView tv_time;
	
	private String activityid;
	
	/***装修公司优惠接口*/
	private String activityUrl = Constant.TOBOSU_URL + "tapp/company/discount";
	
	private HashMap<String, Object> activityParams;
	private ImageView iv_cover;
	private ImageView iv_company_logo;
	
	/** html中所有图片src地址数据集合*/
	private ArrayList<String> allContents;
	
	/**该集合每一个元素都是比较纯净的图片地址和标题文字的集合*/
	private ArrayList<String> allContentDatas;
	
	/**html中的图片最纯净的地址和标题文字的集合*/
	private ArrayList<String> allDatas;
	
	/**本页面装载从html中获取的图片的布局*/
	private LinearLayout ll_contents;
	
	private LinearLayout ll_loading;
	
	/**底部的我要报名*/
	private DesignFreePopupWindow designPopupWindow;
	
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_local_discount_detail);
		
		mContext = LocalDiscountDetailActivity.this;
		
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		
		free_back = (ImageView) findViewById(R.id.free_activity_back);
		image_share = (ImageView) findViewById(R.id.head_right_image_share);
		iv_company_logo = (ImageView) findViewById(R.id.iv_company_logo);
		image_share.setVisibility(View.VISIBLE);
		title_name = (TextView) findViewById(R.id.title_name);
		tv_baoming = (TextView) findViewById(R.id.tv_baoming);
		tv_company_name = (TextView) findViewById(R.id.tv_company_name);
		iv_cover = (ImageView) findViewById(R.id.iv_cover);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		ll_contents = (LinearLayout) findViewById(R.id.ll_contents);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
	}

	private void initData() {
		activityid = getIntent().getStringExtra("activityid");
		activityParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		activityParams.put("activityid", activityid);
		System.out.println(TAG + "---activityid是-->>> "+ activityid+" <<<----");
		requestActivity();
	}

	
	/***
	 * 获取优惠装修公司接口请求
	 */
	private void requestActivity() {
		OKHttpUtil.post(activityUrl, activityParams, new Callback() {
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
							operAcitivity(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	/**
	 * 操作从请求的接口成功返回的数据
	 * @param result
	 * @throws JSONException
	 */
	private void operAcitivity(String result){
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(result);
			if (jsonObject.getInt("error_code") == 0) {
				JSONObject data = jsonObject.getJSONObject("data");
				String contents = data.getString("contents");
				String cover = data.getString("cover"); //封面图片地址
				title = data.getString("title");
				comsimpname = data.getString("comsimpname");
				logosmall = data.getString("logosmall");
				shareUrl = data.getString("shareUrl");
				companyId = data.getString("comid");
				tv_company_name.setText(comsimpname);
				ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_logo, logosmall);
				String starttime = data.getString("starttime").substring(0, data.getString("starttime").lastIndexOf(" "));
				String endtime = data.getString("endtime").substring(0, data.getString("endtime").lastIndexOf(" "));
				if (title != null && title.length() > 0) {
					tv_title.setText("" + title);
					title_name.setText(title);
					ll_loading.setVisibility(View.GONE);
				}
				if (cover != null && cover.length() > 0) {
					ImageLoaderUtil.loadImage(getApplicationContext(), iv_cover, cover);
				}
				tv_time.setText(starttime + " 至  " + endtime);
				
				// 开始抓取从html字符串，并从中抓取图片路径
//				getPicturesFromHtmlString(contents); // FIXME
				getHtml(contents);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 自定义解析html片段
	 */
	private void getHtml(String htmlPiece){
		System.out.println("=====原始->>" + htmlPiece);
		List<String> listData = new ArrayList<String>();
		String httpString = "http";
		
		String rawHtml = htmlPiece.replaceAll("http","#[").replaceAll("g\\\"", "]#").replaceAll("f\\\"", "]#"); 
		
		String[] arrString = rawHtml.split("#");
		for(int i=0,len=arrString.length;i<len;i++){
//			System.out.println("=====图片url->>" + arrString[i]);
			if(arrString[i].contains("[")){
				if(arrString[i].contains(".gi")){
					String urlTemp = httpString + arrString[i].substring(arrString[i].indexOf("[")+1,arrString[i].indexOf("]"))+"f";
					listData.add(urlTemp);
				}else if(arrString[i].contains(".jp")){
					String urlTemp2 = httpString + arrString[i].substring(arrString[i].indexOf("[")+1,arrString[i].indexOf("]"))+"g";
					listData.add(urlTemp2);
				}else if(arrString[i].contains(".pn")){
					String urlTemp3 = httpString + arrString[i].substring(arrString[i].indexOf("[")+1,arrString[i].indexOf("]"))+"g";
					listData.add(urlTemp3);
				}
			}else{
				// 分割的piece没有图片
				String[] textArr = arrString[i].split("<br />"); // 文字分段分割
				for(int j=0,textLen=textArr.length; j<textLen; j++){
					String text = textArr[j].replaceAll("/>", "")
							.replaceAll("<img src=\"", "")
							.replaceAll(" ", "")
							.replaceAll("&nbsp;&nbsp;", "\n")
							.replaceAll("&nbsp;", "\n")
							.replaceAll("<imgstyle=\"_width:true;\"src=\"", "")
							.replaceAll("width=", "")
							.replaceAll("height=", "")
							.replaceAll("align=", "")
							.replaceAll("title=", "")
							.replaceAll("\"", "");

					listData.add(text);
//					System.out.println("--31-->>>>>text：" + text +"<<<<<");
				}
			}
		}
		
		//.replaceAll(" ", "")
		for(int k=0;k<listData.size();k++){
			if(listData.get(k).contains("http")){
				ImageView iv = new ImageView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, DensityUtil.px2dip(this, 10), 0, DensityUtil.px2dip(this, 10));
				iv.setLayoutParams(lp);
				
				ImageLoaderUtil.loadImage(getApplicationContext(), iv, listData.get(k));
				iv.setScaleType(ScaleType.FIT_XY);
				ll_contents.addView(iv);
			}else{
				TextView tv = new TextView(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(0, DensityUtil.px2dip(this, 10), 0, DensityUtil.px2dip(this, 10));
				tv.setLayoutParams(lp);
				tv.setText(listData.get(k));
				tv.setTextColor(getResources().getColor(R.color.color_darkgray));
				tv.setTextSize(15);
				ll_contents.addView(tv);
			}
		}
	}
	
	
	
	
	
	
	/***
	 * 开始抓取从html字符串，并从中抓取图片路径
	 * @param htmlContent 抓取的html页面字符串
	 */
	private void getPicturesFromHtmlString(String htmlContent) {
		if (htmlContent.contains("<img src=\"")) {
			
			// 以<img src="对htmlContent进行分割
			String[] strings = htmlContent.split("<img src=\""); 
			
			// 图片src地址路径集合
			allContents = new ArrayList<String>();
			
			for (int i = 0; i < strings.length; i++) {
				allContents.add(strings[i]);
			}
			
			// 该集合每一个元素都是比较纯净的图片地址
			allContentDatas = new ArrayList<String>();
			
			// 经过这个循环的处理得出allContentDatas存储的都是纯净的图片地址集合
			for (int i = 0; i < allContents.size(); i++) {
				allContentDatas.add(allContents.get(i).replaceAll("<br />", "").replaceAll("/>", "").replaceAll("&nbsp;", "").replace(" ", ""));
			}
			
			// 最纯净的图片地址集合
			allDatas = new ArrayList<String>();
			
			for (int i = 0; i < allContentDatas.size(); i++) {
				if (allContentDatas.get(i).contains("\" ")) { // 是否包含一个 "
					String[] split = allContentDatas.get(i).split("\" "); // allContentDatas中的每一个元素，都以一个"为界，再次分割
					for (int j = 0; j < split.length; j++) {
						if (split[j].length() > 1) { // 如果split数组中有元素，则添加到allatas中
							allDatas.add(split[j]);
						}
					}
				} else {// 不包含一个 " 则意味着本身就是一个图片路径地址
					allDatas.add(allContentDatas.get(i));
				}
				
			}
			
			// 获取图片，加载图upian   获取文字，加载文字
			if (allDatas.size() > 0) {
				ll_contents.setVisibility(View.VISIBLE);
				for (int i = 0; i < allDatas.size(); i++) {
					if (allDatas.get(i).contains("jpg")) { // 这是图片
						ImageView iv = new ImageView(this);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.setMargins(0, DensityUtil.px2dip(this, 20), 0, DensityUtil.px2dip(this, 20));
						iv.setLayoutParams(lp);
						
						ImageLoaderUtil.loadImage(getApplicationContext(), iv, allDatas.get(i));
						iv.setScaleType(ScaleType.FIT_XY);
						ll_contents.addView(iv);
					} else { // 这是文字信息
						TextView tv = new TextView(this);
						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						lp.setMargins(0, DensityUtil.px2dip(this, 20), 0, DensityUtil.px2dip(this, 20));
						tv.setLayoutParams(lp);
						tv.setText(allDatas.get(i));
						tv.setTextColor(getResources().getColor(R.color.color_darkgray));
						tv.setTextSize(15);
						ll_contents.addView(tv);
					}
				}
			}
		}
		
	}

	private void initEvent() {
		free_back.setOnClickListener(this);
		image_share.setOnClickListener(this);
		tv_baoming.setOnClickListener(this);
		tv_company_name.setOnClickListener(this);
		iv_company_logo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_baoming: // 我要报名 弹出窗口
			designPopupWindow = new DesignFreePopupWindow(LocalDiscountDetailActivity.this, itemsClick);
			designPopupWindow.dialog_freedesign_title.setText("我要报名");
			designPopupWindow.showAtLocation(findViewById(R.id.tv_baoming), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.free_activity_back: //返回
			finish();
			break;
		case R.id.head_right_image_share: // 标题栏分享按钮
//			Log.d(TAG, "--分享的接口是--【"+shareUrl+"】");
//			Log.d(TAG, "显示标题内容是：" + title);
//			Log.d(TAG, "优惠地址是：" + shareUrl);
//			Log.d(TAG, "分享的url是：" +shareUrl);
			new ShareUtil(LocalDiscountDetailActivity.this, image_share, title, shareUrl, shareUrl);
			break;
		case R.id.iv_company_logo:
		case R.id.tv_company_name:
			if(!"".equals(companyId)){
				Intent intent = new Intent();
				intent.setClass(mContext, DecorateCompanyDetailActivity.class);
				intent.putExtra("comid", companyId);
//				Log.d(TAG, "公司id是" + companyId);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	/***
	 * 我要报名点击事件
	 */
	private OnClickListener itemsClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.dialog_freedesign_submit: // 弹窗中的确定输入电话号码按钮
				phone = ((DesignFreePopupWindow) designPopupWindow).getPhone();
				if (judgePhone(phone)) {
					ll_loading.setVisibility(View.VISIBLE);
					designPopupWindow.dismiss();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("cellphone", phone);
					map.put("comeurl", Constant.PIPE);
					map.put("urlhistory", Constant.PIPE);
					map.put("city", getSharedPreferences("Save_City_Info", MODE_PRIVATE).getString("save_city_now", ""));
					String userid = "";
					if (!TextUtils.isEmpty(userid)) {
						map.put("userid", userid);
					} else {
						map.put("userid", "0"); // 0是没有注册，没有用户名的 userid标记
					}
					map.put("source", "922"); // 922是发单入口标记
					new SendMessageThread(map).start();

				}

				break;
			default:
				break;
			}
		}

	};
	
	/***
	 * 判断电话号码是否合法
	 * @param phoneNum
	 * @return
	 */
	public boolean judgePhone(String phoneNum) {
		if(TextUtils.isEmpty(phoneNum)){
			ToastUtil.showShort(getApplicationContext(), "联系电话不能为空");
			return false;
		}else{
			String MOBILE = "^1(3[0-9]|5[0-35-9]|7[0136-8]|8[0-9])\\d{8}$";
			Pattern pattern = Pattern.compile(MOBILE);
	        Matcher matcher = pattern.matcher(phoneNum); 
	        boolean flag = matcher.matches();
	        if(!flag){
	        	ToastUtil.showShort(getApplicationContext(), "请输入合法电话号码");
	        }
	        return matcher.matches();
		}
	};
	

	/**
	 * 发单接口  请求网络
	 * @author dec
	 *
	 */
	class SendMessageThread extends Thread {

		private HashMap<String, String> map;

		public SendMessageThread(HashMap<String, String> map) {
			this.map = map;
		}

		@Override
		public void run() {
			String result = HttpPost.doPost(Constant.TOBOSU_URL + "tapi/order/pub_order?", map, "utf-8");
			try {
				if (result != null) {
					JSONObject object = new JSONObject(result);
					Message msg = new Message();
					msg.what = 0;
					msg.obj = object;
					designHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	private Handler designHandler = new Handler() {
		// 在Handler中获取消息，重写handleMessage()方法
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				ll_loading.setVisibility(View.GONE);
				try {
					JSONObject object = (JSONObject) msg.obj;
					String info = object.getString("msg");
					int result = object.getInt("error_code");
					if (result == 0) {
						Intent intent = new Intent(LocalDiscountDetailActivity.this, ApplyforSuccessActivity.class);
						intent.putExtra("phone", phone);
						startActivity(intent);
						Toast.makeText(LocalDiscountDetailActivity.this, info, Toast.LENGTH_SHORT).show();
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
	};
	
	private String comsimpname;
	private String logosmall;
	private String shareUrl;

}