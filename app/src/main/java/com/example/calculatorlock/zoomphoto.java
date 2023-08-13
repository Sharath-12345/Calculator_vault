package com.example.calculatorlock;

import static com.example.calculatorlock.photosactivity.imglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class zoomphoto extends AppCompatActivity {
    ViewPager viewPager;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoomphoto);
        viewPager=findViewById(R.id.viewpager);
        imageAdapter imageAdapter=new imageAdapter(imglist,this);
        viewPager.setAdapter(imageAdapter);
        count=getIntent().getIntExtra("index",0);
        viewPager.setCurrentItem(count);

    }
}