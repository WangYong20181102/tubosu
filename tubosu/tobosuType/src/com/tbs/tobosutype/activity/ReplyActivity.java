package com.tbs.tobosutype.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.tbs.tobosutype.customview.CustomDialog;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.utils.CommonUtil;
import com.tbs.tobosutype.utils.ToastUtil;
import com.tbs.tobosutype.utils.Util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

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
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Mr.Wang on 2018/11/12 17:49.
 * 我要回答界面
 */
public class ReplyActivity extends BaseActivity implements TextWatcher {
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
    /**
     * 当前输入文本内容
     */
    private String currentContent = "";
    private ReplyActivityAdapter adapter;
    private ArrayList<String> listImagePath;
    private ArrayList<String> list;
    private MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");    //上传图片类型
    private OkHttpClient okHttpClient;


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
        okHttpClient = new OkHttpClient();
        etContent.addTextChangedListener(this);
    }

    /**
     * 初始化图片
     */
    private void setRecyclerview() {
        list = new ArrayList<String>();
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
            listImagePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            compress(listImagePath);
        }
    }

    //压缩 拿到返回选中图片的集合url，然后转换成file文件
    private void compress(ArrayList<String> list) {
        for (String imageUrl : list) {
            File file = new File(imageUrl);
            compressImage(file);
        }
        adapter.addMoreItem(list);
    }

    //压缩
    private void compressImage(File file) {

        Luban.get(this)//用的第三方的压缩，开源库
                .load(file)                     //传人要压缩的图片
                .putGear(Luban.THIRD_GEAR)      //设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        //TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(final File file) {
                        URI uri = file.toURI();
                        String[] split = uri.toString().split(":");
                        list.add(split[1]);//压缩后返回的文件，带file字样，所以需要截取
                    }

                    @Override
                    public void onError(Throwable e) {
                        //TODO 当压缩过去出现问题时调用
                    }
                }).launch();//启动压缩
    }


    @OnClick({R.id.tv_cancel, R.id.tv_send, R.id.image_add_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:    //取消
                finish();
                break;
            case R.id.tv_send:  //发布

                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("发不成功").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();



                if (currentContent.length() < 10) {
                    ToastUtil.showShort(this, "问题至少10个字哟");
                    return;
                }
                sendAnswerRequest();
                break;
            case R.id.image_add_photo:  // 添加图片
                CommonUtil.uploadPictures(this, 0, listImagePath);
                break;
        }
    }

    /**
     * 点击发布网络请求(图文)
     */
    private void sendAnswerRequest() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < listImagePath.size(); i++) {
            File file = new File(listImagePath.get(i));
            builder.addFormDataPart("img_url", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        }
        builder.addFormDataPart("token", Util.getDateToken());
        builder.addFormDataPart("answer_content", currentContent);
        builder.addFormDataPart("question_id", "");
        builder.addFormDataPart("answer_uid", "");
        MultipartBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Constant.ASK_ANSWER_ADDANSWER)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });


    }


    /**
     * 点击删除按钮后，数据改变
     */
    public void deleteResult(ArrayList<String> stringArrayList) {
        listImagePath = stringArrayList;
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
}
