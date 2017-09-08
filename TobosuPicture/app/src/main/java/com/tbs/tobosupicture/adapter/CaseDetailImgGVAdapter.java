package com.tbs.tobosupicture.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.SeeBigImgActivity;
import com.tbs.tobosupicture.bean.OnlineDiagram;
import com.tbs.tobosupicture.utils.GlideUtils;
import com.tbs.tobosupicture.utils.Utils;
import com.tbs.tobosupicture.view.MyGridView;
import com.tbs.tobosupicture.view.RoundAngleImageView;
import java.util.ArrayList;

/**
 * 施工阶段 适配器
 * Created by Lie on 2017/8/2.
 */

public class CaseDetailImgGVAdapter extends BaseAdapter {
    private String TAG = "CaseDetailImgGVAdapter";
    private ArrayList<OnlineDiagram> dataList;
    private Context context;
    private LayoutInflater inflater;
    private String stage[] = {"水电", "泥木", "油漆", "竣工"};   //阶段：1水电；2泥木；3油漆；4竣工
    private String imgString;

    public CaseDetailImgGVAdapter(Context context, ArrayList<OnlineDiagram> dataList, String imgString){
        this.context = context;
        this.dataList = dataList;
        this.imgString = imgString;
        this.inflater = LayoutInflater.from(context);
        Utils.setErrorLog(TAG, "===装修阶段的大小是==>>>" + dataList.size());
    }

    @Override
    public int getCount() {
        return dataList == null ?0 : dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position) == null ? null :dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImgViewHolder holder;
        if(convertView==null){
            holder = new ImgViewHolder();
            convertView = inflater.inflate(R.layout.adapter_item_case_detail_gv_img, null);
            holder.tv = (TextView) convertView.findViewById(R.id.tvTitleCaseDetailGv);
            holder.gv = (MyGridView) convertView.findViewById(R.id.gv_detail_case_gv);
            holder.iv = (RoundAngleImageView) convertView.findViewById(R.id.iv_detail_case_gv_more);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);
            convertView.setTag(holder);
        }else {
            holder = (ImgViewHolder) convertView.getTag();
        }


        final ArrayList<String> imgList = dataList.get(position).getImg_url();
        int totalItem = imgList.size();

        if(totalItem==1){
            // 仅仅一条
            holder.iv.setVisibility(View.VISIBLE);
            holder.gv.setVisibility(View.GONE);
            String lastUrl = imgList.get(0);
            final String lastOneUrl = lastUrl.replace("\\/\\/", "//").replace("\\/", "/");
//            Utils.setErrorLog(TAG, "施工阶段 1个 适配器  " + lastUrl + "   $$$$$  " + lastOneUrl);
            GlideUtils.glideLoader(context, lastOneUrl, R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv);
            holder.iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, SeeBigImgActivity.class);
                    it.putExtra("imgStringData", imgString);
                    it.putExtra("imgurl", lastOneUrl);
                    context.startActivity(it);
                }
            });
        }else {
            // 大于1条
            holder.gv.setVisibility(View.VISIBLE);
            if(totalItem % 2 == 0){
                // 偶数
                holder.iv.setVisibility(View.GONE);
                GvStageAdapter stageAdapter = new GvStageAdapter(context, imgList, dataList.get(position).getId());
                holder.gv.setAdapter(stageAdapter);
                holder.iv.setVisibility(View.GONE);
                holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it = new Intent(context, SeeBigImgActivity.class);
                        it.putExtra("imgStringData", imgString);
                        it.putExtra("imgurl", imgList.get(position));
                        context.startActivity(it);
                    }
                });
            }else {
                // 奇数   3 5 7 9 11 ...
                holder.iv.setVisibility(View.VISIBLE);
                String last_Url = imgList.get(imgList.size()-1);  // 获取最后一条
                final String last_One_Url = last_Url.replace("\\/\\/", "//").replace("\\/", "/");  // 处理反斜杠
                ArrayList<String> tempImgList = new ArrayList<String>();
                for(int i=0;i<imgList.size()-1;i++){
                    tempImgList.add(imgList.get(i));  // 获取一个新结合， 除掉最后一条
                }

                GvStageAdapter stageAdapter = new GvStageAdapter(context, tempImgList, dataList.get(position).getId());
                holder.gv.setAdapter(stageAdapter);
                holder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent it = new Intent(context, SeeBigImgActivity.class);
                        it.putExtra("imgStringData", imgString);
                        it.putExtra("imgurl", imgList.get(position));
                        context.startActivity(it);
                    }
                });

                Utils.setErrorLog(TAG, "单张需要加载的图片地址是：" +last_One_Url);
                GlideUtils.glideLoader(context, last_One_Url, R.mipmap.loading_img_fail, R.mipmap.loading_img, holder.iv);
                holder.iv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(context, SeeBigImgActivity.class);
                        it.putExtra("imgStringData", imgString);
                        it.putExtra("imgurl", last_One_Url);
                        context.startActivity(it);
                    }
                });
            }

        }


        if("1".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[0]);
        }else if("2".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[1]);
        }else if("3".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[2]);
        }else if("4".equals(dataList.get(position).getStage())){
            holder.tv.setText(stage[3]);
        }

        holder.tvDesc.setText(dataList.get(position).getDescription());
        return convertView;
    }

    class ImgViewHolder{
        MyGridView gv;
        TextView tv;
        RoundAngleImageView iv;  // 奇数，多出来的一张
        TextView tvDesc;
    }
}
