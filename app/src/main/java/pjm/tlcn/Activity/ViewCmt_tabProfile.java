package pjm.tlcn.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
import pjm.tlcn.Model.Comment;
import pjm.tlcn.R;

public class ViewCmt_tabProfile extends AppCompatActivity {

    private RecyclerView rc_cmt_tabprofile;
    private EditText edt_cmt_tabprofile;
    private ImageView img_sendcmt_tabprofile,img_tabcmt_toolbar;
    private RecyclerView_TabCmt recyclerView_tabCmt;
    private DatabaseReference databaseRef;
    private ArrayList<Comment> cmt_tabProfiles = new ArrayList<Comment>();
    private android.support.v7.widget.Toolbar toolbar_tabcmt;
    private String ref_img;
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
        final String photo_id = getIntent().getExtras().getString("photo_id");
        final String caption = getIntent().getExtras().getString("caption");
        String image_path = getIntent().getExtras().getString("image_path");
        cmt_tabProfiles = getIntent().getParcelableArrayListExtra("ArrayComment");
        ref_img = getIntent().getExtras().getString("ref_img");

        toolbar_tabcmt.setTitle(caption);
        Picasso.with(getApplicationContext()).load(image_path).into(img_tabcmt_toolbar);


        //Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();

        //RC
        recyclerView_tabCmt = new RecyclerView_TabCmt(cmt_tabProfiles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc_cmt_tabprofile.setLayoutManager(layoutManager);
        rc_cmt_tabprofile.setAdapter(recyclerView_tabCmt);
        //Load Cmt
        databaseRef.child("photos").child(photo_id).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cmt_tabProfiles.clear();
                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    Comment comment = new Comment();
                    comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
                    comment.setComment(dSnapshot.getValue(Comment.class).getComment());
                    comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
                    cmt_tabProfiles.add(comment);
                    recyclerView_tabCmt.notifyDataSetChanged();
                }
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
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                    String commentID = databaseRef.push().getKey();
                    Comment comment = new Comment();
                    comment.setComment(edt_cmt_tabprofile.getText().toString()+"");
                    comment.setDate_created(timeStamp);
                    comment.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    databaseRef.child("photos")
                            .child(photo_id)
                            .child("comments")
                            .child(commentID)
                            .setValue(comment);
                    databaseRef.child("user_photos")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(photo_id)
                            .child("comments")
                            .child(commentID)
                            .setValue(comment);
                    edt_cmt_tabprofile.setText("");
                    hiddenKeyboard();

                }
                else
                    Toast.makeText(getApplicationContext(),"You must be enter an comment!",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void hiddenKeyboard(){
        InputMethodManager inputManager =
                (InputMethodManager) this.
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
