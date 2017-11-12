package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Adapter.ListViewUserMessage;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class TabActivity_message extends AppCompatActivity {
private ListView listview_user_message;
private ArrayList<User> arrayUser = new ArrayList<User>();
private ArrayList<pjm.tlcn.Model.Chat> arrayChat = new ArrayList<pjm.tlcn.Model.Chat>();
private ListViewUserMessage listViewUserMessage;
private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
private ImageButton img_new_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_message);


        //And new message
        img_new_chat = (ImageButton) findViewById(R.id.img_new_chat);
        img_new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabActivity_message.this,NewChat.class);
                startActivity(intent);
            }
        });

        //List view
        listview_user_message = (ListView) findViewById(R.id.listview_user_message);
        listViewUserMessage = new ListViewUserMessage(this,R.layout.item_list_message,arrayUser);
        listview_user_message.setAdapter(listViewUserMessage);

        databaseRef.child("Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayUser.clear();
                arrayChat.clear();
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    pjm.tlcn.Model.Chat chat = singleSnapshot.getValue(pjm.tlcn.Model.Chat.class);
                    arrayChat.add(chat);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child("users")
                            .orderByChild("user_id")
                            .equalTo(singleSnapshot.getValue(pjm.tlcn.Model.Chat.class).getUser_id());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        User user = singleSnapshot.getValue(User.class);
                                        arrayUser.add(user);
                                        listViewUserMessage.notifyDataSetChanged();
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

        listview_user_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TabActivity_message.this,Chat.class);
                intent.putExtra("username",arrayUser.get(position).getUsername());
                intent.putExtra("user_avatar",arrayUser.get(position).getAvatarurl());
                intent.putExtra("room_id",arrayChat.get(position).getRoom_id());
                intent.putExtra("user_id",arrayChat.get(position).getUser_id());
                Log.d("username",arrayUser.get(position).getUsername());
                Log.d("user_avatar",arrayUser.get(position).getAvatarurl());
                Log.d("room_id",arrayChat.get(position).getRoom_id());
                Log.d("user_id",arrayChat.get(position).getUser_id());
                startActivity(intent);
            }
        });
    }
}
