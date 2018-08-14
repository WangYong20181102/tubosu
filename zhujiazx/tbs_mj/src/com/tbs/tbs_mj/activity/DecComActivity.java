package com.tbs.tbs_mj.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.adapter.DecComCaseAdapter;
import com.tbs.tbs_mj.adapter.DecComJieshaoAdapter;
import com.tbs.tbs_mj.adapter.DecComShejiFangAnAdapter;
import com.tbs.tbs_mj.adapter.DecComShejishiAdapter;
import com.tbs.tbs_mj.base.*;
import com.tbs.tbs_mj.bean.EC;
import com.tbs.tbs_mj.bean.Event;
import com.tbs.tbs_mj.bean._CompanyDetail;
import com.tbs.tbs_mj.customview.ExpandableTextView;
import com.tbs.tbs_mj.customview.MyScrollView;
import com.tbs.tbs_mj.customview.VerticalTextview;
import com.tbs.tbs_mj.global.Constant;
import com.tbs.tbs_mj.global.OKHttpUtil;
import com.tbs.tbs_mj.utils.AppInfoUtil;
import com.tbs.tbs_mj.utils.EventBusUtil;
import com.tbs.tbs_mj.utils.GlideUtils;
import com.tbs.tbs_mj.utils.SpUtil;
import com.tbs.tbs_mj.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 装修公司主页 从装修公司首页单项列表点击进入
 * 3.6版本新增
 * creat by lin
 */
public class DecComActivity extends com.tbs.tbs_mj.base.BaseActivity {

    @BindView(R.id.dec_com_icon_iv)
    ImageView decComIconIv;//公司的头像图标
    @BindView(R.id.dec_com_back_ll)
    LinearLayout decComBackLl;
    @BindView(R.id.dec_com_share_ll)
    LinearLayout decComShareLl;
    @BindView(R.id.dec_com_shoucang_ll)
    LinearLayout decComShoucangLl;
    @BindView(R.id.dec_com_name_tv)
    TextView decComNameTv;//公司的名称
    @BindView(R.id.dec_com_renzheng_iv)
    ImageView decComRenzhengIv;//公司的认证图标
    @BindView(R.id.dec_com_tuijian_iv)
    ImageView decComTuijianIv;//公司的推荐图标
    @BindView(R.id.dec_com_star_iv_1)
    ImageView decComStarIv1;//星级1
    @BindView(R.id.dec_com_star_iv_2)
    ImageView decComStarIv2;//星级2
    @BindView(R.id.dec_com_star_iv_3)
    ImageView decComStarIv3;
    @BindView(R.id.dec_com_star_iv_4)
    ImageView decComStarIv4;
    @BindView(R.id.dec_com_star_iv_5)
    ImageView decComStarIv5;
    @BindView(R.id.dec_com_star_ll)
    LinearLayout decComStarLl;//星级图层
    @BindView(R.id.dec_com_zixun_num_tv)
    TextView decComZixunNumTv;//咨询数量  （16392咨询）
    @BindView(R.id.dec_com_about_renzheng_ll)
    LinearLayout decComAboutRenzhengLl;//认证图层
    @BindView(R.id.dec_com_address_tv)
    TextView decComAddressTv;//装修公司地址
    @BindView(R.id.dec_com_address_jiantou_iv)
    ImageView decComAddressJiantouIv;
    @BindView(R.id.dec_com_phone_iv)
    ImageView decComPhoneIv;//电话联系
    @BindView(R.id.dec_com_qq_iv)
    ImageView decComQqIv;//qq联系
    @BindView(R.id.dec_com_cone_rl)
    LinearLayout decComConeRl;//联系图层
    @BindView(R.id.dec_com_address_ll)
    LinearLayout decComAddressLl;//公司地址图层
    @BindView(R.id.dec_com_youhui_fenge)
    View decComYouhuiFenge;//优惠条上面的分割线
    @BindView(R.id.dec_com_youhui_lunbo_tv)
    VerticalTextview decComYouhuiLunboTv;//轮播介绍的信息
    @BindView(R.id.dec_com_youhui_rl)
    RelativeLayout decComYouhuiRl;//优惠的图层
    @BindView(R.id.dec_com_shejifangan_fenge)
    View decComShejifanganFenge;
    @BindView(R.id.dec_com_shejifangan_more_tv)
    TextView decComShejifanganMoreTv;//更多的设计方案
    @BindView(R.id.dec_com_shejifangan_more_rl)
    RelativeLayout decComShejifanganMoreRl;//更多的设计方案图层
    @BindView(R.id.dec_com_shejifangan_recyclerview)
    RecyclerView decComShejifanganRecyclerview;//设计方案横向列表
    @BindView(R.id.dec_com_shejifangan_ll)
    LinearLayout decComShejifanganLl;//整个设计方案图层 在装修公司没有上传时要隐藏处理
    @BindView(R.id.dec_com_zhuangxiu_anli_fenge)
    View decComZhuangxiuAnliFenge;//装修案例的分割线
    @BindView(R.id.dec_com_zhuangxiu_anli_more_tv)
    TextView decComZhuangxiuAnliMoreTv;//更多的装修案例
    @BindView(R.id.dec_com_zhuangxiu_anli_more_rl)
    RelativeLayout decComZhuangxiuAnliMoreRl;//更多的装修案例图层
    @BindView(R.id.dec_com_zhuangxiu_anli_recyclerview)
    RecyclerView decComZhuangxiuAnliRecyclerview;//装修案例的横向列表
    @BindView(R.id.dec_com_zhuangxiu_anli_ll)
    LinearLayout decComZhuangxiuAnliLl;//整个装修案例的图层  在装修公司没有上传装修案例时要隐藏整个图层
    @BindView(R.id.dec_com_shejishi_fenge)
    View decComShejishiFenge;
    @BindView(R.id.dec_com_shejishi_more_tv)
    TextView decComShejishiMoreTv;//更多设计师
    @BindView(R.id.dec_com_shejishi_more_rl)
    RelativeLayout decComShejishiMoreRl;//更多的设计师图层
    @BindView(R.id.dec_com_shejishi_recyclerview)
    RecyclerView decComShejishiRecyclerview;//设计师 横向的列表
    @BindView(R.id.dec_com_shejishi_ll)
    LinearLayout decComShejishiLl;//整个设计师图层
    @BindView(R.id.dec_com_jieshao_fenge)
    View decComJieshaoFenge;
    @BindView(R.id.dec_com_jieshao_expend_tv)
    ExpandableTextView decComJieshaoExpendTv;//公司介绍折叠页
    @BindView(R.id.dec_com_jieshao_recyclerview)
    RecyclerView decComJieshaoRecyclerview;//公司介绍的横向列表
    @BindView(R.id.dec_com_jieshao_ll)
    LinearLayout decComJieshaoLl;//整个公司介绍的图层 在没有上传介绍时要进行隐藏
    @BindView(R.id.dec_com_zizhi_fenge)
    View decComZizhiFenge;
    @BindView(R.id.dec_com_zizhi_ll)
    LinearLayout decComZizhiLl;//公司资质
    @BindView(R.id.dec_com_fuwu_xiangmu_fenge)
    View decComFuwuXiangmuFenge;
    @BindView(R.id.dec_com_fuwu_xiangmu_ll)
    LinearLayout decComFuwuXiangmuLl;
    @BindView(R.id.dec_com_find_price_rl)
    RelativeLayout decComFindPriceRl;
    @BindView(R.id.dec_com_shoucang_iv)
    ImageView decComShoucangIv;
    @BindView(R.id.dec_com_scroll_view)
    MyScrollView decComScrollView;
    @BindView(R.id.dec_com_banner_back)
    LinearLayout decComBannerBack;
    @BindView(R.id.dec_com_banner_share)
    LinearLayout decComBannerShare;
    @BindView(R.id.dec_com_banner_shoucang)
    LinearLayout decComBannerShoucang;
    @BindView(R.id.dec_com_banner)
    RelativeLayout decComBanner;
    @BindView(R.id.dec_com_banner_shoucang_iv)
    ImageView decComBannerShoucangIv;
    @BindView(R.id.banner_dever)
    View bannerDever;

    private String TAG = "DecComActivity";
    private Context mContext;
    private Intent mIntent;
    private Gson mGson;
    private String mCompanyId = "";//装修公司的id  从上一个界面传来
    private String mShareUrl = "";//分享的地址
    private LinearLayoutManager mLinearLayoutManager;//控制列表的走向
    private LinearLayoutManager mLinearLayoutManager2;//控制列表的走向
    private LinearLayoutManager mLinearLayoutManager3;//控制列表的走向
    private LinearLayoutManager mLinearLayoutManager4;//控制列表的走向
    private DecComCaseAdapter mDecComCaseAdapter;//装修案例适配器
    private DecComShejiFangAnAdapter mDecComShejiFangAnAdapter;//设计方案适配器
    private DecComShejishiAdapter mDecComShejishiAdapter;//设计师适配器
    private DecComJieshaoAdapter mDecComJieshaoAdapter;//装修公司介绍适配器
    private _CompanyDetail mCompanyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dec_com);
        ButterKnife.bind(this);
        mContext = this;
        initEvent();//UI设置
        HttpGetDecComData();//获取数据
    }


    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected boolean havePageId() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.NOTIF_DECCOMACTIVITY_MODE_IS_COLLECT:
                mCompanyDetail.getSuites().get((int) event.getData()).setIs_collect("1");
                break;
            case EC.EventCode.NOTIF_DECCOMACTIVITY_MODE_IS_NOT_COLLECT:
                mCompanyDetail.getSuites().get((int) event.getData()).setIs_collect("0");
                break;
        }
    }

    private void initEvent() {
        mIntent = getIntent();
        mGson = new Gson();
        mCompanyId = mIntent.getStringExtra("mCompanyId");
        SpUtil.setStatisticsEventPageId(mContext, mCompanyId);
        //设置控件的透明度
        decComBanner.getBackground().setAlpha(0);
        decComBanner.setVisibility(View.GONE);
        //适配器相关的设置
        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManager2 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManager3 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mLinearLayoutManager4 = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        //设置Manager
        //介绍
        decComJieshaoRecyclerview.setLayoutManager(mLinearLayoutManager);
        //设计方案
        decComShejifanganRecyclerview.setLayoutManager(mLinearLayoutManager2);
        //设计师
        decComShejishiRecyclerview.setLayoutManager(mLinearLayoutManager3);
        //装修案例
        decComZhuangxiuAnliRecyclerview.setLayoutManager(mLinearLayoutManager4);
        //设置页面的颜色
        decComScrollView.setBackgroundColor(Color.parseColor("#ffffff"));
        //页面滑动监听
        decComScrollView.setOnScrollListener(onScrollLister);
    }

    private MyScrollView.OnScrollLister onScrollLister = new MyScrollView.OnScrollLister() {

        @Override
        public void onScroll(int scrollY) {
            Log.e(TAG, "页面滑动监听===========" + scrollY);
            if (scrollY > 3) {
                //显示浮动banner  隐藏固定的banner
                decComBanner.setVisibility(View.VISIBLE);
                //隐藏固定banner
                decComShoucangLl.setVisibility(View.GONE);
                decComBackLl.setVisibility(View.GONE);
                decComShareLl.setVisibility(View.GONE);

            } else {
                decComBanner.setVisibility(View.GONE);
                if (AppInfoUtil.getTypeid(mContext).equals("3")){
                    decComShoucangLl.setVisibility(View.GONE);
                }else {
                    decComShoucangLl.setVisibility(View.GONE);
                }
                decComBackLl.setVisibility(View.VISIBLE);
                decComShareLl.setVisibility(View.GONE);
            }
            //设置控件的透明度
            if (scrollY <= 450) {
                decComBanner.getBackground().setAlpha((int) (scrollY * (255 / 450.1)));
                bannerDever.setVisibility(View.GONE);
            } else {
                decComBanner.getBackground().setAlpha(255);
                bannerDever.setVisibility(View.VISIBLE);
            }
        }
    };

    @OnClick({R.id.dec_com_back_ll, R.id.dec_com_share_ll, R.id.dec_com_shoucang_ll,
            R.id.dec_com_address_tv, R.id.dec_com_address_jiantou_iv,
            R.id.dec_com_phone_iv, R.id.dec_com_qq_iv, R.id.dec_com_youhui_rl,
            R.id.dec_com_shejifangan_more_tv, R.id.dec_com_shejifangan_more_rl,
            R.id.dec_com_zhuangxiu_anli_more_tv, R.id.dec_com_zhuangxiu_anli_more_rl,
            R.id.dec_com_shejishi_more_tv, R.id.dec_com_shejishi_more_rl,
            R.id.dec_com_jieshao_expend_tv, R.id.dec_com_zizhi_ll,
            R.id.dec_com_fuwu_xiangmu_ll, R.id.dec_com_find_price_rl,
            R.id.dec_com_banner_back, R.id.dec_com_banner_share, R.id.dec_com_banner_shoucang})
    public void onViewClickedInDecComActivity(View view) {
        switch (view.getId()) {
            case R.id.dec_com_back_ll:
            case R.id.dec_com_banner_back:
                //回退按钮
                finish();
                break;
            case R.id.dec_com_share_ll:
            case R.id.dec_com_banner_share:
                //分享按钮
                // 分享  设置Url等  头像：公司logo  分享标题：土拨鼠—苹果装饰   土拨鼠 + — + 公司简称 分享话术：我在土拨鼠看到一家靠谱的装修公司，现在0元设计家装费用立省一半！
                UMWeb umWeb = new UMWeb(mCompanyDetail.getShare_url() + "&channel=app&subchannel=android&chcode=" + AppInfoUtil.getChannType(mContext));
                umWeb.setDescription("我在土拨鼠看到一家靠谱的装修公司，现在0元设计家装费用立省一半");
                umWeb.setTitle("土拨鼠—" + mCompanyDetail.getName());
                umWeb.setThumb(new UMImage(mContext, mCompanyDetail.getImg_url()));
                new ShareAction(DecComActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ)
                        .withMedia(umWeb).open();
                break;
            case R.id.dec_com_shoucang_ll:
            case R.id.dec_com_banner_shoucang:
                //收藏按钮
                if (TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
                    //用户未登录 跳转到登录页面
//                    Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(mContext, NewLoginActivity.class);
////                    intent.putExtra("isFav", true);
//                    startActivityForResult(intent, 0);
                } else {
                    HttpCompShoucang(mCompanyDetail.getIs_collect(), mCompanyId);
                }

                break;
            case R.id.dec_com_address_tv:
            case R.id.dec_com_address_jiantou_iv:
                // 公司地址 如果有数据则跳转至地图页面
                if (TextUtils.isEmpty(mCompanyDetail.getLng()) || TextUtils.isEmpty(mCompanyDetail.getLat())) {

                } else {
                    //经纬度不为空进行跳转
                    Intent intent = new Intent(mContext, BaiduMapActivity.class);
                    intent.putExtra("mComPointLat", mCompanyDetail.getLat());
                    intent.putExtra("mComPointLong", mCompanyDetail.getLng());
                    intent.putExtra("mCompanyNmae", mCompanyDetail.getName());
                    intent.putExtra("mCompanyAddress", mCompanyDetail.getAddress());
                    mContext.startActivity(intent);
                }

                break;
            case R.id.dec_com_phone_iv:
                //拨打电话按钮
                if (TextUtils.isEmpty(mCompanyDetail.getTelephone())) {
                    showOpenPhone("" + SpUtil.getCustom_service_tel(mContext));
                } else {
                    showOpenPhone(mCompanyDetail.getTelephone());
                }
                break;
            case R.id.dec_com_qq_iv:
                //qq联系按钮
                if (Util.checkApkExist(mContext, "com.tencent.mobileqq")) {
                    String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + mCompanyDetail.getQq_account() + "&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                } else {
                    Toast.makeText(mContext, "本机未安装QQ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dec_com_youhui_rl:
                Intent intent = new Intent(mContext, CoYouHuiActivity.class);
                intent.putExtra("mCompanyId", mCompanyId);
                mContext.startActivity(intent);
                break;
            case R.id.dec_com_shejifangan_more_tv:
            case R.id.dec_com_shejifangan_more_rl:
                //更多的设计方案 跳转到设计方案列表
                Intent intentToDesignCaseActivity = new Intent(mContext, DesignCaseActivity.class);
                intentToDesignCaseActivity.putExtra("mCompanyId", mCompanyId);
                mContext.startActivity(intentToDesignCaseActivity);
                break;
            case R.id.dec_com_zhuangxiu_anli_more_tv:
            case R.id.dec_com_zhuangxiu_anli_more_rl:
                // 更多装修案例 跳转到装修案例列表
                Intent intentToCoDecCaseActivity = new Intent(mContext, CoDecorationCaseActivity.class);
                intentToCoDecCaseActivity.putExtra("mCompanyId", mCompanyId);
                mContext.startActivity(intentToCoDecCaseActivity);
                break;
            case R.id.dec_com_shejishi_more_tv:
            case R.id.dec_com_shejishi_more_rl:
                //更多设计师  跳转到设计师列表
                Intent intentToShejiShi = new Intent(mContext, DesignerListActivity.class);
                intentToShejiShi.putExtra("mCompanyId", mCompanyId);
                mContext.startActivity(intentToShejiShi);
                break;
            case R.id.dec_com_jieshao_expend_tv:
                break;
            case R.id.dec_com_zizhi_ll:
                // 跳转到公司资质页面 将公司资质数据直接传入
                Intent intentToZizhi = new Intent(mContext, CoZizhiActivity.class);
                String qualificationJson = mGson.toJson(mCompanyDetail.getQualification());
                intentToZizhi.putExtra("qualificationJson", qualificationJson);
                mContext.startActivity(intentToZizhi);
                break;
            case R.id.dec_com_fuwu_xiangmu_ll:
                //跳转到服务项目页面
                Intent intentToSerIActiviy = new Intent(mContext, ServiceItemActivity.class);
                String mServiceJson = mGson.toJson(mCompanyDetail.getService());
                intentToSerIActiviy.putExtra("mServiceJson", mServiceJson);
                mContext.startActivity(intentToSerIActiviy);
                break;
            case R.id.dec_com_find_price_rl:
                //跳转到发单页面
                Intent intentToFadan = new Intent(mContext, NewWebViewActivity.class);
                intentToFadan.putExtra("mLoadingUrl", SpUtil.getzjzxaj15(mContext));
                mContext.startActivity(intentToFadan);
                break;
        }
    }

    //打电话
    private void showOpenPhone(final String phoneNum) {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        TextView phone_num = (TextView) popview.findViewById(R.id.phone_num);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //设置电话
        phone_num.setText(phoneNum);
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        pop_phone_zixun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //收藏接口 传入当前的收藏状态
    private void HttpCompShoucang(String state, String mCompanyId) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("state", state);
        param.put("id", mCompanyId);
        param.put("uid", AppInfoUtil.getUuid(mContext));
        param.put("user_type", AppInfoUtil.getTypeid(mContext));
        param.put("type", "5");
        OKHttpUtil.post(Constant.COMPANY_COLLECT, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=====" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        String msg = jsonObject.optString("msg");
                        if (msg.equals("收藏成功")) {
                            //修改收藏的状态
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    decComShoucangIv.setImageResource(R.drawable.shoucang_after);
                                    decComBannerShoucangIv.setImageResource(R.drawable.shoucang_after);
                                    mCompanyDetail.setIs_collect("1");
                                    Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.COLLECT_COMPANY_CODE));
                                }
                            });
                        } else {
                            //修改收藏的状态
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    decComShoucangIv.setImageResource(R.drawable.shoucang_detail_befor);
                                    decComBannerShoucangIv.setImageResource(R.drawable.shoucang_start_black);
                                    mCompanyDetail.setIs_collect("0");
                                    Toast.makeText(mContext, "取消收藏成功！", Toast.LENGTH_SHORT).show();
                                    EventBusUtil.sendEvent(new Event(EC.EventCode.DELETE_COMPANY_CODE));
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取整个页面的数据接口
    private void HttpGetDecComData() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mCompanyId);
        if (!TextUtils.isEmpty(AppInfoUtil.getUserid(mContext))) {
            param.put("uid", AppInfoUtil.getUuid(mContext));
            param.put("user_type", AppInfoUtil.getTypeid(mContext));
        }
        Log.e(TAG, "请求参数===token====" + Util.getDateToken() + "=====id=====" + mCompanyId + "======uid= " + AppInfoUtil.getUuid(mContext));
        OKHttpUtil.post(Constant.COMPANY_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败=========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = new String(response.body().string());
                Log.e(TAG, "获取数据成功========" + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView(json);
                    }
                });
            }
        });
    }

    //UI初始化
    private void initView(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String status = jsonObject.optString("status");
            if (status.equals("200")) {
                //数据获取成功
                String data = jsonObject.optString("data");
                mCompanyDetail = mGson.fromJson(data, _CompanyDetail.class);
                //布置数据
                //用户是否收藏
                if (mCompanyDetail.getIs_collect().equals("1")) {
                    //头部显示  以及滑动的头部显示
                    decComBannerShoucangIv.setImageResource(R.drawable.shoucang_after);
                    decComShoucangIv.setImageResource(R.drawable.shoucang_after);
                } else {
                    decComShoucangIv.setImageResource(R.drawable.shoucang_detail_befor);
                    decComBannerShoucangIv.setImageResource(R.drawable.shoucang_start_black);
                }
                //公司头像
                GlideUtils.glideLoader(mContext, mCompanyDetail.getImg_url(),
                        R.drawable.iamge_loading, R.drawable.iamge_loading, decComIconIv);
                //公司名称
                decComNameTv.setText("" + mCompanyDetail.getName());
                //认证信息
                if (mCompanyDetail.getCertification().equals("0")) {
                    //未认证
                    decComRenzhengIv.setVisibility(View.GONE);
                } else {
                    decComRenzhengIv.setVisibility(View.VISIBLE);
                }
                //推荐信息
                if (mCompanyDetail.getRecommend().equals("0")) {
                    //未推荐
                    decComTuijianIv.setVisibility(View.GONE);
                } else {
                    decComTuijianIv.setVisibility(View.VISIBLE);
                }
                //设置星级
                setStartNum(mCompanyDetail.getGrade());
                //设置咨询数
                decComZixunNumTv.setText("(" + mCompanyDetail.getView_count() + "咨询" + ")");
                //设置地址
                if (TextUtils.isEmpty(mCompanyDetail.getAddress())) {
                    decComAddressTv.setText("该商家未填写地址");
                } else {
                    decComAddressTv.setText("" + mCompanyDetail.getAddress());
                }
                //设置地址箭头的显示
                if (TextUtils.isEmpty(mCompanyDetail.getLat()) || TextUtils.isEmpty(mCompanyDetail.getLng())) {
                    decComAddressJiantouIv.setVisibility(View.GONE);
                } else {
                    decComAddressJiantouIv.setVisibility(View.VISIBLE);
                }
                //设置联系方式
                if (TextUtils.isEmpty(mCompanyDetail.getQq_account())) {
                    //未填写QQ
                    decComQqIv.setVisibility(View.GONE);
                } else {
                    decComQqIv.setVisibility(View.VISIBLE);
                }
                //设置轮播活动
                if (mCompanyDetail.getPromotions_title().isEmpty()) {
                    //活动为空
                    decComYouhuiRl.setVisibility(View.GONE);
                    decComYouhuiFenge.setVisibility(View.GONE);
                } else {
                    //有活动数据
                    decComYouhuiRl.setVisibility(View.VISIBLE);
                    decComYouhuiFenge.setVisibility(View.VISIBLE);
                    decComYouhuiLunboTv.setTextList(mCompanyDetail.getPromotions_title());
                    decComYouhuiLunboTv.setText(12, 2, Color.parseColor("#cc333333"));//设置属性
                    decComYouhuiLunboTv.setTextStillTime(3000);//设置停留时长间隔
                    decComYouhuiLunboTv.setAnimTime(700);//设置进入和退出的时间间隔
                    decComYouhuiLunboTv.setOnItemClickListener(new VerticalTextview.OnItemClickListener() {
                        @Override
                        public void onItemClick(int var1) {
                            Intent intent = new Intent(mContext, CoYouHuiActivity.class);
                            intent.putExtra("mCompanyId", mCompanyId);
                            mContext.startActivity(intent);
                        }
                    });
                    decComYouhuiLunboTv.startAutoScroll();
                }
                //设置设计方案
                if (mCompanyDetail.getSuites().isEmpty()) {
                    decComShejifanganLl.setVisibility(View.GONE);
                    decComShejifanganFenge.setVisibility(View.GONE);
                } else {
                    //有设计方案数据
                    decComShejifanganLl.setVisibility(View.VISIBLE);
                    decComShejifanganFenge.setVisibility(View.VISIBLE);
                    mDecComShejiFangAnAdapter = new DecComShejiFangAnAdapter(mContext, mCompanyDetail.getSuites());
                    decComShejifanganRecyclerview.setAdapter(mDecComShejiFangAnAdapter);
                    mDecComShejiFangAnAdapter.setOnDCShejiItemClickLister(onDCShejiItemClickLister);
                }
                //设置装修案例
                if (mCompanyDetail.getCases().isEmpty()) {
                    decComZhuangxiuAnliLl.setVisibility(View.GONE);
                    decComZhuangxiuAnliFenge.setVisibility(View.GONE);
                } else {
                    decComZhuangxiuAnliLl.setVisibility(View.VISIBLE);
                    decComZhuangxiuAnliFenge.setVisibility(View.VISIBLE);
                    mDecComCaseAdapter = new DecComCaseAdapter(mContext, mCompanyDetail.getCases());
                    decComZhuangxiuAnliRecyclerview.setAdapter(mDecComCaseAdapter);
                    mDecComCaseAdapter.setOnDecComCaseItemClickLister(onDecComCaseItemClickLister);
                }
                //设置设计团队
                if (mCompanyDetail.getDesigners().isEmpty()) {
                    decComShejishiLl.setVisibility(View.GONE);
                    decComShejishiFenge.setVisibility(View.GONE);
                } else {
                    decComShejishiLl.setVisibility(View.VISIBLE);
                    decComShejishiFenge.setVisibility(View.VISIBLE);
                    mDecComShejishiAdapter = new DecComShejishiAdapter(mContext, mCompanyDetail.getDesigners());
                    decComShejishiRecyclerview.setAdapter(mDecComShejishiAdapter);
                    mDecComShejishiAdapter.setOnItemClickLister(onItemClickLister);
                }
                //设置公司介绍
                if (TextUtils.isEmpty(mCompanyDetail.getIntroduction()) && mCompanyDetail.getQualification().isEmpty()) {
                    //介绍和图片都为空
                    decComJieshaoLl.setVisibility(View.GONE);
                    decComJieshaoFenge.setVisibility(View.GONE);
                } else {
                    decComJieshaoLl.setVisibility(View.VISIBLE);
                    decComJieshaoFenge.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(mCompanyDetail.getIntroduction())) {
                    //公司介绍为空
                    decComJieshaoExpendTv.setVisibility(View.GONE);
                } else {
                    decComJieshaoExpendTv.setVisibility(View.VISIBLE);
                    decComJieshaoExpendTv.setText("" + mCompanyDetail.getIntroduction());
                }
                /// TODO: 2017/12/4  公司介绍图片永远为空
                decComJieshaoRecyclerview.setVisibility(View.GONE);
//                if (mCompanyDetail.getQualification().isEmpty()) {
//                    decComJieshaoRecyclerview.setVisibility(View.GONE);
//                } else {
//                    //列表不为空，设置适配器
//                    decComJieshaoRecyclerview.setVisibility(View.VISIBLE);
//                    mDecComJieshaoAdapter = new DecComJieshaoAdapter(mContext, mCompanyDetail.getQualification());
//                    decComJieshaoRecyclerview.setAdapter(mDecComJieshaoAdapter);
//                    mDecComJieshaoAdapter.setOnDCJieshaoClickLister(onDCJieshaoClickLister);
//                }
                //公司资质
//                if (mCompanyDetail.getQualification().isEmpty()) {
//                    //没有公司资质数据 隐藏点击按钮
//                    decComZizhiLl.setVisibility(View.GONE);
//                    decComZizhiFenge.setVisibility(View.GONE);
//                } else {
                decComZizhiLl.setVisibility(View.VISIBLE);
                decComZizhiFenge.setVisibility(View.VISIBLE);
//                }
                //服务项目
                if (mCompanyDetail.getService().isEmpty()) {
                    //服务项目按钮隐藏
                    decComFuwuXiangmuLl.setVisibility(View.GONE);
                    decComFuwuXiangmuFenge.setVisibility(View.GONE);
                } else {
                    //服务项目按钮隐藏
                    decComFuwuXiangmuLl.setVisibility(View.VISIBLE);
                    decComFuwuXiangmuFenge.setVisibility(View.VISIBLE);
                }
            } else {
                //数据获取失败
                Toast.makeText(mContext, "获取数据失败！", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //适配器点击事件
    //设计方案 子项点击事件
    private DecComShejiFangAnAdapter.OnDCShejiItemClickLister onDCShejiItemClickLister = new DecComShejiFangAnAdapter.OnDCShejiItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击了整个图片层 进入图片查看器
            String DImageJson = mGson.toJson(mCompanyDetail.getSuites());
            SpUtil.setDoubleImageListJson(mContext, DImageJson);
            Intent intent = new Intent(mContext, DImageLookingActivity.class);
            intent.putExtra("mPosition", position);
            intent.putExtra("mWhereFrom", "DecComActivity");
            mContext.startActivity(intent);
        }
    };
    //案例的子项点击事件
    private DecComCaseAdapter.OnDecComCaseItemClickLister onDecComCaseItemClickLister = new DecComCaseAdapter.OnDecComCaseItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            //点击跳转到案例详细;
            Intent intent = new Intent(mContext, DecorationCaseDetailActivity.class);
            intent.putExtra("deco_case_id", mCompanyDetail.getCases().get(position).getId());
            mContext.startActivity(intent);
        }
    };
    //设计师子项点击事件
    private DecComShejishiAdapter.OnItemClickLister onItemClickLister = new DecComShejishiAdapter.OnItemClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            /// TODO: 2017/12/4 点击跳转到设计师主页
            Intent it = new Intent(mContext, SheJiShiActivity.class);
            Util.setErrorLog(TAG, "===================设计师id=========>>" + mCompanyDetail.getDesigners().get(position).getDesignerId());
            it.putExtra("designer_id", mCompanyDetail.getDesigners().get(position).getDesignerId());
            startActivity(it);
        }
    };
    //公司介绍的子项点击事件
    private DecComJieshaoAdapter.OnDCJieshaoClickLister onDCJieshaoClickLister = new DecComJieshaoAdapter.OnDCJieshaoClickLister() {
        @Override
        public void onItemClick(View view, int position) {
            /// TODO: 2017/12/4 点击公司介绍的子项进入图片查看器
        }
    };

    //设置星级
    private void setStartNum(String startNum) {
        if (startNum.equals("0")) {
            //0星
            decComStarIv1.setImageResource(R.drawable.decorate_start_none);
            decComStarIv2.setImageResource(R.drawable.decorate_start_none);
            decComStarIv3.setImageResource(R.drawable.decorate_start_none);
            decComStarIv4.setImageResource(R.drawable.decorate_start_none);
            decComStarIv5.setImageResource(R.drawable.decorate_start_none);
        } else if (startNum.equals("1")) {
            decComStarIv1.setImageResource(R.drawable.decorate_start);
            decComStarIv2.setImageResource(R.drawable.decorate_start_none);
            decComStarIv3.setImageResource(R.drawable.decorate_start_none);
            decComStarIv4.setImageResource(R.drawable.decorate_start_none);
            decComStarIv5.setImageResource(R.drawable.decorate_start_none);
        } else if (startNum.equals("2")) {
            decComStarIv1.setImageResource(R.drawable.decorate_start);
            decComStarIv2.setImageResource(R.drawable.decorate_start);
            decComStarIv3.setImageResource(R.drawable.decorate_start_none);
            decComStarIv4.setImageResource(R.drawable.decorate_start_none);
            decComStarIv5.setImageResource(R.drawable.decorate_start_none);
        } else if (startNum.equals("3")) {
            decComStarIv1.setImageResource(R.drawable.decorate_start);
            decComStarIv2.setImageResource(R.drawable.decorate_start);
            decComStarIv3.setImageResource(R.drawable.decorate_start);
            decComStarIv4.setImageResource(R.drawable.decorate_start_none);
            decComStarIv5.setImageResource(R.drawable.decorate_start_none);
        } else if (startNum.equals("4")) {
            decComStarIv1.setImageResource(R.drawable.decorate_start);
            decComStarIv2.setImageResource(R.drawable.decorate_start);
            decComStarIv3.setImageResource(R.drawable.decorate_start);
            decComStarIv4.setImageResource(R.drawable.decorate_start);
            decComStarIv5.setImageResource(R.drawable.decorate_start_none);
        } else {
            decComStarIv1.setImageResource(R.drawable.decorate_start);
            decComStarIv2.setImageResource(R.drawable.decorate_start);
            decComStarIv3.setImageResource(R.drawable.decorate_start);
            decComStarIv4.setImageResource(R.drawable.decorate_start);
            decComStarIv5.setImageResource(R.drawable.decorate_start);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCompanyDetail != null
                && mCompanyDetail.getPromotions_title() != null
                && !mCompanyDetail.getPromotions_title().isEmpty()
                && decComYouhuiLunboTv != null) {
            decComYouhuiLunboTv.startAutoScroll();
        }
        if (AppInfoUtil.getTypeid(mContext).equals("3")) {
            decComBannerShoucang.setVisibility(View.GONE);
            decComShareLl.setVisibility(View.GONE);
        }else {
            decComBannerShoucang.setVisibility(View.GONE);
            decComShareLl.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCompanyDetail != null
                && mCompanyDetail.getPromotions_title() != null
                && !mCompanyDetail.getPromotions_title().isEmpty()
                && decComYouhuiLunboTv != null) {
            decComYouhuiLunboTv.stopAutoScroll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompanyDetail != null
                && mCompanyDetail.getPromotions_title() != null
                && !mCompanyDetail.getPromotions_title().isEmpty()
                && decComYouhuiLunboTv != null) {
            decComYouhuiLunboTv.stopAutoScroll();
        }
    }
}
