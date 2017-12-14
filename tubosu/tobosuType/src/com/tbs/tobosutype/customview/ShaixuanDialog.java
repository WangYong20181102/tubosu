package com.tbs.tobosutype.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.adapter.ComDisctrictAdapter;
import com.tbs.tobosutype.adapter.ComJiatingAdapter;
import com.tbs.tobosutype.bean.CompanyDistrictBean;
import com.tbs.tobosutype.bean.ShaixuanBean;

import java.util.ArrayList;


public class ShaixuanDialog extends Dialog {
    private Context mContext;
    private TextView shuaixuanCity;
    private TextView resetShuaixuan;
    private TextView okShuaixuan;
    private CustomGridView gvServiceArea;
    private CustomGridView gvJiating;
    private CustomGridView gvShangye;

    private ArrayList<CompanyDistrictBean> areaDataList = new ArrayList<CompanyDistrictBean>();
    private ComDisctrictAdapter area;
    private ArrayList<ShaixuanBean> jiatingList =  new ArrayList<ShaixuanBean>();
    private ArrayList<ShaixuanBean> shangyeList = new ArrayList<ShaixuanBean>();

    private String cityName;

    public ShaixuanDialog(Context context) {
        super(context);
        this.mContext = context;
    }


    public ShaixuanDialog(Context context, int themeResId, String cityName) {
        super(context, themeResId);
        this.mContext = context;
        this.cityName = cityName;
    }


    //更新服务区域数据
    public void updateServiceAreaData(ArrayList<CompanyDistrictBean> dataList){
        this.areaDataList.clear();
        this.areaDataList.addAll(dataList);
        area.clearPosition();
    }

    public void setAreaData(ArrayList<CompanyDistrictBean> _areaDataList){
        this.areaDataList.addAll(_areaDataList);
    }

    public void setJiatingData(ArrayList<ShaixuanBean> _jiatingList){
        this.jiatingList.addAll(_jiatingList);
    }

    public void setShangyeData(ArrayList<ShaixuanBean> _shangyeList){
        this.shangyeList.addAll(_shangyeList);
    }

    public void setCity(String city){
        shuaixuanCity.setText(city);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shuaixuan_layout);
        initView();
    }

    private void initView(){
        shuaixuanCity = (TextView) findViewById(R.id.shuaixuanCity);
        RelativeLayout relChooseCitys = (RelativeLayout) findViewById(R.id.relChooseCitys);
        resetShuaixuan = (TextView) findViewById(R.id.resetShuaixuan);
        okShuaixuan = (TextView) findViewById(R.id.okShuaixuan);
        gvServiceArea = (CustomGridView) findViewById(R.id.gvServiceArea);
        gvJiating = (CustomGridView) findViewById(R.id.gvJiating);
        gvShangye = (CustomGridView) findViewById(R.id.gvShangye);



        shuaixuanCity.setText(cityName);

        area = new ComDisctrictAdapter(mContext, areaDataList);
        gvServiceArea.setAdapter(area);
        area.notifyDataSetChanged();
        gvServiceArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onServiceAreaClickListener.OnServiceAreaClickListener(areaDataList.get(i).getDistrict_id());
                area.setSelectedPosition(i);
                area.notifyDataSetChanged();
            }
        });


        final ComJiatingAdapter jiating = new ComJiatingAdapter(mContext, jiatingList);
        gvJiating.setAdapter(jiating);
        jiating.notifyDataSetChanged();
        gvJiating.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onJaitingClickListener.OnJaitingClickListener(jiatingList.get(i).getId());
                jiating.setSelectedPosition(i);
                jiating.notifyDataSetChanged();
            }
        });


        final ComJiatingAdapter shangye = new ComJiatingAdapter(mContext, shangyeList);
        gvShangye.setAdapter(shangye);
        shangye.notifyDataSetChanged();
        gvShangye.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                onShangyeClickListener.OnShangyeClickListener(shangyeList.get(i).getId());
                shangye.setSelectedPosition(i);
                shangye.notifyDataSetChanged();
            }
        });



        // 重置
        resetShuaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                area.clearPosition();
                jiating.clearPosition();
                shangye.clearPosition();
                onResetDataListener.OnResetDataListener();
            }
        });


        okShuaixuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(onOkListener!=null){
                    onOkListener.OnOkListener();
                }
            }
        });

        relChooseCitys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChooseCityListener!=null){
                    onChooseCityListener.OnChooseCityListener();
                }
            }
        });
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity( Gravity.RIGHT);
    }




    public interface OnServiceAreaClickListener{
        void OnServiceAreaClickListener(String areaId);
    }
    private OnServiceAreaClickListener onServiceAreaClickListener;

    public OnServiceAreaClickListener getOnServiceAreaClickListener() {
        return onServiceAreaClickListener;
    }

    public void setOnServiceAreaClickListener(OnServiceAreaClickListener onServiceAreaClickListener) {
        this.onServiceAreaClickListener = onServiceAreaClickListener;
    }



    public interface OnJaitingClickListener{
        void OnJaitingClickListener(String jiatingId);
    }
    private OnJaitingClickListener onJaitingClickListener;

    public OnJaitingClickListener getOnJaitingClickListener() {
        return onJaitingClickListener;
    }

    public void setOnJaitingClickListener(OnJaitingClickListener onJaitingClickListener) {
        this.onJaitingClickListener = onJaitingClickListener;
    }


    public interface OnShangyeClickListener{
        void OnShangyeClickListener(String shangyeId);
    }
    private OnShangyeClickListener onShangyeClickListener;

    public OnShangyeClickListener getOnShangyeClickListener() {
        return onShangyeClickListener;
    }

    public void setOnShangyeClickListener(OnShangyeClickListener onShangyeClickListener) {
        this.onShangyeClickListener = onShangyeClickListener;
    }



    public interface OnChooseCityListener{
        void OnChooseCityListener();
    }

    private OnChooseCityListener onChooseCityListener;

    public OnChooseCityListener getOnChooseCityListener() {
        return onChooseCityListener;
    }

    public void setOnChooseCityListener(OnChooseCityListener onChooseCityListener) {
        this.onChooseCityListener = onChooseCityListener;
    }


    public interface OnResetDataListener{
        void OnResetDataListener();
    }
    private OnResetDataListener onResetDataListener;

    public OnResetDataListener getOnResetDataListener() {
        return onResetDataListener;
    }

    public void setOnResetDataListener(OnResetDataListener onResetDataListener) {
        this.onResetDataListener = onResetDataListener;
    }

    public interface OnOkListener{
        void OnOkListener();
    }

    private OnOkListener onOkListener;

    public OnOkListener getOnOkListener() {
        return onOkListener;
    }

    public void setOnOkListener(OnOkListener onOkListener) {
        this.onOkListener = onOkListener;
    }
}
