package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity._Collect;
import com.tobosu.mydecorate.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/2 14:02.
 * 我的收藏
 */

public class MyCollentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<_Collect> collectList;
    //    private HashMap<Integer, Boolean> checkMap;
    private List<Integer> checkList;
    private int adapterState = 1;//设置加载更多的角标状态 默认加载更多  1--加载更多 2--显示勾选按钮状态  3--默认状态

    public MyCollentAdapter(Context context, List<_Collect> collectList) {
        this.mContext = context;
        this.collectList = collectList;
        checkList = new ArrayList<>();
    }

    public List<Integer> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Integer> checkList) {
        this.checkList = checkList;
    }

    //单项点击事件
    public static interface OnItemClickLister {
        void onItemClick(View view);
    }


    //声明接口
    private OnItemClickLister onItemClickLister = null;


    //实现接口
    public void setOnItemClickLister(OnItemClickLister clickLister) {
        onItemClickLister = clickLister;
    }


    /**
     * 改变布局的状态
     * 1.加载更多状态
     * 2.显示复选框状态
     * 3.正常状态
     */
    public void changeState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 3) {//正常的子项
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_collect, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else if (viewType == 1) {//显示加载更多的复选框
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loadmore, parent, false);
            FootViewHolder holder = new FootViewHolder(view);
            return holder;
        } else if (viewType == 2) {//显示带有复选框的子项
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_collect, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//返回加载更多图层
        } else if (adapterState == 2) {
            return 2;//返回显示复选框图层
        } else {
            return 3;//普通图层
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            //设置文章的标题
            ((MyViewHolder) holder).collectTitle.setText("" + collectList.get(position).getTitle());
            //设置作者的头像
            GlideUtils.glideLoader(mContext, collectList.get(position).getIcon(), 0,
                    R.mipmap.jiazai_loading, ((MyViewHolder) holder).collectIcon,
                    GlideUtils.CIRCLE_IMAGE);
            //设置作者的昵称
            ((MyViewHolder) holder).collectName.setText("" + collectList.get(position).getNick());
            //设置时间
            ((MyViewHolder) holder).collectTime.setText("" + collectList.get(position).getTime());
            //设置封面
            GlideUtils.glideLoader(mContext, collectList.get(position).getImage_url(), 0,
                    R.mipmap.jiazai_loading, ((MyViewHolder) holder).collectItemImg);
            if (adapterState == 2) {
                ((MyViewHolder) holder).collectCheckBox.setTag(position);
                ((MyViewHolder) holder).collectCheckBox.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).collectCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkList.contains(((MyViewHolder) holder).collectCheckBox.getTag())) {
                            //当前的集合中不包含选中的位置
                            checkList.add(position);
                            ((MyViewHolder) holder).collectCheckBox.setImageResource(R.mipmap.check);
                        } else {
                            //集合包含选中的位置
                            for (int i = 0; i < checkList.size(); i++) {
                                if (checkList.get(i) == ((MyViewHolder) holder).collectCheckBox.getTag()) {
                                    checkList.remove(i);
                                    ((MyViewHolder) holder).collectCheckBox.setImageResource(R.mipmap.no_check);
                                }
                            }
                        }
                    }
                });
            }
            if (checkList.contains(position)) {
                ((MyViewHolder) holder).collectCheckBox.setImageResource(R.mipmap.check);
            } else {
                ((MyViewHolder) holder).collectCheckBox.setImageResource(R.mipmap.no_check);
            }

        } else if (holder instanceof FootViewHolder) {
            //设置加载更多的角标
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            if (position == 0) {
                footViewHolder.mProgressBar.setVisibility(View.GONE);
                footViewHolder.mtextView.setVisibility(View.GONE);
            }
            switch (adapterState) {
                case 1://显示角标
                    footViewHolder.mtextView.setVisibility(View.VISIBLE);
                    footViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case 3://显示普通图层
                    footViewHolder.mtextView.setVisibility(View.GONE);
                    footViewHolder.mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return collectList != null ? collectList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickLister != null) {
            onItemClickLister.onItemClick(v);
        }
    }

    public void clearCheckList() {
        if (checkList != null) {
            checkList.clear();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView collectCheckBox;//选择框
        private TextView collectTitle;//文章的标题
        private ImageView collectIcon;//作者的头像
        private TextView collectName;//作者的名字
        private TextView collectTime;//文章的时间
        private ImageView collectItemImg;//文章的封面

        public MyViewHolder(View itemView) {
            super(itemView);
            collectCheckBox = (ImageView) itemView.findViewById(R.id.collect_check_box);
            collectTitle = (TextView) itemView.findViewById(R.id.collect_title);
            collectIcon = (ImageView) itemView.findViewById(R.id.collect_icon);
            collectName = (TextView) itemView.findViewById(R.id.collect_name);
            collectTime = (TextView) itemView.findViewById(R.id.collect_time);
            collectItemImg = (ImageView) itemView.findViewById(R.id.collect_item_img);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;//进度条
        private TextView mtextView;//显示加载更多的字段

        public FootViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.image_new_bar);
            mtextView = (TextView) itemView.findViewById(R.id.iamge_new_tv);
        }
    }
}
