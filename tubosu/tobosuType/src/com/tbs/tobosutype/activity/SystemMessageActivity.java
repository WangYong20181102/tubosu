package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.adapter.utils.AppInfoUtil;
import com.tbs.tobosutype.adapter.utils.HttpServer;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;

/**
 * 系统消息页面
 *
 * @author dec
 */
public class SystemMessageActivity extends Activity implements IXListViewListener {
    private static final String TAG = SystemMessageActivity.class.getSimpleName();

    /***
     * 外部请求标记 <br/>
     * 		0
     */
    private static final String GO_OUT_URL = "0";

    /***
     * 外部请求标记  直接跳转h5 <br/>
     * 		1
     */
    private static final String GO_INTER_URL = "1";

    /**
     * 已读标记为 1
     */
    private static final String HAS_READ_MESSAGE = "1";

    private Context mContext;
    private ImageView sysmessage_back;
    private XListView xlv_sysmessage;

    /**
     * 消息列表接口
     */
    private String userSystemMessageUrl = Constant.TOBOSU_URL + "tapp/msg/user_msg";

    /**
     * 读取消息外链接口
     */
    private String userMsgOne = Constant.TOBOSU_URL + "tapp/msg/user_msg_one";

    private RequestParams userMsgParams;
    private RequestParams userMsgOneParams;
    //banner
    private RelativeLayout rel_system_msg_bar;
    private int page = 1;
    private ArrayList<HashMap<String, String>> msgsDataList;
    private MsgAdapter msgAdapter;

    private ImageView iv_sys_msg_empty_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_system_message);
        mContext = SystemMessageActivity.this;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        rel_system_msg_bar = (RelativeLayout) findViewById(R.id.rel_system_msg_bar);
        sysmessage_back = (ImageView) findViewById(R.id.sysmessage_back);
        xlv_sysmessage = (XListView) findViewById(R.id.xlv_sysmessage);
        iv_sys_msg_empty_data = (ImageView) findViewById(R.id.iv_sys_msg_empty_data);
        xlv_sysmessage.setPullLoadEnable(true);
        xlv_sysmessage.setXListViewListener(this);
        xlv_sysmessage.setSelector(new ColorDrawable(Color.TRANSPARENT));
        rel_system_msg_bar.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        userMsgParams = AppInfoUtil.getPublicParams(getApplicationContext());
        userMsgParams.put("token", AppInfoUtil.getToekn(getApplicationContext()));
        userMsgParams.put("page", page + "");
        msgsDataList = new ArrayList<HashMap<String, String>>();

        requestUserSystemMEssage();
        userMsgOneParams = AppInfoUtil.getPublicParams(getApplicationContext());

        msgAdapter = new MsgAdapter(mContext, msgsDataList);
        xlv_sysmessage.setAdapter(msgAdapter);
    }

    /***
     * 用户的消息列表接口
     */
    private void requestUserSystemMEssage() {
        HttpServer.getInstance().requestPOST(userSystemMessageUrl, userMsgParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] body) {
                Log.d(TAG, "来到这里了 -->>" + new String(body));
                try {
                    JSONObject jsonObject = new JSONObject(new String(body));
                    if (page == 1) {
                        msgsDataList.clear();
                    }
                    if (jsonObject.getInt("error_code") == 0) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("title", data.getJSONObject(i).getString("title"));
                            map.put("id", data.getJSONObject(i).getString("id"));
                            map.put("content", data.getJSONObject(i).getString("content"));
                            map.put("time", data.getJSONObject(i).getString("time"));
                            map.put("url", data.getJSONObject(i).getString("url"));
                            map.put("urlType", data.getJSONObject(i).getString("urlType"));
                            map.put("read", data.getJSONObject(i).getString("read"));
                            msgsDataList.add(map);
                        }
                    }
                    if (msgsDataList.size() != 0 && jsonObject.getInt("error_code") != 0) {
                        Toast.makeText(mContext, "没有更多的系统消息!", Toast.LENGTH_SHORT).show();
                    }
                    if (msgsDataList.size() == 0) {
                        Toast.makeText(mContext, "暂时还没有系统系息!", Toast.LENGTH_SHORT).show();
                        iv_sys_msg_empty_data.setVisibility(View.VISIBLE);
                    }
                    onLoad();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });
    }


    /***
     * 消息列表适配器
     * @author dec
     *
     */
    class MsgAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<HashMap<String, String>> msgs;

        public MsgAdapter(Context context, ArrayList<HashMap<String, String>> msgs) {
            this.context = context;
            this.msgs = msgs;
        }

        @Override
        public int getCount() {
            if (msgs.size() == 0) {
                return 0;
            }
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return msgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_sysmessage, null);
                holder.iv_isread = (ImageView) convertView.findViewById(R.id.iv_isread);
                holder.iv_read = (ImageView) convertView.findViewById(R.id.iv_read);
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_time.setText(msgs.get(position).get("time"));
            holder.tv_content.setText(msgs.get(position).get("content"));
            holder.tv_title.setText(msgs.get(position).get("title"));

            //是否已读标记  1 已读
            if (HAS_READ_MESSAGE.equals(msgs.get(position).get("read"))) {
                holder.iv_isread.setVisibility(View.GONE);
            } else {
                holder.iv_isread.setVisibility(View.VISIBLE);
            }

            holder.iv_read.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(SystemMessageActivity.this, WebViewActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("link", msgs.get(position).get("url"));
                    detailIntent.putExtras(bundle);
                    startActivity(detailIntent);

                    if (GO_OUT_URL.equals(msgs.get(position).get("urlType"))) {
                        Log.d(TAG, "你进入外链哦");
                        questReadUserMsg(msgs.get(position).get("id"));
                    } else {
                        Log.d(TAG, "你进入内链哦");
                    }
                }

            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_time;
            TextView tv_title;
            TextView tv_content;
            ImageView iv_isread;
            ImageView iv_read;
        }
    }


    /***
     * 读取消息外链的接口方法
     * @param id
     */
    private void questReadUserMsg(String id) {
        userMsgOneParams.put("token", AppInfoUtil.getToekn(getApplicationContext()));
        userMsgOneParams.put("id", id);

        HttpServer.getInstance().requestPOST(userMsgOne, userMsgOneParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Log.d(TAG, arg0 + "已读取成功");

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                Log.d(TAG, arg0 + "已读取失败");
            }
        });
    }

    private void initEvent() {
        sysmessage_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onLoad() {
        msgAdapter.notifyDataSetChanged();
        xlv_sysmessage.stopRefresh();
        xlv_sysmessage.stopLoadMore();
        xlv_sysmessage.setRefreshTime();
    }

    @Override
    public void onRefresh() {
        page = 1;
        userMsgParams.put("page", page + "");
        requestUserSystemMEssage();
    }

    @Override
    public void onLoadMore() {
        page++;
        userMsgParams.put("page", page + "");
        requestUserSystemMEssage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
