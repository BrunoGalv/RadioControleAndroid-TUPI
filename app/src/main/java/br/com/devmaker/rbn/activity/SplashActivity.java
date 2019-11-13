package br.com.devmaker.rbn.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;


import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
//import com.ampiri.sdk.Ampiri;
//import com.ampiri.sdk.interstitial.InterstitialAd;
//import com.ampiri.sdk.interstitial.InterstitialAdPool;
//import com.ampiri.sdk.listeners.InterstitialAdCallback;
//import com.ampiri.sdk.mediation.ResponseStatus;
//import com.ampiri.sdk.mediation.admob.AdMobMediation;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.model.Advert;
import br.com.devmaker.rbn.model.Channel;
import br.com.devmaker.rbn.model.Group;
import br.com.devmaker.rbn.model.Radio;
import br.com.devmaker.rbn.model.Settings;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.CognitoClientManager;
import br.com.devmaker.rbn.util.RadiocontroleClient;

/**
 * Created by Des. Android on 05/07/2017.
 */

public class SplashActivity extends Activity {

    private Context mContext;
    private Activity mActivity;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    private ArrayList<String> modulos = new ArrayList<String>();
    private List<Channel> canais = new ArrayList<Channel>();
    private List<Radio> radios = new ArrayList<Radio>();

    private ImageView splash;
//    private InterstitialAd interstitialAd;

    private InterstitialAd mInterstitialAd;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("CREATION","TST");

        setContentView(R.layout.activity_splash);
        mContext = getApplicationContext();
        mActivity = SplashActivity.this;

        final CacheData cacheData = new CacheData(mContext);

        if(getIntent() != null && getIntent().getExtras() != null){
            radios = (List<Radio>) getIntent().getExtras().getSerializable("radios");
        }


        splash = (ImageView) findViewById(R.id.splash);
        if (!cacheData.getString("splash").equals("")){
            Picasso.with(mContext)
                    .load(cacheData.getString("splash"))
                    .into(splash);
        }

        if (!cacheData.getString("color").equals("")){
            if (Build.VERSION.SDK_INT > 23) {
                float[] hsv = new float[3];
                int color = Color.parseColor(cacheData.getString("color"));
                Color.colorToHSV(color, hsv);
                hsv[2] *= 0.8f;
                color = Color.HSVToColor(hsv);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(color);
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                Group group;
                Settings settings;

                if (!cacheData.getString("idRadio").equals("")){
                    settings = client.radioidGet(cacheData.getString("idRadio"));
                }else{
                    group = client.radiogroupRadioGroupIdGet("3371-centroOesteTerradasAguas");

                    for (Radio radio: group){
                        radios.add(radio);
                    }
                    settings = client.radioidGet(radios.get(0).getId());
                }

                canais = new ArrayList<>();
                for (Channel channel: settings.getChannels()){
                    canais.add(channel);
                }

                for (Advert advert : settings.getAdverts()){
                    switch (advert.getLocal()){
                        case "PlayerService":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsPlayerContent", "off");
                            } else if (advert.getType().equals("google")) {
                                cacheData.putString("adsPlayerContent", "google");
                                cacheData.putString("adsPlayerValue", advert.getValueAndroid());
                            } else if (advert.getType().equals("image")) {
                                cacheData.putString("adsPlayerContent", "image");
                                cacheData.putString("adsPlayerValue", advert.getValueAndroid());
                                cacheData.putString("adsPlayerLink", advert.getLink());
                            }
                            break;
                        case "home":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsHomeContent", "off");
                            } else if (advert.getType().equals("google")) {
                                cacheData.putString("adsHomeContent", "google");
                                cacheData.putString("adsHomeValue", advert.getValueAndroid());
                            } else if (advert.getType().equals("image")) {
                                cacheData.putString("adsHomeContent", "image");
                                cacheData.putString("adsHomeValue", advert.getValueAndroid());
                                cacheData.putString("adsHomeLink", advert.getLink());
                            }
                            break;
                        case "play":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsPlayContent", "off");
                            } else if (advert.getType().equals("audio")) {
                                cacheData.putString("adsPlayContent", "audio");
                                cacheData.putString("adsPlayValue", advert.getValueAndroid());
                            }
                            break;
                        case "wall":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsWallContent", "off");
                            } else if (advert.getType().equals("ampiri")) {
                                cacheData.putString("adsWallContent", "ampiri");
                                cacheData.putString("adsWallValue", advert.getValueAndroid());
                            }
                            break;
                        case "interstitial":
                            if (advert.getType().equals("off")){
                                cacheData.putString("adsInterstitialContent", "off");
                            } else if (advert.getType().equals("google")) {
                                cacheData.putString("adsInterstitialContent", "google");
                                cacheData.putString("adsInterstitialValue", advert.getValueAndroid());
                                System.out.println("InterstitialAdCallback: " + cacheData.getString("adsInterstitialContent"));
                            }
                            break;
                        default:
                            break;
                    }
                }
                if (cacheData.getString("adsHomeContent").equals("google")) {
                    MobileAds.initialize(getApplicationContext(), "ca-app-pub-3940256099942544~3347511713");
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                if (cacheData.getString("adsInterstitialContent").equals("google")) {

                    mInterstitialAd = new InterstitialAd(getApplicationContext());
                    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            System.out.println("InterstitialAdCallback: " + "onAdLoaded");
                            if (!cacheData.getString("splash").equals("")){
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mInterstitialAd.isLoaded()){
                                            mInterstitialAd.show();
                                        }
                                    }
                                }, SPLASH_TIME_OUT);
                            }else {
                                if (mInterstitialAd.isLoaded()){
                                    mInterstitialAd.show();
                                }
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onAdOpened() {
                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
                        }



                        @Override
                        public void onAdClosed() {
                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.putExtra("canais", (Serializable) canais);
                            intent.putExtra("radios", (Serializable) radios);
                            startActivity(intent);
                            finish();
                        }
                    });

//                    InterstitialAdCallback interstitialAdListener = new InterstitialAdCallback() {
//                        @Override
//                        public void onAdLoaded(@NonNull final InterstitialAd interstitialAd) {
//                            System.out.println("InterstitialAdCallback: " + "onAdLoaded");
//                            System.out.println("InterstitialAdCallback: " + "onAdLoaded");
//                            if (!cacheData.getString("splash").equals("")){
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (interstitialAd.isReady()){
//                                            interstitialAd.showAd();
//                                        }
//                                    }
//                                }, SPLASH_TIME_OUT);
//                            }else {
//                                if (interstitialAd.isReady()){
//                                    interstitialAd.showAd();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onAdFailed(@NonNull InterstitialAd interstitialAd, ResponseStatus responseStatus) {
//                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
//                            System.out.println("InterstitialAdCallback: " + "onAdFailed");
//                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                            intent.putExtra("canais", (Serializable) canais);
//                            intent.putExtra("radios", (Serializable) radios);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onAdOpened(@NonNull InterstitialAd interstitialAd) {
//                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
//                            System.out.println("InterstitialAdCallback: " + "onAdOpened");
//                        }
//
//                        @Override
//                        public void onAdClicked(@NonNull InterstitialAd interstitialAd) {
//                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
//                            System.out.println("InterstitialAdCallback: " + "onAdClicked");
//                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                            intent.putExtra("canais", (Serializable) canais);
//                            intent.putExtra("radios", (Serializable) radios);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onAdClosed(@NonNull InterstitialAd interstitialAd) {
//                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
//                            System.out.println("InterstitialAdCallback: " + "onAdClosed");
//                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                            intent.putExtra("canais", (Serializable) canais);
//                            intent.putExtra("radios", (Serializable) radios);
//                            startActivity(intent);
//                            finish();
//                        }
//                    };
//                    //interstitialAd = InterstitialAdPool.load( SplashActivity.this, "6b34bf0e-04c4-47f9-b4d3-caeddeab0b13", interstitialAdListener);
//                    interstitialAd = InterstitialAdPool.load( SplashActivity.this, cacheData.getString("adsInterstitialValue"), interstitialAdListener);
//
//                    Ampiri.addMediationAdapter(new AdMobMediation.Builder()
//                            //.addTestDevice("ca-app-pub-4694313893492524~8880658070")
//                            .addTestDevice("ca-app-pub-4694313893492524/4709656385")
//                            .build());
                }else {
                    if (!cacheData.getString("splash").equals("")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                intent.putExtra("canais", (Serializable) canais);
                                intent.putExtra("radios", (Serializable) radios);
                                startActivity(intent);
                                finish();
                            }
                        }, SPLASH_TIME_OUT);
                    }else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("canais", (Serializable) canais);
                        intent.putExtra("radios", (Serializable) radios);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        }.execute();

    }

}