package com.example.applt.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.applt.HomeFragment;
import com.example.applt.R;
import com.example.applt.model.Slider;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private final List<String> mListSlider;
    private List<String> imageUrls;
    private Context mContext;
private LayoutInflater layoutInflater;


    public SliderAdapter(Context mContext, List<String> mListSlider) {
        this.mListSlider = mListSlider;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_slider, container ,false);
        ImageView imgPhoto = view.findViewById(R.id.img_slider);

        Glide.with(mContext)
                .load(mListSlider.get(position))
                .into(imgPhoto);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(mListSlider != null) {
            return mListSlider.size();
        }

        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
