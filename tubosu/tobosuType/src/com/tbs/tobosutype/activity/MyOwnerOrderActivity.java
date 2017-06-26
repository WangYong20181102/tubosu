package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.AllConstants;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.DensityUtil;
import com.tbs.tobosutype.utils.HttpServer;

/**
 * 业主的个人订单列表页
 *
 * @author dec
 */
public class MyOwnerOrderActivity extends Activity {
    private static final String TAG = MyOwnerOrderActivity.class.getSimpleName();
    private Context mContext;

    private ImageView myownerorder_back;

    /**
     * 订单listview
     */
    private ListView myownerorder_listView;

    /***没有发布订单提示信息*/
    private ImageView iv_myownerorder_empty;

    private List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    private String token;
    private RequestParams params;

    /**
     * 用户订单列表适配器
     */
    private MyOwnerOrderAdapter adapter;
    private int page = 1;

    /***正在加载订单时的布局*/
    private LinearLayout myownerorder_loading;
    //顶部banner
    private RelativeLayout rel_myowner_order_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_myownerorder);

        mContext = MyOwnerOrderActivity.this;

        initViews();
        initData();
    }

    private void initViews() {
        rel_myowner_order_bar = (RelativeLayout) findViewById(R.id.rel_myowner_order_bar);
        myownerorder_loading = (LinearLayout) findViewById(R.id.ll_loading);
        token = AppInfoUtil.getToekn(getApplicationContext());
        if (!TextUtils.isEmpty(token)) {
            params = AppInfoUtil.getPublicParams(getApplicationContext());
            params.put("token", token);
            requestMyOwnerOderPost();
        }
        myownerorder_back = (ImageView) findViewById(R.id.myownerorder_back);
        myownerorder_listView = (ListView) findViewById(R.id.myownerorder_listView);
        iv_myownerorder_empty = (ImageView) findViewById(R.id.iv_myownerorder_empty);

        myownerorder_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rel_myowner_order_bar.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {

        adapter = new MyOwnerOrderAdapter(MyOwnerOrderActivity.this, dataList);

        if (adapter.isEnabled(0) && adapter != null) {
            myownerorder_listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        myownerorder_listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent orderDetailIntent = new Intent(MyOwnerOrderActivity.this, OrderDetailActivity.class);
                orderDetailIntent.putExtra("url", dataList.get(position).get("orderDetailUrl"));
                startActivity(orderDetailIntent);

            }
        });
    }


    /****
     * 订单列表适配器
     * @author dec
     *
     */
    class MyOwnerOrderAdapter extends BaseAdapter {
        private List<HashMap<String, String>> list;
        private Context context;

        public MyOwnerOrderAdapter(Context context, List<HashMap<String, String>> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_myownerorder_listview, null);
                mHolder.tv_icommunity = (TextView) convertView.findViewById(R.id.tv_icommunity);
                mHolder.tv_addtime = (TextView) convertView.findViewById(R.id.tv_appoitment_time);
                mHolder.tv_delstatus = (TextView) convertView.findViewById(R.id.tv_delstatus);
                mHolder.tv_orderid = (TextView) convertView.findViewById(R.id.tv_orderid);
                mHolder.tv_delstatusDes = (TextView) convertView.findViewById(R.id.tv_delstatusDes);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();

            }
            String appointmentTime = list.get(position).get("addtime");
            mHolder.tv_addtime.setText(appointmentTime + " 预约");
//			Log.d(TAG, "--开始预约的时间是 " + appointmentTime);
            String delstatus = list.get(position).get("delstatus");
            if ("已确认需求".equals(delstatus)) {
                mHolder.tv_delstatus.setTextColor(Color.parseColor("#91d5ff"));
            }
            if ("预约装修".equals(delstatus)) {
                mHolder.tv_delstatus.setTextColor(getResources().getColor(R.color.color_red));
            }
            Drawable drawable = getResources().getDrawable(R.drawable.red_point_img);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mHolder.tv_delstatus.setText(delstatus);
            mHolder.tv_icommunity.setText("城市： " + list.get(position).get("position") + "   小区："
                    + list.get(position).get("housename"));
            mHolder.tv_orderid.setText("订单号 : " + list.get(position).get("orderid"));
            if ("1".equals(list.get(position).get("clickOrNot"))) {
                LayoutParams params = mHolder.tv_delstatusDes.getLayoutParams();
                params.width = DensityUtil.dip2px(getApplicationContext(), 80);
                mHolder.tv_delstatusDes.setLayoutParams(params);
                mHolder.tv_delstatusDes.setClickable(true);
                mHolder.tv_delstatusDes.setBackground(getResources().getDrawable(R.drawable.user_login_btn_background));
                mHolder.tv_delstatusDes.setTextColor(getResources().getColor(R.color.white));
                if (list.get(position).get("delstatusDes").contains("评价")) {
                    mHolder.tv_delstatusDes.setText("去评价");
                } else {
                    mHolder.tv_delstatusDes.setText("查看");
                }
            } else {
                LayoutParams params = mHolder.tv_delstatusDes.getLayoutParams();
                params.width = LayoutParams.WRAP_CONTENT;
                mHolder.tv_delstatusDes.setLayoutParams(params);
                mHolder.tv_delstatusDes.setClickable(false);
                mHolder.tv_delstatusDes.setText(list.get(position).get("delstatusDes") + "");
            }

            mHolder.tv_delstatusDes.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent = new Intent(MyOwnerOrderActivity.this, OrderDetailActivity.class);
                    orderDetailIntent.putExtra("url", list.get(position).get("orderDetailUrl"));
                    startActivity(orderDetailIntent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tv_icommunity;
            private TextView tv_addtime;
            private TextView tv_delstatus;
            private TextView tv_orderid;
            private TextView tv_delstatusDes;
        }
    }

    /***
     * 获取业主订单列表接口的请求方法
     */
    private void requestMyOwnerOderPost() {

        HttpServer.getInstance().requestPOST(AllConstants.MY_OWNER_ODER_URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
//						Log.d("MyOwnerOrderActivity", result);
                ArrayList<HashMap<String, String>> temDataList = new ArrayList<HashMap<String, String>>();
                Log.d(TAG, result);
                //解析json成数据
                temDataList = parseMyOwnerOderListJSON(result);
                if (temDataList != null) {
                    if (temDataList.size() == 0) {
                        Toast.makeText(mContext, "没有更多订单了", Toast.LENGTH_SHORT).show();
                        iv_myownerorder_empty.setVisibility(View.VISIBLE);
                    } else {
                        if (page == 1) {
                            dataList.clear();
                        }
                        for (int i = 0; i < temDataList.size(); i++) {
                            dataList.add(temDataList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        myownerorder_loading.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    ;

    /***
     * 将json结构数据转化成一个集合
     * @param result
     * @return
     */
    private ArrayList<HashMap<String, String>> parseMyOwnerOderListJSON(String result) {

        System.out.println("MyOwnerOrderActivity--返回json " + result);
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
            JSONObject object = new JSONObject(result);
            if (object.getInt("error_code") == 0) {
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, String> dataMap = new HashMap<String, String>();
                    JSONObject dataObject = array.getJSONObject(i);
                    dataMap.put("orderid", dataObject.getString("orderid"));
                    dataMap.put("addtime", dataObject.getString("addtime"));
                    dataMap.put("housename", dataObject.getString("housename"));
                    dataMap.put("position", dataObject.getString("position"));
                    dataMap.put("delstatus", dataObject.getString("delstatus"));
                    dataMap.put("delstatusDes", dataObject.getString("delstatusDes"));
                    dataMap.put("clickOrNot", dataObject.getString("clickOrNot"));
                    dataMap.put("orderDetailUrl", dataObject.getString("orderDetailUrl"));

                    list.add(dataMap);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
