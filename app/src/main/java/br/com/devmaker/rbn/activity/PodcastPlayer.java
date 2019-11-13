package br.com.devmaker.rbn.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.adapter.PodcastsListAdapter;
import br.com.devmaker.rbn.model.ValueClass;
import br.com.devmaker.rbn.services.PlayerServicePodcast;
import br.com.devmaker.rbn.util.CacheData;

public class PodcastPlayer extends AppCompatActivity {

    private CacheData cacheData;
    private RelativeLayout relativeLayoutTopBar;
    private ImageView imageViewBack;
    private Intent intent;
    private TextView txtNomePodcast;
    TextView dataPublicacaoPodcast, dataPublicacaoPodcast2, dataPublicacaoPodcast3, dataPublicacaoPodcast4, dataPublicacaoPodcast5, dataPublicacaoPodcast6, dataPublicacaoPodcast7, dataPublicacaoPodcast8, dataPublicacaoPodcast9, dataPublicacaoPodcast10;
    TextView tempoAudioPodcast, tempoAudioPodcast2, tempoAudioPodcast3, tempoAudioPodcast4, tempoAudioPodcast5, tempoAudioPodcast6, tempoAudioPodcast7, tempoAudioPodcast8, tempoAudioPodcast9, tempoAudioPodcast10;
    ImageButton btnPlayPausePodcast, btnPlayPausePodcast2, btnPlayPausePodcast3, btnPlayPausePodcast4, btnPlayPausePodcast5, btnPlayPausePodcast6, btnPlayPausePodcast7, btnPlayPausePodcast8, btnPlayPausePodcast9, btnPlayPausePodcast10;
    SeekBar progressBarPodcast, progressBarPodcast2, progressBarPodcast3, progressBarPodcast4, progressBarPodcast5, progressBarPodcast6, progressBarPodcast7, progressBarPodcast8, progressBarPodcast9, progressBarPodcast10;
    ProgressBar loadingIcon, loadingIcon2, loadingIcon3, loadingIcon4, loadingIcon5, loadingIcon6, loadingIcon7, loadingIcon8, loadingIcon9, loadingIcon10;
    RelativeLayout podcast_control, podcast_control2, podcast_control3, podcast_control4, podcast_control5, podcast_control6, podcast_control7, podcast_control8, podcast_control9, podcast_control10;
    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();
    private Boolean isPlaying = false;
    private Boolean firstTime = true;
    Runnable notification, notification2;
    private int lastPodcast, position;
    private String timeAgo;
    public MainActivity mainActivity;
    private Context context;
    private String linkPodcast, linkPodcast2, linkPodcast3, linkPodcast4, linkPodcast5, linkPodcast6, linkPodcast7, linkPodcast8, linkPodcast9, linkPodcast10, data, data2, data3, data4, data5, data6, data7, data8, data9, data10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast_player);

        intent = getIntent();
        cacheData = new CacheData(getApplicationContext());
        int color = Color.parseColor(cacheData.getString("color"));
        context = getApplicationContext();

        if (intent.getStringExtra("link") != null){
            linkPodcast = intent.getStringExtra("link");
            data = intent.getStringExtra("data");
        }
        if (intent.getStringExtra("link2") != null){
            linkPodcast2 = intent.getStringExtra("link2");
            data2 = intent.getStringExtra("data2");
        }
        if (intent.getStringExtra("link3") != null){
            linkPodcast3 = intent.getStringExtra("link3");
            data3 = intent.getStringExtra("data3");
        }
        if (intent.getStringExtra("link4") != null){
            linkPodcast4 = intent.getStringExtra("link4");
            data4 = intent.getStringExtra("data4");
        }
        if (intent.getStringExtra("link5") != null){
            linkPodcast5 = intent.getStringExtra("link5");
            data5 = intent.getStringExtra("data5");
        }
        if (intent.getStringExtra("link6") != null){
            linkPodcast6 = intent.getStringExtra("link6");
            data6 = intent.getStringExtra("data6");
        }
        if (intent.getStringExtra("link7") != null){
            linkPodcast7 = intent.getStringExtra("link7");
            data7 = intent.getStringExtra("data7");
        }
        if (intent.getStringExtra("link8") != null){
            linkPodcast8 = intent.getStringExtra("link8");
            data8 = intent.getStringExtra("data8");
        }
        if (intent.getStringExtra("link9") != null){
            linkPodcast9 = intent.getStringExtra("link9");
            data9 = intent.getStringExtra("data9");
        }
        if (intent.getStringExtra("link10") != null){
            linkPodcast10 = intent.getStringExtra("link10");
            data10 = intent.getStringExtra("data10");
        }


        dataPublicacaoPodcast = (TextView)  findViewById(R.id.dataPublicacaoPodcast);
        tempoAudioPodcast = (TextView)  findViewById(R.id.tempoAudioPodcast);
        btnPlayPausePodcast = (ImageButton)  findViewById(R.id.btnPlayPausePodcast);
        progressBarPodcast = (SeekBar)  findViewById(R.id.progressBarPodcast);
        loadingIcon = (ProgressBar)  findViewById(R.id.loadingIcon);
        podcast_control = (RelativeLayout)  findViewById(R.id.podcast_control);

        podcast_control2 = (RelativeLayout)  findViewById(R.id.podcast_control2);
        tempoAudioPodcast2 = (TextView)  findViewById(R.id.tempoAudioPodcast2);
        btnPlayPausePodcast2 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast2);
        progressBarPodcast2 = (SeekBar)  findViewById(R.id.progressBarPodcast2);
        loadingIcon2 = (ProgressBar)  findViewById(R.id.loadingIcon2);
        dataPublicacaoPodcast2 = (TextView)  findViewById(R.id.dataPublicacaoPodcast2);

        podcast_control3 = (RelativeLayout)  findViewById(R.id.podcast_control3);
        tempoAudioPodcast3 = (TextView)  findViewById(R.id.tempoAudioPodcast3);
        btnPlayPausePodcast3 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast3);
        progressBarPodcast3 = (SeekBar)  findViewById(R.id.progressBarPodcast3);
        loadingIcon3 = (ProgressBar)  findViewById(R.id.loadingIcon3);
        dataPublicacaoPodcast3 = (TextView)  findViewById(R.id.dataPublicacaoPodcast3);

        podcast_control4 = (RelativeLayout)  findViewById(R.id.podcast_control4);
        tempoAudioPodcast4 = (TextView)  findViewById(R.id.tempoAudioPodcast4);
        btnPlayPausePodcast4 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast4);
        progressBarPodcast4 = (SeekBar)  findViewById(R.id.progressBarPodcast4);
        loadingIcon4 = (ProgressBar)  findViewById(R.id.loadingIcon4);
        dataPublicacaoPodcast4 = (TextView)  findViewById(R.id.dataPublicacaoPodcast4);

        podcast_control5 = (RelativeLayout)  findViewById(R.id.podcast_control5);
        tempoAudioPodcast5 = (TextView)  findViewById(R.id.tempoAudioPodcast5);
        btnPlayPausePodcast5 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast5);
        progressBarPodcast5 = (SeekBar)  findViewById(R.id.progressBarPodcast5);
        loadingIcon5 = (ProgressBar)  findViewById(R.id.loadingIcon5);
        dataPublicacaoPodcast5 = (TextView)  findViewById(R.id.dataPublicacaoPodcast5);

        podcast_control6 = (RelativeLayout)  findViewById(R.id.podcast_control6);
        tempoAudioPodcast6 = (TextView)  findViewById(R.id.tempoAudioPodcast6);
        btnPlayPausePodcast6 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast6);
        progressBarPodcast6 = (SeekBar)  findViewById(R.id.progressBarPodcast6);
        loadingIcon6 = (ProgressBar)  findViewById(R.id.loadingIcon6);
        dataPublicacaoPodcast6 = (TextView)  findViewById(R.id.dataPublicacaoPodcast6);

        podcast_control7 = (RelativeLayout)  findViewById(R.id.podcast_control7);
        tempoAudioPodcast7 = (TextView)  findViewById(R.id.tempoAudioPodcast7);
        btnPlayPausePodcast7 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast7);
        progressBarPodcast7 = (SeekBar)  findViewById(R.id.progressBarPodcast7);
        loadingIcon7 = (ProgressBar)  findViewById(R.id.loadingIcon7);
        dataPublicacaoPodcast7 = (TextView)  findViewById(R.id.dataPublicacaoPodcast7);

        podcast_control8 = (RelativeLayout)  findViewById(R.id.podcast_control8);
        tempoAudioPodcast8 = (TextView)  findViewById(R.id.tempoAudioPodcast8);
        btnPlayPausePodcast8 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast8);
        progressBarPodcast8 = (SeekBar)  findViewById(R.id.progressBarPodcast8);
        loadingIcon8 = (ProgressBar)  findViewById(R.id.loadingIcon8);
        dataPublicacaoPodcast8 = (TextView)  findViewById(R.id.dataPublicacaoPodcast8);

        podcast_control9 = (RelativeLayout)  findViewById(R.id.podcast_control9);
        tempoAudioPodcast9 = (TextView)  findViewById(R.id.tempoAudioPodcast9);
        btnPlayPausePodcast9 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast9);
        progressBarPodcast9 = (SeekBar)  findViewById(R.id.progressBarPodcast9);
        loadingIcon9 = (ProgressBar)  findViewById(R.id.loadingIcon9);
        dataPublicacaoPodcast9 = (TextView)  findViewById(R.id.dataPublicacaoPodcast9);

        podcast_control10 = (RelativeLayout)  findViewById(R.id.podcast_control10);
        tempoAudioPodcast10 = (TextView)  findViewById(R.id.tempoAudioPodcast10);
        btnPlayPausePodcast10 = (ImageButton)  findViewById(R.id.btnPlayPausePodcast10);
        progressBarPodcast10 = (SeekBar)  findViewById(R.id.progressBarPodcast10);
        loadingIcon10 = (ProgressBar)  findViewById(R.id.loadingIcon10);
        dataPublicacaoPodcast10 = (TextView)  findViewById(R.id.dataPublicacaoPodcast10);
        relativeLayoutTopBar = findViewById(R.id.relativeLayoutTopBarPodcastPlayer);
        imageViewBack = findViewById(R.id.imageView);
        txtNomePodcast = findViewById(R.id.textView6);

        tempoAudioPodcast.setTextColor(color);
        tempoAudioPodcast2.setTextColor(color);
        tempoAudioPodcast3.setTextColor(color);
        tempoAudioPodcast4.setTextColor(color);
        tempoAudioPodcast5.setTextColor(color);
        tempoAudioPodcast6.setTextColor(color);
        tempoAudioPodcast7.setTextColor(color);
        tempoAudioPodcast8.setTextColor(color);
        tempoAudioPodcast9.setTextColor(color);
        tempoAudioPodcast10.setTextColor(color);

        progressBarPodcast.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast2.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast3.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast4.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast5.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast6.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast7.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast8.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast9.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        progressBarPodcast10.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        progressBarPodcast.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast2.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast3.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast4.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast5.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast6.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast7.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast8.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast9.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        progressBarPodcast10.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        final float scale = podcast_control2.getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (38 * scale + 0.5f);
        int pixels2 = (int) (20 * scale + 0.5f);
        int pixels10dp = (int) (10 * scale + 0.5f);


        LinearLayout.LayoutParams rel_btn = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, pixels);

        LinearLayout.LayoutParams rel_txt = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, pixels2);
        rel_txt.setMargins(0, pixels10dp, 0, (int) (5 * scale + 0.5f));
        rel_txt.setMarginEnd(pixels10dp);
        rel_txt.setMarginStart(pixels10dp);

        LinearLayout.LayoutParams rel_btn2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);

        dataPublicacaoPodcast.setText(data);

        if (linkPodcast2!=null){
            podcast_control2.setVisibility(View.VISIBLE);
            podcast_control2.setLayoutParams(rel_btn);
            dataPublicacaoPodcast2.setText(data2);
            dataPublicacaoPodcast2.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast2.setLayoutParams(rel_txt);
        }else {
            podcast_control2.setVisibility(View.INVISIBLE);
            podcast_control2.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast2.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast2.setLayoutParams(rel_btn2);
        }

        if (linkPodcast3!=null){
            podcast_control3.setVisibility(View.VISIBLE);
            podcast_control3.setLayoutParams(rel_btn);
            dataPublicacaoPodcast3.setText(data3);
            dataPublicacaoPodcast3.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast3.setLayoutParams(rel_txt);
        }else {
            podcast_control3.setVisibility(View.INVISIBLE);
            podcast_control3.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast3.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast3.setLayoutParams(rel_btn2);
        }

        if (linkPodcast4!=null){
            podcast_control4.setVisibility(View.VISIBLE);
            podcast_control4.setLayoutParams(rel_btn);
            dataPublicacaoPodcast4.setText(data4);
            dataPublicacaoPodcast4.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast4.setLayoutParams(rel_txt);
        }else {
            podcast_control4.setVisibility(View.INVISIBLE);
            podcast_control4.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast4.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast4.setLayoutParams(rel_btn2);
        }

        if (linkPodcast5!=null){
            podcast_control5.setVisibility(View.VISIBLE);
            podcast_control5.setLayoutParams(rel_btn);
            dataPublicacaoPodcast5.setText(data5);
            dataPublicacaoPodcast5.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast5.setLayoutParams(rel_txt);
        }else {
            podcast_control5.setVisibility(View.INVISIBLE);
            podcast_control5.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast5.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast5.setLayoutParams(rel_btn2);
        }

        if (linkPodcast6!=null){
            podcast_control6.setVisibility(View.VISIBLE);
            podcast_control6.setLayoutParams(rel_btn);
            dataPublicacaoPodcast6.setText(data6);
            dataPublicacaoPodcast6.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast6.setLayoutParams(rel_txt);
        }else {
            podcast_control6.setVisibility(View.INVISIBLE);
            podcast_control6.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast6.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast6.setLayoutParams(rel_btn2);
        }

        if (linkPodcast7!=null){
            podcast_control7.setVisibility(View.VISIBLE);
            podcast_control7.setLayoutParams(rel_btn);
            dataPublicacaoPodcast7.setText(data7);
            dataPublicacaoPodcast7.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast7.setLayoutParams(rel_txt);
        }else {
            podcast_control7.setVisibility(View.INVISIBLE);
            podcast_control7.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast7.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast7.setLayoutParams(rel_btn2);
        }

        if (linkPodcast8!=null){
            podcast_control8.setVisibility(View.VISIBLE);
            podcast_control8.setLayoutParams(rel_btn);
            dataPublicacaoPodcast8.setText(data8);
            dataPublicacaoPodcast8.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast8.setLayoutParams(rel_txt);
        }else {
            podcast_control8.setVisibility(View.INVISIBLE);
            podcast_control8.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast8.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast8.setLayoutParams(rel_btn2);
        }

        if (linkPodcast9!=null){
            podcast_control9.setVisibility(View.VISIBLE);
            podcast_control9.setLayoutParams(rel_btn);
            dataPublicacaoPodcast9.setText(data9);
            dataPublicacaoPodcast9.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast9.setLayoutParams(rel_txt);
        }else {
            podcast_control9.setVisibility(View.INVISIBLE);
            podcast_control9.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast9.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast9.setLayoutParams(rel_btn2);
        }

        if (linkPodcast10!=null){
            podcast_control10.setVisibility(View.VISIBLE);
            podcast_control10.setLayoutParams(rel_btn);
            dataPublicacaoPodcast10.setText(data10);
            dataPublicacaoPodcast10.setVisibility(View.VISIBLE);
            dataPublicacaoPodcast10.setLayoutParams(rel_txt);
        }else {
            podcast_control10.setVisibility(View.INVISIBLE);
            podcast_control10.setLayoutParams(rel_btn2);
            dataPublicacaoPodcast10.setVisibility(View.INVISIBLE);
            dataPublicacaoPodcast10.setLayoutParams(rel_btn2);
        }

        btnPlayPausePodcast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //
                // SE NENHUM PODCAST ESTIVER TOCANDO
                if (!isPlaying){

                    mudarBtnPlayer();
                    lastPodcast = position;
                    btnPlayPausePodcast.setVisibility(View.INVISIBLE);
                    loadingIcon.setVisibility(View.VISIBLE);


                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon.setVisibility(View.GONE);
                    btnPlayPausePodcast.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater(position);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;

                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {
                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();


                        mudarBtnPlayer();
                        lastPodcast = position;
                        btnPlayPausePodcast.setVisibility(View.INVISIBLE);
                        loadingIcon.setVisibility(View.VISIBLE);


                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon.setVisibility(View.GONE);
                        btnPlayPausePodcast.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater(position);
                    }
                }



            }
        });

        btnPlayPausePodcast2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                
                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 1000;
                    btnPlayPausePodcast2.setVisibility(View.INVISIBLE);
                    loadingIcon2.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast2)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon2.setVisibility(View.GONE);
                    btnPlayPausePodcast2.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast2.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater2(position + 1000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 1000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast2.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 1000;
                        btnPlayPausePodcast2.setVisibility(View.INVISIBLE);
                        loadingIcon2.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast2)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon2.setVisibility(View.GONE);
                        btnPlayPausePodcast2.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast2.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater2(position + 1000);

                    }
                }



            }
        });

        btnPlayPausePodcast3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 3000;
                    btnPlayPausePodcast3.setVisibility(View.INVISIBLE);
                    loadingIcon3.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast3)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon3.setVisibility(View.GONE);
                    btnPlayPausePodcast3.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast3.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater3(position + 3000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 3000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast3.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 3000;
                        btnPlayPausePodcast3.setVisibility(View.INVISIBLE);
                        loadingIcon3.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast3)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon3.setVisibility(View.GONE);
                        btnPlayPausePodcast3.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast3.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater3(position + 3000);

                    }
                }



            }
        });

        btnPlayPausePodcast4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 4000;
                    btnPlayPausePodcast4.setVisibility(View.INVISIBLE);
                    loadingIcon4.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast4)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon4.setVisibility(View.GONE);
                    btnPlayPausePodcast4.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast4.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater4(position + 4000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 4000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast4.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 4000;
                        btnPlayPausePodcast4.setVisibility(View.INVISIBLE);
                        loadingIcon4.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast4)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon4.setVisibility(View.GONE);
                        btnPlayPausePodcast4.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast4.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater4(position + 4000);

                    }
                }



            }
        });

        btnPlayPausePodcast5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 5000;
                    btnPlayPausePodcast5.setVisibility(View.INVISIBLE);
                    loadingIcon5.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast5)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon5.setVisibility(View.GONE);
                    btnPlayPausePodcast5.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast5.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater5(position + 5000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 5000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast5.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 5000;
                        btnPlayPausePodcast5.setVisibility(View.INVISIBLE);
                        loadingIcon5.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast5)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon5.setVisibility(View.GONE);
                        btnPlayPausePodcast5.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast5.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater5(position + 5000);

                    }
                }



            }
        });

        btnPlayPausePodcast6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 6000;
                    btnPlayPausePodcast6.setVisibility(View.INVISIBLE);
                    loadingIcon6.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast6)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon6.setVisibility(View.GONE);
                    btnPlayPausePodcast6.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast6.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater6(position + 6000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 6000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast6.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 6000;
                        btnPlayPausePodcast6.setVisibility(View.INVISIBLE);
                        loadingIcon6.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast6)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon6.setVisibility(View.GONE);
                        btnPlayPausePodcast6.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast6.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater6(position + 6000);

                    }
                }



            }
        });

        btnPlayPausePodcast7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 7000;
                    btnPlayPausePodcast7.setVisibility(View.INVISIBLE);
                    loadingIcon7.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast7)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon7.setVisibility(View.GONE);
                    btnPlayPausePodcast7.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast7.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater7(position + 7000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 7000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast7.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 7000;
                        btnPlayPausePodcast7.setVisibility(View.INVISIBLE);
                        loadingIcon7.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast7)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon7.setVisibility(View.GONE);
                        btnPlayPausePodcast7.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast7.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater7(position + 7000);

                    }
                }



            }
        });

        btnPlayPausePodcast8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 8000;
                    btnPlayPausePodcast8.setVisibility(View.INVISIBLE);
                    loadingIcon8.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast8)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon8.setVisibility(View.GONE);
                    btnPlayPausePodcast8.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast8.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater8(position + 8000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 8000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast8.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 8000;
                        btnPlayPausePodcast8.setVisibility(View.INVISIBLE);
                        loadingIcon8.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast8)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon8.setVisibility(View.GONE);
                        btnPlayPausePodcast8.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast8.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater8(position + 8000);

                    }
                }



            }
        });

        btnPlayPausePodcast9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 9000;
                    btnPlayPausePodcast9.setVisibility(View.INVISIBLE);
                    loadingIcon9.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast9)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon9.setVisibility(View.GONE);
                    btnPlayPausePodcast9.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast9.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater9(position + 9000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 9000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast9.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 9000;
                        btnPlayPausePodcast9.setVisibility(View.INVISIBLE);
                        loadingIcon9.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast9)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon9.setVisibility(View.GONE);
                        btnPlayPausePodcast9.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast9.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater9(position + 9000);

                    }
                }



            }
        });

        btnPlayPausePodcast10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // SE NENHUM PODCAST ESTIVER TOCANDO



                if (!isPlaying){

                    mudarBtnPlayer();

                    lastPodcast = position + 10000;
                    btnPlayPausePodcast10.setVisibility(View.INVISIBLE);
                    loadingIcon10.setVisibility(View.VISIBLE);



                    Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                    serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast10)));
                    serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent);
                    } else {
                        startService(serviceIntent);
                    }

                    ValueClass.getInstance().setAudioTocando(true);
                    isPlaying = true;
                    firstTime = false;
                    loadingIcon10.setVisibility(View.GONE);
                    btnPlayPausePodcast10.setVisibility(View.VISIBLE);
                    btnPlayPausePodcast10.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                    primarySeekBarProgressUpdater10(position + 10000);

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {

                    Log.d("AAAAAA", "ERROR 904");

                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastPodcast == position  + 10000){
                        ValueClass.getInstance().setAudioTocando(false);

                        stopPlaying();
                        btnPlayPausePodcast10.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {

                        ValueClass.getInstance().getPlayer().stop();
                        ValueClass.getInstance().getPlayer().release();

                        Log.d("AAAAAA", "ERROR 905");

                        // PARAR RUNNABLE
                        handler.removeCallbacks(notification);
                        handler2.removeCallbacks(notification2);


                        mudarBtnPlayer();

                        lastPodcast = position + 10000;
                        btnPlayPausePodcast10.setVisibility(View.INVISIBLE);
                        loadingIcon10.setVisibility(View.VISIBLE);



                        Intent serviceIntent = new Intent(getApplicationContext(), PlayerServicePodcast.class);
                        serviceIntent.putExtra("uri", String.valueOf(Uri.parse(linkPodcast10)));
                        serviceIntent.setAction(PlayerServicePodcast.ACTION_RESUME);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(serviceIntent);
                        } else {
                            startService(serviceIntent);
                        }

                        ValueClass.getInstance().setAudioTocando(true);
                        isPlaying = true;
                        firstTime = false;
                        loadingIcon10.setVisibility(View.GONE);
                        btnPlayPausePodcast10.setVisibility(View.VISIBLE);
                        btnPlayPausePodcast10.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);

                        primarySeekBarProgressUpdater10(position + 10000);

                    }
                }



            }
        });

        // SEEKBAR CHANGE
        progressBarPodcast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast2.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast2.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast3.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast3.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast4.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast4.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast5.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast5.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast6.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast6.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast7.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast7.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast7.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast8.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast8.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast8.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast9.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast9.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast9.setProgress(startPosition);
                }
            }
        });

        progressBarPodcast10.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ValueClass.getInstance().getPlayer() != null && fromUser){
                    playPositionInMillisecconds = ((int) ValueClass.getInstance().getPlayer().getDuration() / 100) * progressBarPodcast10.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (ValueClass.getInstance().getPlayer() != null){
                    ValueClass.getInstance().getPlayer().seekTo(playPositionInMillisecconds);
                }else {
                    progressBarPodcast10.setProgress(startPosition);
                }
            }
        });

        txtNomePodcast.setText(intent.getStringExtra("nome"));

        if (!cacheData.getString("color").equals("")) {
            relativeLayoutTopBar.setBackgroundColor(color);
        }

        imageViewBack.setOnClickListener(view -> PodcastPlayer.super.onBackPressed());

    }


    public void setProgressBarColor(ProgressBar progressBar, int newColor){
        LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
        ClipDrawable d1 = (ClipDrawable) ld.findDrawableByLayerId(R.id.progressshape);
        d1.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);

    }

    private void primarySeekBarProgressUpdater(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = () -> primarySeekBarProgressUpdater(position);
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater2(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast2.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast2.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater2(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater2(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater3(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast3.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast3.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater3(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater3(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater4(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast4.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast4.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater4(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater4(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater5(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast5.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast5.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater5(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater5(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater6(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast6.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast6.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater6(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater6(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater7(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast7.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast7.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater7(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater7(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater8(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast8.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast8.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater8(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater8(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater9(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast9.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast9.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater9(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater9(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    private void primarySeekBarProgressUpdater10(final int position) {

        notification2 = () -> {
            if (ValueClass.getInstance().getPlayer()!=null && ValueClass.getInstance().getPlayer().getDuration() > 0){
                progressBarPodcast10.setProgress((int) (((float) ValueClass.getInstance().getPlayer().getCurrentPosition() / (int) ValueClass.getInstance().getPlayer().getDuration()) * 100));
                tempoAudioPodcast10.setText("" + milliSecondsToTimer(ValueClass.getInstance().getPlayer().getCurrentPosition()));
                notification = new Runnable() {
                    public void run() {
                        primarySeekBarProgressUpdater10(position);
                    }
                };
                handler.postDelayed(notification, 1000);
            }else {
                primarySeekBarProgressUpdater10(position);
            }
        };
        handler.postDelayed(notification2, 1000);
    }

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String minutesFormatted;

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (minutes < 10){
            minutesFormatted = "0" + minutes;
        } else {
            minutesFormatted = "" + minutes;
        }

        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutesFormatted + ":" + secondsString;

        return finalTimerString;
    }


    
    private void mudarBtnPlayer(){
        loadingIcon.setVisibility(View.GONE);
        loadingIcon2.setVisibility(View.GONE);
        loadingIcon3.setVisibility(View.GONE);
        loadingIcon4.setVisibility(View.GONE);
        loadingIcon5.setVisibility(View.GONE);
        loadingIcon6.setVisibility(View.GONE);
        loadingIcon7.setVisibility(View.GONE);
        loadingIcon8.setVisibility(View.GONE);
        loadingIcon9.setVisibility(View.GONE);
        loadingIcon10.setVisibility(View.GONE);
        btnPlayPausePodcast.setVisibility(View.VISIBLE);
        btnPlayPausePodcast2.setVisibility(View.VISIBLE);
        btnPlayPausePodcast3.setVisibility(View.VISIBLE);
        btnPlayPausePodcast4.setVisibility(View.VISIBLE);
        btnPlayPausePodcast5.setVisibility(View.VISIBLE);
        btnPlayPausePodcast6.setVisibility(View.VISIBLE);
        btnPlayPausePodcast7.setVisibility(View.VISIBLE);
        btnPlayPausePodcast8.setVisibility(View.VISIBLE);
        btnPlayPausePodcast9.setVisibility(View.VISIBLE);
        btnPlayPausePodcast10.setVisibility(View.VISIBLE);
        btnPlayPausePodcast.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast2.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast3.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast4.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast5.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast6.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast7.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast8.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast9.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
        btnPlayPausePodcast10.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
    }

    @Override
    protected void onStop() {
        stopPlaying();
        super.onStop();
    }

    private void stopPlaying() {
        Intent serviceIntent = new Intent(this, PlayerServicePodcast.class);
        serviceIntent.setAction(PlayerServicePodcast.ACTION_PAUSE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

}