package com.example.calculatorlock;


import static com.example.calculatorlock.videosactivity.allvideolist;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector;
import androidx.media3.ui.PlayerView;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;


public class playingvideo extends AppCompatActivity {
    Uri videouri;
    ExoPlayer player;
    Player.Listener playbackStateListener=createPlaybackStateListener();
    boolean playwhenready=true;
    int current=0;
    long playbackposition=0;
    PlayerView videoView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playingvideo);


       String videostring= getIntent().getStringExtra("key");
       current=getIntent().getIntExtra("position",0);
       videouri= Uri.parse(videostring);
       videoView=findViewById(R.id.videoview);



    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onStart() {
        super.onStart();

            initializePlayer();

    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (  player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

            releasePlayer();

    }

    @Override
    public void onStop() {
        super.onStop();

            releasePlayer();

    }


        @UnstableApi
    private void  initializePlayer() {
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(playingvideo.this);
        trackSelector.setParameters(trackSelector.buildUponParameters().setMaxVideoSizeSd());


        player = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();

        videoView.setPlayer(player);

            List<MediaItem> mediaItems=new ArrayList<>();
           for(Uri single: allvideolist)
           {
               mediaItems.add(MediaItem.fromUri(single));
           }




        player.setMediaItems(mediaItems);
        player.setPlayWhenReady(playwhenready);
        player.seekTo(current, playbackposition);
       player.addListener(playbackStateListener);
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playbackposition = player.getCurrentPosition();
            current = player.getCurrentMediaItemIndex();
            playwhenready = player.getPlayWhenReady();
         player.removeListener(playbackStateListener);
            player.release();
        }
        player = null;
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(
                getWindow(), videoView);

        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    private Player.Listener createPlaybackStateListener() {
        return new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                String stateString = "";
                switch (playbackState) {
                    case ExoPlayer.STATE_IDLE:
                        stateString = "ExoPlayer.STATE_IDLE      -";
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        stateString = "ExoPlayer.STATE_BUFFERING -";
                        break;
                    case ExoPlayer.STATE_READY:
                        stateString = "ExoPlayer.STATE_READY     -";
                        break;
                    case ExoPlayer.STATE_ENDED:
                        stateString = "ExoPlayer.STATE_ENDED     -";
                        break;
                    default:
                        stateString = "UNKNOWN_STATE             -";
                        break;
                }

            }
        };
    }

}