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
 * Created by Mr.Wang on 2019/1/3 09:40.
 * 地板计算
 */
public class FloorCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_room_long)
    DecorationToolCalculationItem editRoomLong;
    @BindView(R.id.edit_room_width)
    DecorationToolCalculationItem editRoomWidth;
    @BindView(R.id.edit_floor_long)
    DecorationToolCalculationItem editFloorLong;
    @BindView(R.id.edit_floor_thickness)
    DecorationToolCalculationItem editFloorThickness;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;
    private Gson gson;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_calculation);
        ButterKnife.bind(this);
        gson = new Gson();
    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回
                finish();
                break;
            case R.id.tv_edit://历史记录
                Intent intent = new Intent(mContext, HistoryRecordActivity.class);
                intent.putExtra("historyType", 3);
                startActivity(intent);
                break;
            case R.id.btn_start_calculation:
                if (!editRoomLong.setInputContentJudge(mContext, "房间长度")) {
                    return;
                }
                if (!editRoomWidth.setInputContentJudge(mContext, "房间宽度")) {
                    return;
                }
                if (!editFloorLong.setInputContentJudge(mContext, "地板长度")) {
                    return;
                }
                if (!editFloorThickness.setInputContentJudge(mContext, "地板宽度")) {
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
                editRoomLong.setEditContent(bean.getDataX().getRoom_length());
                editRoomWidth.setEditContent(bean.getDataX().getRoom_width());
                editFloorLong.setEditContent(bean.getDataX().getTile_length());
                editFloorThickness.setEditContent(bean.getDataX().getTile_width());
                if (!bean.getDataX().getTile_price().trim().equals("0")) {
                    editPrice.setEditContent(bean.getDataX().getTile_price());
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
        params.put("room_length", editRoomLong.getEditContent());
        params.put("room_width", editRoomWidth.getEditContent());
        params.put("tile_length", editFloorLong.getEditContent());
        params.put("tile_width", editFloorThickness.getEditContent());
        params.put("tile_price", editPrice.getEditContent());
        params.put("record_id", record_id);
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_FLOOR, params, new Callback() {
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
                                intent.putExtra("type", 3);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customizeToast1(FloorCalculationActivity.this, jsonObject.optString("msg"));
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
