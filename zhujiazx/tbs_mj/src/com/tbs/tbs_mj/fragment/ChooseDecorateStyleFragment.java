package com.tbs.tbs_mj.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.ChooseActivity;
	/**
	 * 获取风格fragment
	 * @author dec
	 *
	 */
public class ChooseDecorateStyleFragment extends Fragment implements OnClickListener {
	private static final String TAG = ChooseDecorateStyleFragment.class.getSimpleName();
	
	// 田园  现代  地中海  中式 美式  欧式
	private RelativeLayout rel_farm_style, rel_modern_style, rel_med_style, rel_chinese_style, rel_usa_style, rel_europ_style;
	
	private ImageView iv_farm_style, iv_modern_style,iv_med_style,iv_chinese_style,iv_usa_style,iv_europ_style;
	
	private ImageView iv_farm_style_ok,iv_modern_style_ok,iv_med_style_ok,iv_chinese_style_ok,iv_usa_style_ok,iv_europ_style_ok;
	
	private TextView tv_farm_style, tv_modern_style,tv_med_style,tv_chinese_style,tv_usa_style,tv_europ_style;
	
	private static String houseStyle = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_choose_decorate_style, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		
		rel_farm_style = (RelativeLayout) view.findViewById(R.id.rel_farm_style);
		rel_farm_style.setOnClickListener(this);
		rel_modern_style = (RelativeLayout) view.findViewById(R.id.rel_modern_style);
		rel_modern_style.setOnClickListener(this);
		rel_med_style = (RelativeLayout) view.findViewById(R.id.rel_med_style);
		rel_med_style.setOnClickListener(this);
		rel_chinese_style = (RelativeLayout) view.findViewById(R.id.rel_chinese_style);
		rel_chinese_style.setOnClickListener(this);
		rel_usa_style = (RelativeLayout) view.findViewById(R.id.rel_usa_style);
		rel_usa_style.setOnClickListener(this);
		rel_europ_style = (RelativeLayout) view.findViewById(R.id.rel_europ_style);
		rel_europ_style.setOnClickListener(this);
		
		
		iv_farm_style = (ImageView) view.findViewById(R.id.iv_farm_style);
		iv_modern_style = (ImageView) view.findViewById(R.id.iv_modern_style);
		iv_med_style = (ImageView) view.findViewById(R.id.iv_med_style);
		iv_chinese_style = (ImageView) view.findViewById(R.id.iv_chinese_style);
		iv_usa_style = (ImageView) view.findViewById(R.id.iv_usa_style);
		iv_europ_style = (ImageView) view.findViewById(R.id.iv_europ_style);
		
		
		tv_farm_style = (TextView) view.findViewById(R.id.tv_farm_style);
		tv_modern_style = (TextView) view.findViewById(R.id.tv_modern_style);
		tv_med_style = (TextView) view.findViewById(R.id.tv_med_style);
		tv_chinese_style = (TextView) view.findViewById(R.id.tv_chinese_style);
		tv_usa_style = (TextView) view.findViewById(R.id.tv_usa_style);
		tv_europ_style = (TextView) view.findViewById(R.id.tv_europ_style);
		
		
		iv_farm_style_ok = (ImageView) view.findViewById(R.id.iv_farm_style_ok);
		iv_modern_style_ok = (ImageView) view.findViewById(R.id.iv_modern_style_ok);
		iv_med_style_ok = (ImageView) view.findViewById(R.id.iv_med_style_ok);
		iv_chinese_style_ok = (ImageView) view.findViewById(R.id.iv_chinese_style_ok);
		iv_usa_style_ok = (ImageView) view.findViewById(R.id.iv_usa_style_ok);
		iv_europ_style_ok = (ImageView) view.findViewById(R.id.iv_europ_style_ok);
		
		if("田园".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.VISIBLE);
			iv_farm_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
		}else if("现代".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.VISIBLE);
			iv_modern_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
		}else if("地中海".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.VISIBLE);
			iv_med_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
		}else if("中式".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.VISIBLE);
			iv_chinese_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
		}else if("美式".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.VISIBLE);
			iv_usa_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_europ_style_ok.setVisibility(View.GONE);
		}else if("欧式".equals(ChooseActivity.chooseString.get(0))){
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.VISIBLE);
			iv_europ_style_ok.setBackgroundResource(R.drawable.selected_ok);
		}
	}
	
	
	private String getDecorateStyle(int seletedNum){
		switch (seletedNum) {
		case 1:
			iv_farm_style_ok.setVisibility(View.VISIBLE);
			iv_farm_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
			houseStyle = "田园";
			break;
		case 2:
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.VISIBLE);
			iv_modern_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
			houseStyle = "现代";
			break;

		case 3:
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.VISIBLE);
			iv_med_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
			houseStyle = "地中海";
			break;

		case 4:
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.VISIBLE);
			iv_chinese_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.GONE);
			houseStyle = "中式";
			break;

		case 5:
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.VISIBLE);
			iv_usa_style_ok.setBackgroundResource(R.drawable.selected_ok);
			iv_europ_style_ok.setVisibility(View.GONE);
			houseStyle = "美式";
			break;

		case 6:
			iv_farm_style_ok.setVisibility(View.GONE);
			iv_modern_style_ok.setVisibility(View.GONE);
			iv_med_style_ok.setVisibility(View.GONE);
			iv_chinese_style_ok.setVisibility(View.GONE);
			iv_usa_style_ok.setVisibility(View.GONE);
			iv_europ_style_ok.setVisibility(View.VISIBLE);
			iv_europ_style_ok.setBackgroundResource(R.drawable.selected_ok);
			houseStyle = "欧式";
			break;
		default:
			break;
		}
		return houseStyle;
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_farm_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(1));
			break;
		case R.id.rel_modern_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(2));
			break;
		case R.id.rel_med_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(3));
			break;
		case R.id.rel_chinese_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(4));
			break;
		case R.id.rel_usa_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(5));
			break;
		case R.id.rel_europ_style:
			ChooseActivity.chooseString.remove(0);
			ChooseActivity.chooseString.add(0, getDecorateStyle(6));
			break;
		default:
			break;
		}
		
		Intent intent = new Intent();
		intent.setAction(ChooseActivity.SET_PAGE_POSITION_ACTION);
		Bundle b = new Bundle();
		b.putInt("Pager_Position_Bundle", 1);
		intent.putExtra("Pager_Postion_Intent", b);
		getActivity().sendBroadcast(intent);
	}
}
