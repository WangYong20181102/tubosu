package com.tbs.tobosutype.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class MaterialFragment extends Fragment {
    private String[] stringArr = {"水电","泥木","油漆","地砖","石材","窗帘","墙纸","线","涂料"};

    private WriteAccountAdapter adapter;
    private GridView gvMaterial;

    private EditText etCostMaterial;
    private EditText etCostMoney;
    private TextView tvCostTime;
    private EditText etCostContent;

    public String nameText ="";
    public String moneyText ="";
    public String timeText ="";
    public String contentText ="";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, null, false);
        initView(view);
        initAdapter();
        initData();
        setClick();
        return view;
    }

    private void initData() {
        nameText = etCostMaterial.getText().toString();
        moneyText = etCostMoney.getText().toString();
        timeText = tvCostTime.getText().toString();
        contentText = etCostContent.getText().toString();
    }

    private void initAdapter() {
        gvMaterial.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new WriteAccountAdapter(getActivity(), stringArr);
        gvMaterial.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gvMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(etCostMaterial.getText().toString().equals(stringArr[position])){
                    adapter.clearSelection(-1);
                    etCostMaterial.setText("");
                }else {
                    etCostMaterial.setText(stringArr[position]);
                    adapter.setSelection(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initView(View view) {
        gvMaterial = (GridView) view.findViewById(R.id.gv_material);
        etCostMaterial = (EditText)view.findViewById(R.id.et_material_cost_material);
        etCostMoney = (EditText)view.findViewById(R.id.et_material_cost_money);
        tvCostTime = (TextView) view.findViewById(R.id.tv_material_cost_time);
        etCostContent = (EditText)view.findViewById(R.id.et_material_cost_content);

    }

    private void setClick(){
        tvCostTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateChooseWheelViewDialog chooseTimeDialog = new DateChooseWheelViewDialog(getActivity(), new DateChooseWheelViewDialog.DateChooseInterface() {

                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        String t[] = time.split(" ");
                        tvCostTime.setText(t[0]);
                        tvCostTime.setBackgroundResource(R.drawable.shape_time_textview_selected_bg);
                    }
                });
                chooseTimeDialog.setDateDialogTitle("开支时间");
                chooseTimeDialog.showDateChooseDialog();
            }
        });
    }
}
