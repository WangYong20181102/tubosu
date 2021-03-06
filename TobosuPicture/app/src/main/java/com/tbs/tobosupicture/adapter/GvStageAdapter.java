package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.TRoundView;

import java.util.ArrayList;

/**
 *
 * Created by Lie on 2017/7/17.
 */

public class GvStageAdapter extends BaseAdapter {
    private String TAG = "GvStageAdapter";
    private String imgStringUrl = "https:\\/\\/pic.tbscache.com\\/building\\/2017-03-23\\/small\\/p_58d39147d692e.jpg";

    private Context context;
    private ArrayList<String> dataList;
    private LayoutInflater inflater;
    private String id;

    public GvStageAdapter(Context context, ArrayList<String> dataList, String id){
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
        this.id = id;
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
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_img, null);
            holder.iv = (TRoundView) convertView.findViewById(R.id.iv_imgs);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        String url = dataList.get(position);
        Utils.setErrorLog(TAG, "4444适配器前 " + url);
        url = url.replace("\\/\\/", "//").replace("\\/", "/");
        Utils.setErrorLog(TAG, "4444适配器后 " + url);
        holder.iv.setType(1);
        GlideUtils.glideLoader(context, url,R.mipmap.loading_img_fail, R.mipmap.loading_img,holder.iv);


//        holder.iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(context, SeeBigImgActivity.class);
//                it.putExtra("imgStringData", imgString);
//                context.startActivity(it);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TRoundView iv;
    }
}
