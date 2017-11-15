package com.tobosu.mydecorate.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.RelatedAdapter;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.DensityUtil;
import com.tobosu.mydecorate.util.HtmlUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.tobosu.mydecorate.view.ItemDivider;
import com.tobosu.mydecorate.view.MyScrollView;
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
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by dec on 2016/10/12.
 */

public class ArticleDetailActivity extends AppCompatActivity {
    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private Context mContext;
    private boolean isGetData = true;

    private String article_id = "";
    private String type_id = "";
    private CustomWaitDialog waitDialog;

    private String writer_id = "";

    private static int is_coll = -1;

    private String socialize_share_description = "";

    private String socialize_shareUrl = "";

    /**
     * 是否已点赞 <br/>
     * 0 未点赞 <br/>
     * 1 已点赞
     */
    private static int is_up = 0;

    private String concernUserId = "";

    private int concerned = -1;

    /**** 文章详情接口 */
    private String detail_url = Constant.ZHJ + "tapp/mt/getArticle";

    /**** 关注接口 */
    private String concern_url = Constant.ZHJ + "tapp/mt/attentionUser";

    /**** 取消关注接口 */
    private String cancel_concern_url = Constant.ZHJ + "tapp/mt/cancelAttention";

    /**
     * 点赞接口
     */
    private String praise_url = Constant.ZHJ + "tapp/mt/praiseArticle";

    /**
     * 收藏接口
     */
    private String collect_url = Constant.ZHJ + "tapp/mt/collectionArticle";


    private ImageView iv_detail_top_image;

    private TextView tv_detail_top_title;

    private LinearLayout layout_html_content;

    private String rawHtmlContentData = "";

    private RelativeLayout rel_share_weixin;
    private RelativeLayout rel_share_weixin_circle;
    private RelativeLayout rel_share_weibo;

    private RecyclerView my_recycle_article;
    private ArrayList<HashMap<String, String>> relatedDataList = new ArrayList<HashMap<String, String>>();

    private ImageView iv_related_user_pic;
    private TextView tv_related_user;
    private TextView tv_related_user_weixin;
    private TextView tv_copy_weixin;
    private TextView tv_concern_related_user;
    private RelativeLayout recommend_concern_user;


    private RelativeLayout rel_artcle_detail_back;

    private ImageView iv_article_share;

    /**
     * 收藏
     */
    private ImageView iv_article_collect;
    /**
     * 点赞按钮
     */
    private ImageView iv_article_good;

    private String nickName = "";
    private MyScrollView article_container;

    private ImageView iv_get_order;

    private RelativeLayout include_netout_layout;

    //新增的控件
    private ImageView detailShare;
    private TextView detailNeedZx;

    private android.os.Handler uiHandler = new android.os.Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    // 关注成功
                    tv_concern_related_user.setText("已关注");
                    tv_concern_related_user.setTextColor(getResources().getColor(R.color.concern_color));
                    tv_concern_related_user.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
//                    concerned=1;
                    break;
                case 2:
                    iv_article_good.setBackgroundResource(R.mipmap.good_after);
                    break;
                case 3:
                    iv_article_collect.setBackgroundResource(R.mipmap.like_after);
                    break;
                case 6:
                    // 取消关注
                    tv_concern_related_user.setText("关注");
                    tv_concern_related_user.setTextColor(getResources().getColor(R.color.concern_color_normal));
                    tv_concern_related_user.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
//                    concerned=0;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        mContext = ArticleDetailActivity.this;

        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        initViews();
        getIntentData();
        initData();
    }

    private void showLoadingView() {
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

    private void setUmengShareSetting() {
        setShareConfig();
        setSocialShareContent(detailTitlePicture);
    }

    private void getIntentData() {
        Bundle b = getIntent().getBundleExtra("Article_Detail_Bundle");
        article_id = b.getString("article_id");
        type_id = b.getString("type_id");
        detailTitlePicture = b.getString("article_title_pic_url");
        detailTitle = b.getString("article_title");
//        which_position = b.getInt("child_position");
        writer_id = b.getString("uid");
        article_container.setVisibility(View.GONE);
    }


//    private LoadingDataDialog dialog;
//
//    private void showLoadingView(){
//        dialog =new LoadingDataDialog(mContext);
//        dialog.show();
//    }
//
//    private void hideLoadingView(){
//        if(dialog!=null&&dialog.isShowing()){
//            dialog.dismiss();
//        }
//    }

    private void initViews() {
        detailShare = (ImageView) findViewById(R.id.detail_share);
        detailShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.openShare(ArticleDetailActivity.this, false);
            }
        });
        detailNeedZx = (TextView) findViewById(R.id.new_detail_need_zx);
        detailNeedZx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "click_free_design");
//                发单入口所带url
//                1)安卓：http://m.tobosu.com/app/pub?channel=seo&subchannel=zhjandroid
//                入口名称：装好家APP android文章详情发单
//                入口代码：943
                startActivity(new Intent(mContext, FreeActivity.class));
            }
        });
        iv_detail_top_image = (ImageView) findViewById(R.id.iv_detail_top_image);
        tv_detail_top_title = (TextView) findViewById(R.id.new_detail_title);
        layout_html_content = (LinearLayout) findViewById(R.id.new_layout_html_content);

        include_netout_layout = (RelativeLayout) findViewById(R.id.include_netout_layout);

        article_container = (MyScrollView) findViewById(R.id.article_scrollview);
//        ar_include_netout_layout = (RelativeLayout) findViewById(R.id.ar_include_netout_layout);
        rel_share_weixin = (RelativeLayout) findViewById(R.id.rel_share_weixin);
        rel_share_weixin_circle = (RelativeLayout) findViewById(R.id.rel_share_weixin_circle);
        rel_share_weibo = (RelativeLayout) findViewById(R.id.rel_share_weibo);
        rel_share_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分享到微信朋友
                do_share(SHARE_MEDIA.WEIXIN);
//
            }
        });
        rel_share_weixin_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 分享到微信朋友圈
                do_share(SHARE_MEDIA.WEIXIN_CIRCLE);
            }
        });
        rel_share_weibo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                do_share(SHARE_MEDIA.SINA);
            }
        });

        my_recycle_article = (RecyclerView) findViewById(R.id.my_recycle_article);

        recommend_concern_user = (RelativeLayout) findViewById(R.id.recommend_concern_user);
        // 不需要跳转
//        recommend_concern_user.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent concernUserIntent = new Intent(mContext, WriterActivity.class);
//                Bundle b = new Bundle();
//                b.putString("aid", concernUserId); // 被关注的id
//                b.putString("is_att", concerned+"");
//                b.putString("nick", nickName);
//                b.putString("header_pic_url", relatedDataHeadPicUrl);
//
//                if(Util.isLogin(mContext)){
//                    // 登录
//                    b.putString("uid", Util.getUserId(mContext)); // 自己的id
//                }else {
//                    b.putString("uid", "0");
//                }
//                concernUserIntent.putExtra("Writer_User_Bundle", b);
//                startActivity(concernUserIntent);
//            }
//        });

        iv_get_order = (ImageView) findViewById(R.id.iv_get_order);
        iv_get_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "click_free_design");
//                发单入口所带url
//                1)安卓：http://m.tobosu.com/app/pub?channel=seo&subchannel=zhjandroid
//                入口名称：装好家APP android文章详情发单
//                入口代码：943
                startActivity(new Intent(mContext, FreeActivity.class));

            }
        });

        iv_related_user_pic = (ImageView) findViewById(R.id.iv_related_user_pic);
        tv_related_user = (TextView) findViewById(R.id.tv_related_user);
        tv_related_user_weixin = (TextView) findViewById(R.id.tv_related_user_weixin);
        tv_copy_weixin = (TextView) findViewById(R.id.tv_copy_weixin);
        tv_copy_weixin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MobclickAgent.onEvent(mContext, "click_article_copy_weixinhao");
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv_related_user_weixin.getText());
                Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });

        tv_concern_related_user = (TextView) findViewById(R.id.tv_concern_related_user);
        tv_concern_related_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.isLogin(mContext)) {
                    if (Util.isNetAvailable(mContext)) {
                        if (concerned == 0) {
                            do_concern();
                        } else if (concerned == 1) {
                            MobclickAgent.onEvent(mContext, "click_focous_focoused_cut");
                            do_cancelConcern();
                        }
                    } else {
                        hideLoadingView();
                        Toast.makeText(mContext, "网络微弱,稍后再试~", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // 游客模式
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

        rel_artcle_detail_back = (RelativeLayout) findViewById(R.id.rel_artcle_detail_back);
        rel_artcle_detail_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent data = new Intent();
//                Bundle b = new Bundle();
//                b.putInt("goback_which_position", which_position);
//                data.putExtra("Detail_Goback_Bundle", b);
//                setResult(Constant.DETAIL_FINISH_RESULTCODE, data);
                finish();
            }
        });

        iv_article_share = (ImageView) findViewById(R.id.iv_article_share);
        iv_article_collect = (ImageView) findViewById(R.id.iv_article_collect);
        iv_article_good = (ImageView) findViewById(R.id.new_detail_good);

        iv_article_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
                controller.openShare(ArticleDetailActivity.this, false);
            }
        });

        iv_article_good.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // 点赞不需要登录
                if (is_up == 0 && Util.isNetAvailable(mContext)) {
                    do_praise_article();
                } else if (!Util.isNetAvailable(mContext)) {
                    hideLoadingView();
                    Toast.makeText(mContext, "请检查网络 稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_article_collect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Util.isLogin(mContext)) {
                    if (Util.isNetAvailable(mContext) && is_coll == 0) {

                        do_collectActicle();
                    } else if (!Util.isNetAvailable(mContext)) {
                        hideLoadingView();
                        Toast.makeText(mContext, "请检查网络 稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 游客模式
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

    }


    //    private int which_position = -1;
    private void initData() {
        if (Util.isNetAvailable(mContext)) {

            Util.showNetOutView(mContext, include_netout_layout, true);
//            showLoadingView();

//            if(isGetData){
            showLoadingView();
            System.out.println("重新获取数据啊");
            getDataFromNet();
//                isGetData = false;
//            }


        } else {
            hideLoadingView();
            Util.showNetOutView(mContext, include_netout_layout, false);
        }

    }


    private void getDataFromNet() {
        if (Util.isNetAvailable(mContext)) {
            Util.showNetOutView(mContext, include_netout_layout, true);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("id", article_id);
            hashMap.put("type_id", type_id);
            if (Util.isLogin(mContext)) {
                hashMap.put("uid", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", ""));
            } else {
                hashMap.put("uid", "0");
            }
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            okHttpUtil.post(detail_url, hashMap, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String json) {
                    System.out.println("详情页 请求结果" + json);
                    article_container.setVisibility(View.VISIBLE);
                    do_parseJson(json);
                }

                @Override
                public void onFail(Request request, IOException e) {

                }

                @Override
                public void onError(Response response, int code) {

                }
            });

        } else {
            hideLoadingView();
            Util.showNetOutView(mContext, include_netout_layout, false);
        }

    }

    private String detailTitlePicture = "";
    private String detailTitle = "";

    /**
     * 已经分离了图片和文字 --->> 设置文字和图片
     */
    private void setPictureAndText(ArrayList<String> _pureDataList) {

        /*-----------------设置页头-----------------*/
        Picasso.with(mContext)
                .load(detailTitlePicture)
                .fit()
                .placeholder(R.mipmap.occupied1)
//                .error(R.mipmap.icon_head_default)
                .into(iv_detail_top_image);

        tv_detail_top_title.setText(detailTitle);
        /*-----------------设置页头-----------------*/


        int size = _pureDataList.size();
        ArrayList<String> imageUrlList = new ArrayList<String>();
        for (int n = 0; n < size; n++) {
            if (_pureDataList.get(n).contains("http")) {
                imageUrlList.add(_pureDataList.get(n));
            }
        }


        for (int i = 0; i < size; i++) {

            if (_pureDataList.get(i).contains("http")) {
                ImageView iv = new ImageView(mContext);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20));
                iv.setLayoutParams(lp);
                iv.setAdjustViewBounds(true);
                iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Picasso.with(mContext)
                        .load(_pureDataList.get(i))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .into(iv);
                layout_html_content.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
                layout_html_content.addView(iv);
                seePicture(iv, imageUrlList);

            } else {
                TextView tv = new TextView(mContext);
                LinearLayout.LayoutParams textlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textlp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 190), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 60));
                tv.setLayoutParams(textlp);
                tv.setText(_pureDataList.get(i));
//                    tv.setTextColor(Color.parseColor("#080ELA"));
                tv.setTextSize(16);
                layout_html_content.addView(tv);
            }
        }
    }


    private void seePicture(ImageView imageView, final ArrayList<String> urlList) {
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("image_url_list", urlList);
                b.putString("article_id", article_id);
                b.putString("detailTitle", detailTitle);
                b.putString("detailTitlePicture", detailTitlePicture);
                b.putString("socialize_shareUrl", socialize_shareUrl);
                b.putString("socialize_share_description", socialize_share_description);
                it.putExtra("See_Image_Bundle", b);
                startActivity(it);
            }
        });
    }

    private String _aid_ = "**";
    private String _type_id_ = "-*-*";

    /**
     * 获取文章详情页面所有数据的解析方法
     */
    private void do_parseJson(String json) {
        System.out.println("--电站---json" + json);
        try {
            JSONObject detailObj = new JSONObject(json);
            if (detailObj.getInt("error_code") == 0) {
                JSONObject dataObj = detailObj.getJSONObject("data");

                // 对象一  文章详情对象
                JSONObject artcleDataObj = dataObj.getJSONObject("artcleData");
//                HashMap<String, String> dataHashMap = new HashMap<String, String>(); // TODO 1
//                dataHashMap.put("aid",artcleDataObj.getString("aid"));
                _aid_ = artcleDataObj.getString("aid");
//                dataHashMap.put("type_id",artcleDataObj.getString("type_id"));
//                _type_id_ = artcleDataObj.getString("type_id");
//                dataHashMap.put("view_pcount",artcleDataObj.getString("view_pcount"));
//                dataHashMap.put("tup_count",artcleDataObj.getString("tup_count"));
//                dataHashMap.put("collect_count",artcleDataObj.getString("collect_count"));
//                dataHashMap.put("view_mcount",artcleDataObj.getString("view_mcount"));
//                dataHashMap.put("title",artcleDataObj.getString("title"));
                detailTitle = artcleDataObj.getString("title");
//                dataHashMap.put("image_url",artcleDataObj.getString("image_url"));
                detailTitlePicture = artcleDataObj.getString("image_url");

                is_coll = artcleDataObj.getInt("is_coll");
                if (artcleDataObj.getInt("is_coll") == 0) {
                    iv_article_collect.setBackgroundResource(R.mipmap.like_before);
                } else {
                    iv_article_collect.setBackgroundResource(R.mipmap.like_after);
                }


                is_up = artcleDataObj.getInt("is_up");
//                System.out.println(artcleDataObj.getString("is_up")+"电站"+is_up);
                if (artcleDataObj.getInt("is_up") == 0) {
                    iv_article_good.setBackgroundResource(R.mipmap.good_before);
                } else {
                    iv_article_good.setBackgroundResource(R.mipmap.good_after);
                }


                socialize_shareUrl = Constant._SHARE_URL + artcleDataObj.getString("system_code") + "/" + article_id + ".html" + Constant.SOCAIL_SHARE;
//                dataHashMap.put("uid",artcleDataObj.getString("uid"));
//                dataHashMap.put("view_count",artcleDataObj.getString("view_count"));
//                dataHashMap.put("time_show",artcleDataObj.getString("time_show"));
//                dataHashMap.put("time_create",artcleDataObj.getString("time_create"));
//                dataHashMap.put("tag_id_list",artcleDataObj.getString("tag_id_list"));
//                dataHashMap.put("content",artcleDataObj.getString("content"));
//                dataHashMap.put("description",artcleDataObj.getString("description"));
                socialize_share_description = artcleDataObj.getString("description");
//                dataHashMap.put("state",artcleDataObj.getString("state"));

                rawHtmlContentData = artcleDataObj.getString("content");
                //设置内容
                setPictureAndText(handle_htmlContent(rawHtmlContentData));

                /*---------------------------  对象分割线  ----------------------------------*/
                // 对象二  该文章作者的信息对象
                JSONObject userDataObj = dataObj.getJSONObject("userData");
                HashMap<String, String> userHashMap = new HashMap<String, String>(); // TODO 2
//                userHashMap.put("uid",userDataObj.getString("uid"));
                concernUserId = userDataObj.getString("uid");
                userHashMap.put("nick", userDataObj.getString("nick"));
                nickName = userDataObj.getString("nick");
                tv_related_user.setText(userDataObj.getString("nick"));
                userHashMap.put("mobile", userDataObj.getString("mobile"));
                userHashMap.put("user_type", userDataObj.getString("user_type"));
                userHashMap.put("industry_type", userDataObj.getString("industry_type"));
                userHashMap.put("province_id", userDataObj.getString("province_id"));
                userHashMap.put("is_att", userDataObj.getInt("is_att") + "");
                concerned = userDataObj.getInt("is_att");

                if (userDataObj.getInt("is_att") == 0) {
                    tv_concern_related_user.setText("关注");
                    tv_concern_related_user.setTextColor(getResources().getColor(R.color.concern_color_normal));
                    tv_concern_related_user.setBackgroundResource(R.drawable.shape_bg_button_article_concern);
                } else if (userDataObj.getInt("is_att") == 1) {
                    tv_concern_related_user.setText("已关注");
                    tv_concern_related_user.setTextColor(getResources().getColor(R.color.concern_color));
                    tv_concern_related_user.setBackgroundResource(R.drawable.shape_bg_button_article_concerned);
                }

                userHashMap.put("city_id", userDataObj.getString("city_id"));
                userHashMap.put("sort_description", userDataObj.getString("sort_description"));
                userHashMap.put("info", userDataObj.getString("info"));
//                userHashMap.put("header_pic_url",userDataObj.getString("header_pic_url"));
                relatedDataHeadPicUrl = userDataObj.getString("header_pic_url");
//                userHashMap.put("weixin_pic_url",userDataObj.getString("weixin_pic_url"));
//                userHashMap.put("weixin",userDataObj.getString("weixin"));
                tv_related_user_weixin.setText(userDataObj.getString("weixin"));
                userHashMap.put("time_create", userDataObj.getString("time_create"));
                userHashMap.put("time_check", userDataObj.getString("time_check"));
                userHashMap.put("state", userDataObj.getString("state"));
                userHashMap.put("remark_front", userDataObj.getString("remark_front"));
                userHashMap.put("remark_erp", userDataObj.getString("remark_erp"));
                userHashMap.put("count_article", userDataObj.getString("count_article"));
                userHashMap.put("count_view", userDataObj.getString("count_view"));


                /*---------------------------  对象分割线  ----------------------------------*/
                // 对象三 更多内容相关的对象
                JSONArray relatedDataArray = dataObj.getJSONArray("relatedData");
                int relatedLen = relatedDataArray.length();


                for (int k = 0; k < relatedLen; k++) {
                    HashMap<String, String> relateHashMap = new HashMap<String, String>();
                    relateHashMap.put("aid", relatedDataArray.getJSONObject(k).getString("aid"));
                    relateHashMap.put("uid", relatedDataArray.getJSONObject(k).getString("uid"));
                    relateHashMap.put("title", relatedDataArray.getJSONObject(k).getString("title"));
                    relateHashMap.put("type_id", relatedDataArray.getJSONObject(k).getString("type_id"));
                    relateHashMap.put("tup_count", relatedDataArray.getJSONObject(k).getString("tup_count"));
                    relateHashMap.put("collect_count", relatedDataArray.getJSONObject(k).getString("collect_count"));
                    relateHashMap.put("show_count", relatedDataArray.getJSONObject(k).getString("show_count"));
                    relateHashMap.put("image_url", relatedDataArray.getJSONObject(k).getString("image_url"));
                    JSONObject timeObj = relatedDataArray.getJSONObject(k).getJSONObject("time_rec");
                    relateHashMap.put("_time", timeObj.getInt("time") + "");
                    relateHashMap.put("_unit", timeObj.getString("time_unit"));
                    relateHashMap.put("type_name", relatedDataArray.getJSONObject(k).getString("type_name"));
                    relateHashMap.put("nick", relatedDataArray.getJSONObject(k).getString("nick"));
                    relateHashMap.put("header_pic_url", relatedDataArray.getJSONObject(k).getString("header_pic_url"));

                    relatedDataList.add(relateHashMap);

                }

                hideLoadingView();
                initRelatedData();

                setUmengShareSetting();
            } else {
                Toast.makeText(mContext, "没有此文章", Toast.LENGTH_SHORT).show();
            }

            hideLoadingView();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 分割和抽取文版和图片地址
     *
     * @param raw_html_content
     */
    private ArrayList<String> handle_htmlContent(String raw_html_content) {
//        System.out.println("---处理原始->>  " + raw_html_content + "<<----原始结束啦--");
        // 先标记图片
        String temTextHtml = raw_html_content.replaceAll(".*?<body.*?>(.*?)<\\/body>", "$1");

        String html = temTextHtml.replaceAll("<img", "[").replace("/>", "]").replaceAll("&nbsp;", ""); // 将图片标签包围起来

        ArrayList<String> stringData = new ArrayList<String>(); // 存放分割后的字符串

        if (html.contains("</div>")) {
            // 有div标签
//            ArrayList<String> divList = new ArrayList<String>();
            String htmlArray[] = html.split("</div>");
            for (int i = 0, len = htmlArray.length; i < len; i++) {
                String tempElement = new StringBuffer(htmlArray[i]).append("</div>").toString(); // 补回</div>
//                System.out.println("===</div>标签处理-->> " + tempElement + "  <<--</div>--");
//                divList.add(tempElement); // divList是div标签的集合

                if (tempElement.contains("</p>")) {
                    String p_StringArray[] = tempElement.split("</p>");
                    for (int j = 0, arrayLen = p_StringArray.length; j < arrayLen; j++) {
                        String p_element = new StringBuffer(p_StringArray[j]).append("</p>").toString();
                        stringData.add(p_element);
                    }
                } else {
                    // 没有p标签
                    stringData.add(tempElement);
                }

            }
        } else {
            // 没有div标签时  只有p标签
            String htmlPArray[] = html.split("</p>");
            for (int k = 0, pLen = htmlPArray.length; k < pLen; k++) {
                String element = new StringBuffer(htmlPArray[k]).append("</p>").toString();
                stringData.add(element);
            }
        }

//        for(int s=0; s<stringData.size(); s++){
//            System.out.println("---处理wenben-->> " + stringData.get(s) + "<<<--wenben--");
//        }


        ArrayList<String> pureStringList = new ArrayList<String>();
        int size = stringData.size();
        // 分割了文本和图片之后  分别抽取纯文本 和 纯图片地址
        for (int m = 0; m < size; m++) {
            if (stringData.get(m).contains("http")) {
                // 有图片 抽取图片
                String _temp = "*";
                if (stringData.get(m).contains(".png")) {
                    _temp = stringData.get(m).substring(stringData.get(m).indexOf("http"), stringData.get(m).indexOf(".png") + 4);
                } else if (stringData.get(m).contains(".jpg")) {
                    _temp = stringData.get(m).substring(stringData.get(m).indexOf("http"), stringData.get(m).indexOf(".jpg") + 4);
                }
//                System.out.println("---抽取图片地址-->> " + _temp + "<<<--图片地址--");
                pureStringList.add(_temp);
            } else {
                // 去掉 所有标签
                String tem = HtmlUtil.delHTMLTag(stringData.get(m));
                if (!"".equals(tem)) {
                    pureStringList.add(tem);
//                    System.out.println("---抽取文版-->> " + tem + "<<<--抽取文版--");
                }
            }
        }
        return pureStringList;
    }

//    /**
//     * * 由于String.replaceAll方法时如果替换字符中有/或$可能会出现indexOutOfBoundsException
//     * * 固先对其他进行替换
//     * * @param string
//     * * @return
//     * */
//
//    public static String quoteReplacement(String string) {
//        if ((string.indexOf("//") == -1) && (string.indexOf("$") == -1))
//            return string;
//        StringBuffer sb = new StringBuffer();
//        for (int i=0; i<string.length(); i++) {
//            char c = string.charAt(i);
//            if (c == "//") {
//                sb.append("//");
//                sb.append('//');
//            } else if (c == "$") {
//                sb.append("//");
//                sb.append("$");
//            } else {
//                sb.append(c);
//            }
//        }
//        return sb.toString();
//    }



    private void do_praise_article() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("aid", article_id);
        hashMap.put("from_os", "1");
        okHttpUtil.post(praise_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String s) {
                System.out.println("wtb点赞后结果-->>" + s + "---文章id->>" + _aid_ + "---文章type_id->>" + _type_id_);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getInt("error_code") == 0) {
                        Message msg = new Message();
                        msg.what = 2;
                        uiHandler.sendMessage(msg);
                    } else if (obj.getInt("error_code") == 1) {
                        iv_article_good.setBackgroundResource(R.mipmap.good_after);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Toast.makeText(mContext, "点赞失败，请稍后重试~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response response, int code) {

            }
        });

    }


    private String relatedDataHeadPicUrl = "";

    /***
     * 初始化相关的数据
     */
    private void initRelatedData() {

        Picasso.with(mContext)
                .load(relatedDataHeadPicUrl)
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(iv_related_user_pic);


        // 更多相关
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        my_recycle_article.setHasFixedSize(true);
        //创建默认的线性LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        my_recycle_article.setLayoutManager(layoutManager);

        RelatedAdapter adapter = new RelatedAdapter(mContext, relatedDataList);
        my_recycle_article.setAdapter(adapter);
        my_recycle_article.addItemDecoration(new ItemDivider(mContext, ItemDivider.VERTICAL_LIST));

        adapter.setOnItemClickListener(new RelatedAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onRecyclerViewItemClick(View view, HashMap<String, String> data) {
                MobclickAgent.onEvent(mContext, "click_article_more");

                Intent it = new Intent(ArticleDetailActivity.this, ArticleDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("article_id", data.get("aid"));
                System.out.println("----beichakan aid" + data.get("aid"));
                b.putString("type_id", data.get("type_id"));
                System.out.println("----beichakan type_id" + data.get("type_id"));
                b.putString("article_title", data.get("title"));
                b.putString("article_title_pic_url", data.get("image_url"));
                b.putString("writer_id", data.get("uid")); // 被查看的用戶id
                System.out.println("----beichakan uid" + data.get("uid"));
                if (Util.isLogin(mContext)) {
                    b.putString("uid", Util.getUserId(mContext));
                } else {
                    b.putString("uid", "0"); // uid-->>0 是游客模式
                }
                it.putExtra("Article_Detail_Bundle", b);
                startActivity(it);
//                finish();
            }
        });

    }


    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }



    private void do_concern() {
        MobclickAgent.onEvent(mContext, "click_article_focous");

        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("aid", concernUserId);  // 被关注用户id
        hashMap.put("uid", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", "")); // 用户id
        okHttpUtil.post(concern_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getInt("error_code") == 0) {
                        Message msg = new Message();
                        msg.what = 1;
                        uiHandler.sendMessage(msg);
                    } else if (obj.getInt("error_code") == 201) {
                        System.out.println("------关注201-------------");
                    } else {
                        System.out.println("------关注4444------------");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }


    private void do_collectActicle() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("aid", article_id); // 文章id
        hashMap.put("uid", Util.getUserId(mContext)); //登录用户id
        okHttpUtil.post(collect_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String s) {
                System.out.println("---收藏结果--" + s);
                try {
                    JSONObject obj = new JSONObject(s);
                    if (obj.getInt("error_code") == 0) {
                        Message msg = new Message();
                        msg.what = 3;
                        uiHandler.sendMessage(msg);
                    } else if (obj.getInt("error_code") == 1) {
                        iv_article_collect.setBackgroundResource(R.mipmap.like_after);
                    } else if (obj.getInt("error_code") == 250) {
                        System.out.println("--打印结果：" + obj.getString("msg") + " ---");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void do_cancelConcern() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("uid", getSharedPreferences("User_Info_SP", Context.MODE_PRIVATE).getString("user_id", ""));
        hashMap.put("aid", concernUserId); // 被关注的userid
        okHttpUtil.post(cancel_concern_url, hashMap, new OKHttpUtil.BaseCallBack() {

            @Override
            public void onSuccess(com.squareup.okhttp.Response response, String json) {
//                System.out.println("---取消关注的userid-->>>"+concernUserId +"***"+json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if (obj.getInt("error_code") == 0) {
                        /**
                         * {
                         "error_code":0,
                         "msg":"取消成功！"
                         }
                         */
                        Message msg = new Message();
                        msg.what = 6;
                        uiHandler.sendMessage(msg);
                    } else if (obj.getInt("error_code") == 201) {
                        /**
                         * {
                         "error_code":0,
                         "msg":"取消成功！"
                         }
                         */
                        Toast.makeText(mContext, "取消关注失败,请稍后再试~", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("------取消关注333-------------");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(com.squareup.okhttp.Request request, IOException e) {
                Toast.makeText(mContext, "取消关注失败 稍后再试~", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(com.squareup.okhttp.Response response, int code) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        loading_layout_out.setVisibility(View.GONE);
//        initData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        Log.d("result", "onActivityResult");
    }


    // 友盟分享的服务
    private com.umeng.socialize.controller.UMSocialService controller = null;

    private void setShareConfig() {
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
//        UMImage localImage = new UMImage(mContext, "http://www.umeng.com/images/pic/social/integrated_3.png");
        // UMImage resImage = new UMImage(LoginActivity.this, R.drawable.icon);

//        // 视频分享
//        UMVideo video = new UMVideo("http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        // vedio.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        video.setTitle("友盟社会化组件视频");
//        video.setThumb(urlImage);
//        video.setThumb(new UMImage(this, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)));

//        // 音乐分享
//        UMusic uMusic = new UMusic("http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
//        uMusic.setAuthor("umeng");
//        uMusic.setTitle("天籁之音");
//        uMusic.setThumb(urlImage);
        // uMusic.setThumb("http://www.umeng.com/images/pic/social/chart_1.png");

        // 分享到微信好友的内容
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(socialize_share_description); // 描述
        weixinContent.setTitle(detailTitle); // 标题
        weixinContent.setTargetUrl(socialize_shareUrl); // 分享的url
        weixinContent.setShareImage(urlImage); // 分享时呈现图片
//        weixinContent.setShareMedia(urlImage);
        controller.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(socialize_share_description);
        circleMedia.setTitle(detailTitle);
        circleMedia.setShareImage(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(socialize_shareUrl);
        controller.setShareMedia(circleMedia);

//        UMImage qzoneImage = new UMImage(this,"http://www.umeng.com/images/pic/social/integrated_3.png");
//        qzoneImage.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

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

//        // 视频分享
//        UMVideo umVideo = new UMVideo("http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//        umVideo.setThumb("http://www.umeng.com/images/pic/home/social/img-1.png");
//        umVideo.setTitle("友盟社会化组件视频");
//
//        // 腾讯微博分享的内容
//        TencentWbShareContent tencent = new TencentWbShareContent();
//        tencent.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，腾讯微博");
//        // 设置tencent分享内容
//        controller.setShareMedia(tencent);

//         // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
//         MailShareContent mail = new MailShareContent(localImage);
//         mail.setTitle("share form umeng social sdk");
//         mail.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，email");
//         // 设置mail分享内容
//         controller.setShareMedia(mail);

//         // 设置短信分享内容
//         SmsShareContent sms = new SmsShareContent();
//         sms.setShareContent("来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能，短信");
//         sms.setShareImage(urlImage);
//         controller.setShareMedia(sms);

//         新浪微博分享的内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(socialize_share_description);
        sinaContent.setShareImage(urlImage);
        controller.setShareMedia(sinaContent);

    }

    private void weixinConfig() {
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(mContext, Constant.WEIXIN_APP_ID, Constant.WEIXIN_APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    private void qqConfig() {
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((AppCompatActivity) mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qqSsoHandler.addToSocialSDK();

//         添加QQ空间分享平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((AppCompatActivity) mContext, Constant.QQ_APP_ID, Constant.QQ_APP_SECRET);
        qZoneSsoHandler.addToSocialSDK();
    }

    private void sinaConfig() {

        controller.getConfig().setSsoHandler(new SinaSsoHandler(this));

    }


    private void do_share(SHARE_MEDIA platform) {
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 信鸽推送必须要调用这句
    }

}
