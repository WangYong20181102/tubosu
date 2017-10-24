package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._DecorationCaseItem;

import java.util.ArrayList;

/**
 * Created by Mr.Lin on 2017/10/24 09:17.
 * 土拨鼠3.4版本新增
 */

public class DecorationCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private int adapterState = 1;//列表展示的形态  1--加载更多  2--恢复原状
    private ArrayList<_DecorationCaseItem> decorationCaseItemArrayList;

    public DecorationCaseAdapter(Context context, ArrayList<_DecorationCaseItem> decorationCaseItemArrayList) {
        this.mContext = context;
        this.decorationCaseItemArrayList = decorationCaseItemArrayList;
    }

    //图层的转换
    public void changeAdapterState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    //图层的变化
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//加载更多
        } else {
            return 2;//返回普通图层
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_decoration_case, parent, false);
            ItemCaseViewHolder itemCaseViewHolder = new ItemCaseViewHolder(view);
            return itemCaseViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_image_new_foot, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(view);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemCaseViewHolder) {
            // TODO: 2017/10/24  数据的适配以及点击事件 普通子项
            ((ItemCaseViewHolder) holder).itemCaseImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2017/10/24 点击进入查看图集
                }
            });

        } else if (holder instanceof FootViewHolder) {
            //加载更多的项
            if (position == 1) {
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FootViewHolder) holder).mtextView.setVisibility(View.GONE);
                return;
            }
            if (adapterState == 2) {
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FootViewHolder) holder).mtextView.setVisibility(View.GONE);
            } else if (adapterState == 1) {
                //显示加载更多
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).mtextView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return decorationCaseItemArrayList != null ? decorationCaseItemArrayList.size() + 1 : 0;
    }

    //子项布局
    class ItemCaseViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemCaseLL;//整个案例的层级
        private ImageView itemCaseImage;//案例图
        private TextView itemCaseUserDesc;//案例的用户的描述  例如：深圳 桃园盛景 郭先生的家
        private TextView itemCaseHomeDesc;//案例的描述 例如：80㎡ 2居 6万 全包

        public ItemCaseViewHolder(View itemView) {
            super(itemView);
            itemCaseLL = (LinearLayout) itemView.findViewById(R.id.item_case_ll);
            itemCaseImage = (ImageView) itemView.findViewById(R.id.item_case_image);
            itemCaseUserDesc = (TextView) itemView.findViewById(R.id.item_case_user_desc);
            itemCaseHomeDesc = (TextView) itemView.findViewById(R.id.item_case_home_desc);
        }
    }

    //加载更多
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mtextView;//显示加载更多的字段

        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.image_new_bar);
            mtextView = (TextView) itemView.findViewById(R.id.iamge_new_tv);
        }
    }
}
