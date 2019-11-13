package br.com.devmaker.rbn.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.MainActivity;
import br.com.devmaker.rbn.model.ValueClass;

public class PlayerServicePodcast extends Service implements Player.EventListener {
    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private MediaSource mediaSource;
    private String uri;

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_RESUME = "ACTION_RESUME";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "ACTION_NEXT";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();

        switch (action)
        {
            case ACTION_START_FOREGROUND_SERVICE:
                startPlayer(intent);
                break;
            case ACTION_STOP_FOREGROUND_SERVICE:
                stopForeground(true);
                stopSelf();
                break;
            case ACTION_PLAY:
                resumePlayer();
                break;
            case ACTION_PAUSE:
                pausePlayer();
                break;
            case ACTION_PREVIOUS:
                stopPlayer();
                break;
            case ACTION_RESUME:
                if (player!=null && uri.equals(intent.getExtras().getString("uri"))){
                    Log.e("AAA", "A");
                    resumePlayer();
                }else {
                    Log.e("AAA", "B");
                    startPlayer(intent);
                }
                break;
        }

        return START_STICKY;
    }

    private void startPlayer(Intent intent){
        Intent playIntent = new Intent(this, PlayerService.class);
        playIntent.setAction(ACTION_PLAY);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent pauseIntent = new Intent(this, PlayerService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent ppauseIntent = PendingIntent.getService(this, 0,
                pauseIntent, 0);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(ACTION_START_FOREGROUND_SERVICE);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        uri = intent.getExtras().getString("uri");

        if (intent.getExtras()!=null && uri!=null) {
            bandwidthMeter = new DefaultBandwidthMeter();

            trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(trackSelectionFactory);


            mediaSource = buildMediaSource(Uri.parse(intent.getExtras().getString("uri")));

            if (player!= null) {
                player.release();
            }
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            player.addListener(this);
            player.prepare(mediaSource, true, false);


            player.setPlayWhenReady(true);

            if (player != null) {
                ValueClass.getInstance().setPlayer(player);
            }




            //Intent notificationIntent = new Intent(this, TelaPrincipal.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationChannel channel = new NotificationChannel("Player", "Sync Service", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("Service Name");
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                Notification.Builder builder = new Notification.Builder(this, "Player")
                        .setContentTitle("Music Player")
                        .setContentText("My Music")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
                builder.build();

                Notification notification = builder.build();
                startForeground(1, notification);

            } else {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Music Player")
                        .setContentText("My Music")
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);
                builder.build();

                Notification notification = builder.build();
                startForeground(1, notification);
            }
        }
    }

    private void pausePlayer(){
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    private void resumePlayer(){
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    public void stopPlayer(){
        player.stop();
        player.getPlaybackState();
    }

    private MediaSource buildMediaSource(Uri uri) {
        Log.d("XXXX", uri.toString());

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e("Log", error.toString());
    }
}
