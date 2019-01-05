package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * Created by Mr.Wang on 2019/1/3 09:34.
 * 涂料计算
 */
public class PaintCalculationActivity extends BaseActivity {
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    @BindView(R.id.tv_edit)
    ImageView tvEdit;
    @BindView(R.id.edit_room_long)
    DecorationToolCalculationItem editRoomLong;
    @BindView(R.id.edit_room_width)
    DecorationToolCalculationItem editRoomWidth;
    @BindView(R.id.edit_room_height)
    DecorationToolCalculationItem editRoomHeight;
    @BindView(R.id.edit_room_door_height)
    DecorationToolCalculationItem editRoomDoorHeight;
    @BindView(R.id.edit_room_door_width)
    DecorationToolCalculationItem editRoomDoorWidth;
    @BindView(R.id.edit_room_door_num)
    DecorationToolCalculationItem editRoomDoorNum;
    @BindView(R.id.edit_window_height)
    DecorationToolCalculationItem editWindowHeight;
    @BindView(R.id.edit_window_width)
    DecorationToolCalculationItem editWindowWidth;
    @BindView(R.id.edit_window_num)
    DecorationToolCalculationItem editWindowNum;
    @BindView(R.id.edit_tuliao_cover)
    DecorationToolCalculationItem editTuliaoCover;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;
    private Gson gson;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_calculation);
        ButterKnife.bind(this);
        initData();
        gson = new Gson();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        editWindowNum.setNumInputTypeInt(4);
        editRoomDoorNum.setNumInputTypeInt(4);
    }

    @OnClick({R.id.rlBack, R.id.tv_edit, R.id.btn_start_calculation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlBack:   //返回按钮
                finish();
//                hideSystemKeyBroad();
                break;
            case R.id.tv_edit:  //历史记录
                Intent intent = new Intent(mContext, HistoryRecordActivity.class);
                intent.putExtra("historyType", 5);
                startActivity(intent);
                break;
            case R.id.btn_start_calculation:    //开始计算按钮
                if (editRoomLong.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间长度");
                    return;
                }
                if (editRoomWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间宽度");
                    return;
                }
                if (editRoomHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房间高度");
                    return;
                }
                if (editRoomDoorHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门长度");
                    return;
                }
                if (editRoomDoorWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门宽度");
                    return;
                }
                if (editRoomDoorNum.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入房门数量");
                    return;
                }
                if (editWindowHeight.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户高度");
                    return;
                }
                if (editWindowWidth.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户宽度");
                    return;
                }
                if (editWindowNum.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入窗户数量");
                    return;
                }
                if (editTuliaoCover.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入涂料覆盖率");
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
                editRoomHeight.setEditContent(bean.getDataX().getRoom_height());
                editRoomDoorHeight.setEditContent(bean.getDataX().getDoor_height());
                editRoomDoorWidth.setEditContent(bean.getDataX().getDoor_width());
                editRoomDoorNum.setEditContent(bean.getDataX().getDoor_number());
                editWindowHeight.setEditContent(bean.getDataX().getWindow_height());
                editWindowWidth.setEditContent(bean.getDataX().getWindow_width());
                editWindowNum.setEditContent(bean.getDataX().getWindow_number());
                editTuliaoCover.setEditContent(bean.getDataX().getCoverage_rate());

                if (!bean.getDataX().getCoating_price().trim().equals("0")) {
                    editPrice.setEditContent(bean.getDataX().getCoating_price());
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
        params.put("uid", AppInfoUtil.getUserid(mContext));
        params.put("room_length", editRoomLong.getEditContent());
        params.put("room_width", editRoomWidth.getEditContent());
        params.put("room_height", editRoomHeight.getEditContent());
        params.put("door_height", editRoomDoorHeight.getEditContent());
        params.put("door_width", editRoomDoorWidth.getEditContent());
        params.put("door_number", editRoomDoorNum.getEditContent());
        params.put("window_height", editWindowHeight.getEditContent());
        params.put("window_width", editWindowWidth.getEditContent());
        params.put("window_number", editWindowNum.getEditContent());
        params.put("coverage_rate", editTuliaoCover.getEditContent());
        params.put("coating_price", editPrice.getEditContent());
        params.put("record_id", record_id);
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_COATING, params, new Callback() {
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
                                intent.putExtra("type", 5);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(PaintCalculationActivity.this, jsonObject.optString("msg"));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSystemKeyBroad() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isActive()) {   //如果为true,则是键盘正在显示
            ((InputMethodManager) Objects.requireNonNull(getSystemService(Context.INPUT_METHOD_SERVICE))).hideSoftInputFromWindow(Objects.requireNonNull(PaintCalculationActivity.this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            System.gc();
        }
    }

}
