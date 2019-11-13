package br.com.devmaker.rbn.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.devmaker.rbn.R;
import br.com.devmaker.rbn.adapter.NoticiasListAdapter;
import br.com.devmaker.rbn.dal.DatabaseHandler;
import br.com.devmaker.rbn.util.CacheData;
import br.com.devmaker.rbn.util.RecyclerItemClickListener;

import com.prof.rssparser.Article;
import com.prof.rssparser.Parser;


public class NoticiasFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticiasListAdapter adapter;
    //private SwipeRefreshLayout mSwipeRefreshLayout;

    private String RSS_LINK = ""; // http://g1.globo.com/dynamo/rss2.xml http://feeds.feedburner.com/TechCrunch/social

    private List<Article> cachedList = new ArrayList<Article>();

    private DatabaseHandler db;

    private static String TAG = NoticiasFragment.class.getSimpleName();

    public NoticiasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void requestNoticias2(String link) {
        String urlString = link;
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new Parser.OnTaskCompleted() {

            @Override
            public void onTaskCompleted(ArrayList<Article> list) {
                cachedList = list;
                Collections.sort(cachedList, new Comparator<Article>() {
                    @Override
                    public int compare(Article article, Article t1) {
                        return (article.getPubDate()).compareTo((t1.getPubDate()));
                    }
                });
                sendCachedList();
            }

            @Override
            public void onError() {
                //what to do in case of error
            }
        });
    }

//    private void requestNoticias(final Boolean desabilitarSwipe){
//
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
//                .build();
//
//        Uri rssLink = Uri.parse(RSS_LINK);
//        String path = rssLink.getPath();
//        String[] lastIndex = path.split("/");
//        Log.e("AAAAAAAA", "" + lastIndex[lastIndex.length-1]);
//        Log.e("BBBBBBBB", "" + RSS_LINK.split(lastIndex[lastIndex.length-1])[0]);
//
//
//        SimpleXmlConverterFactory conv = SimpleXmlConverterFactory.createNonStrict();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(RSS_LINK.split(lastIndex[lastIndex.length-1])[0])
//                .client(client)
//                .addConverterFactory(conv)
//                .build();
//
//        NoticiasRetrofitAdapter retrofitService = retrofit.create(NoticiasRetrofitAdapter.class);
//        Call<NoticiaFeed> call = retrofitService.getItems(lastIndex[lastIndex.length-1]);
//        call.enqueue(new Callback<NoticiaFeed>() {
//            @Override
//            public void onResponse(Call<NoticiaFeed> call, Response<NoticiaFeed> response) {
//                NoticiaFeed feed = response.body();
//                List<Noticia> mItems = feed.getChannel().getItemList();
//                if (cachedList.size() == 0 && mItems != null) {
//                    cachedList = new ArrayList<Noticia>(mItems);
//                    for ( int i = mItems.size() -1 ; i >= 0 ; i--) {
//                        Noticia item = mItems.get(i);
//                        db.addNoticia(item);
//                    }
//                    Log.d(TAG, "Initialized  cached list");
//                    sendCachedList();
//                } else if ( mItems != null ){
//                    boolean itemsUpdated = false;
//                    for ( int j = mItems.size() -1 ; j >= 0 ; j--) {
//                        Noticia item = mItems.get(j);
//                        boolean itemExists = false;
//                        for (Noticia i: cachedList) {
//                            if (db.isEqualTo(item)) {
//                                itemExists = true;
//                                break;
//                            }
//                        }
//                        if (!itemExists) {
//                            itemsUpdated = true;
//                            Log.d(TAG, "Found a new item " + item.getTituloNoticia());
//                            db.addNoticia(item);
//                            cachedList.add(0, item);
//                        }
//                    }
//
//                    if (itemsUpdated) {
//                        Log.d(TAG, "Finished updating cached list");
//                        //sendCachedList();
//                    } else {
//                        Log.d(TAG,"No updates to cache no need to send an update");
//                        //sendCachedList();
//                    }
//                    if (desabilitarSwipe){
//                        sendCachedList();
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    } else {
//                        sendCachedList();
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<NoticiaFeed> call, Throwable t) {
//                Log.d(TAG, "OnFailure Error is " + t);
//            }
//        });
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CacheData cacheData = new CacheData(getContext());

        RSS_LINK = cacheData.getString("rssLink");

        View rootView = inflater.inflate(R.layout.fragment_noticias, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        adapter = new NoticiasListAdapter(getContext(), cachedList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        int itemPosition = recyclerView.getChildLayoutPosition(view);
                        Article item = cachedList.get(itemPosition);
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                        //mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override public void onLongItemClick(View view, int position) {

                    }
                })
        );

        /*adapter = new NoticiasLis tAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);*/

        //items =  new ArrayList<Noticia>();

//        db = new DatabaseHandler(getActivity());
//        cachedList = db.getAllNoticias();
        //requestNoticias(true);
        requestNoticias2(RSS_LINK);
        /*List<Noticia> noticias = db.getAllNoticias();

        for (Noticia n : noticias) {
            String log = "Tempo Notícia: " + n.getTempoNoticia() + " ,Título Notícia: " + n.getTituloNoticia() + " ,Resumo Notícia: " + n.getResumoNoticia() + " ,Link Notícia: " + n.getLinkNoticia();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }*/

//        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
//        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                updateNoticias();
//            }
//        });

        return rootView;
    }

    private void sendCachedList() {
        if ( cachedList != null ) {
            adapter.clear();

            Collections.reverse(cachedList);
            adapter.addAll(cachedList);

            Log.d(TAG, "Sending cachedList");
        }
        else {
            Log.d(TAG, "Cached list is empty!");
        }
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface NoticiasFragmentListener {
        public void onDrawerItemSelected(View view, int position);
    }

    protected void updateNoticias(){
        requestNoticias2(RSS_LINK);
    }
}
