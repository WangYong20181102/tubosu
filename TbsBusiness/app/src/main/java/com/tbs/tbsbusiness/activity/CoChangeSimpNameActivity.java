package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.util.EventBusUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoChangeSimpNameActivity extends BaseActivity {
    /**
     * 装修公司修改简称
     */


    @BindView(R.id.co_change_simpname_dissmiss_rl)
    RelativeLayout coChangeSimpnameDissmissRl;
    @BindView(R.id.co_change_simpname_ok_rl)
    RelativeLayout coChangeSimpnameOkRl;
    @BindView(R.id.co_change_simpname_edit)
    EditText coChangeSimpnameEdit;
    @BindView(R.id.co_change_simpname_edit_clean)
    RelativeLayout coChangeSimpnameEditClean;
    @BindView(R.id.co_chage_simpname_sign)
    TextView coChageSimpnameSign;
    private String TAG = "CoChangeSimpNameActivity";
    private Context mContext;
    private Intent mIntent;
    private String mSimpName = "";//上个界面带来的名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_change_simp_name);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEvent() {
        mIntent = getIntent();
        mSimpName = mIntent.getStringExtra("mSimpName");//获取全称
        coChangeSimpnameEdit.addTextChangedListener(textWatcher);
        coChangeSimpnameEdit.setText("" + mSimpName);//设置全称
        coChangeSimpnameEdit.setSelection(coChangeSimpnameEdit.getText().toString().length());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                //名称被清除 显示提示语
//                coChageSimpnameSign.setVisibility(View.VISIBLE);
                Toast.makeText(mContext, "请输入公司简称！", Toast.LENGTH_SHORT).show();
            } else {
//                coChageSimpnameSign.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @OnClick({R.id.co_change_simpname_dissmiss_rl, R.id.co_change_simpname_ok_rl, R.id.co_change_simpname_edit_clean})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.co_change_simpname_dissmiss_rl:
                finish();
                break;
            case R.id.co_change_simpname_ok_rl:
                if (!TextUtils.isEmpty(coChangeSimpnameEdit.getText().toString())
                        && coChangeSimpnameEdit.getText().toString().length() >= 2) {
                    changeNameOk(coChangeSimpnameEdit.getText().toString());
                }
                break;
            case R.id.co_change_simpname_edit_clean:
                //清除输入的数据
                coChangeSimpnameEdit.setText("");
                break;
        }
    }

    private void changeNameOk(String simpName) {
        EventBusUtil.sendEvent(new Event(EC.EventCode.NOTICE_CO_NET_STORE_CHANGE_SIMP_NAME, simpName));
        finish();
    }
}
