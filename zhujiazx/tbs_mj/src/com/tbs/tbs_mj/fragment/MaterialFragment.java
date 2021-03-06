package com.tbs.tbs_mj.fragment;

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

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.WriteAccountAdapter;
import com.tbs.tbs_mj.bean.SaveDataEntity;
import com.tbs.tbs_mj.customview.DateChooseWheelViewDialog;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.utils.CacheManager;
import com.tbs.tbs_mj.utils.Util;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material, null, false);
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
        receiver = new MaterialReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_GOTO_EDIT_FRAGMENT);
        filter.addAction(Constant.ACTION_MATERIAL_FRAGMENT_DATA);
        getActivity().registerReceiver(receiver, filter);
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

    private void setTextMessage(String cache){
        String[] pieces = cache.split("#");
        int len = pieces.length;
        if(len <= 1){
        }else if(len == 2){
            etCostMaterial.setText(pieces[1]);
        }else if(len == 3){
            etCostMaterial.setText(pieces[1]);
            etCostMoney.setText(pieces[2]);
        }else if(len == 4){
            etCostMaterial.setText(pieces[1]);
            etCostMoney.setText(pieces[2]);
            tvCostTime.setText(pieces[3]);
        }else {
            etCostMaterial.setText(pieces[1]);
            etCostMoney.setText(pieces[2]);
            tvCostTime.setText(pieces[3]);
            etCostContent.setText(pieces[4]);
        }


    }

    private void initView(View view) {
        gvMaterial = (GridView) view.findViewById(R.id.gv_material);
        etCostMaterial = (EditText)view.findViewById(R.id.et_material_cost_material);
        etCostMoney = (EditText)view.findViewById(R.id.et_material_cost_money);
        tvCostTime = (TextView) view.findViewById(R.id.tv_material_cost_time);
        tvCostTime.setText(Util.getTodayDatetime());
        tvCostTime.setBackgroundResource(R.drawable.shape_time_textview_selected_bg);
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

        etCostMaterial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for(int i=0,len=stringArr.length;i<len;i++){
                    if(etCostMaterial.getText().toString().equals(stringArr[i])){
                        adapter.setSelection(i);
                    }else {
                        adapter.clearSelection(-1);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private MaterialReceiver receiver;
    private class MaterialReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(Constant.ACTION_MATERIAL_FRAGMENT_DATA.equals(intent.getAction())){
                String name = etCostMaterial.getText().toString().trim();
                String typeId = "";
                if(isOtherType(name)){
                    typeId = "6";
                }else {
                    typeId ="2";
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
