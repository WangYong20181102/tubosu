package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.tobosu.mydecorate.entity._ArticleDetail;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.DensityUtil;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.HtmlUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NewArticleDetailActivity extends AppCompatActivity {
    private Context mContext;
    private String TAG = "NewArticleDetailActivi";
    private Intent mIntent;//跳转及传递媒介
    private String id;//文章id 上个界面传来的 请求数据时要用到
    private String author_id;//作者id 上个界面传来的  请求数据时要用到
    private _ArticleDetail detail;//详情数据
    private CustomWaitDialog customDialog;//正在加载图层
    private com.umeng.socialize.controller.UMSocialService controller = null; // 友盟分享的服务
    private String articleContent;

    private RelativeLayout newDetailBack;//返回键
    private ImageView newDetailShare;//分享按钮
    private TextView newDetailNeedZx;//发单按钮
    private ImageView newDetailCollect;//收藏按钮
    private ImageView newDetailGood;//点赞按钮
    private TextView newDetailGoodNum;//点赞数量
    private TextView newDetailGoodNumAddOne;//点赞数量加一
    private TextView newDetailTitle;//详情的标题
    private ImageView newDatailIcon;//文章作者的头像
    private TextView newDetailName;//文章作者的名称
    private TextView newDetailTime;//文章创建时间
    private TextView newDetailAttention;//关注作者的按钮

    private LinearLayout newLayoutHtmlContent;//显示网络信息

    private boolean oneTime = true;

    private ArrayList<String> zzzDataArray = new ArrayList<String>();
    private JSONArray detailJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article_detail);
        mContext = this;
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        mIntent = getIntent();
        initBaseData();
        bindView();
        HttpGetArticleDetail();
        initViewEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        oneTime = true;
//        HttpGetArticleDetail();
//        initViewEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        oneTime = true;
//        HttpGetArticleDetail();
//        initViewEvent();
    }

    //初始化基本数据  主要是上个界面传来的
    private void initBaseData() {
        customDialog = new CustomWaitDialog(mContext);
        id = mIntent.getStringExtra("id");//文章id
        author_id = mIntent.getStringExtra("author_id");//作者ID
        HttpPostViewCount();
    }

    private void bindView() {
        newDetailBack = (RelativeLayout) findViewById(R.id.new_detail_back);
        newDetailShare = (ImageView) findViewById(R.id.new_detail_share);
        newDetailNeedZx = (TextView) findViewById(R.id.new_detail_need_zx);
        newDetailCollect = (ImageView) findViewById(R.id.new_detail_collect);
        newDetailGood = (ImageView) findViewById(R.id.new_detail_good);
        newDetailGoodNum = (TextView) findViewById(R.id.new_detail_good_num);
        newDetailGoodNumAddOne = (TextView) findViewById(R.id.new_detail_num_add_one);
        newDetailTitle = (TextView) findViewById(R.id.new_detail_title);
        newDatailIcon = (ImageView) findViewById(R.id.new_detail_icon);
        newDetailName = (TextView) findViewById(R.id.new_detail_name);
        newDetailTime = (TextView) findViewById(R.id.new_detail_time);
        newDetailAttention = (TextView) findViewById(R.id.new_detail_attention);
        newLayoutHtmlContent = (LinearLayout) findViewById(R.id.new_layout_html_content);
    }

    private void initViewEvent() {
        newDetailBack.setOnClickListener(occl);
        newDetailShare.setOnClickListener(occl);
        newDetailNeedZx.setOnClickListener(occl);
        newDetailCollect.setOnClickListener(occl);
        newDetailGood.setOnClickListener(occl);
        newDetailAttention.setOnClickListener(occl);
        newDatailIcon.setOnClickListener(occl);
    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.new_detail_icon:
                    Intent intent = new Intent(mContext, NewAuthorDetailActivity.class);
                    intent.putExtra("author_id", detail.getAuthor_id());
                    intent.putExtra("page_num", "");
                    startActivity(intent);
                    break;
                case R.id.new_detail_back:
                    finish();
                    break;
                case R.id.new_detail_need_zx:
                    //我要装修发单请求
                    Intent intent1 = new Intent(mContext, NewWebViewActivity.class);
                    intent1.putExtra("mLoadingUrl", Constant.FREE_BAOJIA_FADAN);
                    startActivity(intent1);
                    break;
                case R.id.new_detail_collect:
                    Log.e(TAG, "点击了收藏按钮！");
                    //收藏
                    if (Util.isLogin(mContext)) {
                        HttpCollect();
                    } else {
                        //当前用户没有登录跳转登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case R.id.new_detail_good:
                    //点赞
                    HttpGood();
                    break;
                case R.id.new_detail_attention:

                    //关注作者
                    if (Util.isLogin(mContext)) {
                        //请求关注接口
                        HttpAttention();
                    } else {
                        //当前用户没有登录跳转登录
                        startActivity(new Intent(mContext, LoginActivity.class));
                    }
                    break;
                case R.id.new_detail_share:
                    //分享
                    controller.openShare(NewArticleDetailActivity.this, false);
                    break;
            }
        }
    };

    //用户点赞+1效果以及当前点赞数的变换
    private void goodAnimation() {
        newDetailGoodNumAddOne.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                newDetailGoodNumAddOne.setVisibility(View.GONE);
                int num = Integer.parseInt(newDetailGoodNum.getText().toString());
                int numAddone = num + 1;
                newDetailGoodNum.setText("" + numAddone);
            }
        }, 1000);
    }

    //用户点赞
    private void HttpGood() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("aid", id);
        if (Util.isLogin(mContext)) {
            param.put("uid", Util.getUserId(mContext));
        }
        param.put("author_id", author_id);
        param.put("system_type", 1);
        okHttpUtil.post(Constant.USER_LIKE_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "点赞请求==" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                        if (msg.equals("您已经点过赞了")) {

                        } else {
                            goodAnimation();
                        }
                        newDetailGood.setImageResource(R.mipmap.good_after);
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

    //用户请求收藏和取消收藏
    private void HttpCollect() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("aid", detail.getAid());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", detail.getAuthor_id());
        param.put("system_type", "1");
        //拿到当前用户的收藏情况选择传入的收藏类型
        if (detail.getIs_collect().equals("0")) {
            //未收藏 请求收藏
            param.put("type", "1");
        } else {
            //已收藏 请求取消收藏
            param.put("type", "2");

        }
        okHttpUtil.post(Constant.COLLECT_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "收藏接口请求成功！====" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("200")) {
                        //请求成功
                        oneTime = true;
                        if (msg.equals("收藏成功")) {
                            Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                        }
                        HttpGetArticleDetail();//刷新数据
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFail(Request request, IOException e) {
                //请求失败
                Log.e(TAG, "收藏接口请求失败！====" + e.toString() + "===" + request.toString());
            }

            @Override
            public void onError(Response response, int code) {
                //请求错误
                Log.e(TAG, "收藏接口请求错误！====" + response.toString() + "===" + code);
            }
        });
    }

    //用户请求关注
    private void HttpAttention() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("author_id", detail.getAuthor_id());
        param.put("system_type", "1");
        okHttpUtil.post(Constant.FOLLOW_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = jsonObject.getString("msg");
                    Log.e(TAG, "点击关注返回json===" + json);
                    if (msg.equals("关注成功")) {
                        newDetailAttention.setText("已关注");
                        //修改
                        newDetailAttention.setBackgroundResource(R.drawable.detail_no_guanzhu);
                    } else {
                        newDetailAttention.setText("关注");
                        newDetailAttention.setBackgroundResource(R.drawable.detail_guanzhu);
                    }
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Request request, IOException e) {
                Log.e(TAG, "请求关注失败====原因===" + e.toString());
            }

            @Override
            public void onError(Response response, int code) {
                Log.e(TAG, "请求关注错误====原因===" + code);
            }
        });
    }

    private void HttpGetArticleDetail() {
        customDialog.show();
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);//文章id 上个界面传来的
        param.put("author_id", author_id);//作者的ID 上个界面传来的
        if (Util.isLogin(mContext)) {
            param.put("uid", Util.getUserId(mContext));//用户的ID
        }
        okHttpUtil.post(Constant.ARTICLE_DETAIL_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "数据请求成功===" + json);
                initNetData(json);
            }

            @Override
            public void onFail(Request request, IOException e) {
                //网络请求失败
                Toast.makeText(mContext, "网络请求失败！", Toast.LENGTH_LONG);
                customDialog.dismiss();
            }

            @Override
            public void onError(Response response, int code) {
                //网络请求出错
                Toast.makeText(mContext, "网络请求出错！", Toast.LENGTH_LONG);
                customDialog.dismiss();
            }
        });
    }

    private void HttpPostViewCount() {
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", Util.getUserId(mContext));
        param.put("aid", id);
        okHttpUtil.post(Constant.RECORD_VIEW_COUNT_URL, param, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                Log.e(TAG, "统计===" + json);
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
    }

    private void initNetData(String json) {
        Util.setLog(TAG, "-->>" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.getString("status");
            if (status.equals("200")) {
                //数据获取成功
                String data = jsonObject.getString("data");
                detail = new _ArticleDetail(data);
                newDetailTitle.setText("" + detail.getTitle());
                GlideUtils.glideLoader(getApplicationContext(), detail.getAuthor_img(), 0, R.mipmap.free_img, newDatailIcon, GlideUtils.CIRCLE_IMAGE);
                newDetailName.setText("" + detail.getAuthor_name());
                newDetailTime.setText("" + detail.getTime());
                newDetailGoodNum.setText("" + detail.getTup_count());
                if (zzzDataArray.isEmpty()) {
                    detailJsonArray = detail.getContent();
                    if (detailJsonArray.length() > 0) {
                        zzzDataArray.clear();
                        for (int i = 0; i < detailJsonArray.length(); i++) {
                            String textString = detailJsonArray.get(i).toString();
                            if (!"".equals(textString)) {
                                zzzDataArray.add(textString);
                            }
                        }
                    }
                    setPictureAndText(zzzDataArray);
                }
                customDialog.dismiss();
                if (detail.getIs_follow().equals("0")) {
                    //未关注 默认显示原图
                } else {
                    //已关注
                    newDetailAttention.setText("已关注");
                    newDetailAttention.setBackgroundResource(R.drawable.detail_no_guanzhu);
                }
                Log.e(TAG, "关注状态====>>>>" + detail.getIs_collect());
                if (detail.getIs_collect().equals("0")) {
                    //未收藏 显示空心
                    newDetailCollect.setImageResource(R.mipmap.like_before);
                } else {
                    //已收藏 显示实心收藏
                    newDetailCollect.setImageResource(R.mipmap.like_after);
                }
                if (detail.getIs_tup().equals("0")) {
                    //未点赞
                    newDetailGood.setImageResource(R.mipmap.good_before);
                } else {
                    newDetailGood.setImageResource(R.mipmap.good_after);
                }
                setUmengShareSetting();
            } else {
                //请求出了问题
                customDialog.dismiss();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            customDialog.dismiss();
        }
    }


    /**
     * 已经分离了图片和文字 --->> 设置文字和图片
     */
    private void setPictureAndText(ArrayList<String> _stringArrayList) {
        int size = _stringArrayList.size();
        ArrayList<String> imageUrlList = new ArrayList<String>();
        for (int n = 0; n < size; n++) {
            if (_stringArrayList.get(n).startsWith("http")) {
                imageUrlList.add(_stringArrayList.get(n));
                ImageView iv = new ImageView(mContext);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        DensityUtil.dip2px(mContext, 178));
                lp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20));
                iv.setLayoutParams(lp);
                iv.setAdjustViewBounds(true);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GlideUtils.glideLoader(mContext, _stringArrayList.get(n), R.mipmap.occupied1, R.mipmap.occupied1, iv);
                newLayoutHtmlContent.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
                newLayoutHtmlContent.addView(iv);
                seePicture(iv, imageUrlList);
            } else {
                TextView tv = new TextView(mContext);
                LinearLayout.LayoutParams textlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textlp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20));
                tv.setLayoutParams(textlp);
                tv.setText(_stringArrayList.get(n));
//                    tv.setTextColor(Color.parseColor("#080ELA"));
                tv.setTextSize(16);
                newLayoutHtmlContent.addView(tv);
            }
        }
    }


    /**
     * 已经分离了图片和文字 --->> 设置文字和图片  (旧方法 不使用)
     */
    private void setPictureAndTextOld(ArrayList<String> _pureDataList) {

        /*-----------------设置页头-----------------*/
//        Picasso.with(mContext)
//                .load(detailTitlePicture)
//                .fit()
//                .placeholder(R.mipmap.occupied1)
////                .error(R.mipmap.icon_head_default)
//                .into(iv_detail_top_image);
//
//        tv_detail_top_title.setText(detailTitle);
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

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20));
                iv.setLayoutParams(lp);
                iv.setAdjustViewBounds(true);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Picasso.with(mContext)
                        .load(_pureDataList.get(i))
                        .fit()
                        .placeholder(R.mipmap.occupied1)
                        .into(iv);
                newLayoutHtmlContent.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
                newLayoutHtmlContent.addView(iv);
                seePicture(iv, imageUrlList);

            } else {
                TextView tv = new TextView(mContext);
                LinearLayout.LayoutParams textlp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                textlp.setMargins(DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20), DensityUtil.px2dip(mContext, 40), DensityUtil.px2dip(mContext, 20));
                tv.setLayoutParams(textlp);
                tv.setText(_pureDataList.get(i));
//                    tv.setTextColor(Color.parseColor("#080ELA"));
                tv.setTextSize(16);
                newLayoutHtmlContent.addView(tv);
            }
        }
    }

    /**
     * 分割和抽取文版和图片地址
     *
     * @param raw_html_content
     */
    private ArrayList<String> handle_htmlContent(String raw_html_content) {
        System.out.println("---原始数据->>  " + raw_html_content + "<<----原始数据--");
        // 先标记图片
        String temTextHtml = raw_html_content.replaceAll(".*?<body.*?>(.*?)<\\/body>", "$1");

        System.out.println("---处理后->>  " + temTextHtml + "<<----处理后--");


        String html = temTextHtml.replaceAll("&nbsp;", "").replaceAll("<br />", "").replaceAll("<br/>", ""); // 处理 <br/> <br />

//        getImgSrc(html);
        ArrayList<String> stringData = new ArrayList<String>(); // 存放分割后的字符串

        html = html.replaceAll("<img", "[").replace("/>", "]"); //   将图片标签包围起来

        if (html.contains("</p>")) {
            String p_StringArray[] = html.split("</p>");
            for (int j = 0, arrayLen = p_StringArray.length; j < arrayLen; j++) {
                String p_element = new StringBuffer(p_StringArray[j]).append("</p>").toString();
                stringData.add(p_element);
            }
        } else {
            // 没有p标签
            stringData.add(html);
        }

//        if (html.contains("</div>")) {
//            // 有div标签
////            ArrayList<String> divList = new ArrayList<String>();
//            String htmlArray[] = html.split("</div>");
//            for (int i = 0, len = htmlArray.length; i < len; i++) {
//                String tempElement = new StringBuffer(htmlArray[i]).append("</div>").toString(); // 补回</div>
////                System.out.println("===</div>标签处理-->> " + tempElement + "  <<--</div>--");
////                divList.add(tempElement); // divList是div标签的集合
//
//                if (tempElement.contains("</p>")) {
//                    String p_StringArray[] = tempElement.split("</p>");
//                    for (int j = 0, arrayLen = p_StringArray.length; j < arrayLen; j++) {
//                        String p_element = new StringBuffer(p_StringArray[j]).append("</p>").toString();
//                        stringData.add(p_element);
//                    }
//                } else {
//                    // 没有p标签
//                    stringData.add(tempElement);
//                }
//
//            }
//        } else {
//            // 没有div标签时  只有p标签
//            String htmlPArray[] = html.split("</p>");
//            for (int k = 0, pLen = htmlPArray.length; k < pLen; k++) {
//                String element = new StringBuffer(htmlPArray[k]).append("</p>").toString();
//                stringData.add(element);
//            }
//        }


        for (int s = 0; s < stringData.size(); s++) {
            System.out.println("---xin-->> " + stringData.get(s) + "<<<--xin--");

        }

        ArrayList<String> pureStringList = new ArrayList<String>();

        int size = stringData.size();
        // 分割了文本和图片之后  分别抽取纯文本 和 纯图片地址
        if (oneTime) {
            oneTime = false;
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
        }
        return pureStringList;
    }

    //点击看图
    private void seePicture(ImageView imageView, final ArrayList<String> urlList) {
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, ImageActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("image_url_list", urlList);
                b.putString("article_id", id);
                b.putString("detailTitle", detail.getTitle());
                b.putString("detailTitlePicture", detail.getImage_url());
                b.putString("socialize_shareUrl", detail.getShare_url());
                b.putString("socialize_share_description", detail.getInfo());
                it.putExtra("See_Image_Bundle", b);
                startActivity(it);
            }
        });
    }

    //友盟社会化分享相关设置
    private void setUmengShareSetting() {
        setShareConfig();
        setSocialShareContent(detail.getImage_url());
    }

    private void setShareConfig() {
        controller = UMServiceFactory.getUMSocialService("com.umeng.share");
        weixinConfig();
        qqConfig();
        sinaConfig();
    }

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
        weixinContent.setShareContent(detail.getInfo()); // 描述
        weixinContent.setTitle(detail.getTitle()); // 标题
        weixinContent.setTargetUrl(detail.getShare_url()); // 分享的url
        weixinContent.setShareImage(urlImage); // 分享时呈现图片
//        weixinContent.setShareMedia(urlImage);
        controller.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(detail.getInfo());
        circleMedia.setTitle(detail.getTitle());
        circleMedia.setShareImage(urlImage);
        // circleMedia.setShareMedia(uMusic);
        // circleMedia.setShareMedia(video);
        circleMedia.setTargetUrl(detail.getShare_url());
        controller.setShareMedia(circleMedia);

//        UMImage qzoneImage = new UMImage(this,"http://www.umeng.com/images/pic/social/integrated_3.png");
//        qzoneImage.setTargetUrl("http://www.umeng.com/images/pic/social/integrated_3.png");

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(detail.getInfo());
        qzone.setTargetUrl(detail.getShare_url());
        qzone.setTitle(detail.getTitle());
        qzone.setShareImage(urlImage);
        controller.setShareMedia(qzone);

        // 设置QQ 分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(detail.getInfo());
        qqShareContent.setTitle(detail.getTitle());
        qqShareContent.setShareImage(urlImage);
        qqShareContent.setTargetUrl(detail.getShare_url());
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
        sinaContent.setShareContent(detail.getInfo());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        com.umeng.socialize.utils.Log.d("result", "onActivityResult");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Glide.with(NewArticleDetailActivity.this).pauseRequests();
    }
}
