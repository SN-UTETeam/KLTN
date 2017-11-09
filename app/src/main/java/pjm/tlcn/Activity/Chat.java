package pjm.tlcn.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.firebaseUser;
import static pjm.tlcn.Activity.TabActivity_message.useridchatwith;
import static pjm.tlcn.Activity.TabActivity_message.usernamechatwith;

public class Chat extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton,img_chat_image,img_chat_chose,img_chat_camera;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2;
    private StorageReference s1Database,s2Database;
    Toolbar toolbar_chat;
    TextView tv_toolbar_chat;
    private int RESULT_LOAD_IMG = 1000;
    private static final int REQUEST_CAMERA = 12;
    private Uri uri_img_select;
    public Uri uri_img_download;
    public Bitmap bitmap_img_capture;
    private boolean flag_img_select=false,flag_img_capture=false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        img_chat_image = (ImageView) findViewById(R.id.img_chat_image);
        img_chat_camera = (ImageView) findViewById(R.id.img_chat_camera);
        img_chat_chose = (ImageView) findViewById(R.id.img_chat_chose);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        toolbar_chat = (Toolbar) findViewById(R.id.toolbar_chat);
        tv_toolbar_chat = (TextView) findViewById(R.id.tv_toolbar_chat);

        //Set Back ToolBar
        setSupportActionBar(toolbar_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_chat.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //
        scrollView.fullScroll(View.FOCUS_DOWN);
        tv_toolbar_chat.setText(usernamechatwith);
        //
        Firebase.setAndroidContext(this);
        reference1 = FirebaseDatabase.getInstance().getReference().child("Messages").child("/" + firebaseUser.getUid() + "_" + useridchatwith);
        reference2 = FirebaseDatabase.getInstance().getReference().child("Messages").child("/" + useridchatwith + "_" + firebaseUser.getUid());
        s1Database = FirebaseStorage.getInstance().getReference().child("ImageChat").child("/" + firebaseUser.getUid() + "_" + useridchatwith);

        //Open Galery
        img_chat_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

        //Capture Picture
        img_chat_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        });


        //button send
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String messageText = messageArea.getText().toString();
               final Map<String, String> map = new HashMap<String, String>();
                //Show progressDialog
                progressDialog = new ProgressDialog(Chat.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Sending....");
                progressDialog.setTitle("Sending your message....");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                progressDialog.onStart();
                if(!messageText.equals("")||flag_img_select||flag_img_capture){
                    if(flag_img_select){ //Upload when chosen img form galery
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        float aspectRatio = bitmap_img_capture.getWidth() /
                                (float) bitmap_img_capture.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);
                        bitmap_img_capture = Bitmap.createScaledBitmap(
                                bitmap_img_capture, width, height, false);
                        bitmap_img_capture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        UploadTask uploadTask = s1Database.child("IMG_"+timeStamp).putBytes(data);
                        // Listen for state changes, errors, and completion of the upload.
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
                                progressDialog.dismiss();
                                Toast.makeText(Chat.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle successful uploads on complete
                                uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();

                                map.put("message", messageText+"");
                                map.put("userid", firebaseUser.getUid());
                                map.put("imageurl",uri_img_download+"");
                                reference1.push().setValue(map);
                                reference2.push().setValue(map);
                                flag_img_select=false;
                                messageArea.setText("");
                                img_chat_image.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }
                        });

                    }
                    if(flag_img_capture){

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        float aspectRatio = bitmap_img_capture.getWidth() /
                                (float) bitmap_img_capture.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);
                        bitmap_img_capture = Bitmap.createScaledBitmap(
                                bitmap_img_capture, width, height, false);
                        bitmap_img_capture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        UploadTask uploadTask = s1Database.child("IMG_"+timeStamp).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                                map.put("message", messageText+"");
                                map.put("userid", firebaseUser.getUid());
                                map.put("imageurl",uri_img_download+"");
                                reference1.push().setValue(map);
                                reference2.push().setValue(map);
                                flag_img_capture=false;
                                messageArea.setText("");
                                img_chat_image.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {

                        map.put("message", messageText + "");
                        map.put("userid", firebaseUser.getUid());
                        map.put("imageurl", "NoImage");

                        reference1.push().setValue(map);
                        reference2.push().setValue(map);

                        flag_img_select = false;
                        messageArea.setText("");
                        img_chat_image.setVisibility(View.GONE);
                    }
                }
            }
        });

        reference1.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userId = map.get("userid").toString();
                String imageurl = map.get("imageurl").toString();

                if(userId.equals(firebaseUser.getUid())){
                    addMessageBox(message, 1,imageurl);
                }
                else{
                    addMessageBox(message, 2,imageurl);
                }

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type, String image){

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT,1.0f);
        lp2.setMargins(20,10,20,10);
        TextView textView = new TextView(Chat.this);
        textView.setTextSize(20);
        textView.setPadding(20,10,20,10);
        textView.setText(message);
        textView.setLayoutParams(lp2);


        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT,1.0f);
        ImageView imageView = new ImageView(Chat.this);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(lp1);
        imageView.getLayoutParams().height = 800;
        imageView.getLayoutParams().width = 800;

        imageView.setPadding(20, 10, 20, 10);
        imageView.setVisibility(View.GONE);


        if(!image.equals("NoImage")) {

            Picasso.with(getApplicationContext()).load(image).fit().centerInside().into(imageView);
            imageView.setVisibility(View.VISIBLE);
        }
        else
            imageView.setVisibility(View.GONE);
        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            lp1.gravity = Gravity.RIGHT;
            lp2.setMargins(200,10,20,10);
            lp1.setMargins(200,10,20,10);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.sendmessage);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            lp1.gravity = Gravity.LEFT;
            lp2.setMargins(20,10,200,10);
            lp1.setMargins(20,10,200,10);
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundResource(R.drawable.receivedmessage);
        }
        if(!image.equals("NoImage")){
            imageView.setVisibility(View.VISIBLE);
            layout.addView(imageView);}
            else imageView.setVisibility(View.GONE);
        if(!message.equals("null")&&!message.equals(""))layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if(reqCode==RESULT_LOAD_IMG)
            if (resultCode == RESULT_OK) {
                try {
                    uri_img_select = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(uri_img_select);
                    bitmap_img_capture = BitmapFactory.decodeStream(imageStream);
                    img_chat_image.setImageBitmap(bitmap_img_capture);
                    img_chat_image.setVisibility(View.VISIBLE);
                    flag_img_select=true;
                    flag_img_capture=false;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        if(reqCode==REQUEST_CAMERA)
            if(resultCode==RESULT_OK){
                try {
                    bitmap_img_capture = (Bitmap) data.getExtras().get("data");
                    img_chat_image.setImageBitmap(bitmap_img_capture);
                    img_chat_image.setVisibility(View.VISIBLE);
                    flag_img_capture = true;
                    flag_img_select=false;
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            else
                Toast.makeText(getApplicationContext(), "You haven't Capture Image",Toast.LENGTH_LONG).show();
    }
}
