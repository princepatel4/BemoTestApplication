package com.test.bemoapplication.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.test.bemoapplication.R;
import com.test.bemoapplication.controller.ChatConversationAdapter;
import com.test.bemoapplication.model.chat.ChatConversation;
import com.test.bemoapplication.model.chat.UserDetails;
import com.test.bemoapplication.utils.AppPrefs;
import com.test.bemoapplication.utils.Utils;

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

        appPrefs = new AppPrefs(ChatConversationActivity.this);
        try {
            mFireBaseDatabase = Utils.initiateFireBase(ChatConversationActivity.this, "");
        }catch (Exception e){
            System.out.println("firebase exception "+e.getMessage());
        }

        setUI();
        getUpdatedChat();
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
                    Toast.makeText(ChatConversationActivity.this, "Successfully sent message.", Toast.LENGTH_SHORT).show();
                    editTextMessage.setText("");

                    listChatConversation.add(chatConversation);
                    adapter.updateList(listChatConversation);
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

}
