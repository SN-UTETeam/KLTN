package pjm.tlcn.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pjm.tlcn.Adapter.CustomAdapterSearch;
import pjm.tlcn.Model.Image;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;

public class TabActivity_search extends AppCompatActivity {
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();
    private ArrayList<Image> gridviewArrayImage=new ArrayList<Image>();
    private CustomAdapterSearch customAdapterSearch;
    GridView gridViewSearch;
    List A=new ArrayList();
    List B=new ArrayList();
   // List c =new ArrayList<>();



  //  ArrayList<String> mangkey = new ArrayList<String>();
   // String[] tamp =mStrings.toArray();
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
       customAdapterSearch = new CustomAdapterSearch(this,gridviewArrayImage);
       gridViewSearch.setAdapter(customAdapterSearch);

    }

    void getData(){

        uDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snop : dataSnapshot.getChildren()) {
                    A.add(snop.getKey());
                }

                uDatabase.child("Follow").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snop : dataSnapshot.getChildren()) {
                            //  mangkey.add(snop.getKey());
                            B.add(snop.getKey());
                        }

                        for (int i=0; i<A.size(); i++)
                        {
                          String  a = A.get(i).toString();

                            if (B.contains(a))
                            {
                                B.remove(a);
                                A.remove(a);
                                i--;
                            }
                        }

                        List<String> C = new ArrayList<>();
                        C.addAll(A);
                        C.addAll(B);

                        int size = C.size();
                        for(int i=0;i<size;i++) {

                            String key = C.get(i).toString();
                            //  Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
                            uDatabase.child("Images").child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.getValue() != null) {
                                        for (DataSnapshot snop : dataSnapshot.getChildren()) {

                                            //   Toast.makeText(TabActivity_search.this, mangkey.toString(), Toast.LENGTH_SHORT).show();
                                            Image temp = new Image();
                                            temp = snop.getValue(Image.class);
                                            gridviewArrayImage.add(temp);
                                            customAdapterSearch.notifyDataSetChanged();
                                        }
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
