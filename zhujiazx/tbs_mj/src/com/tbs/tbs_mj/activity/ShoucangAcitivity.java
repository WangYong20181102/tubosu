package com.tbs.tbs_mj.activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.ShoucangAdapter;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean.ShoucangItem;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ShoucangAcitivity extends com.tbs.tbs_mj.base.BaseActivity {
    @BindView(R.id.relBackShoucang)
    RelativeLayout relBackShoucang;
    @BindView(R.id.shouList)
    ListView shouList;
    @BindView(R.id.relMyfavNodata)
    RelativeLayout relMyfavNodata;
    private Context context;
    private String TAG = "ShoucangAcitivity";
    private List<ShoucangItem> favList = new ArrayList<>();
    private ShoucangAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = ShoucangAcitivity.this;
        setContentView(R.layout.activity_shoucang);
        ButterKnife.bind(this);

        getDataFromNet();
    }

    private void getDataFromNet() {
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String type = sp.getString("typeid", "1");
        String userid = sp.getString("userid", Constant.DEFAULT_USER_ID);
        String _id = sp.getString("id", "");
        Util.setErrorLog(TAG,type +  "=======type收藏id=====" + _id);
        if(Util.isNetAvailable(context)){
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            HashMap<String, Object> ha = new HashMap<>();
            ha.put("token", Util.getDateToken());
            ha.put("user_type", type);
            ha.put("uid", _id);
            okHttpUtil.post(Constant.MYFAV_URL, ha, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.setToast(context, "系统繁忙，请稍后再试");
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
                            relMyfavNodata.setVisibility(View.GONE);
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                int status = jsonObject.getInt("status");
                                String msg = jsonObject.getString("msg");
                                if(status==200){
                                    JSONArray dataArr = jsonObject.getJSONArray("data");
                                    favList.clear();
                                    for(int i=0;i<dataArr.length();i++){
                                        ShoucangItem shoucangItem = new ShoucangItem();
                                        shoucangItem.setCount(dataArr.getJSONObject(i).getString("count"));
                                        shoucangItem.setIcon(dataArr.getJSONObject(i).getString("icon"));
                                        shoucangItem.setName(dataArr.getJSONObject(i).getString("name"));
                                        favList.add(shoucangItem);
                                    }
                                    adapter = new ShoucangAdapter(context, favList);
                                    shouList.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    shouList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            if(favList.get(position).getIcon().equals("suite")){
                                                startActivity(new Intent(context, TaoTuAcitivity.class));
                                            }else if(favList.get(position).getIcon().equals("single")){
                                                startActivity(new Intent(context, DanTuAcitivity.class));
                                            }else if(favList.get(position).getIcon().equals("company")){
                                                startActivity(new Intent(context, ZhuangxiuConmpanyAcitivity.class));
                                            }
                                        }
                                    });
                                }else if(status==0 || status==201){
                                    Util.setToast(context, msg);
                                    relMyfavNodata.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
        switch (event.getCode()){
            case EC.EventCode.DELETE_TAOTU_CODE:
                favList.clear();
                adapter = null;
                getDataFromNet();
//                Util.setToast(mContext, "刷新啦");
                break;
            case EC.EventCode.COLLECT_COMPANY_CODE:
                getDataFromNet();
                break;
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @OnClick(R.id.relBackShoucang)
    public void onShoucangViewClicked(View v) {
        switch (v.getId()){
            case R.id.relBackShoucang:
                finish();
                break;
        }
    }
}
