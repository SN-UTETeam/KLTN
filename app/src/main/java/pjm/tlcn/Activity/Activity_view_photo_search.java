package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

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

import pjm.tlcn.Adapter.RecyclerView_TabDiscover;
import pjm.tlcn.Model.Comment;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

import static pjm.tlcn.Adapter.CustomAdapterSearch.key_discover;

public class Activity_view_photo_search extends AppCompatActivity {
    ImageView back_Discover;
    ListView list_Discover;
    private RecyclerView rv_tabpost;
    // SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView_TabDiscover recyclerView_tabDiscover;
    private ArrayList<Photo> photoArrayList = new ArrayList<Photo>();
    private DatabaseReference uDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo_search);
        back_Discover =(ImageView)findViewById(R.id.back_discover);
        back_Discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // list_Discover =(ListView)findViewById(R.id.list_discover);
        rv_tabpost = (RecyclerView) findViewById(R.id.rv_discover);
        loadData();
        recyclerView_tabDiscover = new RecyclerView_TabDiscover(photoArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tabpost.setLayoutManager(layoutManager);
        rv_tabpost.setAdapter(recyclerView_tabDiscover);

        //Swipe
        //    mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
       /* mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                photoArrayList.clear();
                loadData();
                recyclerView_tabPost.notifyDataSetChanged();
            }
        });
*/
    }
    public void loadData() {



        uDatabase = FirebaseDatabase.getInstance().getReference().child("photos");
        Query query = uDatabase.orderByChild("user_id").equalTo(key_discover);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photoArrayList.clear();
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        Photo photo = new Photo();
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                        photo.setCaption(objectMap.get("caption").toString());
                        photo.setPhoto_id(objectMap.get("photo_id").toString());
                        photo.setUser_id(objectMap.get("user_id").toString());
                        photo.setDate_created(objectMap.get("date_created").toString());
                        photo.setImage_path(objectMap.get("image_path").toString());

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
                        photoArrayList.add(photo);
                    }
                    Collections.reverse(photoArrayList);
                    recyclerView_tabDiscover.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}