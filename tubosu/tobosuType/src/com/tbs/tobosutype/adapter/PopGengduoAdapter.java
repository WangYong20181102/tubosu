package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._CheckInfo;

/**
 * Created by Mr.Lin on 2018/9/19 19:37.
 */
public class PopGengduoAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private _CheckInfo mCheckInfo;

    public PopGengduoAdapter(Context context, _CheckInfo checkInfo) {
        this.mContext = context;
        this.mCheckInfo = checkInfo;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            //返回头部  家庭装修的标题
            return 0;
        } else if (position == 1) {
            //返回家庭装修的列表
            return 1;
        } else if (position == 2) {
            //
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_jiatingzhuangxiu, parent, false);
            JiatingViewHolder jiatingViewHolder = new JiatingViewHolder(view);
            return jiatingViewHolder;
        } else if (viewType == 1) {
            //家庭装修列表
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_jiating_liebiao, parent, false);
            JiatingLiebiaoViewHolder jiatingLiebiaoViewHolder = new JiatingLiebiaoViewHolder(view);
            return jiatingLiebiaoViewHolder;
        } else if (viewType == 2) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_shangyezhuangxiu, parent, false);
            ShangyeViewHolder shangyeViewHolder = new ShangyeViewHolder(view);
            return shangyeViewHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_shangyeliebiao, parent, false);
            ShangyeLiebiaoViewHolder shangyeLiebiaoViewHolder = new ShangyeLiebiaoViewHolder(view);
            return shangyeLiebiaoViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JiatingViewHolder) {

        } else if (holder instanceof JiatingLiebiaoViewHolder) {
            GridLayoutManager mGridLayoutManager1 = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
            ((JiatingLiebiaoViewHolder) holder).jiating_liebiao_recycler.setLayoutManager(mGridLayoutManager1);
            PopJiatingAdapter popJiatingAdapter = new PopJiatingAdapter(mContext, mCheckInfo.getData().getMore().get(0).getSub_title(), -1);
            ((JiatingLiebiaoViewHolder) holder).jiating_liebiao_recycler.setAdapter(popJiatingAdapter);
            popJiatingAdapter.notifyDataSetChanged();
        } else if (holder instanceof ShangyeViewHolder) {

        } else if (holder instanceof ShangyeLiebiaoViewHolder) {
            GridLayoutManager mGridLayoutManager2 = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
            ((ShangyeLiebiaoViewHolder) holder).shangye_liebiao_recycler.setLayoutManager(mGridLayoutManager2);
            PopJiatingAdapter popJiatingAdapter = new PopJiatingAdapter(mContext, mCheckInfo.getData().getMore().get(1).getSub_title(), -1);
            ((ShangyeLiebiaoViewHolder) holder).shangye_liebiao_recycler.setAdapter(popJiatingAdapter);
            popJiatingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onClick(View v) {

    }

    private class JiatingViewHolder extends RecyclerView.ViewHolder {
        private TextView jiatingzx_tv;

        public JiatingViewHolder(View itemView) {
            super(itemView);
            jiatingzx_tv = itemView.findViewById(R.id.jiatingzx_tv);
        }
    }

    private class JiatingLiebiaoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView jiating_liebiao_recycler;

        public JiatingLiebiaoViewHolder(View itemView) {
            super(itemView);
            jiating_liebiao_recycler = itemView.findViewById(R.id.jiating_liebiao_recycler);
        }
    }

    private class ShangyeViewHolder extends RecyclerView.ViewHolder {
        private TextView shangyezx_tv;

        public ShangyeViewHolder(View itemView) {
            super(itemView);
            shangyezx_tv = itemView.findViewById(R.id.shangyezx_tv);
        }
    }

    private class ShangyeLiebiaoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView shangye_liebiao_recycler;

        public ShangyeLiebiaoViewHolder(View itemView) {
            super(itemView);
            shangye_liebiao_recycler = itemView.findViewById(R.id.shangye_liebiao_recycler);
        }
    }
}
