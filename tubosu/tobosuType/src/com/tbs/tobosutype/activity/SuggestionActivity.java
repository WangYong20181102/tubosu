package com.tbs.tobosutype.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.SuggestionAdapter;
import com.tbs.tobosutype.base.*;
import com.tbs.tobosutype.bean._ImageUpload;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImgCompressUtils;
import com.tbs.tobosutype.utils.SpUtil;
import com.tbs.tobosutype.utils.Util;
import com.tbs.tobosutype.utils.Utils;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 反馈建议
 * 3.7版本新增
 * creat by lin
 */
public class SuggestionActivity extends com.tbs.tobosutype.base.BaseActivity {

    @BindView(R.id.suggestion_back)
    RelativeLayout suggestionBack;
    @BindView(R.id.suggestion_input)
    EditText suggestionInput;
    @BindView(R.id.suggestion_image_recyclerview)
    RecyclerView suggestionImageRecyclerview;
    @BindView(R.id.suggestion_input_text_num)
    TextView suggestionInputTextNum;
    @BindView(R.id.suggestion_input_phone_num)
    EditText suggestionInputPhoneNum;
    @BindView(R.id.banner_dever)
    View bannerDever;
    @BindView(R.id.suggestion_comite)
    TextView suggestionComite;
    private String TAG = "SuggestionActivity";
    private Context mContext;
    private Gson mGson;
    private String images = "";//上传的图片地址

    private GridLayoutManager mGridLayoutManager;
    private SuggestionAdapter mSuggestionAdapter;
    private ArrayList<String> mImageUriArrayList;//获取的图片的路径
    private ArrayList<String> mUpLoadImageUrlList;//获取的图片的路径
    private ArrayList<String> mCompressImageUriPath;//获取的图片的路径
    private static final int REQUEST_IMAGE = 3;//图片选择器用到的code
    private String resultStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mGson = new Gson();
        mImageUriArrayList = new ArrayList<>();
        mCompressImageUriPath = new ArrayList<>();
        mUpLoadImageUrlList = new ArrayList<>();
        mGridLayoutManager = new GridLayoutManager(mContext, 4,
                GridLayoutManager.VERTICAL, false);
        suggestionImageRecyclerview.setLayoutManager(mGridLayoutManager);
        mSuggestionAdapter = new SuggestionAdapter(mContext, mImageUriArrayList);
        mSuggestionAdapter.setOnImageViewClickLister(onImageViewClickLister);
        suggestionImageRecyclerview.setAdapter(mSuggestionAdapter);
        suggestionInput.addTextChangedListener(textWatcher);
    }

    //输入监听事件
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e(TAG, "输入字体之后============" + s.length());
            suggestionInputTextNum.setText("" + s.length());
        }
    };
    //适配器点击事件
    private SuggestionAdapter.OnImageViewClickLister onImageViewClickLister = new SuggestionAdapter.OnImageViewClickLister() {
        @Override
        public void onImageClick(View view, int position) {
            switch (view.getId()) {
                case R.id.item_suggestion_close:
                    //将图片删除
                    //进行删除事件
                    mImageUriArrayList.remove(position);
                    mSuggestionAdapter.notifyItemRemoved(position);
                    mSuggestionAdapter.notifyItemRangeChanged(position, mImageUriArrayList.size());
                    break;
                case R.id.item_suggestion_img_add:
                    //添加图片 弹出弹窗
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                0);
                    }
                    showOpenPhotoPop();
                    break;
            }
        }
    };

    private void showOpenPhotoPop() {
        View popview = View.inflate(mContext, R.layout.pop_get_photo, null);
        //取消按钮
        TextView pop_get_photo_dismiss = (TextView) popview.findViewById(R.id.pop_get_photo_dismiss);
        //从相册中获取
        TextView pop_get_photo_from_pic = (TextView) popview.findViewById(R.id.pop_get_photo_from_pic);
        //整个可点击层
        RelativeLayout pop_get_photo_rl = (RelativeLayout) popview.findViewById(R.id.pop_get_photo_rl);
        //按键层
        LinearLayout pop_get_photo_11 = (LinearLayout) popview.findViewById(R.id.pop_get_photo_11);

        pop_get_photo_11.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //取消
        pop_get_photo_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //从相册中获取
        pop_get_photo_from_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/1/12  开启相册的选择器
                MultiImageSelector.create(mContext)
                        .showCamera(false)
                        .count(3)
                        .multi()
                        .origin(mImageUriArrayList)
                        .start(SuggestionActivity.this, REQUEST_IMAGE);
                popupWindow.dismiss();
            }
        });
        //点击非按区界面消失
        pop_get_photo_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    @OnClick({R.id.suggestion_back, R.id.suggestion_comite})
    public void onViewClickedInSuggestionActivity(View view) {
        switch (view.getId()) {
            case R.id.suggestion_back:
                finish();
                break;
            case R.id.suggestion_comite:
                //提交建议反馈
                if (!TextUtils.isEmpty(suggestionInput.getText().toString())) {
                    if (!TextUtils.isEmpty(suggestionInputPhoneNum.getText().toString())) {
                        //手机号码不为空的情况下校验手机号码
                        if (suggestionInputPhoneNum.length() == 11
                                && Util.isVerificationPhoneNum(suggestionInputPhoneNum.getText().toString(), mContext)) {
                            sendImageAndContent();
                        } else {
                            Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        //手机号码为空 将数据上传
                        sendImageAndContent();
                    }

                } else {
                    Toast.makeText(mContext, "请先输入内容", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //上传图片
    private void sendImageAndContent() {
        if (!mImageUriArrayList.isEmpty()) {
            //有图片时进行上传
            //压缩图片
            if (!mCompressImageUriPath.isEmpty()) {
                mCompressImageUriPath.clear();
            }
            for (int i = 0; i < mImageUriArrayList.size(); i++) {
                mCompressImageUriPath.add(ImgCompressUtils.CompressAndGetPath(mContext, mImageUriArrayList.get(i)));
            }
            new Thread(uploadImageRunnable).start();
        } else {
            new Thread(uploadImageRunnable).start();
        }

    }

    //上传图片的线程
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            if (!mCompressImageUriPath.isEmpty()) {
                for (int i = 0; i < mCompressImageUriPath.size(); i++) {
                    Log.e(TAG, "要上传的照片======" + mCompressImageUriPath.get(i));
                    if (TextUtils.isEmpty(Constant.UPLOAD_DYNAMIC_IMAGE)) {
                        Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> textParams = null;
                    Map<String, File> fileparams = null;
                    Map<String, String> token = null;
                    try {
                        URL url = new URL(Constant.UPLOAD_DYNAMIC_IMAGE);
                        textParams = new HashMap<String, String>();
                        fileparams = new HashMap<String, File>();
                        token = new HashMap<String, String>();
                        File file = new File(mCompressImageUriPath.get(i));
                        token.put("token", Util.getDateToken());
                        fileparams.put("filedata", file);
                        textParams.put("app_type", "1");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setUseCaches(false);
                        conn.setRequestProperty("Charset", "UTF-8");
                        conn.setRequestProperty("ser-Agent", "Fiddler");
                        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + WriteUtil.BOUNDARY);
                        OutputStream os = conn.getOutputStream();
                        DataOutputStream ds = new DataOutputStream(os);
                        WriteUtil.writeStringParams(textParams, ds);
                        WriteUtil.writeStringParams(token, ds);
                        WriteUtil.writeFileParams(fileparams, ds);
                        WriteUtil.paramsEnd(ds);
                        // 对文件流操作完,要记得及时关闭
                        os.close();
                        // 服务器返回的响应吗
                        int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                        // 对响应码进行判断
                        if (code == 200) {// 返回的响应码200,是成功
                            // 得到网络返回的输入流
                            InputStream is = conn.getInputStream();
                            resultStr = WriteUtil.readString(is);
                            Log.e(TAG, "上传操作之后的结果======" + resultStr);
                        } else {
                            Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handlerUpload.sendEmptyMessage(0);
                }
            } else {
                handlerUpload.sendEmptyMessage(0);
            }

        }
    };
    private Handler handlerUpload = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (!TextUtils.isEmpty(resultStr)) {
                        _ImageUpload dynamicImageUpload = mGson.fromJson(resultStr, _ImageUpload.class);
                        mUpLoadImageUrlList.add(dynamicImageUpload.getData().getUrl());
                        if (mUpLoadImageUrlList.size() == mCompressImageUriPath.size()) {
                            //检测图片返回
//                            for (int i = 0; i < mUpLoadImageUrlList.size(); i++) {
//                                Log.e(TAG, "返回的图片地址=====" + mUpLoadImageUrlList.get(i));
//                            }
                            HashMap<String, Object> param = new HashMap<>();
                            param.put("token", Util.getDateToken());
                            param.put("uid", AppInfoUtil.getUserid(mContext));
                            param.put("content", suggestionInput.getText().toString());
                            param.put("phone_model", android.os.Build.MODEL);
                            param.put("version", AppInfoUtil.getAppVersionName(mContext));
                            param.put("system_version", android.os.Build.VERSION.RELEASE);
                            if (!TextUtils.isEmpty(suggestionInputPhoneNum.getText().toString())
                                    && suggestionInputPhoneNum.length() == 11) {
                                param.put("cellphone", suggestionInputPhoneNum.getText().toString());
                            }
                            if (!mUpLoadImageUrlList.isEmpty()) {
                                images = mUpLoadImageUrlList.toString().substring(1, mUpLoadImageUrlList.toString().length() - 1);
                                Log.e(TAG, "上传的用逗号隔开的图片============" + images);
                                param.put("images", images);
                            }
                            param.put("system_version", android.os.Build.VERSION.RELEASE);
                            OKHttpUtil.post(Constant.ADD_FEEDBACK, param, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String json = new String(response.body().string());
                                    Log.e(TAG, "链接成功======" + json);
                                    try {
                                        JSONObject jsonObject = new JSONObject(json);
                                        String status = jsonObject.getString("status");
                                        final String msg = jsonObject.getString("msg");
                                        if (status.equals("200")) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, "反馈提交成功~", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        } else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        //没有图片
                        HashMap<String, Object> param = new HashMap<>();
                        param.put("token", Util.getDateToken());
                        param.put("uid", AppInfoUtil.getUserid(mContext));
                        param.put("content", suggestionInput.getText().toString());
                        param.put("phone_model", android.os.Build.MODEL);
                        param.put("version", AppInfoUtil.getAppVersionName(mContext));
                        param.put("system_version", android.os.Build.VERSION.RELEASE);
                        if (!TextUtils.isEmpty(suggestionInputPhoneNum.getText().toString())
                                && suggestionInputPhoneNum.length() == 11) {
                            param.put("cellphone", suggestionInputPhoneNum.getText().toString());
                        }
                        param.put("system_version", android.os.Build.VERSION.RELEASE);
                        OKHttpUtil.post(Constant.ADD_FEEDBACK, param, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String json = new String(response.body().string());
                                Log.e(TAG, "链接成功======" + json);
                                try {
                                    JSONObject jsonObject = new JSONObject(json);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("200")) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "反馈提交成功~", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    } else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(mContext, "反馈提交失败，请重新提交~", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    break;
            }
            return false;
        }
    });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE:
                //图片选择器处理回调
                if (resultCode == Activity.RESULT_CANCELED) {
                    return;
                }
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imaPathList = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (!mImageUriArrayList.isEmpty()) {
                        mImageUriArrayList.clear();
                    }
                    mImageUriArrayList.addAll(imaPathList);
                    mSuggestionAdapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
