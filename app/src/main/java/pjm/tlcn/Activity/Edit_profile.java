package pjm.tlcn.Activity;

import android.app.ProgressDialog;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
private ProgressDialog progressDialog;
private Bitmap selectedImage;

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

        //Show progressDialog
        progressDialog = new ProgressDialog(Edit_profile.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Chỉnh sửa thông tin....");
        progressDialog.setTitle("Đang thực hiện....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.onStart();
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
                        edt_describer_editprofile.getText().toString().length()>0)
                {
                    progressDialog.show();
                    uDatabase.child("username").setValue(edt_username_editprofile.getText().toString());
                    uDatabase.child("describer").setValue(edt_describer_editprofile.getText().toString());
                    uDatabase.child("phonenumber").setValue(edt_phonenumber_editprofile.getText().toString());
                    if(flag_img_select){
                        //optimze picture
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //File f = new File(selectedImage.getPath());
                        //Bitmap bitmap = BitmapFactory.decodeFile(f.getPath());
                        float aspectRatio = selectedImage.getWidth() /
                                (float) selectedImage.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);
                        selectedImage = Bitmap.createScaledBitmap(
                                selectedImage, width, height, false);
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        UploadTask uploadTask = sDatabase.child("IMG_"+timeStamp).putBytes(data);
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                final double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                int currentprogress = (int) progress;
                                progressDialog.setProgress(currentprogress);
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                progressDialog.dismiss();
                                Toast.makeText(Edit_profile.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Date currentTime = Calendar.getInstance().getTime();
                                uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                                uDatabase.child("avatarurl").setValue(uri_img_download.toString());
                                progressDialog.dismiss();
                                Toast.makeText(getApplication(),"Chỉnh sửa thành công thành công!!!",Toast.LENGTH_SHORT);
                                finish();
                            }
                        });
                        flag_img_select=false;
                    }
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
                if(!user.getEmail().equals("null")) edt_email_editprofile.setText(user.getEmail());
                else edt_email_editprofile.setText("");
                if(!user.getPhonenumber().equals("null")) edt_phonenumber_editprofile.setText(user.getPhonenumber());
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
                selectedImage = BitmapFactory.decodeStream(imageStream);
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
