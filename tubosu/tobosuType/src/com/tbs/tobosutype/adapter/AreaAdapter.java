package com.tbs.tobosutype.adapter;

import java.util.ArrayList;

import com.tbs.tobosutype.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AreaAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<String> lefts = null;
	LayoutInflater inflater = null;
	private static int currFocusId = 0;

	public AreaAdapter(Context mContext, ArrayList<String> lefts) {
		this.mContext = mContext;
		this.lefts = lefts;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return lefts.size();
	}

	@Override
	public Object getItem(int position) {
		return lefts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_popup_list_area, parent, false);
			holder = new ViewHolder();
			holder.decorate_area = (TextView) convertView.findViewById(R.id.decorate_area);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str= lefts.get(position);
		holder.decorate_area.setText(str);
		
		if (currFocusId == position) {
			holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.color_huxing));
			holder.decorate_area.setTextColor(mContext.getResources().getColor(R.color.layout_color));
		} else {
			holder.layout.setBackgroundColor(Color.TRANSPARENT); 
			holder.decorate_area.setTextColor(mContext.getResources().getColor(R.color.color_neutralgrey));
		}
		return convertView;
	}
	
	static class ViewHolder {
		RelativeLayout layout;
		TextView decorate_area;
	}

	public void setSelectedPosition(int positon) {
		currFocusId = positon;
	}
}
