package pjm.tlcn.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import pjm.tlcn.R;


public class TabActivity_news extends AppCompatActivity {

    ImageView tab_news_imageview, img_camera_tabnew;
    private JZVideoPlayerStandard tab_news_videoview;
    private Integer checked[];
    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private GridView gridview = null;
    private Cursor cc = null;
    private Button btnext,btcancel;
    private Boolean flag_selected=false;
    private  static final int  REQUEST_CAMERA = 12,REQUEST_DONE=13;
    public static Bitmap bitmap_photo;
    public static ArrayList<Uri> imageUri = new ArrayList<Uri>();
    private ContentValues values;
    private String imageurl;
    String[] projection;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                Log.d("TabActivity_news", "onRequestPermissionsResult: called");
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_news);

        MainActivity mainActivity = (MainActivity) getParent();
        mainActivity.CheckPermission();

        tab_news_imageview = (ImageView) findViewById(R.id.tab_news_imageview);
        tab_news_videoview = (JZVideoPlayerStandard) findViewById(R.id.tab_news_videoview);
        img_camera_tabnew = (ImageView) findViewById(R.id.img_camera_tabnew);
        gridview = (GridView) findViewById(R.id.tab_news_gridview);
        btnext =(Button) findViewById(R.id.button_selected_image);
        btcancel = (Button) findViewById(R.id.button_cancel);

        //xu ly su kien khi chon duoc anh

        btnext.setTextColor(Color.BLUE);
        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag_selected) {
                    for(int i =0;i<imageUri.size();i++)
                        Log.d("Uri",imageUri.get(i).toString()+"");
                    Intent intent = new Intent(getApplicationContext(), Activity_share_image.class);
                    startActivityForResult(intent, REQUEST_DONE);
                }
                else {
                    Toast.makeText(TabActivity_news.this, "You must choose a picture!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btcancel.setTextColor(Color.BLUE);
        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity = (MainActivity) getParent();
                mainActivity.setCurrentTab(0);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA);
        }

//        cc = this.getContentResolver().query(
//                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
//                null);

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        CursorLoader cursorLoader = new CursorLoader(
                this,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        );
        cc = cursorLoader.loadInBackground();

        int i=0;
        mUrls = new Uri[cc.getCount()];
        while (cc.moveToNext()){
            Uri uri = Uri.parse(cc.getString(1));
            mUrls[i]=uri;
            i++;
        }
        checked=new Integer[mUrls.length];
        Arrays.fill(checked,0);

        ImageAdapter imageAdapter = new ImageAdapter(this,mUrls,checked);
        gridview.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

        img_camera_tabnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                Uri ui = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                imageUri.add(ui);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                Uri imgUri = null;
                bitmap_photo = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imgUri);
                tab_news_imageview.setImageBitmap(bitmap_photo);
                imageurl = getRealPathFromURI(imgUri);
                flag_selected=true;
                btcancel.setVisibility(View.VISIBLE);
                btnext.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode==REQUEST_DONE){
            if(resultCode==Activity.RESULT_OK) {
                MainActivity mainActivity = (MainActivity) getParent();
                mainActivity.setCurrentTab(0);
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        int column_index;

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getString(column_index);
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private Uri[] uris;
        private Integer checked[];

        public ImageAdapter(Context c,Uri[] uris,Integer[] checked) {
            this.uris = uris;
            mContext = c;
            this.checked=checked;
        }

        public int getCount() {
            return cc.getCount();
        }

        public Object getItem(int position) {
            return uris[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.galchild, null);

            try {
                ImageView imageView = (ImageView) v.findViewById(R.id.ImageView001);
                imageView.setPadding(2,2,2,2);
                final CheckBox checkBox  = (CheckBox) v.findViewById(R.id.chkImage);
                //BitmapFactory.decodeFile(mUrls[position].getPath());
                if(uris[position].getPath().contains(".jpg")||uris[position].getPath().contains(".jpeg")){
                    Bitmap bmp = decodeURI(uris[position].getPath());
                    imageView.setImageBitmap(bmp);
                    if(checked[position]==0)
                        checkBox.setChecked(false);
                    else
                        checkBox.setChecked(true);
                }
                else if(uris[position].getPath().contains(".mp4")){
                    Bitmap bm = ThumbnailUtils.createVideoThumbnail(uris[position].getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    imageView.setImageBitmap(bm);
                }

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File f = new File(uris[position].getPath());
                        if(!imageUri.contains(uris[position])) {
                            if(uris[position].getPath().contains(".jpg")||uris[position].getPath().contains(".jpeg")) {
                                bitmap_photo = BitmapFactory.decodeFile(f.getPath());
                                tab_news_imageview.setImageURI(mUrls[position]);
                                tab_news_imageview.setVisibility(View.VISIBLE);
                                tab_news_videoview.setVisibility(View.GONE);
//                                checked[position]=1;
//                                if(checked[position]==0)
//                                    checkBox.setChecked(false);
//                                else
//                                    checkBox.setChecked(true);
                                checkBox.setChecked(true);
                                notifyDataSetChanged();
                                //Log.d("Checkbox +" + checkBox.isChecked()," postion = "+position);
                            }else if(uris[position].getPath().contains(".mp4")){
                                checkBox.setChecked(true);
//                                checked[position]=1;
//                                if(checked[position]==0){
//                                    checkBox.setChecked(false);
//                                    Log.d("Set check false","Ok");
//                                }
//                                else {
//                                    checkBox.setChecked(true);
//                                    Log.d("Set check false","Ok");
//                                }
                                tab_news_videoview.setUp(f.getPath(), JZVideoPlayer.SCREEN_WINDOW_NORMAL,"");
                                try {
                                    Bitmap bm = ThumbnailUtils.createVideoThumbnail(f.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                                    tab_news_videoview.thumbImageView.setImageBitmap(bm);
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                                tab_news_imageview.setVisibility(View.GONE);
                                tab_news_videoview.setVisibility(View.VISIBLE);
                                notifyDataSetChanged();
                                //Log.d("Checkbox +" + checkBox.isChecked()," postion = "+position);
                            }
                            //imageUri.add(uris[position]);
                            flag_selected = true;
                            btnext.setVisibility(View.VISIBLE);
                            btcancel.setVisibility(View.VISIBLE);
                            //checkBox.setChecked(true);
//                            checked[position]=1;
//                            if(checked[position]==0)
//                                checkBox.setChecked(false);
//                            else
//                                checkBox.setChecked(true);
                            notifyDataSetChanged();
                            //Log.d("Checkbox +" + checkBox.isChecked()," postion = "+position);
                        }
                        else {
                            try {
                                //imageUri.remove(uris[position]);
                                checkBox.setChecked(false);
//                                checked[position]=0;
//                                if(checked[position]==0)
//                                    checkBox.setChecked(false);
//                                else
//                                    checkBox.setChecked(true);
                                notifyDataSetChanged();
                                //Log.d("Checkbox +" + checkBox.isChecked()," postion = "+position);
                            }
                            catch (Exception e){
                                Log.d("Exception",e+"");
                            }

                        }

                    }
                });
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!checkBox.isChecked()){
                            checkBox.setChecked(false);
                            checked[position] = 0;
                            imageUri.remove(uris[position]);
                        }
                        else {
                            checkBox.setChecked(true);
                            checked[position] = 1;
                            imageUri.remove(uris[position]);
                            imageUri.add(uris[position]);
                        }
                    }
                });
            } catch (Exception e) {

            }
            return v;
        }
    }


    /**
     * This method is to scale down the image
     */
    public Bitmap decodeURI(String filePath) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if (options.outHeight * options.outWidth * 2 >= 16384) {
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int) Math.pow(2d, Math.floor(
                            Math.log(sampleSize) / Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }
}