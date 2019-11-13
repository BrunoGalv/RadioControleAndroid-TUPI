package br.com.devmaker.rbn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.model.Mural;
import br.com.devmaker.rbn.util.CacheData;

public class MuralListAdapter extends RecyclerView.Adapter<MuralListAdapter.MyViewHolder> implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    List<Mural> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private String timeAgo;
    private CacheData cacheData;


    private MediaPlayer mp2 = null;


    private int mediaFileLengthInMilliseconds;
    private final Handler handler = new Handler();
    private Boolean isPlaying = false;
    private Boolean firstTime = true;
    private int lastAudio;

    RecyclerView mRecyclerView;

    Runnable notification;

    public MuralListAdapter(Context context, List<Mural> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.mural_row, parent, false);
        mRecyclerView = (RecyclerView) parent;
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Mural current = data.get(position);

        // CORREÇÃO DATA
        DateFormat formatter =  new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date now = new Date();
        Date past = current.getTempoPublicacao();
        if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
            timeAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " dias atrás";
        } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0 ) {
            timeAgo = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " horas atrás";
        } else if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime())> 0 ) {
            timeAgo = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutos atrás";
        } else {
            timeAgo = "agora";
        }

        if (current.getModeloPublicacao().equals("texto")){
            holder.templateTexto.setVisibility(View.VISIBLE);
            holder.templateAudio.setVisibility(View.GONE);
            holder.templateImagem.setVisibility(View.GONE);
            if (!current.getFotoUsuario().equals("")) {
                Picasso.with(context)
                        .load(current.getFotoUsuario())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(holder.fotoUsuario1);
            }else {
                Picasso.with(context)
                        .load(R.drawable.user)
                        .into(holder.fotoUsuario1);
            }
            holder.nomeUsuario1.setText(current.getNomeUsuario());
            holder.tempoPublicacao1.setText(timeAgo);
            holder.textoPublicacao1.setText(current.getTextoPublicacao());
        } else if (current.getModeloPublicacao().equals("imagem")) {
            holder.templateImagem.setVisibility(View.VISIBLE);
            holder.templateAudio.setVisibility(View.GONE);
            holder.templateTexto.setVisibility(View.GONE);
            if (!current.getFotoUsuario().equals("")) {
                Picasso.with(context)
                        .load(current.getFotoUsuario())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(holder.fotoUsuario2);
            }else {
                Picasso.with(context)
                        .load(R.drawable.user)
                        .into(holder.fotoUsuario2);
            }
            holder.nomeUsuario2.setText(current.getNomeUsuario());
            holder.tempoPublicacao2.setText(timeAgo);
            holder.textoPublicacao2.setText(current.getTextoPublicacao());
            if (!current.getImagemPublicacao().equals("")) {
                Picasso.with(context)
                        .load(current.getImagemPublicacao())
                        .error(R.drawable.picture)
                        .into(holder.imagemPublicacao);
            }
        } else if (current.getModeloPublicacao().equals("audio")) {
            holder.templateAudio.setVisibility(View.VISIBLE);
            holder.templateTexto.setVisibility(View.GONE);
            holder.templateImagem.setVisibility(View.GONE);
            if (!current.getFotoUsuario().equals("")) {
                Picasso.with(context)
                        .load(current.getFotoUsuario())
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(holder.fotoUsuario3);
            }else {
                Picasso.with(context)
                        .load(R.drawable.user)
                        .into(holder.fotoUsuario3);
            }
            //holder.video_control.setBackgroundColor(Color.parseColor(cacheData.getString("color")));
            holder.nomeUsuario3.setText(current.getNomeUsuario());
            holder.tempoPublicacao3.setText(timeAgo);

        }

        holder.btnPlayPauseAudio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (!isPlaying){

                    // SE NENHUM PODCAST ESTIVER TOCANDO
                    try {

                        // SE FOR A PRIMEIRA VEZ QUE APERTA PLAY -> CRIA-SE UM NOVO PODCAST
                        if (firstTime){

                            lastAudio = position;
                            holder.btnPlayPauseAudio.setVisibility(View.INVISIBLE);
                            holder.loadingIcon.setVisibility(View.VISIBLE);

                            mp2 = new MediaPlayer();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                AudioAttributes attr = new AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build();
                                mp2.setAudioAttributes(attr);
                            } else {
                                mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            }

                            mp2.setDataSource(String.valueOf(Uri.parse(current.getAudioPublicacao())));
                            mp2.prepare();

                            mp2.setOnPreparedListener(mediaPlayer -> {
                                mediaFileLengthInMilliseconds = mp2.getDuration();
                                primarySeekBarProgressUpdater(holder, position);
                                mp2.start();
                                holder.loadingIcon.setVisibility(View.GONE);
                                holder.btnPlayPauseAudio.setVisibility(View.VISIBLE);
                                holder.btnPlayPauseAudio.setImageResource(R.drawable.ic_pause_white);
                                isPlaying = true;
                                firstTime = false;
                            });

                            mp2.setOnCompletionListener(mediaPlayer -> {
                                handler.removeCallbacks(notification);
                                // RESETAR ITENS DO PODCAST ANTERIOR
                                ImageButton btnItemAnterior = (ImageButton) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.btnPlayPauseAudio);
                                btnItemAnterior.setImageResource(R.drawable.ic_play_white);
                                TextView tempoItemAnterior = (TextView) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.tempoAudio);
                                int color = Color.parseColor(cacheData.getString("color"));
                                tempoItemAnterior.setTextColor(Color.RED);
                                tempoItemAnterior.setText("00:00");
                                holder.progressBarAudio.setProgress(0);
                                isPlaying = false;
                            });


                            // SE FOR A SEGUNDA OU MAIS VEZES QUE APERTA PLAY
                        }else {

                            // SE FOI NO MESMO PODCAST -> RESUME()
                            if (lastAudio == position){
                                int aux = mp2.getCurrentPosition();

                                Log.d("AAAA",String.valueOf(mediaFileLengthInMilliseconds).charAt(1)+"AAAARSM" + String.valueOf(aux));

                                if(String.valueOf(mediaFileLengthInMilliseconds).charAt(1) == String.valueOf(aux).charAt(1)){
                                    aux = 0;
                                }

                                primarySeekBarProgressUpdater(holder, position);
                                mp2.seekTo(aux);
                                mp2.start();
                                holder.btnPlayPauseAudio.setImageResource(R.drawable.ic_pause_white);
                                isPlaying = true;

                                Log.d("AAAA","AAAARSM");

                                //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                            }else {

                                // PARAR RUNNABLE
                                handler.removeCallbacks(notification);

                                // RESETAR ITENS DO PODCAST ANTERIOR
                                ImageButton btnItemAnterior = (ImageButton) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.btnPlayPauseAudio);
                                btnItemAnterior.setImageResource(R.drawable.ic_play_white);
                                TextView tempoItemAnterior = (TextView) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.tempoAudio);
                                int color = Color.parseColor(cacheData.getString("color"));
                                tempoItemAnterior.setText("00:00");

                                // INICIAR NOVO PLAYER DE PODCAST
                                mp2.stop();
                                lastAudio = position;
                                holder.btnPlayPauseAudio.setVisibility(View.INVISIBLE);
                                holder.loadingIcon.setVisibility(View.VISIBLE);


                                mp2 = new MediaPlayer();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    AudioAttributes attr = new AudioAttributes.Builder()
                                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                            .setUsage(AudioAttributes.USAGE_MEDIA)
                                            .build();
                                    mp2.setAudioAttributes(attr);
                                } else {
                                    mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                }

                                mp2.setDataSource(String.valueOf(Uri.parse(current.getAudioPublicacao())));
                                mp2.prepare();


                                mp2.setOnPreparedListener(mediaPlayer -> {
                                    mediaFileLengthInMilliseconds = mp2.getDuration();
                                    primarySeekBarProgressUpdater(holder, position);
                                    mp2.start();
                                    holder.loadingIcon.setVisibility(View.GONE);
                                    holder.btnPlayPauseAudio.setVisibility(View.VISIBLE);
                                    holder.btnPlayPauseAudio.setImageResource(R.drawable.ic_pause_white);
                                    isPlaying = true;
                                    firstTime = false;
                                });


                                mp2.setOnCompletionListener(mediaPlayer -> {
                                    handler.removeCallbacks(notification);

                                    // RESETAR ITENS DO PODCAST ANTERIOR
                                    ImageButton btnItemAnterior1 = (ImageButton) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.btnPlayPauseAudio);
                                    btnItemAnterior1.setImageResource(R.drawable.ic_play_white);
                                    TextView tempoItemAnterior1 = (TextView) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.tempoAudio);
                                    tempoItemAnterior1.setText("00:00");
                                    holder.progressBarAudio.setProgress(0);
                                    isPlaying = false;
                                });



                            }

                        }
                    } catch (IOException e) {
                        Log.v("Exception: ", e.getMessage());
                    }

                    // SE ALGUM PODCAST ESTIVER TOCANDO
                } else {
                    // SE FOI NO MESMO PODCAST -> PAUSE()
                    if (lastAudio == position){
                        mp2.pause();
                        Log.d("AAAA","AAAASTP");
                        holder.btnPlayPauseAudio.setImageResource(R.drawable.ic_play_white);
                        isPlaying = false;
                        //SE FOI EM OUTRO PODCAST -> DESTROI PLAYER E INICIA NOVO PODCAST
                    }else {
                        try {
                            // PARAR RUNNABLE
                            handler.removeCallbacks(notification);
                            // RESETAR ITENS DO PODCAST ANTERIOR
                            ImageButton btnItemAnterior = (ImageButton) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.btnPlayPauseAudio);
                            btnItemAnterior.setImageResource(R.drawable.ic_play_white);
                            TextView tempoItemAnterior = (TextView) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.tempoAudio);
                            tempoItemAnterior.setText("00:00");
                            // INICIAR NOVO PLAYER DE PODCAST
                            mp2.stop();
                            lastAudio = position;
                            holder.btnPlayPauseAudio.setVisibility(View.INVISIBLE);
                            holder.loadingIcon.setVisibility(View.VISIBLE);


                            mp2 = new MediaPlayer();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                AudioAttributes attr = new AudioAttributes.Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .setUsage(AudioAttributes.USAGE_MEDIA)
                                        .build();
                                mp2.setAudioAttributes(attr);
                            } else {
                                mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            }

                            mp2.setDataSource(String.valueOf(Uri.parse(current.getAudioPublicacao())));
                            mp2.prepare();

                            mp2.setOnPreparedListener(mediaPlayer -> {
                                mediaFileLengthInMilliseconds = mp2.getDuration();
                                primarySeekBarProgressUpdater(holder, position);
                                mp2.start();
                                holder.loadingIcon.setVisibility(View.GONE);
                                holder.btnPlayPauseAudio.setVisibility(View.VISIBLE);
                                holder.btnPlayPauseAudio.setImageResource(R.drawable.ic_pause_white);
                                isPlaying = true;
                                firstTime = false;
                            });

                            mp2.setOnCompletionListener(mediaPlayer -> {
                                handler.removeCallbacks(notification);

                                // RESETAR ITENS DO PODCAST ANTERIOR
                                ImageButton btnItemAnterior1 = (ImageButton) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.btnPlayPauseAudio);
                                btnItemAnterior1.setImageResource(R.drawable.ic_play_white);
                                TextView tempoItemAnterior1 = (TextView) mRecyclerView.getChildAt(lastAudio).findViewById(R.id.tempoAudio);
                                tempoItemAnterior1.setText("00:00");
                                holder.progressBarAudio.setProgress(0);
                            });
                        }catch (IOException e){
                            Log.v("Exception: ", e.getMessage());
                        }
                    }

                }

            }
        });

        // SEEKBAR CHANGE
        holder.progressBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int playPositionInMillisecconds;
            int startPosition;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * holder.progressBarAudio.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                startPosition = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (mp2 != null){
                    mp2.seekTo(playPositionInMillisecconds);
                }else {
                    holder.progressBarAudio.setProgress(startPosition);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAllOld(List<Mural> list, int quantidadeAnterior) {
        this.data.addAll(quantidadeAnterior, list);
        notifyItemRangeInserted(quantidadeAnterior - 1, list.size());
        //notifyDataSetChanged();
    }

    public void addAllNew(List<Mural> list) {
        this.data.clear();
        this.data.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout templateTexto;
        RelativeLayout templateImagem;
        RelativeLayout templateAudio;
        RelativeLayout video_control;

        ImageView fotoUsuario1;
        ImageView fotoUsuario2;
        ImageView fotoUsuario3;

        TextView nomeUsuario1;
        TextView nomeUsuario2;
        TextView nomeUsuario3;

        TextView tempoPublicacao1;
        TextView tempoPublicacao2;
        TextView tempoPublicacao3;

        TextView textoPublicacao1;
        TextView textoPublicacao2;

        ImageView imagemPublicacao;

        TextView audioPublicacao;

        ImageButton btnPlayPauseAudio;
        SeekBar progressBarAudio;
        ProgressBar loadingIcon;
        TextView tempoAudio;

        public MyViewHolder(View itemView) {
            super(itemView);

            cacheData = new CacheData(context);

            templateTexto = (RelativeLayout) itemView.findViewById(R.id.templateTexto);
            templateImagem = (RelativeLayout) itemView.findViewById(R.id.templateImagem);
            templateAudio = (RelativeLayout) itemView.findViewById(R.id.templateAudio);
            video_control = (RelativeLayout) itemView.findViewById(R.id.video_control);

            fotoUsuario1 = (ImageView) itemView.findViewById(R.id.fotoUsuario1);
            fotoUsuario2 = (ImageView) itemView.findViewById(R.id.fotoUsuario2);
            fotoUsuario3 = (ImageView) itemView.findViewById(R.id.fotoUsuario3);

            nomeUsuario1 = (TextView) itemView.findViewById(R.id.nomeUsuario1);
            nomeUsuario2 = (TextView) itemView.findViewById(R.id.nomeUsuario2);
            nomeUsuario3 = (TextView) itemView.findViewById(R.id.nomeUsuario3);

            tempoPublicacao1 = (TextView) itemView.findViewById(R.id.tempoPublicacao1);
            tempoPublicacao2 = (TextView) itemView.findViewById(R.id.tempoPublicacao2);
            tempoPublicacao3 = (TextView) itemView.findViewById(R.id.tempoPublicacao3);

            textoPublicacao1 = (TextView) itemView.findViewById(R.id.textoPublicacao1);
            textoPublicacao2 = (TextView) itemView.findViewById(R.id.textoPublicacao2);

            imagemPublicacao = (ImageView) itemView.findViewById(R.id.imagemPublicacao);

            audioPublicacao = (TextView) itemView.findViewById(R.id.audioPublicacao);

            btnPlayPauseAudio = (ImageButton) itemView.findViewById(R.id.btnPlayPauseAudio);
            progressBarAudio = (SeekBar) itemView.findViewById(R.id.progressBarAudio);
            loadingIcon = (ProgressBar) itemView.findViewById(R.id.loadingIcon);
            tempoAudio = (TextView) itemView.findViewById(R.id.tempoAudio);

            int color = Color.parseColor(cacheData.getString("color"));

            tempoAudio.setTextColor(color);
            progressBarAudio.getThumb().setColorFilter(color, PorterDuff.Mode.MULTIPLY);

            progressBarAudio.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void primarySeekBarProgressUpdater(final MyViewHolder holder, final int position) {
        if (mp2!=null) {
            holder.progressBarAudio.setProgress((int) (((float) mp2.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
            holder.tempoAudio.setText("" + milliSecondsToTimer((long) mp2.getCurrentPosition()));
            notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater(holder, position);
                }
            };
            handler.postDelayed(notification, 1000);
        }
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
}
