package pjm.tlcn.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import pjm.tlcn.Adapter.PostGrid_Adapter;
import pjm.tlcn.Model.Item_GridPhoto;
import pjm.tlcn.R;


public class PostGrid extends Fragment {
PostGrid_Adapter postGrid_adapter;
RecyclerView recyclerView;
ArrayList<Item_GridPhoto> arrayPhoto = new ArrayList<Item_GridPhoto>();
SwipeRefreshLayout mSwipeRefreshLayout;
DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_postgrid, container, false);

        //Set  grid view
        recyclerView = (RecyclerView) v.findViewById(R.id.rc_postgird);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        postGrid_adapter = new PostGrid_Adapter(getContext(),arrayPhoto);
        recyclerView.setAdapter(postGrid_adapter);
        loadData();

        //Swipe
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.postgrid_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                arrayPhoto.clear();
                loadData();
                postGrid_adapter.notifyDataSetChanged();
            }
        });

        //Loadmore
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadmoreData(postGrid_adapter.getItemCount());
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        return v;
    }
    public void loadData(){
        final Query query = databaseRef.child("photos").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).limitToLast(6);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int i=0;
                    ArrayList<Item_GridPhoto> list = new ArrayList<Item_GridPhoto>();
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                        if(objectMap.get("user_id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Log.d("Limit", i + "");
                            if (i >= 6) {
                                Log.d("Remove", i + "");
                                break;
                            } else i++;

                            String photo_id = (objectMap.get("photo_id").toString());

                            for (DataSnapshot dSnapshot : singleSnapshot
                                    .child("image_path").getChildren()) {
                                Item_GridPhoto item_gridPhoto = new Item_GridPhoto();
                                String path = dSnapshot.child("path").getValue().toString();
                                item_gridPhoto.setPhoto_id(photo_id);
                                item_gridPhoto.setPath(path);
                                list.add(item_gridPhoto);
                            }
                        }
                    }

                    Collections.reverse(list);
                    arrayPhoto.addAll(list);
                    postGrid_adapter.notifyDataSetChanged();
                    query.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadmoreData(int size){
        final Query query1 = databaseRef.child("photos").orderByChild("user_id").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .limitToLast(size+6);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    int i=0;
                    ArrayList<Item_GridPhoto> list = new ArrayList<Item_GridPhoto>();

                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                        if(objectMap.get("user_id").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Log.d("Limit", i + "");
                            if (i >= 6) {
                                Log.d("Remove", i + "");
                                break;
                            } else i++;
                            if(objectMap.get("photo_id").toString().equals(arrayPhoto.get(arrayPhoto.size()-1).getPhoto_id()))
                                break;

                            String photo_id = (objectMap.get("photo_id").toString());

                            for (DataSnapshot dSnapshot : singleSnapshot
                                    .child("image_path").getChildren()) {
                                Item_GridPhoto item_gridPhoto = new Item_GridPhoto();
                                String path = dSnapshot.child("path").getValue().toString();
                                item_gridPhoto.setPhoto_id(photo_id);
                                item_gridPhoto.setPath(path);
                                list.add(item_gridPhoto);
                            }
                        }
                    }

                    Collections.reverse(list);
                    arrayPhoto.addAll(list);
                    postGrid_adapter.notifyDataSetChanged();
                    query1.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            TabActivity_profile profile = (TabActivity_profile) getActivity();
//            profile.SetTablayout(0);
//
//
//        }else{
//            // fragment is no longer visible
//        }
//    }
}
