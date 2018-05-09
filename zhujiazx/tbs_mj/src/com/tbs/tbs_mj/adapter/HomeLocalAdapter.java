package com.tbs.tbs_mj.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.RoundImageView;
import com.tbs.tbs_mj.global.MyApplication;

public class HomeLocalAdapter extends BaseAdapter{
	private static final String TAG = HomeLocalAdapter.class.getSimpleName();
	
	private Context context;
	private int selectItem;
	private List<Drawable> view_bg;
	private List<HashMap<String, String>> datas;

	public HomeLocalAdapter(Context context, List<HashMap<String, String>> datas) {
		this.context = context;
		this.datas = datas;
		
		view_bg = new ArrayList<Drawable>();
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon1));
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon2));
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon3));
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon1));
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon2));
		view_bg.add(context.getResources().getDrawable(R.drawable.icon_discount_coupon3));
	}

	@Override
	public int getCount() {
		if(datas.size()==0){
			return 0;
		}
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setSelectItem(int selectItem) {

		if (this.selectItem != selectItem) {
			this.selectItem = selectItem;
			notifyDataSetChanged();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_local_discount_gridview_item, null);
			holder = new ViewHolder();
			holder.local_discount_item_layout = (RelativeLayout) convertView.findViewById(R.id.local_discount_item_layout);
			holder.riv_logo = (RoundImageView) convertView.findViewById(R.id.riv_logo);
			holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_local_discount_desc);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_end_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		int num = position % view_bg.size();
//		holder.local_discount_item_layout.setBackground(view_bg.get(num));
		
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 90), DensityUtil.dip2px(context, 125));
//		params.addRule(RelativeLayout.CENTER_IN_PARENT);
//		holder.local_discount_item_layout.setLayoutParams(params);
//		holder.local_discount_item_layout.setFocusable(false);
		
		holder.local_discount_item_layout.setBackground(view_bg.get(position));
		holder.tv_desc.setText(datas.get(position % datas.size()).get("title"));
		holder.tv_time.setText(datas.get(position % datas.size()).get("endtime") + "截止");
		MyApplication.imageLoader.displayImage(datas.get(position % datas.size()).get("logosmall"), holder.riv_logo, MyApplication.options);
		return convertView;
	}

	class ViewHolder {
		RelativeLayout local_discount_item_layout;
		RoundImageView riv_logo;
		TextView tv_desc;
		TextView tv_time;
	}
}
