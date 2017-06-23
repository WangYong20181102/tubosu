package com.tbs.tobosutype.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.WriteAccountAdapter;
import com.tbs.tobosutype.customview.DateChooseWheelViewDialog;

/**
 * Created by Lie on 2017/6/21.
 */

public class ManpowerFragment  extends Fragment {
    private String[] stringArr = {"开工款","施工费","交通","设计","监工","餐饮"};

    private WriteAccountAdapter adapter;
    private GridView gvManpower;

    private EditText etCostManpower;
    private EditText etCostMoney;
    private TextView tvCostTime;
    private EditText etCostContent;

    public String nameText ="";
    public String moneyText ="";
    public String timeText ="";
    public String contentText ="";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manpower, null, false);
        initView(view);
        initAdapter();
        initData();
        setClick();
        return view;
    }


    private void initData() {


        timeText = tvCostTime.getText().toString();
        contentText = etCostContent.getText().toString();
    }

    private void initAdapter() {
        gvManpower.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new WriteAccountAdapter(getActivity(), stringArr);
        gvManpower.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gvManpower.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(etCostManpower.getText().toString().equals(stringArr[position])){
                    adapter.clearSelection(-1);
                    etCostManpower.setText("");
                }else {
                    etCostManpower.setText(stringArr[position]);
                    adapter.setSelection(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initView(View view) {
        gvManpower = (GridView) view.findViewById(R.id.gv_manmpower);
        etCostManpower = (EditText)view.findViewById(R.id.et_manpower_cost_manpower);
        etCostMoney = (EditText)view.findViewById(R.id.et_manpower_cost_money);
        tvCostTime = (TextView) view.findViewById(R.id.tv_manpower_cost_time);
        etCostContent = (EditText)view.findViewById(R.id.et_manpower_cost_content);

    }

    private void setClick(){
        etCostManpower.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nameText = etCostManpower.getText().toString();
            }
        });
        etCostMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                moneyText = etCostMoney.getText().toString();
            }
        });
        etCostContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                contentText = etCostContent.getText().toString();
            }
        });
        tvCostTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateChooseWheelViewDialog chooseTimeDialog = new DateChooseWheelViewDialog(getActivity(), new DateChooseWheelViewDialog.DateChooseInterface() {

                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        String t[] = time.split(" ");
                        tvCostTime.setText(t[0]);
                        tvCostTime.setBackgroundResource(R.drawable.shape_time_textview_selected_bg);
                        timeText = t[0];
                    }
                });
                chooseTimeDialog.setDateDialogTitle("开支时间");
                chooseTimeDialog.showDateChooseDialog();
            }
        });
    }

}
