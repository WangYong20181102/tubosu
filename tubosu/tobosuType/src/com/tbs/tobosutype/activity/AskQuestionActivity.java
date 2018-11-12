package com.tbs.tobosutype.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.AskQuestionActivityAdapter;
import com.tbs.tobosutype.base.BaseActivity;
import com.tbs.tobosutype.utils.CommonUtil;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Mr.Wang on 2018/11/12 10:15.
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
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askquestion_layout);
        ButterKnife.bind(this);
        setRecyclerview();
    }


    /**
     * 初始化图片
     */
    private void setRecyclerview() {
        list = new ArrayList<String>();
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

    @OnClick({R.id.image_add_photo, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_add_photo:

                CommonUtil.uploadPictures(this, 0, listImagePath);

                break;
            case R.id.tv_cancel:
                finish();
                if (adapter != null) {
                    adapter = null;
                }
                break;
        }
    }

    /**
     * 启用系统键盘
     */
    private void startSystemKey() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 点击删除按钮后，数据改变
     */
    public void deleteResult(ArrayList<String> stringArrayList) {
        listImagePath = stringArrayList;
    }

}
