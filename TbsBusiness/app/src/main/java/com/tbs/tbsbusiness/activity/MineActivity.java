package com.tbs.tbsbusiness.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbs.tbsbusiness.R;
import com.tbs.tbsbusiness.base.BaseActivity;
import com.tbs.tbsbusiness.bean.EC;
import com.tbs.tbsbusiness.bean.Event;
import com.tbs.tbsbusiness.bean._User;
import com.tbs.tbsbusiness.config.Constant;
import com.tbs.tbsbusiness.customview.CustomDialog;
import com.tbs.tbsbusiness.util.GlideUtils;
import com.tbs.tbsbusiness.util.OKHttpUtil;
import com.tbs.tbsbusiness.util.SpUtil;
import com.tbs.tbsbusiness.util.ToastUtil;
import com.tbs.tbsbusiness.util.Util;

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
 * 用户信息
 */

public class MineActivity extends BaseActivity {
    @BindView(R.id.company_of_mine_icon_iv)
    ImageView companyOfMineIconIv;
    @BindView(R.id.company_of_mine_name_tv)
    TextView companyOfMineNameTv;
    @BindView(R.id.company_of_mine_huiyuan)
    ImageView companyOfMineHuiyuan;
    @BindView(R.id.company_of_mine_msg_rl)
    RelativeLayout companyOfMineMsgRl;
    @BindView(R.id.company_of_mine_wangdian_rl)
    RelativeLayout companyOfMineWangdianRl;
    @BindView(R.id.company_of_mine_kefu_phone_tv)
    TextView companyOfMineKefuPhoneTv;
    @BindView(R.id.company_of_mine_kefu_rl)
    RelativeLayout companyOfMineKefuRl;
    @BindView(R.id.company_of_mine_xiaochengxu_tv)
    TextView companyOfMineXiaochengxuTv;
    @BindView(R.id.company_of_mine_xiaochengxu_rl)
    RelativeLayout companyOfMineXiaochengxuRl;
    @BindView(R.id.company_of_mine_shezhi_rl)
    RelativeLayout companyOfMineShezhiRl;
    @BindView(R.id.company_of_mine_ll)
    LinearLayout companyOfMineLl;
    private Context mContext;
    private String TAG = "MineActivity";
    private Gson mGson;
    private _User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        mContext = this;
        Log.e(TAG, "==onCreate==");
        initEvent();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEvent(Event event) {
        switch (event.getCode()) {
            case EC.EventCode.USER_LOGIN_OUT:
                Log.e(TAG, "MineActivity退出====");
                finish();
                break;
        }
    }

    private void initEvent() {
        mGson = new Gson();
        companyOfMineXiaochengxuTv.setText("" + SpUtil.getApplets_name(mContext));
        companyOfMineKefuPhoneTv.setText("" + SpUtil.getCustom_service_tel(mContext));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "==onStart==");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "==onStop==");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "==onResume==");
        if (Util.isNetAvailable(mContext)) {
            //有网络的情况下网络获取数据
            getUserInfoFromNet();
        } else {
            //没有网络的情况下 获取本地数据
            initViewEvent();
        }
    }

    //网络获取用户信息并将数据写入本地
    private void getUserInfoFromNet() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("token", Util.getDateToken());
        param.put("uid", SpUtil.getUserId(mContext));
        param.put("device_id", SpUtil.getPushRegisterId(mContext));
        OKHttpUtil.post(Constant.GET_USER_INFO, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "链接失败==========" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = new String(response.body().string());
                Log.e(TAG, "获取数据成功========" + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String status = jsonObject.optString("status");
                    if (status.equals("200")) {
                        //将数据存储本地、更新数据
                        String data = jsonObject.optString("data");
                        saveUserInfo(data);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void saveUserInfo(String data) {
        _User mUser = mGson.fromJson(data, _User.class);
        SpUtil.setUserId(mContext, mUser.getUid());
        SpUtil.setCompany_id(mContext, mUser.getCompany_id());
        SpUtil.setCellphone(mContext, mUser.getCellphone());
        SpUtil.setCellphone_check(mContext, mUser.getCellphone_check());
        SpUtil.setWechat_check(mContext, mUser.getWechat_check());
        SpUtil.setNickname(mContext, mUser.getNickname());
        SpUtil.setIcon(mContext, mUser.getIcon());
        SpUtil.setGender(mContext, mUser.getGender());
        SpUtil.setOrder_count(mContext, String.valueOf(mUser.getOrder_count()));
        SpUtil.setGrade(mContext, String.valueOf(mUser.getGrade()));
        SpUtil.setCommunity(mContext, mUser.getCommunity());
        SpUtil.setCity_name(mContext, mUser.getCity_name());
        SpUtil.setProvince_name(mContext, mUser.getProvince_name());
        SpUtil.setNew_order_count(mContext, String.valueOf(mUser.getNew_order_count()));
        SpUtil.setNot_lf_order_count(mContext, String.valueOf(mUser.getNot_lf_order_count()));
        SpUtil.setLf_order_count(mContext, String.valueOf(mUser.getLf_order_count()));
        SpUtil.setIs_new_sms(mContext, String.valueOf(mUser.getIs_new_sms()));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //布局数据
                initViewEvent();
            }
        });
    }

    private void initViewEvent() {
        //设置用户的头像
        GlideUtils.glideLoader(mContext, SpUtil.getIcon(mContext),
                R.drawable.tbs_logo, R.drawable.tbs_logo, companyOfMineIconIv, 0);
        //设置用户的名称
        companyOfMineNameTv.setText("" + SpUtil.getNickname(mContext));
        //设置用户的等级
        //会员等级
        if (SpUtil.getGrade(mContext).equals("0")) {
            //业主或者非会员 不显示 会员的等级
            companyOfMineHuiyuan.setVisibility(View.GONE);
        } else if (SpUtil.getGrade(mContext).equals("1")) {
            //普通会员
            companyOfMineHuiyuan.setImageResource(R.drawable.huiyuan);
        } else if (SpUtil.getGrade(mContext).equals("2")) {
            //初级会员
            companyOfMineHuiyuan.setImageResource(R.drawable.chuji);
        } else if (SpUtil.getGrade(mContext).equals("3")) {
            //高级会员
            companyOfMineHuiyuan.setImageResource(R.drawable.gaoji);
        } else if (SpUtil.getGrade(mContext).equals("4")) {
            //钻石会员
            companyOfMineHuiyuan.setImageResource(R.drawable.zuanshi);
        } else if (SpUtil.getGrade(mContext).equals("5")) {
            //皇冠会员
            companyOfMineHuiyuan.setImageResource(R.drawable.huangguan);
        } else if (SpUtil.getGrade(mContext).equals("6")) {
            //svip
            companyOfMineHuiyuan.setImageResource(R.drawable.s_vip);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "==onRestart==");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "==onPause==");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "==onDestroy==");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setMessage("你确定退出吗？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    //退出前将数据上传
                                    finish();
                                    System.exit(0);
                                }
                            })
                    .setNegativeButton("再看看",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            builder.create().show();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.company_of_mine_msg_rl, R.id.company_of_mine_wangdian_rl,
            R.id.company_of_mine_kefu_rl, R.id.company_of_mine_xiaochengxu_rl,
            R.id.company_of_mine_shezhi_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.company_of_mine_msg_rl:
                //点击进入我的信息
                startActivity(new Intent(mContext, CoPresonerMsgActivity.class));
                break;
            case R.id.company_of_mine_wangdian_rl:
                //进入网店管理
                startActivity(new Intent(mContext, OnlineStoreActivity.class));
                break;
            case R.id.company_of_mine_kefu_rl:
                //进行客服电话的拨打
                showKefuPopWindow();
                break;
            case R.id.company_of_mine_xiaochengxu_rl:
                //复制小程序的名称
                copyWeChat();
                break;
            case R.id.company_of_mine_shezhi_rl:
                //进入设置页面
                startActivity(new Intent(mContext, SetingActivity.class));
                break;
        }
    }


    //开启客服电话
    private void showKefuPopWindow() {
        View popview = View.inflate(mContext, R.layout.popwindow_qqzixun, null);
        TextView quxiao_phone = (TextView) popview.findViewById(R.id.quxiao_phone);
        TextView phone_num = (TextView) popview.findViewById(R.id.phone_num);
        TextView open_phone = (TextView) popview.findViewById(R.id.open_phone);
        RelativeLayout pop_phone_zixun = (RelativeLayout) popview.findViewById(R.id.pop_phone_zixun);
        LinearLayout phone_pop_window_ll = popview.findViewById(R.id.phone_pop_window_ll);
        phone_pop_window_ll.setBackgroundColor(Color.parseColor("#ffffff"));
        phone_num.setText("" + "土拨鼠热线：" + SpUtil.getCustom_service_tel(mContext));
        final PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        //打电话
        open_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打电话
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + SpUtil.getCustom_service_tel(mContext)));
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

    //复制微信号
    private void copyWeChat() {
        ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("" + SpUtil.getApplets_name(mContext));
        Toast.makeText(mContext, "已复制，请到微信查看小程序", Toast.LENGTH_SHORT).show();
    }
}
