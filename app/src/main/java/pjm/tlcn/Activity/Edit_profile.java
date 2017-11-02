package pjm.tlcn.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

import pjm.tlcn.Model.User;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;

public class Edit_profile extends AppCompatActivity {
private Button btn_edit_profile_cancel,btn_done_editprofile;
private DatabaseReference uDatabase;
private StorageReference sDatabase;
private TextView tv_change_avatar;
private ImageView img_avatar_editprofile;
private EditText edt_username_editprofile,edt_describer_editprofile,edt_phonenumber_editprofile,edt_email_editprofile;
private int RESULT_LOAD_IMG = 1000;
private Uri uri_img_select,uri_img_download;
private boolean flag_img_select=false;

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
        btn_done_editprofile = (Button) findViewById(R.id.btn_done_editprofile);

        //Firebase
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        sDatabase = FirebaseStorage.getInstance().getReference().child("AvatarUsers").child(user_id);

        //Check null
        if(edt_email_editprofile.getText().toString().equals("null")) edt_email_editprofile.setEnabled(true);
        //Start Set Onclick btn_edit_profile_cancel
        btn_edit_profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //End Set Onclick btn_edit_profile_cancel

        //Set click btn_done_editprofile
        btn_done_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(     edt_describer_editprofile.getText().toString().length()>=3 &&
                        edt_phonenumber_editprofile.getText().toString().length()>=10 &&
                        edt_describer_editprofile.getText().toString().length()>0){
                    uDatabase.child("username").setValue(edt_username_editprofile.getText().toString());
                    uDatabase.child("describer").setValue(edt_describer_editprofile.getText().toString());
                    uDatabase.child("phonenumber").setValue(edt_phonenumber_editprofile.getText().toString());
                    if(flag_img_select){
                        //Upload file to firebase storage
                        //Uri file = Uri.fromFile(new File(uri_img_select));
                        StorageReference img_upload = sDatabase.child(uri_img_select.getLastPathSegment());
//                        StorageMetadata metadata = new StorageMetadata.Builder()
//                                .setContentType("image/jpeg")
//                                .build();
                        UploadTask uploadTask = img_upload.putFile(uri_img_select);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                                uDatabase.child("avatarurl").setValue(uri_img_download.toString());
                            }
                        });
                        flag_img_select=false;
                    }
                    Toast.makeText(getApplicationContext(),"Chỉnh sửa thông tin thành công!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập đúng các thông tin!",Toast.LENGTH_LONG).show();
            }
        });

        //Change Avatar
        tv_change_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        //Load user profile
        uDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(getApplicationContext()).load(user.getAvatarurl()).fit().centerInside().into(img_avatar_editprofile);
                edt_username_editprofile.setText(user.getUsername());
                edt_describer_editprofile.setText(user.getDescriber());
                edt_email_editprofile.setText(user.getEmail());
                if(!user.getPhonenumber().equals(null)) edt_phonenumber_editprofile.setText(user.getPhonenumber());
                else edt_phonenumber_editprofile.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode==RESULT_LOAD_IMG)
        if (resultCode == RESULT_OK) {
            try {
                uri_img_select = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(uri_img_select);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                img_avatar_editprofile.setImageBitmap(selectedImage);
                flag_img_select=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
