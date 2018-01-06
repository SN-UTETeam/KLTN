package pjm.tlcn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static com.facebook.FacebookSdk.getApplicationContext;

public class PostList extends Fragment{
    private RecyclerView rv_tabpost;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView_TabPost recyclerView_tabPost;
    private ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
    private DatabaseReference uDatabase;
    private String oldkey;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        rv_tabpost = (RecyclerView) v.findViewById(R.id.rv_tabpost);
        recyclerView_tabPost = new RecyclerView_TabPost(photoArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tabpost.setLayoutManager(layoutManager);
        rv_tabpost.setAdapter(recyclerView_tabPost);
        loadData();

        //Swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                photoArrayList.clear();
                loadData();
                recyclerView_tabPost.notifyDataSetChanged();
            }
        });

        //Loadmore
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadmoreData(recyclerView_tabPost.getItemCount());
            }
        };
        // Adds the scroll listener to RecyclerView
        rv_tabpost.addOnScrollListener(scrollListener);

        return v;
    }

    public void loadData(){
        uDatabase = FirebaseDatabase.getInstance().getReference().child("photos");
        final Query query = uDatabase.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){
                    ArrayList<Photo> list = new ArrayList<Photo>();
                    int i=0;
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        if(objectMap.get("user_id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        {
                            Log.d("Limit",i+"");
                            if(i>=10) {
                                Log.d("Remove",i+"");
                                break;}
                            else i++;
                            Photo photo = new Photo();


                            photo.setCaption(objectMap.get("caption").toString());
                            photo.setPhoto_id(objectMap.get("photo_id").toString());
                            photo.setUser_id(objectMap.get("user_id").toString());
                            photo.setDate_created(objectMap.get("date_created").toString());

                            ArrayList<Comment> comments = new ArrayList<Comment>();
                            for (DataSnapshot dSnapshot : singleSnapshot
                                    .child("comments").getChildren()){
                                Comment comment = new Comment();
                                comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                                comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                                comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                                comments.add(comment);
                            }

                            photo.setComments(comments);
                            list.add(photo);
                        }

                    }

                    Collections.reverse(list);
                    photoArrayList.addAll(list);
                    recyclerView_tabPost.notifyDataSetChanged();
                    query.removeEventListener(this);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadmoreData(int size){
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("photos");
        final Query query1 = uDatabase.orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .limitToLast(size+10);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null){
                    Log.d("Loadmore","Item");
                    ArrayList<Photo> list = new ArrayList<Photo>();
                    int i=0;
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                       // if(!singleSnapshot.getKey().equals(oldkey)) {
                            Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                            if (objectMap.get("user_id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                if (i >= 10) {
                                    break;
                                } else i++;
                                if(objectMap.get("photo_id").toString().equals(photoArrayList.get(photoArrayList.size()-1).getPhoto_id()))
                                    break;
                                Photo photo = new Photo();


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
                                list.add(photo);
                            }
                        //}
                    }

                    Collections.reverse(list);
                    photoArrayList.addAll(list);
                    recyclerView_tabPost.notifyDataSetChanged();
                    query1.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            photoArrayList.clear();
            loadData();
            recyclerView_tabPost.notifyDataSetChanged();


        }else{
            // fragment is no longer visible
        }
    }
}
