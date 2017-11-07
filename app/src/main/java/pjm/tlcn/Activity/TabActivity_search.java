package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pjm.tlcn.Adapter.CustomAdapterSearch;
import pjm.tlcn.Model.User;
import pjm.tlcn.R;

public class TabActivity_search extends AppCompatActivity {
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();
    private ArrayList<User> gridviewArrayUser=new ArrayList<User>();
    private CustomAdapterSearch customAdapterSearch;
    GridView gridViewSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_search);
        LinearLayout lnsearch=(LinearLayout) findViewById(R.id.timkiem);
        lnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timkiem= new Intent(TabActivity_search.this,Activity_timkiem.class);
                startActivity(timkiem);
            }
        });
        getData();
       gridViewSearch =(GridView)findViewById(R.id.grid_search);
       customAdapterSearch = new CustomAdapterSearch(this,gridviewArrayUser);
       gridViewSearch.setAdapter(customAdapterSearch);


        // get user image tra ve gridview


    }
    void getData(){
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
                        gridviewArrayUser.add(temp);
                        customAdapterSearch.notifyDataSetChanged();

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
