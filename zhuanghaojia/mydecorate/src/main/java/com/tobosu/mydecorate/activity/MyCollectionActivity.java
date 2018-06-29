package com.tobosu.mydecorate.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.adapter.CollectionAdapter;
import com.tobosu.mydecorate.base.BaseActivity;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.Util;
import com.tobosu.mydecorate.view.CustomWaitDialog;
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

public class MyCollectionActivity extends BaseActivity {
    private static final String TAG = MyCollectionActivity.class.getSimpleName();
    private Context mContext;

    private RelativeLayout rel_mycollection_back;

    private RelativeLayout include_netout_layout;

    private String uid = "";

    private String mycollection_url = Constant.ZHJ + "tapp/mt/myCollection";

    private ImageView iv_collection_empty_data;

    private ArrayList<HashMap<String, String>> collectionData = null;


    private int myCollectionNum = 0;

    private CollectionAdapter cAdapter = null;

    private ListView lv_my_collection;

    private RelativeLayout rel_has_data;
    private RelativeLayout rel_empty_data;
    private LinearLayout mycollection_container;

    private CustomWaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollection);
        mContext = MyCollectionActivity.this;
        initViews();
        initDatas();
    }


    private void initViews() {
        include_netout_layout = (RelativeLayout) findViewById(R.id.include_netout_layout);
        rel_mycollection_back = (RelativeLayout) findViewById(R.id.rel_mycollection_back);
        rel_mycollection_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rel_has_data = (RelativeLayout) findViewById(R.id.rel_has_data);
        rel_empty_data = (RelativeLayout) findViewById(R.id.rel_empty_data);

        iv_collection_empty_data = (ImageView) findViewById(R.id.iv_collection_empty_data);
        lv_my_collection = (ListView) findViewById(R.id.lv_my_collection);
        mycollection_container = (LinearLayout) findViewById(R.id.mycollection_container);
    }

    private void initDatas() {
        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("Collection_Bundle");
        uid = b.getString("uid");
        if (!"".equals(uid)) {
            requestFromNet();
        }
    }

    private void showLoadingView(){
        waitDialog = new CustomWaitDialog(mContext);
        waitDialog.show();
    }

    private void hideLoadingView(){
        if(waitDialog!=null){
            waitDialog.dismiss();
        }
    }
    private void requestFromNet() {
        mycollection_container.setVisibility(View.GONE);
        if (Util.isNetAvailable(mContext)) {
            showLoadingView();
            Util.showNetOutView(mContext,include_netout_layout,true);

            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("uid", uid);
            OKHttpUtil okHttpUtil = new OKHttpUtil();
            okHttpUtil.post(mycollection_url, hashMap, new OKHttpUtil.BaseCallBack() {
                @Override
                public void onSuccess(Response response, String s) {
                    System.out.println("请求结果:" + s);
                    collectionData = parseMyCollectionJson(s);
                    hideLoadingView();
                    initCollection();
                }

                @Override
                public void onFail(Request request, IOException e) {

                }

                @Override
                public void onError(Response response, int code) {

                }
            });

        }else {
            Util.showNetOutView(mContext,include_netout_layout,false);
        }
        hideLoadingView();
    }


    private void initCollection(){
        mycollection_container.setVisibility(View.VISIBLE);
        hideLoadingView();
        if(collectionData!=null && collectionData.size()>0){
            cAdapter = new CollectionAdapter(mContext, collectionData);
            lv_my_collection.setAdapter(cAdapter);
            lv_my_collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detailIntent = new Intent(mContext, ArticleDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("type_id", collectionData.get(position).get("type_id"));
                    b.putString("article_id", collectionData.get(position).get("article_id"));
//                    b.putString("is_coll", "1");
                    b.putString("uid", collectionData.get(position).get("uid"));
                    b.putString("article_title_pic_url", collectionData.get(position).get("headpic_url"));
                    b.putString("article_title", collectionData.get(position).get("title"));
                    detailIntent.putExtra("Article_Detail_Bundle",b);
                    startActivity(detailIntent);
                }
            });

        }else{
            // 没数据
//            hideLoadingView();
        }
    }


    private ArrayList<HashMap<String, String>> parseMyCollectionJson(String myCollectionJson){
        ArrayList<HashMap<String, String >> list = new ArrayList<HashMap<String, String>>();
        try {
            JSONObject collectionJsonObject = new JSONObject(myCollectionJson);
            if (collectionJsonObject.getInt("error_code")==0) {
                // 注意判断的这字符串中的叹号是中文的
                if ("操作成功！".equals(collectionJsonObject.getString("msg"))){
                    iv_collection_empty_data.setVisibility(View.GONE);

                    JSONObject dataObject = collectionJsonObject.getJSONObject("data");
                    JSONArray collectionArray = dataObject.getJSONArray("collection_list");
                    int len = collectionArray.length();
                    for(int i=0; i<len; i++){
                        HashMap<String, String> dataMap = new HashMap<String, String>();
                        dataMap.put("article_id", collectionArray.getJSONObject(i).getString("aid"));
                        dataMap.put("type_id", collectionArray.getJSONObject(i).getString("type_id"));
                        dataMap.put("uid", collectionArray.getJSONObject(i).getString("uid"));

                        dataMap.put("title",collectionArray.getJSONObject(i).getString("title"));
                        dataMap.put("nick",collectionArray.getJSONObject(i).getString("nick"));
                        dataMap.put("type_name",collectionArray.getJSONObject(i).getString("type_name"));
                        dataMap.put("tup_count",collectionArray.getJSONObject(i).getString("tup_count"));
                        dataMap.put("collect_count",collectionArray.getJSONObject(i).getString("collect_count"));
                        dataMap.put("show_count",collectionArray.getJSONObject(i).getString("show_count"));
                        System.out.println("---------show——count --"+collectionArray.getJSONObject(i).getString("show_count"));
                        dataMap.put("header_pic_url",collectionArray.getJSONObject(i).getString("header_pic_url"));
                        dataMap.put("headpic_url",collectionArray.getJSONObject(i).getString("header_pic_url"));

                        dataMap.put("image_url",collectionArray.getJSONObject(i).getString("image_url"));
                        JSONObject timeObj = collectionArray.getJSONObject(i).getJSONObject("time_rec");
                            dataMap.put("_time",timeObj.getString("time"));
                            dataMap.put("_unit",timeObj.getString("time_unit"));
                        list.add(dataMap);
                    }
                    myCollectionNum = dataObject.getInt("collection_num");
                }else{
                    // 没有收藏时
                    iv_collection_empty_data.setVisibility(View.VISIBLE);
                }
            }else {
                // 其他情况
//                Toast.makeText(mContext,collectionJsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                rel_has_data.setVisibility(View.GONE);
                rel_empty_data.setVisibility(View.VISIBLE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }



    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    public void onResume() {
        super.onResume();
        initDatas();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
