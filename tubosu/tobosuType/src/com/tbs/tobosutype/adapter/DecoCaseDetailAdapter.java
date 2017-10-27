package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.LookPhotoActivity;
import com.tbs.tobosutype.bean._DecoCaseDetail;
import com.tbs.tobosutype.utils.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/10/25 09:56.
 * 案例详情的的适配器 作用于案例详情页
 * 整个适配器包含了头部的显示和尾部空间展示的显示
 */

public class DecoCaseDetailAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "DecoCaseDetailAdapter";
    private _DecoCaseDetail mDecoCaseDetail;
    private Gson mGson;

    public DecoCaseDetailAdapter(Context context, _DecoCaseDetail decoCaseDetail) {
        this.mContext = context;
        this.mDecoCaseDetail = decoCaseDetail;
        mGson = new Gson();
    }

    @Override
    public void onClick(View v) {
        if (onItemBtnClickLister != null) {
            onItemBtnClickLister.onItemBtnClick(v, (int) v.getTag());
        }
    }

    public static interface OnItemBtnClickLister {
        void onItemBtnClick(View view, int position);
    }

    private OnItemBtnClickLister onItemBtnClickLister = null;

    public void setOnItemBtnClickLister(OnItemBtnClickLister onItemBtnClickLister) {
        this.onItemBtnClickLister = onItemBtnClickLister;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;//展示头部
        } else {
            return 1;//展示尾部
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //展示头部的信息
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_deco_case_detail_head, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(view);
            headViewHolder.item_deco_detail_back.setOnClickListener(this);
            headViewHolder.item_deco_detail_share.setOnClickListener(this);
            return headViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_deco_case_detail_foot, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).item_deco_detail_share.setTag(position);
            ((HeadViewHolder) holder).item_deco_detail_back.setTag(position);
            //布局头部展示的信息
            ((HeadViewHolder) holder).item_deco_detail_head_ll.setBackgroundColor(Color.parseColor("#ffffff"));
            //封面图
            ImageLoaderUtil.loadImage(mContext, ((HeadViewHolder) holder).item_deco_detail_head_img, mDecoCaseDetail.getCover_url());
            //封面图的蒙层
            ((HeadViewHolder) holder).item_deco_detail_view.setBackgroundColor(Color.parseColor("#ffffff"));
            //拥有者
            if (TextUtils.isEmpty(mDecoCaseDetail.getOwner_name())) {
                ((HeadViewHolder) holder).item_deco_detail_title_name.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_title_name.setText("" + mDecoCaseDetail.getOwner_name());
            }
            //风格
            if (TextUtils.isEmpty(mDecoCaseDetail.getStyle_name())) {
                ((HeadViewHolder) holder).item_deco_detail_title_msg.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_title_msg.setText("风格: " + mDecoCaseDetail.getStyle_name());
            }
            //设计师
            if (TextUtils.isEmpty(mDecoCaseDetail.getDesigner_name())) {
                //没有设计师 整个设计师层隐藏
                ((HeadViewHolder) holder).item_deco_detail_design_ll.setVisibility(View.GONE);
            } else {
                //设置设计师的头像
                ImageLoaderUtil.loadCircleImage(mContext, ((HeadViewHolder) holder).item_deco_detail_design_icon, mDecoCaseDetail.getDesigner_icon());
                //设置设计师的名字
                ((HeadViewHolder) holder).item_deco_detail_design_name.setText("" + mDecoCaseDetail.getDesigner_name());
                //设置设计师的段位
                ((HeadViewHolder) holder).item_deco_detail_design_title.setText("" + mDecoCaseDetail.getDesigner_position());
                /// TODO: 2017/10/25  找他设计的按钮点击事件
                ((HeadViewHolder) holder).item_deco_detail_find_he_design.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到H5页面
                    }
                });
            }
            //房屋信息
            //户型
            if (TextUtils.isEmpty(mDecoCaseDetail.getLayout_name())) {
                ((HeadViewHolder) holder).item_deco_detail_huxing_ll.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_huxing.setText(mDecoCaseDetail.getLayout_name());
            }
            //面积
            if (TextUtils.isEmpty(mDecoCaseDetail.getArea())) {
                ((HeadViewHolder) holder).item_deco_detail_mianji_ll.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_mianji.setText(mDecoCaseDetail.getArea() + "㎡");
            }
            //位置
            if (TextUtils.isEmpty(mDecoCaseDetail.getProvince_name()) && TextUtils.isEmpty(mDecoCaseDetail.getCity_name()) && TextUtils.isEmpty(mDecoCaseDetail.getCommunity_name())) {
                ((HeadViewHolder) holder).item_deco_detail_weizhi_ll.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_weizhi.setText("" + mDecoCaseDetail.getCity_name() + " " + mDecoCaseDetail.getProvince_name() + " " + mDecoCaseDetail.getCommunity_name());
            }
            //花费
            if (TextUtils.isEmpty(mDecoCaseDetail.getPrice())) {
                ((HeadViewHolder) holder).item_deco_detail_huafei_ll.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_huafei.setText("约" + mDecoCaseDetail.getPrice() + "万元");
            }
            //设置简介
            if (TextUtils.isEmpty(mDecoCaseDetail.getDesc())) {
                ((HeadViewHolder) holder).item_deco_detail_jianjie_ll.setVisibility(View.GONE);
            } else {
                ((HeadViewHolder) holder).item_deco_detail_jianjie.setText("" + mDecoCaseDetail.getDesc());
            }
        } else if (holder instanceof FootViewHolder) {
            //布局尾部展示的信息
            ((FootViewHolder) holder).item_deco_detail_foot_title.setText("# " + mDecoCaseDetail.getSpace_info().get(position - 1).getSpace_name() + " #");
            ImageLoaderUtil.loadImage(mContext, ((FootViewHolder) holder).item_deco_detail_foot_img, mDecoCaseDetail.getSpace_info().get(position - 1).getThumb_img_url());
            //图片点击事件进入到查看图片详情页
            ((FootViewHolder) holder).item_deco_detail_foot_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String lookPhotoJson = mGson.toJson(mDecoCaseDetail);
                    Intent intent = new Intent(mContext, LookPhotoActivity.class);
                    intent.putExtra("lookPhotoJson", lookPhotoJson);//数据对象
                    intent.putExtra("position", position - 1);//点击的位置
                    intent.putExtra("photoDesc", mDecoCaseDetail.getSpace_info().get(position - 1).getSpace_name());//图片的文字描述
                    int listSize = mDecoCaseDetail.getSpace_info().size();//列表总长
                    intent.putExtra("positionDesc", "" + position + "/" + listSize);//所在的位置描述
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDecoCaseDetail.getSpace_info() != null ? mDecoCaseDetail.getSpace_info().size() + 1 : 1;
    }

    //头部展示的信息
    class HeadViewHolder extends RecyclerView.ViewHolder {

        private ImageView item_deco_detail_head_img;//封面图
        private LinearLayout item_deco_detail_head_ll;//整个页面的层级
        private LinearLayout item_deco_detail_back;//fan hui anniu
        private LinearLayout item_deco_detail_share;//fan hui anniu
        private View item_deco_detail_view;//蒙层布局

        private LinearLayout item_deco_detail_name_title_ll;//XX的家以及风格
        private TextView item_deco_detail_title_name;//郭先生的家
        private TextView item_deco_detail_title_msg;//风格：北欧

        private LinearLayout item_deco_detail_design_ll;//设计师模块
        private ImageView item_deco_detail_design_icon;//设计师的头像
        private TextView item_deco_detail_design_name;//害羞的小狮子
        private TextView item_deco_detail_design_title;//首席设计师
        private TextView item_deco_detail_find_he_design;//找TA免费设计

        private LinearLayout item_deco_detail_house_msg_ll;//房屋信息
        private LinearLayout item_deco_detail_huxing_ll;//户型
        private TextView item_deco_detail_huxing;//户型展示

        private LinearLayout item_deco_detail_mianji_ll;//面积
        private TextView item_deco_detail_mianji;//面积展示

        private LinearLayout item_deco_detail_weizhi_ll;//位置
        private TextView item_deco_detail_weizhi;//位置展示

        private LinearLayout item_deco_detail_huafei_ll;//花费
        private TextView item_deco_detail_huafei;//花费展示

        private LinearLayout item_deco_detail_jianjie_ll;//简介
        private TextView item_deco_detail_jianjie;//简介展示

        public HeadViewHolder(View itemView) {
            super(itemView);
            item_deco_detail_view = (View) itemView.findViewById(R.id.item_deco_detail_view);
            item_deco_detail_head_img = (ImageView) itemView.findViewById(R.id.item_deco_detail_head_img);
            item_deco_detail_name_title_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_name_title_ll);
            item_deco_detail_head_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_head_ll);
            item_deco_detail_title_name = (TextView) itemView.findViewById(R.id.item_deco_detail_title_name);
            item_deco_detail_title_msg = (TextView) itemView.findViewById(R.id.item_deco_detail_title_msg);

            item_deco_detail_design_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_design_ll);
            item_deco_detail_back = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_back);
            item_deco_detail_share = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_share);

            item_deco_detail_design_icon = (ImageView) itemView.findViewById(R.id.item_deco_detail_design_icon);
            item_deco_detail_design_name = (TextView) itemView.findViewById(R.id.item_deco_detail_design_name);
            item_deco_detail_design_title = (TextView) itemView.findViewById(R.id.item_deco_detail_design_title);
            item_deco_detail_find_he_design = (TextView) itemView.findViewById(R.id.item_deco_detail_find_he_design);
            item_deco_detail_house_msg_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_house_msg_ll);
            item_deco_detail_huxing_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_huxing_ll);
            item_deco_detail_huxing = (TextView) itemView.findViewById(R.id.item_deco_detail_huxing);
            item_deco_detail_mianji_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_mianji_ll);
            item_deco_detail_mianji = (TextView) itemView.findViewById(R.id.item_deco_detail_mianji);
            item_deco_detail_weizhi_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_weizhi_ll);
            item_deco_detail_weizhi = (TextView) itemView.findViewById(R.id.item_deco_detail_weizhi);
            item_deco_detail_huafei_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_huafei_ll);
            item_deco_detail_huafei = (TextView) itemView.findViewById(R.id.item_deco_detail_huafei);
            item_deco_detail_jianjie_ll = (LinearLayout) itemView.findViewById(R.id.item_deco_detail_jianjie_ll);
            item_deco_detail_jianjie = (TextView) itemView.findViewById(R.id.item_deco_detail_jianjie);
        }
    }

    //尾部的布局  空间展示  文-图格式
    class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView item_deco_detail_foot_title;//标题
        private ImageView item_deco_detail_foot_img;//展示的图片

        public FootViewHolder(View itemView) {
            super(itemView);
            item_deco_detail_foot_title = (TextView) itemView.findViewById(R.id.item_deco_detail_foot_title);
            item_deco_detail_foot_img = (ImageView) itemView.findViewById(R.id.item_deco_detail_foot_img);
        }
    }
}

