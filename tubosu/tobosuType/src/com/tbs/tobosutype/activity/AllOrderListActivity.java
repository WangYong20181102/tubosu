package com.tbs.tobosutype.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CallDialogCompany;
import com.tbs.tobosutype.customview.DropPopWindow;
import com.tbs.tobosutype.customview.LfPwdPopupWindow;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全部订单列表页面
 * <p>
 * [未量房] or [已量房] or [已签单]   订单列表
 *
 * @author dec
 */

public class AllOrderListActivity extends Activity implements IXListViewListener {
    private static final String TAG = AllOrderListActivity.class.getSimpleName();

    /***
     * 未量房
     *     未看订单 -->  0
     */
    private static final String WEI_LIANG_FANG_NOT_SEE_ORDER = "0";

    private Context mContext;

    private ImageView allorder_back;

    /**
     * 全部订单标题
     */
    private TextView allorder_title;

    private ImageView allorder_down;

    private XListView allorder_listview;

    private ImageView iv_empty_orderdata;

    private Window window;
    private LayoutParams params;
    private List<HashMap<String, Object>> allOrderList = new ArrayList<HashMap<String, Object>>();
    private AllOrderAdapter adapter;
    private RequestParams orderParams;
    private String psw = "1";
    private String token;
    private RelativeLayout all_re_banner;
    private int page = 1;

    /***
     *  kind
     *  	0 全部订单
     *  	1 已量房
     *  	2 未量房
     *  	3 已签单
     *  	4 退款
     * */
    private String kind;

    /***所有订单接口*/
    private String allOrderUrl = AllConstants.TOBOSU_URL + "tapp/order/com_order_list";

    private HashMap<String, Object> popMap = new HashMap<String, Object>();

    private String lfangUrl = AllConstants.TOBOSU_URL + "tapp/order/lfang";
    private LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		AppInfoUtil.setActivityTheme(this, R.color.whole_color_theme);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_allorder_list);

        mContext = AllOrderListActivity.this;

        initView();
        initViewAndEvent();
        initAdapter();
    }


    private void initView() {
        all_re_banner = (RelativeLayout) findViewById(R.id.all_re_banner);
        loading = (LinearLayout) findViewById(R.id.amin_allorder_loading);
        loading.setVisibility(View.VISIBLE);

        allorder_back = (ImageView) findViewById(R.id.allorder_back);
        allorder_down = (ImageView) findViewById(R.id.allorder_down);
        allorder_listview = (XListView) findViewById(R.id.allorder_listview);
        allorder_title = (TextView) findViewById(R.id.allorder_title);

        iv_empty_orderdata = (ImageView) findViewById(R.id.iv_empty_orderdata);
        iv_empty_orderdata.setVisibility(View.GONE);

        allorder_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        all_re_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }


    private void initViewAndEvent() {
        token = AppInfoUtil.getToekn(getApplicationContext());
        kind = getIntent().getExtras().getString("kind");

        if (!TextUtils.isEmpty(token) && AllConstants.checkNetwork(mContext)) {
            initParams();
            requestOrderPost();
        }


        if (kind.equals("2")) {
            allorder_title.setText("未量房");
//			allorder_down.setVisibility(View.GONE);
        } else if (kind.equals("3")) {
            allorder_title.setText("已签单");
        } else if (kind.equals("1")) {
            allorder_title.setText("已量房");
        } else if (kind.equals("0")) {
            allorder_title.setText("全部订单");
        }


        allorder_title.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final DropPopWindow addPopWindow = new DropPopWindow(AllOrderListActivity.this);
                addPopWindow.showPopupWindow(v);
                View view = addPopWindow.getContentView();
                TextView item_allorder = (TextView) view.findViewById(R.id.item_allorder);
                TextView item_allorder_notlfang = (TextView) view.findViewById(R.id.item_allorder_notlfang);
                TextView item_allorder_lfang = (TextView) view.findViewById(R.id.item_allorder_lfang);
                TextView item_allorder_qd = (TextView) view.findViewById(R.id.item_allorder_qd);
                TextView item_allorder_notqd = (TextView) view.findViewById(R.id.item_allorder_notqd);
                int lfangNum = Integer.parseInt(popMap.get("lfang").toString());
                int notlfangNum = Integer.parseInt(popMap.get("notlfang").toString());//FIXME
                int qdNum = Integer.parseInt(popMap.get("qd").toString());
                int notqdNum = Integer.parseInt(popMap.get("notqd").toString());
                int count = lfangNum + notlfangNum + qdNum + notqdNum;
                item_allorder.setText("全部订单(" + count + ")");
                item_allorder_notlfang.setText("未量房(" + notlfangNum + ")");
                item_allorder_lfang.setText("已量房(" + lfangNum + ")");
                item_allorder_qd.setText("已签单(" + qdNum + ")");
                item_allorder_notqd.setText("未签单(" + notqdNum + ")");

                //全部订单标记
                if (count != 0) {
                    item_allorder.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            kind = "0";
                            allorder_title.setText("全部订单");
                            onRefresh();
                            addPopWindow.showPopupWindow(v);
                        }
                    });
                }


                // 未量房
                if (notlfangNum != 0) {
                    item_allorder_notlfang.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            allorder_title.setText("未量房");
                            kind = "2";
                            onRefresh();
                            addPopWindow.showPopupWindow(v);
                        }
                    });
                }

                // 已量房
                if (lfangNum != 0) {
                    item_allorder_lfang.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            allorder_title.setText("已量房");
                            kind = "1";
                            onRefresh();
                            addPopWindow.showPopupWindow(v);
                        }
                    });
                }

                //已签单
                if (qdNum != 0) {
                    item_allorder_qd.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            allorder_title.setText("已签单");
                            kind = "3";
                            onRefresh();
                            addPopWindow.showPopupWindow(v);
                        }
                    });
                }

                // 未签单
                if (notqdNum != 0) {
                    item_allorder_notqd.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            allorder_title.setText("未签单");
                            kind = "4";
                            onRefresh();
                            addPopWindow.showPopupWindow(v);
                        }
                    });
                }
            }
        });


    }


    /***
     * 页面的订单适配器 -- AllOrderAdapter
     */
    private void initAdapter() {

        if (allOrderList.size() == 0) {
//			iv_empty_orderdata.setVisibility(View.VISIBLE);
        } else {
            iv_empty_orderdata.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
        }

        adapter = new AllOrderAdapter(mContext, allOrderList);
        allorder_listview.setAdapter(adapter);
        allorder_listview.setPullLoadEnable(true);
        allorder_listview.setXListViewListener(this);
        allorder_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));// 去除默认的黄色背景


    }


    private void initParams() {
        orderParams = AppInfoUtil.getPublicParams(getApplicationContext());
        orderParams.put("token", token);
        orderParams.put("page", page);
        orderParams.put("psw", psw);
        orderParams.put("kind", kind);
    }

    /***
     * 数据适配器
     * @author dec
     *
     */
    class AllOrderAdapter extends BaseAdapter {
        private Context context;
        private List<HashMap<String, Object>> list;

        public AllOrderAdapter(Context context, List<HashMap<String, Object>> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_allorder_listview, null);
                mHolder.item_allorder_date = (TextView) convertView.findViewById(R.id.item_allorder_date);

                mHolder.item_allorder_measure = (ImageView) convertView.findViewById(R.id.item_allorder_measure);
                mHolder.item_allorder_owner = (TextView) convertView.findViewById(R.id.item_allorder_owner);
                mHolder.item_allorder_district = (TextView) convertView.findViewById(R.id.item_allorder_district);
                mHolder.item_allorder_budget = (TextView) convertView.findViewById(R.id.item_allorder_budget);
                mHolder.item_allorder_area = (TextView) convertView.findViewById(R.id.item_allorder_area);
                mHolder.item_allorder_id = (TextView) convertView.findViewById(R.id.item_allorder_id);
                mHolder.item_ollorder_give = (ImageView) convertView.findViewById(R.id.item_ollorder_give);
                mHolder.item_allorder_submit = (TextView) convertView.findViewById(R.id.item_allorder_submit);
                mHolder.item_allorder_phone = (TextView) convertView.findViewById(R.id.item_allorder_phone);
                mHolder.my_allorder_dot_red = (ImageView) convertView.findViewById(R.id.my_allorder_dot_red);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            // 是否已经看了
            if (list.get(position).get("issee").equals(WEI_LIANG_FANG_NOT_SEE_ORDER)) {
                mHolder.my_allorder_dot_red.setVisibility(View.VISIBLE);
            } else {
                mHolder.my_allorder_dot_red.setVisibility(View.GONE);
            }
            String str = list.get(position).get("order_state").toString();
            if (str.equals("未量房")) {
                mHolder.item_allorder_measure.setBackground(getResources().getDrawable(R.drawable.img_not_lf));
            } else if (str.equals("已量房")) {
                mHolder.item_allorder_measure.setBackground(getResources().getDrawable(R.drawable.img_lf));
            } else if (str.equals("已签单")) {
                mHolder.item_allorder_measure.setBackground(getResources().getDrawable(R.drawable.img_qd));
            } else if (str.equals("未签单")) {
                mHolder.item_allorder_measure.setBackground(getResources().getDrawable(R.drawable.img_not_qd));
            }
            if ("".equals(list.get(position).get("contact").toString())) {
                mHolder.item_allorder_owner.setText("--");
            } else {
                mHolder.item_allorder_owner.setText(list.get(position).get("contact").toString());
            }

            mHolder.item_allorder_district.setText(list.get(position).get("qu").toString());
            if ("".equals(list.get(position).get("orderprice").toString())) {
                mHolder.item_allorder_budget.setText("0 万");
            } else {
                mHolder.item_allorder_budget.setText(list.get(position).get("orderprice").toString() + "万");
            }
            if ("".equals(list.get(position).get("housearea").toString())) {
                mHolder.item_allorder_area.setText("0 ㎡");
            } else {
                mHolder.item_allorder_area.setText(list.get(position).get("housearea").toString() + "㎡");
            }
            mHolder.item_allorder_id.setText("订单编号 : " + list.get(position).get("orderid").toString());
            mHolder.item_allorder_date.setText(list.get(position).get("addtime").toString() + " 分单");
            if (list.get(position).get("is_give").toString().equals("1")) {
                mHolder.item_ollorder_give.setVisibility(View.VISIBLE);
            } else {
                mHolder.item_ollorder_give.setVisibility(View.GONE);
            }
            if ("1".equals(list.get(position).get("islfang").toString())) {
                mHolder.item_allorder_submit.setVisibility(View.INVISIBLE);
            } else {
                mHolder.item_allorder_submit.setVisibility(View.VISIBLE);
                mHolder.item_allorder_submit.setText("确认量房");
                mHolder.item_allorder_submit.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String orderid = list.get(position).get("orderid").toString();
                        operLf(orderid);
                    }
                });
            }

            mHolder.item_allorder_phone.setText("电话沟通");
            mHolder.item_allorder_phone.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    String cellphone = "";
                    String tel = list.get(position).get("phone").toString();
                    CallDialogCompany callDialog = new CallDialogCompany(AllOrderListActivity.this, R.style.callDialogTheme, cellphone, tel);
                    window = callDialog.getWindow();
                    params = window.getAttributes();
                    params.width = LayoutParams.MATCH_PARENT;
                    window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    callDialog.show();
                }
            });


            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(mContext, AllOrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(position).get("orderid").toString());
//					bundle.putString("issee",WEI_LIANG_FANG_NOT_SEE_ORDER);
                    detailIntent.putExtras(bundle);
                    startActivity(detailIntent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView item_allorder_date;
            private TextView item_allorder_id;
            private ImageView item_allorder_measure;
            private TextView item_allorder_owner;
            private TextView item_allorder_district;
            private TextView item_allorder_budget;
            private TextView item_allorder_area;
            private TextView item_allorder_submit;
            private TextView item_allorder_phone;
            private ImageView item_ollorder_give;
            private ImageView my_allorder_dot_red;

        }

    }

    private List<HashMap<String, Object>> jsonToAllOrderList(String jsonString) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            JSONObject object1 = new JSONObject(jsonString);
            int error_code = object1.getInt("error_code");
            if (error_code == 0) {
                JSONObject dataObject = object1.getJSONObject("data");
                JSONArray orderArray = dataObject.getJSONArray("orders");
                for (int i = 0; i < orderArray.length(); i++) {
                    HashMap<String, Object> dataMap = new HashMap<String, Object>();
                    JSONObject object = orderArray.getJSONObject(i);
                    dataMap.put("orderid", object.get("orderid").toString());
                    dataMap.put("addtime", object.get("addtime").toString());
                    dataMap.put("is_give", object.get("is_give").toString());
                    dataMap.put("contact", object.get("contact").toString());
                    dataMap.put("qu", object.get("qu").toString());
                    dataMap.put("orderprice", object.get("orderprice").toString());
                    dataMap.put("housearea", object.get("housearea").toString());
                    dataMap.put("isbook", object.get("isbook").toString());
                    dataMap.put("islfang", object.get("islfang").toString());
                    dataMap.put("lftime", object.get("lftime").toString());
                    dataMap.put("tradetime", object.get("tradetime").toString());
                    dataMap.put("issee", object.get("issee").toString());
                    dataMap.put("phone", object.get("phone").toString());
                    dataMap.put("order_state", object.get("order_state").toString());
                    dataMap.put("issee", object.get("issee"));
                    list.add(dataMap);
                }
                return list;
            } else {
                // FIXME
                return null;

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "--jsonToAllOrderList--解析有问题--");
        }

        return null;
    }

    private HashMap<String, Object> jsonToPopWindowList(String jsonString) {
        HashMap<String, Object> countMap = new HashMap<String, Object>();
        try {

            JSONObject object1 = new JSONObject(jsonString);
            int error_code = object1.getInt("error_code");
            if (error_code == 0) {
                JSONObject dataObject = object1.getJSONObject("data");
                JSONArray countArray = dataObject.getJSONArray("count");

                countMap.put("lfang", countArray.get(0));
                countMap.put("notlfang", countArray.get(1));
                countMap.put("qd", countArray.get(2));
                countMap.put("notqd", countArray.get(3));
            } else {
                countMap.put("lfang", "0");
                countMap.put("notlfang", "0");
                countMap.put("qd", "0");
                countMap.put("notqd", "0");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "--jsonToPopWindowList--解析有问题--");
        }

        return countMap;
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (!TextUtils.isEmpty(token) && AllConstants.checkNetwork(mContext)) {
            initParams();
            requestOrderPost();
        }
    }

    @Override
    public void onLoadMore() {
        page++;
        if (!TextUtils.isEmpty(token) && AllConstants.checkNetwork(mContext)) {
            initParams();
            requestOrderPost();
        }
    }

    private void onLoad() {
        loading.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
        allorder_listview.stopRefresh();
        allorder_listview.stopLoadMore();
        allorder_listview.setRefreshTime();
//		iv_empty_orderdata.setVisibility(View.VISIBLE);
    }

    /***
     * 所有订单接口请求
     */
    private void requestOrderPost() {
        HttpServer.getInstance().requestPOST(allOrderUrl, orderParams, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "--来到订单列表页面-statusCode->>>" + statusCode + "  --");
                String result = new String(responseBody);
                Log.d(TAG, "--result-->>>" + result + "  --");


                List<HashMap<String, Object>> temDataList = new ArrayList<HashMap<String, Object>>();
                temDataList = jsonToAllOrderList(result);

                if (temDataList != null) {
                    if (temDataList.size() == 0) {
                        Toast.makeText(mContext, "无更多装修公司订单了", Toast.LENGTH_SHORT).show();
                        iv_empty_orderdata.setVisibility(View.VISIBLE);
                    } else {
                        if (page == 1) {
                            allOrderList.clear();
                        }
                        iv_empty_orderdata.setVisibility(View.GONE);
                        for (int i = 0; i < temDataList.size(); i++) {
                            allOrderList.add(temDataList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                onLoad();

                //FIXME
                popMap = jsonToPopWindowList(result);
            }
        });
    }

    ;

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    @Override
    protected void onResume() {
        requestOrderPost();
        super.onResume();
    }

    @SuppressLint("SimpleDateFormat")
    private void operLf(final String orderid) {
        final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final LfPwdPopupWindow popupWindow = new LfPwdPopupWindow(mContext);
        popupWindow.showAtLocation(allorder_title.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.bt_subit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String recordcontent = popupWindow.et_lf_situation.getText().toString().trim();
                String lftime = sDateFormat.format(new java.util.Date());
                if (TextUtils.isEmpty(recordcontent)) {
                    Toast.makeText(mContext, "量房详情不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                orderParams = AppInfoUtil.getPublicParams(getApplicationContext());
                orderParams.put("recordcontent", recordcontent);
                orderParams.put("token", token);
                orderParams.put("lftime", lftime);
                orderParams.put("orderid", orderid);

                HttpServer.getInstance().requestPOST(lfangUrl, orderParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String result = new String(responseBody);
                        try {
                            JSONObject object = new JSONObject(result);
                            if (object.getInt("error_code") == 0) {
                                Toast.makeText(mContext, "量房成功！", Toast.LENGTH_SHORT).show();
                                popupWindow.dismiss();
                                requestOrderPost();
                            } else {
                                Toast.makeText(mContext, "量房失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
