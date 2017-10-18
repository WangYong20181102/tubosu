package com.tbs.tobosupicture.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
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
import com.tbs.tobosupicture.view.wheelviews.DownWindow;
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
        ArrayList<String> dataUrlList = new ArrayList<String>();
        for (int i = 0; i < imgDataList.size(); i++) {
            TouchImageView iv = new TouchImageView(mContext);
            String urlPic = imgDataList.get(i);
            GlideUtils.glideLoader(mContext, urlPic, R.mipmap.loading_img_fail, R.mipmap.loading_img, iv);
            dataList.add(iv);
            dataUrlList.add(urlPic);
        }

        ImgViewpagerAdapter adapter = new ImgViewpagerAdapter(dataList, dataUrlList);
        vpCaseSeeImg.setAdapter(adapter);
        vpCaseSeeImg.setCurrentItem(locationIndext);
        tvCaseChangeNum.setText((locationIndext + 1) + "");

        vpCaseSeeImg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                tvCaseChangeNum.setText((position + 1) + "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private class ImgViewpagerAdapter extends PagerAdapter {
        private ArrayList<TouchImageView> imgList;
        private ArrayList<String> imgListString;

        public ImgViewpagerAdapter(ArrayList<TouchImageView> imgList,ArrayList<String> imgListString) {
            this.imgList = imgList;
            this.imgListString = imgListString;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ((ViewPager) container).addView(imgList.get(position), 0);
            imgList.get(position).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    Utils.setToast(mContext, imgListString.get(position));
                    showDownload(imgListString.get(position));
                    return true;
                }
            });
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

    private void showDownload(final String url) {
        final DownWindow down = new DownWindow(SeeBigImgActivity.this);
        down.showAtLocation(SeeBigImgActivity.this.findViewById(R.id.seebigimgact), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        down.setOnItemClickListener(new DownWindow.OnItemClickListener() {
            @Override
            public void setOnItemClick(View v) {
                switch (v.getId()){
                    case R.id.id_btn_select:
                        httpDownLoadImg(down, url);
                        break;
                    case R.id.id_btn_cancelo:
                        down.dismiss();
                        break;
                }
            }
        });
    }

    private void httpDownLoadImg(DownWindow down, String downloadUrl) {
        //创建文件夹
        File dirFile = new File(UrlConstans.IMG_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        if (Utils.isNetAvailable(mContext)) {
            HttpUtils.downFile(downloadUrl, dirFile.getPath(), fileName);
            Toast.makeText(mContext, "图片下载成功!", Toast.LENGTH_SHORT).show();
            down.dismiss();
        } else {
            Toast.makeText(mContext, "网络不佳，图片下载失败！", Toast.LENGTH_SHORT).show();
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
