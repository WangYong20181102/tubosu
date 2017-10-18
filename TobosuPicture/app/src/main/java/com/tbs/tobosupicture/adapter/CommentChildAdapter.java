package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.NewReplyActivity;
import com.tbs.tobosupicture.bean._DynamicDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/10/13 10:27.
 */

public class CommentChildAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<_DynamicDetail.Comment.ChildComment> childCommentArrayList;
    private _DynamicDetail.Comment mComment;

    public CommentChildAdapter(Context context, _DynamicDetail.Comment comment) {
        this.mContext = context;
        this.mComment = comment;
        this.childCommentArrayList = comment.getChild_comment();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 1;//显示查看更多
        } else {
            return 2;//显示普通
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            //正常显示
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_child_normal, parent, false);
            CommentChildHolder commentChildHolder = new CommentChildHolder(view);
            return commentChildHolder;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_child_normal, parent, false);
            CommentChildHolderMore commentChildHolderMore = new CommentChildHolderMore(view);
            return commentChildHolderMore;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentChildHolder) {
            //正常显示某某回复某某
            String str = null;
//            for (int i = 0; i < childCommentArrayList.size(); i++) {
//                Log.e("CommentChildAdapter", "子项数据打印结果=====" + i + "====子项数据===" + childCommentArrayList.get(i).getNick());
//                Log.e("CommentChildAdapter", "子项数据打印结果=====" + i + "====子项数据===" + childCommentArrayList.get(i).getC_nick());
//                Log.e("CommentChildAdapter", "子项数据打印结果=====" + i + "====子项数据===" + childCommentArrayList.get(i).getContent());
//            }
            if (!TextUtils.isEmpty(childCommentArrayList.get(position).getC_nick())) {
                //有回复对象
                str = "<font color='#FF882E'>" + childCommentArrayList.get(position).getNick() + "回复 " + childCommentArrayList.get(position).getC_nick() + "</font>:" + childCommentArrayList.get(position).getContent();
            } else {
                str = "<font color='#FF882E'>" + childCommentArrayList.get(position).getNick() + "</font> :" + childCommentArrayList.get(position).getContent();
            }
            ((CommentChildHolder) holder).comment_child_context.setText(Html.fromHtml(str));
            ((CommentChildHolder) holder).comment_child_context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewReplyActivity.class);
                    intent.putExtra("comment_id", mComment.getId());
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof CommentChildHolderMore) {
            int mCommentNum= Integer.parseInt(mComment.getReply_count());
            if (mCommentNum > 3) {
                ((CommentChildHolderMore) holder).comment_child_more.setVisibility(View.VISIBLE);
                ((CommentChildHolderMore) holder).comment_child_more.setTextColor(Color.parseColor("#FF882E"));
                ((CommentChildHolderMore) holder).comment_child_more.setText("查看全部" + mComment.getReply_count() + "条回复");
            } else {
                ((CommentChildHolderMore) holder).comment_child_more.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return childCommentArrayList != null ? childCommentArrayList.size() + 1 : 0;
    }

    //普通层 显示某人的回复
    class CommentChildHolder extends RecyclerView.ViewHolder {
        private TextView comment_child_context;

        public CommentChildHolder(View itemView) {
            super(itemView);
            comment_child_context = (TextView) itemView.findViewById(R.id.comment_child_context);
        }
    }

    //当数量达到一定值的时候显示查看更多的层 显示还有多少条评论
    class CommentChildHolderMore extends RecyclerView.ViewHolder {
        private TextView comment_child_more;

        public CommentChildHolderMore(View itemView) {
            super(itemView);
            comment_child_more = (TextView) itemView.findViewById(R.id.comment_child_context);

        }
    }
}
