package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pjm.tlcn.Adapter.ListView_NewChat;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class NewChat extends AppCompatActivity {
Toolbar toolbar_new_chat;
ListView lv_new_chat;
List<User> userList= new ArrayList<User>();
ListView_NewChat listView_newChat;
DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        databaseRef = FirebaseDatabase.getInstance().getReference();

        //Set back Toolbar
        toolbar_new_chat = (Toolbar) findViewById(R.id.toolbar_new_chat);
        setSupportActionBar(toolbar_new_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_new_chat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_new_chat = (ListView) findViewById(R.id.lv_new_chat);
        listView_newChat = new ListView_NewChat(this,R.layout.item_view_like,userList);
        lv_new_chat.setAdapter(listView_newChat);

        databaseRef.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child("users")
                            .orderByChild("user_id")
                            .equalTo(singleSnapshot.getValue(Follow.class).getUser_id());

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        User user = singleSnapshot.getValue(User.class);
                                        userList.add(user);
                                        listView_newChat.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Set Onlick
        lv_new_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewChat.this,Chat.class);

                //Create room chat
                String newChatkey = databaseRef.push().getKey();
                pjm.tlcn.Model.Chat chat = new pjm.tlcn.Model.Chat();

                chat.setRoom_id(newChatkey);
                chat.setUser_id(userList.get(position).getUser_id());
                databaseRef.child("Chat")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(userList.get(position).getUser_id())
                        .setValue(chat);
                chat.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                databaseRef.child("Chat")
                        .child(userList.get(position).getUser_id())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(chat);

                intent.putExtra("user_id",userList.get(position).getUser_id());
                intent.putExtra("user_avatar",userList.get(position).getAvatarurl());
                intent.putExtra("room_id",newChatkey);
                intent.putExtra("username",userList.get(position).getUsername());
                startActivity(intent);
            }
        });
    }
}
