package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.TaotuJsonItem;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.Util;
import java.io.IOException;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TaoTuAcitivity extends AppCompatActivity {
    @BindView(R.id.relBackTao)
    RelativeLayout relBackTao;
    @BindView(R.id.recyclerviewTaotu)
    RecyclerView recyclerviewTaotu;
    private Context context;
    private String TAG = "TaoTuAcitivity";
    @BindView(R.id.tvEditTaotu)
    TextView tvEditTaotu;
    @BindView(R.id.tvDelelteTaotu)
    TextView tvDelelteTaotu;
    private boolean isEditTaoTu = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = TaoTuAcitivity.this;
        setContentView(R.layout.activity_taotu);
        ButterKnife.bind(this);
        getNetData();
    }


    private int page = 1;
    private void getNetData(){
        if(Util.isNetAvailable(context)){
            SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String type = sp.getString("typeid", "1");
            String userid = sp.getString("userid", "272286");

            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("token", Util.getDateToken());
            hashMap.put("uid", userid);
            hashMap.put("user_type", type);
            hashMap.put("page_size", "10");
            hashMap.put("page", page);

            okHttpUtil.post(Constant.FAV_TAO_TU_URL, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "服务繁忙，稍后再试。");
                        }
                    });
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String json = response.body().string();
                    Util.setErrorLog(TAG, json);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(json.contains("data")){
                                Gson gson = new Gson();
                                TaotuJsonItem taotuJsonItem = gson.fromJson(json, TaotuJsonItem.class);
                                String msg = taotuJsonItem.getMsg();
                                if(taotuJsonItem.getStatus() == 200){

                                }else if(taotuJsonItem.getStatus() == 201 || taotuJsonItem.getStatus() == 0){
                                    Util.setToast(context, msg);
                                }
                            }else {

                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick({R.id.relBackTao, R.id.tvDelelteTaotu, R.id.tvEditTaotu})
    public void onTaoTuAcitivityViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackTao:
                if(isEditTaoTu){
                    isEditTaoTu = !isEditTaoTu;
                    tvEditTaotu.setText("编辑");
                    tvDelelteTaotu.setVisibility(View.GONE);
                }else {
                    finish();
                }
                break;
            case R.id.tvDelelteTaotu:
                 // 删除套图的请求

                break;
            case R.id.tvEditTaotu:
                if (isEditTaoTu){
                    tvEditTaotu.setText("编辑");
                    tvDelelteTaotu.setVisibility(View.GONE);
                }else {
                    tvEditTaotu.setText("取消");
                    tvDelelteTaotu.setVisibility(View.VISIBLE);
                }
                isEditTaoTu = !isEditTaoTu;
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isEditTaoTu){
            isEditTaoTu = !isEditTaoTu;
            tvDelelteTaotu.setVisibility(View.GONE);
            tvEditTaotu.setText("编辑");
            return true;
        }else {
            if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
