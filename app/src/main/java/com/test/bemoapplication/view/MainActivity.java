package com.test.bemoapplication.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.test.bemoapplication.R;
import com.test.bemoapplication.controller.MovieListAdapter;
import com.test.bemoapplication.model.movielist.MovieResult;
import com.test.bemoapplication.model.movielist.RequestResult;
import com.test.bemoapplication.utils.APIHandler;
import com.test.bemoapplication.utils.RecyclerViewPositionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewMoviesList;
    int lastVisiblesItems, visibleItemCount, totalItemCount;
    RecyclerView.LayoutManager mLayoutManager;
    int page = 1;
    RecyclerViewPositionHelper mRecyclerViewHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
    }

    private void setUI(){
        recyclerViewMoviesList = ( RecyclerView) findViewById(R.id.recycler_movies_list);

        recyclerViewMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                /*if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = arrayListTransactionList.size()-1;
                    lastVisiblesItems = mRecyclerViewHelper.findLastVisibleItemPosition();
                    System.out.println("last "+lastVisiblesItems + "total "+totalItemCount);
                    if (hasMore)
                    {

                        if(lastVisiblesItems==(totalItemCount)) {
                            if (isLoading == false) {
                                isLoading = true;
                                getTransactionData(arrayListTransactionList.get(arrayListTransactionList.size() - 1).getId());
                            }
                        }
                    }
                }*/

            }
        });

        getMovieList();
    }

    private void getMovieList(){
        APIHandler.getsharedInstance(MainActivity.this).execute(Request.Method.GET, "https://api.themoviedb.org/3/movie/now_playing?api_key=6906bb8eb453d3271e9304c440f9c1f8&language=en-US&page="+page, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Gson mGson = new GsonBuilder().create();
                    JSONArray jsonArray = response.getJSONArray("results");
                    List<MovieResult> arrayListMovieList = new ArrayList<MovieResult>();
                    for(int i = 0 ; i <jsonArray.length() ; i ++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MovieResult bean = mGson.fromJson(jsonObject.toString(), MovieResult.class);
                        arrayListMovieList.add(bean);
                    }
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewMoviesList.setLayoutManager(mLayoutManager);
                    recyclerViewMoviesList.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerViewMoviesList);
                    MovieListAdapter mAdapter = new MovieListAdapter(MainActivity.this, arrayListMovieList);
                    recyclerViewMoviesList.setAdapter(mAdapter);
                    System.out.println("done");
                }catch (JSONException e){
                    System.out.println("ex "+e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, "");
    }
}
