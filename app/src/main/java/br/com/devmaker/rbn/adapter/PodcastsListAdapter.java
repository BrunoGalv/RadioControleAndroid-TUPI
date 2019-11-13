package br.com.devmaker.rbn.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.MainActivity;
import br.com.devmaker.rbn.activity.PodcastPlayer;
import br.com.devmaker.rbn.model.PodcastApp;
import br.com.devmaker.rbn.util.CacheData;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastsListAdapter extends RecyclerView.Adapter<PodcastsListAdapter.MyViewHolder> {
    List<PodcastApp> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    RecyclerView mRecyclerView;

    public MainActivity mainActivity;


    public PodcastsListAdapter(Context context, List<PodcastApp> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.podcasts_row, parent, false);
        mRecyclerView = (RecyclerView) parent;
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final PodcastApp current = data.get(position);
        CacheData cacheData = new CacheData(context);
        holder.tituloPodcast.setText(current.getTituloPodcast());
        holder.dataPublicacaoPodcast.setText(hora(current.getDataPublicacaoPodcast(), current.getHora()));

        holder.podcast.setOnClickListener(view -> {
            mainActivity.pauseStreaming();
            Intent intent = new Intent(holder.podcast.getContext(), PodcastPlayer.class);
            intent.putExtra("nome", current.getTituloPodcast());

            intent.putExtra("data", hora(current.getDataPublicacaoPodcast(), current.getHora()));
            intent.putExtra("link",current.getLinkPodcast());

            if (current.getLinkPodcast2()!=null){
                intent.putExtra("data2", hora(current.getDataPublicacaoPodcast2(), current.getHora2()));
                intent.putExtra("link2",current.getLinkPodcast2());
            }
            if (current.getLinkPodcast3()!=null){
                intent.putExtra("data3", hora(current.getDataPublicacaoPodcast3(), current.getHora3()));
                intent.putExtra("link3",current.getLinkPodcast3());
            }
            if (current.getLinkPodcast4()!=null){
                intent.putExtra("data4", hora(current.getDataPublicacaoPodcast4(), current.getHora4()));
                intent.putExtra("link4",current.getLinkPodcast4());
            }
            if (current.getLinkPodcast5()!=null){
                intent.putExtra("data5", hora(current.getDataPublicacaoPodcast5(), current.getHora5()));
                intent.putExtra("link5",current.getLinkPodcast5());
            }
            if (current.getLinkPodcast6()!=null){
                intent.putExtra("data6", hora(current.getDataPublicacaoPodcast6(), current.getHora6()));
                intent.putExtra("link6",current.getLinkPodcast6());
            }
            if (current.getLinkPodcast7()!=null){
                intent.putExtra("data7", hora(current.getDataPublicacaoPodcast7(), current.getHora7()));
                intent.putExtra("link7",current.getLinkPodcast7());
            }
            if (current.getLinkPodcast8()!=null){
                intent.putExtra("data8", hora(current.getDataPublicacaoPodcast8(), current.getHora8()));
                intent.putExtra("link8",current.getLinkPodcast8());
            }
            if (current.getLinkPodcast9()!=null){
                intent.putExtra("data9", hora(current.getDataPublicacaoPodcast9(), current.getHora9()));
                intent.putExtra("link9",current.getLinkPodcast9());
            }
            if (current.getLinkPodcast10()!=null){
                intent.putExtra("data10", hora(current.getDataPublicacaoPodcast10(), current.getHora10()));
                intent.putExtra("link10",current.getLinkPodcast10());
            }
            holder.podcast.getContext().startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tituloPodcast, dataPublicacaoPodcast;
        LinearLayout podcast;

        public MyViewHolder(View itemView) {
            super(itemView);
            podcast = itemView.findViewById(R.id.linearLayoutPodcastRow);
            tituloPodcast = itemView.findViewById(R.id.tituloPodcast);
            dataPublicacaoPodcast = itemView.findViewById(R.id.dataPublicacaoPodcast11);
        }
    }

    public String hora(Date date, String hora){
        Date now = new Date();
        Date past = date;
        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
            if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) == 1) {
                return "Publicado " + TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " dia atrás, às "+ String.valueOf(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1))) + ":00" + ".";
            }else {
                return "Publicado " + TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " dias atrás, às "+ String.valueOf(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1))) + ":00" + ".";
            }
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0 ) {
            if ((TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) - Integer.parseInt(String.valueOf(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1))))  ) == 1){
                return "Publicado " + (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) - Integer.parseInt(String.valueOf(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1))))  ) + " hora atrás.";
            }else {
                return "Publicado " + (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) - Integer.parseInt(String.valueOf(String.valueOf(hora.charAt(0)) + String.valueOf(hora.charAt(1))))) + " horas atrás.";
            }
        } else if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime())> 0 ) {
            return "Publicado " + TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutos atrás.";
        } else {
            return "Publicado " + "agora!";
        }
    }
}
