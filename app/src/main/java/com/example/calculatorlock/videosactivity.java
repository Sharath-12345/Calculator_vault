package com.example.calculatorlock;


import static com.example.calculatorlock.menu.userid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class videosactivity extends AppCompatActivity {
    Toolbar toolbarvideo;
    GridLayout videocontainer;
    ImageView playicon;
    int REQUESTCODE_FOR_DELETNG=1000;

    Uri retriveduri;
    List<ImageView> allimageviews;
    FloatingActionButton btngalleryvideo;
    ProgressBar progressBarvideo;
   static List<Uri> allvideolist;
    List<Uri> deletinguris;
    List<ImageView> ticking;
    boolean isselected = false;
    ImageView tick;
    Uri videouri;
    int flag = 0;
    int no_of_videos;
    int count1=0;
    Button selectallvideo;

    ActivityResultLauncher<Intent> videogallerylauncher;
    int height, width;

    ArrayList<Uri> selectedVideo = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videosactivity);

        findingids();

        AdRequest adRequest=new AdRequest.Builder().build();
        AdView adView=findViewById(R.id.adviewvideos);
        adView.loadAd(adRequest);

        selectallvideo.setVisibility(View.GONE);
        progressBarvideo.setVisibility(View.GONE);

        gettingdisplaymetrics();



        retrivingvideosandsiplay();



        selectallvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<ticking.size();i++)
                {
                    ticking.get(i).setVisibility(View.VISIBLE);
                }
                selectedVideo.clear();
                selectedVideo.addAll(allvideolist);
            }
        });





        btngalleryvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(videosactivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(videosactivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    opengallery();
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                }


            }
        });

        videogallerylauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getData() != null) {
                            videouri = data.getData();
                            flag=0;
                            count1=1;
                            upload(videouri);


                        }
                        if (data.getClipData() != null) {
                            flag=0;
                            count1 = data.getClipData().getItemCount();
                            for (int i = 0; i < count1; i++) {
                                videouri = data.getClipData().getItemAt(i).getUri();
                                upload(videouri);
                            }
                        }

                    }
                });


    }

    private void retrivingvideosandsiplay() {

        File folder = new File(getFilesDir(), "Videos"+userid); // Change "MyVideos" to your folder name
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {

                for (File file : files) {

                    if (file.isFile()) {

                        retriveduri=Uri.fromFile(file);
                      if (!allvideolist.contains(retriveduri)) {
                            ImageView imageView = new ImageView(videosactivity.this);
                            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                            layoutParams.height = height / 6;
                            layoutParams.width = width / 3;

                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            playicon=new ImageView(videosactivity.this);
                            playicon.setImageResource(R.drawable.baseline_play_arrow_24);
                            playicon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            playicon.setScaleX(0.5f);
                            playicon.setScaleY(0.5f);

                            tick = new ImageView(videosactivity.this);
                            tick.setImageResource(R.drawable.baseline_check_box_24);
                            tick.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            tick.setScaleY(0.7f);
                            tick.setScaleX(0.7f);

                            FrameLayout layout = new FrameLayout(videosactivity.this);
                            layout.setLayoutParams(layoutParams);
                            layout.addView(imageView);
                            layout.addView(tick);
                            layout.addView(playicon);
                            ticking.add(tick);
                            videocontainer.addView(layout);
                            tick.setVisibility(View.GONE);
                            allvideolist.add(retriveduri);


                            Glide.with(this).load(retriveduri).thumbnail(0.15f).into(imageView);
                            allimageviews.add(imageView);

                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ImageView imageclicked=(ImageView) view;
                                    int count=allimageviews.indexOf(imageclicked);
                                    if (!isselected) {
                                        Intent intent = new Intent(videosactivity.this, zoomvideo.class);
                                        intent.putExtra("imguri", allvideolist.get(count));
                                        intent.putExtra("indexs", count);
                                        startActivity(intent);
                                    }
                                    if (isselected) {


                                        if (selectedVideo.contains(allvideolist.get(count))) {
                                            ticking.get(count).setVisibility(View.GONE);
                                            selectedVideo.remove(allvideolist.get(count));
                                            if (selectedVideo.isEmpty()) {
                                                isselected = false;
                                                selectallvideo.setVisibility(View.GONE);
                                            }
                                        } else {
                                            ticking.get(count).setVisibility(View.VISIBLE);
                                            selectedVideo.add(allvideolist.get(count));
                                            selectallvideo.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            });
                            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    isselected = true;
                                    ImageView clickedimage=(ImageView) view;
                                    int count = allimageviews.indexOf(clickedimage);

                                    if (selectedVideo.contains(allvideolist.get(count))) {
                                        ticking.get(count).setVisibility(View.GONE);
                                        selectedVideo.remove(allvideolist.get(count));
                                        if (selectedVideo.isEmpty()) {
                                            isselected = false;
                                            selectallvideo.setVisibility(View.GONE);
                                        }
                                    } else {
                                        ticking.get(count).setVisibility(View.VISIBLE);
                                        selectedVideo.add(allvideolist.get(count));
                                        selectallvideo.setVisibility(View.VISIBLE);

                                    }
                                    return true;
                                }
                            });
                        }
                    }
                }
            }
        }

    }





    private void upload(Uri videouri) {


                String foldername="Videos"+userid;
           File folder = new File(getFilesDir(), foldername);
           if (!folder.exists()) {
            folder.mkdir();
            }

        String videofilename = "video"+System.currentTimeMillis()+".mp4";

        File internalvideoFile = new File(folder, videofilename);
        try {
            InputStream inputStream = getContentResolver().openInputStream(videouri);
            OutputStream outputStream = new FileOutputStream(internalvideoFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        flag=flag+1;

                                Uri curi = converttocontenturi(videouri);
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                    if (count1 == flag) {
                                       Toast.makeText(videosactivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                    retrivingvideosandsiplay();
                                    }
                                    getContentResolver().delete(curi, null, null);
                                }
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    if (count1 == flag) {
                                        progressBarvideo.setVisibility(View.GONE);

                                        retrivingvideosandsiplay();

                                        PendingIntent editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(),
                                                deletinguris);
                                        try {
                                            startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                                    REQUESTCODE_FOR_DELETNG, null, 0, 0, 0);
                                        } catch (IntentSender.SendIntentException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }



    }

    private Uri converttocontenturi(Uri videouri) {
        String id = videouri.getLastPathSegment();

        StringBuilder builder = new StringBuilder();
        boolean found = false;
        for (int i = id.length() - 1; i >= 0; i--) {
            char c = id.charAt(i);
            if (Character.isDigit(c)) {
                builder.insert(0, c);
                found = true;
            } else if (found) {
                break;
            }
        }


        Uri contenturi = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, builder.toString());
        deletinguris.add(contenturi);
        return contenturi;
    }

    private void opengallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        videogallerylauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE_FOR_DELETNG )
        {
            if(resultCode==Activity.RESULT_OK)
            {

            }
            else
            {
                Dialog dialog=new Dialog(this);
                dialog.setContentView(R.layout.customdialog_intentsender);
                dialog.setCancelable(false);
                dialog.show();
                Button btnokayfordelete=dialog.findViewById(R.id.btnokayfordeleting);
                btnokayfordelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        PendingIntent editPendingIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(),
                                    deletinguris);
                        }
                        try {
                            startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                    REQUESTCODE_FOR_DELETNG, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }

    private void gettingdisplaymetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.videosmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            if (selectedVideo.isEmpty()) {
                Toast.makeText(this, "Please select videos", Toast.LENGTH_SHORT).show();
            } else {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.customdialogdelete);
                dialog.setCancelable(false);
                dialog.show();
                Button btnyes = dialog.findViewById(R.id.btnyesdelete);
                Button btnno = dialog.findViewById(R.id.btnnodelete);
                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isselected=false;
                        Intent intent = new Intent(videosactivity.this, menu.class);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();

                        for(Uri singleuri : selectedVideo)
                        {
                            File deletingvideo=new File(singleuri.getPath());
                            if(deletingvideo.exists())
                            {
                                deletingvideo.delete();
                            }
                        }


                    }
                });
                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < ticking.size(); i++) {
                            ticking.get(i).setVisibility(View.GONE);
                            isselected = false;
                            dialog.dismiss();
                        }
                    }
                });


            }

        } else if (id == R.id.unhide) {

            if (selectedVideo.isEmpty()) {
                Toast.makeText(this, "Please select videos", Toast.LENGTH_SHORT).show();
            } else {
                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.customdialogunhide);
                dialog.setCancelable(false);
                dialog.show();
                Button btnyes = dialog.findViewById(R.id.btnyesunhide);
                Button btnno = dialog.findViewById(R.id.btnnounhide);
                btnyes.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                      progressBarvideo.setVisibility(View.VISIBLE);
                        isselected=false;
                        int savevideos=0;
                      Intent intent=new Intent(videosactivity.this,menu.class);
                      startActivity(intent);
                      finish();

                       for (Uri singlevideouri : selectedVideo) {
                           ContentValues values = new ContentValues();
                           Uri videos=null;
                           if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.Q)
                           {
                            videos=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                            savevideos++;
                           }
                           if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
                           {
                               videos= MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                               savevideos++;
                           }
                           values.put(MediaStore.Video.Media.DISPLAY_NAME, "VID"+ System.currentTimeMillis()+".mp4");
                           values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                               values.put(MediaStore.Video.Media.IS_PENDING, 1);
                           }

                           ContentResolver contentResolver = getContentResolver();
                           Uri insertUri = contentResolver.insert(videos, values);

                           progressBarvideo.setVisibility(View.GONE);
                           if (insertUri != null) {
                               try {


                                   try (OutputStream outputStream = contentResolver.openOutputStream(insertUri)) {
                                       InputStream inputStream = contentResolver.openInputStream(singlevideouri);
                                       byte[] buffer = new byte[1024];
                                       int length;
                                       while ((length = inputStream.read(buffer)) != -1) {
                                           outputStream.write(buffer, 0, length);
                                       }
                                       outputStream.flush();
                                   } finally {

                                       values.clear();
                                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                           values.put(MediaStore.Video.Media.IS_PENDING, 0);
                                       }
                                       contentResolver.update(insertUri, values, null, null);

                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                           if(selectedVideo.size()==1)
                           {
                               Toast.makeText(videosactivity.this, "Video saved to gallery", Toast.LENGTH_SHORT).show();
                           }
                           else if(savevideos==selectedVideo.size())
                           {
                               Toast.makeText(videosactivity.this, "Videos saved to gallery", Toast.LENGTH_SHORT).show();
                           }

                           File deletingfile=new File(singlevideouri.getPath());
                           if(deletingfile.exists())
                           {
                               deletingfile.delete();
                           }

                       }
                    }
                });
                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < ticking.size(); i++) {
                            ticking.get(i).setVisibility(View.GONE);
                            isselected = false;
                            dialog.dismiss();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }





    private void findingids() {

        toolbarvideo=findViewById(R.id.toolbarvideo);
        videocontainer=findViewById(R.id.videocontainer);
        btngalleryvideo=findViewById(R.id.btngalleryvideo);
        progressBarvideo=findViewById(R.id.progressbarvideo);

        deletinguris=new ArrayList<>();
        ticking=new ArrayList<>();
        selectallvideo=findViewById(R.id.selectallvideeo);
         allvideolist=new ArrayList<>();
         allimageviews=new ArrayList<>();
        setSupportActionBar(toolbarvideo);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==200)
        {
            if(grantResults.length>0)
            {
                opengallery();
            }
        }
    }
}