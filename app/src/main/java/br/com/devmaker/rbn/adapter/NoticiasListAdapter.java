package br.com.devmaker.rbn.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prof.rssparser.Article;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import br.com.devmaker.rbn.R;;

public class NoticiasListAdapter extends RecyclerView.Adapter<NoticiasListAdapter.MyViewHolder> {
    List<Article> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    private String noHtml;
    private String timeAgo;


    public NoticiasListAdapter(Context context, List<Article> data) {
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
        View view = inflater.inflate(R.layout.noticias_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article current = data.get(position);
        // CORREÇÃO DATA
        DateFormat formatter =  new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        Date now = new Date();
        try {
            Date past = current.getPubDate();
            if (TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) > 0) {
                timeAgo = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " dias atrás";
            } else if (TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) > 0 ) {
                timeAgo = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " horas atrás";
            } else if (TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime())> 0 ) {
                timeAgo = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutos atrás";
            } else {
                timeAgo = "agora";
            }
        } catch (Error e) {
            Log.e("e",e.getStackTrace().toString());
        }

        holder.tempoNoticia.setText(timeAgo);

        holder.tituloNoticia.setText(current.getTitle());


        holder.resumoNoticia.setText("");

        holder.linkNoticia.setText(current.getLink());
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    public void clear() {
        if (this.data != null){
            this.data.clear();
            notifyDataSetChanged();
        }
    }

    public void addAll(List<Article> list) {
        this.data = list;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tempoNoticia;
        TextView tituloNoticia;
        TextView resumoNoticia;
        TextView linkNoticia;

        public MyViewHolder(View itemView) {
            super(itemView);
            tempoNoticia = (TextView) itemView.findViewById(R.id.txtTempoNoticia);
            tituloNoticia = (TextView) itemView.findViewById(R.id.txtTituloNoticia);
            resumoNoticia = (TextView) itemView.findViewById(R.id.txtResumoNoticia);
            linkNoticia = (TextView) itemView.findViewById(R.id.txtLinkNoticia);
        }
    }
}
