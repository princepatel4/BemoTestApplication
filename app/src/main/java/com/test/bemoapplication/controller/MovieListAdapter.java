package com.test.bemoapplication.controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;
import com.test.bemoapplication.R;
import com.test.bemoapplication.model.movielist.MovieResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prince on 27-07-2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {
    List<MovieResult> arrayListMovieList = new ArrayList<>();
    Activity activity;
    View view;
    public MovieListAdapter(Activity activity, List<MovieResult> arrayListMovieList) {
        this.arrayListMovieList = arrayListMovieList;
        this.activity = activity;
    }

    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.MyViewHolder holder, final int position) {
        //holder.textViewStoreName.setText(arrayListSurvey.get(position).getName());


        holder.textViewTitle.setText("Survey No. "+arrayListMovieList.get(position).getTitle());
        holder.textViewOverView.setText("Taluka:- "+arrayListMovieList.get(position).getOverview());
        Picasso.with(activity)
                .load("https://image.tmdb.org/t/p/w500/"+arrayListMovieList.get(position).getBackdropPath())
                .into(holder.imageViewDelete);
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.linearLayoutSurveyDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListMovieList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutSurveyDetails;
        TextView textViewTitle, textViewOverView, textViewVillage;
        ImageView imageViewDelete;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            textViewOverView = (TextView) itemView.findViewById(R.id.text_overview);
            imageViewDelete = (ImageView) itemView.findViewById(R.id.image_movie_banner);


        }
    }



}
