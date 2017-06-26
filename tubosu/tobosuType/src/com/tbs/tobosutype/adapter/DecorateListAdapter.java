package com.tbs.tobosutype.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DecorateCompanyDetailActivity;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
	/**
	 * 找装修页面中 装修公司列表适配器
	 * @author dec
	 *
	 */
public class DecorateListAdapter extends BaseAdapter {
	private static final String TAG = DecorateListAdapter.class.getSimpleName();
	private ArrayList<HashMap<String, String>> list = null;
	private Context context;

	public DecorateListAdapter(Context context, ArrayList<HashMap<String, String>> list) {
		this.context = context;
		this.list = list;
	}

		public ArrayList<HashMap<String, String>> getList() {
			return list;
		}

		public void setList(ArrayList<HashMap<String, String>> list) {
			this.list = list;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		if (convertView == null) {
			mHolder = new ViewHolder(); //jjb_logo
			convertView = LayoutInflater.from(context).inflate(R.layout.item_decorate_list, null);
			mHolder.cardview = (CardView) convertView.findViewById(R.id.cardview);
			mHolder.item_company_name = (TextView) convertView.findViewById(R.id.item_company_name);
			mHolder.item_company_bao = (ImageView) convertView.findViewById(R.id.item_company_bao);
			mHolder.item_decorate_business_license = (ImageView) convertView.findViewById(R.id.item_decorate_business_license);
			mHolder.item_design_num = (TextView) convertView.findViewById(R.id.item_design_num);
			mHolder.item_design_solution = (TextView) convertView.findViewById(R.id.item_design_solution);
			mHolder.item_company_district = (TextView) convertView.findViewById(R.id.item_company_district);
			mHolder.item_company_address = (TextView) convertView.findViewById(R.id.item_company_address);
			mHolder.item_company_distance = (TextView) convertView.findViewById(R.id.item_company_distance);
			mHolder.item_decorate_logo = (ImageView) convertView.findViewById(R.id.item_decorate_logo);
			mHolder.rl_activity = (RelativeLayout) convertView.findViewById(R.id.rl_activity);
			mHolder.item_activity_title = (TextView) convertView.findViewById(R.id.item_activity_title);
			mHolder.item_iv_hot = (ImageView) convertView.findViewById(R.id.iv_hot);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		String comsimpname = list.get(position).get("comsimpname");
		if (comsimpname.length() > 8) {
			comsimpname = comsimpname.substring(0, 8);
			comsimpname += "...";
		}
		mHolder.item_company_name.setText(comsimpname);
		mHolder.item_company_district.setText(list.get(position).get("districtid"));
		
		String address = null;
		if (list.get(position).get("address").length() > 10) {
			address = list.get(position).get("address").substring(0, 10) + "...";
		} else {
			address = list.get(position).get("address");
		}
//		//地址过长，后面加省略号，然后加多少km
//		if (list.get(position).get("dis") != null && list.get(position).get("dis").length() > 0) {
//			address += " " + list.get(position).get("dis");
//		}
		mHolder.item_company_address.setText(address);
		mHolder.item_company_distance.setText(list.get(position).get("dis"));

		setTextColor(mHolder.item_design_num, list.get(position).get("casenormalcount"), "套设计");
//		mHolder.item_design_num.setText(list.get(position).get("casenormalcount") + "套设计");
		
		if ("1".equals(list.get(position).get("is_hot"))) {
			mHolder.item_iv_hot.setVisibility(View.VISIBLE);
		} else {
			mHolder.item_iv_hot.setVisibility(View.GONE);
		}
		
		if (list.get(position).get("anliCount").equals("0")) {
			mHolder.item_design_solution.setVisibility(View.GONE);
		} else {
			mHolder.item_design_solution.setVisibility(View.VISIBLE);
//			mHolder.item_design_solution.setText(list.get(position).get("anliCount") + "套案例");
			setTextColor(mHolder.item_design_solution, list.get(position).get("anliCount"), "套案例");
		}

		
//		if(position < 5){
//			Log.d(TAG, "公司案例数量"+list.get(position).get("anliCount"));
//			Log.d(TAG, "公司案例数量"+list.get(position).get("casenormalcount"));
//			Log.d(TAG, "公司距离是 【"+list.get(position).get("dis")+"】");
//		}
		
		if ("1".equals(list.get(position).get("activity"))) {
			mHolder.rl_activity.setVisibility(View.VISIBLE);
			mHolder.item_activity_title.setText(list.get(position).get("activityTitle"));
		} else {
			mHolder.rl_activity.setVisibility(View.GONE);
		}
		
		
		// 认证
		final String certificationUrl = list.get(position).get("certification");
		if ("0".equals(certificationUrl)) {
			mHolder.item_decorate_business_license.setVisibility(View.GONE);
		} else {
			mHolder.item_decorate_business_license.setVisibility(View.VISIBLE);
		}
		
		// 装修保
		final String zxb_logo = list.get(position).get("jjb_logo");
		if ("0".equals(zxb_logo)) {
			mHolder.item_company_bao.setVisibility(View.GONE);
		} else {
			mHolder.item_company_bao.setVisibility(View.VISIBLE);
		}
		
		
		final String logoUrl = list.get(position).get("logosmall");
		if ("".equals(logoUrl)) {
			mHolder.item_decorate_logo.setImageResource(R.drawable.decorate_default);
		} else {
			ImageLoaderUtil.loadImage(context, mHolder.item_decorate_logo, logoUrl);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String comid = list.get(position).get("comid");
				Intent detailIntent = new Intent(context, DecorateCompanyDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("comid", comid);
				detailIntent.putExtras(bundle);
				context.startActivity(detailIntent);
			}
		});
		return convertView;
	}

	class ViewHolder {

		private CardView cardview;
		/**装修公司名称*/
		private TextView item_company_name;
		/**装修保*/
		private ImageView item_company_bao;
		/**公司认证*/
		private ImageView item_decorate_business_license;
		/**装修公司有多少套设计*/
		private TextView item_design_num;
		/**装修公司有多少套案例*/
		private TextView item_design_solution;
		/**装修公司所在区域*/
		private TextView item_company_district;
		/**装修公司具体地址*/
		private TextView item_company_address;
		/**装修公司距离*/
		private TextView item_company_distance;
		/**装修公司logo*/
		private ImageView item_decorate_logo;
		/**热门装修公司标记*/
		private ImageView item_iv_hot;
		/**有优惠活动布局*/
		private RelativeLayout rl_activity;
		/**优惠活动标题*/
		private TextView item_activity_title;
	}


	private void setTextColor(TextView tv, String text, String string){
		tv.setText((Html.fromHtml("<font color= '#FF9B58'>" +text+"</font>")+string));

	}
}
