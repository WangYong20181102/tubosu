package com.tbs.tobosutype.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.SelectTypeLeftAdapter;
import com.tbs.tobosutype.adapter.SelectTypeRightAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.SelectTypeBean;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/13 08:57.
 * 选择分类
 */
public class SelectTypeActivity extends BaseActivity {
    @BindView(R.id.lv_left)
    ListView lvLest;
    @BindView(R.id.lv_right)
    ListView lvRight;
    private SelectTypeLeftAdapter adapterLeft;
    private SelectTypeRightAdapter adapterRight;
    private String inputTittle = ""; //标题
    private String inputContent = "";    //内容
    private List<String> listImagePath;//图片路径
    private List<SelectTypeBean> typeBeanList;//选择分类数据集合
    private Gson gson;
    private String first_cate_id;//一级分类id
    private int firstPosition = 0;//一级分类position
    private String second_cate_id;//二级分类id
    private MediaType MEDA_TYPE = MediaType.parse("image/*");
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        ButterKnife.bind(this);
        gson = new Gson();
        initData();
        initAdapter();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        inputTittle = bundle.getString("inputTittle");
        inputContent = bundle.getString("inputContent");
        listImagePath = bundle.getStringArrayList("listImagePath");
        typeBeanList = new ArrayList<>();
        okHttpClient = new OkHttpClient();
        initHttpRequest();

    }

    /**
     * 网络请求加载
     */
    private void initHttpRequest() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("token", Util.getDateToken());
        OKHttpUtil.post(Constant.SELECT_CATEGORYLIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body().string());
                try {
                    Log.i("SelectTypeActivity", "onResponse: 进来请求-------------------");
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        typeBeanList = gson.fromJson(jsonObject.optString("data"), new TypeToken<List<SelectTypeBean>>() {}.getType());
                        Message message = new Message();
                        message.obj = typeBeanList;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (adapterLeft == null) {
                                    adapterLeft = new SelectTypeLeftAdapter(SelectTypeActivity.this, typeBeanList);
                                    lvLest.setAdapter(adapterLeft);
                                    //初始position等于0,防止未点击时拿不到数据
                                    first_cate_id = typeBeanList.get(0).getId();
                                }
                                if (adapterRight == null) {
                                    adapterRight = new SelectTypeRightAdapter(SelectTypeActivity.this, typeBeanList.get(0).getChild());
                                    lvRight.setAdapter(adapterRight);
                                    //初始position等于0,防止未点击时拿不到数据
                                    second_cate_id = typeBeanList.get(0).getChild().get(0).getId();
                                }
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //初始化适配器
    private void initAdapter() {
        lvLest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取一级分类position，对应二级position
                firstPosition = position;
                first_cate_id = typeBeanList.get(position).getId();
                adapterLeft.setSelectItem(position);
                adapterRight.updateData(typeBeanList.get(position).getChild(), 0);
                lvRight.setSelectionAfterHeaderView();
                lvRight.smoothScrollToPosition(0);
            }
        });
        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                second_cate_id = typeBeanList.get(firstPosition).getChild().get(position).getId();
                adapterRight.setSelectItem(position);
            }
        });

    }

    @OnClick({R.id.tv_back, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back: //上一步
                finish();
                break;
            case R.id.tv_send: //发布
                sendHttpRequest();
                break;
        }
    }

    /**
     * 发布按钮网络请求
     */
    private void sendHttpRequest() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", Util.getDateToken());
        builder.addFormDataPart("title", inputTittle);
        builder.addFormDataPart("published_uid", AppInfoUtil.getUserid(mContext));
        builder.addFormDataPart("first_cate_id", first_cate_id);
        builder.addFormDataPart("second_cate_id", second_cate_id);
        builder.addFormDataPart("content", inputContent);
        for (int i = 0; i < listImagePath.size(); i++) {
            File file = new File(listImagePath.get(i));
            builder.addFormDataPart("img_urls",file.getName(), RequestBody.create(MEDA_TYPE,file));
        }
        MultipartBody requestBody = builder.build();
        final Request request = new Request.Builder()
                .url(Constant.ASK_ADDQUESTION)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
