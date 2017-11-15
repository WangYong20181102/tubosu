package com.tbs.tobosutype.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.tbs.tobosutype.R;
import com.tbs.tobosutype.bean.DanTuJsonItem;
import com.tbs.tobosutype.utils.ScreenUtils;
import com.tbs.tobosutype.utils.Util;

import java.util.ArrayList;

/**
 * Created by Lie on 2017/11/13.
 */

public class DanTuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = DanTuAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<DanTuJsonItem.DantuBean> dataList;
    private int ITEM_BODY_TYPE = 1;
    private int ITEM_FOOT_TYPE = 2;
    private boolean more = false;
    private boolean isDeleting = false;

    public DanTuAdapter(Context context, ArrayList<DanTuJsonItem.DantuBean> dataList){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount()-1){
            return ITEM_FOOT_TYPE;
        }else{
            return ITEM_BODY_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_BODY_TYPE){
            View view = inflater.inflate(R.layout.dantu_adapter_item, parent, false);
            DanTuHolder danTuHolder = new DanTuHolder(view);
            return danTuHolder;
        }else {
            View foot = inflater.inflate(R.layout.layout_new_home_foot, parent, false);
            DanFootHolder footHolder = new DanFootHolder(foot);
            return footHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DanTuHolder){
            DanTuHolder dan = (DanTuHolder) holder;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dan.dan_img.getLayoutParams();
            int screenWidth = ScreenUtils.getScreenWidth(context);  // 屏宽
            float imgWidth = (screenWidth - 10) / 2;  // 照片宽

            float imgHeight = dataList.get(position).getImage_height() * imgWidth / dataList.get(position).getImage_width();

            Util.setErrorLog(TAG, imgWidth + "<<===========>>" + imgHeight);

            params.width = (int) imgWidth;
            params.height = (int) imgHeight;


            Glide.with(context)
                 .load(dataList.get(position).getCover_url())
                 .placeholder(R.drawable.new_home_loading)
                 .error(R.drawable.new_home_loading)
                 .override(params.width, params.height)
                 .into(dan.dan_img);

            if(isDeleting){
                dan.dan_fav_img.setVisibility(View.VISIBLE);
            }else {
                dan.dan_fav_img.setVisibility(View.GONE);
            }
        }

        if(holder instanceof DanFootHolder){
            DanFootHolder foot = (DanFootHolder) holder;
            if(more){
                foot.bar.setVisibility(View.VISIBLE);
                foot.loadText.setVisibility(View.VISIBLE);
            }else{
                foot.bar.setVisibility(View.GONE);
                foot.loadText.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null?0:dataList.size() + 1;
    }


    class DanTuHolder extends RecyclerView.ViewHolder{
        ImageView dan_img;
        ImageView dan_fav_img;

        public DanTuHolder(View itemView) {
            super(itemView);
            dan_img = (ImageView) itemView.findViewById(R.id.dan_img);
            dan_fav_img = (ImageView) itemView.findViewById(R.id.dan_fav_img);
        }
    }

    class DanFootHolder extends RecyclerView.ViewHolder{
        ProgressBar bar;
        TextView loadText;
        public DanFootHolder(View itemView) {
            super(itemView);
            bar = (ProgressBar) itemView.findViewById(R.id.newhome_progressbar);
            loadText = (TextView) itemView.findViewById(R.id.newhome_loadmore);
        }
    }
}
