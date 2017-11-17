package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.BaoPopupWindow;
import com.tbs.tobosutype.customview.BusinessLicensePopupWindow;
import com.tbs.tobosutype.customview.CallDialogCompany;
import com.tbs.tobosutype.customview.CompanyImagePopupWindow;
import com.tbs.tobosutype.customview.CustomGridView;
import com.tbs.tobosutype.customview.DialogShowText;
import com.tbs.tobosutype.customview.ScrollViewExtend;
import com.tbs.tobosutype.customview.ScrollViewExtend.OnScrollChangedListener;
import com.tbs.tobosutype.customview.VouchersPopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class DecorateCompanyDetailActivity extends Activity implements OnClickListener {
	private Context mContext;
	private TextView tv_designer_num;
	private TextView tv_images_num;
	private TextView tv_price;
	private String companyId;
	private static final String HAS_DISCOUNT_ACTIVITY = "1";
	private static final String NO_DISCOUNT_ACTIVITY = "0";
	private static final String HAS_VERIFIED = "1";
	private static final String NO_VERIFIED = "0";
	private static final String HAS_INSURANCE = "1";
	private static final String NO_INSURANCE = "0";
	private ImageView ivBack;
	private ImageView decorate_detail_share;
	private ImageView decorate_back;
	private String comid;
	private ImageView decorate_detail_fav;
	private RelativeLayout decorate_detail_phonelayout;
	private ImageView decorate_detail_company_logo;
	private TextView decorate_detail_company;
	private TextView decorate_detail_address;
	private TextView decorate_detail_phonenum;
	private ImageView decorate_detail_business_license;
	private ImageView decorate_detail_bao;
	private ImageView decorate_detail_vouchers;
	
	/***装修公司简介文字*/
	private TextView decorate_detail_introduce;
	
	/***底部右边免费预约*/
	private TextView decorate_detail_freeyuyue;
	
	/***底部电话联系方式*/
	private TextView decorate_detail_cellphone;
	
	/**家居装修gridview*/
	private CustomGridView decorate_detail_gridView_family;
	
	/**无家居装修服务项目text*/
	private TextView decorate_detail_family_empty;
	
	/**工厂装修gridview*/
	private CustomGridView decorate_detail_gridView_factory;
	
	/**无工厂装修服务项目text*/
	private TextView decorate_detail_factory_empty;
	
	/**底部联系方式布局*/
	private RelativeLayout decorate_detail_bottom;
	
	/**设计图册布局*/
	private LinearLayout ll_company_detail_design_chart;
	
	/**设计图数量*/
	private TextView tv_design_chart_count;
	private ImageView iv_design_chart1;
	private ImageView iv_design_chart2;
	private ImageView iv_design_chart3;

	private LinearLayout ll_company_image;
	private ImageView iv_company_image1;
	private ImageView iv_company_image2;
	private ImageView iv_company_image3;
	
	/**公司介绍图片的数目*/
	private TextView tv_company_image_count;
	
	/**QQ联系方式*/
	private TextView tv_qq;
	
	/***装修一平米送一平米布局*/
	private RelativeLayout ll_activity;
	
	private String tel = "";
	private String cellphone = "";
	
	/**展开公司简介flag*/
	private boolean flag = true;
	private Window window;
	private android.view.WindowManager.LayoutParams params;
	private String token;
	private String dataUrl = Constant.TOBOSU_URL + "tapp/company/company_detail";
	private HashMap<String, Object> companyDetailParams;
	private String oper_type = "1";
	private String memberdegree = "1";
	private ScrollViewExtend scrollView;
	private TextView textView8;
	private LinearLayout ll_certification_services;
	private LinearLayout ll_loading;
	private TextView tv_activity_title;
	private List<String> company_images;
	private List<String> design_charts;
	private String qq;
	private String title;
	private String fenxiang_url;
	private String hav_fav = "0";
	
	/**添加或取消收藏*/
	private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";
	
	
	/**顶部bar*/
	private RelativeLayout rel_layout_decorate_bar;
	private FrameLayout framelayoutCompany;
	private View oldVueiew;
	private View decorateVueiew;
	private LinearLayout imageLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_decorate_company_detail);
		mContext = DecorateCompanyDetailActivity.this;
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		framelayoutCompany = (FrameLayout) findViewById(R.id.framelayoutCompany);
		framelayoutCompany.bringToFront();
		rel_layout_decorate_bar = (RelativeLayout) findViewById(R.id.rel_layout_decorate_bar);
		oldVueiew = (View) findViewById(R.id.oldVueiew);
		oldVueiew.setFocusable(true);
		oldVueiew.setFocusableInTouchMode(true);
		oldVueiew.requestFocus();
		oldVueiew.setVisibility(View.VISIBLE);

		decorateVueiew = (View) findViewById(R.id.decorateVueiew);
		decorateVueiew.setVisibility(View.INVISIBLE);
		scrollView = (ScrollViewExtend) findViewById(R.id.scrollView_decorate_company_details);
		scrollView.setOnScrollChangedListener(scrollChangedListener);
		if(scrollView.getScrollY() == 0){
			oldVueiew.setVisibility(View.VISIBLE);
			decorateVueiew.setVisibility(View.INVISIBLE);
		}
		scrollView.smoothScrollTo(1,1);
		decorate_detail_fav = (ImageView) findViewById(R.id.decorate_detail_fav);
		tv_qq = (TextView) findViewById(R.id.tv_qq);
		decorate_detail_company_logo = (ImageView) findViewById(R.id.decorate_detail_company_logo);
		decorate_detail_company = (TextView) findViewById(R.id.decorate_detail_company);
		ll_activity = (RelativeLayout) findViewById(R.id.ll_activity);
		tv_activity_title = (TextView) findViewById(R.id.tv_activity_title);
		decorate_detail_phonelayout = (RelativeLayout) findViewById(R.id.decorate_detail_phonelayout);
		decorate_detail_share = (ImageView) findViewById(R.id.find_decorate_detail_share);
		decorate_detail_business_license = (ImageView) findViewById(R.id.decorate_detail_business_license);
		decorate_detail_bao = (ImageView) findViewById(R.id.decorate_detail_bao);
		decorate_detail_vouchers = (ImageView) findViewById(R.id.decorate_detail_vouchers);
		decorate_detail_address = (TextView) findViewById(R.id.decorate_detail_address);
		decorate_detail_phonenum = (TextView) findViewById(R.id.decorate_detail_phonenum);
		decorate_detail_introduce = (TextView) findViewById(R.id.decorate_detail_introduce);
		decorate_detail_gridView_family = (CustomGridView) findViewById(R.id.decorate_detail_gridView_family);
		decorate_detail_gridView_factory = (CustomGridView) findViewById(R.id.decorate_detail_gridView_factory);
		decorate_detail_gridView_factory.setFocusable(false);
		decorate_detail_family_empty = (TextView) findViewById(R.id.decorate_detail_family_empty);
		decorate_detail_factory_empty = (TextView) findViewById(R.id.decorate_detail_factory_empty);
		decorate_detail_introduce = (TextView) findViewById(R.id.decorate_detail_introduce);
		decorate_detail_freeyuyue = (TextView) findViewById(R.id.decorate_detail_freeyuyue);
		decorate_detail_cellphone = (TextView) findViewById(R.id.decorate_detail_cellphone);
		decorate_detail_bottom = (RelativeLayout) findViewById(R.id.decorate_detail_bottom);
		ll_company_image = (LinearLayout) findViewById(R.id.ll_company_image);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		iv_company_image1 = (ImageView) findViewById(R.id.iv_company_image1);
		iv_company_image2 = (ImageView) findViewById(R.id.iv_company_image2);
		iv_company_image3 = (ImageView) findViewById(R.id.iv_company_image3);
		tv_company_image_count = (TextView) findViewById(R.id.tv_company_image_count);
		ll_company_detail_design_chart = (LinearLayout) findViewById(R.id.ll_company_detail_design_chart);
		iv_design_chart1 = (ImageView) findViewById(R.id.iv_design_chart1);
		iv_design_chart2 = (ImageView) findViewById(R.id.iv_design_chart2);
		iv_design_chart3 = (ImageView) findViewById(R.id.iv_design_chart3);
		tv_design_chart_count = (TextView) findViewById(R.id.tv_design_chart_count);
		textView8 = (TextView) findViewById(R.id.textView8);
		ll_certification_services = (LinearLayout) findViewById(R.id.ll_certification_services);
		decorate_back = (ImageView) findViewById(R.id.decorate_goback);
		tv_designer_num = (TextView) findViewById(R.id.tv_designer_num);
		tv_images_num = (TextView) findViewById(R.id.tv_images_num);
		tv_price = (TextView) findViewById(R.id.tv_price);
		imageLayout = (LinearLayout) findViewById(R.id.ll_image_laylout);
		ivBack = (ImageView) findViewById(R.id.decorate_goback);
	}

	private void initData() {
		comid = getIntent().getExtras().getString("comid");
		Util.setLog("DecorateCompanyDetailActivity", "songchengcai获取到" + comid);
		token = AppInfoUtil.getToekn(getApplicationContext());
		company_images = new ArrayList<String>();
		companyDetailParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		companyDetailParams.put("id", comid);
		companyDetailParams.put("token", token);
		requestCompanyDetailPost();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		token = AppInfoUtil.getToekn(getApplicationContext());
	}

	private void requestCompanyDetailPost() {

		OKHttpUtil.post(dataUrl, companyDetailParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				final String result = response.body().string();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						//解析json
						HashMap<String, Object> companyList = jsonToLoginCompanyList(result);

						if(companyList!=null){
							decorate_detail_company.setText(companyList.get("comname") + "");
							companyId = comid;
							title = companyList.get("comname") + "";
							fenxiang_url = companyList.get("fxurl") + "";
							tel = companyList.get("tel") + "";
							cellphone = companyList.get("cellphone") + "";
							if (cellphone != null && cellphone.length() > 0) {
								decorate_detail_phonenum.setText(tel + " / " + cellphone);
							} else {
								decorate_detail_phonenum.setText(tel);
							}
							cellphone = companyList.get("cellphone") + "";
							memberdegree = companyList.get("memberdegree") + "";
							//
							initCertification(memberdegree);
							decorate_detail_address.setText(companyList.get("address") + "");
							decorate_detail_introduce.setText(companyList.get("intro") + "");
							String logoSmallUrl = companyList.get("logosmall") + "";
							ImageLoaderUtil.loadImage(getApplicationContext(), decorate_detail_company_logo, logoSmallUrl);
							String jjb_logo = companyList.get("jjb_logo") + "";
							String certification = companyList.get("certification") + "";

							if (HAS_INSURANCE.equals(jjb_logo)) {
								decorate_detail_bao.setVisibility(View.VISIBLE);
							} else {
								decorate_detail_bao.setVisibility(View.GONE);
							}
							if (HAS_VERIFIED.equals(certification)) {
								decorate_detail_business_license.setVisibility(View.VISIBLE);
							} else {
								decorate_detail_business_license.setVisibility(View.GONE);
							}

							hav_fav = companyList.get("hav_fav") + "";
							if ("0".equals(hav_fav)) {
								decorate_detail_fav.setImageResource(R.drawable.fa02);
								oper_type = "1";
							} else {
								decorate_detail_fav.setImageResource(R.drawable.fa_red);
								oper_type = "0";
							}

							List familyList = (List) companyList.get("familyList");
							ArrayAdapter<String> familyAdapter = new ArrayAdapter<String>(mContext, R.layout.item_decorate_detail_gridview, familyList);
							decorate_detail_gridView_family.setAdapter(familyAdapter);
							decorate_detail_gridView_family.setEmptyView(decorate_detail_family_empty);
							decorate_detail_gridView_family.setSelector(new ColorDrawable(Color.TRANSPARENT));

							List factoryList = (List) companyList.get("factoryList");
							ArrayAdapter<String> factoryAdapter = new ArrayAdapter<String>(mContext, R.layout.item_decorate_detail_gridview, factoryList);
							decorate_detail_gridView_factory.setSelector(new ColorDrawable(Color.TRANSPARENT));
							decorate_detail_gridView_factory.setAdapter(factoryAdapter);
							decorate_detail_gridView_factory.setEmptyView(decorate_detail_factory_empty);

						}
					}
				});
			}
		});

		OKHttpUtil.post(dataUrl, companyDetailParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {

			}
		});
	}

	
	private void initCertification(String memberdegree) {
		ll_loading.setVisibility(View.GONE);
		decorate_detail_bottom.setVisibility(View.VISIBLE);
		decorate_detail_bottom.setVisibility(View.VISIBLE);
		int degeree = Integer.parseInt(memberdegree);
		if (degeree > 1) {
			textView8.setVisibility(View.VISIBLE);
			ll_certification_services.setVisibility(View.VISIBLE);
		} else {
			textView8.setVisibility(View.GONE);
			ll_certification_services.setVisibility(View.GONE);
		}
	}

	private void getCompanyQQ(JSONObject object2) throws JSONException {
		qq = object2.getString("qq");
		if (qq != null && qq.length() > 0) {
			tv_qq.setVisibility(View.VISIBLE);
			tv_qq.setOnClickListener(this);
		} else {
			tv_qq.setVisibility(View.GONE);
		}
	}
	private void goPreferentialActivity(final JSONObject object2) throws JSONException {
		if (HAS_DISCOUNT_ACTIVITY.equals(object2.getString("activity"))) {
			ll_activity.setVisibility(View.VISIBLE);
			tv_activity_title.setText(object2.getString("activityTitle"));
			tv_activity_title.setClickable(true);
			tv_activity_title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						Intent intent = new Intent(mContext,LocalDiscountDetailActivity.class);
						intent.putExtra("activityid", object2.getString("activityid"));
						startActivity(intent);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			ll_activity.setVisibility(View.GONE);
		}
	}

	private void showCompanyDesignCharts(JSONObject object2) throws JSONException {
		design_charts = new ArrayList<String>();
		String imsCount = object2.getString("imsCount");
		JSONArray threeImsList = object2.getJSONArray("threeImsList");
		int chart_length = threeImsList.length();
		for (int i = 0; i < chart_length; i++) {
			design_charts.add(threeImsList.getJSONObject(i).getString("index_image_url"));
		}
		if (design_charts != null && design_charts.size() == 3) {
			ll_company_detail_design_chart.setVisibility(View.VISIBLE);
			tv_design_chart_count.setText("共 " + imsCount + " 套");
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart1, design_charts.get(0));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart2, design_charts.get(1));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart3, design_charts.get(2));
		} else if (design_charts != null && design_charts.size() == 2) {
			ll_company_detail_design_chart.setVisibility(View.VISIBLE);
			tv_design_chart_count.setText("共 " + imsCount + " 套");
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart1, design_charts.get(0));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart2, design_charts.get(1));
		} else if (design_charts != null && design_charts.size() == 1) {
			ll_company_detail_design_chart.setVisibility(View.VISIBLE);
			tv_design_chart_count.setText("共 " + imsCount + " 套");
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_design_chart1, design_charts.get(0));
		} else {
			ll_company_detail_design_chart.setVisibility(View.GONE);
		}
	}
	private void showCompanyDescriptionImg(JSONObject object2) throws JSONException {
		JSONArray show_img = object2.getJSONArray("show_img");
		company_images.clear();
		for (int i = 0; i < show_img.length(); i++) {
			company_images.add(show_img.getJSONObject(i).getString("url"));
		}
		
		// 多于3张则仅显示3张
		if (company_images.size() >= 3) {
			ll_company_image.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image1, company_images.get(0));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image2, company_images.get(1));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image3, company_images.get(2));
			tv_company_image_count.setText("共" + company_images.size() + "张");
			iv_company_image1.setOnClickListener(this);
			iv_company_image2.setOnClickListener(this);
			iv_company_image3.setOnClickListener(this);

		} else if (company_images.size() == 2) {
			ll_company_image.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image1, company_images.get(0));
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image2, company_images.get(1));
			tv_company_image_count.setText("共" + company_images.size() + "张");
			iv_company_image1.setOnClickListener(this);
			iv_company_image2.setOnClickListener(this);

		} else if (company_images.size() == 1) {
			ll_company_image.setVisibility(View.VISIBLE);
			ImageLoaderUtil.loadImage(getApplicationContext(), iv_company_image1, company_images.get(0));
			tv_company_image_count.setText("共" + company_images.size() + "张");
			iv_company_image1.setOnClickListener(this);
		} else {
			ll_company_image.setVisibility(View.GONE);
		}
	}

	private HashMap<String, Object> jsonToLoginCompanyList(String jsonString) {

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject object = new JSONObject(jsonString);
			JSONObject object2 = object.getJSONObject("data");
			String comid = object2.getString("comid");
			if (!TextUtils.isEmpty(token)) {
				hav_fav = object2.getString("hav_fav");
			} else {
				hav_fav = "0";
			}
			String logosmall = object2.getString("logosmall");
			String comname = object2.getString("comname");
			String address = object2.getString("address");
			String fxurl = object2.getString("fxurl");
			String memberdegree = object2.getString("memberdegree"); // 会员等级
			String tel = object2.getString("tel");
			String cellphone = object2.getString("cellphone");
			String lng = object2.getString("lng");
			String lat = object2.getString("lat");
			String jjb_logo = object2.getString("jjb_logo");
			String certification = object2.getString("certification");
			String intro = object2.getString("intro");

			tv_designer_num.setText(object2.getString("designer_count"));
			tv_images_num.setText(object2.getString("suite_count"));
			tv_price.setText(object2.getString("half_plan_price"));

			// 家居装修
			JSONArray familyArray = object2.getJSONArray("home");
			List<String> familyList = new ArrayList<String>();
			for (int i = 0; i < familyArray.length(); i++) {
				familyList.add((String) familyArray.get(i));
			}

			// 工厂装修
			JSONArray factoryArray = object2.getJSONArray("industry");
			List<String> factoryList = new ArrayList<String>();
			for (int i = 0; i < factoryArray.length(); i++) {
				factoryList.add((String) factoryArray.get(i));
			}
			
			showCompanyDescriptionImg(object2);
			showCompanyDesignCharts(object2);
			goPreferentialActivity(object2);
			getCompanyQQ(object2);
			map.put("comid", comid);
			map.put("hav_fav", hav_fav);
			map.put("logosmall", logosmall);
			map.put("comname", comname);
			map.put("address", address);
			map.put("fxurl", fxurl);
			map.put("memberdegree", memberdegree);
			map.put("tel", tel);
			map.put("cellphone", cellphone);
			map.put("lng", lng);
			map.put("lat", lat);
			map.put("jjb_logo", jjb_logo);
			map.put("certification", certification);
			map.put("intro", intro);
			map.put("familyList", familyList); // 添加家装
			map.put("factoryList", factoryList); // 添加工装
			return map;

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private OnScrollChangedListener scrollChangedListener = new OnScrollChangedListener() {
		
		@Override
		public void onScrollChanged(ScrollView who, int x, int y, int oldx, int oldy) {
			int temp = 1;
			if(y>294){
				temp = 294;
			}else {
				temp = y;
			}
			float f = new BigDecimal((float)temp/294).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			rel_layout_decorate_bar.setAlpha(1);
			decorateVueiew.setAlpha(f);
			if(y==0 && y <144){
//				decorate_detail_fav.setBackgroundResource(R.drawable.fa_white);
				decorate_detail_share.setBackgroundResource(R.drawable.zh02);
				ivBack.setBackgroundResource(R.drawable.activity_back);
				oldVueiew.setVisibility(View.VISIBLE);
				decorateVueiew.setVisibility(View.INVISIBLE);
			}
			if(y > 144){
				//  超过多少 显示黑色
//				decorate_detail_fav.setBackgroundResource(R.drawable.fa_red);
				decorate_detail_share.setBackgroundResource(R.drawable.zh02);
				ivBack.setBackgroundResource(R.drawable.activity_back);
				oldVueiew.setVisibility(View.INVISIBLE);
				decorateVueiew.setVisibility(View.VISIBLE);
			}
		}
	};


	private void initEvent() {
		decorate_detail_bao.setOnClickListener(this);
		decorate_detail_vouchers.setOnClickListener(this);
		iv_design_chart1.setOnClickListener(this);
		iv_design_chart2.setOnClickListener(this);
		iv_design_chart3.setOnClickListener(this);
		tv_design_chart_count.setOnClickListener(this);
		tv_company_image_count.setOnClickListener(this);
		decorate_back.setOnClickListener(this);
		decorate_detail_business_license.setOnClickListener(this);
		decorate_detail_freeyuyue.setOnClickListener(this);
		decorate_detail_cellphone.setOnClickListener(this);
		decorate_detail_phonelayout.setOnClickListener(this);
		decorate_detail_introduce.setOnClickListener(this);
		decorate_detail_fav.setOnClickListener(this);
		decorate_detail_share.setOnClickListener(this);
		imageLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.decorate_detail_business_license:
			BusinessLicensePopupWindow businessLicensePopupWindow = new BusinessLicensePopupWindow(getApplicationContext());
			businessLicensePopupWindow.showAtLocation(
					findViewById(R.id.decorate_detail_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.decorate_goback:
			finish();
			break;
		case R.id.decorate_detail_freeyuyue: // 免费预约
			Intent yuyueIntent = new Intent(mContext, FreeYuyueActivity.class);
			startActivity(yuyueIntent);
			break;
		case R.id.decorate_detail_cellphone:
		case R.id.decorate_detail_phonelayout: // 打电话
			CallDialogCompany callDialog = new CallDialogCompany(mContext, R.style.callDialogTheme, cellphone, tel);
			window = callDialog.getWindow();
			params = window.getAttributes();
			params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
			callDialog.show();
			break;
		case R.id.decorate_detail_introduce: // 点击公司简介文字 展开公司全部简介
			if (flag) {
				flag = false;
				decorate_detail_introduce.setEllipsize(null);
				decorate_detail_introduce.setSingleLine(flag);
			} else {
				flag = true;
				decorate_detail_introduce.setLines(3);
				decorate_detail_introduce.setEllipsize(TextUtils.TruncateAt.END);
			}
			break;
		case R.id.decorate_detail_fav:
			operFav();
			break;
		case R.id.find_decorate_detail_share:
			new ShareUtil(mContext, title, title, fenxiang_url);
			break;
		case R.id.iv_design_chart1:
			if(design_charts!=null && design_charts.size()>0){
				Bundle bundle1 = new Bundle();
				bundle1.putString("companyId", companyId);
				Util.setLog("DecorateCompanyDetailActivity", "songchengcai一张图携带跳故去" + comid);
				bundle1.putString("url", design_charts.get(0));
				Intent intent1 = new Intent(mContext, ImageDetailActivity.class);
				intent1.putExtras(bundle1);
				startActivity(intent1);
			}
			break;
		case R.id.iv_design_chart2:
			if(design_charts!=null && design_charts.size()>1){
				Bundle bundle2 = new Bundle();
				bundle2.putString("url", design_charts.get(1));
				Intent intent2 = new Intent(mContext, ImageDetailActivity.class);
				intent2.putExtras(bundle2);
				startActivity(intent2);
			}

			break;
		case R.id.iv_design_chart3:
			if(design_charts!=null && design_charts.size()>2){
				Bundle bundle3 = new Bundle();
				bundle3.putString("url", design_charts.get(2));
				Intent intent3 = new Intent(mContext, ImageDetailActivity.class);
				intent3.putExtras(bundle3);
				startActivity(intent3);
			}
			break;
		case R.id.decorate_detail_bao: // 点击装修保
			BaoPopupWindow baoPopupWindow = new BaoPopupWindow(getApplicationContext());
			baoPopupWindow.showAtLocation(findViewById(R.id.decorate_detail_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.decorate_detail_vouchers: // 点击抵用券 
			VouchersPopupWindow vouchersPopupWindow = new VouchersPopupWindow(getApplicationContext());
			vouchersPopupWindow.showAtLocation(findViewById(R.id.decorate_detail_layout), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.tv_company_image_count:
		case R.id.iv_company_image1: // 显示介绍图片1
			CompanyImagePopupWindow popupwindow1 = new CompanyImagePopupWindow(this, company_images, 0);
			popupwindow1.showAtLocation(findViewById(R.id.iv_company_image1), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.iv_company_image2: // 显示介绍图片2
			CompanyImagePopupWindow popupwindow2 = new CompanyImagePopupWindow(this, company_images, 1);
			popupwindow2.showAtLocation(findViewById(R.id.iv_company_image2), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.iv_company_image3: // 显示介绍图片3
			CompanyImagePopupWindow popupwindow3 = new CompanyImagePopupWindow(this, company_images, 2);
			popupwindow3.showAtLocation(findViewById(R.id.iv_company_image3), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.tv_qq:
			MobclickAgent.onEvent(mContext, "click_find_com_com_detail_phone_qq(qq_succeed)");
			String url = "mqqwpa://im/chat?chat_type=wpa&uin="+qq;
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			break;

		case R.id.ll_image_laylout:
			// 弹窗
			DialogShowText dialog = new DialogShowText(mContext);
			dialog.show();
			break;
		default:
			break;
		}
	}
	private void operFav() {
		token = AppInfoUtil.getToekn(getApplicationContext());
		if (TextUtils.isEmpty(token)) {
			Util.setToast(mContext, "亲请先登陆后再来收藏！");
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra("isFav", true);
			startActivityForResult(intent, 0);
			return;
		}
		
		HashMap<String, Object> favParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
		favParams.put("fav_conid", comid);
		if (oper_type.equals("1")) {
			favParams.put("token", token);
			favParams.put("fav_type", "com");
			favParams.put("oper_type", "1");
			hav_fav = "1";
		} else {
			favParams.put("token", token);
			favParams.put("id", comid);
			favParams.put("oper_type", "0");
			hav_fav = "0";
		}
		if ("0".equals(hav_fav)) {
			decorate_detail_fav.setImageResource(R.drawable.fa02);
			oper_type = "1";
		} else {
			decorate_detail_fav.setImageResource(R.drawable.fa_red);
			oper_type = "0";
		}
		
		OKHttpUtil.post(favUrl, favParams, new Callback() {
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
							String msg = jsonObject.getString("msg");
							if (msg.equals("操作成功")) {
								if (oper_type.equals("0")) {
									Toast.makeText(getApplicationContext(), "收藏成功!", Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getApplicationContext(), "取消收藏成功!", Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
}
