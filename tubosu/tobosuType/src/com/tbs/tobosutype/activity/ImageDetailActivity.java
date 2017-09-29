package com.tbs.tobosutype.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.DesignFreePopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.tbs.tobosutype.utils.Util;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.utils.Log;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 逛图库详情 或 效果图精选详情的image跳转过来的  页面
 *
 * @author dec
 */
public class ImageDetailActivity extends Activity implements OnClickListener, OnPageChangeListener {
    private static final String TAG = ImageDetailActivity.class.getSimpleName();
    private Context mContext;
    private ImageView iv_back;
    private TextView tv_design;

    private RelativeLayout rel_image_detail_activity_toptitlelayout;

    /***效果图详情接口*/
    private String imgDetailUrl = Constant.TOBOSU_URL + "tapp/impression/detail";

    /***添加取消收藏接口*/
    private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";

    private String favConid;

    /**公司id*/
    private String cid;
    private String id;
    private String token;
    private HashMap<String,String> imgDetailParams;

    private ArrayList<String> imageUrlMapStringList;
    private ViewPager vp_singleMap;
    private TextView tv_number;
    private TextView tv_village;
    private TextView tv_village_desc;

    /**
     * 相关图册一
     */
    private ImageView iv_relate1;
    private TextView tv_relate_desc1;

    /**
     * 相关图册二
     */
    private ImageView iv_relate2;
    private TextView tv_relate_desc2;

    /**
     * 相关图册三
     */
    private ImageView iv_relate3;
    private TextView tv_relate_desc3;

    /**
     * 相关图册四
     */
    private ImageView iv_relate4;
    private TextView tv_relate_desc4;

    private ImageView no_relative_company_images;

    private ImageView iv_company_logo;
    private ImageView image_collect;
    private ImageView image_share;
    private TextView tv_relate_tc;
    private TextView tv_comsimpname;

    /***适配器*/
    private SingleMapAdapter singleMapAdapter;

    private String village;
    private String layout_id;
    private String area_id;
    private String plan_price;
    private int relateCount;

    private List<String> relatedImages;
    private List<String> relatedDescs;

    private int position = 0;
    private String hav_fav;
    private String oper_type = "-1";
    private String fenxiang_url;
    private String comsimpname;
    private String imageUrl;

    /***加载更多的布局*/
    private LinearLayout ll_loading;
    private String comid;

    private DesignFreePopupWindow designPopupWindow;
    private HashMap<String, String> pubOrderParams;
    private String phone;
    private String userid;

    private FrameLayout frame_layout_bars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_image_detail);
        mContext = ImageDetailActivity.this;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        frame_layout_bars = (FrameLayout) findViewById(R.id.frame_layout_bars);
        frame_layout_bars.bringToFront();
        rel_image_detail_activity_toptitlelayout = (RelativeLayout) findViewById(R.id.rel_image_detail_activity_toptitlelayout);
        iv_back = (ImageView) findViewById(R.id.iv_back_image_detail_activity);
        tv_design = (TextView) findViewById(R.id.tv_design);
        vp_singleMap = (ViewPager) findViewById(R.id.vp_singleMap);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_village = (TextView) findViewById(R.id.tv_village);
        tv_village_desc = (TextView) findViewById(R.id.tv_village_desc);
        tv_relate_desc1 = (TextView) findViewById(R.id.tv_relate_desc1);
        tv_relate_desc2 = (TextView) findViewById(R.id.tv_relate_desc2);
        tv_relate_desc3 = (TextView) findViewById(R.id.tv_relate_desc3);
        tv_relate_desc4 = (TextView) findViewById(R.id.tv_relate_desc4);
        no_relative_company_images = (ImageView) findViewById(R.id.no_relative_company_images);
        iv_relate1 = (ImageView) findViewById(R.id.iv_relate1);
        iv_relate2 = (ImageView) findViewById(R.id.iv_relate2);
        iv_relate3 = (ImageView) findViewById(R.id.iv_relate3);
        iv_relate4 = (ImageView) findViewById(R.id.iv_relate4);
        image_share = (ImageView) findViewById(R.id.iv_share_image_detail_activity);
        image_collect = (ImageView) findViewById(R.id.image_collect_imagedetail_activity);
        iv_company_logo = (ImageView) findViewById(R.id.iv_company_logo);
        tv_relate_tc = (TextView) findViewById(R.id.tv_relate_tc);
        tv_comsimpname = (TextView) findViewById(R.id.tv_comsimpname);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
    }

    private void initData() {
        id = getIntent().getExtras().getString("companyId"); // 公司id
        imageUrl = getIntent().getExtras().getString("url");
        token = AppInfoUtil.getToekn(getApplicationContext());
        imgDetailParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());

        imgDetailParams.put("url", imageUrl);
//        imgDetailParams.put("id", id);

        imageUrlMapStringList = new ArrayList<String>();

        pubOrderParams = new HashMap<String, String>();
        userid = getSharedPreferences("userInfo", 0).getString("userid", "");


        if (!TextUtils.isEmpty(token)) {
            imgDetailParams.put("token", token);
        }

        relatedDescs = new ArrayList<String>();
        relatedImages = new ArrayList<String>();
        requestImgDetail();
        singleMapAdapter = new SingleMapAdapter();
        vp_singleMap.setAdapter(singleMapAdapter);
    }

    /****
     * 效果图详情 接口请求
     */
    private void requestImgDetail() {
        if (!Constant.checkNetwork(mContext)) {
            Constant.toastNetOut(mContext);
            return;
        }

        OKHttpUtil.post(imgDetailUrl, imgDetailParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            praseJson(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /***
     * 解析json
     * @param json
     * @throws JSONException
     */
    private void praseJson(String json)throws JSONException {
        Util.setLog(TAG, "songchengcai 详情 的请求结果返回来 【"+ json + "】");

        JSONObject jsonObject = new JSONObject(json);
        if (jsonObject.getInt("error_code") == 0) {
            JSONObject dataObject = jsonObject.getJSONObject("data");
            favConid = dataObject.getString("id");
            //*******************************************************
//            _ImageDetail imageDetail = new _ImageDetail(dataObject);
//            Log.e(TAG, "获取的单个图册信息==village==" + imageDetail.getVillage());
//            Log.e(TAG, "获取的单个图册信息==公司名称==" + imageDetail.getComInfo().getComSimpName());
//            Log.e(TAG, "获取的单个图册信息==图册长度信息==" + imageDetail.getSingleMap().size());
//            for (int i = 0; i < imageDetail.getSingleMap().size(); i++) {
//                Log.e(TAG, "SingleMap中的内容===" + imageDetail.getSingleMap().get(i));
//            }
            //*******************************************************
            JSONArray singleMapArray = dataObject.getJSONArray("single_map");
            hav_fav = dataObject.getString("hav_fav");
            fenxiang_url = dataObject.getString("fenxiang_url");

            if ("1".equals(hav_fav)) {
                oper_type = "0";
                image_collect.setImageResource(R.drawable.image_love_sel);
            } else {
                image_collect.setImageResource(R.drawable.image_love_nor1);
                oper_type = "1";
            }
            imageUrlMapStringList = new ArrayList<String>();
            for (int i = 0; i < singleMapArray.length(); i++) {
                imageUrlMapStringList.add(singleMapArray.getString(i));
            }
            village = dataObject.getString("village");
            layout_id = dataObject.getString("layout_id");
            area_id = dataObject.getString("area_id");
            plan_price = dataObject.getString("plan_price");
            tv_number.setText("1/" + imageUrlMapStringList.size());
            singleMapAdapter.notifyDataSetChanged();
            JSONArray relateArray = dataObject.getJSONArray("relate");
            relateCount = relateArray.length();
            relatedImages.clear();
            relatedDescs.clear();
            int length = relateArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = relateArray.getJSONObject(i);
                relatedImages.add(object.getString("related_index_image_url"));
                relatedDescs.add(object.getString("des"));
            }
            selectView();

            if (length == 0) {
                no_relative_company_images.setVisibility(View.VISIBLE);
            }
            if (dataObject.getString("com_info").equals("false")||dataObject.getJSONObject("com_info").getString("comid").equals("false")) {
                comsimpname = "暂无公司名称";
                comid = "";
            } else {
                comsimpname = dataObject.getJSONObject("com_info").getString("comsimpname");
                comid = dataObject.getJSONObject("com_info").getString("comid");
                ImageLoaderUtil.loadImage(mContext, iv_company_logo, dataObject.getJSONObject("com_info").getString("logosmall"));
            }

            tv_comsimpname.setText(comsimpname);


            if (TextUtils.isEmpty(village)) {
                tv_village.setText("未知小区");
            } else {
                tv_village.setText(village);
            }
            tv_village_desc.setText(layout_id + "-" + area_id + "-" + plan_price + "万元");
            ll_loading.setVisibility(View.GONE);
        }else{
            Util.setLog(TAG, "songchengcai 详情 的请求结果返回来 没有公司id 【"+ json + "】");
        }
    }

    /***
     * 根据不同的相关图册，点击而展示不同的图册
     */
    private void selectView() {
        switch (relateCount) {
            case 0:
                tv_relate_desc1.setVisibility(View.GONE);
                tv_relate_desc2.setVisibility(View.GONE);
                tv_relate_desc3.setVisibility(View.GONE);
                tv_relate_desc4.setVisibility(View.GONE);
                iv_relate1.setVisibility(View.GONE);
                iv_relate2.setVisibility(View.GONE);
                iv_relate3.setVisibility(View.GONE);
                iv_relate4.setVisibility(View.GONE);
                tv_relate_tc.setVisibility(View.GONE);
                break;
            case 1:
                tv_relate_desc1.setText(relatedDescs.get(0));
                ImageLoaderUtil.loadImage(mContext, iv_relate1, relatedImages.get(0));
                tv_relate_desc2.setVisibility(View.INVISIBLE);
                tv_relate_desc3.setVisibility(View.GONE);
                tv_relate_desc4.setVisibility(View.GONE);
                iv_relate2.setVisibility(View.INVISIBLE);
                iv_relate3.setVisibility(View.GONE);
                iv_relate4.setVisibility(View.GONE);
                tv_relate_tc.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_relate_desc1.setText(relatedDescs.get(0));
                ImageLoaderUtil.loadImage(mContext, iv_relate1, relatedImages.get(0));
                tv_relate_desc2.setText(relatedDescs.get(1));
                ImageLoaderUtil.loadImage(mContext, iv_relate2, relatedImages.get(1));
                tv_relate_desc3.setVisibility(View.GONE);
                tv_relate_desc4.setVisibility(View.GONE);
                iv_relate3.setVisibility(View.GONE);
                iv_relate4.setVisibility(View.GONE);
                tv_relate_tc.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_relate_desc1.setText(relatedDescs.get(0));
                ImageLoaderUtil.loadImage(mContext, iv_relate1, relatedImages.get(0));
                tv_relate_desc2.setText(relatedDescs.get(1));
                ImageLoaderUtil.loadImage(mContext, iv_relate2, relatedImages.get(1));
                tv_relate_desc3.setText(relatedDescs.get(2));
                ImageLoaderUtil.loadImage(mContext, iv_relate3, relatedImages.get(2));
                tv_relate_desc4.setVisibility(View.INVISIBLE);
                iv_relate4.setVisibility(View.INVISIBLE);
                tv_relate_tc.setVisibility(View.VISIBLE);
                break;
            case 4:
                // FIXME MARK 加载图片注意看方法
                tv_relate_desc1.setText(relatedDescs.get(0));
                ImageLoaderUtil.loadImage(mContext, iv_relate1, relatedImages.get(0));
                tv_relate_desc2.setText(relatedDescs.get(1));
                ImageLoaderUtil.loadImage(mContext, iv_relate2, relatedImages.get(1));
                tv_relate_desc3.setText(relatedDescs.get(2));
                ImageLoaderUtil.loadImage(mContext, iv_relate3, relatedImages.get(2));
                tv_relate_desc4.setText(relatedDescs.get(3));
                ImageLoaderUtil.loadImage(mContext, iv_relate4, relatedImages.get(3));
                tv_relate_tc.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private void initEvent() {
        rel_image_detail_activity_toptitlelayout.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_design.setOnClickListener(this);
        vp_singleMap.setOnPageChangeListener(this);
        image_collect.setOnClickListener(this);
        image_share.setOnClickListener(this);
        iv_company_logo.setOnClickListener(this);
        iv_relate1.setOnClickListener(this);
        iv_relate2.setOnClickListener(this);
        iv_relate3.setOnClickListener(this);
        iv_relate4.setOnClickListener(this);
    }

    /***
     *  顶部viewpager浏览图片
     * @author dec
     *
     */
    class SingleMapAdapter extends PagerAdapter implements OnClickListener {


        @Override
        public int getCount() {
            if (imageUrlMapStringList.size() != 0) {
                return imageUrlMapStringList.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ScaleType.FIT_XY);
            ImageLoaderUtil.loadImage(mContext, iv, imageUrlMapStringList.get(position));
            container.addView(iv);
            iv.setOnClickListener(this);
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ImageDetailActivity.this, ImageDetailsFullScreenActivity.class);
            intent.putExtra("fav_conid", favConid);
            intent.putExtra("id", id);
            intent.putExtra("url", imageUrl);
            intent.putExtra("index", position);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ImageDetailActivity.class);
        switch (v.getId()) {
            case R.id.rel_image_detail_activity_toptitlelayout:
                break;
            case R.id.iv_back_image_detail_activity:
                finish();
                break;
            case R.id.iv_share_image_detail_activity:
//			String shareTitle = "我在土拨鼠网上看到" + comsimpname + "公司的装修效果图真的很不错，快来看看吧！";
//			String shareDesc = comsimpname + "公司的装修效果图真的很不错，快来看看吧！";

//			Log.d(TAG, "--分享标题【"+tv_village_desc.getText().toString().trim()+"】");
//			Log.d(TAG, "--分享简述【"+fenxiang_url+"】");
//			Log.d(TAG, "--分享url【"+fenxiang_url+"】");
                new ShareUtil(ImageDetailActivity.this, image_share, tv_village_desc.getText().toString().trim(), fenxiang_url, fenxiang_url);
                break;
            case R.id.image_collect_imagedetail_activity: // 添加 或者 取消 收藏
                operCollect();
                break;
            case R.id.iv_company_logo:
                Intent detailIntent = new Intent(this, DecorateCompanyDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("comid", comid);
                detailIntent.putExtras(bundle);
                startActivity(detailIntent);
                break;
            case R.id.tv_design:
                designPopupWindow = new DesignFreePopupWindow(mContext, itemsClick);
                designPopupWindow.showAtLocation(findViewById(R.id.tv_design), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//			startActivity(new Intent(this, FreeYuyueActivity.class));
                break;
            case R.id.iv_relate1:
                if (relatedImages.size() > 0) {
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_related_pic_XX");
                    intent.putExtra("id", favConid);
                    intent.putExtra("url", relatedImages.get(0));
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.iv_relate2:
                if (!relatedImages.isEmpty() && relatedImages.get(1) != null) {
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_related_pic_XX");
                    intent.putExtra("id", favConid);
                    intent.putExtra("url", relatedImages.get(1));
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.iv_relate3:
                if (!relatedImages.isEmpty() && relatedImages.get(2) != null) {
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_related_pic_XX");
                    intent.putExtra("id", favConid);
                    intent.putExtra("url", relatedImages.get(2));
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.iv_relate4:
                if (!relatedImages.isEmpty() && relatedImages.get(3) != null) {
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_related_pic_XX");
                    intent.putExtra("id", favConid);
                    intent.putExtra("url", relatedImages.get(3));
                    startActivity(intent);
                    finish();
                }

                break;

            default:
                break;
        }
    }


    /***
     * 为弹出窗口实现监听类 发单接口
     */
    private OnClickListener itemsClick = new OnClickListener() {

        public void onClick(View v) {

            designPopupWindow.dismiss();

            switch (v.getId()) {
                case R.id.dialog_freedesign_submit: // 免费预约确定按钮
                    phone = ((DesignFreePopupWindow) designPopupWindow).getPhone();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(mContext, "联系电话不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_design_for_me");
                    pubOrderParams.put("cellphone", phone);
//				pubOrderParams.put("comeurl", "http://m.tobosu.com/?channel=product&subchannel=android&chcode=android"); [原来的url]
                    pubOrderParams.put("comeurl", Constant.PIPE);
                    pubOrderParams.put("urlhistory", Constant.PIPE);
                    pubOrderParams.put("source", "893");
                    pubOrderParams.put("city", AppInfoUtil.getCityName(getApplicationContext()));
//				pubOrderParams.put("device", AppInfoUtil.getDeviceName());
//				pubOrderParams.put("version", AppInfoUtil.getAppVersionName(getApplicationContext()));
//				pubOrderParams.put("lat", AppInfoUtil.getLat(getApplicationContext()));
//				pubOrderParams.put("lng", AppInfoUtil.getLng(getApplicationContext()));

                    if (!TextUtils.isEmpty(userid)) {
                        pubOrderParams.put("userid", userid);
                    } else {
                        pubOrderParams.put("userid", "0");
                    }

                    requestPubOrder();
                    break;
                default:
                    break;
            }
        }

        /****
         * 输入电话号码发单接口请求
         */
        private void requestPubOrder() {
            OKHttpUtil.post(Constant.PUB_ORDERS, pubOrderParams, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("error_code") == 0) {
                                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_design_for_me_conform_reservation");
                                    Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
                                    Log.d(TAG, "--相关图册页面底部免费设计--【" + phone + "】-");
                                } else {
                                    Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    };

    /***
     * 添加和取消收藏的接口请求方法
     */
    private void operCollect() {
        token = AppInfoUtil.getToekn(getApplicationContext());
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ImageDetailActivity.this, LoginActivity.class);
            intent.putExtra("isFav", true);
            startActivityForResult(intent, 0);
            return;
        }

        //开始操作  --请注意下面这个if else语句总的fav_id以及它们相应的key value值--
        HashMap<String, String> favParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        favParams.put("fav_type", "showpic");
        favParams.put("token", token);
        favParams.put("fav_conid", favConid);
        Util.setLog(TAG, "必传的" + favConid);
        if (oper_type.equals("1")) {
            favParams.put("oper_type", "1");
            hav_fav = "1";
        } else {
            favParams.put("oper_type", "0");
            hav_fav = "0";
        }
        if (hav_fav.equals("1")) {
            image_collect.setImageResource(R.drawable.image_love_sel);
            oper_type = "0";
        } else {
            image_collect.setImageResource(R.drawable.image_love_nor1);
            oper_type = "1";
        }

        // 接口请求
        OKHttpUtil.post(favUrl, favParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Util.setLog(TAG, result);

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String msg = jsonObject.getString("msg");
                            if (msg.equals("操作成功")) {
                                if (oper_type.equals("0")) {
                                    Toast.makeText(mContext, "收藏成功!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "取消收藏成功!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                Util.setLog(TAG, msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		token = data.getStringExtra("token");
        token = AppInfoUtil.getToekn(getApplicationContext());
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int postion) {
        tv_number.setText(postion + 1 + "/" + imageUrlMapStringList.size());
        this.position = postion;
    }

}
