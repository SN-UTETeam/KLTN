package pjm.tlcn.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.EndlessRecyclerViewScrollListener;
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
    private ArrayList<Photo> lvs = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView gridView;
    private RecyclerView_TabPost recyclerView_tabPost;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private FirebaseAuth mAuth;
    public static String id_image = "";
    String keyPhoto = "";
    ////
    ListView lv;
    GridView gv;
    TextView tvfont, tvall;
    Button follow;
    ArrayList A = new ArrayList();
    DatabaseReference uDatabase = FirebaseDatabase.getInstance().getReference();

    private EndlessRecyclerViewScrollListener scrollListener;


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


        recyclerView = (RecyclerView) findViewById(R.id.list);
        //   gridView = (RecyclerView) findViewById(R.id.grid);
        LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(horizontalManager);

        recyclerView.setHasFixedSize(true);
        recyclerView_tabPost = new RecyclerView_TabPost(lvs);
        recyclerView.setAdapter(recyclerView_tabPost);
        //  gridView.setHasFixedSize(true);
        // setDATA();














        lvs.clear();


        //recyclerView_tabPost.notifyDataSetChanged();

        //set layout manager and adapter for "ListView"


        scrollListener = new EndlessRecyclerViewScrollListener(horizontalManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadmoreData(recyclerView_tabPost.getItemCount());

                //recyclerView_tabPost.notifyDataSetChanged();
                Handler handler = new Handler();

                final Runnable r = new Runnable() {
                    public void run() {
                        recyclerView_tabPost.notifyItemInserted(lvs.size() - 1);
                    }
                };

                handler.post(r);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }



   /* public void loadData() {

    }*/

    public void loadmoreData(final int size) {
        uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                A.clear();
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    //Log.d("Found B",snop.getValue(Follow.class).getUser_id());
                    A.add(snop.getValue(Follow.class).getUser_id());
                }
                A.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                //  final int size = A.size();
                final Query query1 = uDatabase.child("photos").orderByKey().endAt(keyPhoto).limitToLast(size+6);
                query1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            int i = 0;
                            ArrayList<Photo> list = new ArrayList<Photo>();
                            //  lvs.clear();
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Photo photo = new Photo();
                                Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                                keyPhoto = objectMap.get("photo_id").toString();

                                //  final String key = A.get(i).toString();
                                if (A.contains(objectMap.get("user_id").toString())) {
                                    if (i >= 6) {

                                        break;
                                    }
                                    i++;
                                    if (objectMap.get("photo_id").toString().equals(lvs.get(lvs.size() - 1).getPhoto_id()))
                                        break;
                                    Log.d("Found", objectMap.get("photo_id").toString());



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

                            }
                            Collections.reverse(list);
                            lvs.addAll(list);
                            query1.removeEventListener(this);
                            Log.d("List size" + list.size(), "Lvs size" + lvs.size());


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


    }
    protected void onStart() {
        super.onStart();

           lvs.clear();
        uDatabase.child("following").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                A.clear();
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    //Log.d("Found B",snop.getValue(Follow.class).getUser_id());
                    A.add(snop.getValue(Follow.class).getUser_id());
                }
                A.add(FirebaseAuth.getInstance().getCurrentUser().getUid());


                final Query query = uDatabase.child("photos").limitToLast(6);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            int i = 0;
                            final ArrayList<Photo> list = new ArrayList<Photo>();
                            //   lvs.clear();
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Photo photo = new Photo();
                                Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
                                keyPhoto = objectMap.get("photo_id").toString();
                                //  final String key = A.get(i).toString();
                                if (A.contains(objectMap.get("user_id").toString())) {
                                    if (i >= 6) {
                                        query.removeEventListener(this);
                                        break;
                                    }
                                    i++;
                                    Log.d("Found", objectMap.get("photo_id").toString());


                                        Log.d("Log key phto",keyPhoto);

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
                            }
                            Collections.reverse(list);
                            // Log.d("key list" + list.size(), "Lvs size" + lvs.size());


                            for( i=0;i<list.size();i++){
                                Log.d("keyphto",list.get(i).getPhoto_id().toString());
                            }
                            lvs.addAll(list);

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

        recyclerView_tabPost.notifyDataSetChanged();




    }






}
