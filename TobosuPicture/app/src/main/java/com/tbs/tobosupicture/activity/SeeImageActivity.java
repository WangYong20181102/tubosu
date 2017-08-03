package com.tbs.tobosupicture.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.ImgEntity;
import com.tbs.tobosupicture.bean.ImgJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.fragment.HouseFragment;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.TouchImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 工装 家装 看大图
 * Created by Lie on 2017/7/19.
 */

public class SeeImageActivity extends BaseActivity {
    @BindView(R.id.relSeeImgBack)
    RelativeLayout relSeeImgBack;
    @BindView(R.id.ivImgShare)
    ImageView ivImgShare;
    @BindView(R.id.vpSeeImg)
    ViewPager vpSeeImg;
    @BindView(R.id.tvImgDesc)
    TextView tvImgDesc;
    @BindView(R.id.tvImgDesc2)
    TextView tvImgDesc2;
    @BindView(R.id.tvGetPrice)
    TextView tvGetPrice;
    @BindView(R.id.ivCollectImg)
    ImageView ivCollectImg;
    @BindView(R.id.ivDownImg)
    ImageView ivDownImg;
    @BindView(R.id.tvChangeNum)
    TextView tvChangeNum;
    @BindView(R.id.tvImgTotalNum)
    TextView tvImgTotalNum;
    @BindView(R.id.ivShowAndHide)
    ImageView ivShowAndHide;
    @BindView(R.id.relShangla)
    RelativeLayout relShangla;
    @BindView(R.id.numSequeenLayout)
    LinearLayout numSequeenLayout;
    @BindView(R.id.seeImgBottom)
    RelativeLayout seeImgBottom;
    @BindView(R.id.relLayout)
    RelativeLayout relLayout;
    @BindView(R.id.ivBigCollect)
    ImageView ivBigCollect;

    private int currentPosition = 0;
    private ArrayList<ImgEntity> imgEntityList;
    private boolean isShow = true;
    private String imgId;
    private String uid;
    private String isCollect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "SeeImageActivity";
        mContext = SeeImageActivity.this;
        setContentView(R.layout.activity_see_image);
        ButterKnife.bind(this);
        getIntentData();
    }

    private void getIntentData() {
        if (getIntent() != null && getIntent().getBundleExtra("img_bundle") != null) {
            imgId = getIntent().getBundleExtra("img_bundle").getString("img_id");
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("suite_id", imgId);
            hashMap.put("token", Utils.getDateToken());
            if (Utils.isNetAvailable(mContext)) {
                HttpUtils.doPost(UrlConstans.PICTURE_LIST_URL, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.setToast(mContext, "系统繁忙，稍后再试~");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        Utils.setErrorLog(TAG, json);
                        final ImgJsonEntity jsonEntity = new ImgJsonEntity(json);
                        if (jsonEntity.getStatus() == 200) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imgEntityList = jsonEntity.getSeeImgEntity().getImgEntityList();
                                    isCollect = jsonEntity.getSeeImgEntity().getIs_collect();
                                    if (imgEntityList.size() > 0) {
                                        intiViewPager(imgEntityList);
                                    }
                                }
                            });
                        } else {
                            // 无数据

                        }
                    }
                });
            }
        } else {

        }

    }

    private void intiViewPager(final ArrayList<ImgEntity> imgDataList) {
        if("1".equals(isCollect)){
            ivCollectImg.setBackgroundResource(R.mipmap.shoucang4);
        }else{
            ivCollectImg.setBackgroundResource(R.mipmap.shoucang21);
        }
        tvImgTotalNum.setText(imgDataList.size() + "");
        ArrayList<TouchImageView> dataList = new ArrayList<TouchImageView>();
        for (int i = 0; i < imgDataList.size(); i++) {
            TouchImageView iv = new TouchImageView(mContext);
            GlideUtils.glideLoader(mContext, imgDataList.get(i).getImg_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, iv);
            dataList.add(iv);
        }
        tvImgDesc.setText(imgDataList.get(0).getTitle() + " " + imgDataList.get(0).getSpace_name() + " " + imgDataList.get(0).getPart_name());
        tvImgDesc2.setText(imgDataList.get(0).getDesign_concept() + "  预算:" + imgDataList.get(0).getPlan_price() + "万");

        ImgViewpagerAdapter adapter = new ImgViewpagerAdapter(dataList);
        vpSeeImg.setAdapter(adapter);
        vpSeeImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                tvChangeNum.setText((position + 1) + "");
                tvImgDesc.setText(imgDataList.get(position).getTitle() + " " + imgDataList.get(position).getSpace_name() + " " + imgDataList.get(position).getPart_name());
                tvImgDesc2.setText(imgDataList.get(position).getDesign_concept() + "  预算:" + imgDataList.get(position).getPlan_price() + "万");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class ImgViewpagerAdapter extends PagerAdapter {
        private ArrayList<TouchImageView> imgList;

        public ImgViewpagerAdapter(ArrayList<TouchImageView> imgList) {
            this.imgList = imgList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(imgList.get(position), 0);
            return imgList.get(position);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imgList == null ? 0 : imgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    @OnClick({R.id.relSeeImgBack, R.id.ivImgShare, R.id.tvGetPrice, R.id.ivCollectImg, R.id.ivDownImg, R.id.ivShowAndHide})
    public void onViewClickedSeeImageActivity(View view) {
        switch (view.getId()) {
            case R.id.relSeeImgBack:
                finish();
                break;
            case R.id.ivImgShare:
                Utils.setToast(mContext, "分享啊");
                break;
            case R.id.tvGetPrice:
                // 底部弹框
                initPopupWindow();
                break;
            case R.id.ivCollectImg:
                if(Utils.isNetAvailable(mContext)){
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("token", Utils.getDateToken());
                    hashMap.put("uid", UrlConstans.UID);
                    hashMap.put("suite_id", imgId);

                    HttpUtils.doPost(UrlConstans.COLLECT_PIC_URL, hashMap, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Utils.setToast(mContext, "系统繁忙，稍后再试~");
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String json = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    int status = -1;
                                    String msg = "";
                                    try {
                                        JSONObject jsonObject = new JSONObject(json);
                                        status = jsonObject.getInt("status");
                                        msg = jsonObject.getString("msg");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if(status==200){
                                        if(msg.contains("取消")){
                                            // 取消
                                            ivCollectImg.setBackgroundResource(R.mipmap.shoucang21);
                                        }else{
                                            // 关注
                                            ivCollectImg.setBackgroundResource(R.mipmap.shoucang4);
                                            getConcren(ivBigCollect);
                                        }
                                        Utils.setToast(mContext, msg);
                                    }else{
                                        Utils.setToast(mContext, msg);
                                    }
                                }
                            });
                        }
                    });
                }
                break;
            case R.id.ivDownImg:
                httpDownLoadImg(imgEntityList.get(currentPosition).getImg_url());
                break;

            case R.id.ivShowAndHide:
                if(isShow){
                    goDown(seeImgBottom, relLayout);
                    goingUp(numSequeenLayout, ivShowAndHide);
                }else{
                    goUp(seeImgBottom, relLayout);
                    goingDown(numSequeenLayout, ivShowAndHide);
                }
                isShow = !isShow;
                break;
        }
    }

    //TODO 下载图片
    private void httpDownLoadImg(String downloadUrl) {
        //创建文件夹
        File dirFile = new File(UrlConstans.IMG_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        HttpUtils.downFile(downloadUrl, dirFile.getPath(), fileName);
        if (Utils.isNetAvailable(mContext)) {
            Toast.makeText(mContext, "图片下载成功!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "图片下载失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void goingUp(View view, View view1){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", 0.0f, -(view1.getHeight()));
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(844).start();
    }


    private void goUp(View view, View view1){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", view1.getHeight(), 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(844).start();
    }

    private void goingDown(View view, View view1){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", -(view1.getHeight()), 0.0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(844).start();
    }

    private void goDown(View view, View view1){
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("translationX", 0.0f, 0.0f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("translationY", 0.0f, view1.getHeight());
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, valuesHolderX, valuesHolderY);
        objectAnimator.setDuration(844).start();
    }


    private PopupWindow popupWindow;
    private void initPopupWindow() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout_getprice, null);
        ImageView close = (ImageView) contentView.findViewById(R.id.iv_close_popupwindow);
        final EditText etInputPhone = (EditText) contentView.findViewById(R.id.et_input_phone);
        TextView getThisPrice = (TextView) contentView.findViewById(R.id.get_this_price);

        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setContentView(contentView);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        getThisPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("".equals(etInputPhone.getText().toString())){
                    Utils.setToast(mContext, "你还没输入电话号码");
                    return;
                }else{
                    if(etInputPhone.getText().toString().matches(UrlConstans.PHONE_NUM)) {
                        Utils.setToast(mContext, "电话号码不正确，重新输入");
                        return;
                    }
                    if(Utils.isNetAvailable(mContext)){
                        Utils.setToast(mContext, "发单接口没写啊");
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("uid", UrlConstans.UID);
//                        HttpUtils.doPost(UrlConstans.get, hashMap, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                            }
//                        });
                    }
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
            }
        });

        // 这里有红线 不是出错，不用管
        popupWindow.showAsDropDown(findViewById(R.id.rel_consdfaa),0,0,Gravity.BOTTOM);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    private void getConcren(final View view){
        view.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.out_down_to_up);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.clearAnimation();
                view.setVisibility(View.GONE);
            }
        });
        view.startAnimation(animation);
    }
}
