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
import android.widget.Button;
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
import pjm.tlcn.Model.Image;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;


public class TabActivity_home extends AppCompatActivity {

   // private ArrayList<RecyclerViewItem> corporations;
    private ArrayList<User> gr=new ArrayList<User>();
    private ArrayList<Image> lvs;
    private RecyclerView listView;
    private RecyclerView gridView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private FirebaseAuth mAuth;
    public static String id_image="";
    ////
    ListView lv;
    GridView gv;
    TextView tvfont,tvall;
    Button follow;
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();


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
        listViewAdapter = new ListViewAdapter(this, lvs);
        listView.setAdapter(listViewAdapter);

        //set layout manager and adapter for "GridView"
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridView.setLayoutManager(layoutManager);
        gridViewAdapter = new GridViewAdapter(this, gr);
        gridView.setAdapter(gridViewAdapter);
    }

    void setDATA()
    {
      //  String tamp =[user_id];
        lvs =new ArrayList<>();
        uDatabase.child("Images").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lvs.clear();
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot snop:dataSnapshot.getChildren()){
                        Image img =new Image();
                        img = snop.getValue(Image.class);
                        lvs.add(img);
                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        uDatabase.child("Users").addValueEventListener(new ValueEventListener() {

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


}
