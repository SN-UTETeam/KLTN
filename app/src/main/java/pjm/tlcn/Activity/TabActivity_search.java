package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

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

import pjm.tlcn.Adapter.CustomAdapterSearch;
import pjm.tlcn.Adapter.EndlessRecyclerViewScrollListener;
import pjm.tlcn.Adapter.PostGrid_Adapter;
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.Item_GridPhoto;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

public class TabActivity_search extends AppCompatActivity {
    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Item_GridPhoto> gridviewArrayPhoto = new ArrayList<Item_GridPhoto>();
    private CustomAdapterSearch customAdapterSearch;
    RecyclerView recyclerView;
    GridView gridViewSearch;
    ArrayList A = new ArrayList();
    ArrayList B = new ArrayList();
    ArrayList<String> C = new ArrayList<>();
    private String keyPhoto = "";
    private EndlessRecyclerViewScrollListener scrollListener;

    ///
    PostGrid_Adapter postGrid_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_search);
        LinearLayout lnsearch = (LinearLayout) findViewById(R.id.timkiem);
        lnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timkiem = new Intent(TabActivity_search.this, Activity_timkiem.class);
                startActivity(timkiem);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.grid_search);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        //  gridViewSearch = (GridView) findViewById(R.id.grid_search);
        postGrid_adapter = new PostGrid_Adapter(this, gridviewArrayPhoto);
        // customAdapterSearch = new CustomAdapterSearch(this, gridviewArrayPhoto);
        recyclerView.setAdapter(postGrid_adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadmoreData(postGrid_adapter.getItemCount());


              postGrid_adapter.notifyDataSetChanged();
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void loadmoreData(final int site) {
       // gridviewArrayPhoto.clear();
        uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                B.clear();
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    //Log.d("Found B",snop.getValue(Follow.class).getUser_id());
                    B.add(snop.getValue(Follow.class).getUser_id());
                }

                uDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                      //  A.clear();
                        for (DataSnapshot snop : dataSnapshot.getChildren()) {
                            // Log.d("Found A",snop.getKey());
                            A.add(snop.getKey());

                        }
                        for (int i = 0; i < B.size(); i++) {
                            if (A.contains(B.get(i)))
                                A.remove(B.get(i));
                        }
                        //
                        C.clear();
                        C.addAll(A);
                        //    C.addAll(B);
                        C.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        for (int i=0;i<C.size();i++){
                            Log.d("mang C",C.get(i).toString());
                        }
                      //  final int size = C.size();
                        //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        final Query query = uDatabase.child("photos").orderByKey().endAt(keyPhoto).limitToLast(site+6);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() != null) {
                                    gridviewArrayPhoto.clear();


                                    int i = 0;
                                    for (DataSnapshot snop : dataSnapshot.getChildren()) {
                                        Photo photo = new Photo();
                                        Map<String, Object> objectMap = (HashMap<String, Object>) snop.getValue();

                                        keyPhoto = objectMap.get("photo_id").toString();
                                        photo.setCaption(objectMap.get("caption").toString());
                                        // photo.setPhoto_id(objectMap.get("photo_id").toString());
                                        photo.setUser_id(objectMap.get("user_id").toString());
                                        //  photo.setDate_created(objectMap.get("date_created").toString());
                                        String photo_id = (objectMap.get("photo_id").toString());
                                        if (C.contains(objectMap.get("user_id").toString())) {

                                            if (i >= 6) {
                                                query.removeEventListener(this);
                                                break;
                                            }
                                            i++;
                                            Log.d("erro", objectMap.get("photo_id").toString());

                                            for (DataSnapshot dSnapshot : snop
                                                    .child("image_path").getChildren()) {
                                                Item_GridPhoto item_gridPhoto = new Item_GridPhoto();
                                                String path = dSnapshot.child("path").getValue().toString();
                                                item_gridPhoto.setPhoto_id(photo_id);
                                                item_gridPhoto.setPath(path);
                                                gridviewArrayPhoto.add(item_gridPhoto);
                                                Log.d("idphoto", path);
                                            }
                                        }

                                    }
                                    Collections.reverse(gridviewArrayPhoto);
                                    postGrid_adapter.notifyDataSetChanged();
                                } else {

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    protected void onStart() {
        super.onStart();
     //   gridviewArrayPhoto.clear();
        uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                B.clear();
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    //Log.d("Found B",snop.getValue(Follow.class).getUser_id());
                    B.add(snop.getValue(Follow.class).getUser_id());
                }

                uDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        A.clear();
                        for (DataSnapshot snop : dataSnapshot.getChildren()) {
                            // Log.d("Found A",snop.getKey());
                            A.add(snop.getKey());

                        }
                        for (int i = 0; i < B.size(); i++) {
                            if (A.contains(B.get(i)))
                                A.remove(B.get(i));
                        }
                        //
                        C.clear();
                        C.addAll(A);
                        //    C.addAll(B);
                        C.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());

                      //  final int size = C.size();
                        //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        final Query query = uDatabase.child("photos").limitToLast(6);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() != null) {
                                     gridviewArrayPhoto.clear();
                                    int i = 0;
                                    for (DataSnapshot snop : dataSnapshot.getChildren()) {
                                        Photo photo = new Photo();
                                        Map<String, Object> objectMap = (HashMap<String, Object>) snop.getValue();


                                        keyPhoto = objectMap.get("photo_id").toString();

                                        if (C.contains(objectMap.get("user_id").toString())) {

                                            if (i >= 6) {
                                                query.removeEventListener(this);
                                                break;
                                            }
                                            i++;
                                            photo.setCaption(objectMap.get("caption").toString());
                                            // photo.setPhoto_id(objectMap.get("photo_id").toString());
                                            photo.setUser_id(objectMap.get("user_id").toString());
                                            //  photo.setDate_created(objectMap.get("date_created").toString());
                                            String photo_id = (objectMap.get("photo_id").toString());

                                            for (DataSnapshot dSnapshot : snop
                                                    .child("image_path").getChildren()) {
                                                Item_GridPhoto item_gridPhoto = new Item_GridPhoto();
                                                String path = dSnapshot.child("path").getValue().toString();
                                                item_gridPhoto.setPhoto_id(photo_id);
                                                item_gridPhoto.setPath(path);
                                                gridviewArrayPhoto.add(item_gridPhoto);
                                                Log.d("idphoto", path);
                                            }
                                        }

                                    }
                                    Collections.reverse(gridviewArrayPhoto);
                                    postGrid_adapter.notifyDataSetChanged();
                                } else {

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        postGrid_adapter.notifyDataSetChanged();
    }

}
