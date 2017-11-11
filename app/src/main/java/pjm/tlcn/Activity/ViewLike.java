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
import java.util.List;

import pjm.tlcn.Adapter.ListView_ViewLike;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class ViewLike extends AppCompatActivity {

    Toolbar toolbar_view_like;
    ListView lv_view_like;
    ListView_ViewLike view_viewLike;
    List<User> likeList = new ArrayList<User>();
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_like);
        toolbar_view_like = (Toolbar) findViewById(R.id.toolbar_view_like);
        lv_view_like = (ListView) findViewById(R.id.lv_view_like);

        //Set back Toolbar
        setSupportActionBar(toolbar_view_like);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_view_like.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        view_viewLike = new ListView_ViewLike(this,R.layout.item_view_like,likeList);
        lv_view_like.setAdapter(view_viewLike);

        String photo_id = getIntent().getExtras().getString("photo_id");

        databaseRef.child("photos").child(photo_id).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()) {
                    //Log.d("key ",singleSnapshot.child("user_id").getValue().toString());
                   databaseRef.child("users").child(singleSnapshot.child("user_id").getValue().toString()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           User user = dataSnapshot.getValue(User.class);
                           likeList.add(user);
                           view_viewLike.notifyDataSetChanged();
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
