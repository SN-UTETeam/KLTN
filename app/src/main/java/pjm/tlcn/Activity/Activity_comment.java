package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pjm.tlcn.Adapter.CustomAdapterComment;
import pjm.tlcn.Model.Comment;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.firebaseUser;
import static pjm.tlcn.Activity.Login.user_id;
import static pjm.tlcn.Activity.TabActivity_home.id_image;
import static pjm.tlcn.Adapter.ListViewAdapter.idimage;

public class Activity_comment extends AppCompatActivity {
     private ArrayList<Comment> lvcomments;
    private CustomAdapterComment customadapter;
    DatabaseReference uDatabase= FirebaseDatabase.getInstance().getReference();
    ListView lvcommet;
    ImageView imagesend,bt_comment;
    EditText idcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        lvcommet =(ListView)findViewById(R.id.lv_comment);
        imagesend =(ImageView)findViewById(R.id.send_id);
        idcomment=(EditText)findViewById(R.id.idcomment);
        //fisnish comment
        bt_comment =(ImageView) findViewById(R.id.btcomment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imagesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentTime = Calendar.getInstance().getTime();
                Comment fl = new Comment(idcomment.getText().toString(),currentTime.toString(), firebaseUser.getDisplayName());
              //  Log.d("Iamge id",idimage);
              //  Log.d("Path",uDatabase.child("Comments").child(idimage).child(user_id)+"");
                uDatabase.child("Comments").child(id_image).child(user_id).push().setValue(fl);
                idcomment.setText("");

            }
        });

        getData();
        customadapter = new CustomAdapterComment(this, lvcomments);
        lvcommet.setAdapter(customadapter);




    }
    void getData(){


        lvcomments = new ArrayList<>();
        uDatabase.child("Comments").child(idimage).child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    lvcomments.clear();
                    for(DataSnapshot snop:dataSnapshot.getChildren()){
                        Comment cmt =new Comment();
                        cmt = snop.getValue(Comment.class);
                       Log.d("commet",cmt.getComment());
                        lvcomments.add(cmt);
                        customadapter.notifyDataSetChanged();
                    }
                }
               // Toast.makeText(Activity_comment.this,dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
