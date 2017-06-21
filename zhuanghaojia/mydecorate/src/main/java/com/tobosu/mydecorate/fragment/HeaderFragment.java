package com.tobosu.mydecorate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;
import com.tobosu.mydecorate.activity.ArticleDetailActivity;
import com.tobosu.mydecorate.util.Util;


/**
 * Created by dec on 2016/10/23.
 *
 */

public class HeaderFragment extends Fragment{

    private ImageView header_image;

    private String _url;

    private int _position = -1;

    private String _userid = "";
    private String _writerid = "";
    private String _articleid = "";
    private String _typeId = "";
    private String _title = "";


//    private ViewPager viewPager;
//    private int current_pos = 0;
//    private android.os.Handler handler = new android.os.Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 12:
//                    viewPager.setCurrentItem(current_pos);
//                    break;
//            }
//        }
//    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _position = getArguments().getInt("position");
        _url = getArguments().getString("imageUrl");

        _writerid = getArguments().getString("writerid");
        _articleid = getArguments().getString("articleid");
        _typeId = getArguments().getString("typeId");
        _title = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_layout, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        header_image = (ImageView) view.findViewById(R.id.header_image);
        Picasso.with(getActivity())
                .load(_url)
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(header_image);

        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent it = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle b = new Bundle();
                b.putString("article_id", _articleid); //文章id
                b.putString("type_id", _typeId); // 文章類型
                b.putString("article_title_pic_url", _url); // 文章標題所在的標題性圖片的url
                if(Util.isLogin(getActivity())){
                    b.putString("uid", Util.getUserId(getActivity()));
                }else {
                    b.putString("uid", "0");
                }
                it.putExtra("Article_Detail_Bundle", b);

                startActivity(it);
            }
        });
    }

    /**
     * 3,userid,writerid,articleid,typeId,title,url
     * @param position
     * @param url
     * @return
     */
    public static HeaderFragment newInstance(int position,String writerid,String articleid,String typeId,String title,String url) {

        HeaderFragment f = new HeaderFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("imageUrl", url);
        b.putString("writerid",writerid);
        b.putString("articleid",articleid);
        b.putString("typeId",typeId);
        b.putString("title",title);
        f.setArguments(b);
        return f;
    }





}
