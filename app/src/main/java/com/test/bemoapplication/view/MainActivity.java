package com.test.bemoapplication.view;

import android.app.ProgressDialog;
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
    Gson mGson = new GsonBuilder().create();
    List<MovieResult> arrayListMovieList = new ArrayList<MovieResult>();
    int totalPageCount = 1;
    int currentPage = 1;
    MovieListAdapter mAdapter;
    boolean isLoading = false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
    }

    private void setUI(){
        recyclerViewMoviesList = ( RecyclerView) findViewById(R.id.recycler_movies_list);


        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMoviesList.setLayoutManager(mLayoutManager);
        recyclerViewMoviesList.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerViewMoviesList);

        recyclerViewMoviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = arrayListMovieList.size()-1;
                    lastVisiblesItems = mRecyclerViewHelper.findLastVisibleItemPosition();
                    System.out.println("last "+lastVisiblesItems + "total "+totalItemCount);
                    if (currentPage<totalPageCount)
                    {

                        if(lastVisiblesItems==(totalItemCount)) {
                            if (isLoading == false) {
                                isLoading = true;
                                currentPage = currentPage + 1;
                                getMovieList();
                            }
                        }
                    }
                }
            }
        });

        getMovieList();
    }

    private void getMovieList(){
        isLoading = true;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        APIHandler.getsharedInstance(MainActivity.this).execute(Request.Method.GET, APIHandler.restAPI.moviesList+currentPage, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("results");
                    totalPageCount = response.getInt("total_pages");
                    for(int i = 0 ; i <jsonArray.length() ; i ++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        MovieResult bean = mGson.fromJson(jsonObject.toString(), MovieResult.class);
                        arrayListMovieList.add(bean);
                    }

                    if(mAdapter == null)
                    {
                        mAdapter = new MovieListAdapter(MainActivity.this, arrayListMovieList);
                        recyclerViewMoviesList.setAdapter(mAdapter);
                    }else{
                        mAdapter.updateList(arrayListMovieList);
                    }

                    System.out.println("done");
                    isLoading = false;
                }catch (JSONException e){
                    System.out.println("ex "+e);
                }finally {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
        }, "");
    }
}
