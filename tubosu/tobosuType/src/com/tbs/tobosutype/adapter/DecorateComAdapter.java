package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.activity.DecComActivity;
import com.tbs.tobosutype.bean._DecorateCom;
import com.tbs.tobosutype.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2018/9/10 10:01.
 * 装修公司 4.0版本的适配器
 */
public class DecorateComAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String TAG = "DecorateComAdapter";
    private List<_DecorateCom.DataBean> mDecorateComList;

    public DecorateComAdapter(Context context, List<_DecorateCom.DataBean> decorateComList) {
        this.mContext = context;
        this.mDecorateComList = decorateComList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_decorate_com, parent, false);
        DecorateViewHolder decorateViewHolder = new DecorateViewHolder(view);
        return decorateViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DecorateViewHolder) {
            //整个层级点击事件
            ((DecorateViewHolder) holder).item_dec_com_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进入装修公司的收首页
                    Intent intentToDecCom = new Intent(mContext, DecComActivity.class);
                    intentToDecCom.putExtra("mCompanyId", mDecorateComList.get(position).getId());
                    mContext.startActivity(intentToDecCom);
                }
            });
            //设置头像
            GlideUtils.glideLoader(mContext, mDecorateComList.get(position).getImg_url(), ((DecorateViewHolder) holder).item_dec_com_icon_img);
            //名称
            ((DecorateViewHolder) holder).item_dec_com_name_tv.setText("" + mDecorateComList.get(position).getName());
            //是否认证
            if (mDecorateComList.get(position).getCertification().equals("1")) {
                ((DecorateViewHolder) holder).item_dec_com_is_vp_img.setVisibility(View.VISIBLE);
            } else {
                ((DecorateViewHolder) holder).item_dec_com_is_vp_img.setVisibility(View.GONE);
            }
            //距离
            ((DecorateViewHolder) holder).item_dec_com_long_tv.setText("" + mDecorateComList.get(position).getDistance());
            //咨询数
            ((DecorateViewHolder) holder).item_dec_com_zixun_num_tv.setText("咨询:" + mDecorateComList.get(position).getView_count());
            //案例数
            ((DecorateViewHolder) holder).item_dec_com_fangan_num_tv.setText("方案:" + mDecorateComList.get(position).getCase_count());
            //效果图
            ((DecorateViewHolder) holder).item_dec_com_xiaoguotu_num_tv.setText("效果图:" + mDecorateComList.get(position).getSuite_count());
            //优惠信息
            if (TextUtils.isEmpty(mDecorateComList.get(position).getPromotion_title())) {
                //优惠信息为空
                ((DecorateViewHolder) holder).item_dec_com_youhui_ll.setVisibility(View.GONE);
            } else {
                ((DecorateViewHolder) holder).item_dec_com_youhui_ll.setVisibility(View.VISIBLE);
                ((DecorateViewHolder) holder).item_dec_com_youhui_tv.setText("" + mDecorateComList.get(position).getPromotion_title());
            }
            //设置公司星级
            if (mDecorateComList.get(position).getGrade() == 0) {
                //没有星级
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_1);
////                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_2);
////                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_3);
////                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_4);
////                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_grey);
            } else if (mDecorateComList.get(position).getGrade() == 1) {
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_1);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_2);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_3);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_4);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_grey);
            } else if (mDecorateComList.get(position).getGrade() == 2) {
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_1);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_2);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_3);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_4);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_grey);
            } else if (mDecorateComList.get(position).getGrade() == 3) {
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_1);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_2);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_3);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_4);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_grey);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_grey);
            } else if (mDecorateComList.get(position).getGrade() == 4) {
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_1);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_2);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_3);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_4);
//                Glide.with(mContext).load(R.drawable.star_grey).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_grey);
            } else if (mDecorateComList.get(position).getGrade() == 5) {
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_1);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_2);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_3);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_4);
//                Glide.with(mContext).load(R.drawable.star_green).into(((DecorateViewHolder) holder).item_dec_com_start_5);
                ((DecorateViewHolder) holder).item_dec_com_start_1.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_2.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_3.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_4.setImageResource(R.drawable.star_green);
                ((DecorateViewHolder) holder).item_dec_com_start_5.setImageResource(R.drawable.star_green);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDecorateComList == null ? 0 : mDecorateComList.size();
    }

    private class DecorateViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_dec_com_icon_img;//图标
        private TextView item_dec_com_name_tv;//名称
        private ImageView item_dec_com_is_vp_img;//是否认证
        private TextView item_dec_com_long_tv;//距离
        private ImageView item_dec_com_start_1;//星级
        private ImageView item_dec_com_start_2;//星级
        private ImageView item_dec_com_start_3;//星级
        private ImageView item_dec_com_start_4;//星级
        private ImageView item_dec_com_start_5;//星级
        private TextView item_dec_com_zixun_num_tv;//咨询数
        private TextView item_dec_com_fangan_num_tv;//方案数
        private TextView item_dec_com_xiaoguotu_num_tv;//效果图数量
        private LinearLayout item_dec_com_youhui_ll;//优惠信息层
        private TextView item_dec_com_youhui_tv;//优惠说明
        private RelativeLayout item_dec_com_rl;//整个层级

        public DecorateViewHolder(View itemView) {
            super(itemView);
            item_dec_com_icon_img = itemView.findViewById(R.id.item_dec_com_icon_img);
            item_dec_com_name_tv = itemView.findViewById(R.id.item_dec_com_name_tv);
            item_dec_com_is_vp_img = itemView.findViewById(R.id.item_dec_com_is_vp_img);
            item_dec_com_long_tv = itemView.findViewById(R.id.item_dec_com_long_tv);
            item_dec_com_start_1 = itemView.findViewById(R.id.item_dec_com_start_1);
            item_dec_com_start_2 = itemView.findViewById(R.id.item_dec_com_start_2);
            item_dec_com_start_3 = itemView.findViewById(R.id.item_dec_com_start_3);
            item_dec_com_start_4 = itemView.findViewById(R.id.item_dec_com_start_4);
            item_dec_com_start_5 = itemView.findViewById(R.id.item_dec_com_start_5);
            item_dec_com_zixun_num_tv = itemView.findViewById(R.id.item_dec_com_zixun_num_tv);
            item_dec_com_fangan_num_tv = itemView.findViewById(R.id.item_dec_com_fangan_num_tv);
            item_dec_com_xiaoguotu_num_tv = itemView.findViewById(R.id.item_dec_com_xiaoguotu_num_tv);
            item_dec_com_youhui_ll = itemView.findViewById(R.id.item_dec_com_youhui_ll);
            item_dec_com_youhui_tv = itemView.findViewById(R.id.item_dec_com_youhui_tv);
            item_dec_com_rl = itemView.findViewById(R.id.item_dec_com_rl);
        }
    }
}
