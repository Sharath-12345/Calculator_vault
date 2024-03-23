package com.sharathkumar.calculatorlock;



import static com.sharathkumar.calculatorlock.menu.userid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
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
import java.util.Objects;

public class photosactivity extends AppCompatActivity {

    Toolbar toolbar;
    GridLayout photocontainer;
    ImageView tick;
    List<ImageView> allimagelist;
    int saveimages=0;
    Uri retriveduri;
    List<Drawable> imagedrawable;

    ArrayList<Uri> selectedImages = new ArrayList<>();

    Button selectall;
    FloatingActionButton btngalleryphoto;
    boolean isselected;

    ActivityResultLauncher<Intent> gallerylauncher;

    Uri imguri;
    static int height,width;
    int flag=0;
    ProgressBar progressBar;
    List<Uri> deletinguris;

    int count=0;
    static List<Uri> imglist;
    List<ImageView> ticking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photosactivity);

        findingids();
        AdRequest adRequest=new AdRequest.Builder().build();
        AdView adView=findViewById(R.id.adviewphotos);
        adView.loadAd(adRequest);

        selectall.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        gettingdisplaymetrics();

        retriving_and_displaying();

        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedImages.clear();
                selectedImages.addAll(imglist);
                for(int i=0;i<ticking.size();i++)
                {
                    ticking.get(i).setVisibility(View.VISIBLE);
                }


            }
        });




        btngalleryphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT >= 31) {
                    opengallery();
                }
                if (ContextCompat.checkSelfPermission(photosactivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(photosactivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    opengallery();

                } else {

                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
                }


            }
        });

        gallerylauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode()== Activity.RESULT_OK)
                    {
                        Intent data=result.getData();
                        assert data != null;
                        if(data.getData()!=null)
                        {
                            imguri=data.getData();
                            count=1;
                            flag=0;
                            upload(imguri);
                        }
                        if(data.getClipData()!=null)
                        {
                            count=data.getClipData().getItemCount();
                            flag=0;
                            for(int i=0;i<count;i++)
                            {
                                imguri=data.getClipData().getItemAt(i).getUri();
                                upload(imguri);
                            }
                        }

                    }
                });




    }



    private void retriving_and_displaying() {

        File folder = new File(getFilesDir(), "Images" + userid); // Change "MyVideos" to your folder name
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {

                // Add URIs of all video files from the folder to the videoUrisList
                for (File file : files) {

                    if (file.isFile()) {

                        retriveduri = Uri.fromFile(file);


                        if (!imglist.contains(retriveduri)) {
                            ImageView imageView = new ImageView(photosactivity.this);
                            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                            layoutParams.height = height / 6;
                            layoutParams.width = width / 3;

                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            tick = new ImageView(photosactivity.this);
                            tick.setImageResource(R.drawable.baseline_check_box_24);
                            tick.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            tick.setScaleY(0.7f);
                            tick.setScaleX(0.7f);

                            FrameLayout layout = new FrameLayout(photosactivity.this);
                            layout.setLayoutParams(layoutParams);
                            layout.addView(imageView);
                            layout.addView(tick);
                            ticking.add(tick);
                            photocontainer.addView(layout);
                            tick.setVisibility(View.GONE);
                            imglist.add(retriveduri);
                            allimagelist.add(imageView);

                           imageView.setImageURI(retriveduri);


                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ImageView currentimage=(ImageView) view;
                                    int pos=allimagelist.indexOf(currentimage);
                                    if (!isselected) {
                                        Intent intent = new Intent(photosactivity.this, zoomphoto.class);
                                        intent.putExtra("imguri",imglist.get(pos));
                                        intent.putExtra("index",pos);
                                        startActivity(intent);
                                    }
                                    if (isselected) {

                                        if (selectedImages.contains(imglist.get(pos))) {
                                            ticking.get(pos).setVisibility(View.GONE);
                                            selectedImages.remove(imglist.get(pos));
                                            imagedrawable.remove(imageView.getDrawable());
                                            if (selectedImages.isEmpty()) {
                                                isselected = false;
                                                selectall.setVisibility(View.GONE);
                                            }
                                        } else {
                                            ticking.get(pos).setVisibility(View.VISIBLE);
                                            selectedImages.add(imglist.get(pos));
                                            imagedrawable.add(imageView.getDrawable());
                                            selectall.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            });
                            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    isselected = true;
                                    ImageView curentimage=(ImageView) view;
                                    int pos=allimagelist.indexOf(curentimage);
                                    if (selectedImages.contains(imglist.get(pos))) {
                                        ticking.get(pos).setVisibility(View.GONE);
                                        selectedImages.remove(imglist.get(pos));
                                        imagedrawable.remove(imageView.getDrawable());
                                        if (selectedImages.isEmpty()) {
                                            isselected = false;
                                            selectall.setVisibility(View.GONE);
                                        }
                                    } else {
                                        ticking.get(pos).setVisibility(View.VISIBLE);
                                        selectedImages.add(imglist.get(pos));
                                        imagedrawable.add(imageView.getDrawable());
                                        selectall.setVisibility(View.VISIBLE);

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







    private void opengallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        gallerylauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 500) {
            if (resultCode == Activity.RESULT_OK) {
                /* Edit request granted; proceed. */
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You need to delete the photos to store here").setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PendingIntent editPendingIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                            editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(),
                                    deletinguris);
                        }
                        try {
                            startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                    500, null, 0, 0, 0);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if(grantResults.length>0)
            {
               opengallery();
            }
        }
    }
    private void upload(Uri imguri) {

        String folderimagename="Images"+userid;
        File imagefolder=new File(getFilesDir(),folderimagename);
        if(!imagefolder.exists())
        {
            imagefolder.mkdir();
        }
        String imagefilename="image"+System.currentTimeMillis()+".jpg";
        File internalvideoFile = new File(imagefolder,imagefilename);
        try {
            InputStream inputStream = getContentResolver().openInputStream(imguri);
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


        flag++;
                                Uri curi = converttocontenturi(imguri);
                                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                                    if(count==flag)
                                    {
                                        Toast.makeText(photosactivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        retriving_and_displaying();

                                    }
                                        getContentResolver().delete(curi, null, null);
                                    }

                                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
                                {
                                    if(count==flag)
                                    {
                                        progressBar.setVisibility(View.GONE);

                                        retriving_and_displaying();
                                        Toast.makeText(photosactivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                        PendingIntent editPendingIntent = MediaStore.createDeleteRequest(getContentResolver(),
                                                deletinguris);
                                        try {
                                            startIntentSenderForResult(editPendingIntent.getIntentSender(),
                                                    500, null, 0, 0, 0);
                                        } catch (IntentSender.SendIntentException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.photosmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.delete) {
            if (imglist.isEmpty()) {
                Toast.makeText(this, "Please upload images", Toast.LENGTH_SHORT).show();
            } else if (selectedImages.isEmpty()) {
                Toast.makeText(this, "Please select images", Toast.LENGTH_SHORT).show();
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

                       dialog.dismiss();
                       Intent intent=new Intent(photosactivity.this,menu.class);
                       startActivity(intent);
                       finish();
                       for(Uri singleuri : selectedImages)
                       {
                           File deletimgimagefile=new File(singleuri.getPath());
                           if(deletimgimagefile.exists())
                           {
                               deletimgimagefile.delete();
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
        else if(id==R.id.unhide)
        {
            if(imglist.isEmpty())
            {
                Toast.makeText(this, "Please upload images", Toast.LENGTH_SHORT).show();
            }
            else  if(selectedImages.isEmpty())
            {
                Toast.makeText(this, "Please select images", Toast.LENGTH_SHORT).show();
            }
            else {
                Dialog dialog=new Dialog(this);
                dialog.setContentView(R.layout.customdialogunhide);
                dialog.setCancelable(false);
                dialog.show();
                Button btnyes=dialog.findViewById(R.id.btnyesunhide);
                Button btnno=dialog.findViewById(R.id.btnnounhide);
                btnyes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                         progressBar.setVisibility(View.VISIBLE);

                        Intent intent=new Intent(photosactivity.this,menu.class);
                        startActivity(intent);
                        finish();
                        for(Uri singleuri : selectedImages)
                        {
                            try {
                                InputStream is=getContentResolver().openInputStream(singleuri);
                                Bitmap bitmap=BitmapFactory.decodeStream(is);
                                is.close();
                                saveimagetogallery(bitmap);
                                File file=new File(singleuri.getPath());
                                if(file.exists())
                                {
                                    file.delete();
                                }
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                });

                btnno.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(int i=0;i<ticking.size();i++)
                        {
                            ticking.get(i).setVisibility(View.GONE);
                            isselected=false;
                            dialog.dismiss();
                        }
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveimagetogallery(Bitmap bitmap) {

        Uri images;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG" + System.currentTimeMillis() + "jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(images, contentValues);

        try {
            OutputStream outputStream = getContentResolver().openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
            saveimages++;
            if(selectedImages.size()==1) {
                Toast.makeText(photosactivity.this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
            }
            else {
                if (selectedImages.size() == saveimages) {
                    Toast.makeText(photosactivity.this, "Images saved to gallery", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        }


    }

    private Uri converttocontenturi(Uri imguri) {

        String id = imguri.getLastPathSegment();

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


        Uri contenturi = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, builder.toString());
        deletinguris.add(contenturi);
        return contenturi;
    }

    private void findingids() {
        toolbar=findViewById(R.id.toolbar);
        photocontainer=findViewById(R.id.photocontainer);
        btngalleryphoto=findViewById(R.id.btngalleryphoto);
        progressBar=findViewById(R.id.progressbar);
        imglist=new ArrayList<>();
        deletinguris=new ArrayList<>();
        ticking=new ArrayList<>();
        selectall=findViewById(R.id.selectall);
        imagedrawable=new ArrayList<>();
        allimagelist=new ArrayList<>();
        setSupportActionBar(toolbar);
    }
    private void gettingdisplaymetrics() {
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height=displayMetrics.heightPixels;
        width=displayMetrics.widthPixels;

    }

}