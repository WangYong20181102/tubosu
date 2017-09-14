package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.NewArticleDetailActivity;
import com.tobosu.mydecorate.activity.NewAuthorDetailActivity;
import com.tobosu.mydecorate.entity._HomePage;
import com.tobosu.mydecorate.util.GlideUtils;
import com.tobosu.mydecorate.util.Util;
import java.util.List;

/**
 * 首页 作者 适配器
 *
 * Created by Lie on 2017/8/7.
 */

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private String TAG = "AuthorAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    private List<_HomePage.Author> dataList;

    public AuthorAdapter(Context mContext, List dataList){
        this.mContext = mContext;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public AuthorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_adapter_home_author_layout, null);
        AuthorViewHolder holder = new AuthorViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(AuthorViewHolder holder, final int position) {

        String nickname = dataList.get(position).getNick();
        if(nickname.length() < 4){
            holder.item_author_name.setText(dataList.get(position).getNick());
        }else {
            holder.item_author_name.setText(dataList.get(position).getNick().substring(0, 4) + "...");
        }

        holder.item_wenzhang.setText(dataList.get(position).getArticle_count());
        holder.item_liulang.setText(dataList.get(position).getView_count());

        holder.frameLayout.bringToFront();
        GlideUtils.glideLoader(mContext, dataList.get(position).getIcon(), 0, R.mipmap.jiazai_loading,holder.item_author_icon, 0);
        holder.item_author_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewAuthorDetailActivity.class);
                intent.putExtra("author_id", dataList.get(position).getUid());
                intent.putExtra("page_num", dataList.get(position).getArticle_count());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        if(position == 0){
            holder.iv_rank.setBackgroundResource(R.mipmap.gold);
        }else if(position == 1){
            holder.iv_rank.setBackgroundResource(R.mipmap.silver);
        }else if(position == 2){
            holder.iv_rank.setBackgroundResource(R.mipmap.brown);
        }else if(position == 3){
            holder.iv_rank.setBackgroundResource(R.mipmap.img4);
        }else if(position == 4){
            holder.iv_rank.setBackgroundResource(R.mipmap.img5);
        }else if(position == 5){
            holder.iv_rank.setBackgroundResource(R.mipmap.img6);
        }else if(position == 6){
            holder.iv_rank.setBackgroundResource(R.mipmap.img7);
        }else if(position == 7){
            holder.iv_rank.setBackgroundResource(R.mipmap.img8);
        }


        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {

        } else {
            holder.rel_shape.setBackgroundResource(R.drawable.nhf_wz_shape);
        }
    }

    @Override
    public int getItemCount() {
        return dataList==null ? 0 : dataList.size();
    }

    class AuthorViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_rank;
        private ImageView item_author_icon;
        private TextView item_author_name;
        private TextView item_liulang;
        private TextView item_wenzhang;
        private FrameLayout frameLayout;
        private RelativeLayout rel_shape;

        public AuthorViewHolder(View itemView) {
            super(itemView);
            iv_rank = (ImageView) itemView.findViewById(R.id.iv_rank);
            item_author_icon = (ImageView) itemView.findViewById(R.id.item_author_icon);
            item_author_name = (TextView) itemView.findViewById(R.id.item_author_name);
            item_liulang = (TextView) itemView.findViewById(R.id.item_liulang);
            item_wenzhang = (TextView) itemView.findViewById(R.id.item_wenzhang);
            frameLayout = (FrameLayout) itemView.findViewById(R.id.frad);
            rel_shape = (RelativeLayout) itemView.findViewById(R.id.rel_shape);
        }
    }
}
