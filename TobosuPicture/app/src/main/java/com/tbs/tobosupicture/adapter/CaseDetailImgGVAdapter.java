package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.SeeImageActivity;
import com.tbs.tobosupicture.bean.CaseDetailEntity;
import com.tbs.tobosupicture.bean.CaseDetailJsonEntity;
import com.tbs.tobosupicture.bean.OnlineDiagram;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyGridView;
import com.tbs.tobosupicture.view.RoundAngleImageView;

import java.util.ArrayList;

/**
 * 施工阶段 适配器
 * Created by Lie on 2017/8/2.
 */

public class CaseDetailImgGVAdapter extends BaseAdapter {
    private String TAG = "CaseDetailImgGVAdapter";
    private ArrayList<OnlineDiagram> dataList;
    private Context context;
    private LayoutInflater inflater;
    private String stage[] = {"水电", "泥木", "油漆", "竣工"};   //阶段：1水电；2泥木；3油漆；4竣工

    public CaseDetailImgGVAdapter(Context context, ArrayList<OnlineDiagram> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return dataList == null ?0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImgViewHolder holder;
        if(convertView==null){
            holder = new ImgViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_case_detail_gv_img, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tvTitleCaseDetailGv);
            holder.gv = (MyGridView) convertView.findViewById(R.id.gv_detail_case_gv);
            holder.iv = (RoundAngleImageView) convertView.findViewById(R.id.iv_detail_case_gv_more);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(holder);
        }else {
            holder = (ImgViewHolder) convertView.getTag();
        }

        ArrayList<String> imgList = dataList.get(position).getImg_url();
        int totalItem = imgList.size();
        if(totalItem % 2 == 0){
            // 偶数
            GvStageAdapter stageAdapter = new GvStageAdapter(context, imgList, dataList.get(position).getId());
            holder.gv.setAdapter(stageAdapter);
            holder.iv.setVisibility(View.GONE);
        }else {
            // 奇数
            holder.iv.setVisibility(View.VISIBLE);
            if(totalItem==1){
                // 仅仅有一个
                String lastUrl = imgList.get(0);
                lastUrl = lastUrl.replace("\\/\\/", "//").replace("\\/", "/");
//                Utils.setErrorLog(TAG, "施工阶段 1个 适配器  " + lastUrl);
                GlideUtils.glideLoader(context, lastUrl, R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, SeeImageActivity.class);
                        Bundle b = new Bundle();
                        b.putString("img_id", dataList.get(position).getId());
                        i.putExtra("img_bundle", b);
                        context.startActivity(i);
                    }
                });
            }else if(totalItem>1){
                // 3 5 7 9 11 ...
                String last_Url = imgList.remove(imgList.size()-1);
                last_Url = last_Url.replace("\\/\\/", "//").replace("\\/", "/");
//                Utils.setErrorLog(TAG, "施工阶段 奇数图片 适配器  " + last_Url);
                GvStageAdapter stageAdapter = new GvStageAdapter(context, imgList, dataList.get(position).getId());
                holder.gv.setAdapter(stageAdapter);

//                Utils.setErrorLog(TAG, "需要加载的图片地址是：" +last_Url);
                GlideUtils.glideLoader(context, last_Url, R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv);
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, SeeImageActivity.class);
                        Bundle b = new Bundle();
                        b.putString("img_id", dataList.get(position).getId());
                        i.putExtra("img_bundle", b);
                        context.startActivity(i);
                    }
                });
            }
        }

        if("1".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[0]);
        }else if("2".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[1]);
        }else if("3".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[2]);
        }else if("4".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[3]);
        }

        holder.tvDesc.setText(dataList.get(position).getDescription());

        return convertView;
    }

    class ImgViewHolder{
        MyGridView gv;
        TextView tv;
        RoundAngleImageView iv;  // 奇数，多出来的一张
        TextView tvDesc;
    }
}
