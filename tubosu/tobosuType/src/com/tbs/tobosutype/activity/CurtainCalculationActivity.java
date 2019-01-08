package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.base.HistoryRecordBean;
import com.tbs.tobosutype.bean.CalculationResultsBean;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.widget.DecorationToolCalculationItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2019/1/3 09:43.
 * 窗帘计算
 */
public class CurtainCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_window_height)
    DecorationToolCalculationItem editWindowHeight;
    @BindView(R.id.edit_window_width)
    DecorationToolCalculationItem editWindowWidth;
    @BindView(R.id.edit_curtain_width)
    DecorationToolCalculationItem editCurtainWidth;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;
    private Gson gson;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain_calculation);
        ButterKnife.bind(this);
        gson = new Gson();
    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit:  //历史记录
                Intent intent = new Intent(mContext, HistoryRecordActivity.class);
                intent.putExtra("historyType", 6);
                startActivity(intent);
                break;
            case R.id.btn_start_calculation:
                if (editWindowHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户高度");
                    return;
                }
                if (editWindowWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户宽度");
                    return;
                }
                if (editCurtainWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入布料宽度");
                    return;
                }
                httpResultRequest(recordId);
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EC.EventCode.DECORATION_TOOL:
                HistoryRecordBean bean = (HistoryRecordBean) event.getData();
                editWindowHeight.setEditContent(bean.getDataX().getWindow_height());
                editWindowWidth.setEditContent(bean.getDataX().getWindow_width());
                editCurtainWidth.setEditContent(bean.getDataX().getCloth_width());
                if (!bean.getDataX().getCurtains_price().trim().equals("0")) {
                    editPrice.setEditContent(bean.getDataX().getCurtains_price());
                } else {
                    editPrice.setEditContent("");
                }
                recordId = bean.getId();
                break;
            case EC.EventCode.DECORATION_TOOL_RECORDID:
                recordId = "";
                break;
        }
    }

    /**
     * 计算结果请求
     */
    private void httpResultRequest(String record_id) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        if (TextUtils.isEmpty(AppInfoUtil.getUserid(this))) {
            params.put("uid", "0");
        } else {
            params.put("uid", AppInfoUtil.getUserid(mContext));
        }
        params.put("device_id", AppInfoUtil.getNewMac());
        params.put("window_height", editWindowHeight.getEditContent());
        params.put("window_width", editWindowWidth.getEditContent());
        params.put("cloth_width", editCurtainWidth.getEditContent());
        params.put("curtains_price", editPrice.getEditContent());
        params.put("record_id", record_id);
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_WINDOW_CURTAINS, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    final JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        String data = jsonObject.optString("data");
                        final CalculationResultsBean resultsBean = gson.fromJson(data, CalculationResultsBean.class);
                        if (!resultsBean.getRecord_id().trim().isEmpty()) {
                            recordId = resultsBean.getRecord_id();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, DecorationCalculationResultActivity.class);
                                intent.putExtra(CalculationResultsBean.class.getName(), resultsBean);
                                intent.putExtra("type", 6);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(CurtainCalculationActivity.this, jsonObject.optString("msg"));
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
