package br.com.devmaker.rbn.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import br.com.devmaker.rbn.R;

public class PlayerYoutube extends AppCompatActivity {

    private Intent intent;
    private String idVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player_youtube);

        intent = getIntent();
        idVideo = intent.getStringExtra("url");

        WebView displayYoutubeVideo = findViewById(R.id.playerYoutube);

        Log.d("AAAAA", idVideo);
        idVideo = idVideo.substring(32);
        Log.d("AAAAA", idVideo);

        String frameVideo = "<html><body style=\"margin:0px;padding:0px;overflow:hidden\">  <iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+ idVideo +"\" frameborder=\"0\" autoplay; allowfullscreen></iframe></body></html>";


        displayYoutubeVideo.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = displayYoutubeVideo.getSettings();
        webSettings.setJavaScriptEnabled(true);
        displayYoutubeVideo.loadData(frameVideo, "text/html", "utf-8");

    }
}
