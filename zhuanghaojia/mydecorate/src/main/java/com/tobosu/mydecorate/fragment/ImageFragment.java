package com.tobosu.mydecorate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tobosu.mydecorate.R;

/**
 * Created by dec on 2016/10/21.
 */

public class ImageFragment extends Fragment {
    private ImageView see_image;
    private String image_url;
    private int _position = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null, false);
        see_image = (ImageView) view.findViewById(R.id.see_image);
        Picasso.with(getActivity())
                .load(image_url)
                .fit()
                .placeholder(R.mipmap.occupied1)
                .into(see_image);
        return view;
    }


    public static ImageFragment newInstance(int position, String url) {

        ImageFragment f = new ImageFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putString("url", url);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _position = getArguments().getInt("position");
        image_url = getArguments().getString("url");
    }
}
