package com.tbs.tobosutype.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.tbs.tobosutype.activity.EditAccountAcitivity;
import com.tbs.tobosutype.adapter.WriteAccountAdapter;
import com.tbs.tobosutype.bean.SaveDataEntity;
import com.tbs.tobosutype.customview.DateChooseWheelViewDialog;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.model.Constant;
import com.tbs.tobosutype.utils.Util;

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
        receiver = new ManPowerReceiver();
        IntentFilter filter = new IntentFilter(AllConstants.ACTION_MANPOWER_FRAGMENT_DATA);
        getActivity().registerReceiver(receiver, filter);

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

        tvCostTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DateChooseWheelViewDialog chooseTimeDialog = new DateChooseWheelViewDialog(getActivity(), new DateChooseWheelViewDialog.DateChooseInterface() {

                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        String t[] = time.split(" ");
                        tvCostTime.setText(t[0]);
                        tvCostTime.setBackgroundResource(R.drawable.shape_time_textview_selected_bg);
//                        timeText = t[0];
                    }
                });
                chooseTimeDialog.setDateDialogTitle("开支时间");
                chooseTimeDialog.showDateChooseDialog();
            }
        });
    }


    private ManPowerReceiver receiver;
    private class ManPowerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if(AllConstants.ACTION_MANPOWER_FRAGMENT_DATA.equals(intent.getAction())){
                String name = etCostManpower.getText().toString().trim();
                String typeId = "";
                if(!"".equals(name) && isOtherType(name)){
                    typeId = "6";
                }else {
                    typeId = "1";
                }
                SaveDataEntity entity = new SaveDataEntity(name,
                        etCostMoney.getText().toString().trim(),tvCostTime.getText().toString().trim(),
                        etCostContent.getText().toString().trim(), typeId);
                Intent dataIntent = new Intent(AllConstants.ACTION_GET_FRAGMENT_DATA);
                dataIntent.putExtra("dataArray", entity.getDataArray());
                getActivity().sendBroadcast(dataIntent);
            }
        }
    }

    private boolean isOtherType(String type){
        for(int i=0; i<stringArr.length; i++){
            if(type.equals(stringArr[i])){
                return false;
            }else {
                return true;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            getActivity().unregisterReceiver(receiver);
        }
    }
}
