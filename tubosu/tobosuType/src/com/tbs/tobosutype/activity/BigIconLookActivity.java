package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BigIconLookActivity extends AppCompatActivity {
    @BindView(R.id.big_icon_iamge)
    ImageView bigIconIamge;
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

    @OnClick(R.id.big_icon_iamge)
    public void onViewClickedInBigIconLookActivity() {
        finish();
    }
}
