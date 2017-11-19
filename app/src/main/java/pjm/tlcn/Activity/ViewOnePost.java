package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.RecyclerView_TabPost;
import pjm.tlcn.Model.Comment;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

public class ViewOnePost extends AppCompatActivity {

    private RecyclerView rv_viewonepost;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView_TabPost recyclerView_tabPost;
    private ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
    private DatabaseReference uDatabase;
    private String photo_id;
    private Toolbar toolbar_viewonepost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_post);

        //Get value form intent
        photo_id = getIntent().getExtras().getString("photo_id");


        //Toolbar
        toolbar_viewonepost = (Toolbar) findViewById(R.id.toolbar_viewonepost);
        setSupportActionBar(toolbar_viewonepost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_viewonepost.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Var
        rv_viewonepost = (RecyclerView) findViewById(R.id.rv_viewonepost);
        loadData();
        recyclerView_tabPost = new RecyclerView_TabPost(photoArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_viewonepost.setLayoutManager(layoutManager);
        rv_viewonepost.setAdapter(recyclerView_tabPost);


        //Swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_viewonepost);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                photoArrayList.clear();
                loadData();
                recyclerView_tabPost.notifyDataSetChanged();
            }
        });


    }
    public void loadData(){
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("photos").child(photo_id);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){
                    photoArrayList.clear();
                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();

                        photo.setCaption(objectMap.get("caption").toString());
                        photo.setPhoto_id(objectMap.get("photo_id").toString());
                        photo.setUser_id(objectMap.get("user_id").toString());
                        photo.setDate_created(objectMap.get("date_created").toString());
                        photo.setImage_path(objectMap.get("image_path").toString());

                        ArrayList<Comment> comments = new ArrayList<Comment>();
                        for (DataSnapshot dSnapshot : dataSnapshot
                                .child("comments").getChildren()){
                            Comment comment = new Comment();
                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                            comments.add(comment);
                        }

                        photo.setComments(comments);
                        photoArrayList.add(photo);

                    Collections.reverse(photoArrayList);
                    recyclerView_tabPost.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
