package com.tbs.tbs_mj.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.CalculaterActivity;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.ShareUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WallBrickFragment extends Fragment {
	private static final String TAG = WallBrickFragment.class.getSimpleName();

	private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	private ArrayList<ImageView> clearImageList = new ArrayList<ImageView>();

	private EditText etWallroomLength, etWallroomWidth, etWallroomHeight;
	private String roomLen = "";
	private String roomWid = "";
	private String roomHei = "";
	private ImageView ivClearLength, ivClearWidth, ivClearHeight;

	private EditText etWallDoorWidth, etWallDoorHeight, etWallDoorNum;
	private String doorNum = "";
	private String doorWid = "";
	private String doorHei = "";
	private ImageView ivClearDoorWidth, ivClearDoorHeight,ivClearDoorNum;

	private EditText etWallWindowWidth, etWallWindowHeight, etWallWindowNum;
	private String windowNum = "";
	private String windowWid = "";
	private String windowHei = "";
	private ImageView ivClearWindowWidth,ivClearWindowHeight,ivClearWindowNum;

	private String wallBrickText = "";

	private TextView tvUnitPrice;
	private EditText windowBrickUnitPrice;
	private String windowUnitPrice = "";

	private TextView tvWallbrickTotalPrice;
	private TextView tvEmptyText;

	private TextView type200,type300,type400,type500,type600,type3020,type2533;
	private EditText etBrickLength, etBrickWidth;
	private String wallBrickLength = "";
	private String wallBrickWidth = "";

	private Button btnNum1,btnNum2,btnNum3,btnNum4,btnNum5,btnNum6,btnNum7,btnNum8,btnNum9,btnNum0;
	private Button btnDot,btnUp,btnDown,btnBackOff,btnCalculate;
	private RelativeLayout relBackOff;



	private LinearLayout llCalculaterLayout;

	private RelativeLayout relCalculateResultLayout;
	
	private TextView tvWallBrickNum;

	private TextView tvWallShare;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wall_brick, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		etWallroomLength = (EditText) view.findViewById(R.id.et_wallroom_lengh);
		etWallroomWidth = (EditText) view.findViewById(R.id.et_wallroom_width);
		etWallroomHeight = (EditText) view.findViewById(R.id.et_wallroom_height);
		ivClearLength = (ImageView) view.findViewById(R.id.iv_clear_room_lengh);
		ivClearWidth = (ImageView) view.findViewById(R.id.iv_clear_room_width);
		ivClearHeight = (ImageView) view.findViewById(R.id.iv_clear_room_height);
		clearImageList.add(ivClearLength);
		clearImageList.add(ivClearWidth);
		clearImageList.add(ivClearHeight);
		editTextList.add(etWallroomLength);
		editTextList.add(etWallroomWidth);
		editTextList.add(etWallroomHeight);

		etWallDoorNum = (EditText) view.findViewById(R.id.et_door_num);
		etWallDoorWidth = (EditText) view.findViewById(R.id.et_door_width);
		etWallDoorHeight = (EditText) view.findViewById(R.id.et_door_height);

		ivClearDoorNum = (ImageView) view.findViewById(R.id.iv_clear_door_num);
		ivClearDoorWidth = (ImageView) view.findViewById(R.id.iv_clear_door_width);
		ivClearDoorHeight = (ImageView) view.findViewById(R.id.iv_clear_door_height);
		editTextList.add(etWallDoorWidth);
		editTextList.add(etWallDoorHeight);
		editTextList.add(etWallDoorNum);

		etWallWindowNum = (EditText) view.findViewById(R.id.et_window_num);
		etWallWindowWidth = (EditText) view.findViewById(R.id.et_window_width);
		etWallWindowHeight = (EditText) view.findViewById(R.id.et_window_height);
		ivClearWindowNum = (ImageView) view.findViewById(R.id.iv_clear_window_num);
		ivClearWindowWidth = (ImageView) view.findViewById(R.id.iv_clear_window_width);
		ivClearWindowHeight = (ImageView) view.findViewById(R.id.iv_clear_window_height);
		editTextList.add(etWallWindowWidth);
		editTextList.add(etWallWindowHeight);
		editTextList.add(etWallWindowNum);

		etBrickLength = (EditText) view.findViewById(R.id.wall_brick_length);
		etBrickWidth = (EditText) view.findViewById(R.id.wall_brick_width);
//		editTextList.add(etBrickLength);
//		editTextList.add(etBrickWidth);

		type200 = (TextView) view.findViewById(R.id.wall_brick_type200);
		type300 = (TextView) view.findViewById(R.id.wall_brick_type300);
		type400 = (TextView) view.findViewById(R.id.wall_brick_type400);
		type500 = (TextView) view.findViewById(R.id.wall_brick_type500);
		type600 = (TextView) view.findViewById(R.id.wall_brick_type600);
		type3020 = (TextView) view.findViewById(R.id.tv_brick_type3020);
		type2533 = (TextView) view.findViewById(R.id.wall_brick_type2533);

		windowBrickUnitPrice = (EditText) view.findViewById(R.id.et_wallbrick_unit_price);
		tvUnitPrice = (TextView) view.findViewById(R.id.wallbrick_unit_price);

		tvWallbrickTotalPrice = (TextView) view.findViewById(R.id.tv_wallbrick_total_price);
		tvEmptyText = (TextView) view.findViewById(R.id.tv_empty);

		btnNum0 = (Button) view.findViewById(R.id.btn_num0);
		btnNum1 = (Button) view.findViewById(R.id.btn_num1);
		btnNum2 = (Button) view.findViewById(R.id.btn_num2);
		btnNum3 = (Button) view.findViewById(R.id.btn_num3);
		btnNum4 = (Button) view.findViewById(R.id.btn_num4);
		btnNum5 = (Button) view.findViewById(R.id.btn_num5);
		btnNum6 = (Button) view.findViewById(R.id.btn_num6);
		btnNum7 = (Button) view.findViewById(R.id.btn_num7);
		btnNum8 = (Button) view.findViewById(R.id.btn_num8);
		btnNum9 = (Button) view.findViewById(R.id.btn_num9);

		btnDot = (Button) view.findViewById(R.id.btn_dot);
		relBackOff = (RelativeLayout) view.findViewById(R.id.rel_backoff);
		btnBackOff = (Button) view.findViewById(R.id.btn_backoff);
		btnUp = (Button) view.findViewById(R.id.btn_up);
		btnDown = (Button) view.findViewById(R.id.btn_down);
		btnCalculate = (Button) view.findViewById(R.id.btn_calculate);

		llCalculaterLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_walllayout);
		llCalculaterLayout.setVisibility(View.VISIBLE);
		relCalculateResultLayout = (RelativeLayout) view.findViewById(R.id.rel_calculate_wallresult);
		relCalculateResultLayout.setVisibility(View.GONE);
		tvWallBrickNum = (TextView) view.findViewById(R.id.tv_wallbrick_num);
		tvWallShare = (TextView) view.findViewById(R.id.tv_wall_share);

		setClickListener();

	}

	private void setClickListener() {
		/*----------------房------------------*/
		etWallroomLength.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomLen = etWallroomLength.getText().toString().trim();
//				roomLen = CalculaterActivity.clearDot(roomLen);
				if(roomLen.length()>0 && etWallroomLength.hasFocus()){
					ivClearLength.setVisibility(View.VISIBLE);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});

//		etWallroomLength.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallroomLength);
		etWallroomLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallroomLength);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});



		ivClearLength.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallroomLength.setText("");
			}
		});



		etWallroomWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomWid = etWallroomWidth.getText().toString().trim();
//				roomWid = CalculaterActivity.clearDot(roomWid);
				if(roomWid.length()>0 && etWallroomWidth.hasFocus()){
					ivClearWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});
//		etWallroomWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallroomWidth);
		etWallroomWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallroomWidth);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallroomWidth.setText("");
			}
		});

		etWallroomHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomHei = etWallroomHeight.getText().toString().trim();
//				roomHei = CalculaterActivity.clearDot(roomHei);
				if(roomHei.length()>0 && etWallroomHeight.hasFocus()){
					ivClearHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});
//		etWallroomHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallroomHeight);
		etWallroomHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallroomHeight);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});

		ivClearHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallroomHeight.setText("");
			}
		});




		/*----------------门------------------*/
		etWallDoorNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorNum = etWallDoorNum.getText().toString().trim();

//				doorNum = CalculaterActivity.clearDot(doorNum);
				if(doorNum.length()>0 && etWallDoorNum.hasFocus()){
					ivClearDoorNum.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorNum.setVisibility(View.GONE);
				}
			}
		});
//		etWallDoorNum.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallDoorNum);
		etWallDoorNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallDoorNum);
				}else{
					ivClearDoorNum.setVisibility(View.GONE);
				}
			}
		});

		ivClearDoorNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallDoorNum.setText("");
			}
		});

		etWallDoorWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorWid = etWallDoorWidth.getText().toString().trim();
//				doorWid = CalculaterActivity.clearDot(doorWid);
				if(doorWid.length()>0 && etWallDoorWidth.hasFocus()){
					ivClearDoorWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorWidth.setVisibility(View.GONE);
				}
			}
		});
//		etWallDoorWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallDoorWidth);
		etWallDoorWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallDoorWidth);
				}else{
					ivClearDoorWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearDoorWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallDoorWidth.setText("");
			}
		});



		etWallDoorHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorHei = etWallDoorHeight.getText().toString().trim();
//				doorHei = CalculaterActivity.clearDot(doorHei);
				if(doorHei.length()>0 && etWallDoorHeight.hasFocus()){
					ivClearDoorHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorHeight.setVisibility(View.GONE);
				}
			}
		});
//		etWallDoorHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallDoorHeight);
		etWallDoorHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallDoorHeight);
				}else{
					ivClearDoorHeight.setVisibility(View.GONE);
				}
			}
		});
		ivClearDoorHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallDoorHeight.setText("");
			}
		});


		/*----------------窗户------------------*/
		etWallWindowNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowNum = etWallWindowNum.getText().toString().trim();

//				windowNum = CalculaterActivity.clearDot(windowNum);
				if(windowNum.length()>0&&etWallWindowNum.hasFocus()){
					ivClearWindowNum.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowNum.setVisibility(View.GONE);
				}
			}
		});
//		etWallWindowNum.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallWindowNum);
		etWallWindowNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallWindowNum);
				}else{
					ivClearWindowNum.setVisibility(View.GONE);
				}
			}
		});

		ivClearWindowNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallWindowNum.setText("");
			}
		});


		etWallWindowWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowWid = etWallWindowWidth.getText().toString().trim();
//				windowWid = CalculaterActivity.clearDot(windowWid);
				if(windowWid.length()>0&&etWallWindowWidth.hasFocus()){
					ivClearWindowWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});
//		etWallWindowWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallWindowWidth);
		etWallWindowWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallWindowWidth);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});


		ivClearWindowWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallWindowWidth.setText("");
			}
		});


		etWallWindowHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowHei = etWallWindowHeight.getText().toString().trim();
//				windowHei = CalculaterActivity.clearDot(windowHei);
				if(windowHei.length()>0&&etWallWindowHeight.hasFocus()){
					ivClearWindowHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});
//		etWallWindowHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallWindowHeight);
		etWallWindowHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallWindowHeight);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});

		ivClearWindowHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etWallWindowHeight.setText("");
			}
		});


		/*----------------自定选尺寸------------------*/
		etBrickLength.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				wallBrickWidth = etBrickLength.getText().toString().trim();
//				wallBrickWidth = CalculaterActivity.clearDot(wallBrickWidth);
				if(!"".equals(wallBrickWidth)){
					clearBrickTextView();
				}
			}
		});
//		etBrickLength.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etBrickLength);
		etBrickLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etBrickLength);
				}
			}
		});






		etBrickWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				wallBrickLength = etBrickWidth.getText().toString().trim();
//				wallBrickLength = CalculaterActivity.clearDot(wallBrickLength);
				if(!"".equals(wallBrickLength)){
					clearBrickTextView();
				}
			}
		});
//		etBrickWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etBrickWidth);
		etBrickWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etBrickWidth);
				}
			}
		});




		windowBrickUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowUnitPrice = windowBrickUnitPrice.getText().toString().trim();
//				windowUnitPrice = CalculaterActivity.clearDot(windowUnitPrice);

			}
		});
//		windowBrickUnitPrice.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),windowBrickUnitPrice);
		windowBrickUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(windowBrickUnitPrice);
				}
			}
		});


		tvUnitPrice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(llCalculaterLayout.getVisibility()==View.VISIBLE){
					tvUnitPrice.setText("墙砖单价(元/块)");
					tvUnitPrice.setBackgroundResource(R.color.bg_white);
					windowBrickUnitPrice.setVisibility(View.VISIBLE);
				}
			}
		});

		clickBrickType();

	}


	/**
	 * 选择地砖类型
	 */
	private void clickBrickType() {

		if(!"".equals(etBrickLength.getText().toString().trim()) && !"".equals(etBrickWidth.getText().toString().trim())){
			// 选择自己填写
			clearBrickTextView();

		}else {
			// 默认选中第一个
			selectType(2);
			type200.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(2);
				}
			});
			type300.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(3);
				}
			});
			type400.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(4);
				}
			});
			type500.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(5);
				}
			});
			type600.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(6);
				}
			});
			type3020.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(7);
				}
			});
			type2533.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(8);
				}
			});

		}
	}


	private void selectType(int position){

		switch (position){
			case 2:
				type200.setSelected(true);
				type200.setTextColor(Color.parseColor("#FFFFFF"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type200.getText().toString();
				break;
			case 3:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(true);
				type300.setTextColor(Color.parseColor("#FFFFFF"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type300.getText().toString();
				break;
			case 4:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(true);
				type400.setTextColor(Color.parseColor("#FFFFFF"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type400.getText().toString();
				break;
			case 5:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(true);
				type500.setTextColor(Color.parseColor("#FFFFFF"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type500.getText().toString();
				break;
			case 6:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(true);
				type600.setTextColor(Color.parseColor("#FFFFFF"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type600.getText().toString();
				break;

			case 7:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(true);
				type3020.setTextColor(Color.parseColor("#FFFFFF"));
				type2533.setSelected(false);
				type2533.setTextColor(Color.parseColor("#7F8E9C"));
				wallBrickText = type3020.getText().toString();
				break;

			case 8:
				type200.setSelected(false);
				type200.setTextColor(Color.parseColor("#7F8E9C"));
				type300.setSelected(false);
				type300.setTextColor(Color.parseColor("#7F8E9C"));
				type400.setSelected(false);
				type400.setTextColor(Color.parseColor("#7F8E9C"));
				type500.setSelected(false);
				type500.setTextColor(Color.parseColor("#7F8E9C"));
				type600.setSelected(false);
				type600.setTextColor(Color.parseColor("#7F8E9C"));
				type3020.setSelected(false);
				type3020.setTextColor(Color.parseColor("#7F8E9C"));
				type2533.setSelected(true);
				type2533.setTextColor(Color.parseColor("#FFFFFF"));
				wallBrickText = type2533.getText().toString();
				break;
		}

		if(!"".equals(wallBrickText)){
			if(!"".equals(etBrickLength.getText().toString().trim())){
				etBrickLength.setText("");
			}
			if(!"".equals(etBrickWidth.getText().toString().trim())){
				etBrickWidth.setText("");
			}
			clearFocuse();
		}
	}


	private void clearFocuse(){
		etBrickWidth.clearFocus();
		etBrickLength.clearFocus();
	}


	/**
	 * 清除默认的类型
	 */
	private void clearBrickTextView(){
		type200.setSelected(false);
		type200.setTextColor(Color.parseColor("#7F8E9C"));
		type300.setSelected(false);
		type300.setTextColor(Color.parseColor("#7F8E9C"));
		type400.setSelected(false);
		type400.setTextColor(Color.parseColor("#7F8E9C"));
		type500.setSelected(false);
		type500.setTextColor(Color.parseColor("#7F8E9C"));
		type600.setSelected(false);
		type600.setTextColor(Color.parseColor("#7F8E9C"));
		type3020.setSelected(false);
		type3020.setTextColor(Color.parseColor("#7F8E9C"));
		type2533.setSelected(false);
		type2533.setTextColor(Color.parseColor("#7F8E9C"));

		wallBrickText = "";
	}



	private void doCalculate(final EditText et){
		llCalculaterLayout.setVisibility(View.VISIBLE);
		relCalculateResultLayout.setVisibility(View.GONE);

		btnNum0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 0;
					if("00".equals(str) || "0.000".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}else{
						et.setText(str);
					}
				}
				et.setSelection(str.length());
			}
		});


		btnNum1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 1;
					if("01".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}

			}
		});
		btnNum2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 2;
					if("02".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 3;
					if("03".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 4;
					if("04".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 5;
					if("05".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 6;
					if("06".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 7;
					if("07".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 8;
					if("08".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnNum9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String str = et.getText().toString();
				if(str.length()>=0 && str.length()<5){
					str = str + 9;
					if("09".equals(str)){
						et.setText("0");
						et.setSelection(1);
						return;
					}
					et.setText(str);
					et.setSelection(str.length());
				}
			}
		});
		btnDot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				String str = et.getText().toString();
				if(str.indexOf(".") != -1) {
					//判断字符串中是否已经包含了小数点，如果有就什么也不做
				}else {
					//如果没有小数点
					if(str.equals("0") || str.equals("")){
						//如果开始为0
						str = "0.";
						et.setText(str.toString());
					}else{
						str = str + ".";
						et.setText(str);
						if(str.length()==6){
							et.setSelection(str.length()-1);
							return;
						}
					}
					et.setSelection(str.length());
				}
			}
		});




		/*========================计算器退格================================*/
		relBackOff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String str =et.getText().toString();
				if(str.length() > 0){
					str = str.substring(0, str.length() - 1);
					et.setText(str);
				}
				et.setSelection(str.length());
			}
		});
		btnBackOff.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				String str =et.getText().toString();
				if(str.length() > 0){
					str = str.substring(0, str.length() - 1);
					et.setText(str);
				}
				et.setSelection(str.length());
			}
		});
		/*========================计算器退格================================*/



		btnUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				for(int i=0; i<editTextList.size(); i++){
					if(i==0){
//						btnUp.setTextColor(Color.parseColor("#D5C9C9"));
					}else{
						if(editTextList.get(i).hasFocus()){
							editTextList.get(i).clearFocus();
							editTextList.get(i-1).setFocusable(true);
							editTextList.get(i-1).requestFocus();
							break;
						}
					}
				}
			}
		});
		btnDown.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				for(int i=0; i<editTextList.size(); i++){
					if(i==editTextList.size()-1){
//						btnDown.setTextColor(Color.parseColor("#D5C9C9"));
					}else{
						if(editTextList.get(i).hasFocus()){
							editTextList.get(i).clearFocus();
							editTextList.get(i+1).setFocusable(true);
							editTextList.get(i+1).requestFocus();
							break;
						}
					}

				}
			}
		});
		btnCalculate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if("".equals(roomLen)){
					Toast.makeText(getActivity(),"您没填写房间长度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(roomWid)){
					Toast.makeText(getActivity(),"您没填写房间宽度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(roomHei)){
					Toast.makeText(getActivity(),"您没填写房间高度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(doorNum)){
					Toast.makeText(getActivity(),"您没填写房门扇数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(doorWid)){
					Toast.makeText(getActivity(),"您没填写房门宽度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(doorHei)){
					Toast.makeText(getActivity(),"您没填写房门高度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(windowNum)){
					Toast.makeText(getActivity(),"您没填写窗户扇数", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(windowWid)){
					Toast.makeText(getActivity(),"您没填写窗户宽度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(windowHei)){
					Toast.makeText(getActivity(),"您没填写窗户高度", Toast.LENGTH_SHORT).show();
					return;
				}


				if(_compare(windowHei,roomHei)){
					Toast.makeText(getActivity(),"窗户高度不得大于房子高度", Toast.LENGTH_SHORT).show();
					return;
				}

				if(_compare(doorHei,roomHei)){
					Toast.makeText(getActivity(),"房门高度不得大于房子高度", Toast.LENGTH_SHORT).show();
					return;
				}


				if(!"".equals(wallBrickText)){

				}else{
					if("".equals(wallBrickLength) || "".equals(wallBrickWidth)){
						Toast.makeText(getActivity(), "您没选好墙砖尺寸", Toast.LENGTH_SHORT).show();
						return;
					}else if(wallBrickLength.contains(".")||wallBrickLength.contains(".")){
						Toast.makeText(getActivity(), "墙砖尺寸输入不正确", Toast.LENGTH_SHORT).show();
						return;
					}else {
						wallBrickText = wallBrickLength + "*"+ wallBrickWidth;
					}
				}
				System.out.println(">>> wallBrickText -->>>" + wallBrickText + "<<<<");
				ArrayList<Integer> wallBrickLW = calculate(wallBrickText);

				double roomLen_brickLen = 1000*Double.parseDouble(roomLen) / (wallBrickLW.get(0)); //房间的长度÷砖长

				double roomHei_brickWid = 1000*Double.parseDouble(roomHei) / (wallBrickLW.get(1)); //房间高度÷砖宽

				double roomWid_brickLen = 1000*Double.parseDouble(roomWid) / (wallBrickLW.get(0)); //房间的宽度÷砖长

				double windowHei_brickLen = 1000*Double.parseDouble(windowHei) / (wallBrickLW.get(0));

				double windowWid_brickWid = 1000*Double.parseDouble(windowWid) / (wallBrickLW.get(1));

				double doorHei_brickLen = 1000*Double.parseDouble(doorHei) / (wallBrickLW.get(0));

				double doorWid_brickWid = 1000*Double.parseDouble(doorWid) / (wallBrickLW.get(1));


				if(windowNum.contains("0.")){
					windowNum = "0";
				}else if(windowNum.contains(".")){
					windowNum = windowNum.substring(0, windowNum.indexOf("."));
				}


				if(doorNum.contains("0.")){
					doorNum = "0";
				}else if(doorNum.contains(".")){
					doorNum = doorNum.substring(0, doorNum.indexOf("."));
				}

				double totalBrickNum = 1.05*(roomLen_brickLen*roomHei_brickWid*2 +
						roomWid_brickLen*roomHei_brickWid*2 - windowHei_brickLen*windowWid_brickWid*Integer.parseInt(windowNum)
						- doorHei_brickLen*doorWid_brickWid*Integer.parseInt(doorNum));

//				num =[（房间的长度÷砖长）×（房间高度÷砖宽）×2+
//				    （房间的宽度÷砖长）×（房间高度÷砖宽）×2—（窗户的高度÷砖长）×
//				    （窗户的宽度÷砖宽）×个数—（门的高度÷砖长）×（门的宽度÷砖宽）×个数]×1.05





				total_num = new DecimalFormat("#.0").format(totalBrickNum);
				System.out.println("==数量=>" + total_num);
				total_num = total_num.substring(0,total_num.lastIndexOf("."));
				tvWallBrickNum.setText(total_num);

				double totalPrice = 0;
				// 无单价
				if("".equals(windowUnitPrice)){
					tvWallbrickTotalPrice.setText("0");
					tvEmptyText.setVisibility(View.VISIBLE);
				}else{
					total_price = new DecimalFormat("#,##0.0").format(totalBrickNum * Double.parseDouble(windowUnitPrice));
					tvWallbrickTotalPrice.setText(total_price);
					tvEmptyText.setVisibility(View.GONE);
				}

				System.out.println("==数量=>" + total_num + "<---总价==>" + total_price + "<<==");

				llCalculaterLayout.setVisibility(View.GONE);
				relCalculateResultLayout.setVisibility(View.VISIBLE);


				relCalculateResultLayout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						llCalculaterLayout.setVisibility(View.VISIBLE);
						relCalculateResultLayout.setVisibility(View.GONE);
						CalculaterActivity.clearEditText(editTextList);
						windowBrickUnitPrice.setText("");
					}
				});

				tvWallShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// FIXME 分享
						if("".equals(total_num)){
							return;
						}else if("".equals(total_price)){
							Toast.makeText(getActivity(),total_price, Toast.LENGTH_SHORT).show();
							String url = "&tbsNum="+ total_num;
							new ShareUtil(getActivity(), "我的装修，来自一砖一瓦", "我需要"+total_num+"块", Constant.CALCULATER_SHARE_URL + url);
						}else{
							String url = "tbsPrice="+ total_price +"&tbsNum="+ total_num;
							new ShareUtil(getActivity(), "我的装修，来自一砖一瓦", "我需要"+total_num+"块", Constant.CALCULATER_SHARE_URL + url);
						}
					}
				});

			}
		});
	}

	private String total_price = "";
	private String total_num = "";

	private ArrayList<Integer> calculate(String wallBrick){
		ArrayList<Integer> num = new ArrayList<Integer>();
		String[] str = wallBrick.split("\\*");
		for(int i=0, len=str.length; i<len; i++){
			num.add(Integer.parseInt(str[i]));
		}
		return num;
	}

	/**
	 * 比较大小
	 * @param big 大数
	 * @param small 小数
     * @return
     */
	private boolean _compare(String big, String small){
		double x = Double.parseDouble(big);
		double y = Double.parseDouble(small);
		if(x>y){
			return true;
		}else{
			return false;
		}
	}

}
