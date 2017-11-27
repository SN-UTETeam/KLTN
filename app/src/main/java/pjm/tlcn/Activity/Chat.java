package pjm.tlcn.Activity;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    ImageView sendButton,img_chat_image,img_chat_chose,img_chat_camera,img_x;
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
    private Dialog dialog;
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
        img_x = (ImageView) findViewById(R.id.img_x);
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
        try {
            hiddenKeyboard();
        }
        catch (Exception e){
        }
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message= dataSnapshot.getValue(Message.class);
                //Log.d("found mess",message.getMessage());
                arrayMessage.add(message);
                messageAdapter.notifyDataSetChanged();
                lv_chat.setSelection(messageAdapter.getCount()-1);

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

        img_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_img_select=false;
                flag_img_capture=false;
                img_chat_image.setVisibility(View.GONE);
                img_x.setVisibility(View.GONE);
            }
        });

        //button send
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageArea.getText().toString();
                final Map<String, String> map = new HashMap<String, String>();

                ///Created Dialog
                dialog = new Dialog(Chat.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                if(messageText.trim().length()>0||flag_img_select||flag_img_capture){
                    if(flag_img_select){
                        dialog.show();

                        Uri file = Uri.fromFile(new File(compressImage(getPath(getApplicationContext(),uri_img_select))));
                        UploadTask uploadTask = s1Database.child(file.getLastPathSegment()).putFile(file);
                        // Listen for state changes, errors, and completion of the upload.
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                dialog.dismiss();
                                Toast.makeText(Chat.this, "Lỗi", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Handle successful uploads on complete
                                uri_img_download = taskSnapshot.getMetadata().getDownloadUrl();
                                String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                Message message = new Message();
                                message.setHyper_link("NoLink");
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
                                img_x.setVisibility(View.GONE);
                                dialog.dismiss();
                            }
                        });

                    }else
                    if(flag_img_capture){
                        dialog.show();

                        Uri file = Uri.fromFile(new File(compressImage(imageUri.toString())));
                        UploadTask uploadTask = s1Database.child(file.getLastPathSegment()).putFile(file);
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
                                message.setHyper_link("NoLink");
                                message.setMessage(messageText+"");
                                message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                message.setImage_url(uri_img_download+"");
                                message.setUser_avatar(user.getAvatarurl());
                                message.setDatecreated(timeStamp);

                                databaseRef.push().setValue(message);
                                flag_img_select=false;
                                flag_img_capture=false;
                                messageArea.setText("");
                                dialog.dismiss();
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
                        message.setHyper_link("NoLink");
                        message.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        message.setUser_avatar(user.getAvatarurl());
                        message.setImage_url("NoImage");
                        message.setDatecreated(timeStamp);

                        databaseRef.push().setValue(message);

                        flag_img_select = false;
                        flag_img_capture=false;
                        dialog.dismiss();
                        messageArea.setText("");
                        hiddenKeyboard();
                        img_chat_image.setVisibility(View.GONE);

                    }else{
                        dialog.dismiss();
                        Toast.makeText(Chat.this, "Hãy nhập tin nhắn", Toast.LENGTH_SHORT).show();}
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
                    img_x.setVisibility(View.VISIBLE);
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
                    img_x.setVisibility(View.VISIBLE);
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
    //CompressImage
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    //GetPath New
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}