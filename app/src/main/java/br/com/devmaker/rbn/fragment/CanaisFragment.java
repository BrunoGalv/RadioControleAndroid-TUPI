package br.com.devmaker.rbn.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.MainActivity;
import br.com.devmaker.rbn.util.CacheData;

/**
 * Created by leodegeus on 30/01/2018.
 */

public class CanaisFragment extends Fragment  {

    public ArrayAdapter<String> canais;
    public ListView listaCanais;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_canais, container, false);
        listaCanais = v.findViewById(R.id.listViewCanais);
        listaCanais.setBackgroundColor(getColor());
        return v;
    }

    public void createList(ArrayAdapter<String> canaisInt) {
        canais = canaisInt;
        listaCanais.setAdapter(canaisInt);
        listaCanais.requestLayout();
        listaCanais.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getActivity()).changeIcon("play", i);
            }
        });

    }



    int getColor() {

        float[] hsv = new float[3];
        final CacheData cacheData = new CacheData(getActivity());
        int color = Color.parseColor(cacheData.getString("color"));
        return color;
    }
}