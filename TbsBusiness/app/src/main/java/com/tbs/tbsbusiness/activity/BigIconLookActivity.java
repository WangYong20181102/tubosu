package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.util.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BigIconLookActivity extends BaseActivity {

    @BindView(R.id.big_icon_iamge)
    ImageView bigIconIamge;
    @BindView(R.id.big_icon_rl)
    RelativeLayout bigIconRl;
    private Context mContext;
    private String TAG = "BigIconLookActivity";
    private Intent mIntent;
    private String mImageIconUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_icon_look);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mImageIconUrl = mIntent.getStringExtra("mImageIconUrl");
        GlideUtils.glideLoader(mContext, mImageIconUrl, R.drawable.iamge_loading, R.drawable.iamge_loading, bigIconIamge);
    }

    @OnClick({R.id.big_icon_iamge, R.id.big_icon_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.big_icon_iamge:
            case R.id.big_icon_rl:
                finish();
                break;
        }
    }
}