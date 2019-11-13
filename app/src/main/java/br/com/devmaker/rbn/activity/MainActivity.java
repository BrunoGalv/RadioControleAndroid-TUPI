package br.com.devmaker.rbn.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.support.design.widget.BottomNavigationView;
import android.graphics.Color;
import android.media.AudioManager;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.AnalyticsEvent;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.InitializationException;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.MobileAnalyticsManager;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.facebook.FacebookSdk;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import br.com.devmaker.rbn.MyApplication;
import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.adapter.MenuAdapter;
import br.com.devmaker.rbn.dialog.EscolherDialogFragment;
import br.com.devmaker.rbn.dialog.SobreDialogFragment;
import br.com.devmaker.rbn.dialog.SuaContaDialogFragment;
import br.com.devmaker.rbn.fragment.CanaisFragment;
import br.com.devmaker.rbn.fragment.FragmentDrawer;
import br.com.devmaker.rbn.fragment.HomeFragment;
import br.com.devmaker.rbn.fragment.MuralFragment;
import br.com.devmaker.rbn.fragment.NoticiasFragment;
import br.com.devmaker.rbn.fragment.PerfilFragment;
import br.com.devmaker.rbn.fragment.PodcastsFragment;
import br.com.devmaker.rbn.fragment.ProgramacaoFragment;
import br.com.devmaker.rbn.fragment.PromocoesFragment;
import br.com.devmaker.rbn.fragment.SubMenuFragment;
import br.com.devmaker.rbn.model.Channel;
import br.com.devmaker.rbn.model.Host;
import br.com.devmaker.rbn.model.Hosts;
import br.com.devmaker.rbn.model.Program;
import br.com.devmaker.rbn.model.Programs;
import br.com.devmaker.rbn.model.Radio;
import br.com.devmaker.rbn.services.PlayerService;
import br.com.devmaker.rbn.util.ActivityResultBus;
import br.com.devmaker.rbn.util.ActivityResultEvent;
import br.com.devmaker.rbn.util.BottomNavigationViewHelper;
import br.com.devmaker.rbn.util.CognitoClientManager;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.ProgramacaoPlayerReceiver;
import br.com.devmaker.rbn.util.RadiocontroleClient;

//import static accessweb.com.br.radiocontrole.R.id.relativeLogo;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, Player.EventListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private Toolbar pToolbar;
    private FragmentDrawer drawerFragment;
    private DrawerLayout drawerLayout;
    private Context mContext;
    private SlidingUpPanelLayout painel;
    private RelativeLayout dragView;
    private TextView txtRotativo;
    private Activity mActivity;
    private ImageButton imgBtnPlayPause;
    private ImageButton shareButton;
    private ImageView playPauseToolbar;
    private HomeFragment homeFragment;
    private Boolean audioTocando = false;
    private ImageView albumCoverToolbar;
    private ImageView imgCapaAlbum;
    private TextView nomeMusica;
    private TextView txtNomeMusica;
    private TextView nomeArtista;
    private TextView txtNomeCantor;

    private TextView txtHoraInicioPrograma;
    private TextView txtNomePrograma;
    private TextView txtNomeRadialista;
    private TextView txtHoraFimPrograma;


    private LinearLayout areaAnuncio;
    private ImageView imagemAnuncio;
    private List<Channel> canais = new ArrayList<Channel>();
    private int indexCanal = 0;

    private List<Radio> radios = new ArrayList<Radio>();

    private ConnectivityManager connManager;
    private NetworkInfo mWifi;
    private NetworkInfo mMobile;
    private String urlStreaming = "";


    private Table dbTable;
    private AmazonDynamoDBClient dbClient;
    private CognitoCachingCredentialsProvider  credentialsProvider;

    private Button btnLike, btnVerAoVivo;
    private String nameMusic;

    //FFmpegMediaPlayer mp = null;
    private MediaPlayer mp2 = null;


    private Boolean firstTime = true;

    //ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private static ArrayList<String> modulos = new ArrayList<String>();

    private Boolean firstTimeAds = true;

    private static MobileAnalyticsManager analytics;

    private String[] primeiraLetraEmail;

    Programs programas;
    List<Program> programasDia = new ArrayList<Program>();
    Hosts hosts;
    private AdView adView;
    final Handler handler = new Handler();
    Runnable runnable;

    private String JSON_STRING;
    private String JSON_URL;
    private String linkHLS;

    private FirebaseAnalytics mFirebaseAnalytics;


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String programa = intent.getExtras().getString("nomePrograma");
            String locutor = intent.getExtras().getString("nomeLocutor");
            setTextProgramacao(programa, locutor);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());

        /*
        if (FirebaseApp.getApps(this).size() != 1){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(FirebaseApp.initializeApp(this, new FirebaseOptions.Builder().setApplicationId(getResources().getString(R.string.google_app_id)).build(), "[DEFAULT]").getApplicationContext());
        }
        */
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        registerReceiver(broadcastReceiver, new IntentFilter("setTextProgramacao"));

        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        /*
        MyApplication application = (MyApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        Log.i(TAG, "Setting screen name: " + "Tela Inicial");
        mTracker.setScreenName("Image~" + "Tela Inicial");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        */

        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        try {
            analytics = MobileAnalyticsManager.getOrCreateInstance(
                    this.getApplicationContext(),
                    "78901fb9b7ae4dc98c6b4ba0dd54e409", //Amazon Mobile Analytics App ID
                    "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17" //Amazon Cognito Identity Pool ID
            );
        } catch (InitializationException ex) {
            Log.e(this.getClass().getName(), "Failed to initialize Amazon Mobile Analytics", ex);
        }

        final CacheData cacheData = new CacheData(getApplicationContext());

        // RECUPERANDO LISTA DE CANAIS DA SPLASH ACTIVITY
        Bundle extras = getIntent().getExtras();
        canais = (List<Channel>) extras.getSerializable("canais");
        radios = (List<Radio>) extras.getSerializable("radios");

        // INICIANDO COGNITO
        initCognito();



        // Create a new credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17", Regions.US_EAST_1);

        // Create a connection to DynamoDB
        dbClient = new AmazonDynamoDBClient(credentialsProvider);


        btnLike = findViewById(R.id.likeButton);
        btnVerAoVivo = findViewById(R.id.button2);

        linkHLS = cacheData.getString("linkHLS");
        //linkHLS = "https://www.youtube.com/watch?v=Ye29C9Xm-AQ";

        if (linkHLS.equals("")){
            btnVerAoVivo.setVisibility(View.INVISIBLE);
        }

        btnVerAoVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseStreaming();
                //String linkHLS = "https://video-dev.github.io/streams/x36xhzz/x36xhzz.m3u8";
                if (linkHLS.contains("youtube")){
                    Intent intent = new Intent(MainActivity.this, PlayerYoutube.class);
                    intent.putExtra("url", linkHLS);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, PlayerHLS.class);
                    intent.putExtra("url", linkHLS);
                    startActivity(intent);
                }
            }
        });


        ///////////////////////
        ///     Toolbar     ///
        ///////////////////////

        if (Build.VERSION.SDK_INT > 23) {
            float[] hsv = new float[3];
            int color = Color.parseColor(cacheData.getString("color"));
            Color.colorToHSV(color, hsv);
            hsv[2] *= 0.8f;
            color = Color.HSVToColor(hsv);

            Window window = mActivity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Início");

        mToolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Click", "Botão Compartilhar.");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Baixe já o aplicativo Rádio controle da sua rádio favorita:\nVersão Android: " + cacheData.getString("linkAndroid") + "\nVersão iOS: " + cacheData.getString("linkIos"));
                sendIntent.setType("text/plain");
                Intent shareI = Intent.createChooser(sendIntent, "Compartilhar no:");
                shareI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(shareI);
            }
        });


        //////////////////////////////
        ///     FragmentDrawer     ///
        //////////////////////////////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, mToolbar);
        //drawerFragment.setDrawerListener(this);
        displayView("inicio");


        ////////////////////////////////////
        ///     SlidingUpPanelLayout     ///
        ////////////////////////////////////

        dragView = (RelativeLayout) findViewById(R.id.dragView);
        dragView.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        painel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        painel.getChildAt(1).setOnClickListener(null);
        painel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                //Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        painel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                painel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        //////////////////////
        ///     Player     ///
        //////////////////////

        imgBtnPlayPause = (ImageButton) findViewById(R.id.imgBtnPlayPause);
        playPauseToolbar = (ImageView) findViewById(R.id.playPauseToolbar);
        albumCoverToolbar = (ImageView) findViewById(R.id.albumCoverToolbar);
        //Picasso.with(getApplicationContext())
        //        .load(cacheData.getString("logo"))
        //        .error(R.drawable.radio_controle)
        //        .into(albumCoverToolbar);
        imgCapaAlbum = (ImageView) findViewById(R.id.imgCapaAlbum);
        Picasso.with(getApplicationContext())
                .load(cacheData.getString("logo"))
                .error(R.drawable.radio_controle)
                .into(imgCapaAlbum);
        nomeMusica = (TextView) findViewById(R.id.nomeMusica);
        nomeMusica.setText("Ouvindo " + cacheData.getString("nomeRadio"));
        //Log.d( "aaaaaaaaa", cacheData.getString("rds"));

        nomeMusica.setTextColor(Color.WHITE);
        txtNomeMusica = (TextView) findViewById(R.id.txtNomeMusica);
        nomeArtista = (TextView) findViewById(R.id.nomeArtista);
        txtNomeCantor = (TextView) findViewById(R.id.txtNomeCantor);

        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


        txtHoraInicioPrograma = (TextView) findViewById(R.id.txtHoraInicioPrograma);
        txtNomePrograma = (TextView) findViewById(R.id.txtNomePrograma);
        txtNomeRadialista = (TextView) findViewById(R.id.txtNomeRadialista);
        txtHoraFimPrograma = (TextView) findViewById(R.id.txtHoraFimPrograma);

        JSON_URL = cacheData.getString("rds");
        //JSON_URL = "https://radiocontrole.s3.amazonaws.com/radios/3504-radio-hertz-am/app/RDS.XML";
        callLoop();

//        Ampiri.setDebugMode(true);
//        Ampiri.setTestMode(true);

        areaAnuncio = (LinearLayout) findViewById(R.id.areaAnuncio);
        //imagemAnuncio = (ImageView) findViewById(R.id.imagemAnuncio);
        adView = findViewById(R.id.imagemAnuncio);


        //imagemAnuncio.setVisibility(View.VISIBLE);


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                //Toast.makeText(mContext, "FOI", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //Toast.makeText(mContext, "FAIL " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                //.makeText(mContext, "ABRIU", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                //Toast.makeText(mContext, "Fechou", Toast.LENGTH_SHORT).show();
            }
        });



        AnalyticsEvent playerEvent = analytics.getEventClient().createEvent("Player");
        if (!cacheData.getString("userEmail").equals("")) {
            primeiraLetraEmail = cacheData.getString("userEmail").split("");
            playerEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
            playerEvent.addAttribute("Logged", "True");
            playerEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        } else {
            playerEvent.addAttribute("Logged", "False");
            playerEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        }
        analytics.getEventClient().recordEvent(playerEvent);
        AnalyticsEvent homeEvent = analytics.getEventClient().createEvent("Home");
        if (!cacheData.getString("userEmail").equals("")) {
            primeiraLetraEmail = cacheData.getString("userEmail").split("");
            homeEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
            homeEvent.addAttribute("Logged", "True");
            homeEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        } else {
            homeEvent.addAttribute("Logged", "False");
            homeEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        }
        analytics.getEventClient().recordEvent(homeEvent);

        //////////////////////////////
        ///     Texto Rotativo     ///
        //////////////////////////////
        txtRotativo = (TextView) this.findViewById(R.id.txtRotativo);
        makeEllipsized(txtRotativo, cacheData.getString("anuncio"));

        getProgramacao(false);


        final RelativeLayout relativeLayoutPlayer = (RelativeLayout) findViewById(R.id.relativeLayoutPlayer);
        relativeLayoutPlayer.setBackgroundColor(getDarkColor());

        final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(Color.parseColor(cacheData.getString("color")));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(getDarkColor());
        tabLayout.addTab(tabLayout.newTab().setText("AO VIVO"));
        tabLayout.addTab(tabLayout.newTab().setText("EVENTOS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        final MenuAdapter adapter = new MenuAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    viewPager.getLayoutParams().height = 0;
                    viewPager.requestLayout();
                    relativeLayout.requestLayout();
                } else {
                    viewPager.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    viewPager.requestLayout();
                    relativeLayout.requestLayout();
                    CanaisFragment fragmentCanais = (CanaisFragment) adapter.getItem(1);

                    List<String> strings = new ArrayList<String>();

                    for (int i = 0; i < canais.size(); i++) {
                        strings.add(canais.get(i).getName());
                    }

                    ArrayAdapter<String> canaisA = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            android.R.id.text1,
                            strings
                    );

                    fragmentCanais.createList(canaisA);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.getLayoutParams().height = 0;
        viewPager.requestLayout();
        relativeLayout.requestLayout();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombaritem_first:
                                switchFragment(TAG_FRAGMENT_HOME);
                                return true;
                            case R.id.bottombaritem_second:
                                switchFragment(TAG_FRAGMENT_NEWS);
                                return true;
                            case R.id.bottombaritem_third:
                                switchFragment(TAG_FRAGMENT_PROGRAMS);
                                return true;
                            case R.id.bottombaritem_fourth:
                                switchFragment("podcasts");
                                return true;
                            case R.id.bottombaritem_fifth:
                                switchFragment(TAG_FRAGMENT_PLUS);
                                return true;
                        }
                        return false;
                    }
                });

        bottomNavigationView.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        //buildFragmentsList();
        //switchFragment(TAG_FRAGMENT_CALLS);


        RelativeLayout player = findViewById(R.id.playerToolbar);
        player.setBackgroundColor(getDarkColor());

        AlertDialog.Builder alerta = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        LayoutInflater inflater = getLayoutInflater();
        final View viewAlerta = inflater.inflate(R.layout.dialog_loading, null);
        alerta.setView(viewAlerta);
        alerta.setCancelable(false);
        alertDialog = alerta.create();

        if (!audioTocando) {
            changeIcon("play", indexCanal);
        }

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long timestampLong = System.currentTimeMillis()/1000;
                String timestamp = timestampLong.toString();

                Document newMemo = new Document();
                if (cacheData.getString("idRadio") != null && nameMusic !=null) {
                    newMemo.put("radioId", cacheData.getString("idRadio"));
                    //newMemo.put("name", cacheData.getString("userNome"));
                    newMemo.put("nameMusic", txtNomePrograma.getText().toString());
                    newMemo.put("timestamp", timestamp);

                    CreateItemAsyncTask task = new CreateItemAsyncTask();
                    task.execute(newMemo);
                    btnLike.setBackgroundResource(R.mipmap.like);
                }
            }
        });
    }


    private static final String TAG_FRAGMENT_HOME = "inicio";
    private static final String TAG_FRAGMENT_NEWS = "noticias";
    private static final String TAG_FRAGMENT_PROGRAMS = "programas";
    private static final String TAG_FRAGMENT_WALL = "mural";
    private static final String TAG_FRAGMENT_PLUS = "menu";


    private void switchFragment(String tag) {
        displayView(tag);

    }

    private BottomNavigationView bottomNavigationView;

    int getDarkColor() {
        float[] hsv = new float[3];
        final CacheData cacheData = new CacheData(getApplicationContext());
        int color = Color.parseColor(cacheData.getString("color"));
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        color = Color.HSVToColor(hsv);
        return color;
    }


    private void makeEllipsized(final TextView textView, final String marqueeText) {
        textView.post(new Runnable() {
            public void run() {

                boolean check = false;
                String text = marqueeText;
                textView.setText(text);
                do {
                    final Layout layout = textView.getLayout();
                    if (layout != null) {
                        for (int index = 0; index < layout.getLineCount(); ++index) {
                            check = layout.getEllipsisCount(index) > 0;
                            if (check) {
                                break;
                            } else {
                                text = text + " ";
                            }
                        }
                        textView.setText(text);
                    }
                } while (!check);

                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSelected(true);
                textView.setSingleLine(true);
            }
        });
    }

    private void initCognito() {
        CognitoClientManager.init(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (bannerAd != null){
//            bannerAd.onActivityResumed();
//        }
//        if(analytics != null) {
//            analytics.getSessionClient().resumeSession();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (bannerAd != null){
//            bannerAd.onActivityPaused();
//        }
//        if(analytics != null) {
//            analytics.getSessionClient().pauseSession();
//            analytics.getEventClient().submitEvents();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (bannerAd != null){
//            bannerAd.onActivityDestroyed();
//        }
//        unregisterReceiver(broadcastReceiver);
//    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (painel.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
            painel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            // Get the fragment fragment manager - and pop the backstack
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                System.out.println("AAAAAAAAAAAAAA" + getSupportFragmentManager().getBackStackEntryCount());
                getSupportFragmentManager().popBackStack();
            }


        } else if (mp2 != null) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }

    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        System.out.println("BBBBBBB" + requestCode);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    ////////////////////////////
    ///     Menu Toolbar     ///
    ////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        pToolbar = (Toolbar) findViewById(R.id.playerTopToolbar);
        pToolbar.setTitle("Player");
        ImageButton botaoDormir = findViewById(R.id.buttonDormir);
        botaoDormir.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_alarm));
        //pToolbar.inflateMenu(R.menu.player_top_menu);
        pToolbar.setNavigationIcon(R.drawable.ic_arrow_down_white);
        CacheData cacheData = new CacheData(getApplicationContext());
        pToolbar.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
        setSupportActionBar(pToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                painel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        });

        botaoDormir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EscolherDialogFragment dialogListDormir = new EscolherDialogFragment("Selecione o horário para dormir:", "dormir", null);
                dialogListDormir.show(getSupportFragmentManager(), "abrirTelaDesejada");
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////
    ///     Menu Principal Click     ///
    ////////////////////////////////////
    @Override
    public void onDrawerItemSelected(View view, int position) {
        System.out.println(view.getTag().toString());
        displayView(view.getTag().toString());

    }

    public void displayView(String tela) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String tag = null;
        final CacheData cacheData = new CacheData(mContext);
        switch (tela) {
            case "inicio":
                fragment = new HomeFragment();
                title = "Início";
                tag = "inicio";
                break;
            case "noticias":
                fragment = new NoticiasFragment();
                title = "Notícias";
                tag = "noticias";
                break;
            case "mural":
                fragment = new MuralFragment();
                title = "Mural";
                tag = "mural";
                AnalyticsEvent wallEvent = analytics.getEventClient().createEvent("Wall");
                if (!cacheData.getString("userEmail").equals("")) {
                    primeiraLetraEmail = cacheData.getString("userEmail").split("");
                    wallEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
                    wallEvent.addAttribute("Logged", "True");
                    wallEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
                    wallEvent.addAttribute("Logged", "False");
                } else {
                }
                analytics.getEventClient().recordEvent(wallEvent);
                break;
            case "programas":
                fragment = new ProgramacaoFragment();
                title = "Programação";
                tag = "programacao";

                break;
//            case "menu":
//                Log.d("aaaa22", String.valueOf(cacheData.getInt("numRadios")));
//                SubMenuFragment subMenuFragment;
//                if (cacheData.getInt("numRadios") == 1) {
//                    subMenuFragment = new SubMenuFragment(false);
//                } else {
//                    subMenuFragment = new SubMenuFragment(true);
//                }
//                subMenuFragment.mainActivity = this;
//                fragment = subMenuFragment;
//
//                title = "Menu";
//                tag = "menu";
//                break;
            case "perfil":
                System.out.println("sashauisha" + cacheData.getString("userId"));
                if (cacheData.getString("userId").equals("")) {
                    System.out.println("Usuário deslogado!!!");
                    SuaContaDialogFragment dialog = new SuaContaDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("tela", "perfil");
                    dialog.setArguments(bundle);
                    dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    //dialog.show(this.getSupportFragmentManager(), "dialog");
                    ft.add(dialog, "fragment_dialog");
                    ft.commit();
                } else {
                    AnalyticsEvent profileEvent = analytics.getEventClient().createEvent("Profile");
                    if (!cacheData.getString("userEmail").equals("")) {
                        primeiraLetraEmail = cacheData.getString("userEmail").split("");
                        profileEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
                        profileEvent.addAttribute("Logged", "True");
                        profileEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
                    } else {
                        profileEvent.addAttribute("Logged", "False");
                        profileEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
                    }
                    analytics.getEventClient().recordEvent(profileEvent);
                    System.out.println("Usuário logado!!!");
                    fragment = new PerfilFragment();
                    title = "Perfil";
                    tag = "perfil";
                }
                break;
            case "podcasts":
                PodcastsFragment podcastss = new PodcastsFragment();
                podcastss.mainActivity = this;
                fragment = podcastss;
                title = "Tupi + / Podcasts";
                tag = "podcasts";
                break;
            case "promocoes":
                fragment = new PromocoesFragment();
                title = "Promoções";
                tag = "promocoes";
                break;
            case "estacoes":
                Intent intent = new Intent(MainActivity.this, RadioGroupActivity.class);
                intent.putExtra("canais", (Serializable) canais);
                intent.putExtra("radios", (Serializable) radios);

//                pauseStreaming();

                /*if (mp != null) {
                    //mp.release();
                    mp = null;
                }
                */

                stopPlaying();

                cacheData.putString("idRadio", "");
                startActivity(intent);
                finishAffinity();
                break;
            case "sobre o aplicativo":

                fragment = new SobreDialogFragment();
                title = "Sobre";
                tag = "sobre";
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, tag);

            if (!tela.equals("inicio")) {
                fragmentTransaction.addToBackStack(tag);
            }
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            if (tela.equals("inicio") && audioTocando) {
                homeFragment = (HomeFragment) fragment;
                //homeFragment.changeIcon("pause");
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(title);

            if (title == "Início") {
                ImageButton button = toolbar.getRootView().findViewById(R.id.buttonShare);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.v("Click", "Botão Compartilhar.");
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);

                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Baixe já o aplicativo Rádio controle da sua rádio favorita:\nVersão Android: " + cacheData.getString("linkAndroid") + "\nVersão iOS: " + cacheData.getString("linkIos"));


                        sendIntent.setType("text/plain");
                        Intent tr = Intent.createChooser(sendIntent, "Compartilhar no:");
                        tr.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(tr);

                    }
                });
            } else {
                ImageButton button = toolbar.getRootView().findViewById(R.id.buttonShare);
                button.setVisibility(View.INVISIBLE);
            }
        }

    }

    ///////////////////////
    ///     Métodos     ///
    ///////////////////////

    // ABRIR SLIDE UP
    public void abrirPainel(View view) {
        //Log.v("view", "Player toolbar");
        painel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        CacheData cacheData = new CacheData(mContext);
        AnalyticsEvent playerEvent = analytics.getEventClient().createEvent("Player");
        if (!cacheData.getString("userEmail").equals("")) {
            primeiraLetraEmail = cacheData.getString("userEmail").split("");
            playerEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
            playerEvent.addAttribute("Logged", "True");
            playerEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        } else {
            playerEvent.addAttribute("Logged", "False");
            playerEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        }
        analytics.getEventClient().recordEvent(playerEvent);
    }

    public void pauseStreaming() {
        if (audioTocando) {
            playPauseStreaming(null);
        }
    }

    // PLAY PAUSE STREAMING
    public void playPauseStreaming(View view) {
        //Log.v("aaa", ""+ view.getId());
        /*if (fragmentAdapter != null) {
            if (fragmentAdapter.adapter != null) {
                fragmentAdapter.adapter.pauseAllPodcasts();
            }
        }*/
        if (mp2 == null) {
            if (!audioTocando) {
                changeIcon("play", indexCanal);
            } else {
                changeIcon("pause", indexCanal);
            }
        }else {
            if (!mp2.isPlaying()) {
                changeIcon("play", indexCanal);
            } else {
                changeIcon("pause", indexCanal);
            }
        }
    }


    // ALTERAR ÍCONE PLAY PAUSE
    public void changeIcon(final String acao, int index) {
        //homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("inicio");
        if (acao.equals("pause")) {
            audioTocando = false;

            stopPlaying();

            //albumCoverToolbar.setImageResource(R.drawable.album_cover);
            //imgCapaAlbum.setImageResource(R.drawable.album_cover);
            nomeMusica.setText("");
            txtNomeMusica.setText("");
            nomeArtista.setText("");
            txtNomeCantor.setText("");
            imgBtnPlayPause.setImageResource(R.drawable.ic_play_white);

            playPauseToolbar.setImageResource(R.drawable.ic_play_circle_filled_white);
            //if (homeFragment != null)
            //homeFragment.changeIcon("play");
        } else if (acao.equals("play")) {
            Log.d("AAAACANAL", String.valueOf(index));

            stopPlaying();
            if (index == 0) {
                if (mWifi.isAvailable()) {
                    urlStreaming = canais.get(0).getHighStreams().get(0);
                } else {
                    Toast.makeText(mContext, "Você precisa estar conectado a internet para utilizar o aplicativo.", Toast.LENGTH_SHORT).show();
                }
            } else {
                indexCanal = index;
                if (mWifi.isAvailable()) {
                    if (canais.get(0).getHighStreams().size() > 0) {
                        urlStreaming = canais.get(indexCanal).getHighStreams().get(0);
                    } else {
                        urlStreaming = canais.get(indexCanal).getLowStreams().get(0);
                    }
                } else if (mMobile.isAvailable()) {
                    if (canais.get(0).getHighStreams().size() > 0) {
                        urlStreaming = canais.get(indexCanal).getLowStreams().get(0);
                    } else {
                        urlStreaming = canais.get(indexCanal).getHighStreams().get(0);
                    }
                } else {
                    Toast.makeText(mContext, "Você precisa estar conectado a internet para utilizar o aplicativo.", Toast.LENGTH_SHORT).show();
                }
            }

            //alertDialog.show();
            //progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setMessage("Carregando Áudio");
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressDialog.show();
            Uri myUri = Uri.parse(urlStreaming);
            Log.e("Log", myUri.toString());


            Intent serviceIntent = new Intent(this, PlayerService.class);
            serviceIntent.putExtra("uri", urlStreaming);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }



            audioTocando = true;
            imgBtnPlayPause.setImageResource(R.mipmap.stop);
            playPauseToolbar.setImageResource(R.mipmap.pause);

        } else if (acao.equals("propaganda")) {
            firstTimeAds = false;
            CacheData cacheData = new CacheData(mContext);
            //progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setMessage("Carregando Áudio");
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //progressDialog.show();
            Uri myUri = Uri.parse(cacheData.getString("adsPlayValue"));


            mp2 = new MediaPlayer();
            try {
                mp2.setDataSource(String.valueOf(myUri));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    AudioAttributes attr = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build();
                    mp2.setAudioAttributes(attr);
                } else {
                    mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                }
                mp2.prepare();
                mp2.start();
                imgBtnPlayPause.setImageResource(R.drawable.ic_stop_white);
                playPauseToolbar.setImageResource(R.drawable.ic_pause_circle_filled);
            } catch (IOException e) {
                Log.e("Log", e.toString());
            }

            mp2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    changeIcon("play", indexCanal);
                }
            });

            mp2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    MainActivity.this.mp2.start();
                    //progressDialog.dismiss();
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void stopPlaying() {
        stopService(new Intent(this, PlayerService.class));
        if (mp2 != null) {
            if (mp2.isPlaying()) {
                mp2.stop();
            }
            mp2.release();
            mp2 = null;
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e("Log", error.toString());
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        audioTocando = true;
        imgBtnPlayPause.setImageResource(R.mipmap.stop);
        playPauseToolbar.setImageResource(R.mipmap.pause);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.e(getPackageName(), String.format("Error(%s%s)", i, i1));
        mp2.reset();
        return false;
    }

    public void onPrepared() {
        Log.d(TAG, "Stream is prepared");
        if (mp2 == null) {
            mp2.start();
        } else {
            //mp.start();
        }
        //progressDialog.dismiss();
        alertDialog.dismiss();
        firstTime = false;
        audioTocando = true;
        nomeMusica.setText("");
        txtNomeMusica.setText("");
        nomeArtista.setText("");
        txtNomeCantor.setText("");
        imgBtnPlayPause.setImageResource(R.drawable.ic_stop_white);
        playPauseToolbar.setImageResource(R.drawable.ic_pause_circle_filled);
        setProgramacao();
    }

 /*   public boolean onError(FFmpegMediaPlayer mp, int what, int extra) {
        StringBuilder sb = new StringBuilder();
        sb.append("Media Player Error: ");
        switch (what) {
            case FFmpegMediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                sb.append("Not Valid for Progressive Playback");
                break;
            case FFmpegMediaPlayer.MEDIA_ERROR_SERVER_DIED:
                sb.append("Server Died");
                break;
            case FFmpegMediaPlayer.MEDIA_ERROR_UNKNOWN:
                sb.append("Unknown");
                break;
            case 0:
                //progressDialog.dismiss();
                alertDialog.dismiss();
                break;
            default:
                sb.append(" Non standard (");
                sb.append(what);
                sb.append(")");
        }
        sb.append(" (" + what + ") ");
        sb.append(extra);
        Log.e(TAG, sb.toString());
        return true;
    }
*/


    // FECHAR FRAGMENTO PERFIL
    public void voltarTelaInicial() {
        Fragment fragment = null;
        fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, "inicio");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        if (audioTocando) {
            //homeFragment.changeIcon("pause");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Início");
    }

    // ABRIR FRAGMENTO PERFIL
    public void abrirPerfil() {
        CacheData cacheData = new CacheData(mContext);
        AnalyticsEvent profileEvent = analytics.getEventClient().createEvent("Profile");
        if (!cacheData.getString("userEmail").equals("")) {
            primeiraLetraEmail = cacheData.getString("userEmail").split("");
            profileEvent.addAttribute("Email" + primeiraLetraEmail[0].toLowerCase(), cacheData.getString("userEmail"));
            profileEvent.addAttribute("Logged", "True");
            profileEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        } else {
            profileEvent.addAttribute("Logged", "False");
            profileEvent.addAttribute("RadioId", cacheData.getString("idRadio"));
        }
        analytics.getEventClient().recordEvent(profileEvent);


        Fragment fragment = null;
        fragment = new PerfilFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, "perfil");
        fragmentTransaction.addToBackStack("perfil");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public void trocarTituloToolbar(String titulo) {
        mToolbar.setTitle(titulo);
    }

    public void getProgramacao(final Boolean setProgramacao) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                CacheData cacheData = new CacheData(MainActivity.this);
                programas = client.radioidProgramsGet(cacheData.getString("idRadio"));
                hosts = client.radioidHostsGet(cacheData.getString("idRadio"));
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

            }
        }.execute();
    }

    public void setTextProgramacao(String programa, String locutor) {
        if (mp2 != null) {
            if (mp2.isPlaying()) {
                txtHoraInicioPrograma.setText("");
                txtNomePrograma.setText("");
                txtHoraFimPrograma.setText("");
                txtNomeRadialista.setText("");
                txtNomePrograma.setText(programa);
                txtNomeRadialista.setText(locutor);
            }
        }
    }

    public void setProgramacao() {
        txtHoraInicioPrograma.setText("");
        txtNomePrograma.setText("");
        txtHoraFimPrograma.setText("");
        txtNomeRadialista.setText("");

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        programasDia.clear();
        for (Program program : programas) {
            if (program.getMon() && dayOfWeek == 0) {
                programasDia.add(program);
            } else if (program.getTue() && dayOfWeek == 1) {
                programasDia.add(program);
            } else if (program.getWed() && dayOfWeek == 2) {
                programasDia.add(program);
            } else if (program.getThu() && dayOfWeek == 3) {
                programasDia.add(program);
            } else if (program.getFri() && dayOfWeek == 4) {
                programasDia.add(program);
            } else if (program.getSat() && dayOfWeek == 5) {
                programasDia.add(program);
            } else if (program.getSun() && dayOfWeek == 6) {
                programasDia.add(program);
            }
        }

        if (programasDia.size() > 0) {
            Collections.sort(programasDia, new Comparator<Program>() {
                @Override
                public int compare(final Program object1, final Program object2) {
                    return object1.getStartTime().compareTo(object2.getStartTime());
                }
            });
            Boolean test = false;
            for (int i = 0; i < programasDia.size(); i++) {
                String startTime = programasDia.get(i).getStartTime();
                String[] startTimeSplit = startTime.split(":");
                int horaPrograma = Integer.parseInt(startTimeSplit[0]);
                int minutoPrograma = Integer.parseInt(startTimeSplit[1]);
                if (horaPrograma == hora) {
                    txtNomePrograma.setText(programasDia.get(i).getName());
                    for (Host host : hosts) {
                        if (host.getId().equals(programasDia.get(i).getHostId())) {
                            txtNomeRadialista.setText(host.getName());
                            break;
                        }
                    }
                } else if (horaPrograma < hora && i > 0) {
                    String startTimeAnterior = programasDia.get(i).getStartTime();
                    String[] startTimeSplitAnterior = startTimeAnterior.split(":");
                    int horaProgramaAnterior = Integer.parseInt(startTimeSplitAnterior[0]);
                    if (horaProgramaAnterior < hora) {
                        txtNomePrograma.setText(programasDia.get(i).getName());
                        for (Host host : hosts) {
                            if (host.getId().equals(programasDia.get(i).getHostId())) {
                                txtNomeRadialista.setText(host.getName());
                                break;
                            }
                        }
                    }
                }
                if (horaPrograma > hora) {
                    AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
                    ProgramacaoPlayerReceiver programacaoPlayerReceiver = new ProgramacaoPlayerReceiver();
                    Intent intent = new Intent(MainActivity.this, ProgramacaoPlayerReceiver.class);
                    intent.putExtra("nomePrograma", programasDia.get(i).getName());
                    for (Host host : hosts) {
                        if (host.getId().equals(programasDia.get(i).getHostId())) {
                            intent.putExtra("nomeLocutor", host.getName());
                            break;
                        }
                    }
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, i);
                    Calendar nextProgram = Calendar.getInstance();
                    nextProgram.set(Calendar.HOUR_OF_DAY, horaPrograma);
                    nextProgram.set(Calendar.MINUTE, minutoPrograma);
                    nextProgram.set(Calendar.SECOND, 0);

                    long millis = nextProgram.getTimeInMillis();
                    Log.i("aaa", millis + "");
                    Log.i("bbb", System.currentTimeMillis() + "");
                    Log.i("bbb", millis - System.currentTimeMillis() + "");
                    long plusMillis = millis - System.currentTimeMillis();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + plusMillis, pendingIntent);

                }
            }
        }
    }

    private void callLoop() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method

                if (!(JSON_URL.contains("xml") || JSON_URL.contains("XML") || JSON_URL.contains("Xml") )) {
                    new BackgroundTask().execute();
                }else {
                    try {
                        //URL url = new URL("https://radiocontrole.s3.amazonaws.com/radios/3506-radio-hertz-fm/app/RDS.XML");
                        URL url = new URL(JSON_URL);
                        URLConnection conn = url.openConnection();

                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        org.w3c.dom.Document doc = builder.parse(conn.getInputStream());

                        NodeList nodes = doc.getElementsByTagName("musica");
                        for (int i = 0; i < nodes.getLength(); i++) {
                            Element element = (Element) nodes.item(i);
                            NodeList title = element.getElementsByTagName("titulo");
                            Element line = (Element) title.item(0);
                            Log.d("AAAA4", line.getTextContent());
                            nameMusic = "Música: " + line.getTextContent();
                        }
                        //String musicaNext = "Próxima: " + obj.getJSONObject("musica_next").getString("titulo");

                        btnLike.setBackgroundResource(R.mipmap.likeborder);
                        txtNomePrograma.setText(nameMusic);
                        nomeArtista.setText(nameMusic);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //txtNomePrograma.setText(musicaNext);
                                //nomeArtista.setText(musicaNext);
                                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                                if (networkInfo == null) {
                                    txtNomePrograma.setText("Sem conexão com a internet!");
                                    nomeArtista.setText("Sem conexão com a internet!");
                                }
                            }
                        }, 20000);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                if (networkInfo == null) {
                    txtNomePrograma.setText("Sem conexão com a internet!");
                    nomeArtista.setText("Sem conexão com a internet!");
                }
            }
        }, 0, 30000);//put here time 1000 milliseconds=1 second
    }

    private class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                StringBuilder JSON_DATA = new StringBuilder();
                URL url = new URL(JSON_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while ((JSON_STRING = reader.readLine()) != null) {
                    JSON_DATA.append(JSON_STRING).append("\n");
                    //Log.d("aaaa2", JSON_STRING);
                }
                return JSON_DATA.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                readMusics(result);
            }
        }
    }

    private void readMusics(String json) {
        try {

            JSONObject obj = new JSONObject(json);
            String musicaAtual = "Música: " + obj.getJSONObject("musica_play").getString("titulo");
            String musicaNext = "Próxima: " + obj.getJSONObject("musica_next").getString("titulo");

            Log.d("aaaaaaaAtual", obj.getJSONObject("musica_play").getString("titulo"));
            Log.d("aaaaaaaNext", obj.getJSONObject("musica_next").getString("titulo"));
            nameMusic = musicaAtual;
            btnLike.setBackgroundResource(R.mipmap.likeborder);
            txtNomePrograma.setText(musicaAtual);
            nomeArtista.setText(musicaAtual);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    txtNomePrograma.setText(musicaNext);
                    nomeArtista.setText(musicaNext);
                    ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                    if (networkInfo == null) {
                        txtNomePrograma.setText("Sem conexão com a internet!");
                        nomeArtista.setText("Sem conexão com a internet!");
                    }
                }
            }, 20000);


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
    }

    public void create(Document memo) {
        dbTable.putItem(memo);
    }

    private class CreateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            // Create a table reference
            dbTable = Table.loadTable(dbClient, "rc_favorite_songs");
            create(documents[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(mContext, "Música favoritada!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            //
        }
        if (analytics != null) {
            analytics.getSessionClient().pauseSession();
            analytics.getEventClient().submitEvents();
        }
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            //
        }
        if (adView != null) {
            adView.destroy();
        }
        stopService(new Intent(this, PlayerService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (analytics != null) {
            analytics.getSessionClient().resumeSession();
        }
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

}