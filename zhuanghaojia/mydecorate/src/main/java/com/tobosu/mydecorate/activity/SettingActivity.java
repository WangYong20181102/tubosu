package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.database.DBManager;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.util.CacheManager;
import com.tobosu.mydecorate.util.CheckUpdateUtils;
import com.tobosu.mydecorate.util.DataCleanManager;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.ClearCacheDialog;
import com.tobosu.mydecorate.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dec on 2016/9/27.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SettingActivity.class.getSimpleName();
    private Context mContext;
    private RelativeLayout rel_setting_back;

    private RelativeLayout rel_clear_cache;
    private RelativeLayout rel_go_critical;
    private RelativeLayout rel_update_version;
    private RelativeLayout rel_contribution;
    private RelativeLayout rel_logout;

    private TextView tv_cache;
    private ImageView iv_new_verion_flag;

    private TextView tvLogout;

    private CheckUpdateUtils checkUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = SettingActivity.this;
        initViews();
        initData();
    }


    private void initViews() {
        rel_setting_back = (RelativeLayout) findViewById(R.id.rel_setting_back);
        rel_setting_back.setOnClickListener(this);

        tv_cache = (TextView) findViewById(R.id.tv_cache);

        rel_clear_cache = (RelativeLayout) findViewById(R.id.rel_clear_cache);
        rel_go_critical = (RelativeLayout) findViewById(R.id.rel_go_critical);
        rel_update_version = (RelativeLayout) findViewById(R.id.rel_update_version);
        iv_new_verion_flag = (ImageView) findViewById(R.id.iv_new_verion_flag);
        rel_contribution = (RelativeLayout) findViewById(R.id.rel_contribution);
        rel_logout = (RelativeLayout) findViewById(R.id.rel_logout);
        tvLogout = (TextView) findViewById(R.id.tv_logout_setting);

        checkUtils = new CheckUpdateUtils(mContext);

        if (MyApplication.HAS_NEW_VERSION) {
            iv_new_verion_flag.setVisibility(View.VISIBLE);
        } else {
            iv_new_verion_flag.setVisibility(View.GONE);
        }

        rel_clear_cache.setOnClickListener(this);
        rel_go_critical.setOnClickListener(this);
        rel_update_version.setOnClickListener(this);
        rel_contribution.setOnClickListener(this);
        rel_logout.setOnClickListener(this);


    }

    private void initData() {
        //获取缓存
        try {
            if ("0.0Byte".equals(DataCleanManager.getTotalCacheSize(getApplicationContext()))) {
                tv_cache.setText("0 M");
            } else {
                tv_cache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvLogout.setText("退出");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_setting_back:
                finish();
                break;
            case R.id.rel_clear_cache:
                if (!"0 M".equals(tv_cache.getText())) {
                    clearAppCache();
                } else {
                    Toast.makeText(mContext, "你已清理过缓存", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rel_go_critical:
                String mAddress = "market://details?id=" + getPackageName();
                Intent marketIntent = new Intent("android.intent.action.VIEW");
                marketIntent.setData(Uri.parse(mAddress));
                startActivity(Intent.createChooser(marketIntent, "请选择要查看的市场软件"));
                break;
            case R.id.rel_update_version:
                checkUtils.startUpdata();
                break;
            case R.id.rel_contribution:
                // 投稿地址: mt.tobosu.com
                CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
//                builder.setTitle("提示");
                builder.setMessage("投稿地址：您可以登录pc端开通媒体平台哦");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                    }
                });
                builder.create().show();
                break;
            case R.id.rel_logout:
                // 退出当前账户 并没有退出app
//                if(Util.isLogin(mContext)){
//                    SharedPreferences saveInfo = getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = saveInfo.edit();
//                    editor.putString("user_name", "");
//                    editor.putString("head_pic_url", "");
//                    editor.putString("mark", "");
//                    editor.putString("user_id", "");
//                    editor.putString("token", "");
//                    editor.putString("city_name", "");
//                    editor.commit();
//
//                    CacheManager.setUserUid(mContext, "");
//                }else {
                setResult(Constant.APP_LOGOUT);
//                }

//                getSharedPreferences("See_Tip_Sp", Context.MODE_PRIVATE).edit().putString("has_seen_tip", "").commit();

//                DBManager manager = DBManager.getInstance(mContext);
//                manager.deleteTable("Concern_Writer_Table");
//                manager.deleteTable("Main_Data");
//                manager.deleteTable("Recommend_Data");
                finish();
                break;
        }
    }

    /**
     * 清除app缓存
     */
    private void clearAppCache() {
        ClearCacheDialog.Builder builder = new ClearCacheDialog.Builder(this);
        builder.setMessage("是否清除所有缓存")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        DataCleanManager.clearAllCache(getApplicationContext());
                        tv_cache.setText("0 M");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
