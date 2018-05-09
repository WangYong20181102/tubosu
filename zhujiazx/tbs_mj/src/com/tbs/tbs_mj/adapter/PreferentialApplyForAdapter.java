package com.tbs.tbs_mj.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.customview.CallDialogCompany;
import com.tbs.tbs_mj.utils.ImageLoaderUtil;
	/**
	 * 装修公司跳转到优惠报名页面的优惠报名订单适配器
	 * @author dec
	 *
	 */
public class PreferentialApplyForAdapter extends BaseAdapter{
	private static final String TAG = "PreferentialApplyForAdapter";
	private Context mContext;
	private LayoutInflater inflater;
	
	private ArrayList<HashMap<String, Object>> dataList;
	
	private int checkedNum = 0; 
	
	private Window window;
	private android.view.WindowManager.LayoutParams params;
	
	
	public PreferentialApplyForAdapter(Context mContext, ArrayList<HashMap<String, Object>> dataList){
		this.mContext = mContext;
		this.dataList = dataList;
		this.inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if(dataList==null){
			return 0;
		}
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = inflater.inflate(R.layout.item_preferential_applyfor, null);
			holder = new ViewHolder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_Icon);
			holder.tvPhoneNum = (TextView) convertView.findViewById(R.id.tv_phone_num);
			holder.tvAddr = (TextView) convertView.findViewById(R.id.tv_addr);
//			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_applyfor_time);
			holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tv_applyfor_time);
			holder.tvIsChecked = (TextView) convertView.findViewById(R.id.tv_ischecked);
			holder.tvDelete = (TextView) convertView.findViewById(R.id.tv_delete);
			holder.ivCallPhone = (ImageView) convertView.findViewById(R.id.iv_callphone);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// 数据设置
		String coverUrl = dataList.get(position).get("cover").toString();
		ImageLoaderUtil.loadImage(mContext, holder.ivIcon, coverUrl);
		
		// signupid 是什么？
		String signupid = dataList.get(position).get("signupid").toString();
		
//		if("2".equals(signupid)){
//			
//		}else if("3".equals(signupid)){
//			
//		}
		
		
		holder.tvPhoneNum.setText(dataList.get(position).get("mobile").toString());
		holder.tvAddr.setText(dataList.get(position).get("mobileAddress").toString());
		
		//FIXME 为什么没有日期？
//		holder.tvDate.setText(dataList.get(position).get("").toString());
		holder.tvApplyTime.setText(dataList.get(position).get("signuptime")+" 报名");
		
		String isChecked = dataList.get(position).get("issee").toString();
		
		if("1".equals(isChecked)){ // 1 --> 已经查看
			holder.tvIsChecked.setBackgroundResource(R.drawable.shape_btn_checked);
			holder.tvIsChecked.setTextColor(Color.parseColor("#74777A"));
			holder.tvIsChecked.setText("已查看");
			holder.tvIsChecked.setClickable(false);
		}else if("0".equals(isChecked)){  // 0 --> 未查看
			holder.tvIsChecked.setOnClickListener(new OnCheckedClickListener(holder.tvIsChecked));
		}
		
		
		//FIXME 没有delete？
//		holder.tvDelete.setText(dataList.get(position).get("").toString());
		
		final String cellphone = dataList.get(position).get("mobile").toString();
		
		holder.ivCallPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 打电话 
				CallDialogCompany callDialog = new CallDialogCompany(mContext, R.style.callDialogTheme, cellphone, "");
				window = callDialog.getWindow();
				params = window.getAttributes();
				params.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
				window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
				callDialog.show();
			}
		});
		
		return convertView;
	}

	class OnCheckedClickListener implements OnClickListener{
		
		private TextView textview;
		
		public OnCheckedClickListener(TextView textview){
			this.textview = textview;
		}

		@Override
		public void onClick(View v) {
			checkedNum++;
			textview.setBackgroundResource(R.drawable.shape_btn_notchecked);
			textview.setClickable(false);
			Intent i = new Intent("check_preferential_applyfor_num");
			Bundle b = new Bundle();
			b.putInt("CHECKED_NUM", checkedNum);
			i.putExtra("Checked_Bundle_Num", b);
			mContext.sendBroadcast(i);
		}
		
	}

	static class ViewHolder{
		ImageView ivIcon;
		TextView tvPhoneNum;
		TextView tvAddr;
		TextView tvApplyTime;
		TextView tvIsChecked;
		TextView tvDelete;
		ImageView ivCallPhone;
		
		
	}
}
