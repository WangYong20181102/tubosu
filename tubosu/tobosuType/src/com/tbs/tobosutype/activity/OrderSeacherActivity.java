package com.tbs.tobosutype.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderSeacherActivity extends BaseActivity {
    @BindView(R.id.order_seach_back)
    RelativeLayout orderSeachBack;
    @BindView(R.id.order_seach_input_et)
    EditText orderSeachInputEt;
    @BindView(R.id.order_seach_tv_btn)
    TextView orderSeachTvBtn;
    @BindView(R.id.order_seach_clean_iv)
    ImageView orderSeachCleanIv;
    private String TAG = "OrderSeacherActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_seacher);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        orderSeachInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    orderSeachCleanIv.setVisibility(View.GONE);
                } else {
                    orderSeachCleanIv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.order_seach_back, R.id.order_seach_input_et, R.id.order_seach_tv_btn, R.id.order_seach_clean_iv})
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
            case R.id.order_seach_clean_iv:
                orderSeachInputEt.setText("");
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
