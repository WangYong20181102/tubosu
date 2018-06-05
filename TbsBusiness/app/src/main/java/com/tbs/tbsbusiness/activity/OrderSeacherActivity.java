package com.tbs.tbsbusiness.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.config.MyApplication;
import com.tbs.tbsbusiness.util.MD5Util;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderSeacherActivity extends BaseActivity {

    @BindView(R.id.order_seach_back)
    RelativeLayout orderSeachBack;
    @BindView(R.id.order_seach_input_et)
    EditText orderSeachInputEt;
    @BindView(R.id.order_seach_clean_iv)
    ImageView orderSeachCleanIv;
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

    @OnClick({R.id.order_seach_back, R.id.order_seach_clean_iv,
            R.id.order_seach_tv_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_seach_back:
                finish();
                break;
            case R.id.order_seach_clean_iv:
                orderSeachInputEt.setText("");
                break;
            case R.id.order_seach_tv_btn:
                intoSearchResult(orderSeachInputEt.getText().toString());
                break;
        }
    }

    private void intoSearchResult(String searchInfo) {
        if (!TextUtils.isEmpty(searchInfo)) {
            if (MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD) {
                Intent intent = new Intent(mContext, OrderSearchResultActivity.class);
                intent.putExtra("mSearchInfo", searchInfo);
                startActivity(intent);
                finish();
            } else {
                showChadanPassWordPop(searchInfo);
            }

        } else {
            Toast.makeText(mContext, "请输入搜索的内容~", Toast.LENGTH_SHORT).show();
        }
    }
    //处理类型
    private void showChadanPassWordPop(final String searchInfo) {
        /**
         * 用户输入了密码之后 在App没有kill 是可以直接打开订单查询的
         */
        View popview = View.inflate(mContext, R.layout.pop_check_pass_word, null);
        TextView pop_check_pw_quxiao = (TextView) popview.findViewById(R.id.pop_check_pw_quxiao);
        TextView pop_check_pw_ok = (TextView) popview.findViewById(R.id.pop_check_pw_ok);
        final EditText pop_check_pw_edit = popview.findViewById(R.id.pop_check_pw_edit);
        //全局蒙层
        RelativeLayout phone_pop_window_rl = (RelativeLayout) popview.findViewById(R.id.phone_pop_window_rl);
        //白色显示层
        RelativeLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //点击确认按钮
        pop_check_pw_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pop_check_pw_edit.getText().toString())) {
                    Toast.makeText(mContext, "请输入查单密码~", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("token", Util.getDateToken());
                    param.put("id", SpUtil.getCompany_id(mContext));
                    param.put("password", MD5Util.md5(pop_check_pw_edit.getText().toString()));
                    OKHttpUtil.post(Constant.CHECK_ORDER_PWD, param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mContext, "链接服务器失败~", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(final Call call, Response response) throws IOException {
                            String json = new String(response.body().string());
                            Log.e(TAG, "验证订单密码链接成功=============" + json);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                String status = jsonObject.optString("status");
                                final String msg = jsonObject.optString("msg");
                                if (status.equals("200")) {
                                    //验证密码成功 跳转查单页
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyApplication.IS_CHECK_COMPANY_ORDER_PASSWORD = true;
                                            //订单验证成功
                                            Intent intent = new Intent(mContext, OrderSearchResultActivity.class);
                                            intent.putExtra("mSearchInfo", searchInfo);
                                            startActivity(intent);
                                            finish();
                                            popupWindow.dismiss();
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        //取消
        pop_check_pw_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        phone_pop_window_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }
}
