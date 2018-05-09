package com.tbs.tbs_mj.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbs.tbs_mj.R;
import com.tbs.tbs_mj.bean._ArticleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2018/1/3 09:55.
 */

public class PopMoreStyleAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private String TAG = "PopMoreStyleAdapter";
    private ArrayList<_ArticleType> mArticleTypeArrayList;

    public static interface OnPopMoreStyleOnClickLister {
        void onItemClick(View view, int position);
    }

    protected OnPopMoreStyleOnClickLister onPopMoreStyleOnClickLister = null;

    public void setOnPopMoreStyleOnClickLister(OnPopMoreStyleOnClickLister onPopMoreStyleOnClickLister) {
        this.onPopMoreStyleOnClickLister = onPopMoreStyleOnClickLister;
    }

    public PopMoreStyleAdapter(Context context, ArrayList<_ArticleType> articleTypeArrayList) {
        this.mContext = context;
        this.mArticleTypeArrayList = articleTypeArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pop_more_stytle, parent, false);
        PopMoreStyleViewHolder popMoreStyleViewHolder = new PopMoreStyleViewHolder(view);
        popMoreStyleViewHolder.item_pop_more_stytle_ll.setOnClickListener(this);
        return popMoreStyleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PopMoreStyleViewHolder) {
            //设置tag
            ((PopMoreStyleViewHolder) holder).item_pop_more_stytle_ll.setTag(position);
            //名称
            ((PopMoreStyleViewHolder) holder).item_pop_more_stytle_title.setText(mArticleTypeArrayList.get(position).getTitle());
            //设置图片
            switch (mArticleTypeArrayList.get(position).getIndex()) {
                case "0":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.sheji_pop);
                    break;
                case "1":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.yusuan_pop);
                    break;
                case "2":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.xuancai_pop);
                    break;
                case "3":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.hetong_pop);
                    break;
                case "4":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.fangshui_pop);
                    break;
                case "5":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.nigong_pop);
                    break;
                case "6":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.mugong_pop);
                    break;
                case "7":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.anzhuang_pop);
                    break;
                case "8":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.ruanzhuang_pop);
                    break;
                case "9":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.ruzhu_pop);
                    break;
                case "10":
                    ((PopMoreStyleViewHolder) holder).
                            item_pop_more_stytle_image.
                            setImageResource(R.drawable.fengshui_pop);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mArticleTypeArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (onPopMoreStyleOnClickLister != null) {
            onPopMoreStyleOnClickLister.onItemClick(v, (int) v.getTag());
        }
    }

    class PopMoreStyleViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout item_pop_more_stytle_ll;//整个图层
        private ImageView item_pop_more_stytle_image;//显示的图片
        private TextView item_pop_more_stytle_title;//显示的文字

        public PopMoreStyleViewHolder(View itemView) {
            super(itemView);
            item_pop_more_stytle_ll = itemView.findViewById(R.id.item_pop_more_stytle_ll);
            item_pop_more_stytle_image = itemView.findViewById(R.id.item_pop_more_stytle_image);
            item_pop_more_stytle_title = itemView.findViewById(R.id.item_pop_more_stytle_title);
        }
    }
}
