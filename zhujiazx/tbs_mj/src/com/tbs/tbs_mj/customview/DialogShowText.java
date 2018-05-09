package com.tbs.tbs_mj.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tbs.tbs_mj.R;

import java.util.ArrayList;


public class DialogShowText extends Dialog {
	private ImageView iv_dialog_dismiss;
	private ListView dialogList;
	private ArrayList<String> stringList = new ArrayList<String>();
	private ArrayList<Integer> poList = new ArrayList<Integer>();

	public DialogShowText(final Context context) {
		super(context, R.style.warmDialog);
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_showtext, null);
		iv_dialog_dismiss = (ImageView) view.findViewById(R.id.iv_dialog_dismiss);
		dialogList = (ListView) view.findViewById(R.id.dialog_list);

		stringList.add("免费量房设计");
		stringList.add("免费预算报价");
		stringList.add("企业认证");
		stringList.add("业主保障");

		// 要改
		poList.add(R.drawable.tem05);
		poList.add(R.drawable.tem05);
		poList.add(R.drawable.tem05);
		poList.add(R.drawable.tem05);

		DialogAdapter adapter = new DialogAdapter(context, stringList, poList);
		dialogList.setAdapter(adapter);
		adapter.notifyDataSetChanged();


		iv_dialog_dismiss.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		super.setContentView(view);
	}


	private class DialogAdapter extends BaseAdapter{
		private ArrayList<Integer> positionList;
		private ArrayList<String> textList;
		private Context context;
		private LayoutInflater inflater;

		public DialogAdapter(Context context, ArrayList<String> textList, ArrayList<Integer> positionList){
			this.context = context;
			this.textList = textList;
			this.inflater = LayoutInflater.from(context);
			this.positionList = positionList;
		}

		@Override
		public int getCount() {
			return positionList.size();
		}

		@Override
		public Object getItem(int position) {
			return positionList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView==null){
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_dialog_list_layout, null);
				holder.tv = (TextView) convertView.findViewById(R.id.tv_dialog);
				holder.iv = (ImageView) convertView.findViewById(R.id.iv_dialog);
				convertView.setTag(holder);

			}else{
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv.setText(textList.get(position));
			holder.iv.setBackgroundResource(positionList.get(position));
			return convertView;
		}
	}

	class ViewHolder{
		ImageView iv;
		TextView tv;
	}

}
