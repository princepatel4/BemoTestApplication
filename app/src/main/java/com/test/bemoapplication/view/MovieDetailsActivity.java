package com.test.bemoapplication.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.test.bemoapplication.R;
import com.test.bemoapplication.model.chat.UserDetails;
import com.test.bemoapplication.utils.APIHandler;
import com.test.bemoapplication.utils.AppPrefs;
import com.test.bemoapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView imageViewBanner, imageViewPoster;
    TextView textViewMovieTitle, textViewDescription, textViewMovieName, textViewMovieOverView, textViewTotalRatings;
    FloatingActionButton fabChat;
    ProgressDialog progressDialog;

    DatabaseReference mFireBaseDatabase;
    AppPrefs appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setTitle(getString(R.string.activity_movies_details));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPrefs = new AppPrefs(MovieDetailsActivity.this);
        try {
            mFireBaseDatabase = Utils.initiateFireBase(MovieDetailsActivity.this, "");
        }catch (Exception e){
            System.out.println("firebase exception "+e.getMessage());
        }
        setUI();
        int a = getIntent().getIntExtra("movieID",0);
        if(a > 0){
            getMovieDetails(""+getIntent().getIntExtra("movieID",0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement




        if(id == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void setUI()
    {
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        fabChat = (FloatingActionButton) findViewById(R.id.fab_chat);
        imageViewBanner = (ImageView) findViewById(R.id.image_movie_banner);
        imageViewPoster = (ImageView) findViewById(R.id.image_movie_poster);

        textViewMovieTitle = (TextView) findViewById(R.id.text_title);
        textViewDescription = (TextView) findViewById(R.id.text_overview);
        textViewMovieName = (TextView) findViewById(R.id.text_movie_name);
        textViewMovieOverView = (TextView) findViewById(R.id.text_movie_overview);
        textViewTotalRatings = (TextView) findViewById(R.id.text_total_rating);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(appPrefs.getUserID().equalsIgnoreCase("")) {
                    showUserDetails();
                }else {
                    Intent intent = new Intent(MovieDetailsActivity.this, ChatConversationActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void showUserDetails()
    {



        final Dialog dialog = new Dialog(MovieDetailsActivity.this);
        dialog.setContentView(R.layout.popup_user_details);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout((6 * width)/7, height);


        // set the custom dialog components - text, image and button
        final EditText editTextNickName = (EditText) dialog.findViewById(R.id.edit_nick_name);

        Button dialogSubmit = (Button) dialog.findViewById(R.id.button_submit);
        // if button is clicked, close the custom dialog
        dialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mFireBaseDatabase.child("users").orderByChild("nickName").equalTo(editTextNickName.getText().toString().trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                appPrefs.setUserID(snapshot.getKey());
                                appPrefs.setUserName(snapshot.child("nickName").getValue().toString());
                                mFireBaseDatabase.child("users/" + snapshot.getKey()).setValue(new UserDetails(editTextNickName.getText().toString(), refreshedToken));

                            }
                            Intent intent = new Intent(MovieDetailsActivity.this, ChatConversationActivity.class);
                            startActivity(intent);
                        } else {
                            //Username Does Not Exist
                            // TODO: handle the case where the data does not yet exist
                            try {
                                String mGroupId = mFireBaseDatabase.push().getKey();

                                mFireBaseDatabase.child("users/" + mGroupId).setValue(new UserDetails(editTextNickName.getText().toString(), refreshedToken));
                                Toast.makeText(MovieDetailsActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();

                                appPrefs.setUserID(mGroupId);
                                appPrefs.setUserName(editTextNickName.getText().toString());

                                Intent intent = new Intent(MovieDetailsActivity.this, ChatConversationActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            System.out.println("Not Exist");
                        }

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
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
                    Picasso.with(MovieDetailsActivity.this)
                            .load(APIHandler.restAPI.imageUrl+response.getString("poster_path")).placeholder(R.drawable.im_place_holder)
                            .into(imageViewPoster);

                    textViewMovieOverView.setText(response.getString("overview"));
                    textViewMovieName.setText(response.getString("original_title"));
                    textViewMovieTitle.setText(response.getString("original_title"));
                    textViewDescription.setText(response.getString("overview"));
                    textViewTotalRatings.setText(response.getString("vote_average")+"/10 ("+ response.getString("vote_count") +")");

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
