package com.tbs.tobosutype.fragment;

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

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.CalculaterActivity;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.ShareUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FloorBrickFragment extends Fragment {
	private static final String TAG = FloorBrickFragment.class.getSimpleName();
	private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	private EditText etPainroomLength, etPainroomWidth;
	private String roomLen = "";
	private String roomWid = "";
	private ImageView ivClearLength, ivClearWidth;

	private TextView tvFloorBrick300, tvFloorBrick400, tvFloorBrick500, tvFloorBrick600;
	private String floorBrickText = "";

	private EditText etFloorBrickLen, etFloorBrickWid;
	private String floorBrickL, floorBrickW;

	private TextView tvUnitPrice;
	private EditText etFloorbrickUnitPrice;
	private String floorBrickUnitPrice = "";

	private Button btnNum1,btnNum2,btnNum3,btnNum4,btnNum5,btnNum6,btnNum7,btnNum8,btnNum9,btnNum0;
	private Button btnDot,btnUp,btnDown,btnBackOff,btnCalculate;
	private RelativeLayout relBackOff;

	private RelativeLayout relCalculateResult;
	private LinearLayout llCalculaterLayout;

	private TextView tvFloorBrickRecalculate;
	private TextView tvFloorBrickShare;

	private TextView tvFloorbrickNum;
	private TextView tvFloorbrickPrice;
	private TextView tvBrickEmpty;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_floor_brick, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		etPainroomLength = (EditText) view.findViewById(R.id.et_room_lengh);
		etPainroomWidth = (EditText) view.findViewById(R.id.et_room_width);
		ivClearLength = (ImageView) view.findViewById(R.id.iv_clear_room_lengh);
		ivClearWidth = (ImageView) view.findViewById(R.id.iv_clear_room_width);
		editTextList.add(etPainroomLength);
		editTextList.add(etPainroomWidth);

		tvFloorBrick300 = (TextView) view.findViewById(R.id.tv_floorbrick300);
		tvFloorBrick400 = (TextView) view.findViewById(R.id.tv_floorbrick400);
		tvFloorBrick500 = (TextView) view.findViewById(R.id.tv_floorbrick500);
		tvFloorBrick600 = (TextView) view.findViewById(R.id.tv_floorbrick600);

		etFloorBrickLen = (EditText) view.findViewById(R.id.et_floorbrick_length);
		etFloorBrickWid = (EditText) view.findViewById(R.id.et_floorbrick_width);
//		editTextList.add(etFloorBrickLen);
//		editTextList.add(etFloorBrickWid);

		tvUnitPrice = (TextView) view.findViewById(R.id.tv_unit_price);
		etFloorbrickUnitPrice = (EditText) view.findViewById(R.id.et_floorbrick_unit_price);

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
		relBackOff = (RelativeLayout) view.findViewById(R.id.rel_floorbrick_backoff);
		btnBackOff = (Button) view.findViewById(R.id.btn_floorbrick_backoff);
		btnUp = (Button) view.findViewById(R.id.btn_up);
		btnDown = (Button) view.findViewById(R.id.btn_down);
		btnCalculate = (Button) view.findViewById(R.id.btn_calculate);

		relCalculateResult = (RelativeLayout) view.findViewById(R.id.rel_calculate_floorresult);
		relCalculateResult.setVisibility(View.GONE);
		llCalculaterLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_layout);
		llCalculaterLayout.setVisibility(View.VISIBLE);

		tvFloorBrickRecalculate = (TextView) view.findViewById(R.id.tv_floor_recalculate);
		tvFloorBrickShare = (TextView) view.findViewById(R.id.tv_floorbrick_share);

		tvFloorbrickNum = (TextView) view.findViewById(R.id.tv_floorbrick_num);
		tvFloorbrickPrice = (TextView) view.findViewById(R.id.tv_floorbrick_total_price);
		tvBrickEmpty = (TextView) view.findViewById(R.id.tv_floorempty);

		setCiclkListener();
	}

	private void setCiclkListener() {

		/*----------------房------------------*/
		etPainroomLength.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomLen = etPainroomLength.getText().toString().trim();
//				roomLen = CalculaterActivity.clearDot(roomLen);
				if(roomLen.length()>0){
					ivClearLength.setVisibility(View.VISIBLE);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});
//		etPainroomLength.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainroomLength);
		etPainroomLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainroomLength);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});



		ivClearLength.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainroomLength.setText("");
			}
		});



		etPainroomWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomWid = etPainroomWidth.getText().toString().trim();
//				roomWid = CalculaterActivity.clearDot(roomWid);
				if(roomWid.length()>0){
					ivClearWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});
//		etPainroomWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainroomWidth);
		etPainroomWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainroomWidth);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainroomWidth.setText("");
			}
		});

//		etFloorbrickUnitPrice.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etFloorbrickUnitPrice);
		etFloorbrickUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etFloorbrickUnitPrice);
				}
			}
		});
		etFloorbrickUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				floorBrickUnitPrice = etFloorbrickUnitPrice.getText().toString().trim();
//				floorBrickUnitPrice = CalculaterActivity.clearDot(floorBrickUnitPrice);
			}
		});



		etFloorBrickLen.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				floorBrickL = etFloorBrickLen.getText().toString().trim();
//				floorBrickL = CalculaterActivity.clearDot(floorBrickL);
				if(!"".equals(floorBrickL)){
					clearBrickTextView();
				}
			}
		});
//		etFloorBrickLen.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etFloorBrickLen);
		etFloorBrickLen.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etFloorBrickLen);
				}
			}
		});




		etFloorBrickWid.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				floorBrickW = etFloorBrickWid.getText().toString().trim();
//				floorBrickW = CalculaterActivity.clearDot(floorBrickW);
				if(!"".equals(floorBrickW)){
					clearBrickTextView();
				}
			}
		});
//		etFloorBrickWid.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etFloorBrickWid);
		etFloorBrickWid.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etFloorBrickWid);
				}
			}
		});

		clickBrickType();

	}


	/**
	 * 选择地砖类型
	 */
	private void clickBrickType() {

		if(!"".equals(etFloorBrickLen.getText().toString().trim()) && !"".equals(etFloorBrickWid.getText().toString().trim())){
			// 选择自己填写
			clearBrickTextView();
		}else {
			// 默认选中第一个
			selectType(3);
			tvFloorBrick300.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(3);
				}
			});
			tvFloorBrick400.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(4);
				}
			});
			tvFloorBrick500.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(5);
				}
			});
			tvFloorBrick600.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					selectType(6);
				}
			});

		}
	}


	/**
	 * 清除默认的类型
	 */
	private void clearBrickTextView(){
		tvFloorBrick300.setSelected(false);
		tvFloorBrick300.setTextColor(Color.parseColor("#7F8E9C"));
		tvFloorBrick400.setSelected(false);
		tvFloorBrick400.setTextColor(Color.parseColor("#7F8E9C"));
		tvFloorBrick500.setSelected(false);
		tvFloorBrick500.setTextColor(Color.parseColor("#7F8E9C"));
		tvFloorBrick600.setSelected(false);
		tvFloorBrick600.setTextColor(Color.parseColor("#7F8E9C"));

		floorBrickText = "";
	}

	private void selectType(int position){

		switch (position){
			case 3:
				tvFloorBrick300.setSelected(true);
				tvFloorBrick300.setTextColor(Color.parseColor("#FFFFFF"));
				tvFloorBrick400.setSelected(false);
				tvFloorBrick400.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick500.setSelected(false);
				tvFloorBrick500.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick600.setSelected(false);
				tvFloorBrick600.setTextColor(Color.parseColor("#7F8E9C"));
				floorBrickText = tvFloorBrick300.getText().toString();
				break;
			case 4:
				tvFloorBrick300.setSelected(false);
				tvFloorBrick300.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick400.setSelected(true);
				tvFloorBrick400.setTextColor(Color.parseColor("#FFFFFF"));
				tvFloorBrick500.setSelected(false);
				tvFloorBrick500.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick600.setSelected(false);
				tvFloorBrick600.setTextColor(Color.parseColor("#7F8E9C"));
				floorBrickText = tvFloorBrick400.getText().toString();
				break;
			case 5:
				tvFloorBrick300.setSelected(false);
				tvFloorBrick300.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick400.setSelected(false);
				tvFloorBrick400.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick500.setSelected(true);
				tvFloorBrick500.setTextColor(Color.parseColor("#FFFFFF"));
				tvFloorBrick600.setSelected(false);
				tvFloorBrick600.setTextColor(Color.parseColor("#7F8E9C"));
				floorBrickText = tvFloorBrick500.getText().toString();
				break;
			case 6:
				tvFloorBrick300.setSelected(false);
				tvFloorBrick300.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick400.setSelected(false);
				tvFloorBrick400.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick500.setSelected(false);
				tvFloorBrick500.setTextColor(Color.parseColor("#7F8E9C"));
				tvFloorBrick600.setSelected(true);
				tvFloorBrick600.setTextColor(Color.parseColor("#FFFFFF"));
				floorBrickText = tvFloorBrick600.getText().toString();
				break;
		}

		if(!"".equals(floorBrickText)){
			if(!"".equals(etFloorBrickLen.getText().toString().trim())){
				etFloorBrickLen.setText("");
			}
			if(!"".equals(etFloorBrickWid.getText().toString().trim())){
				etFloorBrickWid.setText("");
			}
			clearFocuse();
		}
	}

	private void clearFocuse(){
		etFloorBrickWid.clearFocus();
		etFloorBrickLen.clearFocus();
	}

	private void doCalculate(final EditText et){
		llCalculaterLayout.setVisibility(View.VISIBLE);
		relCalculateResult.setVisibility(View.GONE);
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



		tvUnitPrice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(llCalculaterLayout.getVisibility() == View.VISIBLE){
					tvUnitPrice.setText("地砖单价(元/块)");
					tvUnitPrice.setBackgroundResource(R.color.bg_white);
					etFloorbrickUnitPrice.setVisibility(View.VISIBLE);
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

				if(!"".equals(floorBrickText)){
					System.out.println("==floorBrickText=>" + floorBrickText);
				}else{
					if("".equals(floorBrickL) || "".equals(floorBrickW)){
						Toast.makeText(getActivity(), "你还没选择地砖规格", Toast.LENGTH_SHORT).show();
						return;
					}else if(floorBrickL.contains(".")||floorBrickW.contains(".")){
						Toast.makeText(getActivity(), "地砖尺寸输入不正确", Toast.LENGTH_SHORT).show();
						return;
					}

					floorBrickText =  floorBrickL + "*" + floorBrickW;
				}

				ArrayList<Integer> brickData = _calculate(floorBrickText);

//				地砖数量=（房间长/地砖长）*（房间宽/地砖宽）*1.05      （地砖一般有3%的损耗）
//				地砖价格=地砖数量*地砖单价

				double L = 1000*Double.parseDouble(roomLen) / brickData.get(0);
				double W = 1000*Double.parseDouble(roomWid) / brickData.get(1);
				double LW = L * W * 1.05;
//				System.out.println("==brickData.get(0)=>" + brickData.get(0));
//				System.out.println("==brickData.get(1)=>" + brickData.get(1));
//				System.out.println("==L=>" + L);
//				System.out.println("==W=>" + W);

				BigDecimal bigDecimal = new BigDecimal(LW).setScale(1, BigDecimal.ROUND_HALF_UP);
				System.out.println("==bigDecimal=>" + bigDecimal.toString());
				total_num = new DecimalFormat("#.0").format(bigDecimal);
				String num = total_num;
				System.out.println("==数量=>" + num);


				total_num = total_num.substring(0, total_num.lastIndexOf(".")); // FIXME 去掉小数点
				tvFloorbrickNum.setText(total_num);

				double totalPrice = 0;

				// 无单价
				if("".equals(floorBrickUnitPrice)){
					tvFloorbrickPrice.setText("0");
					tvBrickEmpty.setVisibility(View.VISIBLE);
				}else{
					total_price = new DecimalFormat("#,##0.0").format(Double.parseDouble(num) * Double.parseDouble(floorBrickUnitPrice));
					tvFloorbrickPrice.setText(total_price);
					tvBrickEmpty.setVisibility(View.GONE);
				}

				llCalculaterLayout.setVisibility(View.GONE);
				relCalculateResult.setVisibility(View.VISIBLE);

				tvFloorBrickRecalculate.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						llCalculaterLayout.setVisibility(View.VISIBLE);
						relCalculateResult.setVisibility(View.GONE);
						CalculaterActivity.clearEditText(editTextList);
						etFloorbrickUnitPrice.setText("");
					}
				});

				tvFloorBrickShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// FIXME 分享
						if("".equals(total_num)){
							return;
						}else if("".equals(total_price)){
							Toast.makeText(getActivity(),total_price, Toast.LENGTH_SHORT).show();
							String url = "&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvFloorBrickShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"块", Constant.CALCULATER_SHARE_URL + url);
						}else{
							String url = "tbsPrice="+ total_price +"&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvFloorBrickShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"块", Constant.CALCULATER_SHARE_URL + url);
						}
					}
				});

			}
		});
	}

	private String total_price = "";
	private String total_num = "";

	private ArrayList<Integer> _calculate(String wallBrick){
		ArrayList<Integer> num = new ArrayList<Integer>();
		String[] str = wallBrick.split("\\*");
		for(int i=0, len=str.length; i<len; i++){
			num.add(Integer.parseInt(str[i]));
		}
		return num;
	}

}
