package com.tbs.tobosutype.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 土拨鼠3.4版本 新增
 * 案例列表页 creat by lin
 */
public class DecorationCaseActivity extends BaseActivity {
    @BindView(R.id.deco_case_back)
    LinearLayout decoCaseBack;
    @BindView(R.id.deco_case_recycler)
    RecyclerView decoCaseRecycler;
    @BindView(R.id.deco_case_swipe)
    SwipeRefreshLayout decoCaseSwipe;
    @BindView(R.id.deco_case_find_price)
    TextView decoCaseFindPrice;

    private Context mContext;
    private String TAG = "DecorationCaseActivity";
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoration_case);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvet();
    }

    private void initViewEvet() {
        
    }

    @OnClick({R.id.deco_case_back, R.id.deco_case_find_price})
    public void onViewClickedInDecorationCaseActivity(View view) {
        switch (view.getId()) {
            case R.id.deco_case_back:
                finish();
                break;
            case R.id.deco_case_find_price:
                //跳转到发单页
                break;
        }
    }
}
