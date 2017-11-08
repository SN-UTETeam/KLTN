package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pjm.tlcn.Adapter.RecyclerView_TabCmt;
import pjm.tlcn.Model.Cmt_tabProfile;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;
import static pjm.tlcn.Adapter.RecyclerView_TabPost.img_id;

public class ViewCmt_tabProfile extends AppCompatActivity {

    private RecyclerView rc_cmt_tabprofile;
    private EditText edt_cmt_tabprofile;
    private ImageView img_sendcmt_tabprofile,img_tabcmt_toolbar;
    private RecyclerView_TabCmt recyclerView_tabCmt;
    private DatabaseReference cDatabase,iDatabase;
    private ArrayList<Cmt_tabProfile> cmt_tabProfiles = new ArrayList<Cmt_tabProfile>();
    private android.support.v7.widget.Toolbar toolbar_tabcmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cmt_tab_profile);

        rc_cmt_tabprofile = (RecyclerView) findViewById(R.id.rc_cmt_tabprofile);
        edt_cmt_tabprofile = (EditText) findViewById(R.id.edt_cmt_tabprofile);
        img_sendcmt_tabprofile = (ImageView) findViewById(R.id.img_sendcmt_tabprofile);
        img_tabcmt_toolbar = (ImageView) findViewById(R.id.img_tabcmt_toolbar);
        toolbar_tabcmt = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_tabcmt);

        //Set Back ToolBar
        setSupportActionBar(toolbar_tabcmt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_tabcmt.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String imgurl = getIntent().getExtras().getString("imgurl");
        String status = getIntent().getExtras().getString("status");

        toolbar_tabcmt.setTitle(status);
        Picasso.with(getApplicationContext()).load(imgurl).into(img_tabcmt_toolbar);


        //Firebase
        cDatabase = FirebaseDatabase.getInstance().getReference().child("Comments").child(img_id);
        iDatabase = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);

        //RC
        recyclerView_tabCmt = new RecyclerView_TabCmt(cmt_tabProfiles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc_cmt_tabprofile.setLayoutManager(layoutManager);
        rc_cmt_tabprofile.setAdapter(recyclerView_tabCmt);
        //Load Cmt

        cDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cmt_tabProfiles.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cmt_tabProfile cmt = postSnapshot.getValue(Cmt_tabProfile.class);
                    cmt_tabProfiles.add(cmt);
                }
                //Collections.reverse(cmt_tabProfiles);
                recyclerView_tabCmt.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //On click button send
        img_sendcmt_tabprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_cmt_tabprofile.getText().toString().length()>0){
                    String timeStamp = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());

                    Cmt_tabProfile cmt_tabProfile = new Cmt_tabProfile(user_id+"",
                                                                        edt_cmt_tabprofile.getText().toString()+"",
                                                                        timeStamp+""
                                                                        );
                    cDatabase.push().setValue(cmt_tabProfile);
                    edt_cmt_tabprofile.setText("");

                }
                else
                    Toast.makeText(getApplicationContext(),"You must be enter an comment!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
