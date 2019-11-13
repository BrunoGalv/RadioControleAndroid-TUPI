package br.com.devmaker.rbn.adapter;

/**
 * Created by leodegeus on 20/02/2018.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.devmaker.rbn.R;

public class SubMenuAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public SubMenuAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.menu_row, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.menu_row, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textMenu);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iconImage);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };
}