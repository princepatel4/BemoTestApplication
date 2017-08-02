package com.test.bemoapplication.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.test.bemoapplication.utils.Constants;
import com.test.bemoapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView imageViewBanner;
    TextView textViewMovieTitle, textViewDescription;
    FloatingActionButton fabChat;
    ProgressDialog progressDialog;

    DatabaseReference mFireBaseDatabase;
    AppPrefs appPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
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
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width)/7, (4 * height)/5);


        // set the custom dialog components - text, image and button
        final EditText editTextNickName = (EditText) dialog.findViewById(R.id.edit_nick_name);

        Button dialogSubmit = (Button) dialog.findViewById(R.id.button_submit);
        // if button is clicked, close the custom dialog
        dialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mGroupId = mFireBaseDatabase.push().getKey();
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                mFireBaseDatabase.child("users/" + mGroupId).setValue(new UserDetails(editTextNickName.getText().toString(), refreshedToken));
                Toast.makeText(MovieDetailsActivity.this, "Successfully registered.", Toast.LENGTH_SHORT).show();

                appPrefs.setUserID(mGroupId);
                appPrefs.setUserName(editTextNickName.getText().toString());

                Intent intent = new Intent(MovieDetailsActivity.this, ChatConversationActivity.class);
                startActivity(intent);

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
