package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BigImageLookActivity extends BaseActivity {

    @BindView(R.id.big_image_iamge)
    ImageView bigImageIamge;
    @BindView(R.id.big_iamge_rl)
    RelativeLayout bigIamgeRl;
    private Context mContext;
    private String TAG = "BigIconLookActivity";
    private Intent mIntent;
    private String mImageIconUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image_look);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }


    private void initViewEvent() {
        mIntent = getIntent();
        mImageIconUrl = mIntent.getStringExtra("mImageIconUrl");
        Log.e(TAG, "获取的查看图片================" + mImageIconUrl);

        Glide.with(mContext).load(mImageIconUrl).into(bigImageIamge);
    }

    @OnClick({R.id.big_image_iamge, R.id.big_iamge_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.big_image_iamge:
            case R.id.big_iamge_rl:
                finish();
                break;
        }
    }
}
