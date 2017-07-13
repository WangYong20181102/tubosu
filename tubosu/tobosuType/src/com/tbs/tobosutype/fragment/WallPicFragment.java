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
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.ShareUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WallPicFragment extends Fragment {
	private static final String TAG = WallPicFragment.class.getSimpleName();
	private ArrayList<EditText> editTextList = new ArrayList<EditText>();
	private EditText etRoomLength, etRoomWidth, etRoomHeight;
	private String roomLen = "";
	private String roomWid = "";
	private String roomHei = "";
	private ImageView ivClearLength, ivClearWidth, ivClearHeight;
	
	private EditText etWallPic;
	private ImageView ivClearWallPic;
	private String wallPic = "";

	private TextView tvPicUnitPrice;

	private EditText etPicUnitPrice;
	private String picNum = "";

	private Button btnNum1,btnNum2,btnNum3,btnNum4,btnNum5,btnNum6,btnNum7,btnNum8,btnNum9,btnNum0;
	private Button btnDot,btnUp,btnDown,btnBackOff,btnCalculate;
	private RelativeLayout relBackOff;

	private LinearLayout llCalculaterLayout;
    private RelativeLayout relCalculatePicresult;

	private TextView tvTotalNum, tvTotalPrice, tvEmpty;

	private TextView tvPicShare;
	private TextView tvPicRecalculate;


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_wall_pic, null);
		initView(view);
		return view;
	}

	private void initView(View view) {

		etRoomLength = (EditText) view.findViewById(R.id.et_room_lengh);
		etRoomWidth = (EditText) view.findViewById(R.id.et_room_width);
		etRoomHeight = (EditText) view.findViewById(R.id.et_room_height);
		editTextList.add(etRoomLength);
		editTextList.add(etRoomWidth);
		editTextList.add(etRoomHeight);
		ivClearLength = (ImageView) view.findViewById(R.id.iv_clear_room_length_pic);
		ivClearWidth = (ImageView) view.findViewById(R.id.iv_clear_room_width_pic);
		ivClearHeight = (ImageView) view.findViewById(R.id.iv_clear_room_height_pic);


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

		etWallPic = (EditText) view.findViewById(R.id.et_wall_pic);
		wallPic = etWallPic.getText().toString().trim();
		editTextList.add(etWallPic);
		ivClearWallPic = (ImageView) view.findViewById(R.id.iv_clear_wall_pic);
		tvPicUnitPrice = (TextView) view.findViewById(R.id.tv_pic_unit_price);

		etPicUnitPrice = (EditText) view.findViewById(R.id.et_pic_unit_price);

        llCalculaterLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_Piclayout);
		llCalculaterLayout.setVisibility(View.VISIBLE);
        relCalculatePicresult = (RelativeLayout) view.findViewById(R.id.rel_calculate_picresult);
		relCalculatePicresult.setVisibility(View.GONE);

		tvTotalNum = (TextView) view.findViewById(R.id.tv_pic_num);
		tvTotalPrice = (TextView) view.findViewById(R.id.tv_pic_total_price);
		tvEmpty = (TextView) view.findViewById(R.id.tv_picempty);

		tvPicShare = (TextView) view.findViewById(R.id.tv_pic_share);
		tvPicRecalculate = (TextView) view.findViewById(R.id.tv_pic_recalculate);

		setClickListener();

	}

	private void setClickListener() {

		etRoomLength.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomLen = etRoomLength.getText().toString().trim();
//				roomLen = CalculaterActivity.clearDot(roomLen);
				if(roomLen.length()>0){
					ivClearLength.setVisibility(View.VISIBLE);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});
//		etRoomLength.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etRoomLength);
		etRoomLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etRoomLength);
				}else{
					ivClearLength.setVisibility(View.GONE);
				}
			}
		});

		ivClearLength.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				etRoomLength.setText("");
			}
		});


		etRoomWidth.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomWid = etRoomWidth.getText().toString().trim();
//				roomWid = CalculaterActivity.clearDot(roomWid);
				if(roomWid.length()>0){
					ivClearWidth.setVisibility(View.VISIBLE);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});
//		etRoomWidth.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etRoomWidth);
		etRoomWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etRoomWidth);
				}else{
					ivClearWidth.setVisibility(View.GONE);
				}
			}
		});

		ivClearWidth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etRoomWidth.setText("");
			}
		});



		etRoomHeight.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				roomHei = etRoomHeight.getText().toString().trim();
//				roomHei = CalculaterActivity.clearDot(roomHei);
				if(roomHei.length()>0){
					ivClearHeight.setVisibility(View.VISIBLE);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});
//		etRoomHeight.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etRoomHeight);
		etRoomHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etRoomHeight);
				}else{
					ivClearHeight.setVisibility(View.GONE);
				}
			}
		});

		ivClearHeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				etRoomHeight.setText("");
			}
		});



        etWallPic.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//				wallPic = etWallPic.getText().toString().trim();
//				if(wallPic.length()>0){
//					ivClearWallPic.setVisibility(View.VISIBLE);
//				}else{
//					ivClearWallPic.setVisibility(View.GONE);
//				}
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				wallPic = etWallPic.getText().toString().trim();
//				wallPic = CalculaterActivity.clearDot(wallPic);
				if(wallPic.length()>0){
					ivClearWallPic.setVisibility(View.VISIBLE);
				}else{
					ivClearWallPic.setVisibility(View.GONE);
				}
			}
		});
//        etWallPic.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etWallPic);
        etWallPic.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etWallPic);
				}else{
					ivClearWallPic.setVisibility(View.GONE);
				}
			}
		});

		ivClearWallPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
                etWallPic.setText("");
			}
		});


        tvPicUnitPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				if(llCalculaterLayout.getVisibility()==View.VISIBLE){
					tvPicUnitPrice.setText("壁纸单价(元/平米)");
					tvPicUnitPrice.setBackgroundResource(R.color.bg_white);
					etPicUnitPrice.setVisibility(View.VISIBLE);
				}

            }
        });

		etPicUnitPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				picNum = etPicUnitPrice.getText().toString().trim();
//				picNum = CalculaterActivity.clearDot(picNum);
			}
		});
//		etPicUnitPrice.setShowSoftInputOnFocus(false);
		AppInfoUtil.hideKeyboard(getActivity(),etPicUnitPrice);
		etPicUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){//获得焦点
					doCalculate(etPicUnitPrice);
				}
			}
		});


	}



	private void doCalculate(final EditText et){
		llCalculaterLayout.setVisibility(View.VISIBLE);
		relCalculatePicresult.setVisibility(View.GONE);
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

				if("".equals(wallPic)){
					Toast.makeText(getActivity(),"您没填写壁纸规格", Toast.LENGTH_SHORT).show();
					return;
				}

//				壁纸用量(卷)=房间周长×1.1÷每卷平米数
//				周长=（长+宽）*2*高
//				壁纸价格=壁纸数量*单价

				double needNum = 1.1 * 2 * (Double.parseDouble(roomLen) + Double.parseDouble(roomWid)) * Double.parseDouble(roomHei) / Double.parseDouble(wallPic);
				total_num = new DecimalFormat("#.0").format(needNum);
				tvTotalNum.setText(total_num);

				double totalPrice = 0;
				// 无单价
				if("".equals(picNum)){
					tvTotalPrice.setText("0");
					tvEmpty.setVisibility(View.VISIBLE);
				}else{
					totalPrice = needNum * Double.parseDouble(picNum);
					total_price = new DecimalFormat("#,##0.0").format(totalPrice);
					tvTotalPrice.setText(total_price);
					tvEmpty.setVisibility(View.GONE);
				}

				System.out.println("==数量=>" + total_num + "<---总价==>" + total_price + "<<==");

				llCalculaterLayout.setVisibility(View.GONE);
				relCalculatePicresult.setVisibility(View.VISIBLE);


				tvPicRecalculate.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						llCalculaterLayout.setVisibility(View.VISIBLE);
						relCalculatePicresult.setVisibility(View.GONE);
						CalculaterActivity.clearEditText(editTextList);
						etPicUnitPrice.setText("");
					}
				});

				tvPicShare.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						// FIXME 分享
						if("".equals(total_num)){
							return;
						}else if("".equals(total_price)){
							Toast.makeText(getActivity(),total_price, Toast.LENGTH_SHORT).show();
							String url = "&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvPicShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"卷", Constant.CALCULATER_SHARE_URL + url);
						}else{
							String url = "tbsPrice="+ total_price +"&tbsNum="+ total_num;
							new ShareUtil(getActivity(), tvPicShare, "我的装修，来自一砖一瓦", "我需要"+total_num+"卷", Constant.CALCULATER_SHARE_URL + url);
						}
					}
				});

			}
		});


	}
	private String total_price = "";
	private String total_num = "";

}
