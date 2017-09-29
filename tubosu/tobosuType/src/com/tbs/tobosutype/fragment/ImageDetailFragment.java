package com.tbs.tobosutype.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.ApplyforSuccessActivity;
import com.tbs.tobosutype.activity.ImageDetailsFullScreenActivity;
import com.tbs.tobosutype.activity.LoginActivity;
import com.tbs.tobosutype.bean._CardDataItem;
import com.tbs.tobosutype.bean._ImageDetail;
import com.tbs.tobosutype.customview.CardSlidePanel;
import com.tbs.tobosutype.customview.CustomWaitDialog;
import com.tbs.tobosutype.customview.DesignFreePopupWindow;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.ImageLoaderUtil;
import com.tbs.tobosutype.utils.ShareUtil;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@SuppressLint({"HandlerLeak", "NewApi", "InflateParams"})
public class ImageDetailFragment extends Fragment {
    private String TAG = "ImageDetailFragment";
    private CardSlidePanel slidePanel;
    private Context mContext;
    private Bundle mBundle;//获取数据的数据包
    private String id = "";//接收Id
    private String url = "";//接收url
    private String fav_conid = ""; // 接收的图册id
    private String shareUrl = "";//分享使用的url
    private String shareContent = "";//分享的内容
    private String hav_fav;//关注相关按钮
    private String oper_type = "";
    private String token;//用户唯一标识
    private String phone;//用户填写的手机号
    private String userid;//用户id
    private HashMap<String, String> imgDetailParams;
    private HashMap<String, String> pubOrderParams;//发单参数
    private DesignFreePopupWindow designPopupWindow;//底部弹窗按钮

    /***效果图详情接口*/
    private String imgDetailUrl = Constant.TOBOSU_URL + "tapp/impression/detail";

    /***添加取消收藏接口*/
    private String favUrl = Constant.TOBOSU_URL + "/tapp/user/fav";

    private List<String> singleMap = new ArrayList<String>();

    private View imageDetailLoading;//加载图层
    private ImageView imgDetailBack;//顶部返回按钮
    private TextView imgVillage;//小区名称
    private ImageView imgDetailIcon;//公司的小图标
    private TextView imgDetailConsName;//公司名称
    private TextView imgDetailDisigner;//设计者名称
    private TextView imgDetailStyle;//风格
    private TextView imgDetailArea;//面积
    private TextView imgDetailPrice;//费用
    private Button imgDetailIwant;//我要设计按钮
    private ImageView imgDetailShare;//分享按钮
    private ImageView imgDetailLike;//关注按钮
    private CustomWaitDialog customWaitDialog;//加载图层

    private CardSlidePanel.CardSwitchListener cardSwitchListener;

    private List<_CardDataItem> dataList = new ArrayList<_CardDataItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_detail, null);
        mContext = getActivity();
        customWaitDialog = new CustomWaitDialog(mContext);
        token = AppInfoUtil.getToekn(mContext);
        mBundle = getArguments();
        userid = mBundle.getString("userid");
        initView(rootView);
        bindView(rootView);
        initViewEven();
        initEvent();
        return rootView;
    }

    private void bindView(View rootView) {
        imageDetailLoading = rootView.findViewById(R.id.img_detail_Loading);
        imgDetailBack = (ImageView) rootView.findViewById(R.id.img_detail_back);
        imgVillage = (TextView) rootView.findViewById(R.id.img_detail_village);
        imgDetailIcon = (ImageView) rootView.findViewById(R.id.img_detail_icon);
        imgDetailConsName = (TextView) rootView.findViewById(R.id.img_detail_comsname);
        imgDetailDisigner = (TextView) rootView.findViewById(R.id.img_detail_disinger);
        imgDetailStyle = (TextView) rootView.findViewById(R.id.img_detail_style);
        imgDetailArea = (TextView) rootView.findViewById(R.id.img_detail_area);
        imgDetailPrice = (TextView) rootView.findViewById(R.id.img_detail_price);
        imgDetailIwant = (Button) rootView.findViewById(R.id.img_detail_iwant);
        imgDetailShare = (ImageView) rootView.findViewById(R.id.img_detail_share);
        imgDetailLike = (ImageView) rootView.findViewById(R.id.img_detail_like);
    }

    private void initViewEven() {
        imgDetailBack.setOnClickListener(occl);
        imgDetailIwant.setOnClickListener(occl);
        imgDetailLike.setOnClickListener(occl);
        imgDetailShare.setOnClickListener(occl);
    }

    private void initEvent() {

        id = mBundle.getString("id");
        url = mBundle.getString("url");
        fav_conid = mBundle.getString("fav_conid");
        pubOrderParams = new HashMap<String, String>();
        imgDetailParams = AppInfoUtil.getPublicHashMapParams(mContext);
        imgDetailParams.put("token", AppInfoUtil.getToekn(mContext));
        imgDetailParams.put("id", id);
        imgDetailParams.put("url", url);
        imgDetailParams.put("source", "893");
        imgDetailParams.put("urlhistory", Constant.PIPE); // 渠道代码
        imgDetailParams.put("comeurl", Constant.PIPE); //订单发布页面
        HttpRequestImgDetail();
    }

    private void HttpRequestImgDetail() {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "请求成功=====" + json);
                        disposeJson(json);
                    }
                });
            }
        });
    }

    //处理请求的数据进行布局显示
    private void disposeJson(String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject data = jsonObject.getJSONObject("data");
            _ImageDetail imageDetail = new _ImageDetail(data);
            singleMap.addAll(imageDetail.getSingleMap());
            Log.e(TAG, "当前的图册集合长度=====" + singleMap.size());
//            for (int i = 0; i < singleMap.size(); i++) {
//                Log.e(TAG, "当前的图册集合包含内容=====" + singleMap.get(i));
//            }
            //设置图层
            if (!singleMap.isEmpty()) {
                prepareDataList();
                slidePanel.fillData(dataList);
            }
            //设置小区
            if (TextUtils.isEmpty(imageDetail.getVillage())) {
                imgVillage.setText("未知小区");
            } else {
                imgVillage.setText("" + imageDetail.getVillage());
            }

            if (imageDetail.getComInfo() != null) {
                //设置公司小头像
                if (!TextUtils.isEmpty(imageDetail.getComInfo().getLogoSmall()) || imageDetail.getComInfo().getLogoSmall() != null) {
                    ImageLoaderUtil.loadImage(mContext, imgDetailIcon, imageDetail.getComInfo().getLogoSmall());
                }
                //设置公司名称
                if (!TextUtils.isEmpty(imageDetail.getComInfo().getComSimpName()) || imageDetail.getComInfo().getComSimpName() == null) {
                    imgDetailConsName.setText("" + imageDetail.getComInfo().getComSimpName());
                } else {
                    imgDetailConsName.setText("未知公司名称");
                }
            }

            //设置设计师名称
            if (TextUtils.isEmpty(imageDetail.getDesignerName()) || imageDetail.getDesignerName().equals("")) {
                imgDetailDisigner.setVisibility(View.GONE);
            } else {
                imgDetailDisigner.setText("设计师：" + imageDetail.getDesignerName());
            }

            //设置风格
            imgDetailStyle.setText("风格：" + imageDetail.getStyleId());
            //设置面积
            imgDetailArea.setText("面积：" + imageDetail.getAreId());
            //设置费用
            imgDetailPrice.setText("费用：" + imageDetail.getPlanPrice() + "万");
            //设置分享的url
            shareUrl = imageDetail.getFengxiangUrl();
            Log.e(TAG, "分享的url===" + shareUrl);
            imageDetailLoading.setVisibility(View.GONE);
            shareContent = imageDetail.getLayoutId() + "-" + imageDetail.getAreId() + "-" + imageDetail.getPlanPrice() + "万元";
            //设置关注相关事宜
            hav_fav = imageDetail.getHavFav();
            if ("1".equals(hav_fav)) {
                oper_type = "0";
                imgDetailLike.setImageResource(R.drawable.image_love_sel);
            } else {
                imgDetailLike.setImageResource(R.drawable.image_love_nor1);
                oper_type = "1";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView(View rootView) {
        slidePanel = (CardSlidePanel) rootView.findViewById(R.id.image_slide_panel);
        cardSwitchListener = new CardSlidePanel.CardSwitchListener() {
            @Override
            public void onShow(int index) {

            }

            @Override
            public void onCardVanish(int index, int type) {

            }

            @Override
            public void onItemClick(View cardView, int index) {
//                Log.e("CardFragment", "卡片点击-" + index + "卡片的地址==" + dataList.get(index).imagePath);
                if (dataList.isEmpty()) {
                    Toast.makeText(mContext, "当前装修公司未上传图册！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(mContext, ImageDetailsFullScreenActivity.class);
                    intent.putExtra("fav_conid", fav_conid);
                    intent.putExtra("url", url);
                    int num = (int) Math.sqrt(dataList.size() / 100);
                    if (index > num || index == num) {
                        intent.putExtra("index", index % num);
                    } else {
                        intent.putExtra("index", index);
                    }
                    startActivity(intent);
                }

            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);
    }

    private void prepareDataList() {
        int num = singleMap.size();
        if (num == 0) {

        } else {
            for (int j = 0; j < num * 100; j++) {
                //外部循环尽可能多的处理图片的重复次数
                for (int i = 0; i < num; i++) {
                    _CardDataItem dataItem = new _CardDataItem();
                    dataItem.imagePath = singleMap.get(i);
                    dataList.add(dataItem);
                }
            }
        }

    }

    private View.OnClickListener occl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_detail_back:
                    Log.e(TAG, "当前点击了回退按钮");
                    getActivity().finish();
                    break;
                case R.id.img_detail_iwant:
                    Log.e(TAG, "当前点击了发单按钮");
                    designPopupWindow = new DesignFreePopupWindow(mContext, itemsClick);
                    designPopupWindow.showAtLocation(slidePanel, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    break;
                case R.id.img_detail_share:
                    Log.e(TAG, "当前点击了分享按钮");
                    if (TextUtils.isEmpty(shareUrl)) {
                        Log.e(TAG, "分享链接为空");
                    } else {
                        new ShareUtil(mContext, imgDetailShare, shareContent, shareUrl, shareUrl);
                    }
                    break;
                case R.id.img_detail_like:
                    Log.e(TAG, "当前点击了关注按钮");
                    HttpChangeUserLike();
                    break;
            }
        }
    };

    private void HttpChangeUserLike() {
        token = AppInfoUtil.getToekn(mContext);
        if (TextUtils.isEmpty(token)) {
            Toast.makeText(mContext, "您还没有登陆,请登陆后再来收藏!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra("isFav", true);
            startActivityForResult(intent, 0);
            return;
        }

        //开始操作  --请注意下面这个if else语句总的fav_id以及它们相应的key value值--
        HashMap<String, String> favParams = AppInfoUtil.getPublicHashMapParams(mContext);
        favParams.put("fav_type", "showpic");
        favParams.put("token", token);

        favParams.put("fav_conid", fav_conid);

        if (oper_type.equals("1")) {
            favParams.put("oper_type", "1");
            hav_fav = "1";
        } else {
            favParams.put("oper_type", "0");
            hav_fav = "0";
        }
        if (hav_fav.equals("1")) {
            imgDetailLike.setImageResource(R.drawable.image_love_sel);
            oper_type = "0";
        } else {
            imgDetailLike.setImageResource(R.drawable.image_love_nor1);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private View.OnClickListener itemsClick = new View.OnClickListener() {

        public void onClick(View v) {

            designPopupWindow.dismiss();

            switch (v.getId()) {
                case R.id.dialog_freedesign_submit: // 免费预约确定按钮
                    phone = ((DesignFreePopupWindow) designPopupWindow).getPhone();
                    if (TextUtils.isEmpty(phone)) {
                        Toast.makeText(mContext, "联系电话不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_design_3.0");
                    pubOrderParams.put("cellphone", phone);
                    pubOrderParams.put("comeurl", Constant.PIPE);
                    pubOrderParams.put("urlhistory", Constant.PIPE);
                    pubOrderParams.put("source", "893");
                    pubOrderParams.put("city", AppInfoUtil.getCityName(mContext));

                    if (!TextUtils.isEmpty(userid)) {
                        pubOrderParams.put("userid", userid);
                    } else {
                        pubOrderParams.put("userid", "0");
                    }
                    HttpRequestPubOrder();
                    break;
                default:
                    break;
            }
        }

        /****
         * 输入电话号码发单接口请求
         */
        private void HttpRequestPubOrder() {
            customWaitDialog.show();
            OKHttpUtil.post(Constant.PUB_ORDERS, pubOrderParams, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customWaitDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String result = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (jsonObject.getInt("error_code") == 0) {
                                    customWaitDialog.dismiss();
                                    MobclickAgent.onEvent(mContext, "click_find_decoration_array_design_for_me_conform_reservation_3.0");
                                    Intent intent = new Intent(mContext, ApplyforSuccessActivity.class);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
                                } else {
                                    customWaitDialog.dismiss();
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
}
