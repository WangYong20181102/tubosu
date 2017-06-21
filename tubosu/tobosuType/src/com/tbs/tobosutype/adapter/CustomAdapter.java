package com.tbs.tobosutype.adapter;

import java.util.HashMap;
import java.util.List;



import com.tbs.tobosutype.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> list;
	private LayoutInflater inflater = null;
	public CustomAdapter(Context context,List<HashMap<String, Object>> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_first_gridview, parent, false);
			holder = new ViewHolder();

			holder.item_first_tv = (TextView) convertView
					.findViewById(R.id.item_first_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.item_first_tv.setText(list.get(position)
				.get("class_name").toString());
		
		return convertView;
	}
    class ViewHolder{
    	private TextView item_first_tv;
    }
}
