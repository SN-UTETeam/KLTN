package pjm.tlcn.Activity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pjm.tlcn.Model.Image;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.user_id;
import static pjm.tlcn.Activity.TabActivity_news.bitmap_photo;

public class Activity_share_image extends AppCompatActivity {
     private  ImageView imgshare;
    private EditText edit_status;
    TextView     send;
    ImageButton bt_back;
    private DatabaseReference uDatabase;
    private StorageReference sDatabase;
    private boolean flag_img_select=false;
    private Uri uri_img_select,uri_img_download;
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
        uDatabase = FirebaseDatabase.getInstance().getReference().child("Images").child(user_id);
        sDatabase = FirebaseStorage.getInstance().getReference().child("ShareImages").child(user_id);

        //set onclick send
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create a storage reference from our app
              //  StorageReference storageRef = uDatabase.getReference();
                // Get the data from an ImageView as bytes
                Calendar time = Calendar.getInstance();
                // Create a reference to "mountains.jpg"
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                UploadTask uploadTask = sDatabase.child("IMG_"+timeStamp).putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(Activity_share_image.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Date currentTime = Calendar.getInstance().getTime();
                       // uDatabase.child("datetime").setValue(currentTime.toString());
                        uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                     //  uDatabase.child("imageurl").setValue(uri_img_download.toString());
                       String temp = uDatabase.push().getKey();
                        Image img = new Image(temp,0,currentTime.toString(),uri_img_download+"",0,edit_status.getText().toString()+"");
                        uDatabase.push().setValue(img);
                    }
                });
                imgshare.setImageBitmap(null);
                edit_status.setText("");
                finish();
                Toast.makeText(Activity_share_image.this, "Thanh cong", Toast.LENGTH_SHORT).show();
            }

        });





    }

}
