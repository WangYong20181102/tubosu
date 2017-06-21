package com.tbs.tobosutype.activity;

import java.util.ArrayList;
import java.util.List;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.customview.CustomGridView;
import com.tbs.tobosutype.utils.AppInfoUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 搜索逛图库
 *
 * @author dec
 */
public class SearchImageActivity extends Activity implements OnClickListener, OnKeyListener {
    private static final String TAG = SearchImageActivity.class.getSimpleName();
    private Context context;
    private EditText et_search;
    private TextView tv_cancel;
    private String kw;

    private CustomGridView gv_style;
    private CustomGridView gv_model;
    private CustomGridView gv_area;

    private List<String> styleList;
    private ArrayAdapter<String> styleAdapter;
    private String[] style_arrays;
    private ArrayList<String> modelList;
    private String[] model_arrays;
    private ArrayAdapter<String> modelAdapter;
    private ArrayList<String> areaList;
    private String[] area_arrays;
    private ArrayAdapter<String> areaAdapter;
    private String[] style_ids;
    private String[] model_ids;
    private String[] area_ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfoUtil.setTranslucentStatus(this);
        setContentView(R.layout.activity_search_image);
        context = SearchImageActivity.this;
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        gv_style = (CustomGridView) findViewById(R.id.gv_style);
        gv_model = (CustomGridView) findViewById(R.id.gv_model);
        gv_area = (CustomGridView) findViewById(R.id.gv_area);
        gv_style.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_model.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv_area.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        style_ids = getResources().getStringArray(R.array.style_id);
        model_ids = getResources().getStringArray(R.array.model_id);
        area_ids = getResources().getStringArray(R.array.area_id);
        styleList = new ArrayList<String>();
        style_arrays = getResources().getStringArray(R.array.style_array);
        for (int i = 0; i < style_arrays.length; i++) {
            styleList.add(style_arrays[i]);
        }
        styleAdapter = new ArrayAdapter<String>(context, R.layout.search_image_gridview, styleList);
        gv_style.setAdapter(styleAdapter);
        modelList = new ArrayList<String>();
        model_arrays = getResources().getStringArray(R.array.model_array);
        for (int i = 0; i < model_arrays.length; i++) {
            modelList.add(model_arrays[i]);
        }
        modelAdapter = new ArrayAdapter<String>(context, R.layout.search_image_gridview, modelList);
        gv_model.setAdapter(modelAdapter);
        areaList = new ArrayList<String>();
        area_arrays = getResources().getStringArray(R.array.area_array);
        for (int i = 0; i < area_arrays.length; i++) {
            areaList.add(area_arrays[i]);
        }
        areaAdapter = new ArrayAdapter<String>(context, R.layout.search_image_area_gridview, areaList);
        gv_area.setAdapter(areaAdapter);
    }

    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initEvent() {
        tv_cancel.setOnClickListener(this);
        et_search.setOnKeyListener(this);
        gv_model.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                MobclickAgent.onEvent(context, "click_find_decoration_serach");
                Intent intent = getIntent();
                intent.putExtra("key", "layout_id");
                intent.putExtra("value", model_ids[postion]);
                intent.putExtra("name", model_arrays[postion]);
                setResult(0, intent);
                finish();
            }
        });

        gv_style.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                Intent intent = getIntent();
                intent.putExtra("key", "style_id");
                intent.putExtra("value", style_ids[postion]);
                intent.putExtra("name", style_arrays[postion]);
                setResult(0, intent);
                finish();
            }
        });

        gv_area.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                Intent intent = getIntent();
                intent.putExtra("key", "area_id");
                intent.putExtra("value", area_ids[postion]);
                intent.putExtra("name", area_arrays[postion]);
                setResult(0, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                onBackPressed();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == 66) {
            et_search.requestFocus();
            hideInput(context, et_search);
            kw = et_search.getText().toString().trim();
            Intent intent = getIntent();
            intent.putExtra("key", "communityName");
            intent.putExtra("value", kw);
            intent.putExtra("name", kw);
            setResult(0, intent);
            finish();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("key", "搜索图库");
        setResult(0, intent);
        finish();
    }
}
