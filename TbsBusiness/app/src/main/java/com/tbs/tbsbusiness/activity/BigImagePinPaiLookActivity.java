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

public class BigImagePinPaiLookActivity extends BaseActivity {
    @BindView(R.id.big_image_pipai_iamge)
    ImageView bigImagePipaiIamge;
    @BindView(R.id.big_image_pipai_rl)
    RelativeLayout bigImagePipaiRl;
    private Context mContext;
    private String TAG = "BigImagePinPaiLookActivity";
    private Intent mIntent;
    private String mImageIconUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_pin_pai_look);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mIntent = getIntent();
        mImageIconUrl = mIntent.getStringExtra("mImageIconUrl");
        GlideUtils.glideLoader(mContext, mImageIconUrl, R.drawable.iamge_loading, R.drawable.iamge_loading, bigImagePipaiIamge);
    }

    @OnClick({R.id.big_image_pipai_iamge, R.id.big_image_pipai_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.big_image_pipai_iamge:
            case R.id.big_image_pipai_rl:
                finish();
                break;
        }
    }
}
