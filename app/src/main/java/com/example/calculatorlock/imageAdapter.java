package com.example.calculatorlock;


import static com.example.calculatorlock.menu.height;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import java.util.List;

public class imageAdapter extends PagerAdapter {
    List<Uri> imglist;
    Context context;

    public imageAdapter(List<Uri> imglist, Context context)
    {
        this.imglist=imglist;
        this.context=context;
    }

    @Override
    public int getCount() {
        return imglist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.image,container,false);
        ZoomageView imageView=view.findViewById(R.id.imgview);
        imageView.getLayoutParams().height= (int) (height/1.5);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(imglist.get(position)).into(imageView);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

