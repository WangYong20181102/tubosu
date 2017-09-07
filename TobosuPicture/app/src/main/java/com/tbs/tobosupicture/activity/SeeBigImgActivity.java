package com.tbs.tobosupicture.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
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
 * 案例详情看大图
 * Created by Lie on 2017/7/19.
 */

public class SeeBigImgActivity extends BaseActivity {
    @BindView(R.id.bar_caseFramelayout)
    FrameLayout bar_caseFramelayout;
    @BindView(R.id.relCaseSeeImgBack)
    RelativeLayout relCaseSeeImgBack;
    @BindView(R.id.vpCaseSeeImg)
    ViewPager vpCaseSeeImg;
    @BindView(R.id.tvCaseChangeNum)
    TextView tvCaseChangeNum;
    @BindView(R.id.tvCaseImgTotalNum)
    TextView tvCaseImgTotalNum;
    private ArrayList<String> imgList;
    private String stringData;
    private String imgurl;
    private String locationImg;
    private int locationIndext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = "SeeBigImgActivity";
        mContext = SeeBigImgActivity.this;
        setContentView(R.layout.activity_see_big_img);
        ButterKnife.bind(this);
        initImgData();
    }

    private void initImgData() {
        bar_caseFramelayout.bringToFront();
        Intent it = getIntent();
        if(it!=null){
            if(it.getStringExtra("imgStringData")!=null && it.getStringExtra("imgurl")!=null){
                stringData = it.getStringExtra("imgStringData");
                imgurl = it.getStringExtra("imgurl");
                imgurl = imgurl.replace("\\/\\/", "//").replace("\\/", "/");
                imgurl = imgurl.replace("/small", "");
                imgList = new ArrayList<String>();
                String arrString = stringData.toString();
                arrString = arrString.replace(",", "#").replace("[", "").replace("]", "").replaceAll("\"", "").replace("\\/\\/", "//").replace("\\/", "/");
                String[] tempArr = arrString.split("#");
                for(int i=0;i<tempArr.length;i++){
                    String temp = tempArr[i];
                    if(!"".equals(temp)){
                        imgList.add(temp);
//                        Utils.setErrorLog(TAG, "分割出来的所有图片路径是：" + temp);
                        if(imgurl.equals(temp)){
                            locationImg = imgurl;
                            locationIndext = i;
                        }
                    }
                }
                intiViewPager(imgList);
            }

        }
    }

    private void intiViewPager(ArrayList<String> imgDataList) {
        tvCaseImgTotalNum.setText(imgDataList.size() + "");
        ArrayList<TouchImageView> dataList = new ArrayList<TouchImageView>();
        for (int i = 0; i < imgDataList.size(); i++) {
            TouchImageView iv = new TouchImageView(mContext);
            GlideUtils.glideLoader(mContext, imgDataList.get(i), R.mipmap.loading_img_fail, R.mipmap.loading_img, iv);
            dataList.add(iv);
        }

        ImgViewpagerAdapter adapter = new ImgViewpagerAdapter(dataList);
        vpCaseSeeImg.setAdapter(adapter);
        vpCaseSeeImg.setCurrentItem(locationIndext);
        tvCaseChangeNum.setText((locationIndext + 1) + "");

        vpCaseSeeImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvCaseChangeNum.setText((position + 1) + "");
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

    @OnClick({R.id.relCaseSeeImgBack})
    public void onViewClickedSeeBigImgActivity(View view) {
        switch (view.getId()) {
            case R.id.relCaseSeeImgBack:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }
}
