package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.fragment.FurnitureFragment;
import com.tbs.tobosutype.fragment.KitchenFragment;
import com.tbs.tobosutype.fragment.ManpowerFragment;
import com.tbs.tobosutype.fragment.MaterialFragment;
import com.tbs.tobosutype.fragment.SteelFragment;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

/**
 * Created by Lie on 2017/5/31.
 */

public class EditAccountAcitivity extends FragmentActivity{
    private Context context;
    private static final String TAG = EditAccountAcitivity.class.getSimpleName();
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

    private ManpowerFragment manpowerFragment;
    private MaterialFragment materialFragment;
    private SteelFragment steelFragment;
    private FurnitureFragment furnitureFragment;
    private KitchenFragment kitchenFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Util.setActivityStatusColor(EditAccountAcitivity.this);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_edit_account);

        context = EditAccountAcitivity.this;
        initView();
        initFragment();
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

    private void initFragment(){
        manpowerFragment = new ManpowerFragment();
        materialFragment = new MaterialFragment();
        steelFragment = new SteelFragment();
        furnitureFragment = new FurnitureFragment();
        kitchenFragment = new KitchenFragment();
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
                // 请求网络
                if(saveData==0){
                    mainNameText = manpowerFragment.nameText;
                    mainMoneyText = manpowerFragment.moneyText;
                    mainTimeText = manpowerFragment.timeText;
                    mainContentText = manpowerFragment.contentText;
                }else if(saveData==1){
                    mainNameText = materialFragment.nameText;
                    mainMoneyText = materialFragment.moneyText;
                    mainTimeText = materialFragment.timeText;
                    mainContentText = materialFragment.contentText;
                }if(saveData==2){
                    mainNameText = steelFragment.nameText;
                    mainMoneyText = steelFragment.moneyText;
                    mainTimeText = steelFragment.timeText;
                    mainContentText = steelFragment.contentText;
                }if(saveData==3){
                    mainNameText = furnitureFragment.nameText;
                    mainMoneyText = furnitureFragment.moneyText;
                    mainTimeText = furnitureFragment.timeText;
                    mainContentText = furnitureFragment.contentText;
                }if(saveData==4){
                    mainNameText = kitchenFragment.nameText;
                    mainMoneyText = kitchenFragment.moneyText;
                    mainTimeText = kitchenFragment.timeText;
                    mainContentText = kitchenFragment.contentText;
                }

                Util.setErrorLog(TAG,"******" + saveData + "=" + mainNameText);
                Util.setErrorLog(TAG,"******" + saveData + "=" + mainMoneyText);
                Util.setLog(TAG,"******" + saveData + "=" + mainTimeText);


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


                // 请求啦 FIXME

            }
        });
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
