package pjm.tlcn.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

import pjm.tlcn.R;


public class TabActivity_news extends AppCompatActivity {

    ImageView tab_news_imageview, img_camera_tabnew;
    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private GridView gridview = null;
    private Cursor cc = null;
    private Button btnext,btcancel;
    private Boolean flag_selected=false;
    private  static final int  REQUEST_CAMERA = 12,REQUEST_DONE=13;
    public static Bitmap bitmap_photo;
    private Uri imageUri;
    private ContentValues values;
    private String imageurl;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                Log.d("TabActivity_news", "onRequestPermissionsResult: called");
                if (grantResults.length > 0
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

        tab_news_imageview = (ImageView) findViewById(R.id.tab_news_imageview);
        img_camera_tabnew = (ImageView) findViewById(R.id.img_camera_tabnew);
        gridview = (GridView) findViewById(R.id.tab_news_gridview);
        btnext =(Button) findViewById(R.id.button_selected_image);
        btcancel = (Button) findViewById(R.id.button_cancel);

        //xu ly su kien khi chon duoc anh


            btnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(flag_selected) {
                        Intent intent = new Intent(getApplicationContext(), Activity_share_image.class);
                        startActivityForResult(intent, REQUEST_DONE);
                    }

                }
            });
            btnext.setTextColor(Color.BLUE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA);
        }

        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (cc != null) {
            new Thread() {
                public void run() {
                    try {
                        cc.moveToFirst();
                        mUrls = new Uri[cc.getCount()];
                        strUrls = new String[cc.getCount()];
                        mNames = new String[cc.getCount()];
                        for (int i = 0; i < cc.getCount(); i++) {
                            cc.moveToPosition(i);
                            mUrls[i] = Uri.parse(cc.getString(1));
                            strUrls[i] = cc.getString(1);
                            mNames[i] = cc.getString(3);
                        }

                    } catch (Exception e) {
                    }

                }
            }.start();
            gridview.setAdapter(new ImageAdapter(this));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File f = new File(mUrls[position].getPath());
                    bitmap_photo = BitmapFactory.decodeFile(f.getPath());
                    tab_news_imageview.setImageURI(mUrls[position]);
                    flag_selected=true;
                    btnext.setVisibility(View.VISIBLE);
                    btcancel.setVisibility(View.VISIBLE);
                }
            });

        }

        img_camera_tabnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, REQUEST_CAMERA);

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




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
//            bitmap_photo = (Bitmap) data.getExtras().get("data");
//            tab_news_imageview.setImageBitmap(bitmap_photo);
//            flag_selected=true;
//            btcancel.setVisibility(View.VISIBLE);
//            btnext.setVisibility(View.VISIBLE);

            try {
                bitmap_photo = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), imageUri);
                tab_news_imageview.setImageBitmap(bitmap_photo);
                imageurl = getRealPathFromURI(imageUri);
                flag_selected=true;
                btcancel.setVisibility(View.VISIBLE);
                btnext.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return cc.getCount();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.galchild, null);

            try {

                ImageView imageView = (ImageView) v.findViewById(R.id.ImageView001);
                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                // imageView.setPadding(8, 8, 8, 8);
                Bitmap bmp = decodeURI(mUrls[position].getPath());
                //BitmapFactory.decodeFile(mUrls[position].getPath());
                imageView.setImageBitmap(bmp);
                //bmp.
                //TextView txtName = (TextView) v.findViewById(R.id.TextView01);
                // txtName.setText(mNames[position]);
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