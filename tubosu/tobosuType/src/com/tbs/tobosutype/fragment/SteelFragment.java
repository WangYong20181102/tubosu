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
import com.tbs.tobosutype.adapter.WriteAccountAdapter;
import com.tbs.tobosutype.bean.SaveDataEntity;
import com.tbs.tobosutype.customview.DateChooseWheelViewDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.CacheManager;
import com.tbs.tobosutype.adapter.utils.Util;

/**
 * Created by Lie on 2017/6/21.
 */

public class SteelFragment extends Fragment {
    private String[] stringArr = {"开关插座","门窗","窗帘滑轨","门锁","合页","衣架","水笼头","五金配件","防盗网"};

    private WriteAccountAdapter adapter;
    private GridView gvSteel;

    private EditText etCostSteel;
    private EditText etCostMoney;
    private TextView tvCostTime;
    private EditText etCostContent;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steel, null, false);
        initView(view);
        initAdapter();
        initData();
        setClick();
        initformerRecord();
        return view;
    }

    private void initformerRecord(){
        String cache = CacheManager.getStringArrayList(getActivity());
        if(!"".equals(cache)){
            setTextMessage(cache);
        }
    }

    private void initData() {
        receiver = new SteelReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_STEEL_FRAGMENT_DATA);
        filter.addAction(Constant.ACTION_GOTO_EDIT_FRAGMENT);
        getActivity().registerReceiver(receiver, filter);
    }

    private void initAdapter() {
        gvSteel.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new WriteAccountAdapter(getActivity(), stringArr);
        gvSteel.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gvSteel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(etCostSteel.getText().toString().equals(stringArr[position])){
                    adapter.clearSelection(-1);
                    etCostSteel.setText("");
                }else {
                    etCostSteel.setText(stringArr[position]);
                    adapter.setSelection(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }


    private void setTextMessage(String cache){
        String[] pieces = cache.split("#");
        etCostSteel.setText(pieces[1]);
        etCostMoney.setText(pieces[2]);
        tvCostTime.setText(pieces[3]);
        etCostContent.setText(pieces[4]);

    }

    private void initView(View view) {
        gvSteel = (GridView) view.findViewById(R.id.gv_steel);
        etCostSteel = (EditText)view.findViewById(R.id.et_steel_cost_steel);
        etCostMoney = (EditText)view.findViewById(R.id.et_steel_cost_money);
        tvCostTime = (TextView) view.findViewById(R.id.tv_steel_cost_time);
        tvCostTime.setBackgroundResource(R.drawable.shape_time_textview_selected_bg);
        tvCostTime.setText(Util.getTodayDatetime());
        etCostContent = (EditText)view.findViewById(R.id.et_steel_cost_content);

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

        etCostSteel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for(int i=0,len=stringArr.length;i<len;i++){
                    if(etCostSteel.getText().toString().equals(stringArr[i])){
                        adapter.setSelection(i);
                    }else {
                        adapter.clearSelection(-1);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private SteelReceiver receiver;
    private class SteelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(Constant.ACTION_STEEL_FRAGMENT_DATA.equals(intent.getAction())){
                String name = etCostSteel.getText().toString().trim();
                String typeId = "";
                if(isOtherType(name)){
                    typeId = "6";
                }else {
                    typeId = "3";
                }

                SaveDataEntity entity = new SaveDataEntity(name,
                        etCostMoney.getText().toString().trim(),tvCostTime.getText().toString().trim(),
                        etCostContent.getText().toString().trim(), typeId);
                Intent dataIntent = new Intent(Constant.ACTION_GET_FRAGMENT_DATA);
                dataIntent.putExtra("dataArray", entity.getDataArray());
                getActivity().sendBroadcast(dataIntent);
            }
        }
    }

    private boolean isOtherType(String type){
        int same = -1;
        if(!"".equals(type)){
            for(int i=0; i<stringArr.length; i++){
                if(type.equals(stringArr[i])){
                    same = -1;
                    break;
                }else {
                    same = 1;
                }
            }
        }
        if(same>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            getActivity().unregisterReceiver(receiver);
        }
    }
}
