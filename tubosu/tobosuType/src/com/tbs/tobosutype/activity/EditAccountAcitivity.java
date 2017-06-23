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
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

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

    private int saveData = -1;


    private String mainNameText = "";
    private String mainMoneyText = "";
    private String mainTimeText = "";
    private String mainContentText = "";
    private String outcomeTypeId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(EditAccountAcitivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_edit_account);

        context = EditAccountAcitivity.this;
        initView();
        initDataRecever();
        setClick();
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

    private void setClick(){

        tvManPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = setFragment(0);
            }
        });
        tvMateria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = setFragment(1);
            }
        });
        tvSteel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = setFragment(2);
            }
        });
        tvFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = setFragment(3);
            }
        });
        tvKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData = setFragment(4);
            }
        });

        tvCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = null;
                // 请求网络
                if(saveData==0){
                    intent = new Intent(AllConstants.ACTION_MANPOWER_FRAGMENT_DATA);
                }else if(saveData==1){
                    intent = new Intent(AllConstants.ACTION_MATERIAL_FRAGMENT_DATA);
                }else if(saveData==2){
                    intent = new Intent(AllConstants.ACTION_STEEL_FRAGMENT_DATA);
                }else if(saveData==3){
                    intent = new Intent(AllConstants.ACTION_FURNITURE_FRAGMENT_DATA);
                }else if(saveData==4){
                    intent = new Intent(AllConstants.ACTION_KITCHEN_FRAGMENT_DATA);
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
        IntentFilter filter = new IntentFilter(AllConstants.ACTION_GET_FRAGMENT_DATA);
        registerReceiver(receiver, filter);
    }

    private DataReceiver receiver;
    private class DataReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(AllConstants.ACTION_GET_FRAGMENT_DATA.equals(intent.getAction())){
                String dataArray[] = intent.getStringArrayExtra("dataArray");
                if(dataArray!=null){
                    mainNameText = dataArray[0];
                    mainMoneyText = dataArray[1];
                    mainTimeText = dataArray[2];
                    mainContentText = dataArray[3];
                    outcomeTypeId = dataArray[4];

                    Util.setErrorLog(TAG,"******" + saveData + "=" + mainNameText);
                    Util.setErrorLog(TAG,"******" + saveData + "=" + mainMoneyText);
                    Util.setErrorLog(TAG,"******" + saveData + "=" + mainTimeText);
                    Util.setErrorLog(TAG,"******" + saveData + "=" + mainContentText);

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


                    OKHttpUtil okHttpUtil = new OKHttpUtil();
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("token", Util.getDateToken());
                    hashMap.put("uid", AppInfoUtil.getUserid(context));
                    hashMap.put("type_id", outcomeTypeId);
                    hashMap.put("expend_name", mainNameText);
                    hashMap.put("cost", mainMoneyText);
                    hashMap.put("expend_time", mainTimeText);
                    hashMap.put("content", mainContentText);
                    okHttpUtil.post(AllConstants.EDIT_DECORATE_OUTCOME_URL, hashMap, new OKHttpUtil.BaseCallBack() {
                        @Override
                        public void onSuccess(Response response, String json) {
                            Util.setErrorLog(TAG, json);
                        }

                        @Override
                        public void onFail(Request request, IOException e) {

                        }

                        @Override
                        public void onError(Response response, int code) {

                        }
                    });


                }
            }
        }
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
