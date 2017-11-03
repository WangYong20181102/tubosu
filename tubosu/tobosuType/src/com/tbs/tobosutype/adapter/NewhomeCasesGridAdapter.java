package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.NewHomeDataItem;
import com.tbs.tobosutype.utils.GlideUtils;
import com.tbs.tobosutype.utils.TRoundView;

import java.util.List;
/**
 * Created by Lie on 2017/10/28.
 */

public class NewhomeCasesGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<NewHomeDataItem.NewhomeDataBean.CasesBean> casesBeenList;


    public NewhomeCasesGridAdapter(Context context, List<NewHomeDataItem.NewhomeDataBean.CasesBean> casesBeenList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.casesBeenList = casesBeenList;
//        Util.setErrorLog("send——andli" ,">>>>>>>>>"+casesBeenList.size());
        if(casesBeenList.size() == 0){
            Intent it = new Intent("anli_list_is_empty");
            context.sendBroadcast(it);
        }
    }


    @Override
    public int getCount() {
        return casesBeenList == null? 0 :casesBeenList.size();
    }

    @Override
    public Object getItem(int position) {
        return casesBeenList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CaseViewHolder holder;
        if(convertView==null){
            holder = new CaseViewHolder();
            convertView = inflater.inflate(R.layout.layout_newhome_anli_item, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.newhome_anli_item_img);
            holder.title = (TextView) convertView.findViewById(R.id.newhome_anli_item_title);
            holder.name = (TextView) convertView.findViewById(R.id.newhome_anli_item_name);
            holder.desc = (TextView) convertView.findViewById(R.id.newhome_anli_item_desc);
            convertView.setTag(holder);
        }else {
            holder = (CaseViewHolder) convertView.getTag();
        }
//        Glide.with(context).load(casesBeenList.get(position).getCover_url()).error(R.drawable.new_home_loading).placeholder(R.drawable.new_home_loading).into(holder.iv);
        GlideUtils.glideLoader(context, casesBeenList.get(position).getCover_url(), R.drawable.new_home_loading,R.drawable.new_home_loading,holder.iv);
        holder.title.setText(casesBeenList.get(position).getCommunity_name());
        holder.name.setText(casesBeenList.get(position).getOwner_name());
        holder.desc.setText(casesBeenList.get(position).getSub_title());
        return convertView;
    }

    class CaseViewHolder{
        ImageView iv;
        TextView title;
        TextView name;
        TextView desc;
    }
}