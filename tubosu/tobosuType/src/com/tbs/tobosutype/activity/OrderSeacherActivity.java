package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderSeacherActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.order_seach_back)
    RelativeLayout orderSeachBack;
    @BindView(R.id.order_seach_input_et)
    EditText orderSeachInputEt;
    @BindView(R.id.order_seach_tv_btn)
    TextView orderSeachTvBtn;
    private String TAG = "OrderSeacherActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_seacher);
        ButterKnife.bind(this);
        mContext = this;
    }

    @OnClick({R.id.order_seach_back, R.id.order_seach_input_et, R.id.order_seach_tv_btn})
    public void onViewClickedOnOrderSeacherActivity(View view) {
        switch (view.getId()) {
            case R.id.order_seach_back:
                finish();
                break;
            case R.id.order_seach_input_et:

                break;
            case R.id.order_seach_tv_btn:
                //进入搜索结果页面
                intoSearchResult(orderSeachInputEt.getText().toString());
                break;
        }
    }

    private void intoSearchResult(String searchInfo) {
        if (!TextUtils.isEmpty(searchInfo)) {
            Intent intent = new Intent(mContext, OrderSearchResultActivity.class);
            intent.putExtra("mSearchInfo", searchInfo);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(mContext, "请输入搜索的内容~", Toast.LENGTH_SHORT).show();
        }
    }
}
