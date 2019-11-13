package br.com.devmaker.rbn.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.activity.MainActivity;
import br.com.devmaker.rbn.adapter.PodcastsListAdapter;
import br.com.devmaker.rbn.model.Podcast;
import br.com.devmaker.rbn.model.PodcastApp;
import br.com.devmaker.rbn.model.Podcasts;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.CognitoClientManager;
import br.com.devmaker.rbn.util.RadiocontroleClient;

/**
 * Created by Des. Android on 29/06/2017.
 */

public class PodcastsFragment extends Fragment {

    private RecyclerView recyclerView;
    public PodcastsListAdapter adapter;
    private List<PodcastApp> podcastApps = new ArrayList<>();
    private android.widget.SearchView searchView;
    private String stringSearch = "a";
    private List<PodcastApp> listSearch = new ArrayList<>();

    public MainActivity mainActivity;
    private String JSON_STRING;
    private String JSON_URL;

    public PodcastsFragment() {
        // Required empty public constructor
    }

    public static List<PodcastApp> getData() {
        List<PodcastApp> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            PodcastApp podcast = new PodcastApp();

            podcast.setTituloPodcast("PodcastApp título " + i);
            podcast.setLinkPodcast("http://e.mundopodcast.com.br/podprogramar/podprogramar-013-sistemas-controle-versao.mp3");
            podcast.setDataPublicacaoPodcast(new Date());

            data.add(podcast);

        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_podcasts, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        searchView = rootView.findViewById(R.id.action_search);
        searchView.setQueryHint("Buscar");
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //auxSearchView = true;
                if(newText != null && !newText.isEmpty()){
                    stringSearch = newText;
                    
                    listSearch.clear();
                    search();

                    adapter = new PodcastsListAdapter(getContext(), listSearch);
                    recyclerView.setAdapter(adapter);
                }else{
                    //if search text is null
                    //return default
                    adapter = new PodcastsListAdapter(getContext(), podcastApps);
                    recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ApiClientFactory factory = new ApiClientFactory();
                factory.credentialsProvider(CognitoClientManager.getCredentials());
                factory.apiKey("QgpKgwmkrA3ilAhtFbtW4abS5l9AHNP89Pe0WlrK");
                final RadiocontroleClient client = factory.build(RadiocontroleClient.class);
                CacheData cacheData = new CacheData(getContext());
                Podcasts podcasts = client.radioidPodcastsGet(cacheData.getString("idRadio"));
                JSON_URL = cacheData.getString("podcastXML");
                JSON_URL = "http://www2.tupi.am/_recursos/xml/AudiosOnDemand.aspx";
                if (!JSON_URL.equals("")){
                    chama();
                }

                for (Podcast podcast : podcasts){
                    PodcastApp podcastApp = new PodcastApp();
                    Timestamp timestamp = new Timestamp(podcast.getTimestamp());
                    Date dataPostagem = new Date(timestamp.getTime());

                    podcastApp.setTituloPodcast(podcast.getName());
                    podcastApp.setLinkPodcast("https://s3.amazonaws.com/radiocontrole/radios/" + cacheData.getString("idRadio") + "/podcasts/" + podcast.getFile());
                    podcastApp.setDataPublicacaoPodcast(dataPostagem);
                    podcastApps.add(podcastApp);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                    adapter = new PodcastsListAdapter(getContext(), podcastApps);

                    adapter.mainActivity = mainActivity;
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }
        }.execute();

        return rootView;
    }

    public void chama(){
        new BackgroundTask().execute();
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
                organizar(result);
            }
        }
    }

    private void organizar(String result){
        String result2[] = result.split("\n");

        for (int i = 3; i < result2.length - 2; i++){
            String result3[] = result2[i].split("\"");

            PodcastApp podcastApp = new PodcastApp();


            int x = 1;
            for (int y = i + 1; y > 0; y++) {
                x++;
                if (y < result2.length - 2) {
                    String aux[] = result2[y].split("\"");
                    if (aux[5].equals(result3[5])) {
                        i++;
                        switch (x){
                            case 2:
                                podcastApp.setLinkPodcast2(aux[7]);
                                podcastApp.setHora2(aux[3]);
                                podcastApp.setDataPublicacaoPodcast2(transformarData(aux[1]));
                                break;
                            case 3:
                                podcastApp.setLinkPodcast3(aux[7]);
                                podcastApp.setHora3(aux[3]);
                                podcastApp.setDataPublicacaoPodcast3(transformarData(aux[1]));
                                break;
                            case 4:
                                podcastApp.setLinkPodcast4(aux[7]);
                                podcastApp.setHora4(aux[3]);
                                podcastApp.setDataPublicacaoPodcast4(transformarData(aux[1]));
                                break;
                            case 5:
                                podcastApp.setLinkPodcast5(aux[7]);
                                podcastApp.setHora5(aux[3]);
                                podcastApp.setDataPublicacaoPodcast5(transformarData(aux[1]));
                                break;
                            case 6:
                                podcastApp.setLinkPodcast6(aux[7]);
                                podcastApp.setHora6(aux[3]);
                                podcastApp.setDataPublicacaoPodcast6(transformarData(aux[1]));
                                break;
                            case 7:
                                podcastApp.setLinkPodcast7(aux[7]);
                                podcastApp.setHora7(aux[3]);
                                podcastApp.setDataPublicacaoPodcast7(transformarData(aux[1]));
                                break;
                            case 8:
                                podcastApp.setLinkPodcast8(aux[7]);
                                podcastApp.setHora8(aux[3]);
                                podcastApp.setDataPublicacaoPodcast8(transformarData(aux[1]));
                                break;
                            case 9:
                                podcastApp.setLinkPodcast9(aux[7]);
                                podcastApp.setHora9(aux[3]);
                                podcastApp.setDataPublicacaoPodcast9(transformarData(aux[1]));
                                break;
                            case 10:
                                podcastApp.setLinkPodcast10(aux[7]);
                                podcastApp.setHora10(aux[3]);
                                podcastApp.setDataPublicacaoPodcast10(transformarData(aux[1]));
                                break;

                        }
                    } else {
                        y = -1;
                    }
                }else {
                    y = -1;
                }
            }


            podcastApp.setTituloPodcast(result3[5]);
            podcastApp.setLinkPodcast(result3[7]);
            podcastApp.setHora(result3[3]);
            podcastApp.setDataPublicacaoPodcast(transformarData(result3[1]));
            podcastApps.add(podcastApp);
        }

        adapter = new PodcastsListAdapter(getContext(), podcastApps);
        adapter.mainActivity = mainActivity;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void search(){

        for (int i = 0; i < podcastApps.size(); i++){
            if (podcastApps.get(i).getTituloPodcast().toLowerCase().contains(stringSearch.toLowerCase())){
                listSearch.add(podcastApps.get(i));
                adapter.notifyDataSetChanged();
            }
        }
    }

    public Date transformarData(String data){
        Date d = null;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            d = sdf.parse(data);
        }catch(ParseException ex){
        }
        return d;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Nullable
    @Override
    public Object getExitTransition() {
        Log.d("","");
        return super.getExitTransition();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

