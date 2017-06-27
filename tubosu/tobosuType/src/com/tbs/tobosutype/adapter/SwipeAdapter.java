
package com.tbs.tobosutype.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean._DecorationExpent;

import java.util.ArrayList;


public class SwipeAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private ArrayList<_DecorationExpent.decorate_record> data;
    
    private int mRightWidth = 0;

    public SwipeAdapter(Context context, ArrayList<_DecorationExpent.decorate_record> data, int rightWidth) {
        mContext = context;
        this.data = data;
        mRightWidth = rightWidth;
    }

    @Override
    public int getCount() {
        return data==null ? 0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
    	
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_decorate_expend, parent, false);
            holder = new ViewHolder();
            holder.item_da_name = (TextView) convertView.findViewById(R.id.item_da_name);
            holder.item_da_time = (TextView) convertView.findViewById(R.id.item_da_time);
            holder.item_da_content = (TextView) convertView.findViewById(R.id.item_da_content);
            holder.item_da_money = (TextView) convertView.findViewById(R.id.item_da_money);
            holder.item_right = (RelativeLayout)convertView.findViewById(R.id.item_right);

            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder)convertView.getTag();
        }

        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

        holder.item_da_name.setText(data.get(position).getExpend_name());
        holder.item_da_time.setText(data.get(position).getExpend_time());
        holder.item_da_content.setText(data.get(position).getContent());
        holder.item_da_money.setText(data.get(position).getCost());

        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView item_da_name;//名称
        TextView item_da_time;//时间
        TextView item_da_content;//内容
        TextView item_da_money;//花费金额

        RelativeLayout item_right;
    }
    
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;
    
    public void setOnRightItemClickListener(onRightItemClickListener listener){
    	mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }


}
