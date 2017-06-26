package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._DecorationExpent;

import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/26 16:40.
 */

public class DecorateExpendAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private List<_DecorationExpent.decorate_record> recordList;

    public DecorateExpendAdapter(Context context, List<_DecorationExpent.decorate_record> recordList) {
        this.mContext = context;
        this.recordList = recordList;
    }

    public static interface OnDecExpenlistItemClickLister {
        void onItemClick(View view);
    }

    private OnDecExpenlistItemClickLister onDecExpenlistItemClickLister;

    public void setOnDecExpenlistItemClickLister(OnDecExpenlistItemClickLister onDecExpenlistItemClickLister) {
        this.onDecExpenlistItemClickLister = onDecExpenlistItemClickLister;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_decorate_expend, parent, false);
        MyDecorateExpendViewHolder holder = new MyDecorateExpendViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyDecorateExpendViewHolder) {
            ((MyDecorateExpendViewHolder) holder).item_da_name.setText("" + recordList.get(position).getExpend_name());
            ((MyDecorateExpendViewHolder) holder).item_da_content.setText("" + recordList.get(position).getContent());
            ((MyDecorateExpendViewHolder) holder).item_da_time.setText("" + recordList.get(position).getExpend_time());
            ((MyDecorateExpendViewHolder) holder).item_da_money.setText("¥ " + recordList.get(position).getCost());
        }
    }

    @Override
    public int getItemCount() {
        return recordList != null ? recordList.size() : 0;
    }

    @Override
    public void onClick(View v) {
        if (onDecExpenlistItemClickLister != null) {
            onDecExpenlistItemClickLister.onItemClick(v);
        }
    }

    class MyDecorateExpendViewHolder extends RecyclerView.ViewHolder {
        private TextView item_da_name;//名称
        private TextView item_da_time;//时间
        private TextView item_da_content;//内容
        private TextView item_da_money;//花费金额

        public MyDecorateExpendViewHolder(View itemView) {
            super(itemView);
            item_da_name = (TextView) itemView.findViewById(R.id.item_da_name);
            item_da_time = (TextView) itemView.findViewById(R.id.item_da_time);
            item_da_content = (TextView) itemView.findViewById(R.id.item_da_content);
            item_da_money = (TextView) itemView.findViewById(R.id.item_da_money);
        }
    }
}
