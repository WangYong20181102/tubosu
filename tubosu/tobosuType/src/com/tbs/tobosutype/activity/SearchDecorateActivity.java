package com.tbs.tobosutype.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.DecorateListAdapter;
import com.tbs.tobosutype.global.Constant;
import com.tbs.tobosutype.global.OKHttpUtil;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.tbs.tobosutype.utils.HttpServer;
import com.tbs.tobosutype.utils.PrseJsonUtil;
import com.tbs.tobosutype.xlistview.XListView;
import com.tbs.tobosutype.xlistview.XListView.IXListViewListener;
import com.umeng.analytics.MobclickAgent;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/***
 *  找装修页面顶部 搜索框搜索装修公司和地址的页面
 * @author dec
 *
 */
public class SearchDecorateActivity extends Activity implements IXListViewListener, OnClickListener, OnKeyListener {
    private static final String TAG = SearchDecorateActivity.class.getSimpleName();
    private Context mContext;
    private EditText et_search;
    private TextView tv_cancel;
    private String kw;
    private RelativeLayout search_banner;
    /***装修公司列表接口*/
    private String companyListUrl = Constant.TOBOSU_URL + "tapp/company/company_list";

    private HashMap<String, Object> companyListParams;

    private int page = 1;
    private XListView xlv_decorate;
    private ArrayList<HashMap<String, String>> comList;
    private DecorateListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_search_decorate);
        mContext = SearchDecorateActivity.this;

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        search_banner = (RelativeLayout) findViewById(R.id.search_banner);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        xlv_decorate = (XListView) findViewById(R.id.xlv_decorate);
        xlv_decorate.setPullLoadEnable(true);
        xlv_decorate.setXListViewListener(this);
        xlv_decorate.setSelector(new ColorDrawable(Color.TRANSPARENT));
        search_banner.setBackgroundColor(Color.parseColor("#ff882e"));
    }

    private void initData() {
        comList = new ArrayList<HashMap<String, String>>();
        adapter = new DecorateListAdapter(mContext, comList);
        xlv_decorate.setAdapter(adapter);
    }

    /***
     * 装修公司接口请求
     */
    private void requestCompanyList() {
        if (!Constant.checkNetwork(mContext)) {
            Constant.toastNetOut(mContext);
            return;
        }

        OKHttpUtil.post(companyListUrl, companyListParams, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<HashMap<String, String>> temDataList = PrseJsonUtil.jsonToComList(json);
                        if (temDataList.size() == 0 && comList.size() != 0 && page != 1) {
                            Toast.makeText(SearchDecorateActivity.this, "没有搜索到更多的装修公司了", Toast.LENGTH_SHORT).show();
                        }
                        if (page == 1) {
                            comList.clear();
                        }
                        if (temDataList.size() != 0) {
                            for (int i = 0; i < temDataList.size(); i++) {
                                comList.add(temDataList.get(i));
                            }
                        } else {
                            Toast.makeText(SearchDecorateActivity.this, "没有搜索到相关的装修公司了", Toast.LENGTH_SHORT).show();
                        }
                        onLoad();
                    }
                });
            }
        });

    }

    /***
     * 隐藏虚拟键盘
     * @param context
     * @param view
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        et_search.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == 66) {
            MobclickAgent.onEvent(mContext, "click_find_com_decoration");
            et_search.requestFocus();
            hideInput(mContext, et_search);
            kw = et_search.getText().toString().trim();
            operParams(kw);
        }
        return false;
    }

    private void operParams(String kw) {
        companyListParams = AppInfoUtil.getPublicHashMapParams(getApplicationContext());
        companyListParams.put("page", page + "");
        companyListParams.put("kw", kw);
        companyListParams.put("recommend", "0");
        requestCompanyList();
    }

    private void onLoad() {
        adapter.notifyDataSetChanged();
        xlv_decorate.stopRefresh();
        xlv_decorate.stopLoadMore();
        xlv_decorate.setRefreshTime();
    }

    @Override
    public void onRefresh() {
        page = 1;
        operParams(kw);
    }

    @Override
    public void onLoadMore() {
        page++;
        operParams(kw);
    }

}
