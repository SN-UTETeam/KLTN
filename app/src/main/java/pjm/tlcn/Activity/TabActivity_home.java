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

import pjm.tlcn.Adapter.GridViewAdapter;
import pjm.tlcn.Adapter.ListViewAdapter;
import pjm.tlcn.Adapter.RecyclerViewItem;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;


public class TabActivity_home extends AppCompatActivity {

    private ArrayList<RecyclerViewItem> corporations;
    private ArrayList<User> gr=new ArrayList<User>();
   // private ArrayList<RecyclerViewItem> gr;
    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private FirebaseAuth mAuth;
    ////
    ListView lv;
    GridView gv;
    TextView tvfont,tvall;
    DatabaseReference uDatabase,usDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_home);
        tvfont = (TextView)findViewById(R.id.font);
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
        //set layout manager and adapter for "ListView"
         LinearLayoutManager horizontalManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         listView.setLayoutManager(horizontalManager);
         listViewAdapter = new ListViewAdapter(this, corporations);
        listView.setAdapter(listViewAdapter);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, gr);

        gridView.setAdapter(gridViewAdapter);
    }

    void setDATA()
    {

        corporations = new ArrayList<>();
        corporations.add(new RecyclerViewItem(R.drawable.ava, "Microsoft"));
        corporations.add(new RecyclerViewItem(R.drawable.ic_message_tab, "Apple"));
        corporations.add(new RecyclerViewItem(R.drawable.ic_photo, "Google"));
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        uDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snop : dataSnapshot.getChildren()) {
                        //  gr = dataSnapshot.getValue(gr.class);
                        //   gr.clear();
                        String use = snop.getKey();
                        User temp = new User();
                        temp = snop.getValue(User.class);
                        Log.d("AAA", temp.getUsername());
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

    private void setDummyData() {
        gr.add(new User("1","2","rw","a","ad" ,"sd","NgocDoly"));


        // corporations.add(new RecyclerViewItem(R.drawable.oracle, "Oracle"));
        // corporations.add(new RecyclerViewItem(R.drawable.yahoo, "Yahoo"));
        // corporations.add(new RecyclerViewItem(R.drawable.mozilla, "Mozilla"));

        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                gr.clear();
                if (dataSnapshot.getValue()!=null)
                {
                    for (DataSnapshot snop: dataSnapshot.getChildren()) {
                        //  gr = dataSnapshot.getValue(gr.class);
                        //   gr.clear();
                        String use = snop.getKey();
                        User temp=new User();

                        temp=snop.getValue(User.class);
                        Log.d("AAA",temp.getUsername());
                        gr.add(temp);

                   /* JSONObject uname = null;
                    try {
                        uname = new JSONObject(snop.getValue().toString());
                        String u1 = uname.getString("email");
                        Log.d(use,u1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
//                    String email =snop.getValue().toString();
//                    String a =snop.getValue().toString();
//                    String b =snop.getValue().toString();
//                    String c =snop.getValue().toString();
//                    String d =snop.getValue().toString();

                        // gr.add(new User(u));
                        gridViewAdapter.notifyDataSetChanged();
                        //  Log.e("Get Data", user.getAvatarurl());
                        // gr =snop.getValue(User.class);
                        //     gr.add(snop.getValue().toString());
                    }
                }
                else{
                    Log.d("AAA","NUL");
                }



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        uDatabase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//              //  gr.add(dataSnapshot.)
//               // gr =dataSnapshot.getValue();
//                Log.d("Aa",dataSnapshot.getKey());
//                for (DataSnapshot snop: dataSnapshot.getChildren()) {
//                   String userid= dataSnapshot.getKey();
//                    Log.d("id",userid);
////                   usDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
////                    usDatabase.addValueEventListener(new ValueEventListener() {
////                        @Override
////                        public void onDataChange(DataSnapshot dataSnapshot) {
////                            User user = dataSnapshot.getValue(User.class);
////                            gr.clear();
////                            gr.add(user);
////                            gridViewAdapter.notifyDataSetChanged();
////                        }
////
////                        @Override
////                        public void onCancelled(DatabaseError databaseError) {
////
////                        }
////                    });
//                  //  Log.i("aaaa", snop.getKey());
//                  // // Log.e("Get Data", post.<YourMethod>());
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


       gr.add(new User("1","2","rw","a","ad" ,"sd","NgocDoly"));
      /// operatingSystems.add(new RecyclerViewItem(R.drawable.ic_photo, "Tizen"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.android, "Android"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.symbian, "Symbian"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.firefox_os, "Firefox OS"));
        // operatingSystems.add(new RecyclerViewItem(R.drawable.wp_os, "Windows Phone OS"));
    }
}
