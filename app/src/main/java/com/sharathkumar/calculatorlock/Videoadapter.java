package com.sharathkumar.calculatorlock;
import static com.sharathkumar.calculatorlock.menu.height;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import java.util.List;

public class Videoadapter extends PagerAdapter {
    List<Uri> allvideolist;
    Context context;

    public Videoadapter(List<Uri> allvideolist, Context context)
    {
        this.allvideolist=allvideolist;
        this.context=context;
    }


    @Override
    public int getCount() {
        return allvideolist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.video,container,false);
        ImageView imageView=view.findViewById(R.id.imgview);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageView playicon=view.findViewById(R.id.playicon);
        imageView.getLayoutParams().height= (int) (height/1.5);
        playicon.setScaleY(3.5f);
        playicon.setScaleX(3.5f);
        Glide.with(context).load(allvideolist.get(position)).thumbnail(0.5f).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,playingvideo.class);
                intent.putExtra("position",position);
                intent.putExtra("key",allvideolist.get(position).toString());
                context.startActivity(intent);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
