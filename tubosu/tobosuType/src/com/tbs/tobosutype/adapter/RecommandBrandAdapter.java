package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
	/***
	 *  找装修页面中的 推荐品牌 gridview的适配器
	 * @author dec
	 *
	 */
public class RecommandBrandAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> getAdsDatas;

	public RecommandBrandAdapter(Context context, ArrayList<HashMap<String, String>> getAdsDatas) {
		this.context = context;
		this.getAdsDatas = getAdsDatas;
	}

	@Override
	public int getCount() {
		if (getAdsDatas != null) {
			return getAdsDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return getAdsDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_brad_horizontall_list, null);
			holder = new ViewHolder();
			holder.effectTitle = (TextView) convertView.findViewById(R.id.effectTitle);
			holder.effectDrawable = (ImageView) convertView.findViewById(R.id.effectDrawable);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.effectTitle.setText(getAdsDatas.get(position).get("comsimpname"));
		ImageLoaderUtil.loadImage(context, holder.effectDrawable, getAdsDatas.get(position).get("img"));
		return convertView;
	}

	private class ViewHolder {
		TextView effectTitle;
		ImageView effectDrawable;
	}
}
