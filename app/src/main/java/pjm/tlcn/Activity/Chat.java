package pjm.tlcn.Activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pjm.tlcn.Adapter.MessageAdapter;
import pjm.tlcn.Model.Message;
import pjm.tlcn.R;

import static pjm.tlcn.Activity.Login.firebaseUser;
import static pjm.tlcn.Activity.Login.user;

public class Chat extends AppCompatActivity {

    ImageView sendButton,img_chat_image,img_chat_chose,img_chat_camera;
    EditText messageArea;
    DatabaseReference databaseRef;
    private StorageReference s1Database;
    Toolbar toolbar_chat;
    TextView tv_toolbar_chat;
    ListView lv_chat;
    private int RESULT_LOAD_IMG = 1000;
    private static final int REQUEST_CAMERA = 12;
    private Uri uri_img_select;
    public Uri uri_img_download;
    public Bitmap bitmap_img_capture;
    private boolean flag_img_select=false,flag_img_capture=false;
    private ProgressDialog progressDialog;
    private ArrayList<Message> arrayMessage = new ArrayList<Message>();
    private MessageAdapter messageAdapter;
    private Uri imageUri;
    private ContentValues values;
    private String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lv_chat = (ListView) findViewById(R.id.lv_chat);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        img_chat_image = (ImageView) findViewById(R.id.img_chat_image);
        img_chat_camera = (ImageView) findViewById(R.id.img_chat_camera);
        img_chat_chose = (ImageView) findViewById(R.id.img_chat_chose);
        messageArea = (EditText)findViewById(R.id.messageArea);
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
        messageAdapter = new MessageAdapter(this,arrayMessage);
        lv_chat.setAdapter(messageAdapter);
       //

        //Get Intent
        String room_id = getIntent().getExtras().getString("room_id");
        String user_id = getIntent().getExtras().getString("user_id");
        String username = getIntent().getExtras().getString("username");
        final String user_avatar = getIntent().getExtras().getString("user_avatar");

        tv_toolbar_chat.setText(username);
        Firebase.setAndroidContext(this);
        databaseRef = FirebaseDatabase.getInstance().getReference().child("RoomChat").child(room_id);
        s1Database = FirebaseStorage.getInstance().getReference().child("ImageChat").child("/" + firebaseUser.getUid() + "_" + user_id);
        arrayMessage.clear();
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message= dataSnapshot.getValue(Message.class);
                //Log.d("found mess",message.getMessage());
                arrayMessage.add(message);
                messageAdapter.notifyDataSetChanged();
                lv_chat.setSelection(arrayMessage.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
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

                if(messageText.trim().length()>0||flag_img_select||flag_img_capture){
                    if(flag_img_select){
                        progressDialog.show();
                        progressDialog.onStart();// /Upload when chosen img form galery
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        float aspectRatio = bitmap_img_capture.getWidth() /
                                (float) bitmap_img_capture.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);
                        bitmap_img_capture = Bitmap.createScaledBitmap(
                                bitmap_img_capture, width, height, false);
                        bitmap_img_capture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                Message message = new Message();
                                message.setMessage(messageText+"");
                                message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                message.setImage_url(uri_img_download+"");
                                message.setUser_avatar(user.getAvatarurl());
                                message.setDatecreated(timeStamp);

                                databaseRef.push().setValue(message);
                                flag_img_capture=false;
                                flag_img_select=false;
                                messageArea.setText("");
                                hiddenKeyboard();
                                img_chat_image.setVisibility(View.GONE);
                                progressDialog.dismiss();
                            }
                        });

                    }else
                    if(flag_img_capture){
                        progressDialog.show();
                        progressDialog.onStart();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        float aspectRatio = bitmap_img_capture.getWidth() /
                                (float) bitmap_img_capture.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);
                        bitmap_img_capture = Bitmap.createScaledBitmap(
                                bitmap_img_capture, width, height, false);
                        bitmap_img_capture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                Message message = new Message();
                                message.setMessage(messageText+"");
                                message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                message.setImage_url(uri_img_download+"");
                                message.setUser_avatar(user.getAvatarurl());
                                message.setDatecreated(timeStamp);

                                databaseRef.push().setValue(message);
                                flag_img_select=false;
                                flag_img_capture=false;
                                messageArea.setText("");
                                progressDialog.dismiss();
                                hiddenKeyboard();
                                img_chat_image.setVisibility(View.GONE);
                            }
                        });
                    }
                    else
                        if(messageText.trim().length()>0){

                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        Message message = new Message();
                        message.setMessage(messageText+"");
                        message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        message.setUser_avatar(user.getAvatarurl());
                        message.setImage_url("NoImage");
                        message.setDatecreated(timeStamp);

                        databaseRef.push().setValue(message);

                        flag_img_select = false;
                        flag_img_capture=false;
                        progressDialog.dismiss();
                        messageArea.setText("");
                        hiddenKeyboard();
                        img_chat_image.setVisibility(View.GONE);

                        }else
                            Toast.makeText(Chat.this, "Hãy nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }

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
                Toast.makeText(getApplicationContext(), "You haven't picked Photo",Toast.LENGTH_LONG).show();
            }
        if(reqCode==REQUEST_CAMERA)
            if(resultCode==RESULT_OK){
                try {
                    bitmap_img_capture = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    img_chat_image.setImageBitmap(bitmap_img_capture);
                    imageurl = getRealPathFromURI(imageUri);

                    //bitmap_img_capture = (Bitmap) data.getExtras().get("data");
                    //img_chat_image.setImageBitmap(bitmap_img_capture);
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
                Toast.makeText(getApplicationContext(), "You haven't Capture Photo",Toast.LENGTH_LONG).show();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
