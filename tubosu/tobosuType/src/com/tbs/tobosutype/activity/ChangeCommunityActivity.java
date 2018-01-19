package com.tbs.tobosutype.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.utils.EventBusUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改用户小区信息
 * 3.7版本新增
 * creat by lin
 */
public class ChangeCommunityActivity extends com.tbs.tobosutype.base.BaseActivity {
    @BindView(R.id.change_community_dissmiss_rl)
    RelativeLayout changeCommunityDissmissRl;
    @BindView(R.id.change_community_ok_rl)
    RelativeLayout changeCommunityOkRl;
    @BindView(R.id.change_community_edit)
    EditText changeCommunityEdit;
    @BindView(R.id.change_community_text_num)
    TextView changeCommunityTextNum;
    @BindView(R.id.banner_dever)
    View bannerDever;
    private Context mContext;
    private String TAG = "ChangeCommunityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_community);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initViewEvent() {
        changeCommunityEdit.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changeCommunityTextNum.setText("" + s.length());
        }
    };

    @OnClick({R.id.change_community_dissmiss_rl, R.id.change_community_ok_rl})
    public void onViewClickedInChangeCommunityActivity(View view) {
        switch (view.getId()) {
            case R.id.change_community_dissmiss_rl:
                //取消操作
                finish();
                break;
            case R.id.change_community_ok_rl:
                //确认修改
                EventBusUtil.sendEvent(new Event(EC.EventCode.CHANGE_COMMUNITY, changeCommunityEdit.getText().toString()));
                finish();
                break;
        }
    }
}
