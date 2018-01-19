package com.tbs.tobosutype.adapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DecorateCompanyDetailActivity;
import com.tbs.tobosutype.activity.DesignChartAcitivity;
import com.tbs.tobosutype.activity.ImageDetailActivity;
import com.tbs.tobosutype.activity.NewLoginActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***
	 * 逛图库的适配器
	 * @author dec
	 *
	 */
public class ImageAdapter extends BaseAdapter {
	private static final String TAG = ImageAdapter.class.getSimpleName();
	private List<HashMap<String, String>> datas;
	private Context mContext;
	private LinearLayout ll_loading;
//	private FavDBManager favDBManager;
	
	/**是否已经收藏标记*/
	private String hav_fav;
	
	/**
	 *  操作类型 <br/>
	 *  1 --> 添加收藏<br/>
	 *  0 --> 取消收藏
	 */
	private String oper_type = "";
	
	/**从数据库获取的原始收藏的数据 */
//	private ArrayList<String> isFavDataList = new ArrayList<String>();
	
	/**网络获取的是否已经收藏的标记集合<br/>
	 * 1 --> 未收藏
	 * 0 --> 已收藏
	 * 
	 * */
	private ArrayList<String> favListFromNet = new ArrayList<String>();
	
	/**添加或者取消 收藏接口*/
	private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";
	
	/**获取未点击操作前的 状态*/
//	private String get_hav_fav = "";

	
	public ImageAdapter(List<HashMap<String, String>> datasList, Context mContext, LinearLayout ll_loading) {
		this.datas = datasList;
		this.mContext = mContext;
		this.ll_loading = ll_loading;
//		favDBManager = FavDBManager.getInstance(mContext);
//		isFavDataList = favDBManager.queryFavImage();
	}

	@Override
	public int getCount() {
		if (datas != null) {
			return datas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image_listview, null);
			mHolder.imageactivity_item_company_logo = (ImageView) convertView.findViewById(R.id.imageactivity_item_company_logo);
			
			mHolder.imageactivity_item_company_name = (TextView) convertView.findViewById(R.id.imageactivity_item_company_name);
			mHolder.image_item_fav = (ImageView) convertView.findViewById(R.id.image_item_fav);
			
			mHolder.rel_company_3pictures = (RelativeLayout) convertView.findViewById(R.id.rel_company_3pictures);
			mHolder.image_item_showtime = (TextView) convertView.findViewById(R.id.image_item_showtime);
			mHolder.imageactivity_item_pics_1 = (ImageView) convertView.findViewById(R.id.imageactivity_item_pics_1);
			mHolder.imageactivity_item_pics_2 = (ImageView) convertView.findViewById(R.id.imageactivity_item_pics_2);
			mHolder.imageactivity_item_pics_3 = (ImageView) convertView.findViewById(R.id.imageactivity_item_pics_3);
			mHolder.imageactivity_item_company_location = (TextView) convertView.findViewById(R.id.imageactivity_item_company_location);
			
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(convertView.isHardwareAccelerated()){
			convertView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//			Log.d("ImageAdapter逛图库适配器convertView", "现在关闭硬件加速器");
		}
		
		ImageLoaderUtil.loadImage(mContext, mHolder.imageactivity_item_company_logo, datas.get(position).get("logosmall"));
		ImageLoaderUtil.loadImage(mContext, mHolder.imageactivity_item_pics_1, datas.get(position).get("index_image_url"));
		ImageLoaderUtil.loadImage(mContext, mHolder.imageactivity_item_pics_2, datas.get(position).get("img1"));
		ImageLoaderUtil.loadImage(mContext, mHolder.imageactivity_item_pics_3, datas.get(position).get("img2"));
		
		mHolder.imageactivity_item_company_name.setText(datas.get(position).get("comsimpname"));
		mHolder.image_item_showtime.setText(datas.get(position).get("check_time"));
		mHolder.imageactivity_item_company_location.setText(datas.get(position).get("title"));
		
		mHolder.rel_company_3pictures.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String id = datas.get(position).get("id");
				Bundle bundle = new Bundle();
				bundle.putString("id", id);
				Intent intent = new Intent(mContext, ImageDetailActivity.class);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		
		ll_loading.setVisibility(View.GONE);

		// 找装修中的设计图册也复用了这个adapter
		if (mContext instanceof DesignChartAcitivity) {
			mHolder.image_item_fav.setVisibility(View.GONE);
		} else {
			mHolder.image_item_fav.setVisibility(View.VISIBLE);
			hav_fav = datas.get(position).get("hav_fav");
			Log.d(TAG, position+ " 收藏标记 -> "+hav_fav);
//			Log.d(TAG, "这个item的id "+datas.get(position).get("id"));
			favListFromNet.add(hav_fav);
			
			if("1".equals(hav_fav)) {
				// 收藏
				mHolder.image_item_fav.setImageResource(R.drawable.image_love_sel);
			} else if("0".equals(hav_fav)){
				// 不收藏
				mHolder.image_item_fav.setImageResource(R.drawable.image_love_nor);
			}
		}
		
		// 点击是否收藏
		final ImageView icon_fav = mHolder.image_item_fav;
//		get_hav_fav = favListFromNet.get(position);
//		Log.d(TAG, "列表上的收藏状态是"+favListFromNet.get(position));
		icon_fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				operateCollect(icon_fav, position, mContext);
			}
		});
		
		// 点击装修公司logo 跳转装修公司页面
		mHolder.imageactivity_item_company_logo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent detailIntent = new Intent(mContext, DecorateCompanyDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("comid", datas.get(position).get("comid"));
				detailIntent.putExtras(bundle);
				mContext.startActivity(detailIntent);
			}
		});
		
		return convertView;
	}

	class ViewHolder {

		ImageView image_item_fav; // 收藏图标
		ImageView imageactivity_item_company_logo; // 公司图片
		TextView imageactivity_item_company_name; // 公司名字
		TextView image_item_showtime; // 时间
		ImageView imageactivity_item_pics_1;
		ImageView imageactivity_item_pics_2;
		ImageView imageactivity_item_pics_3;
		TextView imageactivity_item_company_location; // 公司所处区域
		RelativeLayout rel_company_3pictures;  // 3张图片布局
	}

	/***
	 *  点击图标是否收藏
	 * @param ic_fav
	 * @param position
	 */
	private void operateCollect(ImageView ic_fav, int position, final Context mContext) {
		// 收藏id
		String id = datas.get(position).get("id"); //获取收藏id
//		Toast.makeText(mContext, "收藏id是"+id, Toast.LENGTH_SHORT).show();
		
		if (TextUtils.isEmpty(AppInfoUtil.getToekn(mContext))) {
			Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
//			Intent intent = new Intent(mContext, LoginActivity.class);
			Intent intent = new Intent(mContext, NewLoginActivity.class);
//			intent.putExtra("isFav", true);
			((Activity) mContext).startActivityForResult(intent, 0);
			return;
		}
		
		if(!Constant.checkNetwork(mContext)){
			Constant.toastNetOut(mContext);
			return;
		}
		
		final HashMap<String, Object> favParams = AppInfoUtil.getPublicHashMapParams(mContext);
		String state = favListFromNet.get(position);
		if("1".equals(state)) { 
			// 点击前是 1(已收藏) --> 点击后变为0(取消收藏)
			oper_type = "0";
			ic_fav.setImageResource(R.drawable.image_love_nor);
		} else if("0".equals(state)){
			// 点击前变为0(取消收藏) --> 点击后是 1(已收藏)
			oper_type = "1";
			ic_fav.setImageResource(R.drawable.image_love_sel);
		}
		
		favParams.put("token", AppInfoUtil.getToekn(mContext));
		favParams.put("fav_type", "showpic"); // fav_type 操作对象是 图库
		Log.d(TAG, "我明明传了 收藏id"+id);
		
		//开始操作  --请注意下面这个if else语句总的fav_id以及它们相应的key value值--
		if (oper_type.equals("1")) { 
			MobclickAgent.onEvent(mContext, "click_find_decoration_array_comname_favorite");
			// 操作类型是1 添加收藏
			favParams.put("oper_type", "1"); //操作类型1 --> 添加收藏
			favParams.put("fav_conid", id);
			favListFromNet.remove(position);
			favListFromNet.add(position, "1");
			oper_type = "0";
		} else if(oper_type.equals("0")){
			 // 操作类型是0 添加收藏
			favParams.put("oper_type", "0"); //操作类型0 --> 取消收藏
			favParams.put("id", id);
			favListFromNet.remove(position);
			favListFromNet.add(position, "0");
			oper_type = "1";
		}


		OKHttpUtil.post(favUrl, favParams, new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				handler.sendEmptyMessage(444);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String result = response.body().string();
				try {
					JSONObject jsonObject = new JSONObject(result);
					String msg = jsonObject.getString("msg");
					if (msg.equals("操作成功")) {
						if (oper_type.equals("0")) {
							handler.sendEmptyMessage(4);
						} else {
							handler.sendEmptyMessage(44);
						}

					} else {
						handler.sendEmptyMessage(444);
						Log.d(TAG, "操作失败原因是 "+msg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case 4:
					Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
					break;
				case 44:
					Toast.makeText(mContext, "取消收藏成功!", Toast.LENGTH_SHORT).show();
					break;
				case 444:
					Toast.makeText(mContext, "操作失败，请稍后再试!", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

}
