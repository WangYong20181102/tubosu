package com.tbs.tobosupicture.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareWeixinActivity extends BaseActivity {

    @BindView(R.id.share_weixin_back)
    LinearLayout shareWeixinBack;
    @BindView(R.id.share_send_friend)
    LinearLayout shareSendFriend;

    private Context mContext;
    private String TAG = "ShareWeixinActivity";
    //分享
    private UMShareAPI mShareAPI;
    //分享用到的图片
    private UMImage umImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_weixin);
        ButterKnife.bind(this);
        mContext = this;
        initViewEvent();
    }

    private void initViewEvent() {
        mShareAPI = UMShareAPI.get(mContext);
        umImage = new UMImage(mContext, R.mipmap.weixin2);
    }

    @OnClick({R.id.share_weixin_back, R.id.share_send_friend})
    public void onViewClickedInShareWeixin(View view) {
        switch (view.getId()) {
            case R.id.share_weixin_back:
                finish();
                break;
            case R.id.share_send_friend:
                //调用分享
                new ShareAction(ShareWeixinActivity.this).withText("邀您关注土拨鼠微信")
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                        .withMedia(umImage)
                        .open();
                break;
        }
    }
}
