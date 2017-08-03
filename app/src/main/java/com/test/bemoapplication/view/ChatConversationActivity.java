package com.test.bemoapplication.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.test.bemoapplication.R;
import com.test.bemoapplication.controller.ChatConversationAdapter;
import com.test.bemoapplication.model.chat.ChatConversation;
import com.test.bemoapplication.model.chat.UserDetails;
import com.test.bemoapplication.utils.APIHandler;
import com.test.bemoapplication.utils.AppPrefs;
import com.test.bemoapplication.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatConversationActivity extends AppCompatActivity {

    EditText editTextMessage;
    ImageButton imageButtonSendMessage;
    RecyclerView recyclerViewMessage;
    AppPrefs appPrefs;
    List<ChatConversation>  listChatConversation = new ArrayList<>();
    DatabaseReference mFireBaseDatabase;
    String lastDiscussionKey = "";
    ChatConversationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);

        getSupportActionBar().setTitle(getString(R.string.activity_chat_conversation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appPrefs = new AppPrefs(ChatConversationActivity.this);
        try {
            mFireBaseDatabase = Utils.initiateFireBase(ChatConversationActivity.this, "");
        }catch (Exception e){
            System.out.println("firebase exception "+e.getMessage());
        }

        setUI();
        getUpdatedChat();
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
    private void setUI(){
        editTextMessage = (EditText) findViewById(R.id.messageEditText);
        imageButtonSendMessage = (ImageButton) findViewById(R.id.sendMessageButton);
        recyclerViewMessage = (RecyclerView) findViewById(R.id.msgListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatConversationActivity.this);
        recyclerViewMessage.setLayoutManager(layoutManager);
        imageButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editTextMessage.getText().toString().trim().equalsIgnoreCase("")) {
                    String mGroupId = mFireBaseDatabase.push().getKey();

                    ChatConversation chatConversation = new ChatConversation(mGroupId, appPrefs.getUserID(), appPrefs.getUserName(), editTextMessage.getText().toString(), "", "");
                    mFireBaseDatabase.child("discussion/" + mGroupId).setValue(chatConversation);
                    //Toast.makeText(ChatConversationActivity.this, "Successfully sent message.", Toast.LENGTH_SHORT).show();
                    broadcastMessage(editTextMessage.getText().toString());
                    editTextMessage.setText("");

                    listChatConversation.add(chatConversation);
                    if(adapter == null){
                        adapter = new ChatConversationAdapter(ChatConversationActivity.this, listChatConversation);
                        recyclerViewMessage.setAdapter(adapter);
                        recyclerViewMessage.smoothScrollToPosition(recyclerViewMessage.getAdapter().getItemCount());
                    }else{
                        adapter.updateList(listChatConversation);
                    }

                    lastDiscussionKey = mGroupId;
                    recyclerViewMessage.smoothScrollToPosition(recyclerViewMessage.getAdapter().getItemCount());

                    getUpdatedChat();
                }
            }
        });
    }

    private void getUpdatedChat()
    {

        mFireBaseDatabase.child("discussion").orderByKey().startAt(lastDiscussionKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                System.out.println("snapshot "+dataSnapshot.getChildren());
                System.out.println("snapshot "+s);

                if(dataSnapshot.getKey().equalsIgnoreCase(lastDiscussionKey))
                {return;}else {

                    ChatConversation chatConversation = new ChatConversation();
                    chatConversation.setMessageKey(dataSnapshot.child("messageKey").getValue().toString());
                    chatConversation.setUserName(dataSnapshot.child("userName").getValue().toString());
                    chatConversation.setUserKey(dataSnapshot.child("userKey").getValue().toString());
                    System.out.println("snapshot "+dataSnapshot.child("userKey").getValue().toString());
                    chatConversation.setChatMessage(dataSnapshot.child("chatMessage").getValue().toString());
                    chatConversation.setDeviceID(dataSnapshot.child("deviceID").getValue().toString());
                    chatConversation.setTimeStamp(dataSnapshot.child("timeStamp").getValue().toString());
                    if(listChatConversation.contains(chatConversation)){

                    }else {
                        listChatConversation.add(chatConversation);
                    }
                    listChatConversation.size();
                    if(adapter == null){
                        adapter = new ChatConversationAdapter(ChatConversationActivity.this, listChatConversation);
                        recyclerViewMessage.setAdapter(adapter);
                        recyclerViewMessage.smoothScrollToPosition(recyclerViewMessage.getAdapter().getItemCount());
                    }else{
                        adapter.updateList(listChatConversation);
                    }

                    lastDiscussionKey = dataSnapshot.getKey();
                    recyclerViewMessage.smoothScrollToPosition(recyclerViewMessage.getAdapter().getItemCount());
                }
                /*for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //ChatConversation chatConversation = new ChatConversation();
                    System.out.println("snapshot "+snapshot.child("chatMessage").getValue());
                    Toast.makeText(ChatConversationActivity.this, ""+snapshot.child("chatMessage").getValue(), Toast.LENGTH_SHORT).show();
                    //arra.add(snapshot.getValue().toString());
                }*/

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatConversation chatConversation = new ChatConversation();
                chatConversation.setMessageKey(dataSnapshot.child("messageKey").getValue().toString());
                chatConversation.setUserName(dataSnapshot.child("userName").getValue().toString());
                chatConversation.setUserKey(dataSnapshot.child("userKey").getValue().toString());
                System.out.println("snapshot "+dataSnapshot.child("userKey").getValue().toString());
                chatConversation.setChatMessage(dataSnapshot.child("chatMessage").getValue().toString());
                chatConversation.setDeviceID(dataSnapshot.child("deviceID").getValue().toString());
                chatConversation.setTimeStamp(dataSnapshot.child("timeStamp").getValue().toString());
                listChatConversation.remove(chatConversation);
                adapter.updateList(listChatConversation);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void broadcastMessage(String message){


        try {

            final JSONObject jObject = new JSONObject();
            jObject.put("message", message);
            jObject.put("nickName", appPrefs.getUserName());
            System.out.println("request Json "+ jObject);
            APIHandler.getsharedInstance(ChatConversationActivity.this).execute(Request.Method.POST, APIHandler.restAPI.sendMessage, jObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {



                }
            }, "");
        }catch (JSONException e){

        }
    }

}
