package pjm.tlcn.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import pjm.tlcn.Model.Item_GridPhoto;
import pjm.tlcn.R;

/**
 * Created by thienphu on 11/7/2017.
 */

public class CustomAdapterSearch extends BaseAdapter {
    ArrayList<Item_GridPhoto> photo_shes;
    Activity ac;
    ImageView image_search;
    VideoView videoView;
    public static String key_discover ="";
    public CustomAdapterSearch(Activity ac, ArrayList<Item_GridPhoto> photo_shes) {
        this.ac = ac;
        this.photo_shes = photo_shes;
    }

    @Override
    public int getCount() {
        return photo_shes.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = ac.getLayoutInflater();
        convertView = l.inflate(R.layout.custom_adapter_search, null);



        image_search =(ImageView)convertView.findViewById(R.id.image_search);
       videoView =(VideoView) convertView.findViewById(R.id.item_video_search);
      //  final JZVideoPlayerStandard videoView = (JZVideoPlayerStandard) convertView.findViewById(R.id.item_video_search);


        if(photo_shes.get(position).getPath().contains("jpg")||photo_shes.get(position).getPath().contains("jpeg"))
                    {
                                image_search.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.GONE);
                    Picasso.with(ac).load(photo_shes.get(position).getPath()).fit().centerCrop().into(image_search);
               }
       /* else if(photo_shes.get(position).toString().contains("mp4")){
            image_search.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            try {
                videoView.setUp(photo_shes.get(position).toString(), JZVideoPlayer.SCREEN_WINDOW_NORMAL,"");
                videoView.thumbImageView.setImageBitmap(retriveVideoSearchFromVideo(photo_shes.get(position).toString()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }*/
        else if(photo_shes.get(position).getPath().contains("mp4")){
            image_search.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            try {
                image_search.setImageBitmap(retriveVideoSearchFromVideo(photo_shes.get(position).getPath()));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
        return convertView;
    }

    public static Bitmap retriveVideoSearchFromVideo(String videoPath)
            throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}
