package com.tbs.tbs_mj.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.DecorateCompanyDetailActivity;
import com.tbs.tbs_mj.activity.DesignChartAcitivity;
import com.tbs.tbs_mj.activity.ImageDetailActivity;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;

	/***
	 * 逛图库的适配器
	 * @author dec
	 *
	 */
public class DesignChartImageAdapter extends BaseAdapter {
	private static final String TAG = DesignChartImageAdapter.class.getSimpleName();
	private List<HashMap<String, String>> datas;
	private Context mContext;
	private LinearLayout ll_loading;
	
	/**是否已经收藏标记*/
	private String hav_fav;
	
	/**
	 *  操作类型 <br/>
	 *  1 --> 添加收藏<br/>
	 *  0 --> 取消收藏
	 */
	private String oper_type = "";
	
	
	/**添加或者取消 收藏接口*/
	private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";
	

	
	public DesignChartImageAdapter(List<HashMap<String, String>> datas, Context mContext, LinearLayout ll_loading) {
		this.datas = datas;
		this.mContext = mContext;
		this.ll_loading = ll_loading;
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
		}
		
		
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

}
