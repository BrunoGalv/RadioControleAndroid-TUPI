package br.com.devmaker.rbn.adapter;

import br.com.devmaker.rbn.model.NoticiaFeed;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Des. Android on 16/06/2017.
 */

public interface NoticiasRetrofitAdapter {
    /*@GET("/dynamo/rss2.xml")
    Call<NoticiaFeed> getItems();*/
    @GET
    Call<NoticiaFeed> getItems(@Url String url);
}
