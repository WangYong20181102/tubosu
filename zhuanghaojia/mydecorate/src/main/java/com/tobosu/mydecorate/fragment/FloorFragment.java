package com.tobosu.mydecorate.fragment;

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

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.CalculaterActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.ShareUtil;
import com.tobosu.mydecorate.util.Util;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/5/22 11:33.
 */

public class FloorFragment extends Fragment {
    private static final String TAG = FloorFragment.class.getSimpleName();

    private EditText etRoomLength, etRoomWidth;

    private ArrayList<EditText> editTextList = new ArrayList<EditText>();

    private ImageView ivClearLength, ivClearWidth;

    private TextView tvType1, tvType2, tvType3, tvType4;

    private String brick = "";
    private String roomLen = "";
    private String roomWid = "";


    private EditText etBrickLength, etBrickWidth, etBrickHeight;

    /**
     * 砖长
     */
    private String brickLength = "";
    /**
     * 砖宽
     */
    private String brickWidth = "";
    /**
     * 砖高
     */
    private String brickHeight = "";

    private TextView tvUnitPrice;
    private EditText etBrickUnitPrice;

    /**
     * 瓷砖单价
     */
    private String unitPrice = "";

    private Button btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0;
    private Button btnDot, btnUp, btnDown, btnBackOff, btnCalculate;
    private RelativeLayout relBackOff;

    private LinearLayout llCalculaterLayout;

    private RelativeLayout relCalculateResultLayout;


    private TextView tvBrickNum, tvBrickTotalPrice, tvEmptyText;

    private TextView tvFloorShare, tvFloorReCalculate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_floor, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etRoomLength = (EditText) view.findViewById(R.id.et_room_lengh);
        etRoomWidth = (EditText) view.findViewById(R.id.et_room_width);
        editTextList.add(etRoomLength);
        editTextList.add(etRoomWidth);

        ivClearLength = (ImageView) view.findViewById(R.id.iv_clear_room_lengh);
        ivClearWidth = (ImageView) view.findViewById(R.id.iv_clear_room_width);

        tvType1 = (TextView) view.findViewById(R.id.tv_brick_type1);
        tvType2 = (TextView) view.findViewById(R.id.tv_brick_type2);
        tvType3 = (TextView) view.findViewById(R.id.tv_brick_type3);
        tvType4 = (TextView) view.findViewById(R.id.tv_brick_type4);

        etBrickLength = (EditText) view.findViewById(R.id.et_brick_length);
        etBrickWidth = (EditText) view.findViewById(R.id.et_brick_width);
        etBrickHeight = (EditText) view.findViewById(R.id.et_brick_height);

//		editTextList.add(etBrickLength);
//		editTextList.add(etBrickWidth);
//		editTextList.add(etBrickHeight);


        tvUnitPrice = (TextView) view.findViewById(R.id.tv_unit_price);
        etBrickUnitPrice = (EditText) view.findViewById(R.id.et_brick_unit_price);

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

        llCalculaterLayout = (LinearLayout) view.findViewById(R.id.ll_calculater_layout);
        llCalculaterLayout.setVisibility(View.VISIBLE);
        relCalculateResultLayout = (RelativeLayout) view.findViewById(R.id.rel_calculate_result);
        relCalculateResultLayout.setVisibility(View.GONE);
        //FIXME
        tvBrickNum = (TextView) view.findViewById(R.id.tv_brick_num);
        tvBrickTotalPrice = (TextView) view.findViewById(R.id.tv_brick_total_price);
        tvEmptyText = (TextView) view.findViewById(R.id.tv_empty);
        tvFloorShare = (TextView) view.findViewById(R.id.tv_floor_share);
        tvFloorReCalculate = (TextView) view.findViewById(R.id.tv_floor_recalculate);

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
                if (roomLen.length() > 0 && etRoomLength.hasFocus()) {
                    ivClearLength.setVisibility(View.VISIBLE);
                } else {
                    ivClearLength.setVisibility(View.GONE);
                }
            }
        });
//		etRoomLength.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etRoomLength);
        etRoomLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    doCalculate(etRoomLength);
                } else {
                    ivClearLength.setVisibility(View.GONE);
                }
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
                if (roomWid.length() > 0 && etRoomWidth.hasFocus()) {
                    ivClearWidth.setVisibility(View.VISIBLE);
                } else {
                    ivClearWidth.setVisibility(View.GONE);
                }
            }
        });
//		etRoomWidth.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etRoomWidth);
        etRoomWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    doCalculate(etRoomWidth);
                } else {
                    ivClearWidth.setVisibility(View.GONE);
                }
            }
        });


        ivClearLength.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                etRoomLength.setText("");
                ivClearLength.setVisibility(View.GONE);
            }
        });

        ivClearWidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etRoomWidth.setText("");
                ivClearWidth.setVisibility(View.GONE);
            }
        });


        etBrickLength.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                brickLength = etBrickLength.getText().toString().trim();
//				brickLength = CalculaterActivity.clearDot(brickLength);
                if (!"".equals(brickLength)) {
                    clearBrickTextView();
                }
            }
        });
//		etBrickLength.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etBrickLength);
        etBrickLength.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
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
                brickWidth = etBrickWidth.getText().toString().trim();
//				brickWidth = CalculaterActivity.clearDot(brickWidth);
                if (!"".equals(brickWidth)) {
                    clearBrickTextView();
                }
            }
        });
//		etBrickWidth.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etBrickWidth);
        etBrickWidth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    doCalculate(etBrickWidth);
                }
            }
        });


        etBrickHeight.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                brickHeight = etBrickHeight.getText().toString().trim();
//				brickHeight = CalculaterActivity.clearDot(brickHeight);
                if (!"".equals(brickHeight)) {
                    clearBrickTextView();
                }
            }
        });
//		etBrickHeight.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etBrickHeight);
        etBrickHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    doCalculate(etBrickHeight);
                }
            }
        });


        etBrickUnitPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                unitPrice = etBrickUnitPrice.getText().toString().trim();
//				unitPrice = CalculaterActivity.clearDot(unitPrice);

            }
        });
//		etBrickUnitPrice.setShowSoftInputOnFocus(false);
        Util.hideKeyboard(getActivity(), etBrickUnitPrice);
        etBrickUnitPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {//获得焦点
                    doCalculate(etBrickUnitPrice);
                }
            }
        });


        tvUnitPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (llCalculaterLayout.getVisibility() == View.VISIBLE) {
                    tvUnitPrice.setText("地砖单价(元/块)");
                    tvUnitPrice.setBackgroundResource(R.color.bg_white);
                    etBrickUnitPrice.setVisibility(View.VISIBLE);
                }

            }
        });

        // 选择地砖类型
        clickBrickType();

    }


    /**
     * 选择地砖类型
     */
    private void clickBrickType() {

        if (!"".equals(etBrickLength.getText().toString().trim()) && !"".equals(etBrickWidth.getText().toString().trim())
                && !"".equals(etBrickHeight.getText().toString().trim())) {
            // 选择自己填写
            clearBrickTextView();

        } else {
            // 默认选中第一个
            selectType(1);
            tvType1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(1);
                }
            });
            tvType2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(2);
                }
            });
            tvType3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(3);
                }
            });
            tvType4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectType(4);
                }
            });
        }
    }

    /**
     * 清除默认的类型
     */
    private void clearBrickTextView() {
        tvType1.setSelected(false);
        tvType1.setTextColor(Color.parseColor("#7F8E9C"));
        tvType2.setSelected(false);
        tvType2.setTextColor(Color.parseColor("#7F8E9C"));
        tvType3.setSelected(false);
        tvType3.setTextColor(Color.parseColor("#7F8E9C"));
        tvType4.setSelected(false);
        tvType4.setTextColor(Color.parseColor("#7F8E9C"));
        brick = "";
    }

    private void clearFocuse() {
        etBrickHeight.clearFocus();
        etBrickWidth.clearFocus();
        etBrickLength.clearFocus();
    }

    private void selectType(int position) {


        switch (position) {
            case 1:
                tvType1.setSelected(true);
                tvType1.setTextColor(Color.parseColor("#FFFFFF"));
                tvType2.setSelected(false);
                tvType2.setTextColor(Color.parseColor("#7F8E9C"));
                tvType3.setSelected(false);
                tvType3.setTextColor(Color.parseColor("#7F8E9C"));
                tvType4.setSelected(false);
                tvType4.setTextColor(Color.parseColor("#7F8E9C"));
                brick = tvType1.getText().toString();
                break;
            case 2:
                tvType1.setSelected(false);
                tvType1.setTextColor(Color.parseColor("#7F8E9C"));
                tvType2.setSelected(true);
                tvType2.setTextColor(Color.parseColor("#FFFFFF"));
                tvType3.setSelected(false);
                tvType3.setTextColor(Color.parseColor("#7F8E9C"));
                tvType4.setSelected(false);
                tvType4.setTextColor(Color.parseColor("#7F8E9C"));
                brick = tvType2.getText().toString();
                break;
            case 3:
                tvType1.setSelected(false);
                tvType1.setTextColor(Color.parseColor("#7F8E9C"));
                tvType2.setSelected(false);
                tvType2.setTextColor(Color.parseColor("#7F8E9C"));
                tvType3.setSelected(true);
                tvType3.setTextColor(Color.parseColor("#FFFFFF"));
                tvType4.setSelected(false);
                tvType4.setTextColor(Color.parseColor("#7F8E9C"));
                brick = tvType3.getText().toString();
                break;
            case 4:
                tvType1.setSelected(false);
                tvType1.setTextColor(Color.parseColor("#7F8E9C"));
                tvType2.setSelected(false);
                tvType2.setTextColor(Color.parseColor("#7F8E9C"));
                tvType3.setSelected(false);
                tvType3.setTextColor(Color.parseColor("#7F8E9C"));
                tvType4.setSelected(true);
                tvType4.setTextColor(Color.parseColor("#FFFFFF"));
                brick = tvType4.getText().toString();
                break;
        }

        if (!"".equals(brick)) {
            if (!"".equals(etBrickLength.getText().toString().trim())) {
                etBrickLength.setText("");
            }
            if (!"".equals(etBrickWidth.getText().toString().trim())) {
                etBrickWidth.setText("");
            }
            if (!"".equals(etBrickHeight.getText().toString().trim())) {
                etBrickHeight.setText("");
            }
            clearFocuse();
        }
//		System.out.println(">>> brick 你选中的类型是>>>" + brick + "<<<<");
    }


    private void doCalculate(final EditText et) {
        llCalculaterLayout.setVisibility(View.VISIBLE);
        relCalculateResultLayout.setVisibility(View.GONE);

        btnNum0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = et.getText().toString();
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 0;
                    if ("00".equals(str) || "0.000".equals(str)) {
                        et.setText("0");
                        et.setSelection(1);
                        return;
                    } else {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 1;
                    if ("01".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 2;
                    if ("02".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 3;
                    if ("03".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 4;
                    if ("04".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 5;
                    if ("05".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 6;
                    if ("06".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 7;
                    if ("07".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 8;
                    if ("08".equals(str)) {
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
                if (str.length() >= 0 && str.length() < 5) {
                    str = str + 9;
                    if ("09".equals(str)) {
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
                if (str.indexOf(".") != -1) {
                    //判断字符串中是否已经包含了小数点，如果有就什么也不做
                } else {
                    //如果没有小数点
                    if (str.equals("0") || str.equals("")) {
                        //如果开始为0
                        str = "0.";
                        et.setText(str.toString());
                    } else {
                        str = str + ".";
                        et.setText(str);
                        if (str.length() == 6) {
                            et.setSelection(str.length() - 1);
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
                String str = et.getText().toString();
                if (str.length() > 0) {
                    str = str.substring(0, str.length() - 1);
                    et.setText(str);
                }
                et.setSelection(str.length());
            }
        });
        btnBackOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String str = et.getText().toString();
                if (str.length() > 0) {
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
                for (int i = 0; i < editTextList.size(); i++) {
                    if (i == 0) {
//						btnUp.setTextColor(Color.parseColor("#D5C9C9"));
                    } else {
//						btnUp.setTextColor(Color.parseColor("#000000"));
                        if (editTextList.get(i).hasFocus()) {
                            editTextList.get(i).clearFocus();
                            editTextList.get(i - 1).setFocusable(true);
                            editTextList.get(i - 1).requestFocus();
                            break;
                        }
                    }
                }
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < editTextList.size(); i++) {
                    if (i == editTextList.size() - 1) {
//						btnDown.setTextColor(Color.parseColor("#D5C9C9"));
                    } else {
//						btnDown.setTextColor(Color.parseColor("#000000"));
                        if (editTextList.get(i).hasFocus()) {
                            editTextList.get(i).clearFocus();
                            editTextList.get(i + 1).setFocusable(true);
                            editTextList.get(i + 1).requestFocus();
                            break;
                        }
                    }

                }
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CalculaterActivity.clearEditTextFocus(editTextList);

                if (!"".equals(brick)) {

                } else {
                    if ("".equals(brickLength) || "".equals(brickWidth) || "".equals(brickHeight)) {
                        Toast.makeText(getActivity(), "您没选好地砖尺寸", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (brickLength.contains(".") || brickWidth.contains(".") || brickHeight.contains(".")) {
                        Toast.makeText(getActivity(), "地砖尺寸输入不正确", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        brick = brickLength + "*" + brickWidth + "*" + brickHeight;
                    }

                }
                System.out.println(">>> brick -->>>" + brick + "<<<<");
                ArrayList<Integer> brickLW = calculate(brick);

                if ("".equals(roomLen)) {
                    Toast.makeText(getActivity(), "您没填写房间长度", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("".equals(roomWid)) {
                    Toast.makeText(getActivity(), "您没填写房间宽度", Toast.LENGTH_SHORT).show();
                    return;
                }

                double L = ((1000 * Double.parseDouble(roomLen)) / brickLW.get(0));
                double W = ((1000 * Double.parseDouble(roomWid)) / brickLW.get(1));
                int totalBrickNum = (int) (L * W * 1.05);

                total_num = new DecimalFormat("#.0").format(totalBrickNum);
                System.out.println("==数量=>" + total_num);
                total_num = total_num.substring(0, total_num.lastIndexOf("."));
                tvBrickNum.setText(total_num);

                double totalPrice = 0;
                // 无单价
                if ("".equals(unitPrice)) {
                    tvBrickTotalPrice.setText("0");
                    tvEmptyText.setVisibility(View.VISIBLE);
                } else {
                    double d = Double.parseDouble(unitPrice);
//					if(d>200){
//						Toast.makeText(getActivity(),"墙砖价格是否过高",Toast.LENGTH_SHORT).show();
//					}
                    totalPrice = totalBrickNum * d;
                    total_price = String.valueOf(new DecimalFormat("#,##0.0").format(totalPrice));
                    tvBrickTotalPrice.setText(total_price);
                    tvEmptyText.setVisibility(View.GONE);
                }

                System.out.println("==数量=>" + total_num + "<---总价==>" + total_price + "<<==");

                llCalculaterLayout.setVisibility(View.GONE);
                relCalculateResultLayout.setVisibility(View.VISIBLE);


                tvFloorReCalculate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        llCalculaterLayout.setVisibility(View.VISIBLE);
                        relCalculateResultLayout.setVisibility(View.GONE);
                        CalculaterActivity.clearEditText(editTextList);
                        etBrickUnitPrice.setText("");

                    }
                });

                tvFloorShare.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // FIXME 分享
                        if ("".equals(total_num)) {
                            return;
                        } else if ("".equals(total_price)) {
                            Toast.makeText(getActivity(), total_price, Toast.LENGTH_SHORT).show();
                            String url = "&tbsNum=" + total_num;
                            new ShareUtil(getActivity(), tvFloorShare, "我的装修，来自一砖一瓦", "我需要" + total_num + "块", Constant.CALCULATER_SHARE_URL + url);
                        } else {
                            String url = "tbsPrice=" + total_price + "&tbsNum=" + total_num;
                            new ShareUtil(getActivity(), tvFloorShare, "我的装修，来自一砖一瓦", "我需要" + total_num + "块", Constant.CALCULATER_SHARE_URL + url);
                        }
                    }
                });

            }
        });
    }

    private String total_price = "";
    private String total_num = "";

    private ArrayList<Integer> calculate(String brick) {
        ArrayList<Integer> num = new ArrayList<Integer>();
        String[] str = brick.split("\\*");
        for (int i = 0, len = str.length; i < len; i++) {
            num.add(Integer.parseInt(str[i]));
        }
        return num;
    }


//	private void ups(){
//		for(int i=0; i<editTextList.size(); i++){
//			if(i==0 && !editTextList.get(i).hasFocus()){
//				btnUp.setTextColor(Color.parseColor("#D5C9C9"));
//			}else{
//				btnUp.setTextColor(Color.parseColor("#000000"));
//				if(editTextList.get(i).hasFocus()){
//					editTextList.get(i).clearFocus();
//					editTextList.get(i-1).setFocusable(true);
//					editTextList.get(i-1).requestFocus();
//					break;
//				}
//			}
//		}
//	}
//
//	private void downs(){
//		for(int i=0; i<editTextList.size(); i++){
//			if(i==editTextList.size()-1 && !editTextList.get(i).hasFocus()){
//				btnDown.setTextColor(Color.parseColor("#D5C9C9"));
//			}else{
//				btnDown.setTextColor(Color.parseColor("#000000"));
//				if(editTextList.get(i).hasFocus()){
//					editTextList.get(i).clearFocus();
//					editTextList.get(i+1).setFocusable(true);
//					editTextList.get(i+1).requestFocus();
//					break;
//				}
//			}
//
//		}
//	}
}
