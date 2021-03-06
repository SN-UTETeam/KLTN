package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.EndlessRecyclerViewScrollListener;
import pjm.tlcn.Adapter.RecyclerView_TabPost;
import pjm.tlcn.Model.Comment;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

public class Saved extends AppCompatActivity {
    Toolbar toolbar_saved;
    private RecyclerView rv_tabsaved;
    private static TextView example_photo_saved;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView_TabPost recyclerView_tabSaved;
    private ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        example_photo_saved = (TextView) findViewById(R.id.example_photo_saved);

        //Set back Toolbar
        toolbar_saved = (Toolbar) findViewById(R.id.toolbar_saved);
        setSupportActionBar(toolbar_saved);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_saved.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rv_tabsaved = (RecyclerView) findViewById(R.id.rv_tabsaved);
        loadData();
        recyclerView_tabSaved = new RecyclerView_TabPost(photoArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tabsaved.setLayoutManager(layoutManager);
        rv_tabsaved.setAdapter(recyclerView_tabSaved);



        //Swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.saved_swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                photoArrayList.clear();
                loadData();
                example_photo_saved.setVisibility(View.GONE);
                recyclerView_tabSaved.notifyDataSetChanged();
            }
        });


        //Loadmore
        //Loadmore
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadmoreData(recyclerView_tabSaved.getItemCount());
            }
        };
        // Adds the scroll listener to RecyclerView
        rv_tabsaved.addOnScrollListener(scrollListener);
    }

    public void loadData(){
        //Firebase

        Query query = databaseRef.child("photo_saved").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .limitToLast(6);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int i=0;
                    removeExample();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        if(i>=6) {
                            Log.d("Remove",i+"");
                            break;}
                        else i++;
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        final Query query = reference.child("photos")
                                .orderByChild("photo_id").equalTo(singleSnapshot.child("photo_id").getValue().toString());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<Photo> list = new ArrayList<Photo>();
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Photo photo = new Photo();
                                    Log.d("key photo", singleSnapshot.child("photo_id").getValue().toString());
                                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                                    photo.setCaption(objectMap.get("caption").toString());
                                    photo.setPhoto_id(objectMap.get("photo_id").toString());
                                    photo.setUser_id(objectMap.get("user_id").toString());
                                    photo.setDate_created(objectMap.get("date_created").toString());
 //                                   photo.setImage_path(objectMap.get("Image_path").toString());

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
                                    list.add(photo);

                                }
                                Collections.reverse(list);
                                photoArrayList.addAll(list);
                                recyclerView_tabSaved.notifyDataSetChanged();
                                query.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void loadmoreData(int size){
        //Firebase

        Query query = databaseRef.child("photo_saved").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .limitToLast(size + 6);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int i=0;
                    removeExample();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        if(i>=6) {
                            Log.d("Remove",i+"");
                            break;}
                        else i++;
                        Log.d("Size ",photoArrayList.size()+"");
                        if(singleSnapshot.child("photo_id").getValue().toString().equals(photoArrayList.get(photoArrayList.size()-1).getPhoto_id()))
                            break;
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        final Query query = reference.child("photos")
                                .orderByChild("photo_id").equalTo(singleSnapshot.child("photo_id").getValue().toString());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ArrayList<Photo> list = new ArrayList<Photo>();
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Photo photo = new Photo();
                                    Log.d("key photo", singleSnapshot.child("photo_id").getValue().toString());
                                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                                    photo.setCaption(objectMap.get("caption").toString());
                                    photo.setPhoto_id(objectMap.get("photo_id").toString());
                                    photo.setUser_id(objectMap.get("user_id").toString());
                                    photo.setDate_created(objectMap.get("date_created").toString());
                                    //                                   photo.setImage_path(objectMap.get("Image_path").toString());

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
                                   list.add(photo);

                                }
                                Collections.reverse(list);
                                photoArrayList.addAll(list);
                                recyclerView_tabSaved.notifyDataSetChanged();
                                query.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void removeExample(){
        example_photo_saved.setVisibility(View.GONE);
    }
}
