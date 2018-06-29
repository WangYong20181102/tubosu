package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.Constant;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;

/**
 * Created by dec on 2016/10/21.
 */

public class ImageActivity extends BaseActivity{
    private static final String TAG = ImageActivity.class.getSimpleName();
    private Context mContext;

    private RelativeLayout rel_image_share;

    private RelativeLayout rel_setting_back;
    private ViewPager image_viewpager;

    private ArrayList<String> urlDataList = new ArrayList<String>();

    private TextView tv_position;

    private TextView tv_total_image_num;

    private String article_id = "";
    private String detailTitle = "";
    private String detailTitlePicture = "";

    // 友盟分享的服务
    private com.umeng.socialize.controller.UMSocialService controller = null;
    private String socialize_share_description = "";
    private String socialize_shareUrl = "";

//    private android.os.Handler handler = new android.os.Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 125:
//                    tv_position.setText(msg.arg1+"");
//                    break;
//            }
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        mContext = ImageActivity.this;

        controller = UMServiceFactory.getUMSocialService("com.umeng.share");

        initView();
        initData();

    }

    private void initView() {
        rel_setting_back = (RelativeLayout) findViewById(R.id.rel_setting_back);
        rel_image_share = (RelativeLayout) findViewById(R.id.rel_image_share);
        image_viewpager = (ViewPager) findViewById(R.id.image_viewpager);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_total_image_num = (TextView) findViewById(R.id.tv_total_image_num);

        rel_setting_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rel_image_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //  分享
                controller.openShare(ImageActivity.this, false);
            }
        });
    }

    private void initData(){
        Bundle b = getIntent().getBundleExtra("See_Image_Bundle");
        socialize_share_description = b.getString("socialize_share_description");
        socialize_shareUrl = b.getString("socialize_shareUrl");
        urlDataList = b.getStringArrayList("image_url_list");
        tv_total_image_num.setText(""+(urlDataList.size()));
        tv_position.setText("1");
        article_id = b.getString("article_id");
        detailTitle = b.getString("detailTitle");
        detailTitlePicture = b.getString("detailTitlePicture");
//        MyImageViewPager adapter = new MyImageViewPager(getSupportFragmentManager());
        ViewPagerAdapter adapter = new ViewPagerAdapter();
        image_viewpager.setAdapter(adapter);
        image_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_position.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setUmengShareSetting();
    }



    class ViewPagerAdapter extends PagerAdapter{
        public ViewPagerAdapter() {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mContext);
            Picasso.with(mContext)
                    .load(urlDataList.get(position))
                    .fit()
                    .placeholder(R.mipmap.occupied1)
                    .into(view);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            if (urlDataList != null) {
                return urlDataList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }



//    class MyImageViewPager extends FragmentPagerAdapter{
//
//        public MyImageViewPager(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Message msg = new Message();
//            msg.what = 125;
//            msg.arg1 = position;
//            handler.sendMessage(msg);
////            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+urlDataList.get(position)+"<<<<<<<<<<<");
//            return ImageFragment.newInstance(position, urlDataList.get(position));
//        }
//
//        @Override
//        public int getCount() {
//            return urlDataList.size();
//        }
//    }

    private void setUmengShareSetting(){
        setShareConfig();
        setSocialShareContent(detailTitlePicture);
    }



    private void setShareConfig(){
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        weixinConfig();
        qqConfig();
        sinaConfig();
    }

    /**
     * 根据不同的平台设置不同的分享内容</br>
     */
    private void setSocialShareContent(String pictureUrl) {

        // 图片分享
        UMImage urlImage = new UMImage(mContext, pictureUrl);

        // 分享到微信好友的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(socialize_share_description); // 描述
        weixinContent.setTitle(detailTitle); // 标题
        weixinContent.setTargetUrl(socialize_shareUrl); // 分享的url
        weixinContent.setShareImage(urlImage); // 分享时呈现图片
        controller.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(socialize_share_description);
        circleMedia.setTitle(detailTitle);
        circleMedia.setShareImage(urlImage);
        circleMedia.setTargetUrl(socialize_shareUrl);
        controller.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(socialize_share_description);
        qzone.setTargetUrl(socialize_shareUrl);
        qzone.setTitle(detailTitle);
        qzone.setShareImage(urlImage);
        controller.setShareMedia(qzone);

        // 设置QQ 分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(socialize_share_description);
        qqShareContent.setTitle(detailTitle);
        qqShareContent.setShareImage(urlImage);
        qqShareContent.setTargetUrl(socialize_shareUrl);
        controller.setShareMedia(qqShareContent);


//         新浪微博分享的内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(socialize_share_description);
        sinaContent.setShareImage(urlImage);
        controller.setShareMedia(sinaContent);

    }

    private void weixinConfig(){
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void qqConfig(){
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((AppCompatActivity)mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qqSsoHandler.addToSocialSDK();

//         添加QQ空间分享平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((AppCompatActivity)mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void sinaConfig(){

        controller.getConfig().setSsoHandler(new SinaSsoHandler(this));

    }


    private void do_share(SHARE_MEDIA platform){
        controller.postShare(mContext, platform, new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                MobclickAgent.onEvent(mContext, "click_find_decoration_array_favorite(share/share succeed)");
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
