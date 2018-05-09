package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.activity.CoYouHuiActivity;
import com.tbs.tbs_mj.bean.CompanyBannerItem;
import com.tbs.tbs_mj.bean.GongsiItem;
import com.tbs.tbs_mj.bean.RCompanyBean;
import com.tbs.tbs_mj.customview.MyRatingBar;
import com.tbs.tbs_mj.utils.DensityUtil;

import java.util.List;

public class YouXuanGongSiAdapter extends BaseAdapter{
    private Context context;
    private List<GongsiItem> dataList;
    private List<CompanyBannerItem> bannerList;
    private LayoutInflater inflater;
    private List<RCompanyBean> rCompanyBeanList;

    public YouXuanGongSiAdapter(Context context, List<GongsiItem> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View itemView, ViewGroup viewGroup) {
        GongsiViewHolder gongsiViewHodler = null;
        if(itemView == null){
            gongsiViewHodler = new GongsiViewHolder();
            itemView = inflater.inflate(R.layout.gongsi_layout, null, false);
            gongsiViewHodler.iv = (ImageView) itemView.findViewById(R.id.ivCComopanyIcon);
            gongsiViewHodler.name = (TextView) itemView.findViewById(R.id.tvGongsiName);
            gongsiViewHodler.vGongsi = (ImageView) itemView.findViewById(R.id.vGongsi);
            gongsiViewHodler.tuijianGongsi = (ImageView) itemView.findViewById(R.id.tuijianGongsi);
            gongsiViewHodler.ratingBar = (MyRatingBar) itemView.findViewById(R.id.ratingBar);
            gongsiViewHodler.tvZixunNum = (TextView) itemView.findViewById(R.id.zixunNum);
            gongsiViewHodler.tvCaseAndCase = (TextView) itemView.findViewById(R.id.tvCaseAndCase);
            gongsiViewHodler.ccDistance = (TextView) itemView.findViewById(R.id.ccDistance);
            gongsiViewHodler.tvYouhui = (TextView) itemView.findViewById(R.id.tvYouhui);
            itemView.setTag(gongsiViewHodler);
        }else {
            gongsiViewHodler = (GongsiViewHolder) itemView.getTag();
        }

        gongsiViewHodler.name.setText(dataList.get(position).getName());

//        int screenWidth = DensityUtil.dip2px(context, 80);
//        ViewGroup.LayoutParams lp = gongsiViewHodler.iv.getLayoutParams();
//        lp.width = screenWidth;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        gongsiViewHodler.iv.setLayoutParams(lp);
//        gongsiViewHodler.iv.setMaxWidth(screenWidth);
//        gongsiViewHodler.iv.setMaxHeight(screenWidth * 4);

        Glide.with(context).load(dataList.get(position).getImg_url()).placeholder(R.drawable.new_home_loading).error(R.drawable.new_home_loading).into(gongsiViewHodler.iv);

        if(dataList.get(position).getRecommend()!=null && "1".equals(dataList.get(position).getRecommend())){
            gongsiViewHodler.tuijianGongsi.setVisibility(View.VISIBLE);
        }else {
            gongsiViewHodler.tuijianGongsi.setVisibility(View.GONE);
        }

        if(dataList.get(position).getCertification()!=null && "1".equals(dataList.get(position).getCertification())){
            gongsiViewHodler.vGongsi.setVisibility(View.VISIBLE);
        }else {
            gongsiViewHodler.vGongsi.setVisibility(View.GONE);
        }

        String rate = dataList.get(position).getGrade();
        if(rate!=null && !"".equals(rate)){
            gongsiViewHodler.ratingBar.setClickable(false);
            gongsiViewHodler.ratingBar.setStar(Float.parseFloat(rate));
        }

        String shejiText = "";
        String caseText = "";
        String shejiNum = dataList.get(position).getSuite_count();
        String caseNum = dataList.get(position).getCase_count();
        if(!"".equals(shejiNum)){
            shejiText = "设计: " + shejiNum;
        }
        if(!"".equals(caseNum)){
            caseText = "案例: " + caseNum;
        }
        gongsiViewHodler.tvCaseAndCase.setText(shejiText + "    " + caseText);
        String zxNum = dataList.get(position).getView_count();
        gongsiViewHodler.tvZixunNum.setText("("+zxNum+")");
        String prop = dataList.get(position).getPromotion_title();
        if(prop!=null && !"".equals(prop)){
            gongsiViewHodler.tvYouhui.setText(" "+prop);
            gongsiViewHodler.tvYouhui.setVisibility(View.VISIBLE);
            gongsiViewHodler.tvYouhui.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CoYouHuiActivity.class);
                    intent.putExtra("mCompanyId", dataList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }else {
            gongsiViewHodler.tvYouhui.setVisibility(View.GONE);
        }

        String ds = dataList.get(position).getDistance();
        if(!TextUtils.isEmpty(ds)){
            gongsiViewHodler.ccDistance.setText(dataList.get(position).getDistance());
            gongsiViewHodler.ccDistance.setVisibility(View.VISIBLE);
        }else {
            gongsiViewHodler.ccDistance.setVisibility(View.GONE);
        }
        return itemView;
    }

    class GongsiViewHolder{
        ImageView iv;
        TextView name;
        ImageView vGongsi;
        ImageView tuijianGongsi;
        MyRatingBar ratingBar;
        TextView tvZixunNum;
        TextView tvCaseAndCase;
        TextView ccDistance;
        TextView tvYouhui;
    }
}
