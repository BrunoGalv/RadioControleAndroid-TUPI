package br.com.devmaker.rbn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.PromocaoActivity;
import br.com.devmaker.rbn.model.Promocao;
import br.com.devmaker.rbn.util.CacheData;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PromocoesAdapter extends RecyclerView.Adapter<PromocoesAdapter.MyViewHolder> {

    List<Promocao> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public PromocoesAdapter(Context context, List<Promocao> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promocao2_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Promocao current = data.get(position);


        if (current.getImagemPromocao() != null && !current.getImagemPromocao().equals("")) {
            Picasso.with(context)
                    .load(current.getImagemPromocao())
                    .placeholder(R.drawable.picture)
                    .error(R.drawable.picture)
                    .into(holder.imagemPromocao);
        }
        if (current.getTituloPromocao() != null) {
            holder.tituloPromocao.setText(current.getTituloPromocao());
        } else {
            holder.tituloPromocao.setText("ERRO - TÍTULO");
        }

        if (current.getDataEncerramentoPromocao() != null) {
            holder.dataEncerramentoPromocao.setText(current.getDataEncerramentoPromocao());
        } else {
            holder.tituloPromocao.setText("ERRO - DATAE");
        }

        if (current.getDataSorteioPromocao() != null) {
            holder.dataSorteioPromocao.setText(current.getDataSorteioPromocao());
        } else {
            holder.tituloPromocao.setText("ERRO - DATAS");
        }

        final CacheData cacheData = new CacheData(context);


        holder.btnCompartilhar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Botão Compartilhar.");
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setAction(Intent.ACTION_SEND);

                sendIntent.putExtra(Intent.EXTRA_TEXT, "No dia " + current.getDataEncerramentoPromocao() + " encerrará a promoção " + current.getTituloPromocao() + ", venha participar comigo, baixe já o aplicativo Rádio controle:\nVersão Android: " + cacheData.getString("linkAndroid") +"\nVersão iOS: " + cacheData.getString("linkIos"));
                sendIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(sendIntent,"Compartilhar no:" ));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("Click", "Click Item");
                Intent intent = new Intent(context, PromocaoActivity.class);
                intent.putExtra("posicao", position);
                intent.putExtra("lista", (Serializable) data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  this.data == null ? 0 : this.data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagemPromocao;
        TextView tituloPromocao;
        TextView dataEncerramentoPromocao;
        TextView dataSorteioPromocao;
        TextView participando;
        ImageButton btnCompartilhar;

        public MyViewHolder(View itemView) {
            super(itemView);

            imagemPromocao = (ImageView) itemView.findViewById(R.id.imagemPromocao);
            tituloPromocao = (TextView) itemView.findViewById(R.id.tituloPromocao);
            dataEncerramentoPromocao = (TextView) itemView.findViewById(R.id.dataEncerramentoPromocao);
            dataSorteioPromocao = (TextView) itemView.findViewById(R.id.dataSorteioPromocao);
            participando = (TextView) itemView.findViewById(R.id.participando);
            btnCompartilhar = (ImageButton) itemView.findViewById(R.id.btnCompartilhar);
        }
    }

}

