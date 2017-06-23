package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tbs.tobosutype.R;


public class WriteAccountAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private String[] dataList;
	private int selectedPosition = -1;

	public WriteAccountAdapter(Context context, String[] dataList){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.dataList = dataList;
	}

	public void clearSelection(int position) {
		selectedPosition = position;
	}
	public void setSelection(int selected){
		this.selectedPosition = selected;
	}

	@Override
	public int getCount() {
		return dataList==null?0:dataList.length;
	}

	@Override
	public Object getItem(int position) {
		return dataList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoler holer = null;
		if(convertView==null){
			holer = new ViewHoler();
			convertView = inflater.inflate(R.layout.item_adapter_decorate_account_string, null, false);
			holer.tv = (TextView) convertView.findViewById(R.id.tv_decorate_string);
			convertView.setTag(holer);
		}else{
			holer = (ViewHoler) convertView.getTag();
		}
		holer.tv.setText(dataList[position]);

		if(selectedPosition == position){
			holer.tv.setBackgroundResource(R.drawable.shape_style_textview_selected);
		}else{
			holer.tv.setBackgroundResource(R.drawable.shape_style_textview_unselected);
		}

		return convertView;
	}


	class ViewHoler {
		TextView tv;
	}
}
