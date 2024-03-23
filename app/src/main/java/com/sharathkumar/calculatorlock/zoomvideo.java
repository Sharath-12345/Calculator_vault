package com.sharathkumar.calculatorlock;


import static com.sharathkumar.calculatorlock.videosactivity.allvideolist;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

public class zoomvideo extends AppCompatActivity {
    ViewPager viewPager;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomvideo);



        viewPager=findViewById(R.id.viewpager);
        Videoadapter videoadapter=new Videoadapter(allvideolist,zoomvideo.this);
        viewPager.setAdapter(videoadapter);
        count=getIntent().getIntExtra("indexs",0);
        viewPager.setCurrentItem(count);

    }
}