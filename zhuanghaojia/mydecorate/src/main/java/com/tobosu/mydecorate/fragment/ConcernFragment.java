package com.tobosu.mydecorate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.LoginActivity;
import com.tobosu.mydecorate.activity.WriterActivity;
import com.tobosu.mydecorate.adapter.MyConcernWriterAdapter;
import com.tobosu.mydecorate.adapter.RecommendAdapter;
import com.tobosu.mydecorate.database.DBManager;
import com.tobosu.mydecorate.global.Constant;
import com.tobosu.mydecorate.global.OKHttpUtil;
import com.tobosu.mydecorate.util.DensityUtil;
import com.tobosu.mydecorate.util.Util;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by dec on 2016/9/14.
 * 关注页面
 */
public class ConcernFragment extends Fragment {

    private static final String TAG = ConcernFragment.class.getSimpleName();

    private GridView gv_recommend;

    private RecyclerView recycler_my_concerned_writer;

    private ArrayList<HashMap<String, String>> recommandDataList = new ArrayList<HashMap<String, String>>();

    private DBManager manager = null;

    private RelativeLayout rel_empty_concen_data;

    private RecommendAdapter adapter = null;

    MyConcernWriterAdapter myConcenAdapter = null;

    /**
     * 关注用户接口
     */
    private String concern_writer_user_url = Constant.ZHJ + "tapp/mt/attentionUser";
    private String m_uid= "";
    private String m_nick = "";
    private String m_sort_description = "";
    private String m_header_pic_url = "";
    private String m_count_article = "";
    private String m_is_read = "";
    private String m_time_num = "";
    private String m_time_uint = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concern, null, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecommendConcern();
        initMyConcernedWriters();
    }

    private void initView(View view) {
        gv_recommend = (GridView) view.findViewById(R.id.gv_recommend);
        recycler_my_concerned_writer = (RecyclerView) view.findViewById(R.id.recycler_my_concerned_writer);
        rel_empty_concen_data = (RelativeLayout) view.findViewById(R.id.rel_empty_concen_data);

        initRecommendConcern();
        initMyConcernedWriters();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 初始化推荐关注
     */
    private void initRecommendConcern() {
        System.out.println("-->>---读取推荐-- 有数据吗 --");
        recommandDataList.clear();
        if(Util.isLogin(getActivity())) {
            final DBManager manager = DBManager.getInstance(getActivity());
            recommandDataList = manager.queryRecommandData();
        }else {
            //未来登录
            recommandDataList = getUnLoginRecommandData();
        }


        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        //设置水平横向滑动的参数
        int size = recommandDataList.size();

        float density = dm.density;
        int gridviewWidth = (int) (size * (DensityUtil.dip2px(getActivity(),40)) * density);
        int itemWidth = (int) (DensityUtil.dip2px(getActivity(),54) * density);

        // 极其重要的设置
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_recommend.setLayoutParams(params); //设置GirdView布局参数,横向布局的关键
        gv_recommend.setColumnWidth(itemWidth);
        gv_recommend.setHorizontalSpacing(32);
        gv_recommend.setNumColumns(size);

        // 初始化推荐数据
        adapter = new RecommendAdapter(getActivity(), recommandDataList);

        gv_recommend.setAdapter(adapter);



        adapter.setAddConcernListener(new RecommendAdapter.AddConcernListener() {
            @Override
            public void onAddConcernListener(int position) {
                if(Util.isLogin(getActivity())){
                    if(Util.isNetAvailable(getActivity())){
                        MobclickAgent.onEvent(getActivity(),"click_focous_recommend_button");
                        m_uid= recommandDataList.get(position).get("uid");
                        m_nick = recommandDataList.get(position).get("nick");
                        m_sort_description = recommandDataList.get(position).get("sort_description");
                        m_header_pic_url = recommandDataList.get(position).get("header_pic_url");
                        m_count_article = recommandDataList.get(position).get("count_article");
                        m_is_read = recommandDataList.get(position).get("is_read");
                        m_time_num = recommandDataList.get(position).get("time_num");
                        m_time_uint = recommandDataList.get(position).get("time_uint");
                        addConcernt(m_uid);
                    }else {
                        Toast.makeText(getActivity(),"网络不佳，请稍后再试~",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), Constant.APP_LOGIN);
                }


            }
        });

        gv_recommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MobclickAgent.onEvent(getActivity(),"click_focous_recommend");
                // 点击事件 跳转详情
                Intent concernUserIntent = new Intent(getActivity(), WriterActivity.class);
                Bundle b = new Bundle();
                b.putString("writer_uid", recommandDataList.get(position).get("uid"));   // 被查看的用戶id
                b.putString("nick", recommandDataList.get(position).get("nick"));
                b.putString("header_pic_url", recommandDataList.get(position).get("header_pic_url"));
//                b.putString("writer_uid", Util.getUserId(getActivity()));
                b.putString("is_att", recommandDataList.get(position).get("is_att")); // 是否被关注过
                concernUserIntent.putExtra("Writer_User_Bundle", b);
                startActivityForResult(concernUserIntent, Constant.MAINACTIVITY_REQUESTCODE);
            }
        });
    }

    /**
     * 添加关注
     * @param writerId
     */
    private void addConcernt(String writerId){
        OKHttpUtil okHttpUtil = new OKHttpUtil();
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("uid", Util.getUserId(getActivity())); // 我的id
        hashMap.put("aid", writerId); // 被关注的id
        okHttpUtil.post(concern_writer_user_url, hashMap, new OKHttpUtil.BaseCallBack() {
            @Override
            public void onSuccess(Response response, String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject.getInt("error_code")==0){

                        // 插入我的关注表中
                        if(!manager.qurryData(m_uid)){
                            //uid, nick, header_pic_url, count_article, is_att, is_read, time_num， time_uint
                            String sql = "insert into Concern_Writer_Table(uid, nick, sort_description, header_pic_url, count_article, is_att, is_read, time_num, time_uint) values(?,?,?,?,?,?,?,?,?);";
                            Object[] bindArgs = {
                                    m_uid, // uid
                                    m_nick, // nick
                                    m_sort_description, // sort_description 在这里无意义 以后扩展
                                    m_header_pic_url,
                                    m_count_article,
                                    "1",
                                    m_is_read,
                                    m_time_num,
                                    m_time_uint};
                            manager.insertConcernWriter(sql, bindArgs);
//                            manager.updateConcernData("1", "", m_uid, "Recommend_Data");
                        }

                        initRecommendConcern();

                    }else if(jsonObject.getInt("error_code")==1){
                        Toast.makeText(getActivity(),"你已关注过了",Toast.LENGTH_SHORT).show();
                    }else if(jsonObject.getInt("error_code")==201){
                        Toast.makeText(getActivity(),"添加关注失败, 请稍后再试",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initMyConcernedWriters();
            }

            @Override
            public void onFail(Request request, IOException e) {

            }

            @Override
            public void onError(Response response, int code) {

            }
        });
        // 刷新数据
        initMyConcernedWriters();
        initRecommendConcern();
    }

    private ArrayList<HashMap<String, String>> getUnLoginRecommandData(){
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
        String result = getActivity().getSharedPreferences("Save_UnLogin_Recommand_Json", Context.MODE_PRIVATE).getString("UnLogin_Recommand_Json","");
        if(!"".equals(result)){
            try {
                JSONObject obj = new JSONObject(result);
                if (obj.getInt("error_code")==0) {
                    JSONObject data = obj.getJSONObject("data");

                    JSONObject recommandWriterObj = data.getJSONObject("user_rec");
                    JSONArray array = recommandWriterObj.getJSONArray("data");

                    for(int i=0, len=array.length(); i<len; i++){
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("uid", array.getJSONObject(i).getString("uid"));
                        hashMap.put("nick", array.getJSONObject(i).getString("nick"));
                        hashMap.put("sort_description", array.getJSONObject(i).getString("sort_description"));
                        hashMap.put("header_pic_url", array.getJSONObject(i).getString("header_pic_url"));
                        hashMap.put("article_num", array.getJSONObject(i).getString("article_num"));
                        hashMap.put("time_num", array.getJSONObject(i).getJSONObject("time_create").getString("time"));
                        hashMap.put("time_uint", array.getJSONObject(i).getJSONObject("time_create").getString("time_unit"));
                        hashMap.put("is_att", array.getJSONObject(i).getInt("is_att")+"");
                        hashMap.put("is_read", array.getJSONObject(i).getInt("is_read")+"");
                        dataList.add(hashMap);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {

            }
        }else {

        }

        return dataList;
    }


    private ArrayList<HashMap<String, String>> concernedList = new ArrayList<HashMap<String, String>>();

    /**
     * 登录后我的关注列表
     */
    private void initMyConcernedWriters(){

        // 从本地获取 我的关注
        if(manager==null){
            manager = DBManager.getInstance(getActivity());
        }

        if(Util.isLogin(getActivity())){
            concernedList.clear();
            System.out.println("-->>---读取我的关注-- 有数据吗 --");
            concernedList = manager.queryMyConcernWriter();
            if(concernedList.size()>0){
                recycler_my_concerned_writer.setVisibility(View.VISIBLE);
                rel_empty_concen_data.setVisibility(View.GONE);

                // 填充数据
                myConcenAdapter = new MyConcernWriterAdapter(getActivity(), concernedList);

                //创建默认的线性LayoutManager
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setSmoothScrollbarEnabled(true);
                layoutManager.setAutoMeasureEnabled(true);
                recycler_my_concerned_writer.setLayoutManager(layoutManager);

                //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                recycler_my_concerned_writer.setHasFixedSize(true);
                recycler_my_concerned_writer.setNestedScrollingEnabled(false);

                recycler_my_concerned_writer.setAdapter(myConcenAdapter);

                myConcenAdapter.setOnRecyclerViewItemClickListener(new MyConcernWriterAdapter.OnRecyclerViewItemClickListener() {

                    @Override
                    public void onRecyclerViewItemClick(View view, HashMap<String, String> data) {
                        MobclickAgent.onEvent(getActivity(),"click_focous_focoused");
                        Intent concernUserIntent = new Intent(getActivity(), WriterActivity.class);
                        Bundle b = new Bundle();

                        b.putString("writer_uid", data.get("uid"));   // 被查看的用戶id
                        b.putString("nick", data.get("nick"));
                        b.putString("header_pic_url", data.get("header_pic_url"));
                        b.putString("uid", data.get("uid"));

                        //跳转后就是已经查看过
                        if(Util.isLogin(getActivity())){
                            b.putString("uid", Util.getUserId(getActivity()));// 登錄用戶id
                            if(data.get("is_read").equals("0")){
                                // 未查看 -->> 变查看
                                manager.updateConcernData("","1", data.get("uid"), "Concern_Writer_Table");
//                                myConcenAdapter.notifyDataSetChanged();
                            }
                        }

                        b.putString("is_att", data.get("is_att")); // 是否被关注过
                        concernUserIntent.putExtra("Writer_User_Bundle", b);
                        startActivity(concernUserIntent);
                    }
                });

            }else {
                rel_empty_concen_data.setVisibility(View.VISIBLE);
                recycler_my_concerned_writer.setVisibility(View.GONE);
            }
        }else{
            rel_empty_concen_data.setVisibility(View.VISIBLE);
            recycler_my_concerned_writer.setVisibility(View.GONE);
        }
    }
}
