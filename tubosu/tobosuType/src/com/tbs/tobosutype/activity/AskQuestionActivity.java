package com.tbs.tobosutype.activity;

import android.Manifest;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AskQuestionActivityAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.utils.CommonUtil;
import com.tbs.tobosutype.utils.ImgCompressUtils;
import com.tbs.tobosutype.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mr.Wang on 2018/11/12 10:15.
 * 提问界面
 */
public class AskQuestionActivity extends BaseActivity {
    @BindView(R.id.tv_cancel)
    TextView tvCancel;  //取消按钮
    @BindView(R.id.image_next)
    TextView imageNext; //下一步
    @BindView(R.id.et_tittle)
    EditText etTittle;  //问题描述
    @BindView(R.id.et_content)
    EditText etContent; //描述内容
    @BindView(R.id.rv_ask_question)
    RecyclerView rvAskQuestion; //图片
    @BindView(R.id.image_add_photo)
    ImageView imageAdd;
    private AskQuestionActivityAdapter adapter;
    private ArrayList<String> listImagePath;
    private ArrayList<String> listPath;//压缩后的图片路径
    /**
     * 标题
     */
    private String inputTittle = "";

    /**
     * 内容
     */
    private String inputContent = "";
    /**
     * 入口（1问答首页）
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askquestion_layout);
        ButterKnife.bind(this);
        initData();
        setRecyclerview();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        type = getIntent().getIntExtra("type", 0);
        etTittle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputTittle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    /**
     * 回调
     *
     * @param event 事件
     */
    @Override
    protected void receiveEvent(Event event) {
        super.receiveEvent(event);
        switch (event.getCode()) {
            case EC.EventCode.SEND_SUCCESS_CLOSE_ASKANSWER: //详情页进入
                finish();
                break;
            case EC.EventCode.SEND_SUCCESS_CLOSE_ASKANSWER_HOME:    //问答首页进入
                finish();
                break;
        }
    }

    /**
     * 初始化图片
     */
    private void setRecyclerview() {
        listPath = new ArrayList<String>();
        listImagePath = new ArrayList<>();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvAskQuestion.setLayoutManager(layoutManager);
        adapter = new AskQuestionActivityAdapter(this, listImagePath);
        rvAskQuestion.setAdapter(adapter);
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
        for (int i = 0; i < list.size(); i++) { //图片压缩
            listPath.add(ImgCompressUtils.CompressAndGetPath(mContext, listImagePath.get(i)));
        }
        adapter.addMoreItem(list);
    }

    @OnClick({R.id.image_add_photo, R.id.tv_cancel, R.id.image_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_add_photo:
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
            case R.id.tv_cancel:
                finish();
                if (adapter != null) {
                    adapter = null;
                }
                break;
            case R.id.image_next:   //下一步
                if (inputTittle.trim().isEmpty()) {
                    ToastUtil.showShort(this, "请输入您的问题");
                    return;
                } else if (inputTittle.length() < 5) {
                    ToastUtil.showShort(this, "问题至少5个字哟");
                    return;
                }
                Intent intent = new Intent(AskQuestionActivity.this, SelectTypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("listImagePath", listPath);
                bundle.putString("inputTittle", inputTittle);
                bundle.putString("inputContent", inputContent);
                bundle.putInt("type", type);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 点击删除按钮后，数据改变
     */
    public void deleteResult(ArrayList<String> stringArrayList, int position) {
        listImagePath = stringArrayList;
        listPath.remove(position);
    }
}
