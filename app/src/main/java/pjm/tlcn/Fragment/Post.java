package pjm.tlcn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import pjm.tlcn.Adapter.RecyclerView_TabPost;
import pjm.tlcn.Model.Image;
import pjm.tlcn.R;

import static com.facebook.FacebookSdk.getApplicationContext;
import static pjm.tlcn.Activity.Login.user_id;

public class Post extends Fragment{
private RecyclerView rv_tabpost;
SwipeRefreshLayout mSwipeRefreshLayout;
private RecyclerView_TabPost recyclerView_tabPost;
private ArrayList<Image> imageArrayList = new ArrayList<Image>();
private DatabaseReference uDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post, container, false);

        rv_tabpost = (RecyclerView) v.findViewById(R.id.rv_tabpost);

        recyclerView_tabPost = new RecyclerView_TabPost(imageArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_tabpost.setLayoutManager(layoutManager);
        rv_tabpost.setAdapter(recyclerView_tabPost);

        //Swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                imageArrayList.clear();
                loadData();
                recyclerView_tabPost.notifyDataSetChanged();
            }
        });
        loadData();
        return v;
    }

    public void loadData(){
        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageArrayList.clear();
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot snop:dataSnapshot.getChildren()){
                        Image image = new Image();
                        image = snop.getValue(Image.class);
                        imageArrayList.add(image);
                    }
                    Collections.reverse(imageArrayList);
                    recyclerView_tabPost.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
