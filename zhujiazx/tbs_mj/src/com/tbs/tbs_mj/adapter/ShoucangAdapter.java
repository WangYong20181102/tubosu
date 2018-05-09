package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean.ShoucangItem;
import java.util.List;


public class ShoucangAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int[] iconIdArr = new int[]{R.drawable.tao, R.drawable.dan, R.drawable.gongsi};
    private List<ShoucangItem> dataList;

    public ShoucangAdapter(Context context, List<ShoucangItem> dataList){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList==null?0:dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position<iconIdArr.length){

        }
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.shoucang_item_layout, null);
            viewHolder.iv = (ImageView) convertView.findViewById(R.id.shoucang_item_iv);
            viewHolder.nameNum = (TextView) convertView.findViewById(R.id.shoucang_item_num);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv.setBackgroundResource(iconIdArr[position<iconIdArr.length?position:iconIdArr.length-1]);
        viewHolder.nameNum.setText(dataList.get(position).getName() + "  (" + dataList.get(position).getCount() + ")");
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        TextView nameNum;
    }
}
