package com.tbs.tobosutype.fragment;

import java.util.ArrayList;

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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.ChooseActivity;
	/**
	 * 获取面积fragment
	 * @author dec
	 *
	 */
public class ChooseDecorateSquareFragment extends Fragment implements OnClickListener{
	private static final String TAG = ChooseDecorateSquareFragment.class.getSimpleName();
	
	private RelativeLayout relBelow60, relAvove60, relAbove90, relAbove120, relAbove150, relAbove180;
	
	private ImageView ivBelow60, ivAvove60, ivAbove90, ivAbove120, ivAbove150, ivAbove180;
	
	private TextView tvBelow60, tvAvove60, tvAbove90, tvAbove120, tvAbove150, tvAbove180;
	
	private static String houseSquare = "";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_choose_decorate_square, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		relBelow60 = (RelativeLayout) view.findViewById(R.id.rel_square_below60);
		relBelow60.setOnClickListener(this);
		relAvove60 = (RelativeLayout) view.findViewById(R.id.rel_square_above60);
		relAvove60.setOnClickListener(this);
		relAbove90 = (RelativeLayout) view.findViewById(R.id.rel_square_above90);
		relAbove90.setOnClickListener(this);
		relAbove120 = (RelativeLayout) view.findViewById(R.id.rel_square_above120);
		relAbove120.setOnClickListener(this);
		relAbove150 = (RelativeLayout) view.findViewById(R.id.rel_square_above150);
		relAbove150.setOnClickListener(this);
		relAbove180 = (RelativeLayout) view.findViewById(R.id.rel_square_above180);
		relAbove180.setOnClickListener(this);
		
		ivBelow60 = (ImageView) view.findViewById(R.id.iv_square_below60);
		ivAvove60 = (ImageView) view.findViewById(R.id.iv_square_above60);
		ivAbove90 = (ImageView) view.findViewById(R.id.iv_square_above90);
		ivAbove120 = (ImageView) view.findViewById(R.id.iv_square_above120);
		ivAbove150 = (ImageView) view.findViewById(R.id.iv_square_above150);
		ivAbove180 = (ImageView) view.findViewById(R.id.iv_square_above180);
		
		tvBelow60 = (TextView) view.findViewById(R.id.tv_square_below60);
		tvAvove60 = (TextView) view.findViewById(R.id.tv_square_above60);
		tvAbove90 = (TextView) view.findViewById(R.id.tv_square_above90);
		tvAbove120 = (TextView) view.findViewById(R.id.tv_square_above120);
		tvAbove150 = (TextView) view.findViewById(R.id.tv_square_above150);
		tvAbove180 = (TextView) view.findViewById(R.id.tv_square_above180);
		
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rel_square_below60:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(1));
			break;
		case R.id.rel_square_above60:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(2));
			break;
		case R.id.rel_square_above90:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(3));
			break;
		case R.id.rel_square_above120:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(4));
			break;
		case R.id.rel_square_above150:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(5));
			break;
		case R.id.rel_square_above180:
			ChooseActivity.chooseString.remove(1);
			ChooseActivity.chooseString.add(1, getDecorateSquare(6));
			break;
		default:
			break;
		}
		Bundle b = new Bundle();
		b.putInt("Pager_Position_Bundle", 2);
		Intent intent = new Intent();
		intent.setAction(ChooseActivity.SET_PAGE_POSITION_ACTION);
		intent.putExtra("Pager_Postion_Intent", b);
		getActivity().sendBroadcast(intent);
	}
	
	private String getDecorateSquare(int chooseFlag){
		switch (chooseFlag) {
		case 1:
			ivBelow60.setBackgroundResource(R.drawable.square_selected);
			ivAvove60.setBackgroundResource(R.drawable.square_normal);
			ivAbove90.setBackgroundResource(R.drawable.square_normal);
			ivAbove120.setBackgroundResource(R.drawable.square_normal);
			ivAbove150.setBackgroundResource(R.drawable.square_normal);
			ivAbove180.setBackgroundResource(R.drawable.square_normal);
			tvBelow60.setTextColor(Color.parseColor("#46300F")); 
			tvAvove60.setTextColor(Color.parseColor("#92744D"));
			tvAbove90.setTextColor(Color.parseColor("#92744D"));
			tvAbove120.setTextColor(Color.parseColor("#92744D"));
			tvAbove150.setTextColor(Color.parseColor("#92744D"));
			tvAbove180.setTextColor(Color.parseColor("#92744D"));
			houseSquare = "60";
			break;
		case 2:
			ivBelow60.setBackgroundResource(R.drawable.square_normal);
			ivAvove60.setBackgroundResource(R.drawable.square_selected);
			ivAbove90.setBackgroundResource(R.drawable.square_normal);
			ivAbove120.setBackgroundResource(R.drawable.square_normal);
			ivAbove150.setBackgroundResource(R.drawable.square_normal);
			ivAbove180.setBackgroundResource(R.drawable.square_normal);
			tvBelow60.setTextColor(Color.parseColor("#92744D")); 
			tvAvove60.setTextColor(Color.parseColor("#46300F"));
			tvAbove90.setTextColor(Color.parseColor("#92744D"));
			tvAbove120.setTextColor(Color.parseColor("#92744D"));
			tvAbove150.setTextColor(Color.parseColor("#92744D"));
			tvAbove180.setTextColor(Color.parseColor("#92744D"));
			houseSquare = "75"; // 60-90中位数
			break;
		case 3:
			ivBelow60.setBackgroundResource(R.drawable.square_normal);
			ivAvove60.setBackgroundResource(R.drawable.square_normal);
			ivAbove90.setBackgroundResource(R.drawable.square_selected);
			ivAbove120.setBackgroundResource(R.drawable.square_normal);
			ivAbove150.setBackgroundResource(R.drawable.square_normal);
			ivAbove180.setBackgroundResource(R.drawable.square_normal);
			tvBelow60.setTextColor(Color.parseColor("#92744D")); 
			tvAvove60.setTextColor(Color.parseColor("#92744D"));
			tvAbove90.setTextColor(Color.parseColor("#46300F"));
			tvAbove120.setTextColor(Color.parseColor("#92744D"));
			tvAbove150.setTextColor(Color.parseColor("#92744D"));
			tvAbove180.setTextColor(Color.parseColor("#92744D"));
			houseSquare = "105";//90-120中位数
			break;
		case 4:
			ivBelow60.setBackgroundResource(R.drawable.square_normal);
			ivAvove60.setBackgroundResource(R.drawable.square_normal);
			ivAbove90.setBackgroundResource(R.drawable.square_normal);
			ivAbove120.setBackgroundResource(R.drawable.square_selected);
			ivAbove150.setBackgroundResource(R.drawable.square_normal);
			ivAbove180.setBackgroundResource(R.drawable.square_normal);
			tvBelow60.setTextColor(Color.parseColor("#92744D")); 
			tvAvove60.setTextColor(Color.parseColor("#92744D"));
			tvAbove90.setTextColor(Color.parseColor("#92744D"));
			tvAbove120.setTextColor(Color.parseColor("#46300F"));
			tvAbove150.setTextColor(Color.parseColor("#92744D"));
			tvAbove180.setTextColor(Color.parseColor("#92744D"));
			houseSquare = "135"; // 120-150中位数
			break;
		case 5:
			ivBelow60.setBackgroundResource(R.drawable.square_normal);
			ivAvove60.setBackgroundResource(R.drawable.square_normal);
			ivAbove90.setBackgroundResource(R.drawable.square_normal);
			ivAbove120.setBackgroundResource(R.drawable.square_normal);
			ivAbove150.setBackgroundResource(R.drawable.square_selected);
			ivAbove180.setBackgroundResource(R.drawable.square_normal);
			tvBelow60.setTextColor(Color.parseColor("#92744D")); 
			tvAvove60.setTextColor(Color.parseColor("#92744D"));
			tvAbove90.setTextColor(Color.parseColor("#92744D"));
			tvAbove120.setTextColor(Color.parseColor("#92744D"));
			tvAbove150.setTextColor(Color.parseColor("#46300F"));
			tvAbove180.setTextColor(Color.parseColor("#92744D"));
			houseSquare = "165"; // 150-180中位数
			break;
		case 6:
			ivBelow60.setBackgroundResource(R.drawable.square_normal);
			ivAvove60.setBackgroundResource(R.drawable.square_normal);
			ivAbove90.setBackgroundResource(R.drawable.square_normal);
			ivAbove120.setBackgroundResource(R.drawable.square_normal);
			ivAbove150.setBackgroundResource(R.drawable.square_normal);
			ivAbove180.setBackgroundResource(R.drawable.square_selected);
			tvBelow60.setTextColor(Color.parseColor("#92744D")); 
			tvAvove60.setTextColor(Color.parseColor("#92744D"));
			tvAbove90.setTextColor(Color.parseColor("#92744D"));
			tvAbove120.setTextColor(Color.parseColor("#92744D"));
			tvAbove150.setTextColor(Color.parseColor("#92744D"));
			tvAbove180.setTextColor(Color.parseColor("#46300F"));
			houseSquare = "180";
			break;
		default:
			break;
		}
		return houseSquare;
	}
	
}
