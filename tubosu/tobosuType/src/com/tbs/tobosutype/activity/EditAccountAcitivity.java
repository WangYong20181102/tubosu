package com.tbs.tobosutype.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.fragment.FurnitureFragment;
import com.tbs.tobosutype.fragment.KitchenFragment;
import com.tbs.tobosutype.fragment.ManpowerFragment;
import com.tbs.tobosutype.fragment.MaterialFragment;
import com.tbs.tobosutype.fragment.SteelFragment;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CacheManager;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Lie on 2017/5/31.
 */

public class EditAccountAcitivity extends FragmentActivity{
    private Context context;
    private static final String TAG = EditAccountAcitivity.class.getSimpleName();

    /**
     * typeID
     *
     * 人工 1
     * 建材 2
     * 五金 3
     * 家具 4
     * 厨卫 5
     * 其他 6
     */
//    private static final String[] typeID = new String[]{"1","2","3","4","5","6"};

    private TextView tvCancelEdit;
    private TextView tvSaveEdit;
    private TextView tvManPower;
    private TextView tvMateria;
    private TextView tvSteel;
    private TextView tvKitchen;
    private TextView tvFurniture;

    private ImageView ivManpower;
    private ImageView ivMateria;
    private ImageView ivSteel;
    private ImageView ivKitchen;
    private ImageView ivFurniture;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private int saveData = 0;

    private String recordId = "";

    private String mainNameText = "";
    private String mainMoneyText = "";
    private String mainTimeText = "";
    private String mainContentText = "";
    private String outcomeTypeId = "";

//    private String name;
//    private String time;
//    private String money;
//    private String content;
    private int fragmentPosition = -1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(EditAccountAcitivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_edit_account);

        context = EditAccountAcitivity.this;

        initFragment();
        initView();
        initDataRecever();
        setClick();
        initGetIntent();
    }

    private void initView(){
        tvCancelEdit = (TextView) findViewById(R.id.tv_cancel_edit);
        tvSaveEdit = (TextView) findViewById(R.id.tv_save_edit);
        tvManPower = (TextView) findViewById(R.id.tv_manpower);
        tvMateria = (TextView) findViewById(R.id.tv_materia);
        tvSteel = (TextView) findViewById(R.id.tv_steel);
        tvFurniture = (TextView) findViewById(R.id.tv_furniture);
        tvKitchen = (TextView) findViewById(R.id.tv_kitchen);

        ivManpower = (ImageView) findViewById(R.id.iv_manpower);
        ivMateria = (ImageView) findViewById(R.id.iv_materia);
        ivSteel = (ImageView) findViewById(R.id.iv_steel);
        ivKitchen = (ImageView) findViewById(R.id.iv_kitchen);
        ivFurniture = (ImageView) findViewById(R.id.iv_furniture);

        saveData = setFragment(0);

    }

    private void initGetIntent(){
        Intent data = getIntent();
        Bundle b = null;
        // 从记录而来的
        if(data!=null && data.getBundleExtra("check_record_bundle") != null){
            b = data.getBundleExtra("check_record_bundle");
            recordId = b.getString("record_id");
            fragmentPosition = b.getInt("outcome_position");
            saveData = goFragment(fragmentPosition);
        }
    }


    private void setClick(){

        tvManPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                saveData = setFragment(0);
            }
        });
        tvMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                saveData = setFragment(1);
            }
        });
        tvSteel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                saveData = setFragment(2);
            }
        });
        tvFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                saveData = setFragment(3);
            }
        });
        tvKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                saveData = setFragment(4);
            }
        });

        tvCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordId = "";
                CacheManager.clearStringArrayList(context);
                finish();
            }
        });

        tvSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;
                // 请求网络
                if(saveData==0){
                    intent = new Intent(Constant.ACTION_MANPOWER_FRAGMENT_DATA);
                }else if(saveData==1){
                    intent = new Intent(Constant.ACTION_MATERIAL_FRAGMENT_DATA);
                }else if(saveData==2){
                    intent = new Intent(Constant.ACTION_STEEL_FRAGMENT_DATA);
                }else if(saveData==3){
                    intent = new Intent(Constant.ACTION_FURNITURE_FRAGMENT_DATA);
                }else if(saveData==4){
                    intent = new Intent(Constant.ACTION_KITCHEN_FRAGMENT_DATA);
                }else {
                    saveData=0;
                    intent = new Intent(Constant.ACTION_MANPOWER_FRAGMENT_DATA);
                }
                sendBroadcast(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }

    private void initDataRecever(){
        receiver = new DataReceiver();
        IntentFilter filter = new IntentFilter(Constant.ACTION_GET_FRAGMENT_DATA);
        registerReceiver(receiver, filter);
    }

    private DataReceiver receiver;
    private class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {
            if(Constant.ACTION_GET_FRAGMENT_DATA.equals(intent.getAction())){
                String dataArray[] = intent.getStringArrayExtra("dataArray");
                if(dataArray!=null){
                    mainNameText = dataArray[0];
                    mainMoneyText = dataArray[1];
                    mainTimeText = dataArray[2];
                    mainContentText = dataArray[3];
                    outcomeTypeId = dataArray[4];

                    Util.setErrorLog(TAG,"***" + saveData + "=>>>" + mainNameText);
                    Util.setErrorLog(TAG,"***" + saveData + "=>>>" + mainMoneyText);
                    Util.setErrorLog(TAG,"***" + saveData + "=>>>" + mainTimeText);
                    Util.setErrorLog(TAG,"***" + saveData + "=>>>" + mainContentText);
                    Util.setErrorLog(TAG,"***" + saveData + "=>>>=" + outcomeTypeId);

                    if("".equals(mainNameText)){
                        Util.setToast(context,"名称不能为空");
                        return;
                    }
                    if("".equals(mainMoneyText)){
                        Util.setToast(context,"金额不能为空");
                        return;
                    }
                    if("".equals(mainTimeText)){
                        Util.setToast(context,"时间不能为空");
                        return;
                    }

                    if(Util.isNetAvailable(context)){
                        OKHttpUtil okHttpUtil = new OKHttpUtil();
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("token", Util.getDateToken());
                        hashMap.put("uid", AppInfoUtil.getUserid(context));
                        hashMap.put("type_id", outcomeTypeId);
                        hashMap.put("expend_name", mainNameText);
                        hashMap.put("cost", mainMoneyText);
                        hashMap.put("expend_time", mainTimeText);
                        hashMap.put("content", mainContentText+" ");

                        String url = "";
                        if("".equals(recordId)){
                            // 正常而来， 添加开支记录
                            url = Constant.EDIT_DECORATE_OUTCOME_URL;
                        }else{
                            // 修改开支记录
                            url = Constant.MODIFY_RECORD_URL;
                            hashMap.put("id", recordId);
                        }

                        okHttpUtil.post(url, hashMap, new OKHttpUtil.BaseCallBack() {

                            @Override
                            public void onSuccess(Response response, String json) {
                                Util.setErrorLog(TAG, json);
                                try {
                                    JSONObject obj = new JSONObject(json);
                                    if(obj.getInt("status")==200){
                                        Util.setToast(context, getToastMesssage(saveData)+"添加成功");
//                                        setResultCode(Constant.FINISH_SAVE_EDIT_OUTCOME);
                                        finish();
                                    }else if(obj.getInt("status")==0){
                                        Util.setToast(context, getToastMesssage(saveData)+"添加失败,请修改再试!");
                                    }else {
                                        Util.setToast(context, "请修改再试!");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFail(Request request, IOException e) {
                                Util.setToast(context, getToastMesssage(saveData)+"添加失败,请修改再试!");
                            }

                            @Override
                            public void onError(Response response, int code) {
                                Util.setToast(context, getToastMesssage(saveData)+"添加失败,请修改再试!");
                            }
                        });
                    }
                }
            }
        }
    }

    private ManpowerFragment manpower;
    private MaterialFragment material;
    private SteelFragment steel;
    private FurnitureFragment furniture;
    private KitchenFragment kitchen;
    private void initFragment(){
        manpower = new ManpowerFragment();
        material = new MaterialFragment();
        steel = new SteelFragment();
        furniture = new FurnitureFragment();
        kitchen = new KitchenFragment();
    }

    private int goFragment(int position){
        int p = position -1;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (p){
            case 0:
                ft.replace(R.id.edit_account_container,manpower);
                ivManpower.setVisibility(View.VISIBLE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                break;
            case 1:
                ft.replace(R.id.edit_account_container,material);
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.VISIBLE);
                ivSteel.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                break;
            case 2:
                ft.replace(R.id.edit_account_container,steel);
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.VISIBLE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);

                break;
            case 3:
                ft.replace(R.id.edit_account_container,furniture);
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.VISIBLE);
                ivKitchen.setVisibility(View.GONE);
                break;
            case 4:
                ft.replace(R.id.edit_account_container,kitchen);
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.VISIBLE);
                break;
        }
        ft.commit();
        return p;
    }


    private String getToastMesssage(int save){
        String text = "开支";
        switch (save){
            case 0:
                text = "人工开支";
                break;
            case 1:
                text = "建材开支";
                break;
            case 2:
                text = "五金开支";
                break;
            case 3:
                text = "家具开支";
                break;
            case 4:
                text = "厨卫开支";
        }
        return text;
    }

    private int setFragment(int position){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (position){
            case 0:
                ivManpower.setVisibility(View.VISIBLE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                ft.replace(R.id.edit_account_container,new ManpowerFragment());
                break;
            case 1:
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.VISIBLE);
                ivSteel.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                ft.replace(R.id.edit_account_container,new MaterialFragment());
                break;
            case 2:
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.VISIBLE);
                ivKitchen.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                ft.replace(R.id.edit_account_container,new SteelFragment());
                break;
            case 3:
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.VISIBLE);
                ivKitchen.setVisibility(View.GONE);
                ft.replace(R.id.edit_account_container,new FurnitureFragment());
                break;
            case 4:
                ivManpower.setVisibility(View.GONE);
                ivMateria.setVisibility(View.GONE);
                ivSteel.setVisibility(View.GONE);
                ivFurniture.setVisibility(View.GONE);
                ivKitchen.setVisibility(View.VISIBLE);
                ft.replace(R.id.edit_account_container,new KitchenFragment());
                break;
        }
        ft.commit();

        return position;
    }

}
