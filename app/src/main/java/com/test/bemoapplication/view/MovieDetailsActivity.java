package com.test.bemoapplication.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;
import com.test.bemoapplication.R;
import com.test.bemoapplication.controller.MovieListAdapter;
import com.test.bemoapplication.model.movielist.MovieResult;
import com.test.bemoapplication.utils.APIHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView imageViewBanner;
    TextView textViewMovieTitle, textViewDescription;
    FloatingActionButton fabChat;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        setUI();
        int a = getIntent().getIntExtra("movieID",0);
        if(a > 0){
            getMovieDetails(""+getIntent().getIntExtra("movieID",0));
        }
    }
    private void setUI()
    {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        fabChat = (FloatingActionButton) findViewById(R.id.fab_chat);
        imageViewBanner = (ImageView) findViewById(R.id.image_movie_banner);
        textViewMovieTitle = (TextView) findViewById(R.id.text_title);
        textViewDescription = (TextView) findViewById(R.id.text_overview);



        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getMovieDetails(String movieID){

        progressDialog = new ProgressDialog(MovieDetailsActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        if(!progressDialog.isShowing()) {
            progressDialog.show();
        }

        APIHandler.getsharedInstance(MovieDetailsActivity.this).execute(Request.Method.GET, APIHandler.restAPI.getMovieDetailsAPI(movieID), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {


                    Picasso.with(MovieDetailsActivity.this)
                            .load(APIHandler.restAPI.imageUrl+response.getString("backdrop_path")).placeholder(R.drawable.im_place_holder)
                            .into(imageViewBanner);
                    textViewMovieTitle.setText(response.getString("original_title"));
                    textViewDescription.setText(response.getString("overview"));
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
