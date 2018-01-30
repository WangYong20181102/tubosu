package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.tbs.tobosutype.bean.RCompanyBean;
import com.tbs.tobosutype.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lie on 2017/11/29.
 */

public class RCompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<RCompanyBean> dataList = new ArrayList<>();
    private LayoutInflater inflater;


    public RCompanyAdapter(Context context, List<RCompanyBean> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View body = inflater.inflate(R.layout.layout_recommand_comany_item, parent, false);
        RCompanyViewHolder rcHolder = new RCompanyViewHolder(body);
        return rcHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RCompanyViewHolder) {
            RCompanyViewHolder rcomHolder = (RCompanyViewHolder) holder;
            rcomHolder.name.setText(dataList.get(position).getName());
            Glide.with(context).load(dataList.get(position).getImg_url())
                    .asBitmap()
                    .error(R.drawable.new_home_loading)
                    .placeholder(R.drawable.new_home_loading)
                    .into(rcomHolder.iv);
            rcomHolder.layoutGongsiR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Util.isNetAvailable(context)){
                        Intent it = new Intent(context, DecComActivity.class);
                        it.putExtra("mCompanyId", dataList.get(position).getId());
                        context.startActivity(it);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class RCompanyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView iv;
        LinearLayout layoutGongsiR;

        public RCompanyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvCompanyRCName);
            iv = itemView.findViewById(R.id.ivCompanyRCIcon);
            layoutGongsiR = (LinearLayout) itemView.findViewById(R.id.layoutGongsiR);
        }
    }
}
