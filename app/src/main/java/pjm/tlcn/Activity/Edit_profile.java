package pjm.tlcn.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;

public class Edit_profile extends AppCompatActivity {
private Button btn_edit_profile_cancel;
private DatabaseReference uDatabase;
private TextView tv_change_avatar;
private ImageView img_avatar_editprofile;
private EditText edt_username_editprofile,edt_describer_editprofile,edt_phonenumber_editprofile,edt_email_editprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Create Variable
        btn_edit_profile_cancel = (Button) findViewById(R.id.btn_edit_profile_cancel);
        tv_change_avatar = (TextView) findViewById(R.id.tv_change_avatar);
        img_avatar_editprofile = (ImageView) findViewById(R.id.img_avatar_editprofile);
        edt_username_editprofile = (EditText) findViewById(R.id.edt_username_editprofile);
        edt_describer_editprofile = (EditText) findViewById(R.id.edt_describer_editprofile);
        edt_phonenumber_editprofile = (EditText) findViewById(R.id.edt_phonenumber_editprofile);
        edt_email_editprofile = (EditText) findViewById(R.id.edt_email_editprofile);


        //Start Set Onclick btn_edit_profile_cancel
        btn_edit_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //End Set Onclick btn_edit_profile_cancel

        //Load user profile
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(getApplicationContext()).load(user.getAvatarurl()).fit().centerInside().into(img_avatar_editprofile);

                edt_username_editprofile.setText(user.getUsername());
                edt_describer_editprofile.setText(user.getDescriber());
                edt_email_editprofile.setText(user.getEmail());
                edt_phonenumber_editprofile.setText(user.getPhonenumber());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
