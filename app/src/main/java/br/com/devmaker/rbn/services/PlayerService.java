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

import static com.google.android.exoplayer2.offline.DownloadService.startForeground;

/**
 * Created by Meu computador on 08/03/2019.
 */

public class PlayerService extends Service implements Player.EventListener {

    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private MediaSource mediaSource;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bandwidthMeter = new DefaultBandwidthMeter();

        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);

        defaultBandwidthMeter = new DefaultBandwidthMeter();


        mediaSource = buildMediaSource(Uri.parse(intent.getExtras().getString("uri")));


        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        player.addListener(this);
        player.prepare(mediaSource, true, false);


        player.setPlayWhenReady(true);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);



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
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);
            builder.build();

            Notification notification = builder.build();

            startForeground(1, notification);
        }

        return START_STICKY;
    }

    private MediaSource buildMediaSource(Uri uri) {
        Log.d("XXXX", uri.toString());

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e("Log", error.toString());
    }
}
