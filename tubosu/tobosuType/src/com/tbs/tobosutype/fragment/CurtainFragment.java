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

public class CurtainFragment extends Fragment {
	private static final String TAG = CurtainFragment.class.getSimpleName();
	private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	private EditText etCurtainWindowHeight, etCurtainWindowWidth, etCurtainWidth;
	private String windowWid = "";
	private String windowHei = "";
	private String curtainWidth = "";
	private ImageView ivClearWindowWidth,ivClearWindowHeight,ivClearCurtain;

	private TextView tvCurtainUnitPrice;
	private EditText etCurtainUnitPrice;
	private String curtainUnitPrice = "";

	private Button btnNum1,btnNum2,btnNum3,btnNum4,btnNum5,btnNum6,btnNum7,btnNum8,btnNum9,btnNum0;
	private Button btnDot,btnUp,btnDown,btnBackOff,btnCalculate;
	private RelativeLayout relBackOff;

	private LinearLayout llCalculaterCurtainLayout;
	private RelativeLayout relCalculateCurtainResult;

	private TextView tvCurtainNum;
	private TextView tvTotalPrice;
	private TextView tvEmpty;

	private TextView tvCurtainShare;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_curtain, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		etCurtainWindowHeight = (EditText) view.findViewById(R.id.et_window_height);
		etCurtainWindowWidth = (EditText) view.findViewById(R.id.et_window_width);
		etCurtainWidth = (EditText) view.findViewById(R.id.et_curtain_width);
		etCurtainWidth.setText("1.5");
		editTextList.add(etCurtainWindowHeight);
		editTextList.add(etCurtainWindowWidth);
		editTextList.add(etCurtainWidth);

		ivClearWindowWidth = (ImageView) view.findViewById(R.id.iv_clear_window_width);
		ivClearWindowHeight = (ImageView) view.findViewById(R.id.iv_clear_window_height);
		ivClearCurtain = (ImageView) view.findViewById(R.id.iv_clear_curtain);

		tvCurtainUnitPrice = (TextView) view.findViewById(R.id.curtain_unit_price);
		etCurtainUnitPrice = (EditText) view.findViewById(R.id.et_curtain_unit_price);


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


		llCalculaterCurtainLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_curtainlayout);
		relCalculateCurtainResult = (RelativeLayout) view.findViewById(R.id.rel_calculate_curtainresult);
		llCalculaterCurtainLayout.setVisibility(View.VISIBLE);
		relCalculateCurtainResult.setVisibility(View.GONE);

		tvCurtainNum = (TextView) view.findViewById(R.id.tv_curtain_num);
		tvTotalPrice = (TextView) view.findViewById(R.id.tv_curtain_total_price);
		tvEmpty = (TextView) view.findViewById(R.id.tv_curtainempty);

		tvCurtainShare = (TextView) view.findViewById(R.id.tv_curtain_share);

		setClickListener();
	}

	private void setClickListener() {
		etCurtainWindowHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowHei = etCurtainWindowHeight.getText().toString().trim();
//				windowHei = CalculaterActivity.clearDot(windowHei);
				if(windowHei.length()>0){
					ivClearWindowHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});
//		etCurtainWindowHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etCurtainWindowHeight);
		etCurtainWindowHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etCurtainWindowHeight);
				}else{
					ivClearWindowHeight.setVisibility(View.GONE);
				}
			}
		});


		ivClearWindowHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etCurtainWindowHeight.setText("");
			}
		});





		etCurtainWindowWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				windowWid = etCurtainWindowWidth.getText().toString().trim();
//				windowWid = CalculaterActivity.clearDot(windowWid);
				if(windowWid.length()>0){
					ivClearWindowWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});
//		etCurtainWindowWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etCurtainWindowWidth);
		etCurtainWindowWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etCurtainWindowWidth);
				}else{
					ivClearWindowWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearWindowWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etCurtainWindowWidth.setText("");
			}
		});



		etCurtainWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				curtainWidth = etCurtainWidth.getText().toString().trim();
//				curtainWidth = CalculaterActivity.clearDot(curtainWidth);
				if(curtainWidth.length()>0){
					ivClearCurtain.setVisibility(View.VISIBLE);
				}else{
					ivClearCurtain.setVisibility(View.GONE);
				}
			}
		});
//		etCurtainWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etCurtainWidth);
		etCurtainWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etCurtainWidth);
				}else{
					ivClearCurtain.setVisibility(View.GONE);
				}
			}
		});

		ivClearCurtain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etCurtainWidth.setText("");
			}
		});



		etCurtainUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				curtainUnitPrice = etCurtainUnitPrice.getText().toString().trim();
//				curtainUnitPrice = CalculaterActivity.clearDot(curtainUnitPrice);
			}
		});
//		etCurtainUnitPrice.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etCurtainUnitPrice);
		etCurtainUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etCurtainUnitPrice);
				}
			}
		});

	}




	private void doCalculate(final EditText et){
		llCalculaterCurtainLayout.setVisibility(View.VISIBLE);
		relCalculateCurtainResult.setVisibility(View.GONE);
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

		tvCurtainUnitPrice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(llCalculaterCurtainLayout.getVisibility()==View.VISIBLE){
					tvCurtainUnitPrice.setText("窗帘单价(元/平米)");
					tvCurtainUnitPrice.setBackgroundResource(R.color.bg_white);
					etCurtainUnitPrice.setVisibility(View.VISIBLE);
				}

			}
		});



		btnCalculate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				if("".equals(windowHei)){
					Toast.makeText(getActivity(),"您没填写窗户高度", Toast.LENGTH_SHORT).show();
					return;
				}

				if("".equals(windowWid)){
					Toast.makeText(getActivity(),"您没填写窗户宽度", Toast.LENGTH_SHORT).show();
					return;
				}

				if("".equals(etCurtainWidth.getText().toString().trim())){
					Toast.makeText(getActivity(),"您没填写布料宽度", Toast.LENGTH_SHORT).show();
					return;
				}


//				窗帘所需布料= [ （窗户宽+0.15米×2）×2] ÷ 布宽          ×（0.15米+窗户高+0.5米+0.2米）


//				System.out.println("--计算1-->" + Double.parseDouble(windowWid));
//				System.out.println("--计算2-->" + Double.parseDouble(etCurtainWidth.getText().toString().trim()));
//				System.out.println("--计算3-->" + Double.parseDouble(windowHei));

				double w = (Double.parseDouble(windowWid) + 0.3)*2 / Double.parseDouble(etCurtainWidth.getText().toString().trim());

//				System.out.println("--计算w-->" + w);
				double h = Double.parseDouble(windowHei)+ 0.15 + 0.5 +0.2;
//				System.out.println("--计算h-->" + h);
				double wh = w * h ; //所需布料
				total_num = new DecimalFormat("#.0").format(wh);
				tvCurtainNum.setText(total_num);

				double totalPrice = 0;
				// 无单价
				if("".equals(curtainUnitPrice)){
					tvTotalPrice.setText("0");
					tvEmpty.setVisibility(View.VISIBLE);
				}else{
					totalPrice = wh * Double.parseDouble(curtainUnitPrice);
					total_price = new DecimalFormat("#,##0.0").format(totalPrice);
					tvTotalPrice.setText(total_price);
					tvEmpty.setVisibility(View.GONE);
				}

				System.out.println("==数量=>" + total_num + "<---总价==>" + total_price + "<<==");


				llCalculaterCurtainLayout.setVisibility(View.GONE);
				relCalculateCurtainResult.setVisibility(View.VISIBLE);


				relCalculateCurtainResult.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						llCalculaterCurtainLayout.setVisibility(View.VISIBLE);
						relCalculateCurtainResult.setVisibility(View.GONE);
						CalculaterActivity.clearEditText(editTextList);
						etCurtainUnitPrice.setText("");
					}
				});

				tvCurtainShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// FIXME 分享
						if("".equals(total_num)){
							return;
						}else if("".equals(total_price)){
							Toast.makeText(getActivity(),total_price, Toast.LENGTH_SHORT).show();
							String url = "&tbsNum="+ total_num;
							new ShareUtil(getActivity(), "我的装修，来自一砖一瓦", "我需要"+total_num+"平米", Constant.CALCULATER_SHARE_URL + url);
						}else{
							String url = "tbsPrice="+ total_price +"&tbsNum="+ total_num;
							new ShareUtil(getActivity(), "我的装修，来自一砖一瓦", "我需要"+total_num+"平米", Constant.CALCULATER_SHARE_URL + url);
						}
					}
				});

			}
		});
	}

	private String total_price = "";
	private String total_num = "";

}
