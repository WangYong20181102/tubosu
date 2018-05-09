package com.tbs.tobosutype.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.FendanComAdapter;
import com.tbs.tobosutype.adapter.OrderAdapter;
import com.tbs.tobosutype.adapter.OrderScheduleAdapter;
import com.tbs.tobosutype.base.BaseFragment;
import com.tbs.tobosutype.bean.EC;
import com.tbs.tobosutype.bean.Event;
import com.tbs.tobosutype.bean._OrderDetail;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.EventBusUtil;
import com.tbs.tobosutype.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Mr.Lin on 2018/3/26 10:58.
 * 订单详情页 3.7.2版本新增
 */

public class OrderDetailFragment extends BaseFragment {
    @BindView(R.id.new_order_frag_status_tv)
    TextView newOrderFragStatusTv;
    @BindView(R.id.new_order_frag_cus_name_tv)
    TextView newOrderFragCusNameTv;
    @BindView(R.id.new_order_frag_quyu_tv)
    TextView newOrderFragQuyuTv;
    @BindView(R.id.new_order_frag_mian_ji_tv)
    TextView newOrderFragMianJiTv;
    @BindView(R.id.new_order_frag_hu_xing_tv)
    TextView newOrderFragHuXingTv;
    @BindView(R.id.new_order_frag_lei_xing_tv)
    TextView newOrderFragLeiXingTv;
    @BindView(R.id.new_order_frag_xiao_qu_tv)
    TextView newOrderFragXiaoQuTv;
    @BindView(R.id.new_order_frag_di_zhi_tv)
    TextView newOrderFragDiZhiTv;
    @BindView(R.id.new_order_frag_fengge_tv)
    TextView newOrderFragFenggeTv;
    @BindView(R.id.new_order_frag_yao_shi_tv)
    TextView newOrderFragYaoShiTv;
    @BindView(R.id.new_order_frag_gai_zao_fang_tv)
    TextView newOrderFragGaiZaoFangTv;
    @BindView(R.id.new_order_frag_yu_suan_tv)
    TextView newOrderFragYuSuanTv;
    @BindView(R.id.new_order_frag_zhuang_xiu_lei_xing_tv)
    TextView newOrderFragZhuangXiuLeiXingTv;
    @BindView(R.id.new_order_frag_zhuang_xiu_fang_shi_tv)
    TextView newOrderFragZhuangXiuFangShiTv;
    @BindView(R.id.new_order_frag_liang_fang_shi_jian_tv)
    TextView newOrderFragLiangFangShiJianTv;
    @BindView(R.id.new_order_frag_zhuang_xiu_shi_jian_tv)
    TextView newOrderFragZhuangXiuShiJianTv;
    @BindView(R.id.new_order_frag_zhuang_xiu_xu_qiu_tv)
    TextView newOrderFragZhuangXiuXuQiuTv;
    @BindView(R.id.new_order_frag_fendan_gongsi_recycler)
    RecyclerView newOrderFragFendanGongsiRecycler;
    @BindView(R.id.new_order_frag_jindu_recycler)
    RecyclerView newOrderFragJinduRecycler;
    @BindView(R.id.new_order_frag_right_03)
    TextView newOrderFragRight03;
    @BindView(R.id.new_order_frag_right_02)
    TextView newOrderFragRight02;
    @BindView(R.id.new_order_frag_right_01)
    TextView newOrderFragRight01;
    Unbinder unbinder;
    @BindView(R.id.fendan_gongsi_ll)
    LinearLayout fendanGongsiLl;
    private Context mContext;
    private String TAG = "OrderDetailFragment";
    private String mOrderId;
    private Gson mGson;
    private GridLayoutManager mGridLayoutManager;//分单公司的布局
    private LinearLayoutManager mLinearLayoutManager;//订单进度的布局管理者
    private FendanComAdapter mFendanComAdapter;
    private OrderScheduleAdapter mOrderScheduleAdapter;
    private _OrderDetail mOrderDetail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            this.mOrderId = args.getString("mOrderId");
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    public OrderDetailFragment() {

    }

    public static OrderDetailFragment newInstance(String mOrderId) {
        OrderDetailFragment orderDetailFragment = new OrderDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mOrderId", mOrderId);
        orderDetailFragment.setArguments(bundle);
        return orderDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, null);
        unbinder = ButterKnife.bind(this, view);
        mContext = getActivity();
        initViewEvent();
        return view;
    }

    private void initViewEvent() {
        mGson = new Gson();
        mGridLayoutManager = new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false);
        newOrderFragFendanGongsiRecycler.setLayoutManager(mGridLayoutManager);
        newOrderFragFendanGongsiRecycler.setOnTouchListener(onTouchListener);

        mLinearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        newOrderFragJinduRecycler.setLayoutManager(mLinearLayoutManager);
        newOrderFragJinduRecycler.setOnTouchListener(onTouchListener);
        HttpGetOrderDetail();
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    private void HttpGetOrderDetail() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", mOrderId);
        OKHttpUtil.post(Constant.GET_ORDER_DETAIL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获取数据失败============" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
//                    Log.e(TAG, "获取数据成功=========参数==========" + mOrderId + "============" + json);
                    if (status.equals("200")) {
                        final String data = jsonObject.optString("data");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initViewFromNetDate(data);
                            }
                        });
                    } else if (status.equals("0")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //将数据布局到页面
    private void initViewFromNetDate(String data) {
        mOrderDetail = mGson.fromJson(data, _OrderDetail.class);
        //订单状态
        if (mOrderDetail.getState().equals("1")) {
            newOrderFragStatusTv.setText("" + "新订单");
        } else if (mOrderDetail.getState().equals("2")) {
            newOrderFragStatusTv.setText("" + "未量房");
        } else if (mOrderDetail.getState().equals("3")) {
            newOrderFragStatusTv.setText("" + "已量房");
        } else if (mOrderDetail.getState().equals("4")) {
            newOrderFragStatusTv.setText("" + "已签单");
        } else if (mOrderDetail.getState().equals("5")) {
            newOrderFragStatusTv.setText("" + "未签单");
        }

        //业主名称+电话
        newOrderFragCusNameTv.setText("" + mOrderDetail.getOwner_name() + " " + mOrderDetail.getCellphone());
        //区域
        newOrderFragQuyuTv.setText("" + mOrderDetail.getDistrict_name());
        //面积
        newOrderFragMianJiTv.setText("" + mOrderDetail.getArea());
        //户型
        newOrderFragHuXingTv.setText("" + mOrderDetail.getLayout_name());
        //房屋类型
        newOrderFragLeiXingTv.setText("" + mOrderDetail.getHouse_type());
        //小区
        newOrderFragXiaoQuTv.setText("" + mOrderDetail.getCommunity_name());
        //地址
        newOrderFragDiZhiTv.setText("" + mOrderDetail.getAddress());
        //风格
        newOrderFragFenggeTv.setText("" + mOrderDetail.getDecorate_style());
        //是否有钥匙
        if (mOrderDetail.getIs_get_key().equals("1")) {
            newOrderFragYaoShiTv.setText("有");
        } else {
            newOrderFragYaoShiTv.setText("无");
        }
        //是否是改造房
        if (mOrderDetail.getIs_old_house().equals("1")) {
            newOrderFragGaiZaoFangTv.setText("是");
        } else {
            newOrderFragGaiZaoFangTv.setText("否");
        }
        //预算
        newOrderFragYuSuanTv.setText("" + mOrderDetail.getBudget() + "万");
        //装修类型
        newOrderFragZhuangXiuLeiXingTv.setText("" + mOrderDetail.getDecorate_type());
        //装修方式
        newOrderFragZhuangXiuFangShiTv.setText("" + mOrderDetail.getDecorate_mode());
        //量房时间
        newOrderFragLiangFangShiJianTv.setText("" + mOrderDetail.getLf_time());
        //装修时间
        newOrderFragZhuangXiuShiJianTv.setText("" + mOrderDetail.getDecorate_time());
        //装修需求
        newOrderFragZhuangXiuXuQiuTv.setText("" + mOrderDetail.getOrder_demand());
        //设置分单公司
        if (mOrderDetail.getCompany_list().isEmpty()) {
            //分单公司数量为空 分单公司布局页面
            fendanGongsiLl.setVisibility(View.GONE);
        } else {
            mFendanComAdapter = new FendanComAdapter(mContext, mOrderDetail.getCompany_list());
            newOrderFragFendanGongsiRecycler.setAdapter(mFendanComAdapter);
            mFendanComAdapter.notifyDataSetChanged();
        }
        //设置订单进度
        if (!mOrderDetail.getOrder_track().isEmpty()) {
            mOrderScheduleAdapter = new OrderScheduleAdapter(mContext, mOrderDetail.getOrder_track());
            newOrderFragJinduRecycler.setAdapter(mOrderScheduleAdapter);
            mOrderScheduleAdapter.notifyDataSetChanged();
        }
        //设置底部按钮显示
        if (mOrderDetail.getState().equals("1")) {
            //新订单
            newOrderFragRight01.setVisibility(View.VISIBLE);
            newOrderFragRight02.setVisibility(View.VISIBLE);
            newOrderFragRight03.setVisibility(View.VISIBLE);

            newOrderFragRight01.setText("确认量房");
            newOrderFragRight01.setTextColor(Color.parseColor("#ff6c14"));
            GradientDrawable gradientDrawable01 = new GradientDrawable();
            gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable01.setCornerRadius(50);//角度
            gradientDrawable01.setStroke(1, Color.parseColor("#ff6c14"));
            newOrderFragRight01.setBackgroundDrawable(gradientDrawable01);

            newOrderFragRight02.setText("量房失败");
            newOrderFragRight02.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable02 = new GradientDrawable();
            gradientDrawable02.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable02.setCornerRadius(50);//角度
            gradientDrawable02.setStroke(1, Color.parseColor("#d8d8d8"));
            newOrderFragRight02.setBackgroundDrawable(gradientDrawable02);

            newOrderFragRight03.setText("联系业主");
            newOrderFragRight03.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable03 = new GradientDrawable();
            gradientDrawable03.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable03.setCornerRadius(50);//角度
            gradientDrawable03.setStroke(1, Color.parseColor("#d8d8d8"));
            newOrderFragRight03.setBackgroundDrawable(gradientDrawable03);

        } else if (mOrderDetail.getState().equals("2")) {
            //未量房
            newOrderFragRight01.setVisibility(View.VISIBLE);
            newOrderFragRight02.setVisibility(View.VISIBLE);
            newOrderFragRight03.setVisibility(View.VISIBLE);

            newOrderFragRight01.setText("确认量房");
            newOrderFragRight01.setTextColor(Color.parseColor("#ff6c14"));
            GradientDrawable gradientDrawable01 = new GradientDrawable();
            gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable01.setCornerRadius(50);//角度
            gradientDrawable01.setStroke(1, Color.parseColor("#ff6c14"));
            newOrderFragRight01.setBackgroundDrawable(gradientDrawable01);

            newOrderFragRight02.setText("量房失败");
            newOrderFragRight02.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable02 = new GradientDrawable();
            gradientDrawable02.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable02.setCornerRadius(50);//角度
            gradientDrawable02.setStroke(1, Color.parseColor("#d8d8d8"));
            newOrderFragRight02.setBackgroundDrawable(gradientDrawable02);

            newOrderFragRight03.setText("联系业主");
            newOrderFragRight03.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable03 = new GradientDrawable();
            gradientDrawable03.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable03.setCornerRadius(50);//角度
            gradientDrawable03.setStroke(1, Color.parseColor("#d8d8d8"));
            newOrderFragRight03.setBackgroundDrawable(gradientDrawable03);
        } else if (mOrderDetail.getState().equals("3")) {
            //已量房
            newOrderFragRight01.setVisibility(View.VISIBLE);
            newOrderFragRight02.setVisibility(View.VISIBLE);
            newOrderFragRight03.setVisibility(View.VISIBLE);
            //右一的按钮
            newOrderFragRight01.setText("确认签单");
            newOrderFragRight01.setTextColor(Color.parseColor("#ff6c14"));
            GradientDrawable gradientDrawable01 = new GradientDrawable();
            gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable01.setCornerRadius(50);//角度
            gradientDrawable01.setStroke(1, Color.parseColor("#ff6c14"));//边框以及颜色
            newOrderFragRight01.setBackgroundDrawable(gradientDrawable01);
            //中间的按钮
            newOrderFragRight02.setText("未签单");
            newOrderFragRight02.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable02 = new GradientDrawable();
            gradientDrawable02.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable02.setCornerRadius(50);//角度
            gradientDrawable02.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
            newOrderFragRight02.setBackgroundDrawable(gradientDrawable02);
            //左侧按钮
            newOrderFragRight03.setText("联系业主");
            newOrderFragRight03.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable03 = new GradientDrawable();
            gradientDrawable03.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable03.setCornerRadius(50);//角度
            gradientDrawable03.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
            newOrderFragRight03.setBackgroundDrawable(gradientDrawable03);

        } else if (mOrderDetail.getState().equals("4")) {
            //已签单
            newOrderFragRight01.setVisibility(View.VISIBLE);
            newOrderFragRight02.setVisibility(View.GONE);
            newOrderFragRight03.setVisibility(View.GONE);
            //右一的按钮
            newOrderFragRight01.setText("联系业主");
            newOrderFragRight01.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable01 = new GradientDrawable();
            gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable01.setCornerRadius(50);//角度
            gradientDrawable01.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
            newOrderFragRight01.setBackgroundDrawable(gradientDrawable01);

        } else if (mOrderDetail.getState().equals("5")) {
            //未签单
            newOrderFragRight01.setVisibility(View.VISIBLE);
            newOrderFragRight02.setVisibility(View.GONE);
            newOrderFragRight03.setVisibility(View.GONE);
            //右一的按钮
            newOrderFragRight01.setText("反馈");
            newOrderFragRight01.setTextColor(Color.parseColor("#666666"));
            GradientDrawable gradientDrawable01 = new GradientDrawable();
            gradientDrawable01.setColor(Color.parseColor("#ffffff"));//背景
            gradientDrawable01.setCornerRadius(50);//角度
            gradientDrawable01.setStroke(1, Color.parseColor("#d8d8d8"));//边框以及颜色
            newOrderFragRight01.setBackgroundDrawable(gradientDrawable01);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.new_order_frag_right_03, R.id.new_order_frag_right_02, R.id.new_order_frag_right_01})
    public void onViewClickedOnOrderDetailFrag(View view) {
        switch (view.getId()) {
            case R.id.new_order_frag_right_03:
                //最左侧按钮的处理模式
                if (mOrderDetail.getState().equals("1")) {
//                    Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mOrderDetail.getCellphone())) {
                        showCallPhonePopWindow(mOrderDetail.getCellphone());
                    } else {
                        Toast.makeText(mContext, "该业主未填写电话", Toast.LENGTH_SHORT).show();
                    }
                } else if (mOrderDetail.getState().equals("2")) {
                    //未量房处理模式
//                    Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mOrderDetail.getCellphone())) {
                        showCallPhonePopWindow(mOrderDetail.getCellphone());
                    } else {
                        Toast.makeText(mContext, "该业主未填写电话", Toast.LENGTH_SHORT).show();
                    }
                } else if (mOrderDetail.getState().equals("3")) {
                    //已量房处理模式
//                    Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mOrderDetail.getCellphone())) {
                        showCallPhonePopWindow(mOrderDetail.getCellphone());
                    } else {
                        Toast.makeText(mContext, "该业主未填写电话", Toast.LENGTH_SHORT).show();
                    }
                } else if (mOrderDetail.getState().equals("4")) {
                    //未签单处理模式 没有这个按钮
                } else if (mOrderDetail.getState().equals("5")) {
                    //已签单处理模式 没有这个按钮
                }
                break;
            case R.id.new_order_frag_right_02:
                //中间按处理模式
                if (mOrderDetail.getState().equals("1")) {
//                    Toast.makeText(mContext, "量房失败", Toast.LENGTH_SHORT).show();
//                    showLiangfangshibai(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,2,mOrderDetail.getState());

                } else if (mOrderDetail.getState().equals("2")) {
                    //未量房处理模式
//                    Toast.makeText(mContext, "量房失败", Toast.LENGTH_SHORT).show();
//                    showLiangfangshibai(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,2,mOrderDetail.getState());
                } else if (mOrderDetail.getState().equals("3")) {
                    //已量房处理模式
//                    Toast.makeText(mContext, "未签单", Toast.LENGTH_SHORT).show();
//                    showWeiQianDanPop(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,6,mOrderDetail.getState());
                } else if (mOrderDetail.getState().equals("4")) {
                    //已签单处理模式 没有这个按钮
                } else if (mOrderDetail.getState().equals("5")) {
                    //未签单处理模式  没有这个按钮
                }
                break;
            case R.id.new_order_frag_right_01:
                //右侧第一个按钮处理模式
                if (mOrderDetail.getState().equals("1")) {
//                    Toast.makeText(mContext, "确认量房", Toast.LENGTH_SHORT).show();
//                    showQuedingLiangfang(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,3,mOrderDetail.getState());
                } else if (mOrderDetail.getState().equals("2")) {
                    //未量房处理模式
//                    Toast.makeText(mContext, "确认量房", Toast.LENGTH_SHORT).show();
//                    showQuedingLiangfang(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,3,mOrderDetail.getState());
                } else if (mOrderDetail.getState().equals("3")) {
                    //已量房处理模式
//                    showQuedingqiandan(mOrderId, mOrderDetail.getState());
                    showReasonPop(mOrderId,4,mOrderDetail.getState());
//                    Toast.makeText(mContext, "确认签单", Toast.LENGTH_SHORT).show();
                } else if (mOrderDetail.getState().equals("4")) {
                    //已签单处理模式
//                    Toast.makeText(mContext, "联系业主", Toast.LENGTH_SHORT).show();
                    if (!TextUtils.isEmpty(mOrderDetail.getCellphone())) {
                        showCallPhonePopWindow(mOrderDetail.getCellphone());
                    } else {
                        Toast.makeText(mContext, "该业主未填写电话", Toast.LENGTH_SHORT).show();
                    }
                } else if (mOrderDetail.getState().equals("5")) {
                    //未签单处理模式
//                    Toast.makeText(mContext, "反馈", Toast.LENGTH_SHORT).show();
                    EventBusUtil.sendEvent(new Event(EC.EventCode.CHANGE_ORDER_DETAIL_ACTIVITY_TO_FEEDBACK_VIEW));
                }
                break;
        }
    }

    //显示未签单pop
    private void showWeiQianDanPop(final String id, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_weiqiandan, null);
        TextView quxiao_weiqiandan = (TextView) popview.findViewById(R.id.quxiao_weiqiandan);
        TextView queding_weiqiandan = (TextView) popview.findViewById(R.id.queding_weiqiandan);
        RelativeLayout queding_weiqiandan_pop_rl = (RelativeLayout) popview.findViewById(R.id.queding_weiqiandan_pop_rl);
        LinearLayout weiqiandan_ll = popview.findViewById(R.id.weiqiandan_ll);
        weiqiandan_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定未签单
        queding_weiqiandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  确定未签单 操作数据
//                HttpChangeOrderState(id, 6, order_state);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_weiqiandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        queding_weiqiandan_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //显示确定签单
    private void showQuedingqiandan(final String id, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_quedingqiandan, null);
        TextView quxiao_qiandan = (TextView) popview.findViewById(R.id.quxiao_qiandan);
        TextView queding_qiandan = (TextView) popview.findViewById(R.id.queding_qiandan);
        RelativeLayout queding_qiandan_pop_rl = (RelativeLayout) popview.findViewById(R.id.queding_qiandan_pop_rl);
        LinearLayout qiandan_ll = popview.findViewById(R.id.qiandan_ll);
        qiandan_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定未签单
        queding_qiandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 确定签单 操作数据
//                HttpChangeOrderState(id, 4, order_state);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_qiandan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        queding_qiandan_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    // TODO: 2018/5/4  显示输入原因的弹框
    private void showReasonPop(final String id, final int oprate_type, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_order_reason, null);
        //关闭按钮
        RelativeLayout order_reason_pop_close_rl = popview.findViewById(R.id.order_reason_pop_close_rl);
        //确定提交按钮
        TextView order_reason_pop_ok_btn = (TextView) popview.findViewById(R.id.order_reason_pop_ok_btn);
        //整个黑框
        RelativeLayout order_reason_pop_rl = (RelativeLayout) popview.findViewById(R.id.order_reason_pop_rl);
        //整个输入原因的框
        LinearLayout order_reason_pop_ll = popview.findViewById(R.id.order_reason_pop_ll);
        //输入框
        final EditText order_reason_pop_input_et = popview.findViewById(R.id.order_reason_pop_input_et);

        order_reason_pop_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定
        order_reason_pop_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(order_reason_pop_input_et.getText().toString())) {
                    HttpChangeOrderState(id, oprate_type, order_state, order_reason_pop_input_et.getText().toString());
                    popupWindow.dismiss();
                } else {
                    Toast.makeText(mContext, "请输入反馈信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //取消
        order_reason_pop_close_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        order_reason_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);
    }

    //显示量房失败
    private void showLiangfangshibai(final String id, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_liangfangshibai, null);
        TextView quxiao_liangfanshibai = (TextView) popview.findViewById(R.id.quxiao_liangfanshibai);
        TextView queding_liangfangshibai = (TextView) popview.findViewById(R.id.queding_liangfangshibai);
        RelativeLayout liangfang_shibai_pop_rl = (RelativeLayout) popview.findViewById(R.id.liangfang_shibai_pop_rl);
        LinearLayout liangfangshibai_ll = popview.findViewById(R.id.liangfangshibai_ll);
        liangfangshibai_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定量房
        queding_liangfangshibai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HttpChangeOrderState(id, 2, order_state);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_liangfanshibai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        liangfang_shibai_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //拨打业主电话
    private void showCallPhonePopWindow(final String phoneNum) {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        TextView phone_num = popview.findViewById(R.id.phone_num);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //设置电话号码
        phone_num.setText("" + phoneNum);
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

    //显示确定量房窗口
    private void showQuedingLiangfang(final String id, final String order_state) {
        View popview = View.inflate(mContext, R.layout.pop_quedingliangfang, null);
        TextView quxiao_liangfang = (TextView) popview.findViewById(R.id.quxiao_liangfang);
        TextView queding_liangfang = (TextView) popview.findViewById(R.id.queding_liangfang);
        RelativeLayout queding_liangfang_pop_rl = (RelativeLayout) popview.findViewById(R.id.queding_liangfang_pop_rl);
        LinearLayout queding_liangfang_ll = popview.findViewById(R.id.queding_liangfang_ll);
        queding_liangfang_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //确定量房
        queding_liangfang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改变订单状态
//                HttpChangeOrderState(id, 3, order_state);
                popupWindow.dismiss();
            }
        });
        //取消
        quxiao_liangfang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        queding_liangfang_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    //订单发生变化弹窗
    private void showOrderChangePopWindow(String msg) {
        View popview = View.inflate(mContext, R.layout.pop_order_change, null);
        //确定按钮
        TextView queding_order_change = (TextView) popview.findViewById(R.id.queding_order_change);
        //显示提示
        TextView order_change_msg_tv = popview.findViewById(R.id.order_change_msg_tv);
        //整个面板
        RelativeLayout order_change_pop_rl = (RelativeLayout) popview.findViewById(R.id.order_change_pop_rl);
        LinearLayout order_change_ll = popview.findViewById(R.id.order_change_ll);
        order_change_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //设置电话号码
        order_change_msg_tv.setText("" + msg);
        //确定按钮
        queding_order_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新数据
                HttpGetOrderDetail();
                popupWindow.dismiss();
            }
        });
        order_change_pop_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(popview, Gravity.CENTER, 0, 0);
    }

    /**
     * 改变订单的状态
     * 操作类型operate_type：1：查看；2：量房失败；3：确定量房；4：未签单；5：已签单
     * 当前的订单状态：1：查看；2：量房失败；3：确定量房；4：未签单；5：已签单
     */
    private void HttpChangeOrderState(String id, final int operate_type, String order_state, String content) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("id", id);
        param.put("state", order_state);
        param.put("operate_type", operate_type);
        param.put("content", content);
        OKHttpUtil.post(Constant.CHANGE_ORDER_STATE, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "连接失败=========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "连接成功=============" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    final String msg = jsonObject.optString("msg");
                    if (status.equals("200")) {
                        // TODO: 2018/3/27 操作成功 刷新当前页面数据  通知外边页面数据更新
                        //刷新数据
                        HttpGetOrderDetail();
                        //提示已经修改了订单状态
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBusUtil.sendEvent(new Event(EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA));
                                Toast.makeText(mContext, "订单状态已修改！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (status.equals("205")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBusUtil.sendEvent(new Event(EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA));
                                showOrderChangePopWindow(msg);
                            }
                        });

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EventBusUtil.sendEvent(new Event(EC.EventCode.NOTE_ORDER_FRAGMENT_UPDATE_DATA));
                                Toast.makeText(mContext, "操作失败~", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
