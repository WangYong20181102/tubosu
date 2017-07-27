package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.PersonHomePageActivity;
import com.tbs.tobosupicture.bean._ZanUser;
import com.tbs.tobosupicture.utils.GlideUtils;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/7/25 10:08.
 * 显示点赞过的人的适配器
 */

public class ShowZanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<_ZanUser.PraiseUser> praiseUserList;

    public ShowZanAdapter(Context context, List<_ZanUser.PraiseUser> praiseUsers) {
        this.mContext = context;
        this.praiseUserList = praiseUsers;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_his_fans, parent, false);
        ItemView itemViewHolder = new ItemView(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemView) {
            GlideUtils.glideLoader(mContext, praiseUserList.get(position).getIcon(), R.mipmap.default_icon, R.mipmap.default_icon, ((ItemView) holder).zanUserIcon, 0);
            ((ItemView) holder).zanUserName.setText("" + praiseUserList.get(position).getNick());
            if (TextUtils.isEmpty(praiseUserList.get(position).getPersonal_signature())) {
                ((ItemView) holder).zanUserSign.setVisibility(View.GONE);
            } else {
                ((ItemView) holder).zanUserSign.setText("" + praiseUserList.get(position).getPersonal_signature());
            }
            ((ItemView) holder).zanUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PersonHomePageActivity.class);
                    intent.putExtra("homepageUid", praiseUserList.get(position).getUid());
                    intent.putExtra("is_virtual_user", "2");
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return praiseUserList != null ? praiseUserList.size() : 0;
    }

    class ItemView extends RecyclerView.ViewHolder {
        private ImageView zanUserIcon;//头像
        private TextView zanUserName;//用户的昵称
        private TextView zanUserSign;//用户的签名

        public ItemView(View itemView) {
            super(itemView);
            zanUserIcon = (ImageView) itemView.findViewById(R.id.item_hisfans_icon);
            zanUserName = (TextView) itemView.findViewById(R.id.item_hisfans_name);
            zanUserSign = (TextView) itemView.findViewById(R.id.item_hisfans_sign);
        }
    }
}
