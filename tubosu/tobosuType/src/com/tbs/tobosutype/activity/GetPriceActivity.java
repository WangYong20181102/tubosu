package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.customview.ObservableScrollView;
import com.tbs.tobosutype.customview.PmTextView;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.Util;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 首页免费设计按钮  跳转过来的 报价页面  [本来是加载html5页面， 袁总说需要安卓原生页面, 几经周折改成如下页面 ]
 * @author dec
 *
 */
public class GetPriceActivity extends Activity implements OnClickListener {
	private static final String TAG = GetPriceActivity.class.getSimpleName();
	private final int[] imgIdList = {R.drawable.vp1,R.drawable.vp2, R.drawable.vp3};
	private final int cheatNum = 156486;
	private ImageView[] tips;
	private ImageView[] mImageViews;

	private Context mContext;
	private RelativeLayout relBack;
	private ObservableScrollView scrollview;
	private ImageView ivGotop;
	private EditText etName;
	private EditText etPhone;
	private RelativeLayout relChooseCity;
	private TextView tvCity;
	private String city;

	private TextView tvSummit;
	private PmTextView tvCheatTextView;

	private ViewPager vpGetpriceImg;
	private ViewGroup group;

	/**获取保存本地的城市参数*/
	private SharedPreferences getCitySharePre;
	
	private RequestParams pubOrderParams;
	private String phone;
	private String cityid;
	private String userid;
	private String budget;

	private ImageView ivNum1;
	private ImageView ivNum2;
	private ImageView ivNum3;
	private ImageView ivNum4;
	private ImageView ivNum5;
	private ImageView ivNum6;
	private ImageView[] imgArray;

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppInfoUtil.setTranslucentStatus(this);
		setContentView(R.layout.activity_getprice);
		mContext = GetPriceActivity.this;
		initView();
		initData();
		initViewpagerAdapter();
		initEvent();
	}

	@Override
	protected void onStart() {
		super.onStart();
		numHandler.sendEmptyMessage(0);

		timer = new Timer();
		timer.schedule(task, 1000, 1000);  // timeTask
	}


	int recLen = 8;
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					recLen--;
					if(recLen < 0){
						timer.cancel();
						numHandler.sendEmptyMessage(1);
					}
				}
			});
		}
	};

	private void initView() {
		relBack = (RelativeLayout) findViewById(R.id.rel_getprice_back);
		ivGotop = (ImageView) findViewById(R.id.iv_gotop);
		scrollview = (ObservableScrollView) findViewById(R.id.scrollview_getprice);
		vpGetpriceImg = (ViewPager) findViewById(R.id.vp_getprice_img);
		group = (ViewGroup)findViewById(R.id.dot_view_group);
		etName = (EditText) findViewById(R.id.getprice_name);
		etPhone = (EditText) findViewById(R.id.getprice_phone);
		tvCity = (TextView) findViewById(R.id.tv_getprice_city);
		relChooseCity = (RelativeLayout) findViewById(R.id.rel_choose_city);
		tvSummit = (TextView) findViewById(R.id.getprice_submit);
		tvCheatTextView = (PmTextView) findViewById(R.id.cheat_textview);

		ivNum1 = (ImageView) findViewById(R.id.num1);
		ivNum2 = (ImageView) findViewById(R.id.num2);
		ivNum3 = (ImageView) findViewById(R.id.num3);
		ivNum4 = (ImageView) findViewById(R.id.num4);
		ivNum5 = (ImageView) findViewById(R.id.num5);
		ivNum6 = (ImageView) findViewById(R.id.num6);
		imgArray = new ImageView[]{ivNum1,ivNum2,ivNum3,ivNum4,ivNum5};

		scrollview.setOnScollChangedListener(new ObservableScrollView.OnScollChangedListener() {
			@Override
			public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

				if(y <= DensityUtil.dip2px(mContext, 371)){
					ivGotop.setVisibility(View.GONE);
				}else {
					ivGotop.setVisibility(View.VISIBLE);
				}
			}
		});
	}



	private Handler numHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 0:
					startAnimation(imgArray);
					break;
				case 1:
					stopAnimation();
					break;
			}
//
		}
	};

	private void startAnimation(ImageView[] imgs){
		for(int i=0, len = imgs.length;i<len;i++){
			switch (i){
				case 0:
					imgs[0].setBackgroundResource(+R.anim.num_animation0);
					break;
				case 1:
					imgs[1].setBackgroundResource(+R.anim.num_animation00);
					break;
				case 2:
					imgs[2].setBackgroundResource(+R.anim.num_animation000);
					break;
				case 3:
					imgs[3].setBackgroundResource(+R.anim.num_animation0000);
					break;
				case 4:
					imgs[4].setBackgroundResource(+R.anim.num_animation00000);
					break;
			}
			AnimationDrawable anim = (AnimationDrawable) imgs[i].getBackground(); //获取ImageView背景,此时已被编译成AnimationDrawable
			anim.start();  //开始动画
		}
	}

	private void stopAnimation(){
		ivNum1.setBackgroundResource(R.drawable.n6);
		ivNum2.setBackgroundResource(R.drawable.n8);
		ivNum3.setBackgroundResource(R.drawable.n4);
		ivNum4.setBackgroundResource(R.drawable.n6);
		ivNum5.setBackgroundResource(R.drawable.n5);
		ivNum6.setBackgroundResource(R.drawable.n1);
	}


	private String cheatText =
			"武汉的周先生已经获取4份报价 1分钟前  上海的李先生已经获取2份报价 1分钟前"  +
			"北京的何女士已经获取2份报价 3分钟前  深圳的高先生已经获取2份报价 1分钟前"  +
			"南昌的张女士已经获取1份报价 2分钟前  厦门的刘先生已经获取1份报价 1分钟前"  +
			"湖南的于女士已经获取1份报价 6分钟前  武汉的阎先生已经获取1份报价 2分钟前"  +
			"广州的刘女士已经获取1份报价 8分钟前  合肥的曾先生已经获取1份报价 3分钟前"  +
			"上海的陈女士已经获取1份报价 6分钟前  南京的谢先生已经获取1份报价 2分钟前"  +
			"南宁的徐女士已经获取1份报价 6分钟前  石家庄的袁先生已经获取1份报价 2分钟前"  +
			"天津的方女士已经获取1份报价 6分钟前  重庆的胡先生已经获取1份报价 2分钟前";

//	private int getCheatNum(){
//		Random random=new Random();// 定义随机类
//		int result=random.nextInt(16);// 返回[0,16)集合中的整数，注意不包括10
//		return result+1;
//	}

	private void initData() {
		tvCheatTextView.setText(cheatText);
		pubOrderParams = AppInfoUtil.getPublicParams(getApplicationContext());
		getCitySharePre = this.getSharedPreferences("Save_City_Info", MODE_PRIVATE);
		String city = getCitySharePre.getString("save_city_now", "");
		if(!"".equals(city)){
			tvCity.setText(city);
		}
	}

	private void initEvent() {
		tvSummit.setOnClickListener(this);
		relBack.setOnClickListener(this);
		relChooseCity.setOnClickListener(this);
		ivGotop.setOnClickListener(this);
	}

	private void initViewpagerAdapter(){
		//将点点加入到ViewGroup中
		tips = new ImageView[imgIdList.length];
		for(int i=0; i<tips.length; i++){
			ImageView imageView = new ImageView(this);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
			tips[i] = imageView;
			if(i == 0){
				tips[i].setBackgroundResource(R.drawable.dot_on);
			}else{
				tips[i].setBackgroundResource(R.drawable.dot_off);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 5;
			layoutParams.rightMargin = 5;
			group.addView(imageView, layoutParams);
		}


		//将图片装载到数组中
		mImageViews = new ImageView[imgIdList.length];
		for(int i=0; i<mImageViews.length; i++){
			ImageView imageView = new ImageView(this);
			mImageViews[i] = imageView;
			imageView.setBackgroundResource(imgIdList[i]);
		}


		ImgAdapter adapter = new ImgAdapter(mImageViews);
		vpGetpriceImg.setAdapter(adapter);

		vpGetpriceImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				setImageBackground(position%imgIdList.length);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 设置选中的tip的背景
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems){
		for(int i=0; i<tips.length; i++){
			if(i == selectItems){
				tips[i].setBackgroundResource(R.drawable.dot_on);
			}else{
				tips[i].setBackgroundResource(R.drawable.dot_off);
			}
		}
	}

	private CustomWaitDialog waitDialog;
	private void showLoadingView(){
		waitDialog = new CustomWaitDialog(mContext);
		waitDialog.show();
	}

	private void hideLoadingView(){
		if(waitDialog!=null){
			waitDialog.dismiss();
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_getprice_back:
			finish();
			break;
		case R.id.iv_gotop:
			scrollview.smoothScrollTo(0, DensityUtil.dip2px(mContext, 160));
			break;
		case R.id.rel_choose_city:
			Intent selectCityIntent = new Intent(mContext, SelectCtiyActivity.class);
			Bundle b = new Bundle();
			b.putString("fromGetPrice", "647");
			selectCityIntent.putExtra("GetPriceSelectcityBundle", b);
			startActivityForResult(selectCityIntent, 0);
		break;
		case R.id.free_yuyue_submit:
			String name = etName.getText().toString().trim();
			String phone = etPhone.getText().toString().trim();
			if (TextUtils.isEmpty(name)) {
				Util.setToast(mContext, "请输入姓名");
				return;
			}

			if (TextUtils.isEmpty(phone)) {
				Util.setToast(mContext, "请输入手机号码");
				return;
			}

			if(Constant.checkNetwork(mContext)){

				pubOrderParams.put("", name); // FIXME 接口需要更改
				pubOrderParams.put("cellphone", phone);
				pubOrderParams.put("city", city);
				Util.setLog(TAG, "免费预约的城市是" + city);
				pubOrderParams.put("urlhistory", Constant.PIPE);
				// 发单入口
				pubOrderParams.put("comeurl", Constant.PIPE);
				if (!TextUtils.isEmpty(userid)) {
					pubOrderParams.put("userid", userid);
				} else {
					pubOrderParams.put("userid", "0");
				}
				pubOrderParams.put("source", "1112");
				requestPubOrder();
			}else{
				Constant.toastNetOut(mContext);
			}
			break;
		}

	}

	/***
	 * 发单接口请求
	 */
	private void requestPubOrder() {
		showLoadingView();
		HttpServer.getInstance().requestPOST(Constant.PUB_ORDERS, pubOrderParams, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, byte[] body) {
						String result = new String(body);
						try {
							JSONObject jsonObject = new JSONObject(result);
							if (jsonObject.getInt("error_code") == 0) {
								Intent intent = new Intent(GetPriceActivity.this, ApplyforSuccessActivity.class);
								intent.putExtra("phone", phone);
								startActivity(intent);
								finish();
							} else {
                                Util.setToast(mContext, jsonObject.getString("msg"));
							}

							hideLoadingView();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                        Util.setToast(mContext, "请稍后再试~");
                        hideLoadingView();
					}
				});
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode){
			case 124:
				Bundle cityBundle = data.getBundleExtra("city_bundle");
				city = cityBundle.getString("ci");
				tvCity.setText(city);
				break;
		}
	}

	private class ImgAdapter extends PagerAdapter{
		private ImageView[] array;

		public ImgAdapter(ImageView[] array){
			this.array = array;
		}

		@Override
		public int getCount() {
			return array.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}


		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager)container).removeView(array[position % array.length]);
		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager)container).addView(array[position % array.length], 0);
			return array[position % array.length];
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}