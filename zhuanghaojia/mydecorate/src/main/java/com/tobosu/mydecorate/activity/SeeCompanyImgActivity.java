package com.tobosu.mydecorate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.DragAdapter;
import com.tobosu.mydecorate.adapter.OtherAdapter;
import com.tobosu.mydecorate.application.MyApplication;
import com.tobosu.mydecorate.database.ChannelManage;
import com.tobosu.mydecorate.entity.DecorateTitleEntity.ChannelItem;
import com.tobosu.mydecorate.fragment.DecorateArticleListFragment;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.DragGrid;
import com.tobosu.mydecorate.view.OtherGridView;
import com.tobosu.mydecorate.view.TouchImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dec on 2016/9/27.
 */

public class SeeCompanyImgActivity extends Activity{
    private static final String TAG = SeeCompanyImgActivity.class.getSimpleName();
    private Context mContext;
    private TouchImageView see_img;
    private RelativeLayout see_img_activity;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除这个Activity的标题栏
        setContentView(R.layout.activity_see_company_img);
//        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContext = SeeCompanyImgActivity.this;
        initView();
        initData();

    }

    private void initView() {
        see_img = (TouchImageView) findViewById(R.id.see_img);
        see_img_activity = (RelativeLayout) findViewById(R.id.see_img_activity);
        see_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        see_img_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        if(getIntent()!=null && getIntent().getBundleExtra("company_img_bundle")!=null){
            url = getIntent().getBundleExtra("company_img_bundle").getString("company_img");
            GlideUtils.glideLoader(mContext, url, R.mipmap.case_loading_img, R.mipmap.case_loading_img, see_img);
        }
    }


}
