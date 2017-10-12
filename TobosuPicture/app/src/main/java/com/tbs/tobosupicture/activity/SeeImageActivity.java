package com.tbs.tobosupicture.activity;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.tbs.tobosupicture.bean.ImgEntity;
import com.tbs.tobosupicture.bean.ImgJsonEntity;
import com.tbs.tobosupicture.constants.UrlConstans;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.HttpUtils;
import com.tbs.tobosupicture.utils.SpUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.TipDialog1;
import com.tbs.tobosupicture.view.TouchImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
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
    @BindView(R.id.tvNeedGetPrice) // 获取设计和报价
    TextView tvNeedGetPrice;
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
    @BindView(R.id.shareProgress)
    ProgressBar shareProgress;
    @BindView(R.id.bar_framelayout)
    FrameLayout bar_framelayout;


    private int currentPosition = 0;
    private ArrayList<ImgEntity> imgEntityList;
    private boolean isShow = true;
    private String imgId;
    private String uid;
    private String isCollect;
    private String shareTitle;  // 分享 标题
    private String shareImage;  // 分享 图片
    private String shareDesc;   // 分享 简介
    private String shareUrl;    // 分享 链接


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
        bar_framelayout.bringToFront();
        if (getIntent() != null && getIntent().getBundleExtra("img_bundle") != null) {
            imgId = getIntent().getBundleExtra("img_bundle").getString("img_id");
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("suite_id", imgId);
            hashMap.put("token", Utils.getDateToken());
            hashMap.put("uid", SpUtils.getUserUid(mContext));
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

                                    isCollect = jsonEntity.getSeeImgEntity().getIs_collect();
                                    shareTitle = jsonEntity.getSeeImgEntity().getShare_title();
                                    shareImage = jsonEntity.getSeeImgEntity().getShare_image();
                                    shareDesc = jsonEntity.getSeeImgEntity().getShare_desc();
                                    shareUrl = jsonEntity.getSeeImgEntity().getShare_url();

                                    if("1".equals(isCollect)){
                                        ivCollectImg.setBackgroundResource(R.mipmap.shoucang4);
                                    }else{
                                        ivCollectImg.setBackgroundResource(R.mipmap.shoucang21);
                                    }

                                    imgEntityList = jsonEntity.getSeeImgEntity().getImgEntityList();

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
        tvImgTotalNum.setText(imgDataList.size() + "");
        ArrayList<TouchImageView> dataList = new ArrayList<TouchImageView>();
        for (int i = 0; i < imgDataList.size(); i++) {
            TouchImageView iv = new TouchImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            GlideUtils.glideLoader(mContext, imgDataList.get(i).getImg_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, iv);
            dataList.add(iv);
        }
        tvImgDesc.setText(imgDataList.get(0).getTitle() + " " + imgDataList.get(0).getSpace_name() + " " + imgDataList.get(0).getPart_name());
        tvImgDesc2.setText(imgDataList.get(0).getDesign_concept()  + "预算:" + imgDataList.get(0).getPlan_price() + "万");

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
                tvImgDesc2.setText(imgDataList.get(position).getDesign_concept() + "预算:" + imgDataList.get(position).getPlan_price() + "万");
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

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareProgress.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            shareProgress.setVisibility(View.GONE);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            shareProgress.setVisibility(View.GONE);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            shareProgress.setVisibility(View.GONE);
        }
    };

    @OnClick({R.id.relSeeImgBack, R.id.ivImgShare, R.id.tvNeedGetPrice, R.id.ivCollectImg, R.id.ivDownImg, R.id.ivShowAndHide})
    public void onViewClickedSeeImageActivity(View view) {
        switch (view.getId()) {
            case R.id.relSeeImgBack:
                finish();
                break;
            case R.id.ivImgShare:
                UMWeb umWeb = new UMWeb(shareUrl);
                umWeb.setDescription(shareDesc);
                umWeb.setTitle(shareTitle);
                umWeb.setThumb(new UMImage(mContext,shareImage));
                new ShareAction(SeeImageActivity.this)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .withMedia(umWeb)
//                        .setCallback(umShareListener)
                        .open();
                break;
            case R.id.tvNeedGetPrice:
                // 底部弹框
                showPopupWindow();
                break;
            case R.id.ivCollectImg:

                if(Utils.userIsLogin(mContext)){
                    if(Utils.isNetAvailable(mContext)){
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("token", Utils.getDateToken());
                        hashMap.put("uid", SpUtils.getUserUid(mContext));
                        hashMap.put("suite_id", imgId);

//                        Utils.setErrorLog(TAG, "参数 " + Utils.getDateToken()  + "   " +Utils.getDateToken()
//                         + "   " + SpUtils.getUserUid(mContext)+ "   " + imgId);

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
                                Utils.setErrorLog(TAG, json);
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
                }else{
                    Utils.gotoLogin(mContext);
                }

                break;
            case R.id.ivDownImg:
                httpDownLoadImg(imgEntityList.get(currentPosition).getImg_url());
                break;

            case R.id.ivShowAndHide:
                if(isShow){
                    goDown(seeImgBottom, relLayout);
                    goingUp(numSequeenLayout, ivShowAndHide);
                    ivShowAndHide.setBackgroundResource(R.mipmap.shangla);
                }else{
                    goUp(seeImgBottom, relLayout);
                    goingDown(numSequeenLayout, ivShowAndHide);
                    ivShowAndHide.setBackgroundResource(R.mipmap.shouqi2);
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
        if (Utils.isNetAvailable(mContext)) {
            HttpUtils.downFile(downloadUrl, dirFile.getPath(), fileName);
            Toast.makeText(mContext, "图片下载成功!", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(mContext, "图片下载失败！", Toast.LENGTH_SHORT).show();
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


    private void showPopupWindow() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplayout_getprice, null);
        contentView.setFocusable(true);
        ImageView close = (ImageView) contentView.findViewById(R.id.iv_close_popupwindow);
        final EditText etInputPhone = (EditText) contentView.findViewById(R.id.et_input_phone);
        TextView getThisPrice = (TextView) contentView.findViewById(R.id.get_this_price);

        final PopupWindow popupWindow = new PopupWindow(SeeImageActivity.this);
        popupWindow.setContentView(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        getThisPrice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phone = etInputPhone.getText().toString();
                if("".equals(phone)){
                    Utils.setToast(mContext, Utils.inputPhoneNumTip());
                    return;
                }else{
                    if(!phone.startsWith("1")){
                        Utils.setToast(mContext, Utils.inputCorrectPhoneNumTip());
                        return;
                    }else if(phone.length() != 11){
                        Utils.setToast(mContext, Utils.inputCorrectPhoneNumTip());
                        return;
                    }

                    if(Utils.isNetAvailable(mContext)){
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("cellphone", phone);
                        hashMap.put("token", Utils.getDateToken());
                        hashMap.put("urlhistory", UrlConstans.PIPE_CODE);
                        hashMap.put("comeurl", UrlConstans.PIPE_CODE);
                        hashMap.put("source", "1200");
                        String city = SpUtils.getHomeCity(mContext);
                        hashMap.put("city", city);
                        Utils.setErrorLog(TAG, phone + "****city=" +city + "****" +UrlConstans.PIPE_CODE);

                        HttpUtils.doPost(UrlConstans.FADAN_URL, hashMap, new Callback() {

                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.setToast(mContext, "请求失败， 稍后再试~");
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String json = response.body().string();
                                Utils.setErrorLog(TAG, json);
                                if(json.contains("DOCTYPE")){
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {

                                            JSONObject jsonObject = new JSONObject(json);
                                            String msg = jsonObject.getString("msg");

                                            if(jsonObject.getInt("status") == 200){
//                                                Utils.setToast(mContext, msg);
//                                                popupWindow.dismiss();
                                                TipDialog1.Builder builder = new TipDialog1.Builder(mContext);
                                                builder.setTitle("温馨提醒")
                                                        .setPositiveButton("好的", new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                                dialog.dismiss();
                                                                //退出当前账户
//                                                                finish();
                                                            }
                                                        });
                                                builder.create().show();
                                            }else{
                                                Utils.setToast(mContext, msg);
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

        popupWindow.showAtLocation(findViewById(R.id.resdfa), Gravity.BOTTOM, 0, 0);
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
