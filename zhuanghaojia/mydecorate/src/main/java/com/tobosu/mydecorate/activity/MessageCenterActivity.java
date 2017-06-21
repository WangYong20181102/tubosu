package com.tobosu.mydecorate.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.MessageAdapter;
import com.tobosu.mydecorate.database.DBManager;
import com.tobosu.mydecorate.entity.MessageData;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dec on 2016/9/27.
 */

public class MessageCenterActivity extends AppCompatActivity{
    private static final String TAG = MessageCenterActivity.class.getSimpleName();
    private Context mContext;
    private RelativeLayout rel_messagecenter_back;

    private ArrayList<MessageData> messageList = new ArrayList<MessageData>();

    private RecyclerView recycle_message;

    private ImageView message_data_empty;

    private String message_url = Constant.ZHJ + "tapp/mt/getPushLog";

    private int page = 1;
    private int pageSize = 10;

    private MessageAdapter msgAdapter = null;

    private DBManager manager = null;

    private String sql = "insert into Message_Data(aid, title, read_flag) values(?,?,?)";

    private MessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagecenter);
        mContext = MessageCenterActivity.this;
        initViews();
        do_getMessage();
    }

    private void initViews() {
        rel_messagecenter_back = (RelativeLayout) findViewById(R.id.rel_messagecenter_back);
        rel_messagecenter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        message_data_empty = (ImageView) findViewById(R.id.message_data_empty);
        recycle_message = (RecyclerView) findViewById(R.id.recycle_message);

    }


    private void do_getMessage(){
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("page", page+"");
        hashMap.put("pageSize", pageSize+"");
        okHttpUtil.post(message_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                System.out.println("---消息列表--" + json);
                try {
                    JSONObject obj = new JSONObject(json);
                    if(obj.getInt("error_code")==0){
                        message_data_empty.setVisibility(View.GONE);
                        recycle_message.setVisibility(View.VISIBLE);
                        JSONObject data = obj.getJSONObject("data");
                        int page = data.getInt("page");
                        int pageSize = data.getInt("pages");
                        String count = data.getString("count");
                        JSONArray dataArray = data.getJSONArray("data");
                        for(int i=0, len=dataArray.length();i<len; i++){
                            MessageData msgData = new MessageData();
                            msgData.setArtcleId(dataArray.getJSONObject(i).getString("aid"));

                            msgData.setArticleTitle(dataArray.getJSONObject(i).getString("title"));
                            msgData.setTypeId(dataArray.getJSONObject(i).getString("type_id"));
                            msgData.setUserId(dataArray.getJSONObject(i).getString("uid"));
                            msgData.setDescription(dataArray.getJSONObject(i).getString("description"));
                            msgData.setTime(dataArray.getJSONObject(i).getJSONObject("time_rec").getString("time"));
                            msgData.setTimeUnit(dataArray.getJSONObject(i).getJSONObject("time_rec").getString("time_unit"));
                            msgData.setWriterId(dataArray.getJSONObject(i).getString("nick"));
                            msgData.setPicUrl(dataArray.getJSONObject(i).getString("header_pic_url"));

                            // 插入数据库
                            if(manager==null){
                                manager = DBManager.getInstance(mContext);
                            }
                            if(!manager.checkMessageData(dataArray.getJSONObject(i).getString("aid"))){
                                Object[] bindArgs = {dataArray.getJSONObject(i).getString("aid"), dataArray.getJSONObject(i).getString("title"),"0"};
                                manager.insertMessageTable(sql, bindArgs);
                            }else {
//                            System.out.println("--类型有重复--");
                            }

                            messageList.add(msgData);
                        }

                        initAdapter(messageList);

                    }if(obj.getInt("error_code")==201){
                        //没有数据
                        message_data_empty.setVisibility(View.VISIBLE);
                        recycle_message.setVisibility(View.GONE);
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

    private void initAdapter(ArrayList<MessageData> _messageList){
        recycle_message.setHasFixedSize(true);
        //创建默认的线性LayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recycle_message.setLayoutManager(layoutManager);

        adapter = new MessageAdapter(mContext, _messageList);
        recycle_message.setAdapter(adapter);
        adapter.setOnItemClickListener(new MessageAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View view, MessageData data) {
                Intent it = new Intent(mContext, ArticleDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("article_id", data.getArtcleId());
                b.putString("type_id", data.getTypeId());
                b.putString("article_title", data.getArticleTitle());
                b.putString("article_title_pic_url", data.getPicUrl());
                b.putString("writer_id", data.getWriterId()); // 被查看的用戶id
                if(Util.isLogin(mContext)){
                    b.putString("uid", Util.getUserId(mContext));
                }else {
                    b.putString("uid", "0"); // uid-->>0 是游客模式
                }
                it.putExtra("Article_Detail_Bundle",b);

                if(manager==null){
                    manager = DBManager.getInstance(mContext);
                }
                manager.updateMessage(data.getArtcleId());
                startActivity(it);
                //TODO 紅點變沒有
            }
        });
    }

    public void onResume() {
        super.onResume();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
