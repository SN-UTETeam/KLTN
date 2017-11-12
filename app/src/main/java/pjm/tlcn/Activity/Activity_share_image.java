package pjm.tlcn.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import pjm.tlcn.Model.Photo;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.TabActivity_news.bitmap_photo;

public class Activity_share_image extends AppCompatActivity {
     private  ImageView imgshare;
    private EditText edit_status;
    TextView     send;
    ImageButton bt_back;
    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private Uri uri_img_download;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        imgshare =(ImageView)findViewById(R.id.id_img_share);
        edit_status = (EditText)findViewById(R.id.id_status);
        imgshare.setImageBitmap(bitmap_photo);
        send=(TextView)findViewById(R.id.shareid);
        bt_back =(ImageButton)findViewById(R.id.bt_image_back);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        //set onclick send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Show progressDialog
                progressDialog = new ProgressDialog(Activity_share_image.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Uploading....");
                progressDialog.setTitle("Uploading your status....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.onStart();

                //Optimze Picture
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                float aspectRatio = bitmap_photo.getWidth() /
                        (float) bitmap_photo.getHeight();
                int width = 600;
                int height = Math.round(width / aspectRatio);

                bitmap_photo = Bitmap.createScaledBitmap(
                        bitmap_photo, width, height, false);
                bitmap_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                final String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                //Upload to Storage
                UploadTask uploadTask = storageRef.child(FirebaseAuth.getInstance().getUid()).child("photos").child("IMG_"+timeStamp).putBytes(data);
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
                        Toast.makeText(Activity_share_image.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                        String newPhotoKey = databaseRef.child("photos").push().getKey();

                        //Set value
                        Photo photo = new Photo();
                        photo.setCaption(edit_status.getText().toString()+"");
                        photo.setDate_created(timeStamp+"");
                        photo.setImage_path(uri_img_download+"");
                        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        photo.setPhoto_id(newPhotoKey);

                        //insert into database
                        databaseRef.child("photos").child(newPhotoKey).setValue(photo);
                        progressDialog.dismiss();
                        Intent it = new Intent();
                        setResult(Activity.RESULT_OK, it);
                        finish();
                        Toast.makeText(getApplication(),"Đăng tải thành công!!!",Toast.LENGTH_SHORT);
                    }
                });


            }

        });





    }

}
