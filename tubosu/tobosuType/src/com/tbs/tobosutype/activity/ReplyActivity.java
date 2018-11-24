package com.tbs.tobosutype.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ReplyActivityAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.CommonUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.ImgCompressUtils;
import com.tbs.tobosutype.utils.PhotoUploadUtils;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Wang on 2018/11/12 17:49.
 * 我要回答界面
 */
public class ReplyActivity extends BaseActivity implements TextWatcher, PhotoUploadUtils.OnPhotoUploadListener {
    @BindView(R.id.tv_cancel)
    TextView tvCancle;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.rv_reply)
    RecyclerView rvReply;
    @BindView(R.id.image_add_photo)
    ImageView imageAddPhoto;
    @BindView(R.id.et_content)
    EditText etContent;
    private String question_id = "";
    /**
     * 当前输入文本内容
     */
    private String currentContent = "";
    private ReplyActivityAdapter adapter;
    /**
     * 图片路径
     */
    private ArrayList<String> listImagePath;
    /**
     * 压缩后的图片路径
     */
    private ArrayList<String> listPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_layout);
        ButterKnife.bind(this);
        initData();
        setRecyclerview();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        question_id = getIntent().getStringExtra("question_id");
        etContent.addTextChangedListener(this);
    }

    /**
     * 初始化图片
     */
    private void setRecyclerview() {
        listPath = new ArrayList<String>();
        listImagePath = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvReply.setLayoutManager(layoutManager);
        adapter = new ReplyActivityAdapter(this, listImagePath);
        rvReply.setAdapter(adapter);
    }

    //用户选中图片后，拿到回掉结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (!listImagePath.isEmpty() || listImagePath.size() != 0) {
                listImagePath.clear();
            }
            if (!listPath.isEmpty() || listPath.size() != 0) {
                listPath.clear();
            }
            listImagePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            compress(listImagePath);
        }
    }

    //压缩 拿到返回选中图片的集合url，然后转换成file文件
    private void compress(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            listPath.add(ImgCompressUtils.CompressAndGetPath(mContext, listImagePath.get(i)));
        }
        adapter.addMoreItem(list);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_send, R.id.image_add_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:    //取消
                finish();
                break;
            case R.id.tv_send:  //发布
                if (currentContent.length() < 10) {
                    ToastUtil.showShort(this, "问题至少10个字哟");
                    return;
                }
                if (listPath.isEmpty()) {
                    sendAnswerRequest("");
                } else {
                    PhotoUploadUtils uploadUtils = PhotoUploadUtils.getInstences(ReplyActivity.this, listPath);
                    uploadUtils.setOnPhotoUploadListener(this);
                    uploadUtils.startUpload();
                }
                break;
            case R.id.image_add_photo:  // 添加图片
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //权限还没有授予，需要在这里写申请权限的代码
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    } else {
                        //权限已经被授予，在这里直接写要执行的相应方法即可
                        CommonUtil.uploadPictures(this, 0, listImagePath);
                    }
                } else {
                    CommonUtil.uploadPictures(this, 0, listImagePath);
                }
                break;
        }
    }

    /**
     * 点击发布网络请求(图文)
     */
    private void sendAnswerRequest(String imgUrls) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("img_urls", imgUrls);
        params.put("token", Util.getDateToken());
        params.put("answer_content", currentContent);
        params.put("question_id", question_id);
        params.put("answer_uid", AppInfoUtil.getUserid(mContext));
        OKHttpUtil.post(Constant.ASK_ANSWER_ADDANSWER, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.customizeToast(ReplyActivity.this, "回答成功");
                                EventBusUtil.sendEvent(new Event(EC.EventCode.SEND_SUCCESS_REPLY));
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShort(ReplyActivity.this, msg);
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
     * 点击删除按钮后，数据改变
     */
    public void deleteResult(ArrayList<String> stringArrayList, int position) {
        listImagePath = stringArrayList;
        listPath.remove(position);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        currentContent = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void imagePath(String path) {
        sendAnswerRequest(path);
    }
}
