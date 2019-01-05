package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.os.Bundle;
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
 * Created by Mr.Wang on 2019/1/3 09:41.
 * 壁纸计算
 */
public class WallpaperCalculationActivity extends BaseActivity {
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
    @BindView(R.id.edit_wallpaper_specification)
    DecorationToolCalculationItem editWallpaperSpecification;
    @BindView(R.id.edit_price)
    DecorationToolCalculationItem editPrice;
    @BindView(R.id.btn_start_calculation)
    Button btnStartCalculation;
    private Gson gson;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_calculation);
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
                intent.putExtra("historyType", 4);
                startActivity(intent);
                break;
            case R.id.btn_start_calculation:
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
                if (editWallpaperSpecification.getEditContent().isEmpty()) {
                    ToastUtil.customizeToast1(this, "输入壁纸规格");
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
                editWallpaperSpecification.setEditContent(bean.getDataX().getPaper_info());
                if (!bean.getDataX().getPaper_price().trim().equals("0")) {
                    editPrice.setEditContent(bean.getDataX().getPaper_price());
                }else {
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
        params.put("room_height",editRoomHeight.getEditContent());
        params.put("paper_info", editWallpaperSpecification.getEditContent());
        params.put("paper_price", editPrice.getEditContent());
        params.put("record_id", record_id);
        OKHttpUtil.post(Constant.MAPP_DECORATIONTOOL_WALL_PAPER, params, new Callback(){
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
                        if (!resultsBean.getRecord_id().trim().isEmpty()){
                            recordId = resultsBean.getRecord_id();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(mContext, DecorationCalculationResultActivity.class);
                                intent.putExtra(CalculationResultsBean.class.getName(), resultsBean);
                                intent.putExtra("type", 4);
                                startActivity(intent);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(WallpaperCalculationActivity.this, jsonObject.optString("msg"));
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
