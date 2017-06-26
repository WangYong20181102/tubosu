package com.tbs.tobosutype.fragment;

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
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ShareUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PaintFragment extends Fragment {
	private static final String TAG = PaintFragment.class.getSimpleName();
	private EditText etPainroomLength, etPainroomWidth, etPainroomHeight;
	private String roomLen = "";
	private String roomWid = "";
	private String roomHei = "";
	private ImageView ivClearLength, ivClearWidth, ivClearHeight;

	private EditText etPainDoorWidth, etPainDoorHeight, etPainDoorNum;
	private String doorNum = "";
	private String doorWid = "";
	private String doorHei = "";
	private ImageView ivClearDoorWidth, ivClearDoorHeight,ivClearDoorNum;

	private EditText etPaintWindowWidth, etPaintWindowHeight, etPaintWindowNum;
	private String windowNum = "";
	private String windowWid = "";
	private String windowHei = "";
	private ImageView ivClearWindowWidth,ivClearWindowHeight,ivClearWindowNum;

	private EditText etNeedPaintNum;
	private String needPaintNum = "";
	private ImageView ivClearPaint;

	private TextView paintUnitPrice;
	private EditText etPaintUnitPrice;
	private String unitPrice = "";
	private ArrayList<EditText> editTextList = new ArrayList<EditText>();


	private Button btnNum1,btnNum2,btnNum3,btnNum4,btnNum5,btnNum6,btnNum7,btnNum8,btnNum9,btnNum0;
	private Button btnDot,btnUp,btnDown,btnBackOff,btnCalculate;
	private RelativeLayout relBackOff;

	private TextView tvTotalPaintNum;
	private TextView tvPaintTotalPrice;
	private TextView tvPaintEmpty;

	/**计算器布局*/
	private LinearLayout llCalculaterPaintLayout;
	/**计算结果布局*/
	private RelativeLayout relCalculaterPaintResult;

	private TextView tvPaintShare;
	private TextView tvPaintRecalculate;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_paint, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		etPainroomLength = (EditText) view.findViewById(R.id.et_paintroom_lengh);
		etPainroomWidth = (EditText) view.findViewById(R.id.et_paintroom_width);
		etPainroomHeight = (EditText) view.findViewById(R.id.et_paintroom_height);
		editTextList.add(etPainroomLength);
		editTextList.add(etPainroomWidth);
		editTextList.add(etPainroomHeight);
		ivClearLength = (ImageView) view.findViewById(R.id.iv_clear_paintroom_lengh);
		ivClearWidth = (ImageView) view.findViewById(R.id.iv_clear_paintroom_width);
		ivClearHeight = (ImageView) view.findViewById(R.id.iv_clear_paintroom_height);

		etPainDoorWidth = (EditText) view.findViewById(R.id.et_paintdoor_width);
		etPainDoorHeight = (EditText) view.findViewById(R.id.et_paintdoor_height);
		etPainDoorNum = (EditText) view.findViewById(R.id.et_paintdoor_num);
		editTextList.add(etPainDoorWidth);
		editTextList.add(etPainDoorHeight);
		editTextList.add(etPainDoorNum);
		ivClearDoorWidth = (ImageView) view.findViewById(R.id.iv_clear_paintdoor_width);
		ivClearDoorHeight = (ImageView) view.findViewById(R.id.iv_clear_paintdoor_height);
		ivClearDoorNum = (ImageView) view.findViewById(R.id.iv_clear_paintdoor_num);

		etPaintWindowWidth = (EditText) view.findViewById(R.id.et_paintwindow_width);
		etPaintWindowHeight = (EditText) view.findViewById(R.id.et_paintwindow_height);
		etPaintWindowNum = (EditText) view.findViewById(R.id.et_paintwindow_num);
		editTextList.add(etPaintWindowWidth);
		editTextList.add(etPaintWindowHeight);
		editTextList.add(etPaintWindowNum);
		ivClearWindowWidth = (ImageView) view.findViewById(R.id.iv_clear_paintwindow_width);
		ivClearWindowHeight = (ImageView) view.findViewById(R.id.iv_clear_paintwindow_height);
		ivClearWindowNum = (ImageView) view.findViewById(R.id.iv_clear_paintwindow_num);

		etNeedPaintNum = (EditText) view.findViewById(R.id.et_paint_num);
		editTextList.add(etNeedPaintNum);
		etNeedPaintNum.setText("8.6");
		ivClearPaint = (ImageView) view.findViewById(R.id.iv_clear_paint_num);

		paintUnitPrice = (TextView) view.findViewById(R.id.paint_unit_price);
		etPaintUnitPrice = (EditText) view.findViewById(R.id.et_paint_unit_price);

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
		relBackOff = (RelativeLayout) view.findViewById(R.id.rel_paintbackoff);
		btnBackOff = (Button) view.findViewById(R.id.btn_paintbackoff);
		btnUp = (Button) view.findViewById(R.id.btn_up);
		btnDown = (Button) view.findViewById(R.id.btn_down);
		btnCalculate = (Button) view.findViewById(R.id.btn_calculate);

		tvTotalPaintNum = (TextView) view.findViewById(R.id.tv_paint_num);
		tvPaintTotalPrice = (TextView) view.findViewById(R.id.tv_paint_total_price);
		tvPaintEmpty = (TextView) view.findViewById(R.id.tv_paintempty);

		llCalculaterPaintLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_paintayout);
		relCalculaterPaintResult = (RelativeLayout) view.findViewById(R.id.rel_calculate_paintresult);

		tvPaintShare = (TextView) view.findViewById(R.id.tv_paint_share);
		tvPaintRecalculate = (TextView) view.findViewById(R.id.tv_paint_recalculate);

		setClickListener();
	}

	private void setClickListener() {
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


		etPainroomHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomHei = etPainroomHeight.getText().toString().trim();
//				roomHei = CalculaterActivity.clearDot(roomHei);
				if(roomHei.length()>0){
					ivClearHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});
//		etPainroomHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainroomHeight);
		etPainroomHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainroomHeight);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});

		ivClearHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainroomHeight.setText("");
			}
		});




		/*----------------门------------------*/
		etPainDoorWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorWid = etPainDoorWidth.getText().toString().trim();
//				doorWid = CalculaterActivity.clearDot(doorWid);
				if(doorWid.length()>0){
					ivClearDoorWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorWidth.setVisibility(View.GONE);
				}
			}
		});
//		etPainDoorWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainDoorWidth);
		etPainDoorWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainDoorWidth);
				}else{
					ivClearDoorWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearDoorWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainDoorWidth.setText("");
			}
		});

		etPainDoorHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorHei = etPainDoorHeight.getText().toString().trim();
//				doorHei = CalculaterActivity.clearDot(doorHei);
				if(doorHei.length()>0){
					ivClearDoorHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorHeight.setVisibility(View.GONE);
				}
			}
		});
//		etPainDoorHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainDoorHeight);
		etPainDoorHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainDoorHeight);
				}else{
					ivClearDoorHeight.setVisibility(View.GONE);
				}
			}
		});

		ivClearDoorHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainDoorHeight.setText("");
			}
		});



		etPainDoorNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				doorNum = etPainDoorNum.getText().toString().trim();
//				doorNum = CalculaterActivity.clearDot(doorNum);
				if(doorNum.length()>0){
					ivClearDoorNum.setVisibility(View.VISIBLE);
				}else{
					ivClearDoorNum.setVisibility(View.GONE);
				}
			}
		});
//		etPainDoorNum.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPainDoorNum);
		etPainDoorNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPainDoorNum);
				}else{
					ivClearDoorNum.setVisibility(View.GONE);
				}
			}
		});
		ivClearDoorNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPainDoorNum.setText("");
			}
		});


		/*----------------窗户------------------*/
		etPaintWindowWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowWid = etPaintWindowWidth.getText().toString().trim();
//				windowWid = CalculaterActivity.clearDot(windowWid);
				if(windowWid.length()>0){
					ivClearWindowWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});
//		etPaintWindowWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPaintWindowWidth);
		etPaintWindowWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPaintWindowWidth);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearWindowWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPaintWindowWidth.setText("");
			}
		});


		etPaintWindowHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowHei = etPaintWindowHeight.getText().toString().trim();
//				windowHei = CalculaterActivity.clearDot(windowHei);
				if(windowHei.length()>0){
					ivClearWindowHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});
//		etPaintWindowHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPaintWindowHeight);
		etPaintWindowHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPaintWindowHeight);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});


		ivClearWindowHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPaintWindowHeight.setText("");
			}
		});


		etPaintWindowNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowNum = etPaintWindowNum.getText().toString().trim();
//				windowNum = CalculaterActivity.clearDot(windowNum);
				if(windowNum.length()>0){
					ivClearWindowNum.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowNum.setVisibility(View.GONE);
				}
			}
		});
//		etPaintWindowNum.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPaintWindowNum);
		etPaintWindowNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPaintWindowNum);
				}else{
					ivClearWindowNum.setVisibility(View.GONE);
				}
			}
		});

		ivClearWindowNum.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etPaintWindowNum.setText("");
			}
		});


		etNeedPaintNum.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				needPaintNum = etNeedPaintNum.getText().toString().trim();
//				needPaintNum = CalculaterActivity.clearDot(needPaintNum);
				if(needPaintNum.length()>0){
					ivClearPaint.setVisibility(View.VISIBLE);
				}else{
					ivClearPaint.setVisibility(View.GONE);
				}
			}
		});
//		etNeedPaintNum.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etNeedPaintNum);
		etNeedPaintNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etNeedPaintNum);
				}else{
					ivClearPaint.setVisibility(View.GONE);
				}
			}
		});

		ivClearPaint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etNeedPaintNum.setText("");
			}
		});


//		etPaintUnitPrice.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPaintUnitPrice);
		etPaintUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				unitPrice = etPaintUnitPrice.getText().toString().trim();
//				unitPrice = CalculaterActivity.clearDot(unitPrice);
			}
		});

		etPaintUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPaintUnitPrice);
				}
			}
		});


	}

	private void doCalculate(final EditText et){
		llCalculaterPaintLayout.setVisibility(View.VISIBLE);
		relCalculaterPaintResult.setVisibility(View.GONE);
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

		paintUnitPrice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(llCalculaterPaintLayout.getVisibility()==View.VISIBLE){
					paintUnitPrice.setText("涂料单价(元/升)");
					paintUnitPrice.setBackgroundResource(R.color.bg_white);
					etPaintUnitPrice.setVisibility(View.VISIBLE);
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
				if("".equals(doorWid)){
					Toast.makeText(getActivity(),"您没填写房门宽度", Toast.LENGTH_SHORT).show();
					return;
				}
				if("".equals(doorHei)){
					Toast.makeText(getActivity(),"您没填写房门高度", Toast.LENGTH_SHORT).show();
					return;
				}
				if(_compare(doorHei,roomHei)){
					Toast.makeText(getActivity(),"房门高度不得大于房子高度", Toast.LENGTH_SHORT).show();
					return;
				}

				if("".equals(doorNum)){
					Toast.makeText(getActivity(),"您没填写房门扇数", Toast.LENGTH_SHORT).show();
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

				if("".equals(windowNum)){
					Toast.makeText(getActivity(),"您没填写窗户扇数", Toast.LENGTH_SHORT).show();
					return;
				}

				if("".equals(etNeedPaintNum.getText().toString().trim())){
					Toast.makeText(getActivity(),"所需涂料数量不能为空", Toast.LENGTH_SHORT).show();
					return;
				}


//				涂料数量＝［（房长＋房宽）＊2*房高＋房长＊房宽－窗高＊窗宽＊窗扇－门高＊门宽＊门扇］/8.6
//				涂料价格=涂料数量*单价

				double d1 = 2*(Double.parseDouble(roomLen) + Double.parseDouble(roomWid))*Double.parseDouble(roomHei);
//				System.out.println("==-d1-=>" + d1 + "<<==");
				double d2 = Double.parseDouble(roomLen)*Double.parseDouble(roomWid);
//				System.out.println("==-d2-=>" + d2 + "<<==");
				double d3 = Double.parseDouble(windowHei)*Double.parseDouble(windowWid)*Integer.parseInt(windowNum);
//				System.out.println("==-d3-=>" + d3 + "<<==");
				double d4 = Double.parseDouble(doorHei)*Double.parseDouble(doorWid)*Integer.parseInt(doorNum);
//				System.out.println("==-d4-=>" + d4 + "<<==");
				double d = (d1 + d2 - d3 - d4) / Double.parseDouble(etNeedPaintNum.getText().toString().trim());
//				System.out.println("==-d-=>" + d + "<<==");

				total_num = new DecimalFormat("#.0").format(d);
				tvTotalPaintNum.setText(total_num);

				double totalPrice = 0;

				// 无单价
				if("".equals(unitPrice)){
					tvPaintTotalPrice.setText("0");
					tvPaintEmpty.setVisibility(View.VISIBLE);
				}else{
					totalPrice = Double.parseDouble(total_num) * Double.parseDouble(unitPrice);
					total_price = new DecimalFormat("#,##0.0").format(totalPrice);
					tvPaintTotalPrice.setText(total_price);
					tvPaintEmpty.setVisibility(View.GONE);
				}

				System.out.println("==数量=>" + total_num + "<---总价==>" + total_price + "<<==");


				llCalculaterPaintLayout.setVisibility(View.GONE);
				relCalculaterPaintResult.setVisibility(View.VISIBLE);

				tvPaintRecalculate.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						llCalculaterPaintLayout.setVisibility(View.VISIBLE);
						relCalculaterPaintResult.setVisibility(View.GONE);
						CalculaterActivity.clearEditText(editTextList);
						etPaintUnitPrice.setText("");
					}
				});

				tvPaintShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// FIXME 分享
						if("".equals(total_num)){
							return;
						}else if("".equals(total_price)){
							Toast.makeText(getActivity(),total_price, Toast.LENGTH_SHORT).show();
							String url = "&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvPaintShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"升",  Constant.CALCULATER_SHARE_URL + url);
						}else{
							String url = "tbsPrice="+ total_price +"&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvPaintShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"升", Constant.CALCULATER_SHARE_URL + url);
						}
					}
				});

			}
		});
	}

	private String total_price = "";
	private String total_num = "";

	/**
	 * 比较大小
	 * @param big 大数
	 * @param small 小数
	 * @return
	 */
	private boolean _compare(String big, String small){
		double x = Double.parseDouble(big);
		double y = Double.parseDouble(small);
		System.out.println("=====前面" + x+ "========后面" + y);
		if(x>y){
			return true;
		}else{
			return false;
		}
	}

}
