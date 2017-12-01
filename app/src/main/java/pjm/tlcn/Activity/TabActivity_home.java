package pjm.tlcn.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.GridViewAdapter;
import pjm.tlcn.Adapter.ListViewAdapter;
import pjm.tlcn.Adapter.RecyclerView_TabPost;
import pjm.tlcn.Model.Comment;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;


public class TabActivity_home extends AppCompatActivity {

    // private ArrayList<RecyclerViewItem> corporations;
    private ArrayList<User> gr = new ArrayList<User>();
    private ArrayList<Photo> lvs;
    private RecyclerView listView;
    private RecyclerView gridView;
    private RecyclerView_TabPost recyclerView_tabPost;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private FirebaseAuth mAuth;
    public static String id_image = "";
    String keyfollow = "";
    ////
    ListView lv;
    GridView gv;
    TextView tvfont, tvall;
    Button follow;
    ArrayList A = new ArrayList();
    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);
        tvfont = (TextView) findViewById(R.id.font);
        // khai báo và add kiểu font bạn cần
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SNAP____.TTF");
        //add kiểu font vào textview font
        tvfont.setTypeface(typeface);
        ////
        tvall = (TextView) findViewById(R.id.idviewall);
        tvall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TabActivity_viewall.class);
                startActivity(intent);
            }
        });


        listView = (RecyclerView) findViewById(R.id.list);
        gridView = (RecyclerView) findViewById(R.id.grid);

        listView.setHasFixedSize(true);
        gridView.setHasFixedSize(true);
        setDATA();


        lvs = new ArrayList<>();


        uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                A.clear();
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    //Log.d("Found B",snop.getValue(Follow.class).getUser_id());
                    A.add(snop.getValue(Follow.class).getUser_id());
                }
                A.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                final int size = A.size();
                // Query query = uDatabase.child("photos").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                uDatabase.child("photos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            lvs.clear();
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Photo photo = new Photo();
                                Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                                photo.setCaption(objectMap.get("caption").toString());
                                photo.setPhoto_id(objectMap.get("photo_id").toString());
                                photo.setUser_id(objectMap.get("user_id").toString());
                                photo.setDate_created(objectMap.get("date_created").toString());
                                ArrayList<Comment> comments = new ArrayList<Comment>();
                                for (DataSnapshot dSnapshot : singleSnapshot
                                        .child("comments").getChildren()) {
                                    Comment comment = new Comment();
                                    comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                                    comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                                    comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                                    comments.add(comment);
                                }
                                photo.setComments(comments);
                                for (int i = 0; i < size; i++) {
                                    final String key = A.get(i).toString();
                                    if ((key.equals(photo.getUser_id()) )) {
                                        lvs.add(photo);
                                    }
                                }

                            }
                            Collections.reverse(lvs);
                            int a = lvs.size();
                            for(int i=0;i<a;i++){
                                String test = lvs.get(i).toString();
                                Log.d("itemphoto",test);
                            }
                            recyclerView_tabPost.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set layout manager and adapter for "ListView"
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(horizontalManager);
        recyclerView_tabPost = new RecyclerView_TabPost(lvs);
        listView.setAdapter(recyclerView_tabPost);

//        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, gr);
        gridView.setAdapter(gridViewAdapter);
    }

    void setDATA() {

        uDatabase.child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snop : dataSnapshot.getChildren()) {
                        //  gr = dataSnapshot.getValue(gr.class);
                        //   gr.clear();
                        String use = snop.getKey();
                        User temp = new User();
                        temp = snop.getValue(User.class);
                        gr.add(temp);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d("AAA", "NUL");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
