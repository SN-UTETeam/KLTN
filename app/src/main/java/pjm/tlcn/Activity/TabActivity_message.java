package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Adapter.ListViewUserMessage;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;

public class TabActivity_message extends AppCompatActivity {
private ListView listview_user_message;
private ArrayList<Follow> arrayFollow = new ArrayList<Follow>();
private ArrayAdapter arrayAdapterUserMessage=null;
private DatabaseReference fDatabase,ffDatabase;
public static String usernamechatwith ="";
public static String useridchatwith ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_message);

        //Firebase
        fDatabase = FirebaseDatabase.getInstance().getReference().child("Follow").child(user_id);

        //List view
        listview_user_message = (ListView) findViewById(R.id.listview_user_message);
        arrayAdapterUserMessage = new ListViewUserMessage(this,R.layout.item_list_message,arrayFollow);
        listview_user_message.setAdapter(arrayAdapterUserMessage);

        fDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayFollow.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    String uname = postSnapshot.getValue().toString();
                    //Log.d(uid,uname);
                    arrayFollow.add(new Follow(uid,uname));
                    arrayAdapterUserMessage.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listview_user_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                usernamechatwith = arrayFollow.get(position).getUsername();
                useridchatwith = arrayFollow.get(position).getUserid();
                Intent intent = new Intent(TabActivity_message.this,Chat.class);
                startActivity(intent);
            }
        });
    }
}
