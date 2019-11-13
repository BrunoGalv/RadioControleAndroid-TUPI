package br.com.devmaker.rbn.fragment;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import br.com.devmaker.rbn.adapter.SubMenuAdapter;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.MainActivity;

/**
 * Created by leodegeus on 20/02/2018.
 */

public class SubMenuFragment extends Fragment {

    public MainActivity mainActivity;
    private boolean aBoolean;

    public SubMenuFragment() {
    }

    public SubMenuFragment(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub_menu, container, false);

        String myStrings[], myStrings2[];
        myStrings = new String[] { "Mural", "Promoções", "Rádios", "Perfil", "Sobre" };
        myStrings2 = new String[] { "Mural", "Promoções", "Perfil", "Sobre" };

        Integer[] imgid={
                R.mipmap.ic_mural,
                R.drawable.ic_action_ticket,
                R.drawable.ic_radio,
                R.drawable.ic_person,
                R.drawable.ic_info,
        },
        imgid2={
                R.mipmap.ic_mural,
                R.drawable.ic_action_ticket,
                R.drawable.ic_person,
                R.drawable.ic_info,
        };

        SubMenuAdapter adapter;
        if (aBoolean) {
            adapter = new SubMenuAdapter(mainActivity, myStrings, imgid);
        }else {
            adapter = new SubMenuAdapter(mainActivity, myStrings2, imgid2);
        }

        ListView listView = rootView.findViewById(R.id.listViewTeste);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tag = "";
                if (aBoolean) {
                    switch (i) {
                        case 0:
                            tag = "mural";
                            break;
                        case 1:
                            tag = "promocoes";
                            break;
                        case 2:
                            tag = "estacoes";
                            break;
                        case 3:
                            tag = "perfil";
                            break;
                        case 4:
                            tag = "sobre o aplicativo";
                            break;

                    }
                }else {
                    switch (i) {
                        case 0:
                            tag = "mural";
                            break;
                        case 1:
                            tag = "promocoes";
                            break;
                        case 2:
                            tag = "perfil";
                            break;
                        case 3:
                            tag = "sobre o aplicativo";
                            break;
                    }
                }
                mainActivity.displayView(tag);
            }
        });


        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
