package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.NewArticleDetailActivity;
import com.tobosu.mydecorate.entity._HomePage;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;

import java.util.List;

/**
 * 首页 装修头条 适配器
 *
 * Created by Lie on 2017/8/7.
 */

public class DecorateTitleAdapter extends RecyclerView.Adapter<DecorateTitleAdapter.DecorateTitleViewHolder> {
    private String TAG = "DecorateTitleAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    private List<_HomePage.Article> dataList;

    public DecorateTitleAdapter(Context mContext, List dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public DecorateTitleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_adapter_home_decorate_title_layout, null);
        DecorateTitleViewHolder holder = new DecorateTitleViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(DecorateTitleViewHolder holder, final int position) {
        Util.setErrorLog(TAG, dataList.get(position).getImage_url());
        holder.decorate_title_bottom_title.setText(dataList.get(position).getTitle());
        GlideUtils.glideLoader(mContext, dataList.get(position).getImage_url(), 0, R.mipmap.jiazai_loading,holder.decorate_title_img, 1);
        holder.decorate_title_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewArticleDetailActivity.class);
                intent.putExtra("id", dataList.get(position).getAid());
                intent.putExtra("author_id", dataList.get(position).getUid());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {

        } else {
            holder.decorate_title_whole.setBackgroundResource(R.drawable.nhf_wz_shape);
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null ? 0 : dataList.size();
    }

    class DecorateTitleViewHolder extends RecyclerView.ViewHolder{
        private TextView decorate_title_bottom_title;
        private ImageView decorate_title_img;
        private LinearLayout decorate_title_whole;
        public DecorateTitleViewHolder(View itemView) {
            super(itemView);
            decorate_title_bottom_title = (TextView) itemView.findViewById(R.id.decorate_title_bottom_title);
            decorate_title_img = (ImageView) itemView.findViewById(R.id.decorate_title_img);
            decorate_title_whole = (LinearLayout) itemView.findViewById(R.id.decorate_title_whole);
        }
    }
}
