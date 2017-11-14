package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import pjm.tlcn.Model.Follow;
import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

public class TabActivity_search extends AppCompatActivity {
    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Photo> gridviewArrayPhoto = new ArrayList<Photo>();
    private CustomAdapterSearch customAdapterSearch;
    GridView gridViewSearch;
    ArrayList A = new ArrayList();
    ArrayList B = new ArrayList();

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
        getData();
        gridViewSearch = (GridView) findViewById(R.id.grid_search);
        customAdapterSearch = new CustomAdapterSearch(this, gridviewArrayPhoto);
        gridViewSearch.setAdapter(customAdapterSearch);

    }

    void getData() {
        uDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    A.add(snop.getKey());
                }

                uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snop : dataSnapshot.getChildren()) {

                            B.add(snop.getValue(Follow.class).getUser_id());
                        }
                        for (int i = 0; i < A.size(); i++) {
                            String a = A.get(i).toString();
                            if (B.contains(a)) {
                                B.remove(a);
                                A.remove(a);
                                i--;
                            }
                        }
                        ArrayList<String> C = new ArrayList<>();
                        C.addAll(A);
                        C.addAll(B);
                        C.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        int size = C.size();

                        for (int i = 0; i < size; i++) {
                            final String key = C.get(i).toString();
                            Log.d("keyne", key);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            Query query = reference.child("photos")
                                    .orderByChild("user_id").equalTo(key);

                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue() != null) {
                                         gridviewArrayPhoto.clear();
                                        for (DataSnapshot snop : dataSnapshot.getChildren()) {
                                            Photo photo = new Photo();
                                            Map<String, Object> objectMap = (HashMap<String, Object>) snop.getValue();
                                            photo.setCaption(objectMap.get("caption").toString());
                                            photo.setPhoto_id(objectMap.get("photo_id").toString());
                                            photo.setUser_id(objectMap.get("user_id").toString());
                                            photo.setDate_created(objectMap.get("date_created").toString());
                                            photo.setImage_path(objectMap.get("image_path").toString());
                                            // Photo temp = new Photo();
                                            // temp = snop.getValue(Photo.class);
                                            gridviewArrayPhoto.add(photo);
                                        }
                                        Collections.reverse(gridviewArrayPhoto);
                                        customAdapterSearch.notifyDataSetChanged();
                                    } else {

                                    }

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
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
