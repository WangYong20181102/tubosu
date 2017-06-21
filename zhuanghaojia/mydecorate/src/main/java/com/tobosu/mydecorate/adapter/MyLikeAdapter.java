package com.tobosu.mydecorate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.entity.Mylike;
import com.tobosu.mydecorate.util.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/6/11 09:09.
 */

public class MyLikeAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {
    private Context mContext;
    private List<Mylike> mylikeList;
    //布局中的列表状态  1--加载更多 2--显示勾选状态 3--默认一般状态
    private int adapterState = 1;
    //选中集合 将选中的位置保存用于数据的遍历
    private List<Integer> checkList;

    public MyLikeAdapter(Context context, List<Mylike> mylikeList) {
        this.mContext = context;
        this.mylikeList = mylikeList;
        checkList = new ArrayList<>();
    }

    //子项点击事件
    public static interface OnMyLikeItemClickLister {
        void onItemClick(View view);
    }

    //图层变换
    public void changeState(int state) {
        this.adapterState = state;
        notifyDataSetChanged();
    }

    private OnMyLikeItemClickLister onMyLikeItemClickLister = null;

    public void setOnMyLikeItemClickLister(OnMyLikeItemClickLister onMyLikeItemClickLister) {
        this.onMyLikeItemClickLister = onMyLikeItemClickLister;
    }

    public List<Integer> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Integer> checkList) {
        this.checkList = checkList;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 3) {//正常的子项
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mylike, parent, false);
            MylikeViewHolder holder = new MylikeViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        } else if (viewType == 1) {//显示加载更多
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_loadmore, parent, false);
            FootViewHolder holder = new FootViewHolder(view);
            return holder;
        } else if (viewType == 2) {//显示带有复选框的子项
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_mylike, parent, false);
            MylikeViewHolder holder = new MylikeViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MylikeViewHolder) {
            //显示正常的列表选项
            ((MylikeViewHolder) holder).MylikeTitle.setText("" + mylikeList.get(position).getTitle());
            ((MylikeViewHolder) holder).MylikeName.setText("" + mylikeList.get(position).getNick());
            ((MylikeViewHolder) holder).Mylikenum.setText("" + mylikeList.get(position).getTup_count());
            GlideUtils.glideLoader(mContext, mylikeList.get(position).getImage_url(), 0,
                    R.mipmap.jiazai_loading,
                    ((MylikeViewHolder) holder).MylikeItemImg);
            GlideUtils.glideLoader(mContext, mylikeList.get(position).getIcon(), 0,
                    R.mipmap.jiazai_loading,
                    ((MylikeViewHolder) holder).MylikeIcon, GlideUtils.CIRCLE_IMAGE);
            if (adapterState == 2) {
                ((MylikeViewHolder) holder).MylikeCheckBox.setTag(position);
                ((MylikeViewHolder) holder).MylikeCheckBox.setVisibility(View.VISIBLE);
                ((MylikeViewHolder) holder).MylikeCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkList.contains(((MylikeViewHolder) holder).MylikeCheckBox.getTag())) {
                            ((MylikeViewHolder) holder).MylikeCheckBox.setImageResource(R.mipmap.check);
                            checkList.add(position);
                        } else {
                            for (int i = 0; i < checkList.size(); i++) {
                                if (checkList.get(i) == ((MylikeViewHolder) holder).MylikeCheckBox.getTag()) {
                                    checkList.remove(i);
                                    ((MylikeViewHolder) holder).MylikeCheckBox.setImageResource(R.mipmap.no_check);
                                }
                            }
                        }
                    }
                });
            }
            if (checkList.contains(position)) {
                ((MylikeViewHolder) holder).MylikeCheckBox.setImageResource(R.mipmap.check);
            } else {
                ((MylikeViewHolder) holder).MylikeCheckBox.setImageResource(R.mipmap.no_check);
            }
        } else if (holder instanceof FootViewHolder) {
            //显示加载更多的角标
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
        return mylikeList != null ? mylikeList.size() + 1 : 0;
    }

    @Override
    public void onClick(View v) {
        if (onMyLikeItemClickLister != null) {
            onMyLikeItemClickLister.onItemClick(v);
        }
    }

    public class MylikeViewHolder extends RecyclerView.ViewHolder {
        private ImageView MylikeCheckBox;//选择框
        private TextView MylikeTitle;//文章的标题
        private ImageView MylikeIcon;//作者的头像
        private TextView MylikeName;//作者的名字
        private TextView Mylikenum;//文章点赞次数
        private ImageView MylikeItemImg;//文章的封面

        public MylikeViewHolder(View itemView) {
            super(itemView);
            MylikeCheckBox = (ImageView) itemView.findViewById(R.id.mylike_check_box);
            MylikeTitle = (TextView) itemView.findViewById(R.id.mylike_title);
            MylikeIcon = (ImageView) itemView.findViewById(R.id.mylike_icon);
            MylikeItemImg = (ImageView) itemView.findViewById(R.id.mylike_img);
            MylikeName = (TextView) itemView.findViewById(R.id.mylike_name);
            Mylikenum = (TextView) itemView.findViewById(R.id.mylike_num);
        }
    }

    //加载更多角标
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
