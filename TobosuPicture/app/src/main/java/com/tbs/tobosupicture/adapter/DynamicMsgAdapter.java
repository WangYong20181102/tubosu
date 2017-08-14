package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.DynamicDetailActivity;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._DynamicMsg;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/10 10:18.
 */

public class DynamicMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<_DynamicMsg> dynamicMsgList;

    public DynamicMsgAdapter(Context context, List<_DynamicMsg> dynamicMsgList) {
        this.mContext = context;
        this.dynamicMsgList = dynamicMsgList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_msg, parent, false);
        DynamicMsgHolder dynamicMsgHolder = new DynamicMsgHolder(view);
        return dynamicMsgHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DynamicMsgHolder) {
            //昵称
            ((DynamicMsgHolder) holder).item_dm_nick.setText("" + dynamicMsgList.get(position).getNick());
            //标题
            ((DynamicMsgHolder) holder).item_dm_title.setText("" + dynamicMsgList.get(position).getTitle());
            //动态时间以及动态相关
            ((DynamicMsgHolder) holder).item_dm_time_content.setText("" + dynamicMsgList.get(position).getAdd_time() + " " + dynamicMsgList.get(position).getContent());
            //头像
            GlideUtils.glideLoader(mContext, dynamicMsgList.get(position).getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((DynamicMsgHolder) holder).item_dm_icon, 0);
            //头像点击事件
            ((DynamicMsgHolder) holder).item_dm_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击进入用户的个人主页
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", dynamicMsgList.get(position).getUid());
                    intent.putExtra("is_virtual_user", dynamicMsgList.get(position).getUser_type());
                    mContext.startActivity(intent);
                }
            });
            //整个动态点击事件
            ((DynamicMsgHolder) holder).item_dm_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    intent.putExtra("dynamic_id", dynamicMsgList.get(position).getDynamic_id());
                    intent.putExtra("commented_uid", dynamicMsgList.get(position).getDynamic_uid());
                    intent.putExtra("is_virtual_user", dynamicMsgList.get(position).getDynamic_user_type());
                    mContext.startActivity(intent);
                }
            });
            //封面图
            GlideUtils.glideLoader(mContext, dynamicMsgList.get(position).getImage_url(), R.mipmap.loading_img_fail, R.mipmap.loading_img, ((DynamicMsgHolder) holder).item_dm_image);
        }
    }

    @Override
    public int getItemCount() {
        return dynamicMsgList != null ? dynamicMsgList.size() : 0;
    }

    class DynamicMsgHolder extends RecyclerView.ViewHolder {
        private TextView item_dm_title;//标题
        private TextView item_dm_nick;//昵称
        private TextView item_dm_time_content;//时间和内容
        private ImageView item_dm_image;//封面图
        private ImageView item_dm_icon;//头像
        private View item_dm_click;//点击触碰提

        public DynamicMsgHolder(View itemView) {
            super(itemView);
            item_dm_title = (TextView) itemView.findViewById(R.id.item_dm_title);
            item_dm_nick = (TextView) itemView.findViewById(R.id.item_dm_nick);
            item_dm_time_content = (TextView) itemView.findViewById(R.id.item_dm_time_content);
            item_dm_image = (ImageView) itemView.findViewById(R.id.item_dm_image);
            item_dm_icon = (ImageView) itemView.findViewById(R.id.item_dm_icon);
            item_dm_click = (View) itemView.findViewById(R.id.item_dm_click);
        }
    }
}
