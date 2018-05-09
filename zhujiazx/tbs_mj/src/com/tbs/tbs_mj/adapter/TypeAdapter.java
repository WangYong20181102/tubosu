package com.tbs.tbs_mj.adapter;

import java.util.ArrayList;

import com.tbs.tbs_mj.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TypeAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> data = null;
	private LayoutInflater inflater = null;
	private static int currFocusId = 0;

	public TypeAdapter(Context mContext, ArrayList<String> data) {
		this.mContext = mContext;
		this.data = data;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_popup_list_type, parent, false);
			holder = new ViewHolder();
			holder.decorate_type = (TextView) convertView.findViewById(R.id.decorate_type);
			holder.iv = (ImageView)convertView.findViewById(R.id.decorate_type_img);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String str= data.get(position);
		
		if(str.equals("装修保")){
			holder.iv.setBackgroundResource(R.drawable.image_bao);
		}else if(str.equals("实体认证")){
			holder.iv.setBackgroundResource(R.drawable.image_business_license);
		}else{ // 全部 [图标]
			holder.iv.setBackgroundResource(R.drawable.all_icon);
		}
		
		holder.decorate_type.setText(str);
		
		if (currFocusId == position) {
			holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.color_huxing));
			holder.decorate_type.setTextColor(mContext.getResources().getColor(R.color.layout_color));
		} else {
			holder.layout.setBackgroundColor(Color.TRANSPARENT); 
			holder.decorate_type.setTextColor(mContext.getResources().getColor(R.color.color_neutralgrey));
		}
		
		return convertView;
	}
	static class ViewHolder {
		RelativeLayout layout;
		TextView decorate_type;
		ImageView iv; // 图标
	}

	public void setSelectedPosition(int positon) {
		currFocusId = positon;
	}
}
