package com.tbs.tobosutype.customview;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.FreeActivity;
import com.tbs.tobosutype.activity.WebViewActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.DensityUtil;

/**
 * 首页顶部广告
 * 
 * @author dec
 * 
 */
public class SlideShowView extends FrameLayout {

	private ImageLoader imageLoader = ImageLoader.getInstance();
	private final static boolean isAutoPlay = true;
	private String[] imageUrls;
	private List<ImageView> imageViewsList;
	private List<View> dotViewsList;
	/**包含h5发单url*/
	private String containFreeUrl = "http://m.tobosu.com/app/pub?channel=sem&";
	private ViewPager viewPager;
	private int currentItem = 0;
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;
	private String cityName;
	private String urlString = Constant.TOBOSU_URL
			+ "tapp/util/carousel_figure?city=" + cityName + "&version="
			+ AppInfoUtil.getAppVersionName(context) + "&device=android";
	private List<HashMap<String, Object>> slideShowList = new ArrayList<HashMap<String, Object>>();
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}

	};

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		initImageLoader(context);
		SharedPreferences citys = context.getSharedPreferences("cityInfo", 0);
		cityName = citys.getString("cityName", "");
		initUI(context);
		initData();
		if (isAutoPlay) {
			startPlay();
		}

	}

	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	private void initData() {
		String result = context.getSharedPreferences("NavigationCache", 0)
				.getString("result", "");
		if (!TextUtils.isEmpty(result)) {
			showImages(result);
		}
		new GetListTask().execute(urlString);
	}

	private void initUI(Context context) {
		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
				true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		// 连续播放的图片
		if (imageUrls != null && imageUrls.length > 0) {
			for (int i = 0; i < imageUrls.length; i++) {
				ImageView view = new ImageView(context);
				view.setTag(imageUrls[i]);
				view.setScaleType(ScaleType.CENTER_CROP);
				imageViewsList.add(view);
				// 广播图的定位点
				ImageView dotView = new ImageView(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						DensityUtil.dip2px(getContext(), 10),
						DensityUtil.dip2px(getContext(), 10));
				params.leftMargin = 8;
				params.rightMargin = 8;
				dotLayout.addView(dotView, params);
				dotViewsList.add(dotView);
			}
			viewPager.setFocusable(true);
			viewPager.setAdapter(new MyPagerAdapter());
			viewPager.setOnPageChangeListener(new MyPageChangeListener());
		}
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(imageViewsList.get(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {
			ImageView imageView = imageViewsList.get(position);
			imageView.setBackgroundResource(R.drawable.firstloaderror);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheOnDisc(true)
					.showImageOnFail(R.drawable.firstloaderror)
					.imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.resetViewBeforeLoading(true).build();
			
			imageLoader.displayImage(imageView.getTag() + "", imageView, options);
			((ViewPager) container).addView(imageViewsList.get(position));
			
			imageViewsList.get(position).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							String content_url = (String) slideShowList.get(position).get("content_url");
							if (!content_url.contains(containFreeUrl)) {
								Intent detailIntent = new Intent(context, WebViewActivity.class);
								Bundle bundle = new Bundle();
								bundle.putString("link", content_url);
								detailIntent.putExtras(bundle);
								context.startActivity(detailIntent);
							} else {
								Intent intent = new Intent(context, FreeActivity.class);
								context.startActivity(intent);
							}
						}
					});
			return imageViewsList.get(position);
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {

			switch (arg0) {
			case 1:
				isAutoPlay = false;
				break;
			case 2:
				isAutoPlay = true;
				break;
			case 0:
				if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				} else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int pos) {

			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_select);
				} else {
					((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_normal);
				}
			}
		}

	}

	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % imageViewsList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	/**
	 * 异步任务,获取图片数据
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片
				try {
					URL url = new URL(params[0]);
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					httpConn.setDoInput(true);
					httpConn.setDoOutput(false);
					httpConn.connect();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					if (httpConn.getResponseCode() == 200) {
						BufferedInputStream bis = new BufferedInputStream(httpConn.getInputStream());
						byte[] buffer = new byte[1024 * 8];
						int c = 0;
						while ((c = bis.read(buffer)) != -1) {
							baos.write(buffer, 0, c);
							baos.flush();
						}
						byte[] data = baos.toByteArray();
						String result = new String(data);
						context.getSharedPreferences("NavigationCache", 0)
								.edit().putString("result", result).commit();
						showImages(result);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {

				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initUI(context);
			}
		}
	}

	private void showImages(String result) {
		slideShowList = parseSlideShowListJSON(result);
		imageUrls = new String[slideShowList.size()];
		for (int i = 0; i < slideShowList.size(); i++) {
			imageUrls[i] = (String) slideShowList.get(i).get("img_url");
		}
	}

	/**
	 * ImageLoader 图片组件初始化
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	private List<HashMap<String, Object>> parseSlideShowListJSON(String result) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			JSONObject object1 = new JSONObject(result);
			JSONArray array = object1.getJSONArray("data");

			for (int i = 0; i < array.length(); i++) {
				HashMap<String, Object> dataMap = new HashMap<String, Object>();
				JSONObject dataObject = array.getJSONObject(i);
				dataMap.put("img_url", dataObject.getString("img_url"));
				dataMap.put("content_url", dataObject.getString("content_url"));
				list.add(dataMap);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}