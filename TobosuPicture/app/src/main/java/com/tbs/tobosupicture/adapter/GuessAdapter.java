package com.tbs.tobosupicture.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbs.tobosupicture.R;
import com.tbs.tobosupicture.activity.ImgToFriendSeachActivity;
import com.tbs.tobosupicture.bean._Guess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Lin on 2017/8/1 15:10.
 * 以图会友大家都在搜索的适配器
 */

public class GuessAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private String TAG = "GuessAdapter";
    private ArrayList<_Guess> guessList;

    public GuessAdapter(Context context, ArrayList<_Guess> guessList) {
        this.mContext = context;
        this.guessList = guessList;
    }

    public static interface OnGuessAdapterCliskLister {
        void onItemClick(View view);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_guess, parent, false);
        GuessViewHolder holder = new GuessViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof GuessViewHolder) {
            ((GuessViewHolder) holder).item_guess_key.setText(guessList.get(position).getKey_word());
            ((GuessViewHolder) holder).item_guess_key.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "点击了====" + guessList.get(position).getKey_word());
                    Intent intent = new Intent(mContext, ImgToFriendSeachActivity.class);
                    intent.putExtra("item_guess_key", guessList.get(position).getKey_word());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return guessList != null ? guessList.size() : 0;
    }


    class GuessViewHolder extends RecyclerView.ViewHolder {
        private TextView item_guess_key;

        public GuessViewHolder(View itemView) {
            super(itemView);
            item_guess_key = (TextView) itemView.findViewById(R.id.item_guess_key);
        }
    }
}
