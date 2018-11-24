package com.tbs.tobosutype.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.SelectTypeLeftAdapter;
import com.tbs.tobosutype.adapter.SelectTypeRightAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean.SelectTypeBean;
import com.tbs.tobosutype.bean._ImageUpload;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.PhotoUploadUtils;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.utils.WriteUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SelectTypeActivity extends BaseActivity implements PhotoUploadUtils.OnPhotoUploadListener {
    @BindView(R.id.lv_left)
    ListView lvLest;
    @BindView(R.id.lv_right)
    ListView lvRight;
    private SelectTypeLeftAdapter adapterLeft;
    private SelectTypeRightAdapter adapterRight;
    private String inputTittle = ""; //标题
    private String inputContent = "";    //内容
    private List<String> listPath;//图片路径
    private int type = 0;   //进来的入口
    private List<SelectTypeBean> typeBeanList;//选择分类数据集合
    private Gson gson;
    private String first_cate_id;//一级分类id
    private int firstPosition = 0;//一级分类position
    private String second_cate_id;//二级分类id

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
        listPath = bundle.getStringArrayList("listImagePath");
        type = bundle.getInt("type");
        typeBeanList = new ArrayList<>();
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
                        typeBeanList = gson.fromJson(jsonObject.optString("data"), new TypeToken<List<SelectTypeBean>>() {
                        }.getType());
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
                if (listPath.isEmpty()) {
                    sendHttpRequest("");
                } else {
                    PhotoUploadUtils uploadUtils = PhotoUploadUtils.getInstences(SelectTypeActivity.this, listPath);
                    uploadUtils.setOnPhotoUploadListener(this);
                    uploadUtils.startUpload();
                }
                break;
        }
    }


    /**
     * 发布按钮网络请求
     */
    private void sendHttpRequest(String imageUrls) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("img_urls", imageUrls);
        params.put("token", Util.getDateToken());
        params.put("title", inputTittle);
        params.put("published_uid", AppInfoUtil.getUserid(mContext));
        params.put("first_cate_id", first_cate_id);
        params.put("second_cate_id", second_cate_id);
        params.put("content", inputContent);
        OKHttpUtil.post(Constant.ASK_ADDQUESTION, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        final String id = jsonObject.getJSONObject("data").getString("id");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customizeToast(SelectTypeActivity.this, "提问成功");
                                if (type == 1) {
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.SEND_SUCCESS_CLOSE_ASKANSWER_HOME));
                                    Intent intent = new Intent(SelectTypeActivity.this, AnswerItemDetailsActivity.class);
                                    intent.putExtra("question_id", id);
                                    startActivity(intent);
                                } else {
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.SEND_SUCCESS_CLOSE_ASKANSWER, id));
                                }
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void imagePath(String path) {
        sendHttpRequest(path);
    }
}
