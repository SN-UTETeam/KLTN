package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Adapter.ListView_ViewFollow;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class ViewFollow extends AppCompatActivity {
Toolbar toolbar_view_follow;
ListView lv_view_follow;
ListView_ViewFollow listView_viewFollow;
ArrayList<User> arrayUser = new ArrayList<User>();
DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_follow);

        //GetData Form intent
        String view = getIntent().getExtras().getString("view");
        String user_id = getIntent().getExtras().getString("user_id");


                //Set back Toolbar
        toolbar_view_follow = (Toolbar) findViewById(R.id.toolbar_view_follow);
        if(view.equals("followers"))
        toolbar_view_follow.setTitle("Người theo dõi");
        else toolbar_view_follow.setTitle("Đang theo dõi");
        setSupportActionBar(toolbar_view_follow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_view_follow.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_view_follow = (ListView) findViewById(R.id.lv_view_follow);
        listView_viewFollow = new ListView_ViewFollow(this,R.layout.item_view_follow,arrayUser);
        lv_view_follow.setAdapter(listView_viewFollow);

        //Get data form firebase
        databaseRef.child(view).child(user_id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayUser.clear();
                        for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(singleSnapshot.child("user_id").getValue().toString());
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        arrayUser.add(user);
                                        listView_viewFollow.notifyDataSetChanged();

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


    }
}
