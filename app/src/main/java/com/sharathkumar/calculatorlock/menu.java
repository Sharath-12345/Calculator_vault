package com.sharathkumar.calculatorlock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.UUID;

public class menu extends AppCompatActivity {
    LinearLayout linearLayout;
    Button btnphotos,btnvideos;
    static int height,width;
    static String userid;
    Toolbar toolbar;
    NativeAd mnativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findingallids();

        loadNativeAd();

        displaymetrics();

        settingheightandwidth();

        gettingsharedprefernce();

        btnphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(menu.this,photosactivity.class);
                startActivity(intent);
            }
        });
        btnvideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(menu.this,videosactivity.class);
                startActivity(intent);
            }
        });


        setSupportActionBar(toolbar);
    }
    private void loadNativeAd()
    {
       AdLoader.Builder adBuilder=new AdLoader.Builder(this,getResources().getString(R.string.nativeadmenu));
       adBuilder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
           @Override
           public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
               mnativeAd=nativeAd;

               FrameLayout frameLayout=findViewById(R.id.framelayout);
               NativeAdView adView=(NativeAdView) getLayoutInflater().inflate(R.layout.nativead,null);
               populateNativeAd(nativeAd,adView);
               frameLayout.removeAllViews();
               frameLayout.addView(adView);

           }
       });
       AdLoader adLoader=adBuilder.withAdListener(new AdListener() {
           @Override
           public void onAdLoaded() {
               super.onAdLoaded();
           }
       }).build();
       adLoader.loadAd(new AdRequest.Builder().build());
    }
    private void populateNativeAd(NativeAd nativeAd,NativeAdView adView)
    {
        adView.setMediaView(adView.findViewById(R.id.ad_media));

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

    }

    private void gettingsharedprefernce() {
        SharedPreferences sharedPreferences=getSharedPreferences("userid",MODE_PRIVATE);
        userid=sharedPreferences.getString("flag","sh");
        if(userid.equals("sh"))
        {
            userid= UUID.randomUUID().toString();
            SharedPreferences sharedPreferences1=getSharedPreferences("userid",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            editor.putString("flag",userid);
            editor.apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.changepass)
        {
            Intent intent=new Intent(menu.this,changepasswordactivity.class);
            startActivity(intent);
        }
        else if(id==R.id.secuquestion)
        {

            Intent intent=new Intent(menu.this,secquestion.class);
            startActivity(intent);
        }
        else if (id==R.id.getatip)
        {
            Dialog dialog=new Dialog(this);
            dialog.setContentView(R.layout.customdialogtip);
            dialog.setCancelable(false);

            dialog.show();
            Button okay=dialog.findViewById(R.id.btnokayforgetpass);
            okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
        else   if(id==R.id.rateourapp)
        {
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.sharathkumar.calculatorlock");
            Intent gotomarket=new Intent(Intent.ACTION_VIEW,uri);
            gotomarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(gotomarket);
            }
            catch (ActivityNotFoundException e)
            {
                Uri weburi=Uri.parse("https://play.google.com/store/apps/details?id=com.sharathkumar.calculatorlock");
                Intent gotoweb=new Intent(Intent.ACTION_VIEW,weburi);
                startActivity(gotoweb);
            }
        }
        else
        {
             Intent intent=new Intent();
             intent.setAction(Intent.ACTION_SEND);
             intent.setType("text/plain");
             intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.sharathkumar.calculatorlock");
             startActivity(Intent.createChooser(intent,"Share via"));
        }
        return super.onOptionsItemSelected(item);
    }



    private void displaymetrics() {
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height=displayMetrics.heightPixels;
        width=displayMetrics.widthPixels;

    }

    private void settingheightandwidth() {
        btnphotos.setHeight(height/5);
        btnphotos.setWidth(width/2);
        btnvideos.setHeight(height/5);
        btnvideos.setWidth(width/2);
    }

    private void findingallids() {
        linearLayout=findViewById(R.id.linearla);
        btnphotos=findViewById(R.id.btnphotos);
        btnvideos=findViewById(R.id.btnvideos);
        toolbar=findViewById(R.id.toolbar);
    }
}